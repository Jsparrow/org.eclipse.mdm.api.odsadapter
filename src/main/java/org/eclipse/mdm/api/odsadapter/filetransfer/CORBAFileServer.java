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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.asam.ods.AoSession;
import org.asam.ods.ElemId;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.odsadapter.ODSContext;
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

/**
 * Service provides access to the low level {@link CORBAFileServerIF} file
 * service API.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class CORBAFileServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CORBAFileServer.class);
	private static final int DEFAULT_BUFFER_SIZE = 100_000;
	private static final int SOCKET_TIMEOUT = 5_000;
	private static final String INTERFACE_NAME_PROPERTY = "org.eclipse.mdm.api.odsadapter.filetransfer.interfaceName";

	private final CORBAFileServerIF fileServer;
	private final AoSession aoSession;
	private final ORB orb;

	private final Transfer transfer;

	private final int bufferSize;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            Used for setup.
	 * @param transfer
	 *            The transfer type for up- and downloads.
	 */
	CORBAFileServer(ODSContext context, Transfer transfer) {
		ModelManager mm = context.getModelManager().orElseThrow(() -> new ServiceNotProvidedException(ModelManager.class));
		if (!(mm instanceof ODSModelManager)) {
			throw new IllegalArgumentException("The supplied ModelManager must be an ODSModelManager!");
		}

		fileServer = context.getFileServer();
		aoSession = context.getAoSession();
		orb = context.getORB();
		this.transfer = transfer;

		bufferSize = getBufferSize();
	}

	/**
	 * Opens a consumable download {@link InputStream} for given
	 * {@link FileLink}.
	 *
	 * @param fileLink
	 *            Used to access the remote path.
	 * @param elemId
	 *            Used for security checks.
	 * @return The consumable {@code InputStream} is returned.
	 * @throws IOException
	 *             Thrown if unable to open an the {@code InputStream}.
	 */
	public InputStream openStream(FileLink fileLink, ElemId elemId) throws IOException {
		InputStream inputStream;
		if (transfer.isStream()) {
			inputStream = new InputStreamAdapter(openSimpleStream(fileLink, elemId));
		} else if (transfer.isSocket()) {
			inputStream = openSocketStream(fileLink, elemId);
		} else {
			throw new IllegalStateException("Transfer state '" + transfer + "' is not supported.");
		}

		return new BufferedInputStream(inputStream, bufferSize);
	}

	/**
	 * Uploads given {@link InputStream} for given {@link FileLink}.
	 *
	 * @param inputStream
	 *            The {@code InputStream} to be uploaded.
	 * @param fileLink
	 *            The associated {@code FileLink}.
	 * @param elemId
	 *            Used for security checks.
	 * @throws IOException
	 *             Thrown if unable to upload given {@code InputStream}.
	 */
	public void uploadStream(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		String remotePath;
		if (transfer.isStream()) {
			remotePath = uploadVIAStream(inputStream, fileLink, elemId);
		} else if (transfer.isSocket()) {
			remotePath = uploadVIASocket(inputStream, fileLink, elemId);
		} else {
			throw new IllegalStateException("Transfer state '" + transfer + "' is not supported.");
		}

		fileLink.setRemotePath(remotePath);
	}

	/**
	 * Loads the file size for given {@link FileLink}.
	 *
	 * @param fileLink
	 *            The {@code FileLink} whose file size will be loaded.
	 * @param elemId
	 *            Used for security checks.
	 * @return The file size is returned.
	 * @throws IOException
	 *             Thrown if unable to load the file size.
	 */
	public long loadSize(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return fileServer.getSizeForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to query file size due to: " + e.reason, e);
		}
	}

	/**
	 * Deletes the {@link FileLink} from the remote storage.
	 *
	 * @param fileLink
	 *            Will be deleted from the remote storage.
	 * @param elemId
	 *            Used for security checks.
	 * @throws IOException
	 *             Thrown if unable to delete given {@code FileLink}.
	 */
	public void delete(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			fileServer.deleteForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to delete remote file due to: " + e.reason, e);
		}
	}

	/**
	 * Opens a simple {@link InputStreamIF} using the {@link CORBAFileServerIF}.
	 *
	 * @param fileLink
	 *            Used to access the remote path.
	 * @param elemId
	 *            Used for security checks.
	 * @return The {@code InputStreamIF} is returned.
	 * @throws IOException
	 *             Thrown if unable to open the {@code InputStreamIF}.
	 */
	private InputStreamIF openSimpleStream(FileLink fileLink, ElemId elemId) throws IOException {
		try {
			return fileServer.readForInstance(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid);
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to open stream for file transfer due to: " + e.reason, e);
		}
	}

	/**
	 * Opens a socket {@link InputStream} using the {@link CORBAFileServerIF}.
	 *
	 * @param fileLink
	 *            Used to access the remote path.
	 * @param elemId
	 *            Used for security checks.
	 * @return The {@code InputStream} is returned.
	 * @throws IOException
	 *             Thrown if unable to open the socket {@code
	 * 		InputStream}.
	 */
	private InputStream openSocketStream(FileLink fileLink, ElemId elemId) throws IOException {
		// auto assigned port with awaiting exactly ONE incoming connection
		try (ServerSocket serverSocket = new ServerSocket(0, 1, getInterfaceAddress())) {
			serverSocket.setSoTimeout(SOCKET_TIMEOUT * 6);

			new Thread(() -> {
				try {
					/*
					 * NOTE: Since a socket file transfer registration may block
					 * until this server socket's accept method is called, the
					 * registration is done asynchronously!
					 */
					fileServer.getForInstanceBySocket(aoSession, fileLink.getRemotePath(), elemId.aid, elemId.iid,
							InetAddress.getLocalHost().getHostAddress(), serverSocket.getLocalPort());
				} catch (CORBAFileServerException | IOException e) {
					LOGGER.error("Unable to initialize socket stream, awaiting socket timeout.", e);
				}
			}).start();

			Socket client = serverSocket.accept();
			client.setSoTimeout(SOCKET_TIMEOUT);
			return client.getInputStream();
		}
	}

	private InetAddress getInterfaceAddress() throws SocketException {
		String property = System.getProperty(INTERFACE_NAME_PROPERTY);
		if (StringUtils.isEmpty(property)) {
			LOGGER.debug("Using no specified interface for file transfer, property not set.");
			return null;
		}
		List<NetworkInterface> interfaces = getInterfaceList();
		List<NetworkInterface> filteredInterfaces = getFilteredInterfaces(interfaces);
		for (NetworkInterface filteredInterface : filteredInterfaces) {
			if (filteredInterface.getName().equals(property)
					&& filteredInterface.getInetAddresses().hasMoreElements()) {
				InetAddress inetAddress = filteredInterface.getInetAddresses().nextElement();
				LOGGER.debug("Using interface {} with address {} for file transfer.", filteredInterface.getName(),
						inetAddress);
				return inetAddress;
			}
		}
		return getFallback(filteredInterfaces);
	}

	private InetAddress getFallback(List<NetworkInterface> filteredInterfaces) {
		if (filteredInterfaces.isEmpty()) {
			LOGGER.debug("Using no specified interface for file transfer, property set but no running interface.");
			return null;
		}
		NetworkInterface networkInterface = filteredInterfaces.get(0);
		Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
		if (inetAddresses.hasMoreElements()) {
			InetAddress address = inetAddresses.nextElement();
			LOGGER.debug("Using interface {} with address {} for file transfer.", networkInterface.getName(), address);
			return address;
		}
		return null;
	}

	private List<NetworkInterface> getFilteredInterfaces(List<NetworkInterface> interfaces) throws SocketException {
		List<NetworkInterface> filteredInterfaces = new ArrayList<>();
		for (NetworkInterface anInterface : interfaces) {
			if (anInterface.isUp() && !anInterface.isLoopback() && !anInterface.isVirtual()) {
				filteredInterfaces.add(anInterface);
			}
		}
		return filteredInterfaces;
	}

	private List<NetworkInterface> getInterfaceList() throws SocketException {
		List<NetworkInterface> result = new ArrayList<>();
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			result.add(networkInterfaces.nextElement());
		}
		return result;
	}

	/**
	 * Uploads given {@link InputStream} for given {@link FileLink} using the
	 * {@link CORBAFileServerIF}.
	 *
	 * @param inputStream
	 *            The {@code InputStream} to be uploaded.
	 * @param fileLink
	 *            The associated {@code FileLink}.
	 * @param elemId
	 *            Used for security checks.
	 * @return The remote path of the uploaded {@code InputStream} is returned.
	 * @throws IOException
	 *             Thrown if unable to upload given {@code InputStream}.
	 */
	private String uploadVIAStream(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		try (CORBAInputStreamAdapter stream = new CORBAInputStreamAdapter(orb, inputStream, fileLink.getSize())) {
			return fileServer.saveForInstance(aoSession, fileLink.getFileName(), "", elemId.aid, elemId.iid,
					stream._this());
		} catch (CORBAFileServerException e) {
			throw new IOException("Unable to upload file via stream due to: " + e.reason, e);
		}
	}

	/**
	 * Uploads given {@link InputStream} for given {@link FileLink} via socket
	 * upload using the {@link CORBAFileServerIF}.
	 *
	 * @param inputStream
	 *            The {@code InputStream} to be uploaded.
	 * @param fileLink
	 *            The associated {@code FileLink}.
	 * @param elemId
	 *            Used for security checks.
	 * @return The remote path of the uploaded {@code InputStream} is returned.
	 * @throws IOException
	 *             Thrown if unable to upload given {@code InputStream}.
	 */
	private String uploadVIASocket(InputStream inputStream, FileLink fileLink, ElemId elemId) throws IOException {
		// auto assigned port with awaiting exactly ONE incoming connection
		try (ServerSocket serverSocket = new ServerSocket(0, 1, getInterfaceAddress())) {
			serverSocket.setSoTimeout(SOCKET_TIMEOUT * 6);

			new Thread(() -> {
				try (Socket client = serverSocket.accept(); OutputStream outputStream = client.getOutputStream()) {
					byte[] buffer = new byte[bufferSize];

					int length;
					while ((length = inputStream.read(buffer)) > -1) {
						outputStream.write(buffer, 0, length);
					}
				} catch (IOException e) {
					LOGGER.error("Unable to initialize socket stream, awaiting socket timeout.", e);
				}
			}).start();

			int localPort = serverSocket.getLocalPort();
			String localHostName = serverSocket.getInetAddress().getHostName();
			try {
				return fileServer.saveForInstanceBySocket(aoSession, fileLink.getFileName(), "", elemId.aid, elemId.iid,
						localHostName, localPort);
			} catch (CORBAFileServerException e) {
				String message = String.format("Unable to upload file via socket to %s:%d due to: %s", localHostName, localPort, e.reason);
				throw new IOException(message, e);
			}
		}
	}

	/**
	 * Tries to load the buffer size used by the {@link CORBAFileServerIF} to
	 * reach best performance. In case of errors a default buffer size of of
	 * {@value #DEFAULT_BUFFER_SIZE} is used.
	 *
	 * @return The buffer size is returned.
	 */
	private int getBufferSize() {
		try {
			// try to use the same buffer size as the corba file server for best
			// performance
			return Integer.parseInt(fileServer.getContext(aoSession, "CORBAFileServer.BufferSize"));
		} catch (NumberFormatException | CORBAFileServerException e) {
			return DEFAULT_BUFFER_SIZE;
		}
	}

	/**
	 * A simple {@link InputStream} adapter implementation for an
	 * {@link InputStreamIF}.
	 */
	private static final class InputStreamAdapter extends InputStream {

		private final InputStreamIF inputStream;

		/**
		 * Constructor.
		 *
		 * @param inputStream
		 *            The wrapped {@link InputStreamIF}.
		 */
		private InputStreamAdapter(InputStreamIF inputStream) {
			this.inputStream = inputStream;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read() throws IOException {
			byte[] b = new byte[1];
			return read(b) == -1 ? -1 : b[0];
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(byte[] buffer, int offset, int length) throws IOException {
			try {
				DS_BYTEHolder byteHolder = new DS_BYTEHolder();
				int receivedBytes = inputStream.read(byteHolder, offset, length);
				if (receivedBytes > 0) {
					System.arraycopy(byteHolder.value, 0, buffer, 0, receivedBytes);
				}
				return receivedBytes;
			} catch (CORBAFileServerException e) {
				throw new IOException("Failed to retrieve bytes from CORBA input stream due to: " + e.reason, e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
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
	/**
	 * A simple {@link InputStreamIF} adapter implementation for an
	 * {@link InputStream}.
	 */
	private static final class CORBAInputStreamAdapter extends InputStreamIFPOA implements AutoCloseable {

		private final InputStream inputStream;
		private final long length;

		private final byte[] objectID;
		private final POA poa;

		/**
		 * Constructor.
		 *
		 * @param orb
		 *            Used to access the root {@link POA} to activate this CORBA
		 *            service object.
		 * @param inputStream
		 *            The wrapped {@link InputStream}.
		 * @param length
		 *            The length of the wrapped {@code InputStream}.
		 * @throws IOException
		 *             Thrown on errors.
		 */
		private CORBAInputStreamAdapter(ORB orb, InputStream inputStream, long length) throws IOException {
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
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR,
						e.getMessage());
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
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR,
						e.getMessage());
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
			 * NOTE: A file length is of type long and therefore, for very large
			 * files (> 2.14 GB), the exact length is lost due to narrowing
			 * conversion!
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
				throw new CORBAFileServerException(ErrorCode.FILESERVER_IO_EXCEPTION, SeverityFlag.ERROR,
						e.getMessage());
			}
		}

	}

}
