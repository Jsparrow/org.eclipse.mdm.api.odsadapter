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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameUnitId;
import org.asam.ods.AoException;
import org.asam.ods.JoinDef;
import org.asam.ods.NameValueUnit;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.SelAIDNameUnitId;
import org.asam.ods.SelItem;
import org.asam.ods.SelOrder;
import org.asam.ods.SelValueExt;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Condition;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.odscache.ExtQueryResult;
import org.eclipse.mdm.api.odsadapter.odscache.ODSCache;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSQuery implements Query {
	
	private final ODSCache odsCache;
	
	private Set<Entity> selects = new HashSet<>();

	private final List<SelAIDNameUnitId> anuSeq = new ArrayList<SelAIDNameUnitId>();

	private final List<JoinDef> joinSeq = new ArrayList<JoinDef>();

	private final List<AIDName> groupBy = new ArrayList<AIDName>();

	private final List<SelOrder> orderBy = new ArrayList<SelOrder>();
	
	ODSQuery(ODSCache odsCache) {		
		this.odsCache = odsCache;
	}

	private ODSQuery(ODSQuery query) {
		odsCache = query.odsCache;
		
		selects.addAll(query.selects);
		anuSeq.addAll(query.anuSeq);
		joinSeq.addAll(query.joinSeq);
		groupBy.addAll(query.groupBy);
		orderBy.addAll(query.orderBy);
	}
	
	@Override
	public Query selectAll(List<Entity> entities) {
		for(Entity entity : entities) {
			selectAll(entity);
		}
		return this;
	}
	
	@Override
	public Query selectAll(Entity entity) {
		return select(entity.getAttributes());
	}
	
	public Query select(Entity entity, String... names) {
		for (String name : names) {
			select(entity.getAttribute(name));
		}
		return this;
	}
	
	public Query selectID(Entity entity) {
		return select(entity.getIDAttribute());
	}
	
	@Override
	public Query select(List<Attribute> attributes) {
		for(Attribute attribute : attributes) {
			select(attribute);
		}
		return this;
	}
	
	public Query select(Attribute attribute) {
		return select(attribute, Aggregation.NONE);
	}

	@Override
	public Query select(Attribute attribute, Aggregation aggregation) {
		selects.add(attribute.getEntity());
		anuSeq.add(createSelect(attribute, aggregation));
		return this;
	}
	
	@Override
	public Query join(Relation relation, Join join) {
		joinSeq.add(createJoin(relation, join));
		return this;
	}

	@Override
	public Query join(Relation relation) {
		return join(relation, Join.INNER);
	}

	@Override
	public Query join(List<Relation> relations) {
		for(Relation rel : relations) {
			join(rel, Join.INNER);
		}
		return this;
	}

	@Override
	public Query group(List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			group(attribute);
		}
		return this;
	}
	
	@Override
	public Query group(Attribute attribute) {
		groupBy.add(createAIDName(attribute));
		return this;
	}
	
	public Query order(Attribute attribute) {
		return order(attribute, true);
	}

	@Override
	public Query order(Attribute attribute, boolean ascending) {
		orderBy.add(createOrderBy(attribute, ascending));
		return this;
	}
	
	public Result fetchSingleton() throws DataAccessException {
		return fetchSingleton(Filter.empty());
	}
	
	public Result fetchSingleton(Filter filter) throws DataAccessException {
		List<Result> results = fetch(filter);		
		if(results.isEmpty()) {
			Result result = new Result();
			for(Entity select : this.selects) {
				result.addRecord(select, new HashMap<String, Value>());
			}
			return result;
		}
		
		if(results.size() > 1) {
			throw new DataAccessException("multiple results found after executing the singleton query!");
		}
		
		return results.get(0);
	}
	
	public List<Result> fetch() throws DataAccessException {
		return fetch(Filter.empty());
	}
	
	public List<Result> fetch(Filter filter) throws DataAccessException {
		try {

			List<SelItem> condSeq = new ArrayList<SelItem>();
			for(FilterItem conditionItem : filter) {
				SelItem selItem = new SelItem();
				if(conditionItem.isCondition()) {
					selItem.value(createCondition(conditionItem.getCondition()));
				} else if(conditionItem.isOperator()) {
					selItem.operator(ODSUtils.OPERATORS.convert(conditionItem.getOperator()));
				} else {
					throw new IllegalArgumentException("Passed filter item is neither an operator nor a condition.");
				}
				
				condSeq.add(selItem);
			}
			
		    QueryStructureExt qse = new QueryStructureExt();
		    qse.anuSeq = anuSeq.toArray(new SelAIDNameUnitId[anuSeq.size()]);
		    qse.condSeq = condSeq.toArray(new SelItem[condSeq.size()]);
		    qse.groupBy = groupBy.toArray(new AIDName[groupBy.size()]);
		    qse.joinSeq = joinSeq.toArray(new JoinDef[joinSeq.size()]);
		    qse.orderBy = orderBy.toArray(new SelOrder[orderBy.size()]);

			ExtQueryResult res = new ExtQueryResult(odsCache, odsCache.getAoSession().getApplElemAccess().getInstancesExt(qse, 0));
			
			List<Result> resultList = new ArrayList<Result>();
			
			while(res.next()) {
				Result result = new Result();
				
				for(Entity select : this.selects) {
					NameValueUnit[] nvus = res.getNameValueUnitSeq(select.getName());
					Map<String, Value> values = ODSUtils.nameValueUnits2Values(nvus);
					result.addRecord(select, values);
				}
				
				resultList.add(result);
			}
			return resultList;
			
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} 
	}
	
	public Query clone() {
		return new ODSQuery(this);
	}
	
	private SelAIDNameUnitId createSelect(Attribute attribute, Aggregation aggregation) {
		SelAIDNameUnitId sanu = new SelAIDNameUnitId();
		
		sanu.aggregate = ODSUtils.AGGREGATIONS.convert(aggregation);
		sanu.attr = createAIDName(attribute);
		sanu.unitId = new T_LONGLONG();
		
		return sanu;
	}
	
	private SelValueExt createCondition(Condition condition) throws DataAccessException {
		SelValueExt sve = new SelValueExt();
		
		sve.oper = ODSUtils.OPERATIONS.convert(condition.getOperation());
		sve.attr = new AIDNameUnitId();
		sve.attr.unitId = new T_LONGLONG();
		sve.attr.attr = createAIDName(condition.getAttribute());
		sve.value = ODSUtils.value2TSValue(condition.getValue());
		
		return sve;
	}

	private JoinDef createJoin(Relation relation, Join join) {
		JoinDef joinDef = new JoinDef();
		
		joinDef.fromAID = ((ODSEntity) relation.getSource()).getODSID();
		joinDef.toAID = ((ODSEntity) relation.getTarget()).getODSID();
		joinDef.refName = relation.getName();
		joinDef.joiningType = ODSUtils.JOINS.convert(join);
		
		return joinDef;
	}
	
	private SelOrder createOrderBy(Attribute attribute, boolean ascending) {
		SelOrder selOrder = new SelOrder();
		
		selOrder.attr = createAIDName(attribute);
		selOrder.ascending = ascending;
		
		return selOrder;
	}
	
	private AIDName createAIDName(Attribute attribute) {
		AIDName aidName = new AIDName();
		
		aidName.aid = ((ODSEntity) attribute.getEntity()).getODSID();
		aidName.aaName = attribute.getName();
		
		return aidName;
	}
	
}
