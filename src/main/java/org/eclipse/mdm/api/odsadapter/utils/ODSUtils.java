/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import org.asam.ods.AggrFunc;
import org.asam.ods.DataType;
import org.asam.ods.JoinType;
import org.asam.ods.RelationType;
import org.asam.ods.SelOpcode;
import org.asam.ods.SelOperator;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Operator;
import org.eclipse.mdm.api.base.query.Relationship;

/**
 * Utility class provides bidirectional mappings for ODS types
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public abstract class ODSUtils {

	// ======================================================================
	// Class variables
	// ======================================================================

	/**
	 * Maps {@link Relationship} to the corresponding ODS {@link RelationType}.
	 */
	public static final BiDiMapper<Relationship, RelationType> RELATIONSHIPS = new BiDiMapper<>();

	/**
	 * Maps {@link Aggregation} to the corresponding ODS {@link AggrFunc}.
	 */
	public static final BiDiMapper<Aggregation, AggrFunc> AGGREGATIONS = new BiDiMapper<>();

	/**
	 * Maps {@link ContextType} to the corresponding ODS {@link String}.
	 */
	public static final BiDiMapper<ContextType, String> CONTEXTTYPES = new BiDiMapper<>();

	/**
	 * Maps {@link Operation} to the corresponding ODS {@link SelOpcode}.
	 */
	public static final BiDiMapper<Operation, SelOpcode> OPERATIONS = new BiDiMapper<>();

	/**
	 * Maps {@link Operator} to the corresponding ODS {@link SelOperator}.
	 */
	public static final BiDiMapper<Operator, SelOperator> OPERATORS = new BiDiMapper<>();

	/**
	 * Maps {@link ValueType} to the corresponding ODS {@link DataType}.
	 */
	public static final BiDiMapper<ValueType, DataType> VALUETYPES = new BiDiMapper<>();

	/**
	 * Maps {@link Join} to the corresponding ODS {@link JoinType}.
	 */
	public static final BiDiMapper<Join, JoinType> JOINS = new BiDiMapper<>();

	static {
		RELATIONSHIPS.addMappings(Relationship.FATHER_CHILD, RelationType.FATHER_CHILD);
		RELATIONSHIPS.addMappings(Relationship.INFO, RelationType.INFO);
		RELATIONSHIPS.addMappings(Relationship.INHERITANCE, RelationType.INHERITANCE);

		AGGREGATIONS.addMappings(Aggregation.NONE, AggrFunc.NONE);
		AGGREGATIONS.addMappings(Aggregation.COUNT, AggrFunc.COUNT);
		AGGREGATIONS.addMappings(Aggregation.DISTINCT_COUNT, AggrFunc.DCOUNT);
		AGGREGATIONS.addMappings(Aggregation.MINIMUM, AggrFunc.MIN);
		AGGREGATIONS.addMappings(Aggregation.MAXIMUM, AggrFunc.MAX);
		AGGREGATIONS.addMappings(Aggregation.AVERAGE, AggrFunc.AVG);
		AGGREGATIONS.addMappings(Aggregation.DEVIATION, AggrFunc.STDDEV);
		AGGREGATIONS.addMappings(Aggregation.SUM, AggrFunc.SUM);
		AGGREGATIONS.addMappings(Aggregation.DISTINCT, AggrFunc.DISTINCT);

		CONTEXTTYPES.addMappings(ContextType.UNITUNDERTEST, "UnitUnderTest");
		CONTEXTTYPES.addMappings(ContextType.TESTSEQUENCE, "TestSequence");
		CONTEXTTYPES.addMappings(ContextType.TESTEQUIPMENT, "TestEquipment");

		OPERATIONS.addMappings(Operation.LIKE, SelOpcode.LIKE);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_LIKE, SelOpcode.CI_LIKE);
		OPERATIONS.addMappings(Operation.NOT_LIKE, SelOpcode.NOTLIKE);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_NOT_LIKE, SelOpcode.CI_NOTLIKE);
		OPERATIONS.addMappings(Operation.EQUAL, SelOpcode.EQ);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_EQUAL, SelOpcode.CI_EQ);
		OPERATIONS.addMappings(Operation.NOT_EQUAL, SelOpcode.NEQ);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_NOT_EQUAL, SelOpcode.CI_NEQ);
		OPERATIONS.addMappings(Operation.GREATER_THAN, SelOpcode.GT);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_GREATER_THAN, SelOpcode.CI_GT);
		OPERATIONS.addMappings(Operation.GREATER_THAN_OR_EQUAL, SelOpcode.GTE);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_GREATER_THAN_OR_EQUAL, SelOpcode.CI_GTE);
		OPERATIONS.addMappings(Operation.LESS_THAN, SelOpcode.LT);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_LESS_THAN, SelOpcode.CI_LT);
		OPERATIONS.addMappings(Operation.LESS_THAN_OR_EQUAL, SelOpcode.LTE);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_LESS_THAN_OR_EQUAL, SelOpcode.CI_LTE);
		OPERATIONS.addMappings(Operation.IS_NULL, SelOpcode.IS_NULL);
		OPERATIONS.addMappings(Operation.IS_NOT_NULL, SelOpcode.IS_NOT_NULL);
		OPERATIONS.addMappings(Operation.IN_SET, SelOpcode.INSET);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_IN_SET, SelOpcode.CI_INSET);
		OPERATIONS.addMappings(Operation.NOT_IN_SET, SelOpcode.NOTINSET);
		OPERATIONS.addMappings(Operation.CASE_INSENSITIVE_NOT_IN_SET, SelOpcode.CI_NOTINSET);
		OPERATIONS.addMappings(Operation.BETWEEN, SelOpcode.BETWEEN);

		OPERATORS.addMappings(Operator.AND, SelOperator.AND);
		OPERATORS.addMappings(Operator.OR, SelOperator.OR);
		OPERATORS.addMappings(Operator.NOT, SelOperator.NOT);
		OPERATORS.addMappings(Operator.OPEN, SelOperator.OPEN);
		OPERATORS.addMappings(Operator.CLOSE, SelOperator.CLOSE);

		VALUETYPES.addMappings(ValueType.UNKNOWN, DataType.DT_UNKNOWN);
		VALUETYPES.addMappings(ValueType.STRING, DataType.DT_STRING);
		VALUETYPES.addMappings(ValueType.STRING_SEQUENCE, DataType.DS_STRING);
		VALUETYPES.addMappings(ValueType.DATE, DataType.DT_DATE);
		VALUETYPES.addMappings(ValueType.DATE_SEQUENCE, DataType.DS_DATE);
		VALUETYPES.addMappings(ValueType.BOOLEAN, DataType.DT_BOOLEAN);
		VALUETYPES.addMappings(ValueType.BOOLEAN_SEQUENCE, DataType.DS_BOOLEAN);
		VALUETYPES.addMappings(ValueType.BYTE, DataType.DT_BYTE);
		VALUETYPES.addMappings(ValueType.BYTE_SEQUENCE, DataType.DS_BYTE);
		VALUETYPES.addMappings(ValueType.SHORT, DataType.DT_SHORT);
		VALUETYPES.addMappings(ValueType.SHORT_SEQUENCE, DataType.DS_SHORT);
		VALUETYPES.addMappings(ValueType.INTEGER, DataType.DT_LONG);
		VALUETYPES.addMappings(ValueType.INTEGER_SEQUENCE, DataType.DS_LONG);
		VALUETYPES.addMappings(ValueType.LONG, DataType.DT_LONGLONG);
		VALUETYPES.addMappings(ValueType.LONG_SEQUENCE, DataType.DS_LONGLONG);
		VALUETYPES.addMappings(ValueType.FLOAT, DataType.DT_FLOAT);
		VALUETYPES.addMappings(ValueType.FLOAT_SEQUENCE, DataType.DS_FLOAT);
		VALUETYPES.addMappings(ValueType.DOUBLE, DataType.DT_DOUBLE);
		VALUETYPES.addMappings(ValueType.DOUBLE_SEQUENCE, DataType.DS_DOUBLE);
		VALUETYPES.addMappings(ValueType.BYTE_STREAM, DataType.DT_BYTESTR);
		VALUETYPES.addMappings(ValueType.BYTE_STREAM_SEQUENCE, DataType.DS_BYTESTR);
		VALUETYPES.addMappings(ValueType.FLOAT_COMPLEX, DataType.DT_COMPLEX);
		VALUETYPES.addMappings(ValueType.FLOAT_COMPLEX_SEQUENCE, DataType.DS_COMPLEX);
		VALUETYPES.addMappings(ValueType.DOUBLE_COMPLEX, DataType.DT_DCOMPLEX);
		VALUETYPES.addMappings(ValueType.DOUBLE_COMPLEX_SEQUENCE, DataType.DS_DCOMPLEX);
		VALUETYPES.addMappings(ValueType.ENUMERATION, DataType.DT_ENUM);
		VALUETYPES.addMappings(ValueType.ENUMERATION_SEQUENCE, DataType.DS_ENUM);
		VALUETYPES.addMappings(ValueType.FILE_LINK, DataType.DT_EXTERNALREFERENCE);
		VALUETYPES.addMappings(ValueType.FILE_LINK_SEQUENCE, DataType.DS_EXTERNALREFERENCE);
		VALUETYPES.addMappings(ValueType.BLOB, DataType.DT_BLOB);

		JOINS.addMappings(Join.INNER, org.asam.ods.JoinType.JTDEFAULT);
		JOINS.addMappings(Join.OUTER, org.asam.ods.JoinType.JTOUTER);
	}

}
