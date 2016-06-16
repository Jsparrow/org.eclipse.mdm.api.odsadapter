package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

public final class EntityLoader {

	private final ODSModelManager modelManager;

	public EntityLoader(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}

	// ################################################################################################################

	public <T extends Entity> T load(Key<T> key, Long instanceID) throws DataAccessException {
		List<T> entities = loadAll(key, Collections.singletonList(instanceID));
		if(entities.size() != 1) {
			throw new DataAccessException("TODO");
		}
		return entities.get(0);
	}

	public <T extends Entity> List<T> loadAll(Key<T> key, String pattern) throws DataAccessException {
		return createRequest(key).loadAll(pattern);
	}

	public <T extends Entity> List<T> loadAll(Key<T> key, Collection<Long> instanceIDs) throws DataAccessException {
		// TODO ensure all instances have been loaded?!
		//		instanceIDs.size == result.size
		return createRequest(key).loadAll(instanceIDs);
	}

	private <T extends Entity> EntityRequest<T> createRequest(Key<T> key) {
		/*
		 * TODO: add custom request implementations here!
		 */
		return new EntityRequest<>(modelManager, modelManager.getEntityConfig(key));
	}

}
