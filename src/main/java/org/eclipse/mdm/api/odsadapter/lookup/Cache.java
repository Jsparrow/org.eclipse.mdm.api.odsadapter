package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;


final class Cache {

	private final Map<EntityConfig<?>, EntityResult<?>> cache = new HashMap<>();

	public void add(EntityResult<?> entityResult) {
		cache.put(entityResult.request.entityConfig, entityResult);
	}

	public EntityResult<?> get(EntityConfig<?> entityConfig) {
		EntityResult<?> entityResult = cache.get(entityConfig);
		if(entityResult == null) {
			throw new IllegalArgumentException(); // TODO result not cached!
		}

		return entityResult;
	}

}
