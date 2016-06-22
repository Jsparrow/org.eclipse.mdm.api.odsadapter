/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ElemId;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InsertStatement extends BaseStatement {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertStatement.class);

	private final Map<Class<? extends Entity>, List<Entity>> childrenMap = new HashMap<>();
	private final List<Core> cores = new ArrayList<>();
	private final Map<String, List<Value>> insertMap = new HashMap<>();

	InsertStatement(ODSTransaction transaction, EntityType entityType) {
		super(transaction, entityType);
	}

	@Override
	public void execute(Collection<Entity> entities) throws AoException, DataAccessException {
		entities.forEach(e -> readEntityCore(extract(e)));
		execute();
	}

	public void executeWithCores(Collection<Core> cores) throws AoException, DataAccessException {
		cores.forEach(this::readEntityCore);
		execute();
	}

	private void execute() throws AoException, DataAccessException {
		List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
		T_LONGLONG aID = getEntityType().getODSID();

		for(Entry<String, List<Value>> entry : insertMap.entrySet()) {
			AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
			anvsu.attr = new AIDName(aID, entry.getKey());
			anvsu.unitId = ODSConverter.toODSLong(0);
			anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
			anvsuList.add(anvsu);
		}

		long start = System.currentTimeMillis();
		ElemId[] elemIds = getApplElemAccess().insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		writeInstanceIDs(elemIds);
		long stop = System.currentTimeMillis();

		LOGGER.debug("{} " + getEntityType() + " instances created in {} ms.", elemIds.length, stop - start);

		// URIs have to be up to date before children may be created!
		for(List<Entity> children : childrenMap.values()) {
			getTransaction().create(children);
		}
	}

	private void writeInstanceIDs(ElemId[] elemIds) {
		for(int i = 0; i < elemIds.length; i++) {
			long instanceID = ODSConverter.fromODSLong(elemIds[i].iid);
			cores.get(i).setID(instanceID);
		}
	}

	private void readEntityCore(Core core) {
		if(!core.getTypeName().equals(getEntityType().getName())) {
			throw new IllegalArgumentException("Given entity core '" + core.getTypeName()
			+ "' is incompatible with current insert statement for entity type '" + getEntityType() + "'.");
		}

		cores.add(core);

		// add all entity values
		for(Value value : core.getAllValues().values()) {
			/*
			 * TODO: in case of ContextComponent instances we have to
			 * add missing Value containers (this are those we have
			 * omitted due to the underlying template component)
			 */

			// TODO scan for values with file links and collect them!
			insertMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value);
		}

		// define "empty" values for informative relations
		for(Relation relation : getEntityType().getInfoRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// define "empty" values for parent relations
		for(Relation relation : getEntityType().getParentRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(core.getMutableStore().getCurrent());
		setRelationIDs(core.getPermanentStore().getCurrent());

		for(Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : core.getChildrenStore().getCurrent().entrySet()) {
			childrenMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
		}

		getTransaction().addCore(core);
	}

	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for(Entity relatedEntity : relatedEntities) {
			if(relatedEntity.getID() < 1) {
				throw new IllegalArgumentException("Related entity must be a persited entity.");
			}

			Relation relation = getEntityType().getRelation(getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = insertMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation + "' is incompatible with insert statement "
						+ "for entity type '" + getEntityType() + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getID());
		}
	}

}
