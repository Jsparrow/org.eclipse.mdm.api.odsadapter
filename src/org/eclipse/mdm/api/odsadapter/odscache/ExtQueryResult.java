/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.odscache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.DataType;
import org.asam.ods.ElemResultSetExt;
import org.asam.ods.ErrorCode;
import org.asam.ods.NameValueSeqUnitId;
import org.asam.ods.NameValueUnit;
import org.asam.ods.ResultSetExt;
import org.asam.ods.SeverityFlag;
import org.asam.ods.TS_Union;
import org.asam.ods.TS_UnionSeq;
import org.asam.ods.TS_Value;
import org.asam.ods.TS_ValueSeq;
import org.asam.ods.T_ExternalReference;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * Wrapper to handle simple access to the result of an ODS extended query.
 * 
 * @author Christian Rechner
 */
public class ExtQueryResult {

	private final ODSCache cache;

	// key=alias name, value=result value (aeName,value)
	private final Map<String, Map<String, NameValueSeqUnitId>> columns;

	private int size = 0;

	private int currentPosition = -1;

	/**
	 * Constructor.
	 * 
	 * @param cache
	 *            the ODS cache
	 * @param resultSet
	 *            Query result set.
	 * @throws AoException
	 *             if something went wrong
	 */
	public ExtQueryResult(ODSCache cache, ResultSetExt[] resultSet) throws AoException {
		this.cache = cache;
		columns = new HashMap<String, Map<String, NameValueSeqUnitId>>();

		// get list of columns
		for (ResultSetExt rsx : resultSet) {
			ElemResultSetExt[] rgErsx = rsx.firstElems;
			for (ElemResultSetExt ersx : rgErsx) {
				for (NameValueSeqUnitId nvsuid : ersx.values) {
					size = nvsuid.value.flag.length;

					// create alias name
					long aid = ODSUtils.asJLong(ersx.aid);
					String aeName = cache.getApplElem(aid).aeName;
					String aaName = nvsuid.valName;

					// put to map
					getAeMap(aeName).put(aaName, nvsuid);

				}
			}
		}

		long sqlMaxRows = cache.getSQLMaxRows();
		if (sqlMaxRows > 0 && size >= sqlMaxRows) {
			throw new AoException(ErrorCode.AO_QUERY_INCOMPLETE, SeverityFlag.ERROR, 0,
					"Result length of the extended query reached the limit of '" + sqlMaxRows + "' rows.");
		}
	}

	/**
	 * Returns the aeMap from the internal columns map. If the map does not
	 * exist it is created and stored in the columns map.
	 * 
	 * @param aeName
	 *            Name of the requested aeMap.
	 * @return Non null aeMap is returned.
	 */
	private Map<String, NameValueSeqUnitId> getAeMap(String aeName) {
		Map<String, NameValueSeqUnitId> aeMap = columns.get(aeName);
		if (aeMap == null) {
			aeMap = new HashMap<String, NameValueSeqUnitId>();
			columns.put(aeName, aeMap);
		}
		return aeMap;
	}

	/**
	 * Returns the number of rows of the query result.
	 * 
	 * @return the number of rows
	 */
	public final int size() {
		return size;
	}

	/**
	 * Moves the current row pointer one down.
	 * 
	 * @return false, if the end of the result set is reached, otherwise true
	 */
	public boolean next() {
		if (hasNext()) {
			currentPosition++;
			return true;
		}
		return false;
	}

