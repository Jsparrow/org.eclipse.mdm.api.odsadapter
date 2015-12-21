/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ElemResultSetExt;
import org.asam.ods.JoinDef;
import org.asam.ods.NameValueSeqUnitId;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.ResultSetExt;
import org.asam.ods.SelAIDNameUnitId;
import org.asam.ods.SelItem;
import org.asam.ods.SelOrder;
import org.asam.ods.SelValueExt;
import org.asam.ods.TS_ValueSeq;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Condition;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSQuery implements Query {

	private static final String GROUPE_NAME = "name";
	private static final Pattern AGGREGATION_NAME_PATTERN = Pattern.compile("\\S+\\((?<" + GROUPE_NAME + ">\\S+)\\)");

	private Set<Entity> queriedEntities = new HashSet<>();

	private Set<Entity> selects = new LinkedHashSet<>();

	private final List<SelAIDNameUnitId> anuSeq = new ArrayList<SelAIDNameUnitId>();

	private final List<JoinDef> joinSeq = new ArrayList<JoinDef>();

	private final List<AIDName> groupBy = new ArrayList<AIDName>();

	private final List<SelOrder> orderBy = new ArrayList<SelOrder>();

	private final ODSQueryService queryService;

	ODSQuery(ODSQueryService queryService) {
		this.queryService = queryService;
	}

	@Override
	public boolean isQueried(Entity entity) {
		return queriedEntities.contains(entity);
	}

	@Override
	public Query select(Attribute attribute, Aggregation aggregation, String unit) {
		selects.add(attribute.getEntity());
		queriedEntities.add(attribute.getEntity());
		anuSeq.add(createSelect(attribute, aggregation, unit));
		return this;
	}

	@Override
	public Query join(Relation relation, Join join) {
		queriedEntities.add(relation.getSource());
		queriedEntities.add(relation.getTarget());
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

			List<Result> results = new ArrayList<Result>();

			for(ResultSetExt resultSetExt : queryService.getApplElemAccess().getInstancesExt(qse, 0)) {
				for(Result result : new ODSResultSEQ(resultSetExt)) {
					results.add(result);
				}
			}

			return results;

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	private SelAIDNameUnitId createSelect(Attribute attribute, Aggregation aggregation, String unit) {
		SelAIDNameUnitId sanu = new SelAIDNameUnitId();

		sanu.aggregate = ODSUtils.AGGREGATIONS.convert(aggregation);
		sanu.attr = createAIDName(attribute);
		sanu.unitId = ODSConverter.toODSLong(0l); // TODO unit

		return sanu;
	}

	private SelValueExt createCondition(Condition condition) throws DataAccessException {
		SelValueExt sve = new SelValueExt();

		sve.oper = ODSUtils.OPERATIONS.convert(condition.getOperation());
		sve.attr = new AIDNameUnitId();
		sve.attr.unitId = new T_LONGLONG(); // TODO
		sve.attr.attr = createAIDName(condition.getAttribute());
		sve.value = ODSConverter.toODSValue(condition.getValue());

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

	private final class ODSResultSEQ implements Iterable<Result>, Iterator<Result> {

		private final List<ODSRecordSEQ> odsRecordSEQs = new ArrayList<>();
		private final int length;
		private int index;

		public ODSResultSEQ(ResultSetExt resultSetExt) throws AoException {
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

		private final List<ODSValueSEQ> odsValueSEQs = new ArrayList<>();
		private final Entity entity;

		private ODSRecordSEQ(ElemResultSetExt elemResultSetExt) throws AoException {
			entity = queryService.getEntity(ODSConverter.fromODSLong(elemResultSetExt.aid));
			for (NameValueSeqUnitId nvsui : elemResultSetExt.values) {
				odsValueSEQs.add(new ODSValueSEQ(nvsui));
			}
		}

		private int getLength() {
			return odsValueSEQs.isEmpty() ? 0 : odsValueSEQs.get(0).getLength();
		}

		private Record createRecord(int index) {
			Record record = new Record(entity);
			for(ODSValueSEQ odsValueSEQ : odsValueSEQs) {
				record.addValue(odsValueSEQ.createValue(index));
			}

			return record;
		}

	}

	private final class ODSValueSEQ {

		private final TS_ValueSeq odsValueSEQ;
		private final String name;
		private final ValueType type;
		private final String unit;

		private final List<Object> values;

		public ODSValueSEQ(NameValueSeqUnitId nvsui) throws AoException {
			odsValueSEQ = nvsui.value;
			type = ODSUtils.VALUETYPES.revert(odsValueSEQ.u.discriminator());
			unit = ""; // TODO UNIT MAPPING
			Matcher matcher = AGGREGATION_NAME_PATTERN.matcher(nvsui.valName);
			name = matcher.matches() ? matcher.group(GROUPE_NAME) : nvsui.valName;
			values = ODSConverter.extract(odsValueSEQ);
		}

		private int getLength() {
			return odsValueSEQ.flag.length;
		}

		private Value createValue(int index) {
			return type.newValue(name, unit, odsValueSEQ.flag[index] == 15, values.get(index));
		}

	}

}
