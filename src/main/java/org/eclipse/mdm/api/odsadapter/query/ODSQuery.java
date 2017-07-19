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

/**
 * ODS implementation of the {@link Query} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class ODSQuery implements Query {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSQuery.class);

	private static final String GROUPE_NAME = "name";
	private static final Pattern AGGREGATION_NAME_PATTERN = Pattern.compile("\\S+\\((?<" + GROUPE_NAME + ">\\S+)\\)");

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<String, EntityType> entityTypesByID = new HashMap<>();
	private final Set<EntityType> queriedEntityTypes = new HashSet<>();
	private final List<SelAIDNameUnitId> anuSeq = new ArrayList<>();
	private final List<JoinDef> joinSeq = new ArrayList<>();
	private final List<AIDName> groupBy = new ArrayList<>();
	private final List<SelOrder> orderBy = new ArrayList<>();

	private final ApplElemAccess applElemAccess;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param applElemAccess
	 *            Used to execute the query.
	 */
	ODSQuery(ApplElemAccess applElemAccess) {
		this.applElemAccess = applElemAccess;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQueried(EntityType entityType) {
		return queriedEntityTypes.contains(entityType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query select(Attribute attribute, Aggregation aggregation) {
		EntityType entityType = attribute.getEntityType();
		entityTypesByID.put(Long.toString(ODSConverter.fromODSLong(((ODSEntityType) entityType).getODSID())),
				entityType);
		queriedEntityTypes.add(entityType);
		anuSeq.add(createSelect(attribute, aggregation));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query join(Relation relation, Join join) {
		queriedEntityTypes.add(relation.getSource());
		queriedEntityTypes.add(relation.getTarget());
		joinSeq.add(createJoin(relation, join));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query group(List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			group(attribute);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query group(Attribute attribute) {
		groupBy.add(createAIDName(attribute));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query order(Attribute attribute, boolean ascending) {
		orderBy.add(createOrderBy(attribute, ascending));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Result> fetchSingleton(Filter filter) throws DataAccessException {
		List<Result> results = fetch(filter);
		if (results.isEmpty()) {
			return Optional.empty();
		} else if (results.size() > 1) {
			throw new DataAccessException("Multiple results found after executing the singleton query!");
		}

		return Optional.of(results.get(0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Result> fetch(Filter filter) throws DataAccessException {
		try {

			List<SelItem> condSeq = new ArrayList<>();
			int condCount = 0;
			for (FilterItem conditionItem : filter) {
				SelItem selItem = new SelItem();
				if (conditionItem.isCondition()) {
					selItem.value(createCondition(conditionItem.getCondition()));
				} else if (conditionItem.isOperator()) {
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
			for (Result result : new ResultFactory(entityTypesByID, applElemAccess.getInstancesExt(qse, 0)[0])) {
				results.add(result);
			}
			long stop = System.currentTimeMillis();

			LOGGER.debug("Query executed in {} ms and retrieved {} result rows ({} selections, {} conditions, "
					+ "{} joins).", stop - start, results.size(), anuSeq.size(), condCount, joinSeq.size());
			return results;
		} catch (AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Converts given {@link Attribute} and {@link Aggregation} to an ODS
	 * {@link SelAIDNameUnitId}.
	 *
	 * @param attribute
	 *            The {@code Attribute}.
	 * @param aggregation
	 *            The {@code Aggregation}.
	 * @return The corresponding {@code SelAIDNameUnitId} is returned.
	 */
	private SelAIDNameUnitId createSelect(Attribute attribute, Aggregation aggregation) {
		SelAIDNameUnitId sanu = new SelAIDNameUnitId();

		sanu.aggregate = ODSUtils.AGGREGATIONS.convert(aggregation);
		sanu.attr = createAIDName(attribute);
		sanu.unitId = new T_LONGLONG();

		return sanu;
	}

	/**
	 * Converts given {@link Condition} to an ODS {@link SelValueExt}.
	 *
	 * @param condition
	 *            The {@code Condition}.
	 * @return The corresponding {@code SelValueExt} is returned.
	 * @throws DataAccessException
	 *             Thrown in case of errors.
	 */
	private SelValueExt createCondition(Condition condition) throws DataAccessException {
		SelValueExt sve = new SelValueExt();

		sve.oper = ODSUtils.OPERATIONS.convert(condition.getOperation());
		sve.attr = new AIDNameUnitId();
		sve.attr.unitId = new T_LONGLONG();
		sve.attr.attr = createAIDName(condition.getAttribute());
		sve.value = ODSConverter.toODSValue(condition.getValue());

		return sve;
	}

	/**
	 * Converts given {@link Relation} and {@link Join} to an ODS
	 * {@link JoinDef}.
	 *
	 * @param relation
	 *            The {@code Relation}.
	 * @param join
	 *            The {@code Join}.
	 * @return The corresponding {@code JoinDef} is returned.
	 */
	private JoinDef createJoin(Relation relation, Join join) {
		JoinDef joinDef = new JoinDef();

		joinDef.fromAID = ((ODSEntityType) relation.getSource()).getODSID();
		joinDef.toAID = ((ODSEntityType) relation.getTarget()).getODSID();
		joinDef.refName = relation.getName();
		joinDef.joiningType = ODSUtils.JOINS.convert(join);

		return joinDef;
	}

	/**
	 * Converts given {@link Attribute} and sort order flag to an ODS
	 * {@link SelOrder}.
	 *
	 * @param attribute
	 *            The {@code Attribute}.
	 * @param ascending
	 *            The sort order.
	 * @return The corresponding {@code SelOrder} is returned.
	 */
	private SelOrder createOrderBy(Attribute attribute, boolean ascending) {
		SelOrder selOrder = new SelOrder();

		selOrder.attr = createAIDName(attribute);
		selOrder.ascending = ascending;

		return selOrder;
	}

	/**
	 * Converts given {@link Attribute} to an ODS {@link AIDName}.
	 *
	 * @param attribute
	 *            The {@code Attribute}.
	 * @return The corresponding {@code AIDName} is returned.
	 */
	private AIDName createAIDName(Attribute attribute) {
		AIDName aidName = new AIDName();

		aidName.aid = ((ODSEntityType) attribute.getEntityType()).getODSID();
		aidName.aaName = attribute.getName();

		return aidName;
	}

	// ======================================================================
	// Inner classes
	// ======================================================================

	/**
	 * Traverses the ODS {@link ResultSetExt} and creates a {@link Result} for
	 * each row.
	 */
	private static final class ResultFactory implements Iterable<Result>, Iterator<Result> {

		// ======================================================================
		// Instance variables
		// ======================================================================

		private final List<RecordFactory> recordFactories = new ArrayList<>();
		private final int length;
		private int index;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param entityTypes
		 *            Used to access {@link EntityType} by its ODS ID.
		 * @param resultSetExt
		 *            The ODS values sequence containers.
		 * @throws DataAccessException
		 *             Thrown on conversion errors.
		 */
		public ResultFactory(Map<String, EntityType> entityTypes, ResultSetExt resultSetExt)
				throws DataAccessException {
			for (ElemResultSetExt elemResultSetExt : resultSetExt.firstElems) {
				EntityType entityType = entityTypes.get(Long.toString(ODSConverter.fromODSLong(elemResultSetExt.aid)));
				recordFactories.add(new RecordFactory(entityType, elemResultSetExt.values));
			}

			length = recordFactories.isEmpty() ? 0 : recordFactories.get(0).getLength();
		}

		// ======================================================================
		// Public methods
		// ======================================================================

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return index < length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Result next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No such element available.");
			}
			Result result = new Result();

			for (RecordFactory recordFactory : recordFactories) {
				result.addRecord(recordFactory.createRecord(index));
			}

			index++;
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Result> iterator() {
			return this;
		}

	}

	/**
	 * Creates a {@link Record} for given index from the original ODS values
	 * sequence for a given {@link EntityType}.
	 */
	private static final class RecordFactory {

		// ======================================================================
		// Instance variables
		// ======================================================================

		private final List<ValueFactory> valueFactories = new ArrayList<>();
		private final EntityType entityType;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param entityType
		 *            The associated {@link EntityType}.
		 * @param nvsuis
		 *            The ODS value sequence containers.
		 * @throws DataAccessException
		 *             Thrown on conversion errors.
		 */
		private RecordFactory(EntityType entityType, NameValueSeqUnitId[] nvsuis) throws DataAccessException {
			this.entityType = entityType;
			for (NameValueSeqUnitId nvsui : nvsuis) {
				Matcher matcher = AGGREGATION_NAME_PATTERN.matcher(nvsui.valName);
				String attributeName = matcher.matches() ? matcher.group(GROUPE_NAME) : nvsui.valName;
				valueFactories.add(new ValueFactory(entityType.getAttribute(attributeName), nvsui));
			}
		}

		// ======================================================================
		// Private methods
		// ======================================================================

		private int getLength() {
			return valueFactories.isEmpty() ? 0 : valueFactories.get(0).getLength();
		}

		private Record createRecord(int index) {
			Record record = new Record(entityType);
			for (ValueFactory valueFactory : valueFactories) {
				record.addValue(valueFactory.createValue(index));
			}

			return record;
		}

	}

	/**
	 * Creates a {@link Value} container for given index from the original ODS
	 * value sequence for a given {@link Attribute}.
	 */
	private static final class ValueFactory {

		// ======================================================================
		// Instance variables
		// ======================================================================

		private final List<Value> values;
		private final String unit;
		private final int length;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param attribute
		 *            The associated {@link Attribute}.
		 * @param nvsui
		 *            The ODS value sequence container.
		 * @throws DataAccessException
		 *             Thrown on conversion errors.
		 */
		private ValueFactory(Attribute attribute, NameValueSeqUnitId nvsui) throws DataAccessException {
			length = nvsui.value.flag.length;
			unit = attribute.getUnit();
			values = ODSConverter.fromODSValueSeq(attribute, unit, nvsui.value);
		}

		// ======================================================================
		// Private methods
		// ======================================================================

		/**
		 * Returns the length of the sequence.
		 *
		 * @return Length of the sequence is returned.
		 */
		private int getLength() {
			return length;
		}

		/**
		 * Returns the {@link Value} for given index.
		 *
		 * @param index
		 *            Index within the sequence.
		 * @return The corresponding {@code Value} is returned.
		 */
		private Value createValue(int index) {
			return values.get(index);
		}

	}

}
