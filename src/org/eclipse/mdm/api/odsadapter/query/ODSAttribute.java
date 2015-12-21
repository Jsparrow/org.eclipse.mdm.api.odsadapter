/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import org.asam.ods.ApplAttr;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSAttribute implements Attribute {

	private final ApplAttr applAttr;
	private final Entity entity;
	private final ValueType valueType;

	public ODSAttribute(ApplAttr applAttr, Entity entity) {
		this.applAttr = applAttr;
		this.entity = entity;
		valueType = ODSUtils.VALUETYPES.revert(applAttr.dType);
	}

	@Override
	public String getName() {
		return applAttr.aaName;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public ValueType getType() {
		return valueType;
	}

	@Override
	public String toString() {
		return getName();
	}

}
