/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.DataType;
import org.asam.ods.ErrorCode;
import org.asam.ods.NameValue;
import org.asam.ods.SeverityFlag;
import org.asam.ods.TS_Union;
import org.asam.ods.TS_UnionSeq;
import org.asam.ods.TS_Value;
import org.asam.ods.TS_ValueSeq;
import org.asam.ods.T_COMPLEX;
import org.asam.ods.T_DCOMPLEX;
import org.asam.ods.T_ExternalReference;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
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

	/**
	 * TODO:
	 *
	 * - sort types by expected occurrence count
	 * - ensure types are implemented in all relevant methods
	 */

	public static List<Object> extract(TS_ValueSeq tsvs) throws AoException {
		DataType dataType = tsvs.u.discriminator();
		List<Object> values;

		if(DataType.DT_STRING == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.stringVal());
		} else if(DataType.DT_LONGLONG == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_LONGLONG longValue : tsvs.u.longlongVal()) {
				values.add(fromODSLong(longValue));
			}
		} else if(DataType.DT_BOOLEAN == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Boolean booleanValue : tsvs.u.booleanVal()) {
				values.add(booleanValue);
			}
		} else if(DataType.DT_BYTE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Byte byteValue : tsvs.u.byteVal()) {
				values.add(byteValue);
			}
		} else if(DataType.DT_BYTESTR == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.bytestrVal());
		} else if(DataType.DT_COMPLEX == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_COMPLEX complexValue : tsvs.u.complexVal()) {
				values.add(null); // TODO
			}
		} else if(DataType.DT_DATE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(String dateValue : tsvs.u.dateVal()) {
				values.add(fromODSDate(dateValue));
			}
		} else if(DataType.DT_DCOMPLEX == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_DCOMPLEX dcomplexValue : tsvs.u.dcomplexVal()) {
				values.add(null); // TODO
			}
		} else if(DataType.DT_DOUBLE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Double doubleValue : tsvs.u.doubleVal()) {
				values.add(doubleValue);
			}
		} else if(DataType.DT_ENUM == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Integer enumValue : tsvs.u.enumVal()) {
				values.add(enumValue);
			}
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_ExternalReference externalReferenceValue : tsvs.u.extRefVal()) {
				values.add(null); // TODO
			}
		} else if(DataType.DT_FLOAT == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Float floatValue : tsvs.u.floatVal()) {
				values.add(floatValue);
			}
		} else if(DataType.DT_LONG == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Integer integerValue : tsvs.u.longVal()) {
				values.add(integerValue);
			}
		} else if(DataType.DT_SHORT == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(Short shortValue : tsvs.u.shortVal()) {
				values.add(shortValue);
			}
		} else if(DataType.DS_BOOLEAN == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.booleanSeq());
		} else if(DataType.DS_BYTE == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.byteSeq());
		} else if(DataType.DS_BYTESTR == dataType) {
			values = Arrays.asList((Object) tsvs.u.bytestrSeq());
		} else if(DataType.DS_COMPLEX == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_COMPLEX[] complexValues : tsvs.u.complexSeq()) {
				values.add(null); // TODO
			}
		} else if(DataType.DS_DATE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(String[] dateValues : tsvs.u.dateSeq()) {
				values.add(fromODSDateSEQ(dateValues));
			}
		} else if(DataType.DS_DCOMPLEX == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_DCOMPLEX[] dcomplexValues : tsvs.u.dcomplexSeq()) {
				values.add(null); // TODO
			}
		} else if(DataType.DS_DOUBLE == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.doubleSeq());
		} else if(DataType.DS_ENUM == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(int[] enumValues : tsvs.u.enumSeq()) {
				values.add(enumValues); // TODO
			}
		} else if(DataType.DS_EXTERNALREFERENCE == dataType) {
			values = new ArrayList<>(tsvs.flag.length);
			for(T_ExternalReference[] externalReferenceValues : tsvs.u.extRefSeq()) {
				values.add(null); // TODO
			}
		} else if(DataType.DS_FLOAT == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.floatSeq());
		} else if(DataType.DS_LONG == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.longSeq());
		} else if(DataType.DS_LONGLONG == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.longlongSeq());
		} else if(DataType.DS_SHORT == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.shortSeq());
		} else if(DataType.DS_STRING == dataType) {
			values = Arrays.asList((Object[]) tsvs.u.stringSeq());
		} else {
			throw new AoException(ErrorCode.AO_INVALID_DATATYPE, SeverityFlag.ERROR, 0,
					"Unknown DataType: " + dataType.value());
		}

		return values;
	}

	public static Value fromODSValueSeq(String name, String unit, TS_ValueSeq tsvs) {
		DataType dataType = tsvs.u.discriminator();
		Object input = null;

		if (DataType.DT_STRING == dataType) {
			input = tsvs.u.stringVal();
		} else if (DataType.DT_SHORT == dataType) {
			input = tsvs.u.shortVal();
		} else if (DataType.DT_FLOAT == dataType) {
			input = tsvs.u.floatVal();
		} else if (DataType.DT_BOOLEAN == dataType) {
			input = tsvs.u.booleanVal();
		} else if (DataType.DT_BYTE == dataType) {
			input = tsvs.u.byteVal();
		} else if (DataType.DT_LONG == dataType) {
			input = tsvs.u.longVal();
		} else if (DataType.DT_DOUBLE == dataType) {
			input = tsvs.u.doubleVal();
		} else if (DataType.DT_LONGLONG == dataType) {
			input = tsvs.u.longlongVal();
		} else if (DataType.DT_DATE == dataType) {
			input = fromODSDateSEQ(tsvs.u.dateVal());
		} else if (DataType.DT_BYTESTR == dataType) {
			input = tsvs.u.bytestrVal();
		} else if (DataType.DT_COMPLEX == dataType) {
			input =  null; // TODO
		} else if (DataType.DT_DCOMPLEX == dataType) {
			input =  null; // TODO
		} else if (DataType.DT_EXTERNALREFERENCE == dataType) {
			input =  null; // TODO
		} else if (DataType.DT_ENUM == dataType) {
			input =  tsvs.u.enumVal(); // TODO
		} else {
			// TODO
			throw new IllegalStateException();
		}

		return ODSUtils.VALUETYPES.revert(dataType).toSequenceType().newValue(name, unit, input);
	}

	public static TS_ValueSeq toODSValueSeq(List<Value> values) throws DataAccessException {

		TS_ValueSeq tsvs = new TS_ValueSeq();
		tsvs.flag = new short[values.size()];
		tsvs.u = new TS_UnionSeq();

		if(values == null || values.size() <= 0) {
			tsvs.u.__default();
			return tsvs;
		}

		DataType dataType = ODSUtils.VALUETYPES.convert(values.get(0).getValueType());

		if(DataType.DT_STRING == dataType) {

			List<String> list = new ArrayList<String>();
			for(int i=0; i<values.size(); i++) {
				list.add(values.get(i).extract());
			}
			tsvs.u.stringVal(list.toArray(new String[list.size()]));
		} else if(DataType.DT_DATE == dataType) {

			List<String> list = new ArrayList<String>();
			for(int i=0; i<values.size(); i++) {
				list.add(toODSDate(values.get(i).extract()));
			}
			tsvs.u.dateVal(list.toArray(new String[list.size()]));
		} else if(DataType.DT_BOOLEAN == dataType) {

			boolean[] list = new boolean[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Boolean)values.get(i).extract()).booleanValue();
			}
			tsvs.u.booleanVal(list);
		} else if(DataType.DT_BYTE == dataType) {

			byte[] list = new byte[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Byte)values.get(i).extract()).byteValue();
			}
			tsvs.u.byteVal(list);
		} else if(DataType.DT_SHORT == dataType) {

			short[] list = new short[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Short)values.get(i).extract()).shortValue();
			}
			tsvs.u.shortVal(list);
		} else if(DataType.DT_LONG == dataType) {

			int[] list = new int[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Integer)values.get(i).extract()).intValue();
			}
			tsvs.u.longVal(list);
		} else if(DataType.DT_LONGLONG == dataType) {

			T_LONGLONG[] list = new T_LONGLONG[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = toODSLong(values.get(i).extract());
			}
			tsvs.u.longlongVal(list);
		} else if(DataType.DT_FLOAT == dataType) {

			float[] list = new float[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Float)values.get(i).extract()).floatValue();
			}
			tsvs.u.floatVal(list);
		} else if(DataType.DT_DOUBLE == dataType) {

			double[] list = new double[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Double)values.get(i).extract()).doubleValue();
			}
			tsvs.u.doubleVal(list);
		} else if(DataType.DT_ENUM == dataType) {
			int[] list = new int[values.size()];
			for(int i=0; i<values.size(); i++) {
				list[i] = ((Integer)values.get(i).extract()).intValue();
			}
			tsvs.u.enumVal(list);

		} else if(DataType.DS_LONG == dataType) {
			int[][] list = new int[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (int[])values.get(i).extract();
			}
			tsvs.u.longSeq(list);
		} else if(DataType.DS_DOUBLE == dataType) {
			double[][] list = new double[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (double[])values.get(i).extract();
			}
			tsvs.u.doubleSeq(list);
		} else if(DataType.DS_FLOAT == dataType) {
			float[][] list = new float[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (float[])values.get(i).extract();
			}
			tsvs.u.floatSeq(list);
		} else if(DataType.DS_SHORT == dataType) {
			short[][] list = new short[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (short[])values.get(i).extract();
			}
			tsvs.u.shortSeq(list);
		} else if(DataType.DS_BOOLEAN == dataType) {
			boolean[][] list = new boolean[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (boolean[])values.get(i).extract();
			}
			tsvs.u.booleanSeq(list);
		} else if(DataType.DS_BYTE == dataType) {
			byte[][] list = new byte[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (byte[])values.get(i).extract();
			}
			tsvs.u.byteSeq(list);
		} else if(DataType.DS_STRING == dataType) {
			String[][] list = new String[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = (String[])values.get(i).extract();
			}
			tsvs.u.stringSeq(list);
		} else if(DataType.DS_DATE == dataType) {
			String[][] list = new String[values.size()][];
			for(int i=0; i<values.size(); i++) {
				list[i] = toODSDateSEQ(values.get(i).extract());
			}
			tsvs.u.dateSeq(list);
		} else if(DataType.DS_BYTESTR == dataType) {
			byte[][][] list = new byte[values.size()][][];
			for(int i=0; i<values.size(); i++) {
				list[i] = values.get(i).extract();
			}
			tsvs.u.bytestrSeq(list);
		} else if(DataType.DS_EXTERNALREFERENCE == dataType) {
			tsvs.u.extRefSeq(new T_ExternalReference[0][0]);
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			tsvs.u.extRefVal(new T_ExternalReference[0]);
		} else {
			throw new DataAccessException("unsupporeted DataType " + dataType.toString());
		}

		for(int i=0; i<values.size(); i++) {
			tsvs.flag[i] = (short) 0;
			if(values.get(i).isValid()) {
				tsvs.flag[i] = (short) 15;
			}
		}
		return tsvs;
	}

	public static TS_Value toODSValue(Value value) throws DataAccessException {
		TS_Value tsv = new TS_Value();
		tsv.flag = (short) (value.isValid() ? 15 : 0);
		tsv.u = new TS_Union();

		DataType dataType = ODSUtils.VALUETYPES.convert(value.getValueType());
		if(DataType.DT_STRING == dataType) {
			tsv.u.stringVal(value.extract());
		} else if(DataType.DT_DATE == dataType) {
			tsv.u.dateVal(toODSDate(value.extract()));
		} else if(DataType.DT_BOOLEAN == dataType) {
			tsv.u.booleanVal(value.extract());
		} else if(DataType.DT_BYTE == dataType) {
			tsv.u.byteVal(value.extract());
		} else if(DataType.DT_SHORT == dataType) {
			tsv.u.shortVal(value.extract());
		} else if(DataType.DT_LONG == dataType) {
			tsv.u.longVal(value.extract());
		} else if(DataType.DT_LONGLONG == dataType) {
			tsv.u.longlongVal(toODSLong(value.extract()));
		} else if(DataType.DT_FLOAT == dataType) {
			tsv.u.floatVal(value.extract());
		} else if(DataType.DT_DOUBLE == dataType) {
			tsv.u.doubleVal(value.extract());
		} else if(DataType.DT_BYTESTR == dataType) {
			tsv.u.bytestrVal(value.extract());
		} else if(DataType.DT_COMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DT_DCOMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DT_ENUM == dataType) {
			tsv.u.enumVal(value.extract());
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			value = null; // TODO
		} else if(DataType.DT_BLOB == dataType) {
			value = null; // TODO
		} else if(DataType.DS_STRING == dataType) {
			tsv.u.stringSeq(getSeq(value));
		} else if(DataType.DS_DATE == dataType) {
			tsv.u.dateSeq(toODSDateSEQ(value.extract()));
		} else if(DataType.DS_BOOLEAN == dataType) {
			tsv.u.booleanSeq(getPrimitiveSeq(boolean[].class, value));
		} else if(DataType.DS_BYTE == dataType) {
			tsv.u.byteSeq(getPrimitiveSeq(byte[].class, value));
		} else if(DataType.DS_SHORT == dataType) {
			tsv.u.shortSeq(getPrimitiveSeq(short[].class, value));
		} else if(DataType.DS_LONG == dataType) {
			tsv.u.longSeq(getPrimitiveSeq(int[].class, value));
		} else if(DataType.DS_LONGLONG == dataType) {
			tsv.u.longlongSeq(toODSLongSEQ(value.extract()));
		} else if(DataType.DS_FLOAT == dataType) {
			tsv.u.floatSeq(getPrimitiveSeq(float[].class, value));
		} else if(DataType.DS_DOUBLE == dataType) {
			tsv.u.doubleSeq(getPrimitiveSeq(double[].class, value));
		} else if(DataType.DS_BYTESTR == dataType) {
			tsv.u.bytestrSeq(getSeq(value));
		} else if(DataType.DS_COMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DS_DCOMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DS_ENUM == dataType) {
			tsv.u.enumSeq(value.extract()); // TODO
		} else if(DataType.DS_EXTERNALREFERENCE == dataType) {
			value = null; // TODO
		} else {
			throw new DataAccessException("Type '" + tsv.u.discriminator() + "' not implemented!");
		}

		return tsv;
	}

	public static Value fromODS(NameValue nameValue) {
		ValueType type = ODSUtils.VALUETYPES.revert(nameValue.value.u.discriminator());
		return type.newValue(nameValue.valName, "", nameValue.value.flag == 15, read(nameValue.value.u));
	}

	@Deprecated // TODO
	private static Object read(TS_Union tsu) {
		DataType dataType = tsu.discriminator();
		Object value;

		if(DataType.DT_STRING == dataType) {
			value = tsu.stringVal();
		} else if(DataType.DT_LONGLONG == dataType) {
			value = fromODSLong(tsu.longlongVal());
		} else if(DataType.DT_BOOLEAN == dataType) {
			value = tsu.booleanVal();
		} else if(DataType.DT_BYTE == dataType) {
			value = tsu.byteVal();
		} else if(DataType.DT_BYTESTR == dataType) {
			value = tsu.bytestrVal();
		} else if(DataType.DT_COMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DT_DATE == dataType) {
			value = fromODSDate(tsu.dateVal());
		} else if(DataType.DT_DCOMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DT_DOUBLE == dataType) {
			value = tsu.doubleVal();
		} else if(DataType.DT_ENUM == dataType) {
			value = tsu.enumVal(); // TODO
		} else if(DataType.DT_EXTERNALREFERENCE == dataType) {
			value = null; // TODO
		} else if(DataType.DT_FLOAT == dataType) {
			value = tsu.floatVal();
		} else if(DataType.DT_LONG == dataType) {
			value = tsu.longVal();
		} else if(DataType.DT_SHORT == dataType) {
			value = tsu.shortVal();
		} else if(DataType.DS_BOOLEAN == dataType) {
			value = tsu.booleanSeq();
		} else if(DataType.DS_BYTE == dataType) {
			value = tsu.byteSeq();
		} else if(DataType.DS_BYTESTR == dataType) {
			value = tsu.bytestrSeq();
		} else if(DataType.DS_COMPLEX == dataType) {
			value = null; // TODO
		} else if(DataType.DS_DATE == dataType) {
			value = fromODSDateSEQ(tsu.dateSeq());
		} else if(DataType.DS_DCOMPLEX == dataType) {
			value = null;
		} else if(DataType.DS_DOUBLE == dataType) {
			value = tsu.doubleSeq();
		} else if(DataType.DS_ENUM == dataType) {
			value = tsu.enumSeq(); // TODO
		} else if(DataType.DS_EXTERNALREFERENCE == dataType) {
			value = null;
		} else if(DataType.DS_FLOAT == dataType) {
			value = tsu.floatSeq();
		} else if(DataType.DS_LONG == dataType) {
			value = tsu.longSeq();
		} else if(DataType.DS_LONGLONG == dataType) {
			value = fromODSLongSEQ(tsu.longlongSeq());
		} else if(DataType.DS_SHORT == dataType) {
			value = tsu.shortSeq();
		} else if(DataType.DS_STRING == dataType) {
			value = tsu.stringSeq();
		} else {
			// TODO throw
			throw new IllegalStateException();
		}

		return value;
	}

	@SuppressWarnings("unchecked")
	private static <T> T[] getSeq(Value value) {
		return (T[]) ((List<T>) value.extract()).toArray();
	}

	@SuppressWarnings("unchecked")
	private static <T> T getPrimitiveSeq(Class<T> clazz, Value value) {
		List<?> values = value.extract();
		T result = (T) Array.newInstance(clazz.getComponentType(), values.size());

		for(int index = 0; index < values.size(); index++) {
			Array.set(result, index, values.get(index));
		}

		return result;
	}

	private static List<Long> fromODSLongSEQ(T_LONGLONG[] input) {
		List<Long> result = new ArrayList<>();

		if(input != null) {
			for(T_LONGLONG value : input) {
				result.add(fromODSLong(value));
			}
		}

		return result;
	}

	public static Long fromODSLong(T_LONGLONG input) {
		T_LONGLONG value = input == null ? new T_LONGLONG() : input;
		return (value.high + (value.low >= 0 ? 0 : 1)) * 0x100000000L + value.low;
	}

	private static T_LONGLONG[] toODSLongSEQ(List<Long> input) {
		List<T_LONGLONG> result = new ArrayList<>(input.size());

		for (Long value : input) {
			result.add(toODSLong(value));
		}

		return input.toArray(new T_LONGLONG[input.size()]);
	}

	public static T_LONGLONG toODSLong(Long input) {
		long value = input == null ? 0 : input.longValue();
		return new T_LONGLONG((int) (value >> 32 & 0xffffffffL), (int) (value & 0xffffffffL));
	}

	private static LocalDateTime[] fromODSDateSEQ(String[] input) {
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

	private static String[] toODSDateSEQ(LocalDateTime[] input) {
		List<String> result = new ArrayList<>(input.length);

		for (LocalDateTime value : input) {
			result.add(toODSDate(value));
		}

		return result.toArray(new String[result.size()]);
	}

	private static String toODSDate(LocalDateTime input) {
		return input == null ? "" : input.format(ODS_DATE_FORMATTERS.get(14));
	}

}
