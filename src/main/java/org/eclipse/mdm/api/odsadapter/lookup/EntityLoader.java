package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

/**
 * Loads complete {@link Entity}s by using the {@link EntityConfig} identified
 * by given {@link Key}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class EntityLoader {
	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSModelManager modelManager;
	private final QueryService queryService;
	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            The {@link ODSModelManager}.
	 */
	public EntityLoader(ODSModelManager modelManager, QueryService queryService) {
		this.modelManager = modelManager;
		this.queryService = queryService;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Loads the entity with given instance ID.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used to resolve the entity configuration.
	 * @param instanceID
	 *            The instance ID.
	 * @return The queried {@code Entity} is returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public <T extends Entity> T load(Key<T> key, String instanceID) throws DataAccessException {
		List<T> entities = loadAll(key, Collections.singletonList(instanceID));
		if (entities.size() != 1) {
			throw new DataAccessException("Failed to load entity by instance ID.");
		}
		return entities.get(0);
	}

	/**
	 * Loads all entities matching given name pattern.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used to resolve the entity configuration.
	 * @param pattern
	 *            Is always case sensitive and may contain wildcard characters
	 *            as follows: "?" for one matching character and "*" for a
	 *            sequence of matching characters.
	 * @return A {@link List} with queried entities is returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public <T extends Entity> List<T> loadAll(Key<T> key, String pattern) throws DataAccessException {
		return createRequest(key).loadAll(pattern);
	}

	/**
	 * Loads all entities matching given instance IDs.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used to resolve the entity configuration.
	 * @param instanceIDs
	 *            The instance IDs.
	 * @return A {@link List} with queried entities is returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load entities.
	 */
	public <T extends Entity> List<T> loadAll(Key<T> key, Collection<String> instanceIDs) throws DataAccessException {
		return createRequest(key).loadAll(instanceIDs);
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Creates a new {@link EntityRequest} for given {@link Key}.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used to resolve the entity configuration.
	 * @return The created {@code EntityRequest} is returned.
	 */
	private <T extends Entity> EntityRequest<T> createRequest(Key<T> key) {
		/*
		 * TODO: add custom request implementations here!
		 */
		return new EntityRequest<>(modelManager, queryService, key);
	}

}
