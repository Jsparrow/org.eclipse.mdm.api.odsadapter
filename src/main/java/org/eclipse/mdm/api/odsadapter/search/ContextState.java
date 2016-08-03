/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

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
