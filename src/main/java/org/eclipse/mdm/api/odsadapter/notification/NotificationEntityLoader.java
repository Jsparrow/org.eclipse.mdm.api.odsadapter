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


package org.eclipse.mdm.api.odsadapter.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationEntityLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEntityLoader.class);

	private final ODSModelManager modelManager;
	private final QueryService queryService;
	private final EntityLoader loader;

	private boolean loadContextDescribable;

	public NotificationEntityLoader(ODSModelManager modelManager, QueryService queryService, boolean loadContextDescribable) {
		this.modelManager = modelManager;
		this.queryService =  queryService;
		this.loader = new EntityLoader(modelManager, queryService);
		this.loadContextDescribable = loadContextDescribable;
	}

	public <T extends Entity> T load(Key<T> key, String userId) {
		return loader.load(key, userId);
	}

	public List<? extends Entity> loadEntities(String aid, List<String> ids) {
		return loadEntities(modelManager.getEntityType(aid), ids);
	}

	/**
	 * @param entityType
	 *            entity type of the entities to load.
	 * @param ids
	 *            IDs of the entities to load.
	 * @return loaded entities.
	 * @throws DataAccessException
	 *             Throw if the entities cannot be loaded.
	 */
	public List<? extends Entity> loadEntities(EntityType entityType, List<String> ids) {

		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		EntityConfig<?> config = getEntityConfig(entityType);

		if (config == null || isLoadContextDescribable(config)) {
			// entityType not modelled in MDM, try to load its
			// ContextDescribable if it is a ContextRoot/ContextComponent
			final EntityType testStep = modelManager.getEntityType(TestStep.class);
			final EntityType measurement = modelManager.getEntityType(Measurement.class);

			if (hasRelationTo(entityType, testStep, measurement)) {
				return loadEntityForContextRoot(entityType, ids);
			} else if (hasRelationTo(entityType, modelManager.getEntityType("UnitUnderTest"),
					modelManager.getEntityType("TestSequence"), modelManager.getEntityType("TestEquipment"))) {
				return loadEntityForContextComponent(entityType, ids);
			} else {
				LOGGER.debug(new StringBuilder().append("Cannot load entitis for entityType ").append(entityType).append(" and ids ").append(ids).toString());
				return Collections.emptyList();
			}
		} else {
			return loader.loadAll(config.getKey(), ids);
		}
	}

	/**
	 * Loads the ContextDescribables to the given context root instances
	 * 
	 * @param contextRoot
	 *            entityType of the context root
	 * @param ids
	 *            IDs of the context roots.
	 * @return the loaded ContextDescribables
	 * @throws DataAccessException
	 *             Throw if the ContextDescribables cannot be loaded.
	 */
	private List<ContextDescribable> loadEntityForContextRoot(EntityType contextRoot, List<String> ids) {

		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);

		List<String> testStepIDs = queryService.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), JoinType.OUTER)
				.fetch(Filter.and().add(ComparisonOperator.IN_SET.create(contextRoot.getIDAttribute(), ids.toArray(new String[0]))))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());

		List<String> measurementIDs = queryService.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), JoinType.OUTER)
				.fetch(Filter.and().add(ComparisonOperator.IN_SET.create(contextRoot.getIDAttribute(), ids.toArray(new String[0]))))
				.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());

		List<ContextDescribable> list = new ArrayList<>();
		list.addAll(loader.loadAll(new Key<>(TestStep.class), testStepIDs));
		list.addAll(loader.loadAll(new Key<>(Measurement.class), measurementIDs));

		return list;
	}

	/**
	 * Loads the ContextDescribables to the given context component instances
	 * 
	 * @param contextComponent
	 *            entityType of the context component
	 * @param ids
	 *            IDs of the contextComponents to load.
	 * @return the loaded ContextDescribables
	 * @throws DataAccessException
	 *             Throw if the ContextDescribables cannot be loaded.
	 */
	private List<ContextDescribable> loadEntityForContextComponent(EntityType contextComponent, List<String> ids) {

		// ContextComponent can only have one parent
		final EntityType contextRoot = contextComponent.getParentRelations().get(0).getTarget();

		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);

		List<String> testStepIDs = queryService.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), JoinType.OUTER)
				.join(contextRoot.getRelation(contextComponent), JoinType.OUTER)
				.fetch(Filter.and().add(ComparisonOperator.IN_SET.create(contextComponent.getIDAttribute(), ids.toArray(new String[0]))))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());

		List<String> measurementIDs = queryService.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), JoinType.OUTER)
				.join(contextRoot.getRelation(contextComponent), JoinType.OUTER)
				.fetch(Filter.and().add(ComparisonOperator.IN_SET.create(contextComponent.getIDAttribute(), ids.toArray(new String[0]))))
				.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());

		List<ContextDescribable> list = new ArrayList<>();
		list.addAll(loader.loadAll(new Key<>(TestStep.class), testStepIDs));
		list.addAll(loader.loadAll(new Key<>(Measurement.class), measurementIDs));
		return list;
	}

	/**
	 * @param entityConfig
	 * @return true, if the entityConfig belongs to a context root or context
	 *         component and the option loadContextDescribable
	 */
	private boolean isLoadContextDescribable(EntityConfig<?> entityConfig) {
		return loadContextDescribable && (entityConfig.getEntityClass().isAssignableFrom(ContextRoot.class)
				|| entityConfig.getEntityClass().isAssignableFrom(ContextComponent.class));
	}

	/**
	 * Checks if a relation between sourceEntityType and at least one entity
	 * type in targetEntityType exists.
	 * 
	 * @param sourceEntityType
	 *            source entity type.
	 * @param targetEntityTypes
	 *            list of target enitity types.
	 * @return true, if relation between source entity type and at least one
	 *         target entity type exists.
	 */
	private boolean hasRelationTo(EntityType sourceEntityType, EntityType... targetEntityTypes) {
		for (EntityType e : targetEntityTypes) {
			try {
				sourceEntityType.getRelation(e);
				return true;
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}

		return false;
	}

	/**
	 * @param entityType
	 *            entity type the {@link EntityConfig} is requested for
	 * @return {@link EntityConfig} or null if not config was found for the
	 *         specified entity type
	 */
	private EntityConfig<?> getEntityConfig(EntityType entityType) {
		try {
			return modelManager.getEntityConfig(entityType);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
