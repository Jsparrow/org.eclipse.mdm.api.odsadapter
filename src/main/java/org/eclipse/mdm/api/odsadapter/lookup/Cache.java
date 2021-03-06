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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

/**
 * Used to temporarily cache {@link EntityResult}s fore reuse in subsequent
 * {@link EntityRequest}s.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class Cache {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<EntityConfig<?>, EntityResult<?>> cache = new HashMap<>();

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Caches given {@link EntityResult}.
	 *
	 * @param entityResult
	 *            The {@link EntityResult}.
	 */
	public void add(EntityResult<?> entityResult) {
		cache.put(entityResult.request.entityConfig, entityResult);
	}

	/**
	 * Returns the cached {@link EntityResult} associated with given
	 * {@link EntityConfig}.
	 *
	 * @param entityConfig
	 *            Used as identifier.
	 * @return The {@code EntityResult} is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if requested {@code
	 * 		EntityResult} not found.
	 */
	public EntityResult<?> get(EntityConfig<?> entityConfig) {
		EntityResult<?> entityResult = cache.get(entityConfig);
		if (entityResult == null) {
			throw new IllegalArgumentException("Entity result not found");
		}

		return entityResult;
	}

}
