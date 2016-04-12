/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.Objects;

import org.asam.ods.ApplAttr;
import org.asam.ods.ApplRel;
import org.asam.ods.DataType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

final class ODSRelation implements Relation {

	private final Relationship relationship;
	private final EntityType source;
	private final EntityType target;
	private final String name;

	private final int rangeMax;

	private Attribute attribute;

	ODSRelation(ApplRel applRel, EntityType source, EntityType target) {
		this.source = source;
		this.target = target;
		name = applRel.arName;
		relationship = ODSUtils.RELATIONSHIPS.revert(applRel.arRelationType);
		rangeMax = applRel.arRelationRange.max;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public EntityType getSource() {
		return source;
	}

	@Override
	public EntityType getTarget() {
		return target;
	}

	@Override
	public Relationship getRelationship() {
		return relationship;
	}

	@Override
	public Attribute getAttribute() {
		if(attribute == null) {
			attribute = new ODSAttribute(getSource(), new ApplAttr(getName(), "", DataType.DT_LONGLONG, 0, true, false, null), null);
		}

		return attribute;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSource(), getTarget(), getName());
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof ODSRelation) {
			Relation relation = (Relation) object;
			return getSource().equals(relation.getSource()) &&
					getTarget().equals(relation.getTarget()) &&
					getName().equals(relation.getName());
		}

		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Deprecated
	//relation from N Site (= Fremdschlüsselspalte ist in gleicher Tabelle)
	boolean isIncomming(Relationship relationship) {
		return relationship.equals(getRelationship()) && rangeMax == 1;
	}

	@Deprecated
	//relation to N Site (= Fremdschlüsselspalte ist in Ziel Tabelle)
	boolean isOutgoing(Relationship relationship) {
		return relationship.equals(getRelationship()) && rangeMax == -1;
	}

}
