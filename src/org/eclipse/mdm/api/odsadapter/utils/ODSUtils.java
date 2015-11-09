/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.AggrFunc;
import org.asam.ods.AoException;
import org.asam.ods.DataType;
import org.asam.ods.InstanceElement;
import org.asam.ods.JoinType;
import org.asam.ods.NameValue;
import org.asam.ods.NameValueIterator;
import org.asam.ods.NameValueUnit;
import org.asam.ods.RelationType;
import org.asam.ods.SelOpcode;
import org.asam.ods.SelOperator;
import org.asam.ods.TS_Union;
import org.asam.ods.TS_Value;
import org.asam.ods.T_COMPLEX;
import org.asam.ods.T_DCOMPLEX;
import org.asam.ods.T_ExternalReference;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ChannelInfo;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Operator;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.base.utils.BiDiMapper;
import org.eclipse.mdm.api.odsadapter.odscache.ODSCache;

public final class ODSUtils {
	
	public static final BiDiMapper<Operator, SelOperator> OPERATORS = new BiDiMapper<>();
	public static final BiDiMapper<Aggregation, AggrFunc> AGGREGATIONS = new BiDiMapper<>();
	public static final BiDiMapper<Operation, SelOpcode> OPERATIONS = new BiDiMapper<>();
	public static final BiDiMapper<ValueType, DataType> VALUETYPES = new BiDiMapper<>();
	public static final BiDiMapper<Relationship, RelationType> RELATIONSHIPS = new BiDiMapper<>();
	public static final BiDiMapper<Join, JoinType> JOINS = new BiDiMapper<>();
	
	private static final BiDiMapper<Class<? extends DataItem>, String> AE_NAME_MAPPING = new BiDiMapper<>();	
	private static final Map<String, String> DEFAULT_MIMETYPES = new HashMap<String, String>();
	
	// prepare dateformats to avoid instantiation a single object everting
	// parsing a date.
	public static Map<Integer, DateFormat> ODS_DATEFORMATS = new HashMap<Integer, DateFormat>();
	
