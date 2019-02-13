/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter.lookup;

import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

/**
 * Extends {@link EntityRequest} to load children for a given
 * {@link EntityRequest}.
 *
 * @param <T>
 *            The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class ChildRequest<T extends Deletable> extends EntityRequest<T> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final EntityRequest<?> parent;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param parentRequest
	 *            The parent {@link EntityRequest}.
	 * @param entityConfig
	 *            The {@link EntityConfig}.
	 */
	ChildRequest(EntityRequest<?> parentRequest, EntityConfig<T> entityConfig) {
		super(parentRequest, entityConfig);
		parent = parentRequest;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Loads all related child entities.
	 *
	 * @return Returns the queried {@code EntityResult}.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public EntityResult<T> load() {
		filtered = parent.filtered;

		EntityType entityType = entityConfig.getEntityType();
		Relation parentRelation = entityConfig.getEntityType().getRelation(parent.entityConfig.getEntityType());
		Relation reflexiveRelation = entityConfig.isReflexive() ? entityType.getRelation(entityType) : null;

		Query query = queryService.createQuery()
				// select entity attributes
				.selectAll(entityConfig.getEntityType())
				// select parent entity ID
				.select(parentRelation.getAttribute());

		if (entityConfig.isReflexive()) {
			query.select(reflexiveRelation.getAttribute());
			// entities with children have to be processed before their
			// children!
			query.order(entityType.getIDAttribute());
		}

		// prepare relations select statements
		List<RelationConfig> optionalRelations = selectRelations(query, entityConfig.getOptionalConfigs(), false);
		List<RelationConfig> mandatoryRelations = selectRelations(query, entityConfig.getMandatoryConfigs(), true);
		List<RelationConfig> inheritedRelations = selectRelations(query, entityConfig.getInheritedConfigs(), true);

		// configure filter
		Filter adjustedFilter = Filter.or();
		if (filtered) {
			// preserve current conditions
			adjustedFilter.ids(parentRelation, parent.entityResult.getIDs());
			if (entityConfig.isReflexive()) {
				// extend to retrieve all reflexive child candidates
				adjustedFilter.add(ComparisonOperator.IS_NOT_NULL.create(reflexiveRelation.getAttribute(), 0L));
			}
		}

		// load entities and prepare mappings for required related entities
		List<EntityRecord<?>> parentRecords = new ArrayList<>();
		for (Record record : collectRecords(query.fetch(adjustedFilter))) {
			Optional<String> parentID = record.getID(parentRelation);
			Optional<String> reflexiveParentID = Optional.empty();
			if (entityConfig.isReflexive()) {
				reflexiveParentID = record.getID(reflexiveRelation);
			}
			EntityRecord<T> entityRecord;

			if (parentID.isPresent()) {
				EntityResult<?> parentResult = parent.entityResult;
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Optional<EntityRecord<?>> parentRecord = (Optional) parentResult.get(parentID.get());
				if (!parentRecord.isPresent()) {
					continue;
				}

				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else if (entityConfig.isReflexive() && reflexiveParentID.isPresent()) {
				Optional<EntityRecord<T>> parentRecord = entityResult.get(reflexiveParentID.get());
				if (!parentRecord.isPresent()) {
					// this entity's parent was not loaded -> skip
					continue;
				}
				// reflexive child
				entityRecord = entityResult.add(parentRecord.get(), record);
				parentRecords.add(parentRecord.get());
			} else {
				throw new IllegalStateException("Entity without parent found");
			}

			// collect related instance IDs
			concat(concat(optionalRelations.stream(), mandatoryRelations.stream()), inheritedRelations.stream())
					.forEach(rc -> rc.add(entityRecord, record));
		}

		if (entityResult.isEmpty()) {
			// no entities found -> neither related nor child entities required
			return entityResult;
		}

		// load and map related entities
		loadRelatedEntities(optionalRelations);
		loadRelatedEntities(mandatoryRelations);
		assignRelatedEntities(inheritedRelations);

		// sort children of parent
		for (EntityRecord<?> entityRecord : parentRecords) {
			entityRecord.core.getChildrenStore().sort(entityConfig.getEntityClass(), entityConfig.getComparator());
		}

		// load children
		for (EntityConfig<? extends Deletable> childConfig : entityConfig.getChildConfigs()) {
			cache.add(new ChildRequest<>(this, childConfig).load());
		}

		return entityResult;
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Maps related entities for each given {@link RelationConfig} using the
	 * entities loaded in previous {@link EntityRequest}.
	 *
	 * @param relationConfigs
	 *            The {@code RelationConfig}s.
	 * @throws DataAccessException
	 *             Thrown if unable to load related entities.
	 */
	private void assignRelatedEntities(List<RelationConfig> relationConfigs) {
		for (RelationConfig relationConfig : relationConfigs) {
			EntityConfig<?> relatedConfig = relationConfig.entityConfig;

			boolean isContextTypeDefined = entityConfig.getContextType().isPresent();
			cache.get(relatedConfig).getEntities().forEach(relatedEntity -> {
				boolean setByContextType = !isContextTypeDefined && relatedConfig.getContextType().isPresent();
				List<EntityRecord<?>> entityRecords = relationConfig.dependants.remove(relatedEntity.getID());
				entityRecords = entityRecords == null ? new ArrayList<EntityRecord<?>>() : entityRecords;
				for (EntityRecord<?> entityRecord : entityRecords) {
					setRelatedEntity(entityRecord, relatedEntity,
							setByContextType ? relatedConfig.getContextType().get() : null);
				}
			});

			if (!relationConfig.dependants.isEmpty()) {
				// this may occur if the instance id of the related entity
				// is defined, but the entity itself does not exist
				throw new IllegalStateException("Unable to load related entities.");
			}
		}
	}
}
