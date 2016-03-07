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

final class SearchableNode implements Searchable {

	private final List<Searchable> relatedSearchables = new ArrayList<>();
	private final EntityType entityType;
	private final boolean implicit;

	SearchableNode(EntityType entityType, boolean implicit) {
		this.entityType = entityType;
		this.implicit = implicit;
	}

	@Override
	public List<Searchable> getRelatedSearchables() {
		return Collections.unmodifiableList(relatedSearchables);
	}

	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	@Override
	public boolean isImplicit() {
		return implicit;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Searchable(").append("entityType = ").append(entityType);
		sb.append(", implicit = ").append(isImplicit());

		if(!isLeaf()) {
			sb.append(", relatedSearchables = ").append(
					relatedSearchables.stream()
					.map(Searchable::getEntityType)
					.collect(Collectors.toList()));
		}

		return sb.append(')').toString();
	}

	void addRelated(Searchable searchable) {
		relatedSearchables.add(searchable);
	}

}