	static {
		AE_NAME_MAPPING.addMappings(Environment.class, "Environment");
		AE_NAME_MAPPING.addMappings(Measurement.class, "MeaResult");		
		AE_NAME_MAPPING.addMappings(Channel.class, "MeaQuantity");
		AE_NAME_MAPPING.addMappings(ChannelGroup.class, "SubMatrix");
//		AE_NAME_MAPPING.addMappings(Parameter.class, "ResultParameter");
//		AE_NAME_MAPPING.addMappings(ParameterSet.class, "ResultParameterSet");
		AE_NAME_MAPPING.addMappings(PhysicalDimension.class, "PhysDimension");
		AE_NAME_MAPPING.addMappings(User.class, "User");
		AE_NAME_MAPPING.addMappings(Test.class, "Test");
		AE_NAME_MAPPING.addMappings(TestStep.class, "TestStep");
		AE_NAME_MAPPING.addMappings(Quantity.class, "Quantity");
		AE_NAME_MAPPING.addMappings(Unit.class, "Unit");
		AE_NAME_MAPPING.addMappings(ChannelInfo.class, "LocalColumn");
		AE_NAME_MAPPING.toReadOnly();

		
		DEFAULT_MIMETYPES.put("Test", "application/x-asam.aosubtest.test");
		DEFAULT_MIMETYPES.put("TestStep", "application/x-asam.aosubtest.teststep");
		DEFAULT_MIMETYPES.put("Measurement", "application/x-asam.aomeasurement");
		DEFAULT_MIMETYPES.put("Channel", "application/x-asam.aomeasurementquantity");
		DEFAULT_MIMETYPES.put("Channelgroup", "application/x-asam.aosubmatrix");
		DEFAULT_MIMETYPES.put("User", "application/x-asam.aouser");
		DEFAULT_MIMETYPES.put("Environment", "application/x-asam.aoenvironment");
		DEFAULT_MIMETYPES.put("Unit", "application/x-asam.aounit");
		DEFAULT_MIMETYPES.put("Parameter", "application/x-asam.aoparameter.resultparameter");
		DEFAULT_MIMETYPES.put("ParameterSet", "application/x-asam.aoparameterset.resultparameterset");
		DEFAULT_MIMETYPES.put("PhysicalDimension", "application/x-asam.aophysicaldimension.PhysDimension");
		DEFAULT_MIMETYPES.put("Quantity", "application/x-asam.aoquantity");
		DEFAULT_MIMETYPES.put("UNITUNDERTEST", "application/x-asam.aounitundertest.unitundertest");
		DEFAULT_MIMETYPES.put("TESTSEQUENCE", "application/x-asam.aotestsequence.testsequence");
		DEFAULT_MIMETYPES.put("TESTEQUIPMENT", "application/x-asam.aotestequipment.testequipment");
		
		OPERATORS.addMappings(Operator.AND, SelOperator.AND);
		OPERATORS.addMappings(Operator.OR, SelOperator.OR);
		OPERATORS.addMappings(Operator.NOT, SelOperator.NOT);
		OPERATORS.addMappings(Operator.OPEN, SelOperator.OPEN);
		OPERATORS.addMappings(Operator.CLOSE, SelOperator.CLOSE);
		OPERATORS.toReadOnly();
				
		AGGREGATIONS.addMappings(Aggregation.NONE, AggrFunc.NONE);
		AGGREGATIONS.addMappings(Aggregation.COUNT, AggrFunc.COUNT);
		AGGREGATIONS.addMappings(Aggregation.DCOUNT, AggrFunc.DCOUNT);
		AGGREGATIONS.addMappings(Aggregation.MIN, AggrFunc.MIN);
		AGGREGATIONS.addMappings(Aggregation.MAX, AggrFunc.MAX);
		AGGREGATIONS.addMappings(Aggregation.AVG, AggrFunc.AVG);
		AGGREGATIONS.addMappings(Aggregation.STDDEV, AggrFunc.STDDEV);
		AGGREGATIONS.addMappings(Aggregation.SUM, AggrFunc.SUM);
		AGGREGATIONS.addMappings(Aggregation.DISTINCT, AggrFunc.DISTINCT);
		AGGREGATIONS.toReadOnly();
		
		OPERATIONS.addMappings(Operation.LIKE, SelOpcode.LIKE);
		OPERATIONS.addMappings(Operation.CI_LIKE, SelOpcode.CI_LIKE);		
		OPERATIONS.addMappings(Operation.NOT_LIKE, SelOpcode.NOTLIKE);
		OPERATIONS.addMappings(Operation.CI_NOT_LIKE, SelOpcode.CI_NOTLIKE);
		OPERATIONS.addMappings(Operation.EQ, SelOpcode.EQ);
		OPERATIONS.addMappings(Operation.CI_EQ, SelOpcode.CI_EQ);
		OPERATIONS.addMappings(Operation.NEQ, SelOpcode.NEQ);
		OPERATIONS.addMappings(Operation.CI_NEQ, SelOpcode.CI_NEQ);
		OPERATIONS.addMappings(Operation.GT, SelOpcode.GT);
		OPERATIONS.addMappings(Operation.CI_GT, SelOpcode.CI_GT);		
		OPERATIONS.addMappings(Operation.GTE, SelOpcode.GTE);
		OPERATIONS.addMappings(Operation.CI_GTE, SelOpcode.CI_GTE);		
		OPERATIONS.addMappings(Operation.LT, SelOpcode.LT);
		OPERATIONS.addMappings(Operation.CI_LT, SelOpcode.CI_LT);		
		OPERATIONS.addMappings(Operation.LTE, SelOpcode.LTE);
		OPERATIONS.addMappings(Operation.CI_LTE, SelOpcode.CI_LTE);		
		OPERATIONS.addMappings(Operation.IS_NULL, SelOpcode.IS_NULL);		
		OPERATIONS.addMappings(Operation.IS_NOT_NULL, SelOpcode.IS_NOT_NULL);		
		OPERATIONS.addMappings(Operation.INSET, SelOpcode.INSET);		
		OPERATIONS.addMappings(Operation.CI_INSET, SelOpcode.CI_INSET);		
		OPERATIONS.addMappings(Operation.NOT_INSET, SelOpcode.NOTINSET);		
		OPERATIONS.addMappings(Operation.CI_NOT_INSET, SelOpcode.CI_NOTINSET);
		OPERATIONS.toReadOnly();
				
		
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
		VALUETYPES.addMappings(ValueType.COMPLEX, DataType.DT_COMPLEX);
		VALUETYPES.addMappings(ValueType.COMPLEX_SEQUENCE, DataType.DS_COMPLEX);		
		VALUETYPES.addMappings(ValueType.DCOMPLEX, DataType.DT_DCOMPLEX);
		VALUETYPES.addMappings(ValueType.DCOMPLEX_SEQUENCE, DataType.DS_DCOMPLEX);		
		VALUETYPES.addMappings(ValueType.ENUM, DataType.DT_ENUM);
		VALUETYPES.addMappings(ValueType.ENUM_SEQUENCE, DataType.DS_ENUM);				
		VALUETYPES.addMappings(ValueType.FILE_LINK, DataType.DT_EXTERNALREFERENCE);
		VALUETYPES.addMappings(ValueType.FILE_LINK_SEQUENCE, DataType.DS_EXTERNALREFERENCE);		
		VALUETYPES.addMappings(ValueType.BLOB, DataType.DT_BLOB);
		VALUETYPES.toReadOnly();
				
		RELATIONSHIPS.addMappings(Relationship.FATHER_CHILD, RelationType.FATHER_CHILD);
		RELATIONSHIPS.addMappings(Relationship.INFO, RelationType.INFO);
		RELATIONSHIPS.addMappings(Relationship.INHERITANCE, RelationType.INHERITANCE);
		RELATIONSHIPS.toReadOnly();
		
		JOINS.addMappings(Join.INNER, org.asam.ods.JoinType.JTDEFAULT);
		JOINS.addMappings(Join.OUTER, org.asam.ods.JoinType.JTOUTER);
		JOINS.toReadOnly();
		
        ODS_DATEFORMATS.put(4, new SimpleDateFormat("yyyy"));
        ODS_DATEFORMATS.put(6, new SimpleDateFormat("yyyyMM"));
        ODS_DATEFORMATS.put(8, new SimpleDateFormat("yyyyMMdd"));
        ODS_DATEFORMATS.put(10, new SimpleDateFormat("yyyyMMddHH"));
        ODS_DATEFORMATS.put(12, new SimpleDateFormat("yyyyMMddHHmm"));
        ODS_DATEFORMATS.put(14, new SimpleDateFormat("yyyyMMddHHmmss"));
        ODS_DATEFORMATS.put(16, new SimpleDateFormat("yyyyMMddHHmmssSS")); // NOT ODS conform!!!
        ODS_DATEFORMATS.put(17, new SimpleDateFormat("yyyyMMddHHmmssSSS"));
	}

	
	public static String getAEName(Class<? extends DataItem> clazz) {
		String aeName = AE_NAME_MAPPING.convert(clazz);
		if(aeName == null) {
			return clazz.getSimpleName();
		}
		return aeName;
	}
	
