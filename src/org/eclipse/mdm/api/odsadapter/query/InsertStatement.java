/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
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
import java.util.Optional;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ElemId;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;



public class InsertStatement {

	private final ApplElemAccess applElemAccess;
	private final Entity entity;

	private Map<String, List<Value>> insertMap = new HashMap<String, List<Value>>();
	private Map<String, String> referenceAttributeMap = new HashMap<String, String>();

	private int position = -1;


	public InsertStatement(ApplElemAccess applElemAccess, Entity entity) {
		this.applElemAccess = applElemAccess;
		this.entity = entity;
		initialize();
	}



	public int next() {
		position ++;
		createValueRow();
		return position;
	}



	public void insertValue(Value value) throws DataAccessException {
		if(position < 0) {
			next();
		}

		String attributeName = value.getName();
		if(referenceAttributeMap.containsKey(value.getName())) {
			attributeName = referenceAttributeMap.get(value.getName());
		}

		if(!insertMap.containsKey(attributeName)) {
			throw new DataAccessException("unknown attribute name or reference name '"
					+ attributeName + "' for entity '" + entity + "'");
		}
		insertMap.get(attributeName).set(position, value);
	}


	public void insertValues(Collection<Value> values) throws DataAccessException {
		for(Value value : values) {
			insertValue(value);
		}
	}


	public List<Long> execute() throws DataAccessException {
		try {
			List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<AIDNameValueSeqUnitId>();
			T_LONGLONG aID = ((ODSEntity)entity).getODSID();

			for(Entry<String, List<Value>> entry : insertMap.entrySet()) {

				if(entry.getKey().equalsIgnoreCase(entity.getIDAttribute().getName())) {
					continue;
				}

				AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
				anvsu.attr = new AIDName(aID, entry.getKey());
				anvsu.unitId = ODSConverter.toODSLong((long)0);
				anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
				anvsuList.add(anvsu);
			}
			ElemId[] elemIds = applElemAccess.insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
			return extractInstanceIDs(elemIds);
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}

	}



	private void createValueRow() {
		for(Attribute attribute : entity.getAttributes()) {
			Value emptyValue = attribute.getType().emptyValue(attribute.getName());
			insertMap.get(attribute.getName()).add(emptyValue);
		}

		Optional<Relation> parentRelation = entity.getParentRelation();
		if(parentRelation.isPresent()) {
			Value emptyValue = ValueType.LONG.emptyValue(parentRelation.get().getName());
			insertMap.get(parentRelation.get().getName()).add(emptyValue);
		}

		List<Relation> infoRelations = entity.getInfoRelations();
		for(Relation infoRelation : infoRelations) {
			Value emptyValue = ValueType.LONG.emptyValue(infoRelation.getName());
			insertMap.get(infoRelation.getName()).add(emptyValue);
		}
	}



	private void initialize() {

		for(Attribute attribute : entity.getAttributes()) {
			insertMap.put(attribute.getName(), new ArrayList<Value>());
		}

		Optional<Relation> parentRelation = entity.getParentRelation();
		if(parentRelation.isPresent()) {
			referenceAttributeMap.put(parentRelation.get().getTarget().getName(), parentRelation.get().getName());
			insertMap.put(parentRelation.get().getName(), new ArrayList<Value>());
		}

		List<Relation> infoRelations = entity.getInfoRelations();
		for(Relation infoRelation : infoRelations) {
			referenceAttributeMap.put(infoRelation.getTarget().getName(), infoRelation.getName());
			insertMap.put(infoRelation.getName(), new ArrayList<Value>());
		}
	}

	private List<Long> extractInstanceIDs(ElemId[] elemIds) {
		List<Long> list = new ArrayList<Long>();
		for(ElemId elemId : elemIds) {
			list.add(ODSConverter.fromODSLong(elemId.iid));
		}
		return list;
	}

}
