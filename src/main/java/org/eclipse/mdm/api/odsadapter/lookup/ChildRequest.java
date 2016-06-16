package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

final class ChildRequest<T extends Deletable> extends EntityRequest<T> {

	private final EntityRequest<?> parent;

	ChildRequest(EntityRequest<?> parentRequest, EntityConfig<T> entityConfig) {
		super(parentRequest, entityConfig);
		parent = parentRequest;
	}

	public EntityResult<T> load() throws DataAccessException {
		/*
		 * ########################################################
		 */
		//System.err.println(parent.entityConfig.getEntityType() + "." + entityConfig.getEntityType());
		/*
		 * ########################################################
		 */
		filtered = parent.filtered;

		EntityType entityType = entityConfig.getEntityType();
		Relation parentRelation = entityConfig.getEntityType().getRelation(parent.entityConfig.getEntityType());
		Relation reflexiveRelation = entityConfig.isReflexive() ? entityType.getRelation(entityType) : null;

		Query query = modelManager.createQuery()
				// select entity attributes
				.selectAll(entityConfig.getEntityType())
				// select parent entity ID
				.select(parentRelation.getAttribute());

		if(entityConfig.isReflexive()) {
			query.select(reflexiveRelation.getAttribute());
			// entities with children have to be processed before their children!
			query.order(entityType.getIDAttribute());
		}

		// prepare relations select statements
		List<RelationConfig> optionalRelations = selectRelations(query, entityConfig.getOptionalConfigs(), false);
		List<RelationConfig> mandatoryRelations = selectRelations(query, entityConfig.getMandatoryConfigs(), true);
		List<RelationConfig> inheritedRelations = selectRelations(query, entityConfig.getInheritedConfigs(), true);

		// configure filter
		Filter adjustedFilter = Filter.or();
		if(filtered) {
			// preserve current conditions
			adjustedFilter.add(createRelationCondition(parentRelation, parent.entityResult.getIDs()));
			if(entityConfig.isReflexive()) {
				// extend to retrieve all reflexive child candidates
				adjustedFilter.add(Operation.IS_NOT_NULL.create(reflexiveRelation.getAttribute(), 0L));
			}
		}

		// load entities and prepare mappings for required related entities
		List<EntityRecord<?>> parentRecords = new ArrayList<>();
		for(Record record : collectRecords(query.fetch(adjustedFilter))) {
			Optional<Long> parentID = record.getID(parentRelation);
			Optional<Long> reflexiveParentID = entityConfig.isReflexive() ? record.getID(reflexiveRelation) : Optional.empty();
			EntityRecord<T> entityRecord;

			if(parentID.isPresent()) {
				EntityResult<?> parentResult = parent.entityResult;
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Optional<EntityRecord<?>> parentRecord = (Optional) parentResult.get(parentID.get());
				if(!parentRecord.isPresent()) {
					// TODO LOG WARNING: related parent entity does not exist!
					continue;
				}

				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else if(entityConfig.isReflexive() && reflexiveParentID.isPresent()) {
				Optional<EntityRecord<T>> parentRecord = entityResult.get(reflexiveParentID.get());
				if(!parentRecord.isPresent()) {
					// this entity's parent was not loaded -> skip
					continue;
				}
				// reflexive child
				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else {
				throw new IllegalStateException(); // TODO entity without parent
			}

			Stream.concat(Stream.concat(optionalRelations.stream(), mandatoryRelations.stream()), inheritedRelations.stream()).forEach(rc -> rc.add(entityRecord, record));
		}

		if(entityResult.isEmpty()) {
			// no entities found -> neither related nor child entities required
			return entityResult;
		}

		// load and map related entities
		loadRelatedEntities(optionalRelations);
		loadRelatedEntities(mandatoryRelations);
		assignRelatedEntities(inheritedRelations);

		// sort children
		for(EntityRecord<?> entityRecord : parentRecords) {
			entityRecord.core.getChildrenStore().sort(entityConfig.getEntityClass(), entityConfig.getComparator());
		}

		// load children
		for(EntityConfig<? extends Deletable> childConfig : entityConfig.getChildConfigs()) {
			cache.add(new ChildRequest<>(this, childConfig).load());
		}

		return entityResult;
	}

	private void assignRelatedEntities(List<RelationConfig> relationConfigs) throws DataAccessException {
		for(RelationConfig relationConfig : relationConfigs) {
			EntityConfig<?> relatedConfig = relationConfig.entityConfig;

			EntityResult<?> cachedResult = cache.get(relatedConfig);
			if(cachedResult == null) {
				throw new IllegalStateException(); // TODO cached result not found!
			}

			boolean isContextTypeDefined = entityConfig.getContextType().isPresent();
			// TODO: iterate over Map id to entity !
			for(Entity relatedEntity : cachedResult.getEntities()) {
				boolean setByContextType = !isContextTypeDefined && relatedConfig.getContextType().isPresent();
				List<EntityRecord<?>> entityRecords = relationConfig.dependants.remove(relatedEntity.getID());
				// TODO entityRecords may be null!
				for(EntityRecord<?> entityRecord : entityRecords == null ? new ArrayList<EntityRecord<?>>() : entityRecords) {
					setRelatedEntity(entityRecord, relatedEntity,  setByContextType ? relatedConfig.getContextType().get() : null);
				}
			}

			if(!relationConfig.dependants.isEmpty()) {
				// TODO: in case of optional -> LOG Warning otherwise throw IllegalStateException!

				// this may occur if the instance id of the related entity
				// is defined, but the entity itself does not exist
				throw new IllegalStateException("Unable to load related entities.");
			}
		}
	}
}
