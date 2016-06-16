/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.filetransfer;

import java.io.IOException;
import java.net.InetAddress;

import org.asam.ods.AoSession;
import org.asam.ods.ElemId;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

import com.highqsoft.corbafileserver.generated.CORBAFileServerException;
import com.highqsoft.corbafileserver.generated.CORBAFileServerIF;

final class ODSFileServer {

	private final CORBAFileServerIF fileServer;
	private final AoSession aoSession;

	public ODSFileServer(ODSModelManager modelManager) {
		fileServer = modelManager.getFileServer();
		aoSession = modelManager.getAoSession();
	}

	public InputStreamWrapper createStream(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return new InputStreamWrapper(fileServer.readForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid));
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to create input stream wrapper due to: " + e.reason, e);
		}
	}

	public void registerSocket(int serverPort, FileLink fileLink, ElemId elemId) throws IOException {
		try {
			fileServer.getForInstanceBySocket(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid,
					InetAddress.getLocalHost().getHostName(), serverPort);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to register server socket due to: " + e.reason, e);
		}
	}

	public long getSize(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return fileServer.getSizeForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to query file size due to: " + e.reason, e);
		}
	}

}
