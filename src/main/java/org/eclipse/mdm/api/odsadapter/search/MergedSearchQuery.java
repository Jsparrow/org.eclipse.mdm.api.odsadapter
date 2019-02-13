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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.search.SearchQuery;
import org.eclipse.mdm.api.base.search.Searchable;

/**
 * Merges 2 distinct search queries, where one queries context data as ordered
 * and the other context as measured.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class MergedSearchQuery implements SearchQuery {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final EntityType entityType;

	private final BaseEntitySearchQuery byResult;
	private final BaseEntitySearchQuery byOrder;

	// ======================================================================
	// Constructors
	// ======================================================================

	MergedSearchQuery(EntityType entityType, Function<ContextState, BaseEntitySearchQuery> factory) {
		this.entityType = entityType;

		byResult = factory.apply(ContextState.MEASURED);
		byOrder = factory.apply(ContextState.ORDERED);
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EntityType> listEntityTypes() {
		return byOrder.listEntityTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Searchable getSearchableRoot() {
		return byOrder.getSearchableRoot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Value> getFilterValues(Attribute attribute, Filter filter) {
		List<Value> orderValues = byOrder.getFilterValues(attribute, filter);
		List<Value> resultValues = byResult.getFilterValues(attribute, filter);

		return Stream.concat(orderValues.stream(), resultValues.stream())
				// group by value and merge values
				.collect(groupingBy(Value::extract, reducing((v1, v2) -> v1)))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Result> fetchComplete(List<EntityType> entityTypes, Filter filter) {
		return mergeResults(byOrder.fetchComplete(entityTypes, filter), byResult.fetchComplete(entityTypes, filter));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Result> fetch(List<Attribute> attributes, Filter filter) {
		return mergeResults(byOrder.fetch(attributes, filter), byResult.fetch(attributes, filter));
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Merges given {@link Result}s to one using the root entity type of this
	 * search query.
	 *
	 * @param results1
	 *            The first {@code Result}.
	 * @param results2
	 *            The second {@code Result}.
	 * @return The merged {@link Result} is returned.
	 */
	private List<Result> mergeResults(List<Result> results1, List<Result> results2) {
		return Stream.concat(results1.stream(), results2.stream())
				// group by instance ID and merge grouped results
				.collect(groupingBy(r -> r.getRecord(entityType).getID(), reducing(Result::merge)))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

}
