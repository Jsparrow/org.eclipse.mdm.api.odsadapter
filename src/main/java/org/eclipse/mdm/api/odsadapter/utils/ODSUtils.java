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

public final class ODSUtils {

	// ======================================================================
	// Class variables
	// ======================================================================

	public static final BiDiMapper<Relationship, RelationType> RELATIONSHIPS = relationships();

	public static final BiDiMapper<Aggregation, AggrFunc> AGGREGATIONS = aggregations();

	public static final BiDiMapper<ContextType, String> CONTEXTTYPES = contextTypes();

	public static final BiDiMapper<Operation, SelOpcode> OPERATIONS = operations();

	public static final BiDiMapper<Operator, SelOperator> OPERATORS = operators();

	public static final BiDiMapper<ValueType, DataType> VALUETYPES = valueTypes();

	public static final BiDiMapper<Join, JoinType> JOINS = joins();

	private ODSUtils() {
		// hide constructor
	}

	private static BiDiMapper<Relationship, RelationType> relationships() {
		BiDiMapper<Relationship, RelationType> relationships = new BiDiMapper<>();
		relationships.addMappings(Relationship.FATHER_CHILD, RelationType.FATHER_CHILD);
		relationships.addMappings(Relationship.INFO, RelationType.INFO);
		relationships.addMappings(Relationship.INHERITANCE, RelationType.INHERITANCE);
		return relationships;
	}

	private static BiDiMapper<Aggregation, AggrFunc> aggregations() {
		BiDiMapper<Aggregation, AggrFunc> aggregations = new BiDiMapper<>();
		aggregations.addMappings(Aggregation.NONE, AggrFunc.NONE);
		aggregations.addMappings(Aggregation.COUNT, AggrFunc.COUNT);
		aggregations.addMappings(Aggregation.DISTINCT_COUNT, AggrFunc.DCOUNT);
		aggregations.addMappings(Aggregation.MINIMUM, AggrFunc.MIN);
		aggregations.addMappings(Aggregation.MAXIMUM, AggrFunc.MAX);
		aggregations.addMappings(Aggregation.AVERAGE, AggrFunc.AVG);
		aggregations.addMappings(Aggregation.DEVIATION, AggrFunc.STDDEV);
		aggregations.addMappings(Aggregation.SUM, AggrFunc.SUM);
		aggregations.addMappings(Aggregation.DISTINCT, AggrFunc.DISTINCT);
		return aggregations;
	}

	private static BiDiMapper<ContextType, String> contextTypes() {
		BiDiMapper<ContextType, String> contextTypes = new BiDiMapper<>();
		contextTypes.addMappings(ContextType.UNITUNDERTEST, "UnitUnderTest");
		contextTypes.addMappings(ContextType.TESTSEQUENCE, "TestSequence");
		contextTypes.addMappings(ContextType.TESTEQUIPMENT, "TestEquipment");
		return contextTypes;
	}

	private static BiDiMapper<Operation, SelOpcode> operations() {
		BiDiMapper<Operation, SelOpcode> operations = new BiDiMapper<>();
		operations.addMappings(Operation.LIKE, SelOpcode.LIKE);
		operations.addMappings(Operation.CASE_INSENSITIVE_LIKE, SelOpcode.CI_LIKE);
		operations.addMappings(Operation.NOT_LIKE, SelOpcode.NOTLIKE);
		operations.addMappings(Operation.CASE_INSENSITIVE_NOT_LIKE, SelOpcode.CI_NOTLIKE);
		operations.addMappings(Operation.EQUAL, SelOpcode.EQ);
		operations.addMappings(Operation.CASE_INSENSITIVE_EQUAL, SelOpcode.CI_EQ);
		operations.addMappings(Operation.NOT_EQUAL, SelOpcode.NEQ);
		operations.addMappings(Operation.CASE_INSENSITIVE_NOT_EQUAL, SelOpcode.CI_NEQ);
		operations.addMappings(Operation.GREATER_THAN, SelOpcode.GT);
		operations.addMappings(Operation.CASE_INSENSITIVE_GREATER_THAN, SelOpcode.CI_GT);
		operations.addMappings(Operation.GREATER_THAN_OR_EQUAL, SelOpcode.GTE);
		operations.addMappings(Operation.CASE_INSENSITIVE_GREATER_THAN_OR_EQUAL, SelOpcode.CI_GTE);
		operations.addMappings(Operation.LESS_THAN, SelOpcode.LT);
		operations.addMappings(Operation.CASE_INSENSITIVE_LESS_THAN, SelOpcode.CI_LT);
		operations.addMappings(Operation.LESS_THAN_OR_EQUAL, SelOpcode.LTE);
		operations.addMappings(Operation.CASE_INSENSITIVE_LESS_THAN_OR_EQUAL, SelOpcode.CI_LTE);
		operations.addMappings(Operation.IS_NULL, SelOpcode.IS_NULL);
		operations.addMappings(Operation.IS_NOT_NULL, SelOpcode.IS_NOT_NULL);
		operations.addMappings(Operation.IN_SET, SelOpcode.INSET);
		operations.addMappings(Operation.CASE_INSENSITIVE_IN_SET, SelOpcode.CI_INSET);
		operations.addMappings(Operation.NOT_IN_SET, SelOpcode.NOTINSET);
		operations.addMappings(Operation.CASE_INSENSITIVE_NOT_IN_SET, SelOpcode.CI_NOTINSET);
		operations.addMappings(Operation.BETWEEN, SelOpcode.BETWEEN);
		return operations;
	}

