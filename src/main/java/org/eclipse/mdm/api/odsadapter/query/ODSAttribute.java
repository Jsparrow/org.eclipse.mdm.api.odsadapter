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

final class ODSAttribute implements Attribute {

	private final Class<? extends Enum<?>> enumClass;
	private final String name;
	private final EntityType entityType;
	private final ValueType valueType;

	ODSAttribute(EntityType entityType, ApplAttr applAttr, Class<? extends Enum<?>> enumClass) {
		this.entityType = entityType;
		name = applAttr.aaName;
		valueType = ODSUtils.VALUETYPES.revert(applAttr.dType);

		if(valueType.isEnumerationType() && enumClass == null) {
			throw new IllegalStateException("A modeled attribute with an enumeration vaue type must have an "
					+ "enumeration definition.");
		}

		this.enumClass = enumClass;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	@Override
	public ValueType getValueType() {
		return valueType;
	}

	@Override
	public Class<? extends Enum<?>> getEnumClass() {
		if(getValueType().isEnumerationType()) {
			return enumClass;
		}

		throw new IllegalStateException("The value type of this attribute is not an enumeration type.");
	}

	@Override
	public int hashCode() {
		return Objects.hash(getEntityType(), getName());
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof ODSAttribute) {
			Attribute attribute = (Attribute) object;
			return getEntityType().equals(attribute.getEntityType()) && getName().equals(attribute.getName());
		}

		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
