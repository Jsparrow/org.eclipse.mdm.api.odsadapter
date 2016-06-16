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

import com.highqsoft.corbafileserver.generated.CORBAFileServerException;
import com.highqsoft.corbafileserver.generated.DS_BYTEHolder;
import com.highqsoft.corbafileserver.generated.InputStreamIF;

public class InputStreamWrapper extends InputStream {

	private final InputStreamIF inputStream;

	InputStreamWrapper(InputStreamIF inputStream) {
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
			throw new IllegalStateException("Failed to retrieve bytes from CORBA input stream due to: " +e.reason, e);
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