	public static Class<? extends DataItem> getClass(String aeName) {
		Class<? extends DataItem> clazz = AE_NAME_MAPPING.revert(aeName);
		return clazz;
	}
	
	public static String getAEName(ContextType contextType) {
		if(contextType.equals(ContextType.UNITUNDERTEST)) {
			return "UnitUnderTest";
		} else if(contextType.equals(ContextType.TESTSEQUENCE)) {
			return "TestSequence";
		} else if(contextType.equals(ContextType.TESTEQUIPMENT)) {
			return "TestEquipment";
		}
		return "";
	}

	public static String getDefaultMimeType(String typeName) {
		return DEFAULT_MIMETYPES.get(typeName);
	}
	
	public static Map<String, Value> nameValues2Values(NameValue[] nvs) throws DataAccessException {
		
		Map<String, Value> map = new HashMap<String, Value>();
		
		for(NameValue nv : nvs) {
			Value value = nameValue2Value(nv);
			map.put(value.getName(), value);
		}
		return map;
	}
		
	public static Value nameValue2Value(NameValue nv) throws DataAccessException {
		return tsValue2Value(nv.valName, nv.value, "");
	}
	
	public static Map<String, Value> nameValueUnits2Values(NameValueUnit[] nvus) throws DataAccessException {
		Map<String, Value> map = new HashMap<String, Value>();
		for(NameValueUnit nvu : nvus) {
			Value value = tsValue2Value(nvu.valName, nvu.value, nvu.unit);
			map.put(value.getName(), value);
		}
		return map;
	}
	
