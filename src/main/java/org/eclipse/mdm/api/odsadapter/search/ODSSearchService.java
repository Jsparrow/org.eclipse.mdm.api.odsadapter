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
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.base.query.Searchable;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

public final class ODSSearchService implements SearchService {

	private final Map<Class<? extends Entity>, SearchQuery> searchQueriesByType = new HashMap<>();

	private final EntityLoader entityLoader;
	private final ODSModelManager modelManager;

	public ODSSearchService(ODSModelManager modelManager, EntityLoader entityLoader) {
		this.entityLoader = entityLoader;
		this.modelManager = modelManager;

		registerMergedSearchQuery(Test.class, c -> new TestSearchQuery(modelManager, c));
		registerMergedSearchQuery(TestStep.class, c -> new TestStepSearchQuery(modelManager, c));
		registerMergedSearchQuery(Measurement.class, c -> new MeasurementSearchQuery(modelManager, c));
		registerMergedSearchQuery(Channel.class, c -> new ChannelSearchQuery(modelManager, c));
	}

	@Override
	public List<Class<? extends Entity>> listSearchableTypes() {
		return new ArrayList<>(searchQueriesByType.keySet());
	}

	@Override
	public List<EntityType> listEntityTypes(Class<? extends Entity> type) {
		return findSearchQuery(type).listEntityTypes();
	}

	@Override
	public Searchable getSearchableRoot(Class<? extends Entity> type) {
		return findSearchQuery(type).getSearchableRoot();
	}

	@Override
	public List<Value> getFilterValues(Class<? extends Entity> type, Attribute attribute, Filter filter) throws DataAccessException {
		return findSearchQuery(type).getFilterValues(attribute, filter);
	}

	@Override
	public <T extends Entity> Map<T, List<Record>> fetchComplete(Class<T> type, List<EntityType> entityTypes, Filter filter) throws DataAccessException {
		return createResult(type, findSearchQuery(type).fetchComplete(entityTypes, filter));
	}

	@Override
	public <T extends Entity> Map<T, List<Record>> fetch(Class<T> type, List<Attribute> attributes, Filter filter) throws DataAccessException {
		return createResult(type, findSearchQuery(type).fetch(attributes, filter));
	}

	private <T extends Entity> Map<T, List<Record>> createResult(Class<T> type, List<Result> results) throws DataAccessException {
		List<EntityType> relatedEntityTypes = modelManager.getImplicitEntityTypes(type);
		EntityType entityType = modelManager.getEntityType(type);

		Map<Long, List<Record>> resultsByEntityID = new HashMap<>();
		for(Result result : results) {
			List<Record> relatedRecords = result.retainAll(relatedEntityTypes);
			resultsByEntityID.put(result.getRecord(entityType).getID(), relatedRecords);
		}

		Map<T, List<Record>> resultsByEntity = new HashMap<>();
		for(T entity : entityLoader.loadAll(type, resultsByEntityID.keySet())) {
			resultsByEntity.put(entity, resultsByEntityID.get(entity.getURI().getID()));
		}

		return resultsByEntity;
	}

	private SearchQuery findSearchQuery(Class<? extends Entity> type) {
		SearchQuery searchQuery = searchQueriesByType.get(type);
		if(searchQuery == null) {
			throw new IllegalArgumentException("Search query for type '" + type.getSimpleName() + "' not found.");
		}

		return searchQuery;
	}

	private void registerMergedSearchQuery(Class<? extends Entity> type, Function<ContextState, BaseEntitySearchQuery> factory) {
		searchQueriesByType.put(type, new MergedSearchQuery<>(type, modelManager, factory));
	}

}
