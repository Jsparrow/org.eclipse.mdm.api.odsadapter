/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ElemId;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public final class InsertStatement {

	private final ODSTransaction transaction;
	private final EntityType entityType;

	private Map<String, List<Value>> insertMap = new HashMap<>();

	public InsertStatement(ODSTransaction transaction, EntityType entityType) {
		this.transaction = transaction;
		this.entityType = entityType;
	}

	/*
	 *  TODO add further utility methods
	 *  - add(List<DataItem>)
	 *  - add(List<Core>)
	 *
	 *
	 *  we should omit the usage of Core in public methods, since we have to check instanceof Sortable...
	 */

	public void add(Core core, DataItem... implicitlyRelated) {
		if(!core.getTypeName().equals(entityType.getName())) {
			throw new IllegalArgumentException("Given data item core '" + core.getTypeName()
			+ "' is incompatible with current insert statement for entity type '" + entityType + "'.");
		}

		// TODO we need custom behavior for sortable data items
		// - if sortable has parent -> find max sort index out of sibling entities
		// - if parent does not exist -> find max sort index of all entities

		// add all data item values
		for(Value value : core.getValues().values()) {
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
		setRelationIDs(core.getInfoRelations().values());
		setRelationIDs(Arrays.asList(implicitlyRelated));
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
			ElemId[] elemIds = transaction.getApplElemAccess().insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
			return extractInstanceIDs(elemIds);
			// TODO should we track given cores and replace the URIs as soon as we have instance IDs?
			// if so, then we can change this method's return type to void!
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}

		/*
		 * TODO exceptional cases:
		 * - instance ID of created ContextRoot data items must be updated in ContextRoot.Version! MDM4...
		 * - others?
		 */
	}

	// TODO duplicate of UpdateStatement.setRelationIDs
	private void setRelationIDs(Collection<DataItem> relatedDataItems) {
		for(DataItem relatedDataItem : relatedDataItems) {
			Relation relation = entityType.getRelation(transaction.getModelManager().getEntityType(relatedDataItem));
			List<Value> relationValues = insertMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation + "' is incompatible with insert statement for entity type '" + entityType + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedDataItem.getURI().getID());
		}
	}

	private List<Long> extractInstanceIDs(ElemId[] elemIds) {
		return Arrays.stream(elemIds).map(e -> ODSConverter.fromODSLong(e.iid)).collect(Collectors.toList());
	}

}
