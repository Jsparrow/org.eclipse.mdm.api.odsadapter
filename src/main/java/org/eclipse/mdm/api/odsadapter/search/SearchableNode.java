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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.search.Searchable;

/**
 * Implementation of the {@link Searchable} interface for use in search queries
 * derived from {@link BaseEntitySearchQuery}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class SearchableNode implements Searchable {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final List<Searchable> relatedSearchables = new ArrayList<>();
	private final EntityType entityType;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param entityType
	 *            The associated {@link EntityType}.
	 */
	SearchableNode(EntityType entityType) {
		this.entityType = entityType;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Searchable> getRelatedSearchables() {
		return Collections.unmodifiableList(relatedSearchables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Searchable(").append("EntityType = ").append(entityType);

		if (!isLeaf()) {
			sb.append(", relatedSearchables = ")
					.append(relatedSearchables.stream().map(Searchable::getEntityType).collect(Collectors.toList()));
		}

		return sb.append(')').toString();
	}

	// ======================================================================
	// Package methods
	// ======================================================================

	/**
	 * Adds given {@link Searchable} as a child to this searchable.
	 *
	 * @param searchable
	 *            Will be added a child.
	 */
	void addRelated(Searchable searchable) {
		relatedSearchables.add(searchable);
	}

}