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
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.FileService.ProgressListener;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService.Transfer;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

final class UploadService {

	private final Map<TemplateAttribute, Value> templateAttributeFileLinks = new HashMap<>();

	private final List<FileLink> uploaded = new ArrayList<>();

	private final Map<Path, String> remotePaths = new HashMap<>();

	private final List<FileLink> toRemove = new ArrayList<>();

	private final CORBAFileService fileService;
	private final Entity entity;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public UploadService(ODSModelManager modelManager,Entity entity,  Transfer transfer) {
		fileService = new CORBAFileService(modelManager, transfer);
		this.entity = entity;

		scheduler.scheduleAtFixedRate(() -> {
			try {
				modelManager.getAoSession().getName();
			} catch(AoException e) {
				/*
				 * NOTE: This is done to keep the parent transaction's session alive
				 * till its commit or abort method is called. If this session refresh
				 * results in an error, then any running file transfer will abort
				 * with a proper error, therefore any exception here is completely
				 * ignored and explicitly NOT logged!
				 */
			}
		}, 5, 5, TimeUnit.MINUTES);
	}

	public void upload(Collection<TemplateAttribute> templateAttributes, ProgressListener progressListener) throws IOException {
		List<FileLink> fileLinks = new ArrayList<>();
		for(TemplateAttribute templateAttribute : templateAttributes) {
			Value defaultValue = templateAttribute.getDefaultValue();
			if(!defaultValue.isValid()) {
				continue;
			}

			if(defaultValue.getValueType().isFileLink()) {
				fileLinks.add(defaultValue.extract());
			} else if(defaultValue.getValueType().isFileLinkSequence()) {
				fileLinks.addAll(Arrays.asList((FileLink[])defaultValue.extract()));
			} else {
				throw new IllegalStateException("Template attribute's value type is not of type file link.");
			}

			templateAttributeFileLinks.put(templateAttribute, defaultValue);
		}

		if(!fileLinks.isEmpty()) {
			uploadParallel(fileLinks, progressListener);
			// remote paths available -> update template attribute
			templateAttributeFileLinks.forEach((ta, v) -> ta.setDefaultValue(v.extract()));
		}
	}

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

	public void addToRemove(Collection<FileLink> fileLinks) {
		toRemove.addAll(fileLinks);
	}

	public void commit() {
		fileService.delete(entity, toRemove.stream().collect(Collectors.groupingBy(FileLink::getRemotePath))
				.values().stream().map(l -> l.get(0)).collect(Collectors.toList()));
		scheduler.shutdown();
	}

	public void abort() {
		fileService.delete(entity, uploaded.stream().collect(Collectors.groupingBy(FileLink::getRemotePath))
				.values().stream().map(l -> l.get(0)).collect(Collectors.toList()));
		uploaded.forEach(fl -> fl.setRemotePath(null));
		templateAttributeFileLinks.forEach((ta, v) -> ta.setDefaultValue(v.extract()));
		scheduler.shutdown();
	}

	private List<FileLink> retainForUpload(Collection<FileLink> fileLinks) {
		List<FileLink> filtered = new ArrayList<>(fileLinks);
		for(FileLink fileLink : fileLinks) {
			String remotePath = remotePaths.get(fileLink.getLocalPath());
			if(remotePath != null && !remotePath.isEmpty()) {
				fileLink.setRemotePath(remotePath);
				filtered.remove(fileLink);
				uploaded.add(fileLink);
			}
		}

		return filtered;
	}

}
