/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.Blob;
import org.asam.ods.DataType;
import org.asam.ods.NameValueSeqUnit;
import org.asam.ods.TS_Union;
import org.asam.ods.TS_UnionSeq;
import org.asam.ods.TS_Value;
import org.asam.ods.TS_ValueSeq;
import org.asam.ods.T_COMPLEX;
import org.asam.ods.T_DCOMPLEX;
import org.asam.ods.T_ExternalReference;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.DoubleComplex;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.FloatComplex;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;

public final class ODSConverter {

	private static final Map<Integer, DateTimeFormatter> ODS_DATE_FORMATTERS = new HashMap<>();

	static {
		ODS_DATE_FORMATTERS.put(4, DateTimeFormatter.ofPattern("yyyy"));
		ODS_DATE_FORMATTERS.put(6, DateTimeFormatter.ofPattern("yyyyMM"));
		ODS_DATE_FORMATTERS.put(8, DateTimeFormatter.ofPattern("yyyyMMdd"));
		ODS_DATE_FORMATTERS.put(10, DateTimeFormatter.ofPattern("yyyyMMddHH"));
		ODS_DATE_FORMATTERS.put(12, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		ODS_DATE_FORMATTERS.put(14, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		ODS_DATE_FORMATTERS.put(17, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	}

	private ODSConverter() {}

	public static List<Value> fromODSValueSeq(Attribute attribute, String unit, TS_ValueSeq odsValueSeq)
			throws DataAccessException {
		DataType dataType = odsValueSeq.u.discriminator();
		short[] flags = odsValueSeq.flag;
		List<Value> values = new ArrayList<>(flags.length);

		if(DataType.DT_STRING == dataType) {
			String[] odsValues = odsValueSeq.u.stringVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_STRING == dataType) {
			String[][] odsValues = odsValueSeq.u.stringSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_DATE == dataType) {
			String[] odsValues = odsValueSeq.u.dateVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSDate(odsValues[i])));
			}
		} else if(DataType.DS_DATE == dataType) {
			String[][] odsValues = odsValueSeq.u.dateSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSDateSeq(odsValues[i])));
			}
		} else if(DataType.DT_BOOLEAN == dataType) {
			boolean[] odsValues = odsValueSeq.u.booleanVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_BOOLEAN == dataType) {
			boolean[][] odsValues = odsValueSeq.u.booleanSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_BYTE == dataType) {
			byte[] odsValues = odsValueSeq.u.byteVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_BYTE == dataType) {
			byte[][] odsValues = odsValueSeq.u.byteSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_SHORT == dataType) {
			short[] odsValues = odsValueSeq.u.shortVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_SHORT == dataType) {
			short[][] odsValues = odsValueSeq.u.shortSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_LONG == dataType) {
			int[] odsValues = odsValueSeq.u.longVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_LONG == dataType) {
			int[][] odsValues = odsValueSeq.u.longSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_LONGLONG == dataType) {
			T_LONGLONG[] odsValues = odsValueSeq.u.longlongVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSLong(odsValues[i])));
			}
		} else if(DataType.DS_LONGLONG == dataType) {
			T_LONGLONG[][] odsValues = odsValueSeq.u.longlongSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSLongSeq(odsValues[i])));
			}
		} else if(DataType.DT_FLOAT == dataType) {
			float[] odsValues = odsValueSeq.u.floatVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_FLOAT == dataType) {
			float[][] odsValues = odsValueSeq.u.floatSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_DOUBLE == dataType) {
			double[] odsValues = odsValueSeq.u.doubleVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_DOUBLE == dataType) {
			double[][] odsValues = odsValueSeq.u.doubleSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_BYTESTR == dataType) {
			byte[][] odsValues = odsValueSeq.u.bytestrVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DS_BYTESTR == dataType) {
			byte[][][] odsValues = odsValueSeq.u.bytestrSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, odsValues[i]));
			}
		} else if(DataType.DT_COMPLEX == dataType) {
			T_COMPLEX[] odsValues = odsValueSeq.u.complexVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSFloatComplex(odsValues[i])));
			}
		} else if(DataType.DS_COMPLEX == dataType) {
			T_COMPLEX[][] odsValues = odsValueSeq.u.complexSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSFloatComplexSeq(odsValues[i])));
			}
		} else if(DataType.DT_DCOMPLEX == dataType) {
			T_DCOMPLEX[] odsValues = odsValueSeq.u.dcomplexVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSDoubleComplex(odsValues[i])));
			}
		} else if(DataType.DS_DCOMPLEX == dataType) {
			T_DCOMPLEX[][] odsValues = odsValueSeq.u.dcomplexSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSDoubleComplexSeq(odsValues[i])));
			}
		} else if(DataType.DT_ENUM == dataType) {
			int[] odsValues = odsValueSeq.u.enumVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15,
						ODSEnumerations.fromODSEnum(attribute.getEnumClass(), odsValues[i])));
			}
		} else if(DataType.DS_ENUM == dataType) {
			int[][] odsValues = odsValueSeq.u.enumSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15,
						ODSEnumerations.fromODSEnumSeq(attribute.getEnumClass(), odsValues[i])));
			}
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			T_ExternalReference[] odsValues = odsValueSeq.u.extRefVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSExternalReference(odsValues[i])));
			}
		} else if(DataType.DS_EXTERNALREFERENCE == dataType) {
			T_ExternalReference[][] odsValues = odsValueSeq.u.extRefSeq();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSExternalReferenceSeq(odsValues[i])));
			}
		} else if(DataType.DT_BLOB == dataType) {
			Blob[] odsValues = odsValueSeq.u.blobVal();
			for(int i = 0; i < flags.length; i++) {
				values.add(attribute.createValue(unit, flags[i] == 15, fromODSBlob(odsValues[i])));
			}
		} else {
			throw new DataAccessException("Conversion for ODS data type '" + dataType.toString() + "' does not exist.");
		}

		return values;
	}

	public static TS_ValueSeq toODSValueSeq(List<Value> values) throws DataAccessException {
		int size = values == null ? 0 : values.size();
		short[] flags = new short[size];

		TS_ValueSeq odsValueSeq = new TS_ValueSeq(new TS_UnionSeq(), flags);
		if(values == null || size < 1) {
			odsValueSeq.u.__default();
			return odsValueSeq;
		}

		ValueType type = values.get(0).getValueType();
		if(ValueType.STRING == type) {
			String[] odsValues = new String[size];
			for(int i = 0; i < size; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.stringVal(odsValues);
		} else if(ValueType.STRING_SEQUENCE == type) {
			String[][] odsValues = new String[size][];
			for(int i = 0; i < size; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.stringSeq(odsValues);
		} else if(ValueType.DATE == type) {
			String[] odsValues = new String[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSDate(value.extract());
			}
			odsValueSeq.u.dateVal(odsValues);
		} else if(ValueType.DATE_SEQUENCE == type) {
			String[][] odsValues = new String[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSDateSeq(value.extract());
			}
			odsValueSeq.u.dateSeq(odsValues);
		} else if(ValueType.BOOLEAN == type) {
			boolean[] odsValues = new boolean[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.booleanVal(odsValues);
		} else if(ValueType.BOOLEAN_SEQUENCE == type) {
			boolean[][] odsValues = new boolean[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.booleanSeq(odsValues);
		} else if(ValueType.BYTE == type) {
			byte[] odsValues = new byte[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.byteVal(odsValues);
		} else if(ValueType.BYTE_SEQUENCE == type) {
			byte[][] odsValues = new byte[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.byteSeq(odsValues);
		} else if(ValueType.SHORT == type) {
			short[] odsValues = new short[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.shortVal(odsValues);
		} else if(ValueType.SHORT_SEQUENCE == type) {
			short[][] odsValues = new short[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.shortSeq(odsValues);
		} else if(ValueType.INTEGER == type) {
			int[] odsValues = new int[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.longVal(odsValues);
		} else if(ValueType.INTEGER_SEQUENCE == type) {
			int[][] odsValues = new int[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.longSeq(odsValues);
		} else if(ValueType.LONG == type) {
			T_LONGLONG[] odsValues = new T_LONGLONG[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSLong(value.extract());
			}
			odsValueSeq.u.longlongVal(odsValues);
		} else if(ValueType.LONG_SEQUENCE == type) {
			T_LONGLONG[][] odsValues = new T_LONGLONG[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSLongSeq(value.extract());
			}
			odsValueSeq.u.longlongSeq(odsValues);
		} else if(ValueType.FLOAT == type) {
			float[] odsValues = new float[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.floatVal(odsValues);
		} else if(ValueType.FLOAT_SEQUENCE == type) {
			float[][] odsValues = new float[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.floatSeq(odsValues);
		} else if(ValueType.DOUBLE == type) {
			double[] odsValues = new double[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.doubleVal(odsValues);
		} else if(ValueType.DOUBLE_SEQUENCE == type) {
			double[][] odsValues = new double[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.doubleSeq(odsValues);
		} else if(ValueType.BYTE_STREAM == type) {
			byte[][] odsValues = new byte[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.bytestrVal(odsValues);
		} else if(ValueType.BYTE_STREAM_SEQUENCE == type) {
			byte[][][] odsValues = new byte[size][][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = value.extract();
			}
			odsValueSeq.u.bytestrSeq(odsValues);
		} else if(ValueType.FLOAT_COMPLEX == type) {
			T_COMPLEX[] odsValues = new T_COMPLEX[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSFloatComplex(value.extract());
			}
			odsValueSeq.u.complexVal(odsValues);
		} else if(ValueType.FLOAT_COMPLEX_SEQUENCE == type) {
			T_COMPLEX[][] odsValues = new T_COMPLEX[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSFloatComplexSeq(value.extract());
			}
			odsValueSeq.u.complexSeq(odsValues);
		} else if(ValueType.DOUBLE_COMPLEX == type) {
			T_DCOMPLEX[] odsValues = new T_DCOMPLEX[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSDoubleComplex(value.extract());
			}
			odsValueSeq.u.dcomplexVal(odsValues);
		} else if(ValueType.DOUBLE_COMPLEX_SEQUENCE == type) {
			T_DCOMPLEX[][] odsValues = new T_DCOMPLEX[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSDoubleComplexSeq(value.extract());
			}
			odsValueSeq.u.dcomplexSeq(odsValues);
		} else if(ValueType.ENUMERATION == type) {
			int[] odsValues = new int[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = ODSEnumerations.toODSEnum(value.extract());
			}
			odsValueSeq.u.enumVal(odsValues);
		} else if(ValueType.ENUMERATION_SEQUENCE == type) {
			int[][] odsValues = new int[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = ODSEnumerations.toODSEnumSeq(value.extract());
			}
			odsValueSeq.u.enumSeq(odsValues);
		} else if(ValueType.FILE_LINK == type) {
			T_ExternalReference[] odsValues = new T_ExternalReference[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSExternalReference(value.extract());
			}
			odsValueSeq.u.extRefVal(odsValues);
		} else if(ValueType.FILE_LINK_SEQUENCE == type) {
			T_ExternalReference[][] odsValues = new T_ExternalReference[size][];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSExternalReferenceSeq(value.extract());
			}
			odsValueSeq.u.extRefSeq(odsValues);
		} else if(ValueType.BLOB == type) {
			Blob[] odsValues = new Blob[size];
			for(int i = 0; i < flags.length; i++) {
				Value value = values.get(i);
				flags[i] = toODSValidFlag(value.isValid());
				odsValues[i] = toODSBlob(value.extract());
			}
			odsValueSeq.u.blobVal(odsValues);
		} else {
			throw new DataAccessException("Mapping for value type '" + type + "' does not exist.");
		}

		return odsValueSeq;
	}

	public static List<MeasuredValues> fromODSMeasuredValuesSeq(NameValueSeqUnit[] odsMeasuredValuesSeq) throws DataAccessException {
		List<MeasuredValues> measuredValues = new ArrayList<>(odsMeasuredValuesSeq.length);

		for(NameValueSeqUnit odsMeasuredValues : odsMeasuredValuesSeq) {
			measuredValues.add(fromODSMeasuredValues(odsMeasuredValues));
		}

		return measuredValues;
	}

	private static MeasuredValues fromODSMeasuredValues(NameValueSeqUnit odsMeasuredValues) throws DataAccessException {
		TS_ValueSeq odsValueSeq = odsMeasuredValues.value;
		DataType dataType = odsValueSeq.u.discriminator();
		ScalarType scalarType;
		Object values;

		if(DataType.DT_STRING == dataType) {
			scalarType = ScalarType.STRING;
			values = odsValueSeq.u.stringVal();
		} else if(DataType.DT_DATE == dataType) {
			scalarType = ScalarType.DATE;
			values = fromODSDateSeq(odsValueSeq.u.dateVal());
		} else if(DataType.DT_BOOLEAN == dataType) {
			scalarType = ScalarType.BOOLEAN;
			values = odsValueSeq.u.booleanVal();
		} else if(DataType.DT_BYTE == dataType) {
			scalarType = ScalarType.BYTE;
			values = odsValueSeq.u.byteVal();
		} else if(DataType.DT_SHORT == dataType) {
			scalarType = ScalarType.SHORT;
			values = odsValueSeq.u.shortVal();
		} else if(DataType.DT_LONG == dataType) {
			scalarType = ScalarType.INTEGER;
			values = odsValueSeq.u.longVal();
		} else if(DataType.DT_LONGLONG == dataType) {
			scalarType = ScalarType.LONG;
			values = fromODSLongSeq(odsValueSeq.u.longlongVal());
		} else if(DataType.DT_FLOAT == dataType) {
			scalarType = ScalarType.FLOAT;
			values = odsValueSeq.u.floatVal();
		} else if(DataType.DT_DOUBLE == dataType) {
			scalarType = ScalarType.DOUBLE;
			values = odsValueSeq.u.doubleVal();
		} else if(DataType.DT_BYTESTR == dataType) {
			scalarType = ScalarType.BYTE_STREAM;
			values = odsValueSeq.u.bytestrVal();
		} else if(DataType.DT_COMPLEX == dataType) {
			scalarType = ScalarType.FLOAT_COMPLEX;
			values = fromODSFloatComplexSeq(odsValueSeq.u.complexVal());
		} else if(DataType.DT_DCOMPLEX == dataType) {
			scalarType = ScalarType.DOUBLE_COMPLEX;
			values = fromODSDoubleComplexSeq(odsValueSeq.u.dcomplexVal());
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			scalarType = ScalarType.FILE_LINK;
			values = fromODSExternalReferenceSeq(odsValueSeq.u.extRefVal());
		} else {
			throw new DataAccessException("Conversion for ODS measured points of type '" + dataType.toString()
			+ "' does not exist.");
		}

		return scalarType.createMeasuredValues(odsMeasuredValues.valName, odsMeasuredValues.unit,
				values, fromODSValidFlagSeq(odsValueSeq.flag));
	}

	public static TS_Value toODSValue(Value value) throws DataAccessException {
		TS_Value odsValue = new TS_Value(new TS_Union(), toODSValidFlag(value.isValid()));
		ValueType type = value.getValueType();

		if(ValueType.STRING == type) {
			odsValue.u.stringVal(value.extract());
		} else if(ValueType.STRING_SEQUENCE == type) {
			odsValue.u.stringSeq(value.extract());
		} else if(ValueType.DATE == type) {
			odsValue.u.dateVal(toODSDate(value.extract()));
		} else if(ValueType.DATE_SEQUENCE == type) {
			odsValue.u.dateSeq(toODSDateSeq(value.extract()));
		} else if(ValueType.BOOLEAN == type) {
			odsValue.u.booleanVal(value.extract());
		} else if(ValueType.BOOLEAN_SEQUENCE == type) {
			odsValue.u.booleanVal(value.extract());
		} else if(ValueType.BYTE == type) {
			odsValue.u.byteVal(value.extract());
		} else if(ValueType.BYTE_SEQUENCE == type) {
			odsValue.u.byteSeq(value.extract());
		} else if(ValueType.SHORT == type) {
			odsValue.u.shortVal(value.extract());
		} else if(ValueType.SHORT_SEQUENCE == type) {
			odsValue.u.shortSeq(value.extract());
		} else if(ValueType.INTEGER == type) {
			odsValue.u.longVal(value.extract());
		} else if(ValueType.INTEGER_SEQUENCE == type) {
			odsValue.u.longSeq(value.extract());
		} else if(ValueType.LONG == type) {
			odsValue.u.longlongVal(toODSLong(value.extract()));
		} else if(ValueType.LONG_SEQUENCE == type) {
			odsValue.u.longlongSeq(toODSLongSeq(value.extract()));
		} else if(ValueType.FLOAT == type) {
			odsValue.u.floatVal(value.extract());
		} else if(ValueType.FLOAT_SEQUENCE == type) {
			odsValue.u.floatSeq(value.extract());
		} else if(ValueType.DOUBLE == type) {
			odsValue.u.doubleVal(value.extract());
		} else if(ValueType.DOUBLE_SEQUENCE == type) {
			odsValue.u.doubleSeq(value.extract());
		} else if(ValueType.BYTE_STREAM == type) {
			odsValue.u.bytestrVal(value.extract());
		} else if(ValueType.BYTE_STREAM_SEQUENCE == type) {
			odsValue.u.bytestrSeq(value.extract());
		} else if(ValueType.FLOAT_COMPLEX == type) {
			odsValue.u.complexVal(toODSFloatComplex(value.extract()));
		} else if(ValueType.FLOAT_COMPLEX_SEQUENCE == type) {
			odsValue.u.complexSeq(toODSFloatComplexSeq(value.extract()));
		} else if(ValueType.DOUBLE_COMPLEX == type) {
			odsValue.u.dcomplexVal(toODSDoubleComplex(value.extract()));
		} else if(ValueType.DOUBLE_COMPLEX_SEQUENCE == type) {
			odsValue.u.dcomplexSeq(toODSDoubleComplexSeq(value.extract()));
		} else if(ValueType.ENUMERATION == type) {
			odsValue.u.enumVal(ODSEnumerations.toODSEnum(value.extract()));
		} else if(ValueType.ENUMERATION_SEQUENCE == type) {
			odsValue.u.enumSeq(ODSEnumerations.toODSEnumSeq(value.extract()));
		} else if(ValueType.FILE_LINK == type) {
			odsValue.u.extRefVal(toODSExternalReference(value.extract()));
		} else if(ValueType.FILE_LINK_SEQUENCE == type) {
			odsValue.u.extRefSeq(toODSExternalReferenceSeq(value.extract()));
		} else if(ValueType.BLOB == type) {
			odsValue.u.blobVal(toODSBlob(value.extract()));
		} else {
			throw new DataAccessException("Mapping for value type '" + type + "' does not exist.");
		}

		return odsValue;
	}

	private static boolean[] fromODSValidFlagSeq(short[] input) {
		boolean[] result = new boolean[input.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = fromODSValidFlag(input[i]);
		}

		return result;
	}

	private static boolean fromODSValidFlag(short input) {
		return input == 15;
	}

	public static short[] toODSValidFlagSeq(boolean[] input) {
		short[] result = new short[input.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = toODSValidFlag(input[i]);
		}

		return result;
	}

	public static short toODSValidFlag(boolean input) {
		return (short) (input ? 15 : 0);
	}

	private static long[] fromODSLongSeq(T_LONGLONG[] input) {
		long[] result = new long[input.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = fromODSLong(input[i]);
		}

		return result;
	}

	public static long fromODSLong(T_LONGLONG input) {
		T_LONGLONG value = input == null ? new T_LONGLONG() : input;
		return (value.high + (value.low >= 0 ? 0 : 1)) * 0x100000000L + value.low;
	}

	private static T_LONGLONG[] toODSLongSeq(long[] input) {
		List<T_LONGLONG> result = new ArrayList<>(input.length);
		for (long value : input) {
			result.add(toODSLong(value));
		}

		return result.toArray(new T_LONGLONG[result.size()]);
	}

	public static T_LONGLONG toODSLong(long input) {
		return new T_LONGLONG((int) (input >> 32 & 0xffffffffL), (int) (input & 0xffffffffL));
	}

	private static LocalDateTime[] fromODSDateSeq(String[] input) {
		List<LocalDateTime> result = new ArrayList<>();
		if(input != null) {
			for(String value : input) {
				result.add(fromODSDate(value));
			}
		}

		return result.toArray(new LocalDateTime[result.size()]);
	}

	private static LocalDateTime fromODSDate(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		}

		DateTimeFormatter formatter = ODS_DATE_FORMATTERS.get(input.length());
		if (formatter == null) {
			throw new IllegalArgumentException("Invalid ODS date: " + input);
		}

		try {
			return LocalDateTime.parse(input, formatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid ODS date: " + input);
		}
	}

	private static String[] toODSDateSeq(LocalDateTime[] input) {
		List<String> result = new ArrayList<>(input.length);
		for (LocalDateTime value : input) {
			result.add(toODSDate(value));
		}

		return result.toArray(new String[result.size()]);
	}

	private static String toODSDate(LocalDateTime input) {
		return input == null ? "" : input.format(ODS_DATE_FORMATTERS.get(14));
	}

	private static FloatComplex[] fromODSFloatComplexSeq(T_COMPLEX[] input) {
		List<FloatComplex> result = new ArrayList<>();
		if(input != null) {
			for(T_COMPLEX value : input) {
				result.add(fromODSFloatComplex(value));
			}
		}

		return result.toArray(new FloatComplex[result.size()]);
	}

	private static FloatComplex fromODSFloatComplex(T_COMPLEX input) {
		return input == null ? null : new FloatComplex(input.r, input.i);
	}

	private static T_COMPLEX[] toODSFloatComplexSeq(FloatComplex[] input) {
		List<T_COMPLEX> result = new ArrayList<>(input.length);
		for (FloatComplex value : input) {
			result.add(toODSFloatComplex(value));
		}

		return result.toArray(new T_COMPLEX[result.size()]);
	}

	private static T_COMPLEX toODSFloatComplex(FloatComplex input) {
		return input == null ? null : new T_COMPLEX(input.real(), input.imaginary());
	}

	private static DoubleComplex[] fromODSDoubleComplexSeq(T_DCOMPLEX[] input) {
		List<DoubleComplex> result = new ArrayList<>();
		if(input != null) {
			for(T_DCOMPLEX value : input) {
				result.add(fromODSDoubleComplex(value));
			}
		}

		return result.toArray(new DoubleComplex[result.size()]);
	}

	private static DoubleComplex fromODSDoubleComplex(T_DCOMPLEX input) {
		return input == null ? null : new DoubleComplex(input.r, input.i);
	}

	private static T_DCOMPLEX[] toODSDoubleComplexSeq(DoubleComplex[] input) {
		List<T_DCOMPLEX> result = new ArrayList<>(input.length);
		for (DoubleComplex value : input) {
			result.add(toODSDoubleComplex(value));
		}

		return result.toArray(new T_DCOMPLEX[result.size()]);
	}

	private static T_DCOMPLEX toODSDoubleComplex(DoubleComplex input) {
		return input == null ? null : new T_DCOMPLEX(input.real(), input.imaginary());
	}

	// TODO EXTERNAL_REFERENCE #########################################################################################

	private static FileLink[] fromODSExternalReferenceSeq(T_ExternalReference[] input) {
		List<FileLink> result = new ArrayList<>();
		if(input != null) {
			for(T_ExternalReference value : input) {
				result.add(fromODSExternalReference(value));
			}
		}

		return result.toArray(new FileLink[result.size()]);
	}

	private static FileLink fromODSExternalReference(T_ExternalReference input) {
		return null; // TODO
	}

	private static T_ExternalReference[] toODSExternalReferenceSeq(FileLink[] input) {
		List<T_ExternalReference> result = new ArrayList<>(input.length);
		for (FileLink value : input) {
			result.add(toODSExternalReference(value));
		}

		return result.toArray(new T_ExternalReference[result.size()]);
	}

	private static T_ExternalReference toODSExternalReference(FileLink input) {
		return null; // TODO
	}

	// TODO BLOB #########################################################################################

	@Deprecated
	private static Object fromODSBlob(Blob input) {
		return null; // TODO
	}

	@Deprecated
	private static Blob toODSBlob(Object input) {
		return null; // TODO
	}

}
