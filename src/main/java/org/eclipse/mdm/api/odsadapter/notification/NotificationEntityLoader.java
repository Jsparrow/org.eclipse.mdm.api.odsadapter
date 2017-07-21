package org.eclipse.mdm.api.odsadapter.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Operation;
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
	private final EntityLoader loader;

	private boolean loadContextDescribable;

	public NotificationEntityLoader(ODSModelManager modelManager, boolean loadContextDescribable) {
		this.modelManager = modelManager;
		this.loader = new EntityLoader(modelManager);
		this.loadContextDescribable = loadContextDescribable;
	}

	public <T extends Entity> T load(Key<T> key, String userId) throws DataAccessException {
		return loader.load(key, userId);
	}

	public List<? extends Entity> loadEntities(String aid, List<String> ids) throws DataAccessException {
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
	public List<? extends Entity> loadEntities(EntityType entityType, List<String> ids) throws DataAccessException {

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
				LOGGER.debug("Cannot load entitis for entityType " + entityType + " and ids " + ids);
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
	private List<ContextDescribable> loadEntityForContextRoot(EntityType contextRoot, List<String> ids)
			throws DataAccessException {

		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);

		List<String> testStepIDs = modelManager.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextRoot.getIDAttribute(), ids)))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());

		List<String> measurementIDs = modelManager.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextRoot.getIDAttribute(), ids)))
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
	private List<ContextDescribable> loadEntityForContextComponent(EntityType contextComponent, List<String> ids)
			throws DataAccessException {

		// ContextComponent can only have one parent
		final EntityType contextRoot = contextComponent.getParentRelations().get(0).getTarget();

		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);

		List<String> testStepIDs = modelManager.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), Join.OUTER)
				.join(contextRoot.getRelation(contextComponent), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextComponent.getIDAttribute(), ids)))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());

		List<String> measurementIDs = modelManager.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), Join.OUTER)
				.join(contextRoot.getRelation(contextComponent), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextComponent.getIDAttribute(), ids)))
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
