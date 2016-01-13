/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Searchable;

final class SearchableNode implements Searchable {

	private final List<Searchable> related = new ArrayList<>();
	private final Entity entity;
	private final boolean implicit;

	SearchableNode(Entity entity, boolean implicit) {
		this.implicit = implicit;
		this.entity = entity;
	}

	@Override
	public List<Searchable> getRelated() {
		return Collections.unmodifiableList(related);
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public boolean isImplicit() {
		return implicit;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Searchable(").append("entity = ").append(entity);
		sb.append(", implicit = ").append(isImplicit());

		if(!isLeaf()) {
			sb.append(", related = ").append(related.stream().map(n -> n.getEntity()).collect(Collectors.toList()));
		}

		return sb.append(')').toString();
	}

	void addRelated(Searchable entity) {
		related.add(entity);
	}

}