	private static BiDiMapper<Operator, SelOperator> operators() {
		BiDiMapper<Operator, SelOperator> operators = new BiDiMapper<>();
		operators.addMappings(Operator.AND, SelOperator.AND);
		operators.addMappings(Operator.OR, SelOperator.OR);
		operators.addMappings(Operator.NOT, SelOperator.NOT);
		operators.addMappings(Operator.OPEN, SelOperator.OPEN);
		operators.addMappings(Operator.CLOSE, SelOperator.CLOSE);
		return operators;
	}

	private static BiDiMapper<ValueType, DataType> valueTypes() {
		BiDiMapper<ValueType, DataType> valueTypes = new BiDiMapper<>();
		valueTypes.addMappings(ValueType.UNKNOWN, DataType.DT_UNKNOWN);
		valueTypes.addMappings(ValueType.STRING, DataType.DT_STRING);
		valueTypes.addMappings(ValueType.STRING_SEQUENCE, DataType.DS_STRING);
		valueTypes.addMappings(ValueType.DATE, DataType.DT_DATE);
		valueTypes.addMappings(ValueType.DATE_SEQUENCE, DataType.DS_DATE);
		valueTypes.addMappings(ValueType.BOOLEAN, DataType.DT_BOOLEAN);
		valueTypes.addMappings(ValueType.BOOLEAN_SEQUENCE, DataType.DS_BOOLEAN);
		valueTypes.addMappings(ValueType.BYTE, DataType.DT_BYTE);
		valueTypes.addMappings(ValueType.BYTE_SEQUENCE, DataType.DS_BYTE);
		valueTypes.addMappings(ValueType.SHORT, DataType.DT_SHORT);
		valueTypes.addMappings(ValueType.SHORT_SEQUENCE, DataType.DS_SHORT);
		valueTypes.addMappings(ValueType.INTEGER, DataType.DT_LONG);
		valueTypes.addMappings(ValueType.INTEGER_SEQUENCE, DataType.DS_LONG);
		valueTypes.addMappings(ValueType.LONG, DataType.DT_LONGLONG);
		valueTypes.addMappings(ValueType.LONG_SEQUENCE, DataType.DS_LONGLONG);
		valueTypes.addMappings(ValueType.FLOAT, DataType.DT_FLOAT);
		valueTypes.addMappings(ValueType.FLOAT_SEQUENCE, DataType.DS_FLOAT);
		valueTypes.addMappings(ValueType.DOUBLE, DataType.DT_DOUBLE);
		valueTypes.addMappings(ValueType.DOUBLE_SEQUENCE, DataType.DS_DOUBLE);
		valueTypes.addMappings(ValueType.BYTE_STREAM, DataType.DT_BYTESTR);
		valueTypes.addMappings(ValueType.BYTE_STREAM_SEQUENCE, DataType.DS_BYTESTR);
		valueTypes.addMappings(ValueType.FLOAT_COMPLEX, DataType.DT_COMPLEX);
		valueTypes.addMappings(ValueType.FLOAT_COMPLEX_SEQUENCE, DataType.DS_COMPLEX);
		valueTypes.addMappings(ValueType.DOUBLE_COMPLEX, DataType.DT_DCOMPLEX);
		valueTypes.addMappings(ValueType.DOUBLE_COMPLEX_SEQUENCE, DataType.DS_DCOMPLEX);
		valueTypes.addMappings(ValueType.ENUMERATION, DataType.DT_ENUM);
		valueTypes.addMappings(ValueType.ENUMERATION_SEQUENCE, DataType.DS_ENUM);
		valueTypes.addMappings(ValueType.FILE_LINK, DataType.DT_EXTERNALREFERENCE);
		valueTypes.addMappings(ValueType.FILE_LINK_SEQUENCE, DataType.DS_EXTERNALREFERENCE);
		valueTypes.addMappings(ValueType.BLOB, DataType.DT_BLOB);
		return valueTypes;
	}

	private static BiDiMapper<Join, JoinType> joins() {
		BiDiMapper<Join, JoinType> joins = new BiDiMapper<>();
		joins.addMappings(Join.INNER, org.asam.ods.JoinType.JTDEFAULT);
		joins.addMappings(Join.OUTER, org.asam.ods.JoinType.JTOUTER);
		return joins;
	}

}
