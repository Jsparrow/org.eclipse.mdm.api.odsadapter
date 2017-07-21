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

/**
 * ODS implementation of the {@link Relation} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class ODSRelation implements Relation {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Relationship relationship;
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
		relationship = ODSUtils.RELATIONSHIPS.revert(applRel.arRelationType);
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
	public Relationship getRelationship() {
		return relationship;
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
		if (object instanceof ODSRelation) {
			Relation relation = (Relation) object;
			return getSource().equals(relation.getSource()) && getTarget().equals(relation.getTarget())
					&& getName().equals(relation.getName());
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	// ======================================================================
	// Package methods
	// ======================================================================

	/**
	 * Checks whether this relation is of the same {@link Relationship} as the
	 * given one and whether the foreign key is in the table of the source
	 * entity type.
	 *
	 * @param relationship
	 *            The {@code Relationship}.
	 * @return Returns {@code true} this relation's {@code Relationship} is
	 *         equal with the given one and it is is an outgoing relation.
	 */
	boolean isOutgoing(Relationship relationship) {
		return relationship.equals(getRelationship()) && rangeMax == 1;
	}

	/**
	 * Checks whether this relation is of the same {@link Relationship} as the
	 * given one and whether the foreign key is in the table of the target
	 * entity type.
	 *
	 * @param relationship
	 *            The {@code Relationship}.
	 * @return Returns {@code true} this relation's {@code Relationship} is
	 *         equal with the given one and it is is an incoming relation.
	 */
	boolean isIncoming(Relationship relationship) {
		return relationship.equals(getRelationship()) && rangeMax == -1;
	}

}
