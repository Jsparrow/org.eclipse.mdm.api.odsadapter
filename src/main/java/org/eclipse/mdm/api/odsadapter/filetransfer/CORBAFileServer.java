/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.filetransfer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.asam.ods.AoSession;
import org.asam.ods.ElemId;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService.Transfer;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highqsoft.corbafileserver.generated.CORBAFileServerException;
import com.highqsoft.corbafileserver.generated.CORBAFileServerIF;
import com.highqsoft.corbafileserver.generated.DS_BYTEHolder;
import com.highqsoft.corbafileserver.generated.ErrorCode;
import com.highqsoft.corbafileserver.generated.InputStreamIF;
import com.highqsoft.corbafileserver.generated.InputStreamIFPOA;
import com.highqsoft.corbafileserver.generated.SeverityFlag;

final class CORBAFileServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CORBAFileServer.class);

	private static final int DEFAULT_BUFFER_SIZE = 100_000;

	private static final int SOCKET_TIMEOUT = 5_000;

	private final CORBAFileServerIF fileServer;
	private final AoSession aoSession;
	private final ORB orb;

	private final Transfer transfer;

	private final int bufferSize;

	CORBAFileServer(ODSModelManager modelManager, Transfer transfer) {
		fileServer = modelManager.getFileServer();
		aoSession = modelManager.getAoSession();
		orb = modelManager.getORB();
		this.transfer = transfer;

		bufferSize = getBufferSize();
	}

	// ####################### DOWNLOAD

	public InputStream openStream(FileLink fileLink, ElemId elemId) throws IOException {
		InputStream inputStream;
		if(transfer.isStream()) {
			inputStream = new InputStreamAdapter(openSimpleStream(fileLink, elemId));
		} else if(transfer.isSocket()) {
			inputStream = openSocketStream(fileLink, elemId);
		} else {
			throw new IllegalStateException("Transfer state '" + transfer + "' is not supported.");
		}

		return new BufferedInputStream(inputStream, bufferSize);
	}

	private InputStreamIF openSimpleStream(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return fileServer.readForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to open stream for file transfer due to: " + e.reason, e);
		}
	}

	private InputStream openSocketStream(FileLink fileLink, ElemId elemId) throws IOException {
		// auto assigned port with awaiting exactly ONE incoming connection
		try(ServerSocket serverSocket = new ServerSocket(0, 1)) {
			serverSocket.setSoTimeout(SOCKET_TIMEOUT * 6);

			new Thread(() -> {
				try {
					/*
					 * NOTE: Since a socket file transfer registration may block until
					 * this server socket's accept method is called, the registration
					 * is done asynchronously!
					 */
					fileServer.getForInstanceBySocket(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid,
							InetAddress.getLocalHost().getHostName(), serverSocket.getLocalPort());
				} catch (CORBAFileServerException | IOException e) {
					LOGGER.error("Unable to initialize socket stream, awaiting socket timeout.", e);
				}
			}).start();

			Socket client = serverSocket.accept();
			client.setSoTimeout(SOCKET_TIMEOUT);
			return client.getInputStream();
		}
	}

	public long loadSize(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return fileServer.getSizeForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to query file size due to: " + e.reason, e);
		}
	}

	// ######################### UPLOAD

	public String uploadStream(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		if(transfer.isStream()) {
			return uploadVIAStream(inputStream, fileLink, elemId);
		} else if(transfer.isSocket()) {
			return uploadVIASocket(inputStream, fileLink, elemId);
		} else {
			throw new IllegalStateException("Transfer state '" + transfer + "' is not supported.");
		}
	}

	private String uploadVIAStream(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		try(CORBAInputStreamAdapter stream = new CORBAInputStreamAdapter(orb, inputStream, fileLink.getSize())) {
			return fileServer.saveForInstance(aoSession, fileLink.getFileName(), "", elemId.aid, elemId.iid, stream._this());
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to upload file via stream due to: " + e.reason, e);
		}
	}

	private String uploadVIASocket(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		// auto assigned port with awaiting exactly ONE incoming connection
		try (ServerSocket serverSocket = new ServerSocket(0, 1)) {
			serverSocket.setSoTimeout(SOCKET_TIMEOUT * 6);

			new Thread(() -> {
				try(Socket client = serverSocket.accept(); OutputStream outputStream = client.getOutputStream()) {
					byte[] buffer = new byte[bufferSize];

					int length;
					while((length = inputStream.read(buffer)) > -1) {
						outputStream.write(buffer, 0, length);
					}
				} catch(IOException e) {
					LOGGER.error("Unable to initialize socket stream, awaiting socket timeout.", e);
				}
			}).start();

			try {
				return fileServer.saveForInstanceBySocket(aoSession, fileLink.getFileName(), "", elemId.aid, elemId.iid,
						InetAddress.getLocalHost().getHostName(), serverSocket.getLocalPort());
			} catch (CORBAFileServerException e) {
				throw new IOException("Unable to upload file via socket due to: " + e.reason, e);
			}
		}
	}

	public void delete(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			fileServer.deleteForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to delete remote file due to: " + e.reason, e);
		}
	}

	private int getBufferSize() {
		try {
			// try to use the same buffer size as the corba file server for best performance
			return Integer.parseInt(fileServer.getContext(aoSession, "CORBAFileServer.BufferSize"));
		} catch (CORBAFileServerException e) {
			return DEFAULT_BUFFER_SIZE;
		}
	}

	// ------- CLASSES

	// locally consumable remote input stream
	private static final class InputStreamAdapter extends InputStream {

		private final InputStreamIF inputStream;

		InputStreamAdapter(InputStreamIF inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public int read() throws IOException {
			byte[] b = new byte[1];
			return read(b) == -1 ? -1 : b[0];
		}

		@Override
		public int read(byte[] buffer, int offset, int length) throws IOException {
			try {
				DS_BYTEHolder byteHolder = new DS_BYTEHolder();
				int receivedBytes = inputStream.read(byteHolder, offset, length);
				if(receivedBytes > 0) {
					System.arraycopy(byteHolder.value, 0, buffer, 0, receivedBytes);
				}
				return receivedBytes;
			} catch(CORBAFileServerException e) {
				throw new IOException("Failed to retrieve bytes from CORBA input stream due to: " + e.reason, e);
			}
		}

		@Override
		public void close() throws IOException {
			try {
				inputStream.close();
			} catch (CORBAFileServerException e) {
				throw new IOException("Unable to close CORBA input stream due to: " + e.reason, e);
			} finally {
				inputStream._release();
			}
		}

	}

	// remotely consumable local input stream
	private static final class CORBAInputStreamAdapter extends InputStreamIFPOA implements AutoCloseable {

		private final InputStream inputStream;
		private final long length;

		private final byte[] objectID;
		private final POA poa;

		public CORBAInputStreamAdapter(ORB orb, InputStream inputStream, long length) throws IOException {
			this.inputStream = inputStream;
			this.length = length;

			try {
				poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
				poa.the_POAManager().activate();
				objectID = poa.activate_object(this);
			} catch (AdapterInactive | InvalidName | ServantAlreadyActive | WrongPolicy e) {
				throw new IOException("Unable to create CORBA input stream.", e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(DS_BYTEHolder b, int off, int len) throws CORBAFileServerException {
			b.value = new byte[len];

			try {
				return inputStream.read(b.value, off, len);
			} catch (IOException e) {
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR, e.getMessage());
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws CORBAFileServerException {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR, e.getMessage());
			} finally {
				try {
					poa.deactivate_object(objectID);
				} catch (ObjectNotActive | WrongPolicy e) {
					LOGGER.warn("Unable to deactive CORBA input stream", e);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int length() throws CORBAFileServerException {
			/*
			 * NOTE: A file length is of type long and therefore,
			 * for very large files (> 2.14 GB), the exact length
			 * is lost due to narrowing conversion!
			 */
			return (int) length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reset() throws CORBAFileServerException {
			try {
				inputStream.reset();
			} catch (IOException e) {
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR, e.getMessage());
			}
		}

	}

}