	/**
	 * Indicates whether it is possible to move the row pointer one down.
	 * 
	 * @return false if the current row pointer is at the end of the result set
	 */
	public boolean hasNext() {
		if (currentPosition < size() - 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Utility method checks whether result attribute values are valid or not at
	 * current position.
	 * 
	 * @param valueSeq
	 *            The value sequence.
	 * @return True if the flag in the sequence at current position is 15.
	 */
	private boolean isNullVal(TS_ValueSeq valueSeq) {
		if (valueSeq.flag[currentPosition] != 15) {
			return true;
		}
		return false;
	}

	/**
	 * Returns result set attribute of the specified application element name
	 * and application attribute name with the unit id.
	 * 
	 * @param aeName
	 *            the application element name
	 * @param aaName
	 *            The application attribute name.
	 * @return <code>org.asam.ods.NameValueUnitId</code> is returned.
	 * @throws AoException
	 *             if no values of specified application element could be found
	 *             in the query result
	 */
	public NameValueSeqUnitId getNameValueSeqUnitId(String aeName, String aaName) throws AoException {
		Map<String, NameValueSeqUnitId> aeMap = columns.get(aeName);
		if (aeMap != null) {
			NameValueSeqUnitId nvsui = aeMap.get(aaName);
			if (nvsui != null) {
				return nvsui;
			}
		}
		throw new AoException(ErrorCode.AO_NOT_FOUND, SeverityFlag.ERROR, 0,
				"Unable to find value in query result [aeName=" + aeName + ",aaName=" + aaName + "]");
	}

	/**
	 * Returns all result attributes of the specified application element name
	 * at the current row pointer.
	 * 
	 * @param aeName
	 *            the application element name
	 * @return array of <code>org.asam.ods.NameValueUnit</code>
	 * @throws AoException
	 *             if no values of specified application element could be found
	 *             in the query result
	 */
	public NameValueUnit[] getNameValueUnitSeq(String aeName) throws AoException {
		List<NameValueUnit> list = new ArrayList<NameValueUnit>();
		Map<String, NameValueSeqUnitId> aeMap = columns.get(aeName);
		if (aeMap != null) {
			for (NameValueSeqUnitId nvsui : aeMap.values()) {
				NameValueUnit nvu = new NameValueUnit();
				long unitId = ODSUtils.asJLong(nvsui.unitId);
				nvu.unit = cache.getUnitNameById(unitId);
				nvu.valName = nvsui.valName;
				nvu.value = ExtQueryResult.tsValueSeq2tsValue(nvsui.value, currentPosition);
				list.add(nvu);
			}
			return list.toArray(new NameValueUnit[list.size()]);
		}
		throw new AoException(ErrorCode.AO_NOT_FOUND, SeverityFlag.ERROR, 0,
				"Unable to find values in query result [aeName=" + aeName + "]");
	}

	/**
	 * Returns the value sequence for passed application element and attribute
	 * names.
	 * 
	 * @param aeName
	 *            The application element name.
	 * @param aaName
	 *            The application attribute name.
	 * @return value sequence is returned.
	 * @throws AoException
	 *             if no values of could be found in the query result.
	 */
	public TS_ValueSeq getTS_ValueSeq(String aeName, String aaName) throws AoException {
		return getNameValueSeqUnitId(aeName, aaName).value;
	}

	/**
	 * Returns the string value for an application attribute.
	 * 
	 * @param aeName
	 *            The application element name.
	 * @param aaName
	 *            The application attribute name.
	 * @return The value at current position is returned.
	 * @throws AoException
	 *             if no values of could be found in the query result.
	 */
	public String getStringVal(String aeName, String aaName) throws AoException {
		if (isNullVal(aeName, aaName)) {
			return "";
		}
		TS_ValueSeq valueSeq = getTS_ValueSeq(aeName, aaName);
		return valueSeq.u.stringVal()[currentPosition];
	}

	/**
	 * Returns the long long value for an application attribute.
	 * 
	 * @param aeName
	 *            The application element name.
	 * @param aaName
	 *            The application attribute name.
	 * @return The value at current position is returned.
	 * @throws AoException
	 *             if no values of could be found in the query result.
	 */
	public long getLongLongVal(String aeName, String aaName) throws AoException {
		TS_ValueSeq valueSeq = getTS_ValueSeq(aeName, aaName);
		if (isNullVal(valueSeq)) {
			return 0;
		}
		T_LONGLONG ll = valueSeq.u.longlongVal()[currentPosition];
		return ODSUtils.asJLong(ll);
	}

	/**
	 * Returns the validity flag for an application attribute.
	 * 
	 * @param aeName
	 *            The application element name.
	 * @param aaName
	 *            The application attribute name.
	 * @return True if the value at current position is 15.
	 * @throws AoException
	 *             if no values of could be found in the query result.
	 */
	public boolean isNullVal(String aeName, String aaName) throws AoException {
		TS_ValueSeq valueSeq = getTS_ValueSeq(aeName, aaName);
		if (valueSeq.flag[currentPosition] != 15) {
			return true;
		}
		return false;
	}

	/*******************************************************************************************************************
	 * Methods for datatype conversions.
	 ******************************************************************************************************************/

	public static TS_Value tsValueSeq2tsValue(TS_ValueSeq valueSeq, int pos) throws AoException {
		TS_Value tsValue = new TS_Value();
		tsValue.u = tsUnionSeq2tsUnion(valueSeq.u, pos);
		tsValue.flag = valueSeq.flag[pos];
		return tsValue;
	}

	public static TS_Union tsUnionSeq2tsUnion(TS_UnionSeq uSeq, int pos) throws AoException {
		TS_Union u = new TS_Union();
		DataType dt = uSeq.discriminator();
		// DS_BOOLEAN
		if (dt == DataType.DS_BOOLEAN) {
			u.booleanSeq(uSeq.booleanSeq()[pos]);
		}
		// DS_BYTE
		else if (dt == DataType.DS_BYTE) {
			u.byteSeq(uSeq.byteSeq()[pos]);
		}
		// DS_BYTESTR
		else if (dt == DataType.DS_BYTESTR) {
			u.bytestrSeq(uSeq.bytestrSeq()[pos]);
		}
		// DS_COMPLEX
		else if (dt == DataType.DS_COMPLEX) {
			u.complexSeq(uSeq.complexSeq()[pos]);
		}
		// DS_DATE
		else if (dt == DataType.DS_DATE) {
			u.dateSeq(uSeq.dateSeq()[pos]);
		}
		// DS_DCOMPLEX
		else if (dt == DataType.DS_DCOMPLEX) {
			u.dcomplexSeq(uSeq.dcomplexSeq()[pos]);
		}
		// DS_DOUBLE
		else if (dt == DataType.DS_DOUBLE) {
			u.doubleSeq(uSeq.doubleSeq()[pos]);
		}
		// DS_ENUM
		else if (dt == DataType.DS_ENUM) {
			u.enumSeq(uSeq.enumSeq()[pos]);
		}
		// DS_EXTERNALREFERENCE
		else if (dt == DataType.DS_EXTERNALREFERENCE) {
			if (uSeq.extRefSeq().length <= pos) {
				u.extRefSeq(new T_ExternalReference[0]);
			} else {
				u.extRefSeq(uSeq.extRefSeq()[pos]);
			}
		}
		// DS_FLOAT
		else if (dt == DataType.DS_FLOAT) {
			u.floatSeq(uSeq.floatSeq()[pos]);
		}
		// DS_LONG
		else if (dt == DataType.DS_LONG) {
			u.longSeq(uSeq.longSeq()[pos]);
		}
		// DS_LONGLONG
		else if (dt == DataType.DS_LONGLONG) {
			u.longlongSeq(uSeq.longlongSeq()[pos]);
		}
		// DS_SHORT
		else if (dt == DataType.DS_SHORT) {
			u.shortSeq(uSeq.shortSeq()[pos]);
		}
		// DS_STRING
		else if (dt == DataType.DS_STRING) {
			u.stringSeq(uSeq.stringSeq()[pos]);
		}
		// DT_BOOLEAN
		else if (dt == DataType.DT_BOOLEAN) {
			u.booleanVal(uSeq.booleanVal()[pos]);
		}
		// DT_BYTE
		else if (dt == DataType.DT_BYTE) {
			u.byteVal(uSeq.byteVal()[pos]);
		}
		// DT_BYTESTR
		else if (dt == DataType.DT_BYTESTR) {
			u.bytestrVal(uSeq.bytestrVal()[pos]);
		}
		// DT_COMPLEX
		else if (dt == DataType.DT_COMPLEX) {
			u.complexVal(uSeq.complexVal()[pos]);
		}
		// DT_DATE
		else if (dt == DataType.DT_DATE) {
			u.dateVal(uSeq.dateVal()[pos]);
		}
		// DT_DCOMPLEX
		else if (dt == DataType.DT_DCOMPLEX) {
			u.dcomplexVal(uSeq.dcomplexVal()[pos]);
		}
		// DT_DOUBLE
		else if (dt == DataType.DT_DOUBLE) {
			u.doubleVal(uSeq.doubleVal()[pos]);
		}
		// DT_ENUM
		else if (dt == DataType.DT_ENUM) {
			u.enumVal(uSeq.enumVal()[pos]);
		}
		// DT_EXTERNALREFERENCE
		else if (dt == DataType.DT_EXTERNALREFERENCE) {
			if (uSeq.extRefVal().length <= pos) {
				u.extRefVal(null);
			} else {
				u.extRefVal(uSeq.extRefVal()[pos]);
			}
		}
		// DT_FLOAT
		else if (dt == DataType.DT_FLOAT) {
			u.floatVal(uSeq.floatVal()[pos]);
		}
		// DT_LONG
		else if (dt == DataType.DT_LONG) {
			u.longVal(uSeq.longVal()[pos]);
		}
		// DT_LONGLONG
		else if (dt == DataType.DT_LONGLONG) {
			u.longlongVal(uSeq.longlongVal()[pos]);
		}
		// DT_SHORT
		else if (dt == DataType.DT_SHORT) {
			u.shortVal(uSeq.shortVal()[pos]);
		}
		// DT_STRING
		else if (dt == DataType.DT_STRING) {
			u.stringVal(uSeq.stringVal()[pos]);
		}
		// unknown dataType
		else {
			throw new AoException(ErrorCode.AO_INVALID_DATATYPE, SeverityFlag.ERROR, 0,
					"Unknown DataType: " + dt.value());
		}
		return u;
	}

}
