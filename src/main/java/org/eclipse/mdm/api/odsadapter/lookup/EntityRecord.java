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


package org.eclipse.mdm.api.odsadapter.lookup;

import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.model.Entity;

/**
 * Utility class to group {@link Entity} and its {@link Core}.
 *
 * @param <T>
 *            The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class EntityRecord<T extends Entity> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	final Core core;
	final T entity;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param entity
	 *            The {@link Entity}.
	 * @param core
	 *            The {@link Core} of the {@code Entity}.
	 */
	EntityRecord(T entity, Core core) {
		this.entity = entity;
		this.core = core;
	}

}
