/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.util.HashMap;
import java.util.Map;

import org.asam.ods.AggrFunc;
import org.asam.ods.DataType;
import org.asam.ods.JoinType;
import org.asam.ods.RelationType;
import org.asam.ods.SelOpcode;
import org.asam.ods.SelOperator;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Parameter;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
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

	public static final BiDiMapper<Relationship, RelationType> RELATIONSHIPS = new BiDiMapper<>();
	public static final BiDiMapper<Aggregation, AggrFunc> AGGREGATIONS = new BiDiMapper<>();
	public static final BiDiMapper<Operator, SelOperator> OPERATORS = new BiDiMapper<>();
	public static final BiDiMapper<Operation, SelOpcode> OPERATIONS = new BiDiMapper<>();
	public static final BiDiMapper<ValueType, DataType> VALUETYPES = new BiDiMapper<>();
	public static final BiDiMapper<Join, JoinType> JOINS = new BiDiMapper<>();

	/*
	 * TODO: Since modules will introduce new entity types it must be possible
	 * to extend the mappings of DEFAULT_MIMETYPES and AE_NAME_MAPPING!
	 * In either case on miss matches a checked exception should be thrown ?!
	 */
	public static final Map<String, String> DEFAULT_MIMETYPES = new HashMap<>();
	private static final BiDiMapper<Class<? extends Entity>, String> AE_NAME_MAPPING = new BiDiMapper<>();

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

		OPERATORS.addMappings(Operator.AND, SelOperator.AND);
		OPERATORS.addMappings(Operator.OR, SelOperator.OR);
		OPERATORS.addMappings(Operator.NOT, SelOperator.NOT);
		OPERATORS.addMappings(Operator.OPEN, SelOperator.OPEN);
		OPERATORS.addMappings(Operator.CLOSE, SelOperator.CLOSE);

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

		AE_NAME_MAPPING.addMappings(Environment.class, "Environment");
		AE_NAME_MAPPING.addMappings(Measurement.class, "MeaResult");
		AE_NAME_MAPPING.addMappings(Channel.class, "MeaQuantity");
		AE_NAME_MAPPING.addMappings(ChannelGroup.class, "SubMatrix");
		AE_NAME_MAPPING.addMappings(Parameter.class, "ResultParameter");
		AE_NAME_MAPPING.addMappings(ParameterSet.class, "ResultParameterSet");
		AE_NAME_MAPPING.addMappings(PhysicalDimension.class, "PhysDimension");
		AE_NAME_MAPPING.addMappings(User.class, "User");
		AE_NAME_MAPPING.addMappings(Test.class, "Test");
		AE_NAME_MAPPING.addMappings(TestStep.class, "TestStep");
		AE_NAME_MAPPING.addMappings(Quantity.class, "Quantity");
		AE_NAME_MAPPING.addMappings(Unit.class, "Unit");


		/*
		 * TODO: MimeTypes could and should be stored in the corresponding classes?!
		 */

		//TODO: change  MIME type of Test as soon as default model is implemented
		DEFAULT_MIMETYPES.put("Test", "application/x-asam.aotest");
		DEFAULT_MIMETYPES.put("TestStep", "application/x-asam.aosubtest.teststep");
		DEFAULT_MIMETYPES.put("Measurement", "application/x-asam.aomeasurement");
		DEFAULT_MIMETYPES.put("Channel", "application/x-asam.aomeasurementquantity");
		DEFAULT_MIMETYPES.put("ChannelGroup", "application/x-asam.aosubmatrix");
		DEFAULT_MIMETYPES.put("User", "application/x-asam.aouser");
		DEFAULT_MIMETYPES.put("Environment", "application/x-asam.aoenvironment");
		DEFAULT_MIMETYPES.put("Unit", "application/x-asam.aounit");
		DEFAULT_MIMETYPES.put("Parameter", "application/x-asam.aoparameter.resultparameter");
		DEFAULT_MIMETYPES.put("ParameterSet", "application/x-asam.aoparameterset.resultparameterset");
		DEFAULT_MIMETYPES.put("PhysicalDimension", "application/x-asam.aophysicaldimension");
		DEFAULT_MIMETYPES.put("Quantity", "application/x-asam.aoquantity");
		DEFAULT_MIMETYPES.put("LocalColumn", "application/x-asam.aolocalcolumn");
		DEFAULT_MIMETYPES.put("UNITUNDERTEST", "application/x-asam.aounitundertest.unitundertest");
		DEFAULT_MIMETYPES.put("TESTSEQUENCE", "application/x-asam.aotestsequence.testsequence");
		DEFAULT_MIMETYPES.put("TESTEQUIPMENT", "application/x-asam.aotestequipment.testequipment");
	}

	public static String getAEName(Class<? extends Entity> clazz) {
		return AE_NAME_MAPPING.convert(clazz);
	}

	public static Class<? extends Entity> getClass(String aeName) {
		return AE_NAME_MAPPING.revert(aeName);
	}

}
