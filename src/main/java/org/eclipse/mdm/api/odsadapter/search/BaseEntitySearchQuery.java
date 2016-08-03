/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.Searchable;
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

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager Used to load {@link EntityType}s.
	 * @param entityClass The source entity class of this search query.
	 * @param rootEntityClass The root entity class of this search query.
	 */
	protected BaseEntitySearchQuery(ODSModelManager modelManager, Class<? extends Entity> entityClass,
			Class<? extends Entity> rootEntityClass) {
		this.modelManager = modelManager;
		this.entityClass = entityClass;
		this.rootEntityClass = rootEntityClass;

		EntityConfig<?> entityConfig = modelManager.getEntityConfig(new Key<>(entityClass));
		EntityType source = entityConfig.getEntityType();

		entityConfig.getOptionalConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			joinTree.addNode(source, entityType, true, Join.OUTER);
		});

		entityConfig.getMandatoryConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			joinTree.addNode(source, entityType, true, Join.INNER);
		});
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
		Function<String, SearchableNode> factory = k -> {
			return new SearchableNode(modelManager.getEntityType(k));
		};

		Map<String, SearchableNode> nodes = new HashMap<>();
		for(Entry<String, List<String>> entry : joinTree.getTree().entrySet()) {
			SearchableNode parent = nodes.computeIfAbsent(entry.getKey(), factory);

			for(String childName : entry.getValue()) {
				parent.addRelated(nodes.computeIfAbsent(childName, factory));
			}
		}

		return nodes.get(modelManager.getEntityType(rootEntityClass).getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Value> getFilterValues(Attribute attribute, Filter filter) throws DataAccessException {
		Query query = modelManager.createQuery().select(attribute, Aggregation.DISTINCT).group(attribute);

		// add required joins
		filter.stream().filter(FilterItem::isCondition).map(FilterItem::getCondition)
		.forEach(c -> {
			addJoins(query, c.getAttribute().getEntityType());
		});

		return query.fetch(filter).stream().map(r -> r.getValue(attribute)).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<Result> fetchComplete(List<EntityType> entityTypes, Filter filter) throws DataAccessException {
		Query query = modelManager.createQuery().selectID(modelManager.getEntityType(entityClass));

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
	public final List<Result> fetch(List<Attribute> attributes, Filter filter) throws DataAccessException {
		Query query = modelManager.createQuery().selectID(modelManager.getEntityType(entityClass));

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
	 * Adds given {@link JoinConfig} to the internally managed {@link
	 * JoinTree}.
	 *
	 * @param joinConfig Will be added.
	 */
	protected final void addJoinConfig(JoinConfig joinConfig) {
		EntityConfig<?> targetEntityConfig = modelManager.getEntityConfig(new Key<>(joinConfig.target));
		EntityType target = targetEntityConfig.getEntityType();

		// add dependency source to target
		joinTree.addNode(modelManager.getEntityType(joinConfig.source), target, joinConfig.viaParent, Join.INNER);

		// add target's optional dependencies
		targetEntityConfig.getOptionalConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			joinTree.addNode(target, entityType, true, Join.OUTER);
		});

		// add target's mandatory dependencies
		targetEntityConfig.getMandatoryConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			joinTree.addNode(target, entityType, true, Join.INNER);
		});
	}

	/**
	 * Adds joins to context data according to the given {@link ContextState}.
	 *
	 * @param contextState The {@code ContextState}.
	 */
	protected final void addJoinConfig(ContextState contextState) {
		if(contextState == null) {
			// nothing to do
			return;
		}

		Class<? extends Entity> source = contextState.isOrdered() ? TestStep.class : Measurement.class;
		for(ContextType contextType : ContextType.values()) {
			EntityType rootEntityType = modelManager.getEntityType(ContextRoot.class, contextType);
			for(Relation componentRelation : rootEntityType.getChildRelations()) {
				joinTree.addNode(componentRelation.getSource(), componentRelation.getTarget(), true, Join.OUTER);

				for(Relation sensorRelation : componentRelation.getTarget().getChildRelations()) {
					joinTree.addNode(sensorRelation.getSource(), sensorRelation.getTarget(), true, Join.OUTER);
				}
			}

			joinTree.addNode(modelManager.getEntityType(source), rootEntityType, true, Join.OUTER);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Executes given {@link Query} using given {@link Filter}. Joins required
	 * for the given {@code Filter} will be implicitly added as needed.
	 *
	 * @param query Will be executed.
	 * @param filter The query filtering sequence.
	 * @return Returns the {@link Result}s in a {@code List}.
	 * @throws DataAccessException Thrown if failed to execute given {@code Query}.
	 */
	private List<Result> fetch(Query query, Filter filter) throws DataAccessException {
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
	 * Adds join statements to given target {@link EntityType} as needed to
	 * be able to execute given {@code Query}.
	 *
	 * @param query The {@link Query}.
	 * @param entityType The target {@link EntityType}.
	 */
	private void addJoins(Query query, EntityType entityType) {
		if(query.isQueried(entityType)) {
			return;
		}

		JoinNode joinNode = joinTree.getJoinNode(entityType.getName());
		EntityType sourceEntityType = modelManager.getEntityType(joinNode.source);
		addJoins(query, sourceEntityType);
		query.join(sourceEntityType.getRelation(entityType), joinNode.join);
	}

}
