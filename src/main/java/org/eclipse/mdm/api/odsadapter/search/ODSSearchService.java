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


package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.search.SearchQuery;
import org.eclipse.mdm.api.base.search.SearchService;
import org.eclipse.mdm.api.base.search.Searchable;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.odsadapter.ODSContext;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * ODS implementation of the {@link SearchService} interface.
 *
 * @since 1.0.0
 * @author jst, Peak Solution GmbH
 */
public class ODSSearchService implements SearchService {

	private final Map<Class<? extends Entity>, SearchQuery> searchQueries = new HashMap<>();

	private final ODSContext context;
	private final EntityLoader entityLoader;
	private final String esHost;
	private ODSFreeTextSearch freeTextSearch;

	public static final String PARAM_ELASTIC_SEARCH_URL = "elasticsearch.url";

	/**
	 * Constructor.
	 *
	 * @param context
	 *            Used to retrieve {@link ODSModelManager}.
	 * @param entityLoader
	 *            Used to load complete {@link Entity}s.
	 */
	public ODSSearchService(ODSContext context, QueryService queryService, EntityLoader entityLoader) {
		this.context = context;
		this.entityLoader = entityLoader;
		esHost = context.getParameters().get(PARAM_ELASTIC_SEARCH_URL);
		
		registerMergedSearchQuery(Project.class, c -> new ProjectSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(Pool.class, c -> new PoolSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(Test.class, c -> new TestSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(TestStep.class, c -> new TestStepSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(Measurement.class, c -> new MeasurementSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(ChannelGroup.class, c -> new ChannelGroupSearchQuery(context.getODSModelManager(), queryService, c));
		registerMergedSearchQuery(Channel.class, c -> new ChannelSearchQuery(context.getODSModelManager(), queryService, c));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Class<? extends Entity>> listSearchableTypes() {
		return new ArrayList<>(searchQueries.keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EntityType> listEntityTypes(Class<? extends Entity> entityClass) {
		return findSearchQuery(entityClass).listEntityTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Searchable getSearchableRoot(Class<? extends Entity> entityClass) {
		return findSearchQuery(entityClass).getSearchableRoot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Value> getFilterValues(Class<? extends Entity> entityClass, Attribute attribute, Filter filter) {
		return findSearchQuery(entityClass).getFilterValues(attribute, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> fetchComplete(Class<T> entityClass, List<EntityType> entityTypes,
			Filter filter) {
		return createResult(entityClass, findSearchQuery(entityClass).fetchComplete(entityTypes, filter));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> fetch(Class<T> entityClass, List<Attribute> attributes, Filter filter) {
		return createResult(entityClass, findSearchQuery(entityClass).fetch(attributes, filter));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Result> fetchResults(Class<? extends Entity> entityClass, List<Attribute> attributes, Filter filter,
			String query) {
		Filter mergedFilter = getMergedFilter(filter, query);
		if (mergedFilter.isEmtpty()) {
			return Collections.emptyList();
		}

		EntityType entityType = context.getODSModelManager().getEntityType(entityClass);
		Map<String, Result> recordsByEntityID = new HashMap<>();
		findSearchQuery(entityClass).fetch(attributes, mergedFilter).forEach(result -> recordsByEntityID.put(result.getRecord(entityType).getID(), result));

		return new ArrayList<>(recordsByEntityID.values());
	}

	@Override
	public Map<Class<? extends Entity>, List<Entity>> fetch(String query) {
		if (freeTextSearch == null) {
			initFreetextSearch();
		}

		return freeTextSearch.search(query);
	}

	@Override
	public boolean isTextSearchAvailable() {
		return true;
	}

	/**
	 * Returns a filter merged from an existing filter and a filter resulting
	 * from a freetext search result.
	 * 
	 * @param filter
	 *            first filter to merge
	 * @param query
	 *            freetext query, which returns the ids to generate the second
	 *            filter to merge
	 * @return conjunction of the first and second filter
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	protected Filter getMergedFilter(Filter filter, String query) {
		Preconditions.checkNotNull(filter, "Filter cannot be null!");

		Filter freetextIdsFilter = getFilterForFreetextQuery(query);
		// If freetext search query is provided but yields no results, return empty
		// filter for compatibility with previous behaviour:
		if (null == freetextIdsFilter) {
			return Filter.or();
		} else if (filter.isEmtpty()) {
			return freetextIdsFilter;
		} else {
			return Filter.and().merge(filter, freetextIdsFilter);
		}
	}

	/**
	 * Executes a free text search and returns the IDs of the matching entities.
	 * 
	 * @param query
	 *            search query
	 * @return found entity IDs grouped by entity.
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	protected Map<Class<? extends Entity>, List<String>> fetchIds(String query) {
		if (Strings.isNullOrEmpty(query)) {
			return Collections.emptyMap();
		}

		if (freeTextSearch == null) {
			initFreetextSearch();
		}

		return freeTextSearch.searchIds(query);
	}

	/**
	 * Delegates to {@link ODSFreeTextSearch} to retrieve a map of all entity IDs
	 * found by the given query. With the results a filter is generated, which can
	 * be used to query the entity instances of result of the free text query.
	 * 
	 * @param query
	 *            fulltext search query
	 * @return A map with the found entity IDs grouped by {@link Entity} class or
	 *         null if a query was provided but yielded no results.
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	private Filter getFilterForFreetextQuery(String query) {

		if (Strings.isNullOrEmpty(query)) {
			// No query provided => return empty filter for merging with other filters:
			return Filter.or();
		}
		
		Filter freeTextResultsFilter = null;
		for (Map.Entry<Class<? extends Entity>, List<String>> entry : fetchIds(query).entrySet()) {
			if (!entry.getValue().isEmpty()) {
				if (null == freeTextResultsFilter) {
					freeTextResultsFilter = Filter.or();
				}

				freeTextResultsFilter.ids(context.getODSModelManager().getEntityType(entry.getKey()), entry.getValue());
			}
		}

		return freeTextResultsFilter; // null if query yielded no results
	}

	/**
	 * Loads {@link Entity}s of given entity class for given {@link Result}s.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param entityClass
	 *            Entity class of the loaded {@code Entity}s.
	 * @param results
	 *            The queried {@code Result}s.
	 * @return All loaded entities are returned as a {@link List}.
	 * @throws DataAccessException
	 *             Thrown if unable to load the {@code Entity}s.
	 */
	private <T extends Entity> List<T> createResult(Class<T> entityClass, List<Result> results) {
		EntityType entityType = context.getODSModelManager().getEntityType(entityClass);
		Map<String, Result> recordsByEntityID = new HashMap<>();
		results.forEach(result -> recordsByEntityID.put(result.getRecord(entityType).getID(), result));

		Map<T, Result> resultsByEntity = new HashMap<>();
		entityLoader.loadAll(new Key<>(entityClass), recordsByEntityID.keySet()).forEach(entity -> resultsByEntity.put(entity, recordsByEntityID.get(entity.getID())));

		return new ArrayList<>(resultsByEntity.keySet());
	}

	/**
	 * Returns the {@link SearchQuery} for given entity class.
	 *
	 * @param entityClass
	 *            Used as identifier.
	 * @return The {@link SearchQuery}
	 */
	private SearchQuery findSearchQuery(Class<? extends Entity> entityClass) {
		SearchQuery searchQuery = searchQueries.get(entityClass);
		if (searchQuery == null) {
			throw new IllegalArgumentException(
					new StringBuilder().append("Search query for type '").append(entityClass.getSimpleName()).append("' not found.").toString());
		}

		return searchQuery;
	}

	/**
	 * Registers a {@link SearchQuery} for given entity class.
	 *
	 * @param entityClass
	 *            The entity class produced by using this query.
	 * @param factory
	 *            The {@code SearchQuery} factory.
	 */
	private void registerMergedSearchQuery(Class<? extends Entity> entityClass,
			Function<ContextState, BaseEntitySearchQuery> factory) {
		searchQueries.put(entityClass, new MergedSearchQuery(context.getODSModelManager().getEntityType(entityClass), factory));
	}

	private void initFreetextSearch() {
		List<Environment> all = entityLoader.loadAll(new Key<>(Environment.class), "*");
		if (all.isEmpty()) {
			throw new DataAccessException("No environment loaded. So the Search does not know where to search");
		}
		String sourceName = all.get(0).getSourceName();
		freeTextSearch = new ODSFreeTextSearch(entityLoader, sourceName, esHost);
	}
}
