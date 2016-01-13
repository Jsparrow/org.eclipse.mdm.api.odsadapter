/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.Searchable;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

abstract class AbstractDataItemSearchQuery implements SearchQuery {

	private final Map<String, DependencyRelation> dependencyRelations = new HashMap<>();

	private final Map<String, List<String>> dependencyMap = new HashMap<>();
	private final ODSQueryService queryService;

	private final Class<? extends DataItem> root;
	private final Class<? extends DataItem> type;

	protected AbstractDataItemSearchQuery(ODSQueryService queryService, Class<? extends DataItem> type,
			Class<? extends DataItem> root, Optional<ContextState> contextState) {
		this.queryService = queryService;
		this.root = root;
		this.type = type;

		contextState.ifPresent(c -> addDependencyToContext(c.getType()));
	}

	@Override
	public final List<Entity> listEntities() {
		return dependencyRelations.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue().source))
				.distinct().map(queryService::getEntity).collect(Collectors.toList());
	}

	@Override
	public final Searchable getSearchableRoot() {
		Map<String, SearchableNode> nodes = new HashMap<>();
		List<Entity> implicitEntities = queryService.getImplicitEntities(type);
		Function<String, SearchableNode> factory = k -> createSearchable(k, implicitEntities);

		for(Entry<String, List<String>> entry : dependencyMap.entrySet()) {
			SearchableNode parent = nodes.computeIfAbsent(entry.getKey(), factory);

			for(String childName : entry.getValue()) {
				parent.addRelated(nodes.computeIfAbsent(childName, factory));
			}
		}

		return nodes.get(ODSUtils.getAEName(root));
	}

	@Override
	public final List<Value> getFilterValues(Attribute attribute, Filter filter) throws DataAccessException {
		Query query = queryService.createQuery().select(attribute, Aggregation.DISTINCT).group(attribute);
		return query.fetch(filter).stream().map(r -> r.getValue(attribute)).collect(Collectors.toList());
	}

	@Override
	public final List<Result> fetchComplete(List<Entity> entities, Filter filter) throws DataAccessException {
		Query query = queryService.createQuery(type);
		entities.stream().forEach(e -> addJoins(query, e));
		/**
		 * TODO remove implicit selected / related entities? e.g.: Test & User
		 */
		query.selectAll(entities);
		return fetch(query, filter);
	}

	@Override
	public final List<Result> fetch(List<Attribute> attributes, Filter filter) throws DataAccessException {
		Query query = queryService.createQuery(type);
		attributes.stream().forEach(a -> addJoins(query, a.getEntity()));
		/**
		 * TODO remove implicit selected / related attributes? e.g.: Test & User
		 */
		query.select(attributes);
		return fetch(query, filter);
	}

	protected final void addDependency(Class<? extends DataItem> targetType, Class<? extends DataItem> sourceType,
			boolean viaParent, Join join) {
		addDependency(ODSUtils.getAEName(targetType), ODSUtils.getAEName(sourceType), viaParent, join);
	}

	private List<Result> fetch(Query query, Filter filter) throws DataAccessException {
		filter.stream().filter(FilterItem::isCondition).map(FilterItem::getCondition)
		.forEach(c -> addJoins(query, c.getAttribute().getEntity()));

		Entity typeEntity = queryService.getEntity(type);
		return query.order(typeEntity.getIDAttribute()).fetch(filter).stream()
				// group by instance ID and merge grouped results
				.collect(groupingBy(r -> r.getRecord(typeEntity).getID(), reducing((r1, r2) -> r1.merge(r2))))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	private final void addDependencyToContext(Class<? extends ContextDescribable> type) {
		for(ContextType contextType : ContextType.values()) {
			Entity contextRootEntity = queryService.getEntity(contextType);
			for(Relation relation : contextRootEntity.getRelations(Relationship.FATHER_CHILD)) {
				addDependency(relation.getTarget().getName(), relation.getSource().getName(), true, Join.OUTER);

				for(Relation sensorRelation : relation.getTarget().getChildRelations()) {
					addDependency(sensorRelation.getTarget().getName(), sensorRelation.getSource().getName(), true, Join.OUTER);
				}
			}

			addDependency(contextRootEntity.getName(), ODSUtils.getAEName(type), true, Join.OUTER);
		}
	}

	private void addJoins(Query query, Entity entity) {
		if(query.isQueried(entity)) {
			return;
		}

		DependencyRelation dependencyRelation = dependencyRelations.get(entity.getName());
		if(dependencyRelation == null) {
			throw new IllegalArgumentException("Missing join relation for entity '" + entity + "'.");
		}

		Entity sourceEntity = queryService.getEntity(dependencyRelation.source);
		addJoins(query, sourceEntity);
		query.join(sourceEntity.getRelation(entity), dependencyRelation.join);
	}


	private void addDependency(String targetName, String sourceName, boolean viaParent, Join join) {
		if(dependencyRelations.put(targetName, new DependencyRelation(sourceName, join)) != null) {
			throw new IllegalArgumentException("Target entity is not allowed to depend on multiple source entities.");
		}

		if(viaParent) {
			dependencyMap.computeIfAbsent(sourceName, k -> new ArrayList<>()).add(targetName);
		} else {
			dependencyMap.computeIfAbsent(targetName, k -> new ArrayList<>()).add(sourceName);
		}
	}

	private SearchableNode createSearchable(String name, List<Entity> implicitEntities) {
		Entity entity = queryService.getEntity(name);
		return new SearchableNode(entity, implicitEntities.contains(entity));
	}

	private static final class DependencyRelation {

		private final String source;
		private final Join join;

		private DependencyRelation(String source, Join join) {
			this.source = source;
			this.join = join;
		}

	}

}
