/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.asam.ods.ApplAttr;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Enumeration;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * ODS implementation of the {@link Attribute} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class ODSAttribute implements Attribute {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Enumeration<?> enumObj;
	private final String name;
	private final String unit;
	private final EntityType entityType;
	private final ValueType<?> valueType;
	private final boolean isIdAttribute;

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
	 * @param enumObj
	 *            The enumeration class, may be null.
	 */
	ODSAttribute(EntityType entityType, ApplAttr applAttr, String unit, Enumeration<?> enumObj) {
		this.entityType = entityType;
		name = applAttr.aaName;
		this.unit = unit == null ? "" : unit;

		if (isIDAttribute(entityType, applAttr)) {
			valueType = ValueType.STRING;
			isIdAttribute = true;
		} else {
			valueType = ODSUtils.VALUETYPES.inverse().get(applAttr.dType);
			isIdAttribute = false;
		}

		if (valueType.isEnumerationType() && enumObj == null) {
			throw new IllegalStateException(
					"A modeled attribute with an enumeration value type must have an " + "enumeration definition.");
		}

		this.enumObj = enumObj;
	}

	private boolean isIDAttribute(EntityType entityType, ApplAttr applAttr) {
		for (Relation r : entityType.getRelations()) {
			if (applAttr.aaName.equalsIgnoreCase(r.getName())) {
				return true;
			}
		}
		return "id".equalsIgnoreCase(applAttr.baName);
	}

	public boolean isIdAttribute() {
		return isIdAttribute;
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
	public ValueType<?> getValueType() {
		return valueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<?> getEnumObj() {
		if (getValueType().isEnumerationType()) {
			return enumObj;
		}

		throw new IllegalStateException("The value type of this attribute is not an enumeration type.");
	}

	@Override
	public Value createValue(String unit, boolean valid, Object input) {
		return Attribute.super.createValue(unit, valid, convertInputForIdAttribute(input));
	}

	@Override
	public Value createValueSeq(String unit, Object input) {
		return Attribute.super.createValueSeq(unit, convertInputForIdAttribute(input));
	}

	/**
	 * Converts the input object from long/long-array/int/int-array to a
	 * String/String-array
	 * 
	 * @param input
	 *            The input to convert
	 * @return The converted input
	 */
	private Object convertInputForIdAttribute(Object input) {
		if (isIdAttribute) {
			if (input instanceof Long || input instanceof Integer) {
				return input.toString();
			} else if (input instanceof long[]) {
				return LongStream.of((long[]) input).mapToObj(Long::toString).toArray(String[]::new);
			} else if (input instanceof int[]) {
				return IntStream.of((int[]) input).mapToObj(Integer::toString).toArray(String[]::new);
			}
		}

		return input;
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
