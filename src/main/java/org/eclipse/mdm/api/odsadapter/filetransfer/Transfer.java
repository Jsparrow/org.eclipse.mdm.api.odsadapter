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

import java.io.InputStream;

/**
 * Transfer type enumeration.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public enum Transfer {

	// ======================================================================
	// Enum constants
	// ======================================================================

	/**
	 * Simple {@link InputStream}s are used for up- and downloads.
	 */
	STREAM,

	/**
	 * Socket {@link InputStream}s are used for up- and downloads.
	 */
	SOCKET;

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Checks whether this transfer is {@link #STREAM}.
	 *
	 * @return Returns {@code true} if this instance is {@link #STREAM}.
	 */
	boolean isStream() {
		return STREAM == this;
	}

	/**
	 * Checks whether this transfer is {@link #SOCKET}.
	 *
	 * @return Returns {@code true} if this instance is {@link #SOCKET}.
	 */
	boolean isSocket() {
		return SOCKET == this;
	}

}