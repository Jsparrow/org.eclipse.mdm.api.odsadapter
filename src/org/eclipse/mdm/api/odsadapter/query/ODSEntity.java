/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.ApplAttr;
import org.asam.ods.ApplElem;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Entity;

public final class ODSEntity implements Entity {

	private final Map<String, Attribute> attributeByName = new HashMap<String, Attribute>();
	private final ApplElem applElem;
	
	public ODSEntity(ApplElem applElem) throws AoException {
		this.applElem = applElem;
		for(ApplAttr applAttr : applElem.attributes) {
			Attribute attribute = new ODSAttribute(applAttr, this);
			/**
			 * TODO ODS server produces wrong result rows each time 
			 * an attribute whose type is a sequence type is selected.
			 */
			if(attribute.getType().isSequence()) {
				continue;
			}
			this.attributeByName.put(applAttr.aaName, attribute);
		}
	}
	
	@Override
	public String getName() {
		return this.applElem.aeName;
	}

	@Override
	public List<Attribute> getAttributes() {
		return new ArrayList<>(attributeByName.values());
	}

	@Override
	public Attribute getIDAttribute() {
		return getAttribute(DataItem.ATTR_ID);
	}
	
	public T_LONGLONG getODSID() {
		return applElem.aid;
	}
	
	public Attribute getAttribute(String name) {
		Attribute attribute = this.attributeByName.get(name);
		if(attribute == null) {
			throw new IllegalArgumentException("attribute with name '" + name 
				+ "' not found at entity '" + getName() + "'");
		}
		return attribute;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
