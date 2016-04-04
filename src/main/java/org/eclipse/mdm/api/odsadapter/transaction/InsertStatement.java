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
import java.util.Optional;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ElemId;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.URI;
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
	private final List<EntityCore> entityCores = new ArrayList<>();
	private final Map<String, List<Value>> insertMap = new HashMap<>();

	protected InsertStatement(ODSTransaction transaction, EntityType entityType) {
		super(transaction, entityType);
	}

	@Override
	public List<URI> execute(Collection<Entity> entities) throws AoException, DataAccessException {
		entities.forEach(e -> readEntityCore(extract(e)));
		return execute();
	}

	public List<URI> executeWithCores(Collection<EntityCore> entityCores) throws AoException, DataAccessException {
		entityCores.forEach(this::readEntityCore);
		return execute();
	}

	private List<URI> execute() throws AoException, DataAccessException {
		List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
		T_LONGLONG aID = getEntityType().getODSID();

		for(Entry<String, List<Value>> entry : insertMap.entrySet()) {
			AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
			anvsu.attr = new AIDName(aID, entry.getKey());
			anvsu.unitId = ODSConverter.toODSLong(0);

			//TODO Value enhancement - LocalColumn.Value will contain an array box!
			// each values entry is bound to a sequence type

			anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
			anvsuList.add(anvsu);
		}

		long start = System.currentTimeMillis();
		ElemId[] elemIds = getApplElemAccess().insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		long stop = System.currentTimeMillis();

		LOGGER.debug("{} " + getEntityType() + " instances created in {} ms.", elemIds.length, stop - start);

		List<URI> uris = updateURIs(elemIds);
		// URIs have to be up to date before children may be created!
		for(List<Entity> children : childrenMap.values()) {
			getTransaction().create(children);
		}

		/*
		 * TODO exceptional cases:
		 * - instance ID of created ContextRoot entities must be updated in ContextRoot.Version! MDM4...
		 * - others?
		 */

		return uris;
	}

	private List<URI> updateURIs(ElemId[] elemIds) {
		List<URI> uris = new ArrayList<>();

		for(int i = 0; i < elemIds.length; i++) {
			long instanceID = ODSConverter.fromODSLong(elemIds[i].iid);
			URI uri = new URI(getEntityType().getSourceName(), getEntityType().getName(), instanceID);
			entityCores.get(i).setURI(uri);
			uris.add(uri);
		}

		return uris;
	}

	private void readEntityCore(EntityCore entityCore) {
		if(!entityCore.getURI().getTypeName().equals(getEntityType().getName())) {
			throw new IllegalArgumentException("Given entity core '" + entityCore.getURI().getTypeName()
					+ "' is incompatible with current insert statement for entity type '" + getEntityType() + "'.");
		}

		entityCores.add(entityCore);

		// TODO we need custom behavior for sortable entities
		// - if sortable has parent -> find max sort index out of sibling entities
		// - if parent does not exist -> find max sort index of all entities

		// add all entity values
		for(Value value : entityCore.getValues().values()) {
			// TODO scan for values with file links and collect them!
			insertMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value);
		}

		// define "empty" values for informative relations
		for(Relation relation : getEntityType().getInfoRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// define "empty" values for parent relation
		Optional<Relation> parentRelation = getEntityType().getParentRelation();
		if(parentRelation.isPresent()) {
			Relation relation = parentRelation.get();
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(entityCore.getInfoRelations().values());
		setRelationIDs(entityCore.getImplicitRelations().values());

		for(Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : entityCore.getChildren().entrySet()) {
			childrenMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
		}
	}

	// TODO duplicate of UpdateStatement.setRelationIDs
	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for(Entity relatedEntity : relatedEntities) {
			Relation relation = getEntityType().getRelation(getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = insertMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation + "' is incompatible with insert statement "
						+ "for entity type '" + getEntityType() + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getURI().getID());
		}
	}

}
