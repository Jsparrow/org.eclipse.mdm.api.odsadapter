/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InsertStatement {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertStatement.class);

	private final ODSTransaction transaction;

	private final EntityType entityType;

	private final Method getCoreMethod;

	private final List<EntityCore> entityCores = new ArrayList<>();
	private Map<String, List<Value>> insertMap = new HashMap<>();

	public InsertStatement(ODSTransaction transaction, EntityType entityType) {
		this.transaction = transaction;
		this.entityType = entityType;

		try {
			getCoreMethod = BaseEntity.class.getDeclaredMethod("getCore");
			getCoreMethod.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/*
	 *  TODO add further utility methods
	 *  - add(List<Entity>)
	 *  - add(List<Core>)
	 *
	 *
	 *  we should omit the usage of Core in public methods, since we have to check instanceof Sortable...
	 */

	public void add(Entity entity) {

		/*
		 * TODO
		 *
		 * this is currently done to be able to update the uri of the persisted entities
		 */
		try {
			add((EntityCore) getCoreMethod.invoke(entity));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(EntityCore entityCore) {
		if(!entityCore.getURI().getTypeName().equals(entityType.getName())) {
			throw new IllegalArgumentException("Given entity core '" + entityCore.getURI().getTypeName()
					+ "' is incompatible with current insert statement for entity type '" + entityType + "'.");
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
		for(Relation relation : entityType.getInfoRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// define "empty" values for parent relation
		Optional<Relation> parentRelation = entityType.getParentRelation();
		if(parentRelation.isPresent()) {
			Relation relation = parentRelation.get();
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(entityCore.getInfoRelations().values());
		setRelationIDs(entityCore.getImplicitRelations().values());
	}

	public List<Long> execute() throws DataAccessException {
		try {
			List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
			T_LONGLONG aID = ((ODSEntityType) entityType).getODSID();

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
			ElemId[] elemIds = transaction.getApplElemAccess().insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
			long stop = System.currentTimeMillis();

			LOGGER.debug("{} " + entityType + " instances created in {} ms.", elemIds.length, stop - start);
			return extractInstanceIDs(elemIds);
			// TODO should we track given cores and replace the URIs as soon as we have instance IDs?
			// if so, then we can change this method's return type to void!
		} catch(AoException aoe) {
			throw new DataAccessException("Unable to create new instances due to: " + aoe.reason, aoe);
		}

		/*
		 * TODO exceptional cases:
		 * - instance ID of created ContextRoot entities must be updated in ContextRoot.Version! MDM4...
		 * - others?
		 */
	}

	// TODO duplicate of UpdateStatement.setRelationIDs
	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for(Entity relatedEntity : relatedEntities) {
			Relation relation = entityType.getRelation(transaction.getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = insertMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation + "' is incompatible with insert statement for entity type '" + entityType + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getURI().getID());
		}
	}

	private List<Long> extractInstanceIDs(ElemId[] elemIds) {
		List<Long> instanceIDs = new ArrayList<>();

		for(int i = 0; i < elemIds.length; i++) {
			long instanceID = ODSConverter.fromODSLong(elemIds[i].iid);
			entityCores.get(i).setURI(new URI(entityType.getSourceName(), entityType.getName(), instanceID));
			instanceIDs.add(instanceID);
		}

		return instanceIDs;
	}

}
