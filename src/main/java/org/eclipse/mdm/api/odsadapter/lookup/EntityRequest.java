package org.eclipse.mdm.api.odsadapter.lookup;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextSensor;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.Condition;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateSensor;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

public class EntityRequest<T extends Entity> {

	final ModelManager modelManager; // TODO

	final EntityConfig<T> entityConfig;
	final EntityResult<T> entityResult = new EntityResult<>(this);

	final Cache cache;

	boolean filtered;

	// public entry point -> global cache is initialized for loaded children
	public EntityRequest(ModelManager modelManager, EntityConfig<T> config) {
		this.modelManager = modelManager;
		this.entityConfig = config;
		cache = new Cache();
	}

	protected EntityRequest(EntityRequest<?> parentRequest, EntityConfig<T> entityConfig) {
		modelManager = parentRequest.modelManager;
		cache = parentRequest.cache;
		this.entityConfig = entityConfig;
	}

	// load instances matched by name pattern
	public List<T> loadAll(String pattern) throws DataAccessException {
		return load(Filter.nameOnly(entityConfig.getEntityType(), pattern)).getSortedEntities();
	}

	// load instances by ids
	public List<T> loadAll(Collection<Long> instanceIDs) throws DataAccessException {
		if(instanceIDs.isEmpty()) {
			// just to be sure...
			return Collections.emptyList();
		}

		return load(Filter.idsOnly(entityConfig.getEntityType(), instanceIDs)).getSortedEntities();
	}

