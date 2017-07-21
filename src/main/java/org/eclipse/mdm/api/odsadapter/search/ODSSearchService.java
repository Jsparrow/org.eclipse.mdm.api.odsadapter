/*
 * Copyright (c) 2016 Peak Solution GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Environment;
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
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
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

	private final ODSModelManager modelManager;
	private final EntityLoader entityLoader;
	private final String esHost;
	private ODSFreeTextSearch freeTextSearch;

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to retrieve {@link EntityType}s.
	 * @param entityLoader
	 *            Used to load complete {@link Entity}s.
	 */
	public ODSSearchService(ODSModelManager modelManager, EntityLoader entityLoader, String host) {
		this.modelManager = modelManager;
		this.entityLoader = entityLoader;
		esHost = host;

		registerMergedSearchQuery(Project.class, c -> new ProjectSearchQuery(modelManager, c));
		registerMergedSearchQuery(Pool.class, c -> new PoolSearchQuery(modelManager, c));
		registerMergedSearchQuery(Test.class, c -> new TestSearchQuery(modelManager, c));
		registerMergedSearchQuery(TestStep.class, c -> new TestStepSearchQuery(modelManager, c));
		registerMergedSearchQuery(Measurement.class, c -> new MeasurementSearchQuery(modelManager, c));
		registerMergedSearchQuery(ChannelGroup.class, c -> new ChannelGroupSearchQuery(modelManager, c));
		registerMergedSearchQuery(Channel.class, c -> new ChannelSearchQuery(modelManager, c));
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
	public List<Value> getFilterValues(Class<? extends Entity> entityClass, Attribute attribute, Filter filter)
			throws DataAccessException {
		return findSearchQuery(entityClass).getFilterValues(attribute, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> Map<T, Result> fetchComplete(Class<T> entityClass, List<EntityType> entityTypes,
			Filter filter) throws DataAccessException {
		return createResult(entityClass, findSearchQuery(entityClass).fetchComplete(entityTypes, filter));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> Map<T, Result> fetch(Class<T> entityClass, List<Attribute> attributes, Filter filter)
			throws DataAccessException {
		return createResult(entityClass, findSearchQuery(entityClass).fetch(attributes, filter));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Result> fetchResults(Class<? extends Entity> entityClass, List<Attribute> attributes, Filter filter,
			String query) throws DataAccessException {
		Filter mergedFilter = getMergedFilter(filter, query);
		if (mergedFilter.isEmtpty()) {
			return Collections.emptyList();
		}

		EntityType entityType = modelManager.getEntityType(entityClass);
		Map<String, Result> recordsByEntityID = new HashMap<>();
		for (Result result : findSearchQuery(entityClass).fetch(attributes, mergedFilter)) {
			recordsByEntityID.put(result.getRecord(entityType).getID(), result);
		}

		return new ArrayList<>(recordsByEntityID.values());
	}

	@Override
	public Map<Class<? extends Entity>, List<Entity>> fetch(String query) throws DataAccessException {
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
	 *            filer to merge
	 * @return conjunction of the first and second filter
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	protected Filter getMergedFilter(Filter filter, String query) throws DataAccessException {
		Preconditions.checkNotNull(filter, "Filter cannot be null!");

		Filter freetextIdsFilter = getFilterForFreetextQuery(query);
		if (filter.isEmtpty()) {
			return freetextIdsFilter;
		} else if (freetextIdsFilter.isEmtpty()) {
			return filter;
		} else {
			return Filter.and().merge(filter, freetextIdsFilter);
		}
	}

	/**
	 * Executes a free text search and returns the IDs of the matching enities.
	 * 
	 * @param query
	 *            search query
	 * @return found entity IDs grouped by entity.
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	protected Map<Class<? extends Entity>, List<String>> fetchIds(String query) throws DataAccessException {
		if (Strings.isNullOrEmpty(query)) {
			return Collections.emptyMap();
		}

		if (freeTextSearch == null) {
			initFreetextSearch();
		}

		return freeTextSearch.searchIds(query);
	}

	/**
	 * Delegates to {@link ODSFreeTextSearch} to retrieves a map of all entity
	 * IDs found by the given query. With the results a filter is generated,
	 * which can be used to query the entity instances of result of the free
	 * text query.
	 * 
	 * @param query
	 *            fulltext search query
	 * @return A map with the found entity IDs grouped by {@link Entity} class.
	 * @throws DataAccessException
	 *             Thrown if {@link ODSFreeTextSearch} is unavailable or cannot
	 *             execute the query.
	 */
	private Filter getFilterForFreetextQuery(String query) throws DataAccessException {

		Filter freeTextResultsFilter = Filter.or();
		for (Map.Entry<Class<? extends Entity>, List<String>> entry : fetchIds(query).entrySet()) {
			if (!entry.getValue().isEmpty()) {
				freeTextResultsFilter.ids(modelManager.getEntityType(entry.getKey()), entry.getValue());
			}
		}

		return freeTextResultsFilter;
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
	 * @return All Results are returned in a Map, which maps entities to the
	 *         corresponding results.
	 * @throws DataAccessException
	 *             Thrown if unable to load the {@code Entity}s.
	 */
	private <T extends Entity> Map<T, Result> createResult(Class<T> entityClass, List<Result> results)
			throws DataAccessException {
		EntityType entityType = modelManager.getEntityType(entityClass);
		Map<String, Result> recordsByEntityID = new HashMap<>();
		for (Result result : results) {
			recordsByEntityID.put(result.getRecord(entityType).getID(), result);
		}

		Map<T, Result> resultsByEntity = new HashMap<>();
		for (T entity : entityLoader.loadAll(new Key<>(entityClass), recordsByEntityID.keySet())) {
			resultsByEntity.put(entity, recordsByEntityID.get(entity.getID()));
		}

		return resultsByEntity;
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
					"Search query for type '" + entityClass.getSimpleName() + "' not found.");
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
		searchQueries.put(entityClass, new MergedSearchQuery(modelManager.getEntityType(entityClass), factory));
	}

	private void initFreetextSearch() throws DataAccessException {
		List<Environment> all = entityLoader.loadAll(new Key<>(Environment.class), "*");
		if (all.isEmpty()) {
			throw new DataAccessException("No environment loaded. So the Search does not know where to search");
		}
		String sourceName = all.get(0).getSourceName();
		freeTextSearch = new ODSFreeTextSearch(entityLoader, sourceName, esHost);
	}
}
