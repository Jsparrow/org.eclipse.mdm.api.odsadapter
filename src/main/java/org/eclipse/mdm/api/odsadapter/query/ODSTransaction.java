/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.List;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElemAccess;
import org.eclipse.mdm.api.base.query.EntityType;

public final class ODSTransaction {

	private final ODSModelManager modelManager;
	private final ApplElemAccess applElemAccess;
	private final AoSession aoSession;
	private final String id;

	private final List<EntityType> modified = new ArrayList<>();
	private final List<EntityType> deleted = new ArrayList<>();

	//	private final List<FileLink> fileLinks = new ArrayList<>();

	public ODSTransaction(ODSModelManager modelManager) throws AoException {
		this.modelManager = modelManager;
		aoSession = modelManager.getAoSession().createCoSession();
		applElemAccess = aoSession.getApplElemAccess();
		aoSession.startTransaction();
		id = aoSession.toString();
	}

	public ODSModelManager getModelManager() {
		return modelManager;
	}

	public ApplElemAccess getApplElemAccess() {
		return applElemAccess;
	}

	public String getID() {
		return id;
	}

	public void modified(EntityType entityType) {
		// clear the cache of this entity type
		modified.add(entityType);
	}

	public void deleted(EntityType entityType) {
		// clear the cache of this entity type
		// and clear the caches of related entity types!
		deleted.add(entityType);
	}

	//	public void addFileLinks(List<FileLink> fileLinks) {
	//		this.fileLinks.addAll(fileLinks);
	//	}
	//
	//	public void addFileLink(FileLink fileLink) {
	//		fileLinks.add(fileLink);
	//	}

	public void commitTransaction() throws AoException {
		// TODO try to upload files here
		// - abort transaction on failures

		aoSession.commitTransaction();
		aoSession.close();
		aoSession._release();

		// TODO try to remove files here
		// - log on failures
	}

	public void abortTransaction() throws AoException {
		aoSession.abortTransaction();
		aoSession.close();
		aoSession._release();
	}

}
