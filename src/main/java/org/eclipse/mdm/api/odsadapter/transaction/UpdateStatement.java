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
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public final class UpdateStatement {

	private final ODSTransaction transaction;
	private final EntityType entityType;

	private Map<String, List<Value>> updateMap = new HashMap<>();

	private final List<Relation> updatableRelations;

	public UpdateStatement(ODSTransaction transaction, EntityType entityType) {
		this.transaction = transaction;
		this.entityType = entityType;

		updatableRelations = transaction.getModelManager().getRelatedTypes(entityType.getName())
				.stream().map(entityType::getRelation).collect(Collectors.toList());
	}

	/*
	 *  TODO add further utility methods
	 *  - add(List<Entity>)
	 *  - add(List<Core>)
	 *
	 *
	 *  we should omit the usage of Core in public methods, since we have to check instanceof Versionable...
	 */

	public void add(Entity entity) {
		if(!entity.getURI().getTypeName().equals(entityType.getName())) {
			throw new IllegalArgumentException("Given entity core '" + entity.getURI().getTypeName()
					+ "' is incompatible with current update statement for entity type '" + entityType + "'.");
		}

		// TODO we need custom behavior for versionable entities
		// they are only allowed to be updated if the VersionState transition is one of:
		// - EDITABLE -> VALID ==> modification of other fields is allowed
		// - VALID -> ARCHIVED ==> modification of other fields is not allowed!

		// add all entity values
		for(Value value : entity.getValues().values()) {
			// TODO scan for values with file links and collect them!
			updateMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value);
		}

		updateMap.computeIfAbsent(entityType.getIDAttribute().getName(), k -> new ArrayList<>())
		.add(entityType.getIDAttribute().createValue(entity.getURI().getID()));

		// define "empty" values for informative relations
		for(Relation relation : updatableRelations) {
			updateMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(entity.getInfoRelations());
	}

	public void execute() throws DataAccessException {
		try {
			List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
			T_LONGLONG aID = ((ODSEntityType) entityType).getODSID();

			for(Entry<String, List<Value>> entry : updateMap.entrySet()) {
				AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
				anvsu.attr = new AIDName(aID, entry.getKey());
				anvsu.unitId = ODSConverter.toODSLong(0);
				anvsu.values = ODSConverter.toODSValueSeq(entry.getValue());
				anvsuList.add(anvsu);
			}

			transaction.getApplElemAccess().updateInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			/*
			 * TODO implicitly removed children maps of updated entities (ContextRoot, etc) have to be cleared!
			 * we should collect them here and leave this to the transaction -> on successful commit
			 * in case of an abort all present entity core meta data should remain unchanged!
			 *
			 * - do we need something similar for Insert- & UpdateStatement?!
			 */
		}
	}

	// TODO duplicate of InsertStatement.setRelationIDs
	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for(Entity relatedEntity : relatedEntities) {
			Relation relation = entityType.getRelation(transaction.getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = updateMap.get(relation.getName());
			if(relationValues == null) {
				throw new IllegalStateException("Relation '" + relation
						+ "' is incompatible with update statement for entity type '" + entityType + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getURI().getID());
		}
	}

}
