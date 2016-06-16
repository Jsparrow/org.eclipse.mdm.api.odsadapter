package org.eclipse.mdm.api.odsadapter.lookup.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

public final class EntityConfigRepository {

	@Deprecated // TODO required for URI....
	private final Map<String, EntityConfig<?>> allEntityConfigs = new HashMap<>();

	// root types
	private final Map<Key<?>, EntityConfig<?>> entityConfigs = new HashMap<>();

	// child types (implicit load only!)
	private final Map<Key<?>, EntityConfig<?>> childConfigs = new HashMap<>();

	// ################################################################################################################

	public <T extends Entity> EntityConfig<T> find(Key<T> key) {
		Optional<EntityConfig<T>> entityConfig = get(entityConfigs, key);
		if(entityConfig.isPresent()) {
			return entityConfig.get();
		}

		return get(childConfigs, key).orElseThrow(() ->new IllegalArgumentException("Entity configuration not found."));
	}

	public <T extends Entity> EntityConfig<T> findRoot(Key<T> key) {
		return get(entityConfigs, key).orElseThrow(() ->new IllegalArgumentException("Entity configuration not found."));
	}

	public <T extends Entity> EntityConfig<T> findImplicit(Key<T> key) {
		return get(childConfigs, key).orElseThrow(() ->new IllegalArgumentException("Entity configuration not found."));
	}

	// ################################################################################################################

	@Deprecated
	public EntityConfig<?> find(String typeName) {
		EntityConfig<?> entityConfig = allEntityConfigs.get(typeName);
		if(entityConfig == null) {
			throw new IllegalArgumentException("Entity configuration not found.");
		}

		return entityConfig;
	}

	public /* TODO HIDE */ void register(EntityConfig<?> entityConfig) {
		registerChildConfigs(entityConfig);

		EntityConfig<?> currentByClass = entityConfigs.put(entityConfig.getKey(), entityConfig);
		EntityConfig<?> currentByType = allEntityConfigs.put(entityConfig.getEntityType().getName(), entityConfig);
		if(currentByClass != null || currentByType != null) {
			throw new IllegalArgumentException("It is not allowed to overwrite existing configurations.");
		}
	}

	private void registerChildConfigs(EntityConfig<?> entityConfig) {
		if(ContextRoot.class.equals(entityConfig.getEntityClass())) {
			return;
		}
		for(EntityConfig<?> childConfig : entityConfig.getChildConfigs()) {
			if(childConfigs.put(childConfig.getKey(), childConfig) != null) {
				throw new IllegalArgumentException("It is not allowed to overwrite existing configurations.");
			}

			registerChildConfigs(childConfig);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends Entity> Optional<EntityConfig<T>> get(Map<Key<?>, EntityConfig<?>> entityConfigs, Key<T> key) {
		return Optional.ofNullable((EntityConfig<T>) entityConfigs.get(key));
	}

}
