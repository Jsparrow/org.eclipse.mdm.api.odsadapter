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


package org.eclipse.mdm.api.odsadapter.query;

import java.util.Objects;

import org.asam.ods.ApplAttr;
import org.asam.ods.ApplRel;
import org.asam.ods.DataType;
import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.adapter.RelationType;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * ODS implementation of the {@link Relation} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class ODSRelation implements Relation {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final RelationType relationType;
	private final EntityType source;
	private final EntityType target;
	private final String name;

	private final int rangeMax;

	private Attribute attribute;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param applRel
	 *            The ODS meta data for this relation.
	 * @param source
	 *            The source {@link EntityType}.
	 * @param target
	 *            The target {@code EntityType}.
	 */
	ODSRelation(ApplRel applRel, EntityType source, EntityType target) {
		this.source = source;
		this.target = target;
		name = applRel.arName;
		relationType = ODSUtils.RELATIONSHIPS.inverse().get(applRel.arRelationType);
		rangeMax = applRel.arRelationRange.max;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getSource() {
		return source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getTarget() {
		return target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RelationType getRelationType() {
		return relationType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute getAttribute() {
		if (attribute == null) {
			attribute = new ODSAttribute(getSource(),
					new ApplAttr(getName(), "", DataType.DT_LONGLONG, 0, true, false, null), null, null);
		}

		return attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getSource(), getTarget(), getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ODSRelation)) {
			return false;
		}
		Relation relation = (Relation) object;
		return getSource().equals(relation.getSource()) && getTarget().equals(relation.getTarget())
				&& getName().equals(relation.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOutgoing(RelationType relationType) {
		return relationType == getRelationType() && rangeMax == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIncoming(RelationType relationType) {
		return relationType == getRelationType() && rangeMax == -1;
	}

}
