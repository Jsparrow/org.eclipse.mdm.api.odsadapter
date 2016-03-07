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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.base.query.Searchable;

final class MergedSearchQuery<T extends BaseEntitySearchQuery> implements SearchQuery {

	private final Class<? extends Entity> type;
	private final ModelManager modelManager;

	private final T byResult;
	private final T byOrder;

	public MergedSearchQuery(Class<? extends Entity> type, ModelManager modelManager, Function<ContextState, T> factory) {
		this.type = type;
		this.modelManager = modelManager;

		byResult = factory.apply(ContextState.MEASURED);
		byOrder = factory.apply(ContextState.ORDERED);
	}

	@Override
	public List<EntityType> listEntityTypes() {
		return byOrder.listEntityTypes();
	}

	@Override
	public Searchable getSearchableRoot() {
		return byOrder.getSearchableRoot();
	}

	@Override
	public List<Value> getFilterValues(Attribute attribute, Filter filter) throws DataAccessException {
		List<Value> orderValues = byOrder.getFilterValues(attribute, filter);
		List<Value> resultValues = byResult.getFilterValues(attribute, filter);

		return Stream.concat(orderValues.stream(), resultValues.stream())
				// group by value and merge values
				.collect(groupingBy(v -> v.extract(), reducing((v1, v2) -> v1)))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	@Override
	public List<Result> fetchComplete(List<EntityType> entityTypes, Filter filter) throws DataAccessException {
		return mergeResults(byOrder.fetchComplete(entityTypes, filter), byResult.fetchComplete(entityTypes, filter));
	}

	@Override
	public List<Result> fetch(List<Attribute> attributes, Filter filter) throws DataAccessException {
		return mergeResults(byOrder.fetch(attributes, filter), byResult.fetch(attributes, filter));
	}

	private List<Result> mergeResults(List<Result> results1, List<Result> results2) {
		EntityType typeEntity = modelManager.getEntityType(type);
		return Stream.concat(results1.stream(), results2.stream())
				// group by instance ID and merge grouped results
				.collect(groupingBy(r -> r.getRecord(typeEntity).getID(), reducing(Result::merge)))
				// collect merged results
				.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

}
