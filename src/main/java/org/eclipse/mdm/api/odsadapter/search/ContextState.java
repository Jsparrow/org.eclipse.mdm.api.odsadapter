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


package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;

/**
 * Context state enumeration.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
enum ContextState {

	// ======================================================================
	// Enum constants
	// ======================================================================

	/**
	 * References {@link ContextRoot}s of {@link TestStep}s.
	 */
	ORDERED,

	/**
	 * References {@link ContextRoot}s of {@link Measurement}s.
	 */
	MEASURED;

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Checks whether this context state is {@link #ORDERED}.
	 *
	 * @return Returns {@code true} if this instance is {@link #ORDERED}.
	 */
	public boolean isOrdered() {
		return ORDERED == this;
	}

	/**
	 * Checks whether this context state is {@link #MEASURED}.
	 *
	 * @return Returns {@code true} if this instance is {@link #MEASURED}.
	 */
	public boolean isMeasured() {
		return MEASURED == this;
	}

}
