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
import java.util.stream.Collectors;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
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

final class UpdateStatement extends BaseStatement {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStatement.class);

	private final Map<Class<? extends Entity>, List<Entity>> childrenToCreate = new HashMap<>();
	private final Map<Class<? extends Entity>, List<Entity>> childrenToUpdate = new HashMap<>();
	private final Map<Class<? extends Deletable>, List<Deletable>> childrenToRemove = new HashMap<>();
	private Map<String, List<Value>> updateMap = new HashMap<>();
	private final List<URI> uris = new ArrayList<>();
	private final List<Relation> updatableRelations;

	protected UpdateStatement(ODSTransaction transaction, EntityType entityType) {
		super(transaction, entityType);

		updatableRelations = getModelManager().getRelatedTypes(entityType.getName())
				.stream().map(entityType::getRelation).collect(Collectors.toList());
	}

	@Override
	public List<URI> execute(Collection<Entity> entities) throws AoException, DataAccessException {
		entities.forEach(e -> readEntityCore(extract(e)));
		return execute();
	}

	private List<URI> execute() throws AoException, DataAccessException {
		List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
		T_LONGLONG aID = getEntityType().getODSID();

		for(Entry<String, List<Value>> entry : updateMap.entrySet()) {
			AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
			anvsu.attr = new AIDName(aID, entry.getKey());
			anvsu.unitId = ODSConverter.toODSLong(0);
			anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
			anvsuList.add(anvsu);
		}

		long start = System.currentTimeMillis();
		getApplElemAccess().updateInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		long stop = System.currentTimeMillis();

		LOGGER.debug("{} " + getEntityType() + " instances updated in {} ms.", uris.size(), stop - start);

		// delete first to make sure naming collisions do not occur!
		for(List<Deletable> children : childrenToRemove.values()) {
			getTransaction().delete(children);
		}
		for(List<Entity> children : childrenToCreate.values()) {
			getTransaction().create(children);
		}
		for(List<Entity> children : childrenToUpdate.values()) {
			getTransaction().update(children);
		}

		return uris;
	}

	private void readEntityCore(EntityCore entityCore) {
		if(!entityCore.getURI().getTypeName().equals(getEntityType().getName())) {
			throw new IllegalArgumentException("Given entity core '" + entityCore.getURI().getTypeName()
					+ "' is incompatible with current update statement for entity type '" + getEntityType() + "'.");
		}

		// TODO we need custom behavior for versionable entities
		// they are only allowed to be updated if the VersionState transition is one of:
		// - EDITABLE -> VALID ==> modification of other fields is allowed
		// - VALID -> ARCHIVED ==> modification of other fields is not allowed!

		// add all entity values
		for(Value value : entityCore.getValues().values()) {
			// TODO scan for values with file links and collect them!
			updateMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value);
		}

		updateMap.computeIfAbsent(getEntityType().getIDAttribute().getName(), k -> new ArrayList<>())
		.add(getEntityType().getIDAttribute().createValue(entityCore.getURI().getID()));

		// define "empty" values for informative relations
		for(Relation relation : updatableRelations) {
			updateMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(entityCore.getInfoRelations().values());

		uris.add(entityCore.getURI());

		collectChildEntities(entityCore);
	}

	private void collectChildEntities(EntityCore entityCore) {
		for (Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : entityCore.getChildren().entrySet()) {
			Map<Boolean, List<Entity>> patrition = entry.getValue().stream().collect(Collectors.partitioningBy(e -> e.getURI().getID() < 1));
			List<Entity> virtualEntities = patrition.get(Boolean.TRUE);
			if(virtualEntities != null && !virtualEntities.isEmpty()) {
				childrenToCreate.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(virtualEntities);
			}
			List<Entity> existingEntities = patrition.get(Boolean.FALSE);
			if(existingEntities != null && !existingEntities.isEmpty()) {
				childrenToUpdate.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(existingEntities);
			}
		}

		for (Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : entityCore.getRemovedChildren().entrySet()) {
			List<Deletable> toDelete = entry.getValue().stream().filter(e -> e.getURI().getID() > 0).collect(Collectors.toList());
			childrenToRemove.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(toDelete);
		}
	}

	// TODO duplicate of InsertStatement.setRelationIDs
	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for(Entity relatedEntity : relatedEntities) {
			Relation relation = getEntityType().getRelation(getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = updateMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation
						+ "' is incompatible with update statement for entity type '" + getEntityType() + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getURI().getID());
		}
	}

}
