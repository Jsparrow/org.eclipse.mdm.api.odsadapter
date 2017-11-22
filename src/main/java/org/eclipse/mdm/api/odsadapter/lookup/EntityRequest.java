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

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextSensor;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateSensor;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

/**
 * Recursively loads entities for a given {@link EntityConfig} with all resolved
 * dependencies (optional, mandatory children).
 *
 * @param <T>
 *            The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class EntityRequest<T extends Entity> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	final ODSModelManager odsModelManager;
	final QueryService queryService;
	final EntityConfig<T> entityConfig;
	final EntityResult<T> entityResult = new EntityResult<>(this);

	final Cache cache;

	boolean filtered;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            The {@link ODSModelManager}.
	 * @param config
	 *            The {@link EntityConfig}.
	 */
	public EntityRequest(ODSModelManager modelManager, QueryService queryService, Key<T> key) {
		this.odsModelManager = modelManager;
		this.queryService = queryService;
		this.entityConfig = modelManager.getEntityConfig(key);
		cache = new Cache();
	}

	/**
	 * Constructor.
	 *
	 * @param parentRequest
	 *            The parent {@link EntityRequest}.
	 * @param entityConfig
	 *            The {@link EntityConfig}.
	 */
	protected EntityRequest(EntityRequest<?> parentRequest, EntityConfig<T> entityConfig) {
		odsModelManager = parentRequest.odsModelManager;
		queryService = parentRequest.queryService;
		cache = parentRequest.cache;
		this.entityConfig = entityConfig;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Loads all entities matching given name pattern.
	 *
	 * @param pattern
	 *            Is always case sensitive and may contain wildcard characters
	 *            as follows: "?" for one matching character and "*" for a
	 *            sequence of matching characters.
	 * @return A sorted {@link List} with queried entities is returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public List<T> loadAll(String pattern) throws DataAccessException {
		return load(Filter.nameOnly(entityConfig.getEntityType(), pattern)).getSortedEntities();
	}

	/**
	 * Loads all entities matching given instance IDs.
	 *
	 * @param instanceIDs
	 *            The instance IDs.
	 * @return A sorted {@link List} with queried entities is returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public List<T> loadAll(Collection<String> instanceIDs) throws DataAccessException {
		if (instanceIDs.isEmpty()) {
			// just to be sure...
			return Collections.emptyList();
		}

		return load(Filter.idsOnly(entityConfig.getEntityType(), instanceIDs)).getSortedEntities();
	}
	
	public ODSModelManager getODSModelManager() {
		return odsModelManager;
	}

	// ======================================================================
	// Protected methods
	// ======================================================================

	/**
	 * Adds foreign key select statements to given {@link Query} for each given
	 * {@link EntityConfig}.
	 *
	 * @param query
	 *            The {@link Query}.
	 * @param relatedConfigs
	 *            The {@code EntityConfig}s.
	 * @param mandatory
	 *            Flag indicates whether given {@code EntityConfig}s are
	 *            mandatory or not.
	 * @return For each {@code EntityConfig} a corresponding {@code
	 * 		RelationConfig} is returned in a {@code List}.
	 */
	protected List<RelationConfig> selectRelations(Query query, List<EntityConfig<?>> relatedConfigs,
			boolean mandatory) {
		List<RelationConfig> relationConfigs = new ArrayList<>();
		EntityType entityType = entityConfig.getEntityType();
		for (EntityConfig<?> relatedEntityConfig : relatedConfigs) {
			RelationConfig relationConfig = new RelationConfig(entityType, relatedEntityConfig, mandatory);
			query.select(relationConfig.relation.getAttribute());
			relationConfigs.add(relationConfig);
		}

		return relationConfigs;
	}

	/**
	 * Convenience method collects the queried {@link Record} from each
	 * {@link Result}.
	 *
	 * @param results
	 *            The {@code Result}s.
	 * @return The queried {@link Record}s are returned.
	 */
	protected List<Record> collectRecords(List<Result> results) {
		return results.stream().map(r -> r.getRecord(entityConfig.getEntityType())).collect(toList());
	}

	/**
	 * Loads and maps related entities for each given {@link RelationConfig}.
	 *
	 * @param relationConfigs
	 *            The {@code RelationConfig}s.
	 * @throws DataAccessException
	 *             Thrown if unable to load related entities.
	 */
	protected void loadRelatedEntities(List<RelationConfig> relationConfigs) throws DataAccessException {
		for (RelationConfig relationConfig : relationConfigs) {
			EntityConfig<?> relatedConfig = relationConfig.entityConfig;

			boolean isContextTypeDefined = entityConfig.getContextType().isPresent();
			for (Entity relatedEntity : new EntityRequest<>(this, relatedConfig)
					.loadAll(relationConfig.dependants.keySet())) {
				boolean setByContextType = !isContextTypeDefined && relatedConfig.getContextType().isPresent();
				for (EntityRecord<?> entityRecord : relationConfig.dependants.remove(relatedEntity.getID())) {
					setRelatedEntity(entityRecord, relatedEntity,
							setByContextType ? relatedConfig.getContextType().get() : null);
				}
			}

			if (!relationConfig.dependants.isEmpty()) {
				// this may occur if the instance id of the related entity
				// is defined, but the entity itself does not exist
				throw new IllegalStateException("Unable to load related entities.");
			}
		}
	}

	/**
	 * Assigns given related {@link Entity} to given {@link EntityRecord}.
	 *
	 * @param entityRecord
	 *            The {@code EntityRecord} which references given
	 *            {@code Entity}.
	 * @param relatedEntity
	 *            The related {@code Entity}.
	 * @param contextType
	 *            Used as qualifier for relation assignment.
	 */
	protected void setRelatedEntity(EntityRecord<?> entityRecord, Entity relatedEntity, ContextType contextType) {
		if (contextType == null) {
			entityRecord.core.getMutableStore().set(relatedEntity);
		} else {
			entityRecord.core.getMutableStore().set(relatedEntity, contextType);
		}

		List<TemplateAttribute> templateAttributes = new ArrayList<>();
		if (entityRecord.entity instanceof ContextComponent && relatedEntity instanceof TemplateComponent) {
			templateAttributes.addAll(((TemplateComponent) relatedEntity).getTemplateAttributes());
		} else if (entityRecord.entity instanceof ContextSensor && relatedEntity instanceof TemplateSensor) {
			templateAttributes.addAll(((TemplateSensor) relatedEntity).getTemplateAttributes());
		}

		if (!templateAttributes.isEmpty()) {
			// hide Value containers that are missing in the template
			Set<String> names = new HashSet<>(entityRecord.core.getValues().keySet());
			names.remove(Entity.ATTR_NAME);
			names.remove(Entity.ATTR_MIMETYPE);
			templateAttributes.stream().map(Entity::getName).forEach(names::remove);
			entityRecord.core.hideValues(names);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Loads all entities matching given {@link Filter} including all of related
	 * entities (optional, mandatory and children).
	 *
	 * @param filter
	 *            The {@link Filter}.
	 * @return Returns the queried {@code EntityResult}.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	private EntityResult<T> load(Filter filter) throws DataAccessException {
		filtered = !filter.isEmtpty() || entityConfig.isReflexive();

		EntityType entityType = entityConfig.getEntityType();
		Relation reflexiveRelation = entityConfig.isReflexive() ? entityType.getRelation(entityType) : null;

		Query query = queryService.createQuery().selectAll(entityConfig.getEntityType());

		if (entityConfig.isReflexive()) {
			query.select(reflexiveRelation.getAttribute());
			// entities with children have to be processed before their
			// children!
			query.order(entityType.getIDAttribute());
		}

		// prepare relations select statements
		List<RelationConfig> optionalRelations = selectRelations(query, entityConfig.getOptionalConfigs(), false);
		List<RelationConfig> mandatoryRelations = selectRelations(query, entityConfig.getMandatoryConfigs(), true);

		// configure filter
		Filter adjustedFilter = Filter.or();
		if (filtered) {
			// preserve current conditions
			adjustedFilter.merge(filter);
			if (entityConfig.isReflexive()) {
				// extend to retrieve all reflexive child candidates
				adjustedFilter.add(ComparisonOperator.IS_NOT_NULL.create(reflexiveRelation.getAttribute(), 0L));
			}
		}

		// load entities and prepare mappings for required related entities
		List<EntityRecord<?>> parentRecords = new ArrayList<>();
		for (Record record : collectRecords(query.fetch(adjustedFilter))) {
			Optional<String> reflexiveParentID = Optional.empty();
			if (entityConfig.isReflexive()) {
				reflexiveParentID = record.getID(reflexiveRelation);
			}
			EntityRecord<T> entityRecord;

			if (entityConfig.isReflexive() && reflexiveParentID.isPresent()) {
				Optional<EntityRecord<T>> parentRecord = entityResult.get(reflexiveParentID.get());
				if (!parentRecord.isPresent()) {
					// this entity's parent was not loaded -> skip
					continue;
				}

				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else {
				entityRecord = entityResult.add(record);
			}

			// collect related instance IDs
			Stream.concat(optionalRelations.stream(), mandatoryRelations.stream())
					.forEach(rc -> rc.add(entityRecord, record));
		}

		if (entityResult.isEmpty()) {
			// no entities found -> neither related nor child entities required
			return entityResult;
		}

		// load and map related entities
		loadRelatedEntities(optionalRelations);
		loadRelatedEntities(mandatoryRelations);

		// sort children of parent
		if (entityConfig.isReflexive()) {
			@SuppressWarnings("unchecked")
			EntityConfig<Deletable> childConfig = (EntityConfig<Deletable>) entityConfig;
			for (EntityRecord<?> entityRecord : parentRecords) {
				entityRecord.core.getChildrenStore().sort(childConfig.getEntityClass(), childConfig.getComparator());
			}
		}

		// load children
		for (EntityConfig<? extends Deletable> childConfig : entityConfig.getChildConfigs()) {
			cache.add(new ChildRequest<>(this, childConfig).load());
		}

		return entityResult;
	}

}
