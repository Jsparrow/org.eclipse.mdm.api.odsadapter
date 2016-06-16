/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.base.query.Searchable;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

public final class ODSSearchService implements SearchService {

	private final Map<Class<? extends Entity>, SearchQuery> searchQueries = new HashMap<>();

	private final ODSModelManager modelManager;
	private final EntityLoader entityLoader;

	public ODSSearchService(ODSModelManager modelManager, EntityLoader entityLoader) {
		this.modelManager = modelManager;
		this.entityLoader = entityLoader;

		registerMergedSearchQuery(Test.class, c -> new TestSearchQuery(modelManager, c));
		registerMergedSearchQuery(TestStep.class, c -> new TestStepSearchQuery(modelManager, c));
		registerMergedSearchQuery(Measurement.class, c -> new MeasurementSearchQuery(modelManager, c));
		registerMergedSearchQuery(Channel.class, c -> new ChannelSearchQuery(modelManager, c));
	}

	@Override
	public List<Class<? extends Entity>> listSearchableTypes() {
		return new ArrayList<>(searchQueries.keySet());
	}

	@Override
	public List<EntityType> listEntityTypes(Class<? extends Entity> entityClass) {
		return findSearchQuery(entityClass).listEntityTypes();
	}

	@Override
	public Searchable getSearchableRoot(Class<? extends Entity> entityClass) {
		return findSearchQuery(entityClass).getSearchableRoot();
	}

	@Override
	public List<Value> getFilterValues(Class<? extends Entity> entityClass, Attribute attribute, Filter filter) throws DataAccessException {
		return findSearchQuery(entityClass).getFilterValues(attribute, filter);
	}

	@Override
	public <T extends Entity> Map<T, Result> fetchComplete(Class<T> entityClass, List<EntityType> entityTypes, Filter filter) throws DataAccessException {
		return createResult(entityClass, findSearchQuery(entityClass).fetchComplete(entityTypes, filter));
	}

	@Override
	public <T extends Entity> Map<T, Result> fetch(Class<T> entityClass, List<Attribute> attributes, Filter filter) throws DataAccessException {
		return createResult(entityClass, findSearchQuery(entityClass).fetch(attributes, filter));
	}

	private <T extends Entity> Map<T, Result> createResult(Class<T> entityClass, List<Result> results) throws DataAccessException {
		EntityType entityType = modelManager.getEntityType(entityClass);
		Map<Long, Result> recordsByEntityID = new HashMap<>();
		for(Result result : results) {
			recordsByEntityID.put(result.getRecord(entityType).getID(), result);
		}

		Map<T, Result> resultsByEntity = new HashMap<>();
		for(T entity : entityLoader.loadAll(new Key<>(entityClass), recordsByEntityID.keySet())) {
			resultsByEntity.put(entity, recordsByEntityID.get(entity.getID()));
		}

		return resultsByEntity;
	}

	private SearchQuery findSearchQuery(Class<? extends Entity> entityClass) {
		SearchQuery searchQuery = searchQueries.get(entityClass);
		if(searchQuery == null) {
			throw new IllegalArgumentException("Search query for type '" + entityClass.getSimpleName() + "' not found.");
		}

		return searchQuery;
	}

	private void registerMergedSearchQuery(Class<? extends Entity> entityClass, Function<ContextState, BaseEntitySearchQuery> factory) {
		searchQueries.put(entityClass, new MergedSearchQuery<>(entityClass, modelManager, factory));
	}

}