	public static Value tsValue2Value(String name, TS_Value tsv, String unit) throws DataAccessException {
		ValueType type = VALUETYPES.revert(tsv.u.discriminator());		
		Object value;
		
		if(tsv.u.discriminator().equals(DataType.DT_STRING)) {
			value = tsv.u.stringVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_DATE)) {
			value = tsv.u.dateVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_BOOLEAN)) {
			value = tsv.u.booleanVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_BYTE)) {
			value = tsv.u.byteVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_SHORT)) {
			value = tsv.u.shortVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_LONG)) {
			value = tsv.u.longVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_LONGLONG)) {
			value = ODSUtils.asJLong(tsv.u.longlongVal());
		} else if(tsv.u.discriminator().equals(DataType.DT_FLOAT)) {
			value = tsv.u.floatVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_DOUBLE)) {
			value = tsv.u.doubleVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_BYTESTR)) {
			value = tsv.u.bytestrVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_COMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_DCOMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_ENUM)) {
			value = tsv.u.enumVal();
		} else if(tsv.u.discriminator().equals(DataType.DT_EXTERNALREFERENCE)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_BLOB)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_STRING)) {
			value = tsv.u.stringSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_DATE)) {
			value = ODSUtils.asJDate(tsv.u.dateSeq());
		} else if(tsv.u.discriminator().equals(DataType.DS_BOOLEAN)) {
			value = tsv.u.booleanSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_BYTE)) {
			value = tsv.u.byteSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_SHORT)) {
			value = tsv.u.shortSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_LONG)) {
			value = tsv.u.longSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_LONGLONG)) {
			value = ODSUtils.asJLong(tsv.u.longlongSeq());
		} else if(tsv.u.discriminator().equals(DataType.DS_FLOAT)) {
			value = tsv.u.floatSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_DOUBLE)) {
			value = tsv.u.doubleSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_BYTESTR)) {
			value = tsv.u.bytestrSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_COMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_DCOMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_ENUM)) {
			value = tsv.u.enumSeq();
		} else if(tsv.u.discriminator().equals(DataType.DS_EXTERNALREFERENCE)) {
			value = null;
		} else {
			throw new DataAccessException("type '" + tsv.u.discriminator() + "' not implemented!");
		}
		
		return type.newValue(name, value, unit, flag2bool(tsv));
	}
	
	public static TS_Value value2TSValue(Value value) throws DataAccessException {
		TS_Value tsv = ODSUtils.createEmptyTS_Value(VALUETYPES.convert(value.getValueType()));
		
		if(tsv.u.discriminator().equals(DataType.DT_STRING)) {
			tsv.u.stringVal((String)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_DATE)) {
			tsv.u.dateVal(ODSUtils.asODSDate((Date)value.getValue()));
		} else if(tsv.u.discriminator().equals(DataType.DT_BOOLEAN)) {
			tsv.u.booleanVal((boolean)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_BYTE)) {
			tsv.u.byteVal((byte)value.getValue());			
		} else if(tsv.u.discriminator().equals(DataType.DT_SHORT)) {
			tsv.u.shortVal((short)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_LONG)) {
			tsv.u.longVal((int)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_LONGLONG)) {
			tsv.u.longlongVal(ODSUtils.asODSLongLong((long)value.getValue()));
		} else if(tsv.u.discriminator().equals(DataType.DT_FLOAT)) {
			tsv.u.floatVal((float)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_DOUBLE)) {
			tsv.u.doubleVal((double)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_BYTESTR)) {
			tsv.u.bytestrVal((byte[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_COMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_DCOMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_ENUM)) {
			tsv.u.enumVal((int)value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DT_EXTERNALREFERENCE)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DT_BLOB)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_STRING)) {
			tsv.u.stringSeq((String[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_DATE)) {
			tsv.u.dateSeq(ODSUtils.asODSDate((Date[])value.getValue()));
		} else if(tsv.u.discriminator().equals(DataType.DS_BOOLEAN)) {
			tsv.u.booleanSeq((boolean[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_BYTE)) {
			tsv.u.byteSeq((byte[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_SHORT)) {
			tsv.u.shortSeq((short[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_LONG)) {
			tsv.u.longSeq((int[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_LONGLONG)) {
			tsv.u.longlongSeq(ODSUtils.asODSLongLong((long[])value.getValue()));
		} else if(tsv.u.discriminator().equals(DataType.DS_FLOAT)) {
			tsv.u.floatSeq((float[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_DOUBLE)) {
			tsv.u.doubleSeq((double[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_BYTESTR)) {
			tsv.u.bytestrSeq((byte[][])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_COMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_DCOMPLEX)) {
			value = null;
		} else if(tsv.u.discriminator().equals(DataType.DS_ENUM)) {
			tsv.u.enumSeq((int[])value.getValue());
		} else if(tsv.u.discriminator().equals(DataType.DS_EXTERNALREFERENCE)) {
			value = null;
		} else {
			throw new DataAccessException("type '" + tsv.u.discriminator() + "' not implemented!");
		}
		
		tsv.flag = boolen2flag(value.isValid());
		return tsv;
	}
	
	public static Map<String, Value> createDataItemForEntity(Entity entity) {
		Map<String, Value> valueMap = new HashMap<String, Value>();
		for(Attribute attribute : entity.getAttributes()) {
			valueMap.put(attribute.getName(),attribute.create(null));
		}
		return valueMap;
	}
	
	public static void destroyInstanceElement(InstanceElement ie) throws DataAccessException {
		try {
			if(ie != null) {
				ie.destroy();
			}
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}
		
	public static Map<String, Value> getContext(ODSCache odsCache) throws DataAccessException {
		try {
			NameValueIterator nvi  = odsCache.getAoSession().getContext("*");
			NameValue[] context = nvi.nextN(nvi.getCount());
			return nameValues2Values(context);
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}
	
	private static boolean flag2bool(TS_Value tsv) {
		if(tsv.flag == (short)15) {
			return true;
		}
		return false;
	}
	
	private static short boolen2flag(boolean flag) {
		return flag?(short)15:(short)0;
	}

	/**
	 * Returns an array of ODS T_LONGLONG from Java longs.
	 * 
	 * @param v array of Java long values
	 * @return array of ODS T_LONGLONG values
	 */
	public static T_LONGLONG[] asODSLongLong(long[] v) {
	    T_LONGLONG[] ar = new T_LONGLONG[v.length];
	    for (int i = 0; i < v.length; i++) {
	        ar[i] = ODSUtils.asODSLongLong(v[i]);
	    }
	    return ar;
	}

	/**
	 * Return ODS T_LONGLONG from Java long.
	 * 
	 * @param v Java long value
	 * @return ODS T_LONGLONG with the same value as v
	 */
	public static T_LONGLONG asODSLongLong(long v) {
	    return new T_LONGLONG((int) ((v >> 32) & 0xffffffffL), (int) (v & 0xffffffffL));
	}

	/**
	 * Returns an array of Java long from ODS T_LONGLONG.
	 * 
	 * @param ll array of ODS T_LONGLONG values
	 * @return array of Java long values
	 */
	public static long[] asJLong(T_LONGLONG[] ll) {
	    long[] ar = new long[ll.length];
	    for (int i = 0; i < ll.length; i++) {
	        ar[i] = ODSUtils.asJLong(ll[i]);
	    }
	    return ar;
	}

	/**
	 * Returns a Java long from ODS T_LONGLONG.
	 * 
	 * @param ll ODS T_LONGLONG value
	 * @return Java long with the same value as ll
	 */
	public static long asJLong(T_LONGLONG ll) {
	    long tmp;
	    if (ll.low >= 0) {
	        tmp = (long) ll.high * 0x100000000L + (long) ll.low;
	    } else {
	        tmp = (long) (ll.high + 1) * 0x100000000L + (long) ll.low;
	    }
	    return (tmp);
	}

	public static synchronized Date[] asJDate(String[] odsDates) {
	   	List<Date> list = new ArrayList<Date>();
		for(String odsDate : odsDates) {
			list.add(ODSUtils.asJDate(odsDate));
		}
		return list.toArray(new Date[list.size()]);
	}

	/**
	 * Returns the java date from an ODS date.
	 * 
	 * @param odsDate the ODS date string
	 * @return the java <code>java.util.Date</code> object, null if empty date
	 * @throws IllegalArgumentException unable to parse
	 */
	public static synchronized Date asJDate(String odsDate) {
	    try {
	        if (odsDate == null || odsDate.length() < 1) {
	            return null;
	        }
	        DateFormat format = ODS_DATEFORMATS.get(odsDate.length());
	        if (format == null) {
	            throw new IllegalArgumentException("Invalid ODS date: " + odsDate);
	        }
	        return format.parse(odsDate);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Invalid ODS date: " + odsDate);
	    }
	}

	public static synchronized String[] asODSDate(Date[] dates) {
		List<String> list = new ArrayList<String>();
		for(Date date : dates) {
			list.add(ODSUtils.asODSDate(date));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Return an ODS date from a <code>java.util.Date</code>.
	 * 
	 * @param date the <code>java.util.Date</code> to convert
	 * @return the date in ODS date-format (YYYYMMDDhhmmss)
	 */
	public static synchronized String asODSDate(Date date) {
	    if (date == null) {
	        return "";
	    }
	    return ODS_DATEFORMATS.get(14).format(date);
	}

	/**
	 * Creates an empty <code>org.asam.ods.TS_Value</code> with given datatype as discriminator and flag=0
	 * 
	 * @param dt the datatype
	 * @return TS_Value
	 */
	public static TS_Value createEmptyTS_Value(DataType dt) {
	    TS_Value value = new TS_Value();
	    value.flag = 0;
	    value.u = createEmptyTS_Union(dt);
	    return value;
	}

	public static TS_Union createEmptyTS_Union(DataType dt) {
		TS_Union u = new TS_Union();
		// DS_BOOLEAN
		if (dt == DataType.DS_BOOLEAN) {
			u.booleanSeq(new boolean[0]);
		}
		// DS_BYTE
		else if (dt == DataType.DS_BYTE) {
			u.byteSeq(new byte[0]);
		}
		// DS_BYTESTR
		else if (dt == DataType.DS_BYTESTR) {
			u.bytestrSeq(new byte[0][0]);
		}
		// DS_COMPLEX
		else if (dt == DataType.DS_COMPLEX) {
			u.complexSeq(new T_COMPLEX[0]);
		}
		// DS_DATE
		else if (dt == DataType.DS_DATE) {
			u.dateSeq(new String[0]);
		}
		// DS_DCOMPLEX
		else if (dt == DataType.DS_DCOMPLEX) {
			u.dcomplexSeq(new T_DCOMPLEX[0]);
		}
		// DS_DOUBLE
		else if (dt == DataType.DS_DOUBLE) {
			u.doubleSeq(new double[0]);
		}
		// DS_ENUM
		else if (dt == DataType.DS_ENUM) {
			u.enumSeq(new int[0]);
		}
		// DS_EXTERNALREFERENCE
		else if (dt == DataType.DS_EXTERNALREFERENCE) {
			u.extRefSeq(new T_ExternalReference[0]);
		}
		// DS_FLOAT
		else if (dt == DataType.DS_FLOAT) {
			u.floatSeq(new float[0]);
		}
		// DS_LONG
		else if (dt == DataType.DS_LONG) {
			u.longSeq(new int[0]);
		}
		// DS_LONGLONG
		else if (dt == DataType.DS_LONGLONG) {
			u.longlongSeq(new T_LONGLONG[0]);
		}
		// DS_SHORT
		else if (dt == DataType.DS_SHORT) {
			u.shortSeq(new short[0]);
		}
		// DS_STRING
		else if (dt == DataType.DS_STRING) {
			u.stringSeq(new String[0]);
		}
		// DT_BOOLEAN
		else if (dt == DataType.DT_BOOLEAN) {
			u.booleanVal(false);
		}
		// DT_BYTE
		else if (dt == DataType.DT_BYTE) {
			u.byteVal((byte) 0);
		}
		// DT_BYTESTR
		else if (dt == DataType.DT_BYTESTR) {
			u.bytestrVal(new byte[0]);
		}
		// DT_COMPLEX
		else if (dt == DataType.DT_COMPLEX) {
			T_COMPLEX complex = new T_COMPLEX();
			complex.i = 0;
			complex.r = 0;
			u.complexVal(complex);
		}
		// DT_DATE
		else if (dt == DataType.DT_DATE) {
			u.dateVal("");
		}
		// DT_DCOMPLEX
		else if (dt == DataType.DT_DCOMPLEX) {
			T_DCOMPLEX dcomplex = new T_DCOMPLEX();
			dcomplex.i = 0;
			dcomplex.r = 0;
			u.dcomplexVal(dcomplex);
		}
		// DT_DOUBLE
		else if (dt == DataType.DT_DOUBLE) {
			u.doubleVal(0);
		}
		// DT_ENUM
		else if (dt == DataType.DT_ENUM) {
			u.enumVal(0);
		}
		// DT_EXTERNALREFERENCE
		else if (dt == DataType.DT_EXTERNALREFERENCE) {
			T_ExternalReference extRef = new T_ExternalReference();
			extRef.description = "";
			extRef.mimeType = "";
			extRef.location = "";
			u.extRefVal(extRef);
		}
		// DT_FLOAT
		else if (dt == DataType.DT_FLOAT) {
			u.floatVal(0);
		}
		// DT_LONG
		else if (dt == DataType.DT_LONG) {
			u.longVal(0);
		}
		// DT_LONGLONG
		else if (dt == DataType.DT_LONGLONG) {
			u.longlongVal(ODSUtils.asODSLongLong(0));
		}
		// DT_SHORT
		else if (dt == DataType.DT_SHORT) {
			u.shortVal((short) 0);
		}
		// DT_STRING
		else if (dt == DataType.DT_STRING) {
			u.stringVal("");
		}
		return u;
	}
	
}
