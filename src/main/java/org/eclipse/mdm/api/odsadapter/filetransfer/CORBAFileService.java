/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter.filetransfer;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.asam.ods.ElemId;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.file.FileService;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.odsadapter.ODSContext;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CORBA file service implementation of the {@link FileService} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class CORBAFileService implements FileService {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Logger LOGGER = LoggerFactory.getLogger(CORBAFileService.class);

	private static final int THREAD_POOL_SIZE = 5;

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final CORBAFileServer fileServer;
	private final ModelManager modelManager;
	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param context
	 *            Used for {@link Entity} to {@link ElemId} conversion.
	 * @param transfer
	 *            The transfer type for up- and downloads.
	 */
	public CORBAFileService(ODSContext context, Transfer transfer) {
		this.modelManager = context.getModelManager().orElseThrow(() -> new ServiceNotProvidedException(ModelManager.class));
		
		fileServer = new CORBAFileServer(context, transfer);
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void downloadSequential(Entity entity, Path target, Collection<FileLink> fileLinks,
			ProgressListener progressListener) throws IOException {
		Map<String, List<FileLink>> groups = fileLinks.stream().filter(FileLink::isRemote)
				.collect(Collectors.groupingBy(FileLink::getRemotePath));

		long totalSize = calculateDownloadSize(entity, groups);
		final AtomicLong transferred = new AtomicLong();
		LocalTime start = LocalTime.now();
		UUID id = UUID.randomUUID();
		LOGGER.debug("Sequential download of {} file(s) with id '{}' started.", groups.size(), id);
		for (List<FileLink> group : groups.values()) {
			FileLink fileLink = group.get(0);

			download(entity, target, fileLink, (b, p) -> {
				double tranferredBytes = transferred.addAndGet(b);
				if (progressListener != null) {
					progressListener.progress(b, (float) (tranferredBytes / totalSize));
				}
			});

			group.subList(1, group.size()).forEach(other -> other.setLocalPath(fileLink.getLocalPath()));
		}
		LOGGER.debug("Sequential download with id '{}' finished in {}.", id, Duration.between(start, LocalTime.now()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void downloadParallel(Entity entity, Path target, Collection<FileLink> fileLinks,
			ProgressListener progressListener) throws IOException {
		Map<String, List<FileLink>> groups = fileLinks.stream().filter(FileLink::isRemote)
				.collect(Collectors.groupingBy(FileLink::getRemotePath));

		long totalSize = calculateDownloadSize(entity, groups);
		final AtomicLong transferred = new AtomicLong();
		List<Callable<Void>> downloadTasks = new ArrayList<>();
		for (List<FileLink> group : groups.values()) {
			downloadTasks.add(() -> {
				FileLink fileLink = group.get(0);

				download(entity, target, fileLink, (b, p) -> {
					double tranferredBytes = transferred.addAndGet(b);
					if (progressListener != null) {
						progressListener.progress(b, (float) (tranferredBytes / totalSize));
					}
				});

				group.subList(1, group.size()).forEach(other -> other.setLocalPath(fileLink.getLocalPath()));

				return null;
			});
		}

		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		LocalTime start = LocalTime.now();
		UUID id = UUID.randomUUID();
		LOGGER.debug("Parallel download of {} file(s) with id '{}' started.", groups.size(), id);
		try {
			List<Throwable> errors = executorService.invokeAll(downloadTasks).stream().map(future -> {
				try {
					future.get();
					return null;
				} catch (ExecutionException | InterruptedException e) {
					LOGGER.error("Download of failed due to: " + e.getMessage(), e);
					return e;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

			if (!errors.isEmpty()) {
				throw new IOException(new StringBuilder().append("Download faild for '").append(errors.size()).append("' files.").toString());
			}
			LOGGER.debug("Parallel download with id '{}' finished in {}.", id,
					Duration.between(start, LocalTime.now()));
		} catch (InterruptedException e) {
			throw new IOException("Unable to download files due to: " + e.getMessage(), e);
		} finally {
			executorService.shutdown();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void download(Entity entity, Path target, FileLink fileLink, ProgressListener progressListener)
			throws IOException {
		if (Files.exists(target)) {
			if (!Files.isDirectory(target)) {
				throw new IllegalArgumentException("Target path is not a directory.");
			}
		} else {
			Files.createDirectory(target);
		}

		try (InputStream inputStream = openStream(entity, fileLink, progressListener)) {
			fileLink.setLocalPath(target.resolve(fileLink.getFileName()));
			Path absolutePath = fileLink.getLocalPath().toAbsolutePath();
			String remotePath = fileLink.getRemotePath();
			LOGGER.debug("Starting download of file '{}' to '{}'.", remotePath, absolutePath);
			LocalTime start = LocalTime.now();
			Files.copy(inputStream, fileLink.getLocalPath());
			LOGGER.debug("File '{}' successfully downloaded in {} to '{}'.", remotePath,
					Duration.between(start, LocalTime.now()), absolutePath);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openStream(Entity entity, FileLink fileLink, ProgressListener progressListener)
			throws IOException {
		InputStream sourceStream;
		if (fileLink.isLocal()) {
			// file is locally available -> USE this shortcut!
			sourceStream = Files.newInputStream(fileLink.getLocalPath());
		} else if (fileLink.isRemote()) {
			sourceStream = fileServer.openStream(fileLink, toElemID(entity));
		} else {
			throw new IllegalArgumentException("File link is neither in local nor remote state: " + fileLink);
		}

		// NOTE: Access to immediate input stream is buffered.
		if (progressListener == null) {
			return sourceStream;
		}
		loadSize(entity, fileLink);
		// NOTE: Progress updates immediately triggered by the stream
		// consumer.
		return new TracedInputStream(sourceStream, progressListener, fileLink.getSize());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadSize(Entity entity, FileLink fileLink) throws IOException {
		if (fileLink.getSize() > -1) {
			// file size is already known
			return;
		} else if (fileLink.isLocal()) {
			fileLink.setFileSize(Files.size(fileLink.getLocalPath()));
		} else if (fileLink.isRemote()) {
			fileLink.setFileSize(fileServer.loadSize(fileLink, toElemID(entity)));
		} else {
			throw new IllegalArgumentException("File link is neither in local nor remote state: " + fileLink);
		}
	}

	/**
	 * Sequential upload of given {@link FileLink}s. Local {@link Path}s linked
	 * multiple times are uploaded only once. The upload progress may be traced
	 * with a progress listener.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param fileLinks
	 *            Collection of {@code FileLink}s to upload.
	 * @param progressListener
	 *            The progress listener.
	 * @throws IOException
	 *             Thrown if unable to upload files.
	 */
	public void uploadSequential(Entity entity, Collection<FileLink> fileLinks, ProgressListener progressListener)
			throws IOException {
		Map<Path, List<FileLink>> groups = fileLinks.stream().filter(FileLink::isLocal)
				.collect(Collectors.groupingBy(FileLink::getLocalPath));

		long totalSize = groups.values().stream().map(l -> l.get(0)).mapToLong(FileLink::getSize).sum();
		final AtomicLong transferred = new AtomicLong();
		LocalTime start = LocalTime.now();
		UUID id = UUID.randomUUID();
		LOGGER.debug("Sequential upload of {} file(s) with id '{}' started.", groups.size(), id);
		for (List<FileLink> group : groups.values()) {
			FileLink fileLink = group.get(0);

			upload(entity, fileLink, (b, p) -> {
				double tranferredBytes = transferred.addAndGet(b);
				if (progressListener != null) {
					progressListener.progress(b, (float) (tranferredBytes / totalSize));
				}
			});

			group.subList(1, group.size()).forEach(other -> other.setRemotePath(fileLink.getRemotePath()));
		}
		LOGGER.debug("Sequential upload with id '{}' finished in {}.", id, Duration.between(start, LocalTime.now()));
	}

	/**
	 * Parallel upload of given {@link FileLink}s. Local {@link Path}s linked
	 * multiple times are uploaded only once. The upload progress may be traced
	 * with a progress listener.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param fileLinks
	 *            Collection of {@code FileLink}s to upload.
	 * @param progressListener
	 *            The progress listener.
	 * @throws IOException
	 *             Thrown if unable to upload files.
	 */
	public void uploadParallel(Entity entity, Collection<FileLink> fileLinks, ProgressListener progressListener)
			throws IOException {
		Map<Path, List<FileLink>> groups = fileLinks.stream().filter(FileLink::isLocal)
				.collect(Collectors.groupingBy(FileLink::getLocalPath));

		long totalSize = groups.values().stream().map(l -> l.get(0)).mapToLong(FileLink::getSize).sum();
		final AtomicLong transferred = new AtomicLong();
		List<Callable<Void>> downloadTasks = new ArrayList<>();
		for (List<FileLink> group : groups.values()) {
			downloadTasks.add(() -> {
				FileLink fileLink = group.get(0);

				upload(entity, fileLink, (b, p) -> {
					double tranferredBytes = transferred.addAndGet(b);
					if (progressListener != null) {
						progressListener.progress(b, (float) (tranferredBytes / totalSize));
					}
				});

				group.subList(1, group.size()).forEach(other -> other.setRemotePath(fileLink.getRemotePath()));

				return null;
			});
		}

		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		LocalTime start = LocalTime.now();
		UUID id = UUID.randomUUID();
		LOGGER.debug("Parallel upload of {} file(s) with id '{}' started.", groups.size(), id);
		try {
			List<Throwable> errors = executorService.invokeAll(downloadTasks).stream().map(future -> {
				try {
					future.get();
					return null;
				} catch (ExecutionException | InterruptedException e) {
					LOGGER.error("Upload of failed due to: " + e.getMessage(), e);
					return e;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

			if (!errors.isEmpty()) {
				throw new IOException(new StringBuilder().append("Upload faild for '").append(errors.size()).append("' files.").toString());
			}
			LOGGER.debug("Parallel upload with id '{}' finished in {}.", id, Duration.between(start, LocalTime.now()));
		} catch (InterruptedException e) {
			throw new IOException("Unable to upload files due to: " + e.getMessage(), e);
		} finally {
			executorService.shutdown();
		}
	}

	/**
	 * Deletes given {@link FileLink}s form the remote storage.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param fileLinks
	 *            Collection of {@code FileLink}s to delete.
	 */
	public void delete(Entity entity, Collection<FileLink> fileLinks) {
		fileLinks.stream().filter(FileLink::isRemote)
				.collect(groupingBy(FileLink::getRemotePath, reducing((fl1, fl2) -> fl1))).values().stream()
				.filter(Optional::isPresent).map(Optional::get).forEach(fl -> delete(entity, fl));
	}

	/**
	 * Deletes given {@link FileLink} form the remote storage.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param fileLink
	 *            The {@code FileLink}s to delete.
	 */
	public void delete(Entity entity, FileLink fileLink) {
		if (!fileLink.isRemote()) {
			// nothing to do
			return;
		}

		try {
			fileServer.delete(fileLink, toElemID(entity));
			LOGGER.debug("File '{}' sucessfully deleted.", fileLink.getRemotePath());
		} catch (IOException e) {
			LOGGER.warn("Failed to delete remote file.", e);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Uploads given {@link FileLink}. The upload progress may be traced with a
	 * progress listener.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param fileLink
	 *            The {@code FileLink} to upload.
	 * @param progressListener
	 *            The progress listener.
	 * @throws IOException
	 *             Thrown if unable to upload file.
	 */
	private void upload(Entity entity, FileLink fileLink, ProgressListener progressListener) throws IOException {
		if (fileLink.isRemote()) {
			// nothing to do
			return;
		} else if (!fileLink.isLocal()) {
			throw new IllegalArgumentException("File link does not have a local path.");
		}

		InputStream sourceStream = Files.newInputStream(fileLink.getLocalPath());
		if (progressListener != null) {
			sourceStream = new TracedInputStream(sourceStream, progressListener, fileLink.getSize());
		}

		Path absolutePath = fileLink.getLocalPath().toAbsolutePath();
		LOGGER.debug("Starting upload of file '{}'.", absolutePath);
		LocalTime start = LocalTime.now();
		fileServer.uploadStream(sourceStream, fileLink, toElemID(entity));
		LOGGER.debug("File '{}' successfully uploaded in {} to '{}'.", absolutePath,
				Duration.between(start, LocalTime.now()), fileLink.getRemotePath());
	}

	/**
	 * Creates an ODS entity identity {@link ElemId} object for given
	 * {@link Entity}.
	 *
	 * @param entity
	 *            The {@code Entity}.
	 * @return The created {@code ElemId} is returned.
	 */
	private ElemId toElemID(Entity entity) {
		return new ElemId(((ODSEntityType) modelManager.getEntityType(entity)).getODSID(),
				ODSConverter.toODSID(entity.getID()));
	}

	/**
	 * Calculates the total download size for given {@link FileLink} groups.
	 *
	 * @param entity
	 *            Used for security checks.
	 * @param groups
	 *            The {@code FileLink} groups.
	 * @return The total download size is returned.
	 * @throws IOException
	 *             Thrown if unable to load the file size.
	 */
	private long calculateDownloadSize(Entity entity, Map<String, List<FileLink>> groups) throws IOException {
		List<FileLink> links = groups.values().stream().map(l -> l.get(0)).collect(Collectors.toList());
		long totalSize = 0;
		for (FileLink fileLink : links) {
			loadSize(entity, fileLink);
			// overflow may occur in case of total size exceeds 9223 PB!
			totalSize = Math.addExact(totalSize, fileLink.getSize());
		}

		return totalSize;
	}

}