	private EntityResult<T> load(Filter filter) throws DataAccessException {
		/*
		 * ########################################################
		 */
		//System.err.println(entityConfig.getEntityType()); // TODO REMOVE
		/*
		 * ########################################################
		 */
		filtered = !filter.isEmtpty() || entityConfig.isReflexive();

		EntityType entityType = entityConfig.getEntityType();
		Relation reflexiveRelation = entityConfig.isReflexive() ? entityType.getRelation(entityType) : null;

		Query query = modelManager.createQuery().selectAll(entityConfig.getEntityType());

		if(entityConfig.isReflexive()) {
			query.select(reflexiveRelation.getAttribute());
			// entities with children have to be processed before their children!
			query.order(entityType.getIDAttribute());
		}

		// prepare relations select statements
		List<RelationConfig> optionalRelations = selectRelations(query, entityConfig.getOptionalConfigs(), false);
		List<RelationConfig> mandatoryRelations = selectRelations(query, entityConfig.getMandatoryConfigs(), true);

		// configure filter
		Filter adjustedFilter = Filter.or();
		if(filtered) {
			// preserve current conditions
			adjustedFilter.merge(filter);
			if(entityConfig.isReflexive()) {
				// extend to retrieve all reflexive child candidates
				adjustedFilter.add(Operation.IS_NOT_NULL.create(reflexiveRelation.getAttribute(), 0L));
			}
		}

		// load entities and prepare mappings for required related entities
		List<EntityRecord<?>> parentRecords = new ArrayList<>();
		for(Record record : collectRecords(query.fetch(adjustedFilter))) {
			Optional<Long> reflexiveParentID = entityConfig.isReflexive() ? record.getID(reflexiveRelation) : Optional.empty();
			EntityRecord<T> entityRecord;

			if(entityConfig.isReflexive() && reflexiveParentID.isPresent()) {
				Optional<EntityRecord<T>> parentRecord = entityResult.get(reflexiveParentID.get());
				if(!parentRecord.isPresent()) {
					// this entity's parent was not loaded -> skip
					continue;
				}

				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else {
				entityRecord = entityResult.add(record);
			}

			// collect related instance IDs
			Stream.concat(optionalRelations.stream(), mandatoryRelations.stream()).forEach(rc -> rc.add(entityRecord, record));
		}

		if(entityResult.isEmpty()) {
			// no entities found -> neither related nor child entities required
			return entityResult;
		}

		// load and map related entities
		loadRelatedEntities(optionalRelations);
		loadRelatedEntities(mandatoryRelations);

		// sort children
		if(entityConfig.isReflexive()) {
			@SuppressWarnings("unchecked") EntityConfig<Deletable> childConfig = (EntityConfig<Deletable>) entityConfig;
			for(EntityRecord<?> entityRecord : parentRecords) {
				entityRecord.core.getChildrenStore().sort(childConfig.getEntityClass(), childConfig.getComparator());
			}
		}

		// load children
		for(EntityConfig<? extends Deletable> childConfig : entityConfig.getChildConfigs()) {
			cache.add(new ChildRequest<>(this, childConfig).load());
		}

		return entityResult;
	}

	protected List<RelationConfig> selectRelations(Query query, List<EntityConfig<?>> relatedConfigs, boolean mandatory) {
		List<RelationConfig> relationConfigs = new ArrayList<>();
		EntityType entityType = entityConfig.getEntityType();
		for(EntityConfig<?> relatedEntityConfig : relatedConfigs) {
			RelationConfig relationConfig = new RelationConfig(entityType, relatedEntityConfig, mandatory);
			query.select(relationConfig.relation.getAttribute());
			relationConfigs.add(relationConfig);
		}

		return relationConfigs;
	}

	protected List<Record> collectRecords(List<Result> results) {
		return results.stream().map(r -> r.getRecord(entityConfig.getEntityType())).collect(toList());
	}

	protected Condition createRelationCondition(Relation relation, Collection<Long> ids) {
		List<Long> distinctIDs = ids.stream().distinct().collect(toList());
		long[] unboxedIDs = new long[distinctIDs.size()];
		int i = 0;
		for(Long id : distinctIDs) {
			unboxedIDs[i++] = id;
		}

		return Operation.IN_SET.create(relation.getAttribute(), unboxedIDs);
	}

	protected void loadRelatedEntities(List<RelationConfig> relationConfigs) throws DataAccessException {
		for(RelationConfig relationConfig : relationConfigs) {
			EntityConfig<?> relatedConfig = relationConfig.entityConfig;

			boolean isContextTypeDefined = entityConfig.getContextType().isPresent();
			for(Entity relatedEntity : new EntityRequest<>(this, relatedConfig).loadAll(relationConfig.dependants.keySet())) {
				boolean setByContextType = !isContextTypeDefined && relatedConfig.getContextType().isPresent();
				for(EntityRecord<?> entityRecord : relationConfig.dependants.remove(relatedEntity.getID())) {
					// TODO setByContextType is CRAP!
					setRelatedEntity(entityRecord, relatedEntity, setByContextType ? relatedConfig.getContextType().get() : null);
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

	protected void setRelatedEntity(EntityRecord<?> entityRecord, Entity relatedEntity, ContextType contextType) {
		if(contextType == null) {
			entityRecord.core.getMutableStore().set(relatedEntity);
		} else {
			entityRecord.core.getMutableStore().set(relatedEntity, contextType);
		}

		if(entityRecord.entity instanceof ContextComponent && relatedEntity instanceof TemplateComponent) {
			hideAttributes(entityRecord, ((TemplateComponent) relatedEntity).getTemplateAttributes());
		} else if(entityRecord.entity instanceof ContextSensor && relatedEntity instanceof TemplateSensor) {
			hideAttributes(entityRecord, ((TemplateSensor) relatedEntity).getTemplateAttributes());
		}
	}

	private void hideAttributes(EntityRecord<?> entityRecord, List<TemplateAttribute> templateAttributes) {
		Set<String> names = new HashSet<>(entityRecord.core.getValues().keySet());
		names.remove(Entity.ATTR_NAME);
		names.remove(Entity.ATTR_MIMETYPE);
		templateAttributes.stream().map(Entity::getName).forEach(names::remove);
		entityRecord.core.hideValues(names);
	}

}
