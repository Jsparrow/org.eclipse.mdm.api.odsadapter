/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Searchable;

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