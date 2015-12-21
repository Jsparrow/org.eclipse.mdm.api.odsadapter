/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import org.asam.ods.ApplRel;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSRelation implements Relation {

	private final ApplRel applRel;
	private final Entity source;
	private final Entity target;

	public ODSRelation(ApplRel applRel, Entity source, Entity target) {
		this.applRel = applRel;
		this.source = source;
		this.target = target;
	}

	@Override
	public String getName() {
		return applRel.arName;
	}

	@Override
	public Entity getSource() {
		return source;
	}

	@Override
	public Entity getTarget() {
		return target;
	}

	@Override
	public Relationship getRelationship() {
		return ODSUtils.RELATIONSHIPS.revert(applRel.arRelationType);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Deprecated
	//relation from N Site (= Fremdschl�sselspalte ist in gleicher Tabelle)
	boolean isIncomming(Relationship relationship) {
		return relationship.equals(getRelationship()) && applRel.arRelationRange.max == 1;
	}

	@Deprecated
	//relation to N Site (= Fremdschl�sselspalte ist in Ziel Tabelle)
	boolean isOutgoing(Relationship relationship) {
		return relationship.equals(getRelationship()) && applRel.arRelationRange.max == -1;
	}

}
