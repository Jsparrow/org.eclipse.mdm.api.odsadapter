/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ElemResultSetExt;
import org.asam.ods.JoinDef;
import org.asam.ods.NameValueSeqUnitId;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.ResultSetExt;
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
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODSQuery implements Query {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSQuery.class);

	private static final String GROUPE_NAME = "name";
	private static final Pattern AGGREGATION_NAME_PATTERN = Pattern.compile("\\S+\\((?<" + GROUPE_NAME + ">\\S+)\\)");

	private final Map<Long, EntityType> entityTypesByID = new HashMap<>();
	private final Set<EntityType> queriedEntityTypes = new HashSet<>();

	private final List<SelAIDNameUnitId> anuSeq = new ArrayList<>();

	private final List<JoinDef> joinSeq = new ArrayList<>();

	private final List<AIDName> groupBy = new ArrayList<>();

	private final List<SelOrder> orderBy = new ArrayList<>();

	private final ApplElemAccess applElemAccess;

	ODSQuery(ApplElemAccess applElemAccess) {
		this.applElemAccess = applElemAccess;
	}

	@Override
	public boolean isQueried(EntityType entityType) {
		return queriedEntityTypes.contains(entityType);
	}

	@Override
	public Query select(Attribute attribute, Aggregation aggregation, String unit) {
		EntityType entityType = attribute.getEntityType();
		entityTypesByID.put(ODSConverter.fromODSLong(((ODSEntityType) entityType).getODSID()), entityType);
		queriedEntityTypes.add(entityType);
		anuSeq.add(createSelect(attribute, aggregation, unit));
		return this;
	}

	@Override
	public Query join(Relation relation, Join join) {
		queriedEntityTypes.add(relation.getSource());
		queriedEntityTypes.add(relation.getTarget());
		joinSeq.add(createJoin(relation, join));
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

	@Override
	public Query order(Attribute attribute, boolean ascending) {
		orderBy.add(createOrderBy(attribute, ascending));
		return this;
	}

	@Override
	public Optional<Result> fetchSingleton(Filter filter) throws DataAccessException {
		List<Result> results = fetch(filter);
		if(results.isEmpty()) {
			return Optional.empty();
		} else if(results.size() > 1) {
			throw new DataAccessException("Multiple results found after executing the singleton query!");
		}

		return Optional.of(results.get(0));
	}

	@Override
	public List<Result> fetch(Filter filter) throws DataAccessException {
		try {

			List<SelItem> condSeq = new ArrayList<>();
			int condCount = 0;
			for(FilterItem conditionItem : filter) {
				SelItem selItem = new SelItem();
				if(conditionItem.isCondition()) {
					selItem.value(createCondition(conditionItem.getCondition()));
				} else if(conditionItem.isOperator()) {
					selItem._operator(ODSUtils.OPERATORS.convert(conditionItem.getOperator()));
					condCount++;
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

			List<Result> results = new ArrayList<>();
			long start = System.currentTimeMillis();
			for(ResultSetExt resultSetExt : applElemAccess.getInstancesExt(qse, 0)) {
				for(Result result : new ODSResultSEQ(resultSetExt)) {
					results.add(result);
				}
			}
			long stop = System.currentTimeMillis();

			LOGGER.debug("Query executed in {} ms and retrieved {} result rows ({} selections, {} conditions, "
					+ "{} joins).", stop - start, results.size(), anuSeq.size(), condCount, joinSeq.size());
			return results;
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	private SelAIDNameUnitId createSelect(Attribute attribute, Aggregation aggregation, String unit) {
		SelAIDNameUnitId sanu = new SelAIDNameUnitId();

		sanu.aggregate = ODSUtils.AGGREGATIONS.convert(aggregation);
		sanu.attr = createAIDName(attribute);
		sanu.unitId = ODSConverter.toODSLong(0L); // TODO unit

		return sanu;
	}

	private SelValueExt createCondition(Condition condition) throws DataAccessException {
		SelValueExt sve = new SelValueExt();

		sve.oper = ODSUtils.OPERATIONS.convert(condition.getOperation());
		sve.attr = new AIDNameUnitId();
		sve.attr.unitId = new T_LONGLONG(); // TODO condition.getValue().getUnit();
		sve.attr.attr = createAIDName(condition.getAttribute());
		sve.value = ODSConverter.toODSValue(condition.getValue());

		return sve;
	}

	private JoinDef createJoin(Relation relation, Join join) {
		JoinDef joinDef = new JoinDef();

		joinDef.fromAID = ((ODSEntityType) relation.getSource()).getODSID();
		joinDef.toAID = ((ODSEntityType) relation.getTarget()).getODSID();
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

		aidName.aid = ((ODSEntityType) attribute.getEntityType()).getODSID();
		aidName.aaName = attribute.getName();

		return aidName;
	}

	private final class ODSResultSEQ implements Iterable<Result>, Iterator<Result> {

		private final List<ODSRecordSEQ> odsRecordSEQs = new ArrayList<>();
		private final int length;
		private int index;

		public ODSResultSEQ(ResultSetExt resultSetExt) throws DataAccessException {
			for(ElemResultSetExt elemResultSetExt : resultSetExt.firstElems) {
				odsRecordSEQs.add(new ODSRecordSEQ(elemResultSetExt));
			}

			length = odsRecordSEQs.isEmpty() ? 0 : odsRecordSEQs.get(0).getLength();
		}

		@Override
		public boolean hasNext() {
			return index < length;
		}

		@Override
		public Result next() {
			if(!hasNext()) {
				throw new NoSuchElementException("No such element available.");
			}
			Result result = new Result();

			for(ODSRecordSEQ odsRecordSEQ : odsRecordSEQs) {
				result.addRecord(odsRecordSEQ.createRecord(index));
			}

			index++;
			return result;
		}

		@Override
		public Iterator<Result> iterator() {
			return this;
		}

	}

	private final class ODSRecordSEQ {

		private final List<ODSValueSeq> odsValueSeqs = new ArrayList<>();
		private final EntityType entityType;

		private ODSRecordSEQ(ElemResultSetExt elemResultSetExt) throws DataAccessException {
			entityType = entityTypesByID.get(ODSConverter.fromODSLong(elemResultSetExt.aid));
			for (NameValueSeqUnitId nvsui : elemResultSetExt.values) {
				Matcher matcher = AGGREGATION_NAME_PATTERN.matcher(nvsui.valName);
				String attributeName = matcher.matches() ? matcher.group(GROUPE_NAME) : nvsui.valName;
				odsValueSeqs.add(new ODSValueSeq(entityType.getAttribute(attributeName), nvsui));
			}
		}

		private int getLength() {
			return odsValueSeqs.isEmpty() ? 0 : odsValueSeqs.get(0).getLength();
		}

		private Record createRecord(int index) {
			Record record = new Record(entityType);
			for(ODSValueSeq odsValueSEQ : odsValueSeqs) {
				record.addValue(odsValueSEQ.createValue(index));
			}

			return record;
		}

	}

	private final class ODSValueSeq {

		private final String unit;
		private final int length;

		private final List<Value> values;

		public ODSValueSeq(Attribute attribute, NameValueSeqUnitId nvsui) throws DataAccessException {
			length = nvsui.value.flag.length;
			unit = ""; // TODO UNIT MAPPING
			values = ODSConverter.fromODSValueSeq(attribute, unit, nvsui.value);
		}

		private int getLength() {
			return length;
		}

		private Value createValue(int index) {
			return values.get(index);
		}

	}

}
