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


package org.eclipse.mdm.api.odsadapter.transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.file.FileService.ProgressListener;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.odsadapter.ODSContext;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService;
import org.eclipse.mdm.api.odsadapter.filetransfer.Transfer;

/**
 * Manages new or removed externally linked files.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class UploadService {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final Map<TemplateAttribute, Value> templateAttributeFileLinks = new HashMap<>();
	private final List<FileLink> uploaded = new ArrayList<>();
	private final Map<Path, String> remotePaths = new HashMap<>();
	private final List<FileLink> toRemove = new ArrayList<>();

	private final CORBAFileService fileService;
	private final Entity entity;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param context
	 *            Used for setup.
	 * @param entity
	 *            Used for security checks.
	 * @param transfer
	 *            The transfer type.
	 */
	UploadService(ODSContext context, Entity entity, Transfer transfer) {
		fileService = new CORBAFileService(context, transfer);
		this.entity = entity;

		scheduler.scheduleAtFixedRate(() -> {
			try {
				context.getAoSession().getName();
			} catch (AoException e) {
				/*
				 * NOTE: This is done to keep the parent transaction's session
				 * alive till its commit or abort method is called. If this
				 * session refresh results in an error, then any running file
				 * transfer will abort with a proper error, therefore any
				 * exception here is completely ignored and explicitly NOT
				 * logged!
				 */
			}
		}, 5, 5, TimeUnit.MINUTES);
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Uploads new externally linked files stored in given
	 * {@link TemplateAttribute}s. The upload progress may be traced with a
	 * progress listener.
	 *
	 * @param templateAttributes
	 *            The {@link TemplateAttribute}s.
	 * @param progressListener
	 *            The progress listener.
	 * @throws IOException
	 *             Thrown if unable to upload files.
	 */
	public void upload(Collection<TemplateAttribute> templateAttributes, ProgressListener progressListener)
			throws IOException {
		List<FileLink> fileLinks = new ArrayList<>();
		for (TemplateAttribute templateAttribute : templateAttributes) {
			Value defaultValue = templateAttribute.getDefaultValue();
			if (!defaultValue.isValid()) {
				continue;
			}

			if (defaultValue.getValueType().isFileLink()) {
				fileLinks.add(defaultValue.extract());
			} else if (defaultValue.getValueType().isFileLinkSequence()) {
				fileLinks.addAll(Arrays.asList((FileLink[]) defaultValue.extract()));
			} else {
				throw new IllegalStateException("Template attribute's value type is not of type file link.");
			}

			templateAttributeFileLinks.put(templateAttribute, defaultValue);
		}

		if (!fileLinks.isEmpty()) {
			uploadParallel(fileLinks, progressListener);
			// remote paths available -> update template attribute
			templateAttributeFileLinks.forEach((ta, v) -> ta.setDefaultValue(v.extract()));
		}
	}

	/**
	 * Parallel upload of given {@link FileLink}s. Local {@link Path}s linked
	 * multiple times are uploaded only once. The upload progress may be traced
	 * with a progress listener.
	 *
	 * @param fileLinks
	 *            Collection of {@code FileLink}s to upload.
	 * @param progressListener
	 *            The progress listener.
	 * @throws IOException
	 *             Thrown if unable to upload files.
	 */
	public void uploadParallel(Collection<FileLink> fileLinks, ProgressListener progressListener) throws IOException {
		List<FileLink> filtered = retainForUpload(fileLinks);
		try {
			fileService.uploadParallel(entity, filtered, progressListener);
		} finally {
			filtered.stream().filter(FileLink::isRemote).forEach(fl -> {
				remotePaths.put(fl.getLocalPath(), fl.getRemotePath());
				uploaded.add(fl);
			});
		}
	}

	/**
	 * Once {@link #commit()} is called given {@link FileLink}s will be deleted
	 * from the remote storage.
	 *
	 * @param fileLinks
	 *            Collection of {@code FileLink}s to delete.
	 */
	public void addToRemove(Collection<FileLink> fileLinks) {
		toRemove.addAll(fileLinks);
	}

	/**
	 * Commits modifications of externally linked files.
	 */
	public void commit() {
		fileService.delete(entity, toRemove);
		scheduler.shutdown();
	}

	/**
	 * Aborts modifications of externally linked files.
	 */
	public void abort() {
		fileService.delete(entity, uploaded);
		uploaded.forEach(fl -> fl.setRemotePath(null));
		templateAttributeFileLinks.forEach((ta, v) -> ta.setDefaultValue(v.extract()));
		scheduler.shutdown();
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Filters given {@link FileLink}s by removing already uploaded ones.
	 *
	 * @param fileLinks
	 *            Will be filtered.
	 * @return Returns {@code FileLink}s which have to be uploaded.
	 */
	private List<FileLink> retainForUpload(Collection<FileLink> fileLinks) {
		List<FileLink> filtered = new ArrayList<>(fileLinks);
		for (FileLink fileLink : fileLinks) {
			String remotePath = remotePaths.get(fileLink.getLocalPath());
			if (remotePath != null && !remotePath.isEmpty()) {
				fileLink.setRemotePath(remotePath);
				filtered.remove(fileLink);
				uploaded.add(fileLink);
			}
		}

		return filtered;
	}

}
