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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
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

abstract class BaseEntitySearchQuery implements SearchQuery {

	private final Set<String> implicitTypeNames = new HashSet<>();

	private final JoinTree joinTree = new JoinTree();
	private final Class<? extends Entity> entityClass;

	private final ODSModelManager modelManager;

	protected BaseEntitySearchQuery(ODSModelManager modelManager, Class<? extends Entity> entityClass) {
		this.modelManager = modelManager;
		this.entityClass = entityClass;

		EntityConfig<?> entityConfig = modelManager.getEntityConfig(new Key<>(entityClass));
		EntityType source = entityConfig.getEntityType();

		implicitTypeNames.add(source.getName());

		entityConfig.getOptionalConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			implicitTypeNames.add(entityType.getName());
			joinTree.addNode(source, entityType, true, Join.OUTER);
		});

		entityConfig.getMandatoryConfigs().stream().map(EntityConfig::getEntityType)
		.forEach(entityType -> {
			implicitTypeNames.add(entityType.getName());
			joinTree.addNode(source, entityType, true, Join.INNER);
		});
	}

	@Override
	public final List<EntityType> listEntityTypes() {
		return joinTree.getNodeNames().stream().map(modelManager::getEntityType).collect(Collectors.toList());
	}

	@Override
	public final Searchable getSearchableRoot() {
		Function<String, SearchableNode> factory = k -> {
			EntityType entityType = modelManager.getEntityType(k);
			return new SearchableNode(entityType, implicitTypeNames.contains(entityType.getName()));
		};

		Map<String, SearchableNode> nodes = new HashMap<>();
		for(Entry<String, List<String>> entry : joinTree.getTree().entrySet()) {
			SearchableNode parent = nodes.computeIfAbsent(entry.getKey(), factory);

			for(String childName : entry.getValue()) {
				parent.addRelated(nodes.computeIfAbsent(childName, factory));
			}
		}

		return nodes.get(modelManager.getEntityType(Test.class).getName());
	}

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

	@Override
	public final List<Result> fetchComplete(List<EntityType> entityTypes, Filter filter) throws DataAccessException {
		Query query = modelManager.createQuery().selectID(modelManager.getEntityType(entityClass));

		// add required joins
		entityTypes.stream().filter(et -> !implicitTypeNames.contains(et.getName()))
		.forEach(entityType -> {
			addJoins(query, entityType);
			query.selectAll(entityType);
		});

		return fetch(query, filter);
	}

	@Override
	public final List<Result> fetch(List<Attribute> attributes, Filter filter) throws DataAccessException {
		Query query = modelManager.createQuery().selectID(modelManager.getEntityType(entityClass));

		// add required joins
		attributes.stream().filter(a -> !implicitTypeNames.contains(a.getEntityType().getName()))
		.forEach(attribute -> {
			addJoins(query, attribute.getEntityType());
			query.select(attribute);
		});

		return fetch(query, filter);
	}

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
