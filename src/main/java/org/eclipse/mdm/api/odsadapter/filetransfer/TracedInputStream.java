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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.mdm.api.base.file.FileService.ProgressListener;

/**
 * This is an {@link InputStream} wrapper implementation to trace the progress
 * of an {@code InputStream}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class TracedInputStream extends InputStream {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ProgressListener progressListener;
	private final InputStream inputStream;
	private final long size;

	private double transferred = 0;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param inputStream
	 *            The traced {@link InputStream}.
	 * @param progressListener
	 *            The listener will be used to fire update notifications.
	 * @param length
	 *            The length of the consumed {@code InputStream}.
	 */
	TracedInputStream(InputStream inputStream, ProgressListener progressListener, long length) {
		this.progressListener = progressListener;
		this.inputStream = inputStream;
		size = length;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		int read = inputStream.read(buffer, offset, length);
		if (read > -1) {
			transferred += read;
			progressListener.progress(read, (float) (transferred / size));
		}
		return read;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		inputStream.close();
	}

}
