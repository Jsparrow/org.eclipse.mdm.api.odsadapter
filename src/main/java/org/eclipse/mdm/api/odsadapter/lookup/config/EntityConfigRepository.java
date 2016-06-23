package org.eclipse.mdm.api.odsadapter.lookup.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

public final class EntityConfigRepository {

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

	public EntityConfig<?> find(EntityType entityType) {
		return entityConfigs.values().stream().filter(ec -> ec.getEntityType().equals(entityType)).findAny()
				.orElseThrow(() -> new IllegalArgumentException("Entity configuration for type '" + entityType + "' not found."));
	}


	// ################################################################################################################

	public /* TODO HIDE */ void register(EntityConfig<?> entityConfig) {
		registerChildConfigs(entityConfig);

		EntityConfig<?> currentByClass = entityConfigs.put(entityConfig.getKey(), entityConfig);
		if(currentByClass != null) {
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
