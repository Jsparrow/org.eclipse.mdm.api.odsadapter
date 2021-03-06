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


package org.eclipse.mdm.api.odsadapter.lookup.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

/**
 * Repository for {@link EntityConfig}s.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class EntityConfigRepository {

	// ======================================================================
	// Instance variables
	// ======================================================================

	// root types
	private final Map<Key<?>, EntityConfig<?>> entityConfigs = new HashMap<>();

	// child types (implicit load only!)
	private final Map<Key<?>, EntityConfig<?>> childConfigs = new HashMap<>();
	private final Map<String, EntityConfig<?>> contextConfigs = new HashMap<>();

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the {@link EntityConfig} associated with given {@link Key}. This
	 * method tries to find the associated {@code EntityConfig} in the root
	 * configurations. If it is not found there, then it tries to find it in the
	 * child {@code EntityConfig}s.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used as identifier.
	 * @return The {@code EntityConfig} is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if unable to find associated {@code EntityConfig}.
	 */
	public <T extends Entity> EntityConfig<T> find(Key<T> key) {
		Optional<EntityConfig<T>> entityConfig = get(entityConfigs, key);
		if (entityConfig.isPresent()) {
			return entityConfig.get();
		}

		return get(childConfigs, key)
				.orElseThrow(() -> new IllegalArgumentException("Entity configuration not found."));
	}

	/**
	 * Returns the {@link EntityConfig} associated with given {@link Key}. This
	 * method tries to find the associated {@code EntityConfig} in the root
	 * configurations.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used as identifier.
	 * @return The {@code EntityConfig} is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if unable to find associated {@code EntityConfig}.
	 */
	public <T extends Entity> EntityConfig<T> findRoot(Key<T> key) {
		return get(entityConfigs, key)
				.orElseThrow(() -> new IllegalArgumentException("Entity configuration not found."));
	}

	/**
	 * Returns the {@link EntityConfig} associated with given {@link Key}. This
	 * method tries to find the associated {@code EntityConfig} in the child
	 * configurations.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used as identifier.
	 * @return The {@code EntityConfig} is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if unable to find associated {@code EntityConfig}.
	 */
	public <T extends Entity> EntityConfig<T> findImplicit(Key<T> key) {
		return get(childConfigs, key)
				.orElseThrow(() -> new IllegalArgumentException("Entity configuration not found."));
	}

	/**
	 * Returns the {@link EntityConfig} associated with given
	 * {@link EntityType}.
	 *
	 * @param entityType
	 *            Its name is used as identifier.
	 * @return The {@code EntityConfig} is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if unable to find associated {@code EntityConfig}.
	 */
	public EntityConfig<?> find(EntityType entityType) {
		Optional<EntityConfig<?>> entityConfig = entityConfigs.values().stream()
				.filter(ec -> ec.getEntityType().equals(entityType)).findFirst();
		if (entityConfig.isPresent()) {
			// entity config is a root type
			return entityConfig.get();
		}

		entityConfig = childConfigs.values().stream().filter(ec -> ec.getEntityType().equals(entityType)).findFirst();
		if (entityConfig.isPresent()) {
			// entity config is an implicitly loaded child type
			return entityConfig.get();
		}

		EntityConfig<?> config = contextConfigs.get(entityType.getName());
		if (config == null) {
			throw new IllegalArgumentException(new StringBuilder().append("Entity configuration for type '").append(entityType).append("' not found.").toString());
		}

		// config is either a context component or context sensor type
		return config;
	}

	/**
	 * Stores given {@link EntityConfig} in this repository.
	 *
	 * @param entityConfig
	 *            The {@code EntityConfig}.
	 * @throws IllegalArgumentException
	 *             Thrown if an attempt to overwrite an existing
	 *             {@code EntityConfig} is recorded.
	 */
	public void register(EntityConfig<?> entityConfig) {
		registerChildConfigs(entityConfig);

		EntityConfig<?> currentByClass = entityConfigs.put(entityConfig.getKey(), entityConfig);
		if (currentByClass != null) {
			throw new IllegalArgumentException("It is not allowed to overwrite existing configurations.");
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Recursively registers all child configurations of given
	 * {@link EntityConfig}.
	 *
	 * @param entityConfig
	 *            The {@code EntityConfig}.
	 * @throws IllegalArgumentException
	 *             Thrown if an attempt to overwrite an existing
	 *             {@code EntityConfig} is recorded.
	 */
	private void registerChildConfigs(EntityConfig<?> entityConfig) {
		Class<? extends Entity> entityClass = entityConfig.getEntityClass();
		if (ContextRoot.class.equals(entityClass) || ContextComponent.class.equals(entityClass)) {
			for (EntityConfig<?> childConfig : entityConfig.getChildConfigs()) {
				if (contextConfigs.put(childConfig.getEntityType().getName(), childConfig) != null) {
					throw new IllegalArgumentException("It is not allowed to overwrite existing configurations.");
				}

				registerChildConfigs(childConfig);
			}

			return;
		}
		for (EntityConfig<?> childConfig : entityConfig.getChildConfigs()) {
			if (childConfigs.put(childConfig.getKey(), childConfig) != null) {
				throw new IllegalArgumentException("It is not allowed to overwrite existing configurations.");
			}

			registerChildConfigs(childConfig);
		}
	}

	/**
	 * Retrieves the {@link EntityConfig} associated with given {@link Key} from
	 * given {@code Map}.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param entityConfigs
	 *            Used to retrieve requested {@code EntityConfig}.
	 * @param key
	 *            Used as identifier.
	 * @return {@code Optional} is empty if {@code EntityConfig} not found.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Entity> Optional<EntityConfig<T>> get(Map<Key<?>, EntityConfig<?>> entityConfigs,
			Key<T> key) {
		return Optional.ofNullable((EntityConfig<T>) entityConfigs.get(key));
	}
}
