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
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * ODS implementation of the {@link Attribute} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class ODSAttribute implements Attribute {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Class<? extends Enum<?>> enumClass;
	private final String name;
	private final String unit;
	private final EntityType entityType;
	private final ValueType valueType;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param entityType
	 *            The parent {@link EntityType}.
	 * @param applAttr
	 *            The ODS meta data for this attribute.
	 * @param unit
	 *            The unit name.
	 * @param enumClass
	 *            The enumeration class, may be null.
	 */
	ODSAttribute(EntityType entityType, ApplAttr applAttr, String unit, Class<? extends Enum<?>> enumClass) {
		this.entityType = entityType;
		name = applAttr.aaName;
		this.unit = unit == null ? "" : unit;
		if ("id".equalsIgnoreCase(applAttr.baName))
		{
			valueType = ValueType.STRING;
		}
		else
		{
			valueType = ODSUtils.VALUETYPES.revert(applAttr.dType);
		}

		if (valueType.isEnumerationType() && enumClass == null) {
			throw new IllegalStateException(
					"A modeled attribute with an enumeration vaue type must have an " + "enumeration definition.");
		}

		this.enumClass = enumClass;
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
	public String getUnit() {
		return unit;
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
	public ValueType getValueType() {
		return valueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getEnumClass() {
		if (getValueType().isEnumerationType()) {
			return enumClass;
		}

		throw new IllegalStateException("The value type of this attribute is not an enumeration type.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getEntityType(), getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof ODSAttribute) {
			Attribute attribute = (Attribute) object;
			return getEntityType().equals(attribute.getEntityType()) && getName().equals(attribute.getName());
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

}
