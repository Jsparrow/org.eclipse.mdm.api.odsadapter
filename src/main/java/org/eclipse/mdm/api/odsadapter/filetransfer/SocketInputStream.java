/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.asam.ods.ElemId;
import org.eclipse.mdm.api.base.model.FileLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SocketInputStream extends InputStream {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketInputStream.class);

	private static final int SOCKET_TIMEOUT = 5_000;

	private final ServerSocket serverSocket;
	private final Socket clientSocket;
	private final InputStream inputStream;

	public SocketInputStream(ODSFileServer fileServer, FileLink fileLink, ElemId elemId) throws IOException {
		// auto assigned port with awaiting exactly ONE incoming connection
		serverSocket = new ServerSocket(0, 1);
		serverSocket.setSoTimeout(SOCKET_TIMEOUT * 6);

		new Thread(() -> {
			try {
				/*
				 * NOTE: Since a socket file transfer registration may block until
				 * this server socket's accept method is called, the registration
				 * is done asynchronously!
				 */
				fileServer.registerSocket(serverSocket.getLocalPort(), fileLink, elemId);
			} catch (IOException e) {
				LOGGER.error("Failed to register socket file transfer, awaiting socket timeout.", e);
			}
		}).start();

		clientSocket = serverSocket.accept();
		clientSocket.setSoTimeout(SOCKET_TIMEOUT);
		inputStream = clientSocket.getInputStream();
	}

	@Override
	public int read() throws IOException {
		byte[] b = new byte[1];
		return read(b) == -1 ? -1 : b[0];
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	@Override
	public void close() throws IOException {
		try {
			// closing the input stream implicitly closes the associated socket
			inputStream.close();
		} finally {
			serverSocket.close();
		}
	}

}
