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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.search.SearchQuery;
import org.eclipse.mdm.api.base.search.Searchable;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinNode;

/**
 * Base implementation for entity {@link SearchQuery}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
abstract class BaseEntitySearchQuery implements SearchQuery {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Class<? extends Entity> rootEntityClass;
	private final JoinTree joinTree = new JoinTree();
	private final Class<? extends Entity> entityClass;

	private final ODSModelManager modelManager;
	private final QueryService queryService;
	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to load {@link EntityType}s.
	 * @param entityClass
	 *            The source entity class of this search query.
	 * @param rootEntityClass
	 *            The root entity class of this search query.
	 */
	protected BaseEntitySearchQuery(ODSModelManager modelManager, QueryService queryService, Class<? extends Entity> entityClass,
			Class<? extends Entity> rootEntityClass) {
		this.modelManager = modelManager;
		this.queryService = queryService;
		this.entityClass = entityClass;
		this.rootEntityClass = rootEntityClass;

		EntityConfig<?> entityConfig = modelManager.getEntityConfig(new Key<>(entityClass));
		EntityType source = entityConfig.getEntityType();

		entityConfig.getOptionalConfigs().stream().map(EntityConfig::getEntityType).forEach(entityType -> joinTree.addNode(source, entityType, true, JoinType.OUTER));

		entityConfig.getMandatoryConfigs().stream().map(EntityConfig::getEntityType).forEach(entityType -> joinTree.addNode(source, entityType, true, JoinType.INNER));
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<EntityType> listEntityTypes() {
		return joinTree.getNodeNames().stream().map(modelManager::getEntityType).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Searchable getSearchableRoot() {
		Function<String, SearchableNode> factory = k -> new SearchableNode(modelManager.getEntityType(k));

		Map<String, SearchableNode> nodes = new HashMap<>();
		joinTree.getTree().entrySet().forEach(entry -> {
			SearchableNode parent = nodes.computeIfAbsent(entry.getKey(), factory);

			entry.getValue().forEach(childName -> parent.addRelated(nodes.computeIfAbsent(childName, factory)));
		});

		return nodes.get(modelManager.getEntityType(rootEntityClass).getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Value> getFilterValues(Attribute attribute, Filter filter) {
		Query query = queryService.createQuery().select(attribute, Aggregation.DISTINCT).group(attribute);

		// add required joins
		filter.stream().filter(FilterItem::isCondition).map(FilterItem::getCondition).forEach(c -> addJoins(query, c.getAttribute().getEntityType()));

		return query.fetch(filter).stream().map(r -> r.getValue(attribute)).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Result> fetchComplete(List<EntityType> entityTypes, Filter filter) {
		Query query = queryService.createQuery().selectID(modelManager.getEntityType(entityClass));

		// add required joins
		entityTypes.stream().forEach(entityType -> {
			addJoins(query, entityType);
			query.selectAll(entityType);
		});

		return fetch(query, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Result> fetch(List<Attribute> attributes, Filter filter) {
		Query query = queryService.createQuery().selectID(modelManager.getEntityType(entityClass));

		// add required joins
		attributes.stream().forEach(attribute -> {
			addJoins(query, attribute.getEntityType());
			query.select(attribute);
		});

		return fetch(query, filter);
	}

	// ======================================================================
	// Protected methods
	// ======================================================================

	/**
	 * Adds given {@link JoinConfig} to the internally managed {@link JoinTree}.
	 *
	 * @param joinConfig
	 *            Will be added.
	 */
	protected final void addJoinConfig(JoinConfig joinConfig) {
		EntityConfig<?> targetEntityConfig = modelManager.getEntityConfig(new Key<>(joinConfig.target));
		EntityType target = targetEntityConfig.getEntityType();

		// add dependency source to target
		joinTree.addNode(modelManager.getEntityType(joinConfig.source), target, joinConfig.viaParent, JoinType.INNER);

		// add target's optional dependencies
		targetEntityConfig.getOptionalConfigs().stream().map(EntityConfig::getEntityType).forEach(entityType -> joinTree.addNode(target, entityType, true, JoinType.OUTER));

		// add target's mandatory dependencies
		targetEntityConfig.getMandatoryConfigs().stream().map(EntityConfig::getEntityType).forEach(entityType -> joinTree.addNode(target, entityType, true, JoinType.INNER));
	}

	/**
	 * Adds joins to context data according to the given {@link ContextState}.
	 *
	 * @param contextState
	 *            The {@code ContextState}.
	 */
	protected final void addJoinConfig(ContextState contextState) {
		if (contextState == null) {
			// nothing to do
			return;
		}

		Class<? extends Entity> source = contextState.isOrdered() ? TestStep.class : Measurement.class;
		for (ContextType contextType : ContextType.values()) {
			EntityType rootEntityType = modelManager.getEntityType(ContextRoot.class, contextType);
			rootEntityType.getChildRelations().forEach(componentRelation -> {
				joinTree.addNode(componentRelation.getSource(), componentRelation.getTarget(), true, JoinType.OUTER);

				componentRelation.getTarget().getChildRelations().forEach(sensorRelation -> joinTree.addNode(sensorRelation.getSource(), sensorRelation.getTarget(), true, JoinType.OUTER));
			});

			joinTree.addNode(modelManager.getEntityType(source), rootEntityType, true, JoinType.OUTER);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Executes given {@link Query} using given {@link Filter}. Joins required
	 * for the given {@code Filter} will be implicitly added as needed.
	 *
	 * @param query
	 *            Will be executed.
	 * @param filter
	 *            The query filtering sequence.
	 * @return Returns the {@link Result}s in a {@code List}.
	 * @throws DataAccessException
	 *             Thrown if failed to execute given {@code Query}.
	 */
	private List<Result> fetch(Query query, Filter filter) {
		filter.stream().filter(FilterItem::isCondition).map(FilterItem::getCondition)
				.forEach(c -> addJoins(query, c.getAttribute().getEntityType()));

		EntityType entityType = modelManager.getEntityType(entityClass);
		return query.order(entityType.getIDAttribute()).fetch(filter).stream()
				// group by instance ID and merge grouped results
				.collect(groupingBy(r -> r.getRecord(entityType).getID(), reducing(Result::merge)))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	/**
	 * Adds join statements to given target {@link EntityType} as needed to be
	 * able to execute given {@code Query}.
	 *
	 * @param query
	 *            The {@link Query}.
	 * @param entityType
	 *            The target {@link EntityType}.
	 */
	private void addJoins(Query query, EntityType entityType) {
		if (query.isQueried(entityType)) {
			return;
		}

		JoinNode joinNode = joinTree.getJoinNode(entityType.getName());
		EntityType sourceEntityType = modelManager.getEntityType(joinNode.source);
		addJoins(query, sourceEntityType);
		query.join(sourceEntityType.getRelation(entityType), joinNode.joinType);
	}

}
