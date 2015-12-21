/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.base.query.Searchable;

public final class ODSSearchService implements SearchService {

	private final Map<Class<? extends DataItem>, SearchQuery> searchQueriesByType = new HashMap<>();

	private final DataItemFactory dataItemFactory;
	private final ODSQueryService queryService;

	public ODSSearchService(ODSQueryService queryService, DataItemFactory dataItemFactory) {
		this.dataItemFactory = dataItemFactory;
		this.queryService = queryService;

		registerMergedSearchQuery(Test.class, c -> new TestSearchQuery(queryService, c));
		registerMergedSearchQuery(TestStep.class, c -> new TestStepSearchQuery(queryService, c));
		registerMergedSearchQuery(Measurement.class, c -> new MeasurementSearchQuery(queryService, c));
		registerMergedSearchQuery(Channel.class, c -> new ChannelSearchQuery(queryService, c));
	}

	@Override
	public List<Class<? extends DataItem>> listSearchableTypes() {
		return new ArrayList<>(searchQueriesByType.keySet());
	}

	@Override
	public List<Entity> listEntities(Class<? extends DataItem> type) {
		return findSearchQuery(type).listEntities();
	}

	@Override
	public Searchable getSearchableRoot(Class<? extends DataItem> type) {
		return findSearchQuery(type).getSearchableRoot();
	}

	@Override
	public List<Value> getFilterValues(Class<? extends DataItem> type, Attribute attribute, Filter filter) throws DataAccessException {
		return findSearchQuery(type).getFilterValues(attribute, filter);
	}

	@Override
	public <T extends DataItem> Map<T, List<Record>> fetchComplete(Class<T> type, List<Entity> entities, Filter filter) throws DataAccessException {
		return createResult(type, findSearchQuery(type).fetchComplete(entities, filter));
	}

	@Override
	public <T extends DataItem> Map<T, List<Record>> fetch(Class<T> type, List<Attribute> attributes, Filter filter) throws DataAccessException {
		return createResult(type, findSearchQuery(type).fetch(attributes, filter));
	}

	private <T extends DataItem> Map<T, List<Record>> createResult(Class<T> type, List<Result> results) throws DataAccessException {
		List<Entity> relatedEntities = queryService.getImplicitEntities(type);
		Map<T, List<Record>> resultMap = new HashMap<>();
		for(Result result : results) {
			/**
			 * TODO this should be refactored as soon as a data item factory is implemented
			 */
			List<Record> relatedRecords = result.retainAll(relatedEntities);
			resultMap.put(dataItemFactory.createDataItem(type, result), relatedRecords);
		}

		return resultMap;
	}

	private SearchQuery findSearchQuery(Class<? extends DataItem> type) {
		SearchQuery searchQuery = searchQueriesByType.get(type);
		if(searchQuery == null) {
			throw new IllegalArgumentException("Search query for type '" + type.getSimpleName() + "' not found.");
		}

		return searchQuery;
	}

	private void registerMergedSearchQuery(Class<? extends DataItem> type, Function<ContextState, AbstractDataItemSearchQuery> factory) {
		searchQueriesByType.put(type, new MergedSearchQuery<>(type, queryService, factory));
	}

}
