/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public class UpdateStatement {

	private final ApplElemAccess applElemAccess;
	private final Entity entity;
	private final long id;

	private Map<String, List<Value>> updateMap = new HashMap<String, List<Value>>();
	private Map<String, String> referenceAttributeMap = new HashMap<String, String>();

	private int position = -1;


	public UpdateStatement(ApplElemAccess applElemAccess, Entity entity) {
		this.applElemAccess = applElemAccess;
		this.entity = entity;
		id = 0;
		initialize();
	}


	public UpdateStatement(ApplElemAccess applElemAccess, Entity entity, long id) {
		this.applElemAccess = applElemAccess;
		this.entity = entity;
		this.id = id;
		initialize();
	}



	public int next(long id) {
		position ++;
		createValueRow(id);
		return position;
	}



	public void setValue(Value value) throws DataAccessException {

		if(id <= 0 && position < 0) {
			throw new DataAccessException("no id set for single update statement"
					+ " (use the next-method or the other contructor)");
		}

		if(position < 0) {
			next(id);
		}

		String attributeName = value.getName();
		if(referenceAttributeMap.containsKey(value.getName())) {
			attributeName = referenceAttributeMap.get(value.getName());
		}

		if(!updateMap.containsKey(attributeName)) {
			throw new DataAccessException("unknown attribute name or reference name '"
					+ attributeName + "' for entity '" + entity + "'");
		}

		updateMap.get(attributeName).set(position, value);
	}



	public void setValues(Collection<Value> values) throws DataAccessException {
		for(Value value : values) {
			setValue(value);
		}
	}



	public void execute() throws DataAccessException {
		try {
			List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<AIDNameValueSeqUnitId>();
			T_LONGLONG aID = ((ODSEntity)entity).getODSID();

			for(Entry<String, List<Value>> entry : updateMap.entrySet()) {
				AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
				anvsu.attr = new AIDName(aID, entry.getKey());
				anvsu.unitId = ODSConverter.toODSLong((long)0);
				anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
				anvsuList.add(anvsu);
			}

			applElemAccess.updateInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}

	}



	private void createValueRow(long id) {
		for(Attribute attribute : entity.getAttributes()) {
			Value emptyValue = attribute.getType().emptyValue(attribute.getName());
			updateMap.get(attribute.getName()).add(emptyValue);
		}

		List<Relation> infoRelations = entity.getInfoRelations();
		for(Relation infoRelation : infoRelations) {
			Value emptyValue = ValueType.LONG.emptyValue(infoRelation.getName());
			updateMap.get(infoRelation.getName()).add(emptyValue);
		}

		String idAttributeName = entity.getIDAttribute().getName();
		Value idValue = ValueType.LONG.newValue(idAttributeName, id);
		updateMap.get(idAttributeName).set(position, idValue);
	}



	private void initialize() {
		for(Attribute attribute : entity.getAttributes()) {
			updateMap.put(attribute.getName(), new ArrayList<Value>());
		}

		List<Relation> infoRelations = entity.getInfoRelations();
		for(Relation infoRelation : infoRelations) {
			referenceAttributeMap.put(infoRelation.getTarget().getName(), infoRelation.getName());
			updateMap.put(infoRelation.getName(), new ArrayList<Value>());
		}
	}

}
