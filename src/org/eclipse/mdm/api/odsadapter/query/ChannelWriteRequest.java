/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.asam.ods.ApplElemAccess;
import org.eclipse.mdm.api.base.model.ChannelValue;
import org.eclipse.mdm.api.base.model.ChannelValuesWriteRequest;
import org.eclipse.mdm.api.base.model.Representation;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ChannelWriteRequest {

	private static final String AE_NAME_CHANNEL = "MeaQuantity";
	private static final String AE_NAME_CHANNELGROUP = "SubMatrix";

	private static final String AE_LC_ATTR_INDEPENDENT = "IndependentFlag";
	private static final String AE_LC_ATTR_REPRESENTATION = "SequenceRepresentation";
	private static final String AE_LC_ATTR_PARAMETERS = "GenerationParameters";
	private static final String AE_LC_ATTR_RAWDATATYPE = "RawDatatype";
	private static final String AE_LC_ATTR_AXISTYPE = "axistype";
	private static final String AE_LC_ATTR_VALUES = "Values";
	private static final String AE_LC_ATTR_FLAGS = "Flags";
	private static final String AE_LC_ATTR_GLOBAL_FLAG = "GlobalFlag";

	private static List<ValueType> SUPPORTED_VALUE_TYPES = new ArrayList<ValueType>();

	static {
		SUPPORTED_VALUE_TYPES.add(ValueType.STRING);
		SUPPORTED_VALUE_TYPES.add(ValueType.STRING_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.SHORT);
		SUPPORTED_VALUE_TYPES.add(ValueType.SHORT_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.INTEGER);
		SUPPORTED_VALUE_TYPES.add(ValueType.INTEGER_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.LONG);
		SUPPORTED_VALUE_TYPES.add(ValueType.LONG_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.FLOAT);
		SUPPORTED_VALUE_TYPES.add(ValueType.FLOAT_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.DOUBLE);
		SUPPORTED_VALUE_TYPES.add(ValueType.DOUBLE_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.BYTE);
		SUPPORTED_VALUE_TYPES.add(ValueType.BYTE_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.DATE);
		SUPPORTED_VALUE_TYPES.add(ValueType.DATE_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.BYTE_STREAM);
		SUPPORTED_VALUE_TYPES.add(ValueType.BYTE_STREAM_SEQUENCE);
		SUPPORTED_VALUE_TYPES.add(ValueType.ENUMERATION);
		SUPPORTED_VALUE_TYPES.add(ValueType.ENUMERATION_SEQUENCE);
	}


	private final Entity lcEntity;
	private final InsertStatement is;
	private List<URI> channelURIs = new ArrayList<URI>();

	public ChannelWriteRequest(ApplElemAccess applElemAccess, Entity entity) {
		lcEntity = entity;
		is = new InsertStatement(applElemAccess, lcEntity);
	}

	public void addWriteRequest(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		channelURIs.add(writeRequest.getChannel().getURI());

		checkRequest(writeRequest);

		List<Value> values = createStorageValues(writeRequest);
		is.next();
		is.insertValues(values);
	}

	public List<URI> send() throws DataAccessException {
		is.execute();
		return channelURIs;
	}


	private List<Value> createStorageValues(ChannelValuesWriteRequest writeRequest)
			throws DataAccessException {

		List<Value> list = new ArrayList<Value>();

		String attrName = lcEntity.getNameAttribute().getName();
		String attrMimeType = lcEntity.getMimeTypeAttribute().getName();

		boolean independent = writeRequest.isIndependent();
		short independentShort = booleanFlag2shortFlag(independent);
		int representation = writeRequest.getRepresentation().ordinal();

		int axisType = writeRequest.getAxisType().ordinal();
		double[] parameters = writeRequest.getParameters();

		long smID = writeRequest.getChannelGroup().getURI().getID();
		long mqID = writeRequest.getChannel().getURI().getID();

		String defaultMimeType = ODSUtils.getDefaultMimeType(lcEntity.getName());

		list.add(ValueType.STRING.newValue(attrName, writeRequest.getChannel().getName()));
		list.add(ValueType.STRING.newValue(attrMimeType, defaultMimeType));
		list.add(ValueType.SHORT.newValue(AE_LC_ATTR_INDEPENDENT, independentShort));
		list.add(ValueType.ENUMERATION.newValue(AE_LC_ATTR_RAWDATATYPE, 0));
		list.add(ValueType.ENUMERATION.newValue(AE_LC_ATTR_REPRESENTATION, representation));
		list.add(ValueType.ENUMERATION.newValue(AE_LC_ATTR_AXISTYPE, axisType));
		list.add(ValueType.DOUBLE_SEQUENCE.newValue(AE_LC_ATTR_PARAMETERS, parameters));
		list.add(ValueType.LONG.newValue(AE_NAME_CHANNEL, mqID));
		list.add(ValueType.LONG.newValue(AE_NAME_CHANNELGROUP, smID));

		createFlagsAndValues(list, writeRequest);

		return list;
	}


	private void createFlagsAndValues(List<Value> list, ChannelValuesWriteRequest writeRequest)
			throws DataAccessException {
		//values
		Value dataValues = writeRequest.getChannelValue().getDataValues();
		String unitName = writeRequest.getChannel().getUnit().getName();
		Value dataValueSeq = dataValues.getValueType().toSequenceType().newValue(dataValues.getName(),
				unitName, dataValues.extract());

		list.add(dataValueSeq);

		//flags
		if(writeRequest.isGlobalValid()) {
			list.add(ValueType.SHORT.newValue(AE_LC_ATTR_GLOBAL_FLAG, (short)15));
		} else {
			short[] flags = booleanFlags2shortFlags(writeRequest.getChannelValue().getFlags());
			list.add(ValueType.SHORT_SEQUENCE.newValue(AE_LC_ATTR_FLAGS, "", true, flags));
		}
	}

	private short[] booleanFlags2shortFlags(boolean[] flags) {
		short[] shortFlags = new short[flags.length];
		for(int i=0; i<shortFlags.length; i++) {
			shortFlags[i] = booleanFlag2shortFlag(flags[i]);
		}
		return shortFlags;
	}

	private short booleanFlag2shortFlag(boolean flag) {
		return flag ? (short)15 : (short)0;
	}

	private void checkRequest(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		checkRepresenstation(writeRequest);
		checkValueType(writeRequest);
		checkValueContent(writeRequest);
		checkFlags(writeRequest);

	}

	private void checkRepresenstation(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		double[] genParameters = writeRequest.getParameters();
		Representation representation = writeRequest.getRepresentation();
		int genParametersLength = genParameters == null ? 0 : genParameters.length;

		if(representation.equals(Representation.EXPLICIT) && genParametersLength > 0) {
			throw new DataAccessException("Representation EXPLICIT "
					+ "must not have generation parameters");
		}

		if(representation.equals(Representation.IMPLICIT_CONSTANT) && genParametersLength != 1) {
			throw new DataAccessException("Representation IMPLICIT_CONSTANT "
					+ "must have 1 generation parameter");
		}

		if(representation.equals(Representation.IMPLICIT_LINEAR) && genParametersLength != 2) {
			throw new DataAccessException("Representation IMPLICIT_LINEAR "
					+ "must have 2 generation parameters");
		}

		if(representation.equals(Representation.IMPLICIT_SAW) && genParametersLength != 3) {
			throw new DataAccessException("Representation IMPLICIT_SAW "
					+ "must have 3 generation parameters");
		}

		if(representation.equals(Representation.RAW_LINEAR) && genParametersLength != 2) {
			throw new DataAccessException("Representation RAW_LINEAR "
					+ "must have 2 generation parameters");
		}

		if(representation.equals(Representation.RAW_LINEAR_CALIBRATED) && genParametersLength != 3) {
			throw new DataAccessException("Representation RAW_LINEAR_CALIBRATED "
					+ "must have 3 generation parameters");
		}

		if(representation.equals(Representation.RAW_POLYNOMIAL) && genParametersLength < 2) {
			throw new DataAccessException("Representation RAW_POLYNOMIAL"
					+ "must have >=2 generation parameters");
		}

		if(representation.equals(Representation.EXTERNAL_COMPONENT)
				|| representation.equals(Representation.RAW_LINEAR_EXTERNAL)
				|| representation.equals(Representation.RAW_LINEAR_CALIBRATED_EXTERNAL)
				|| representation.equals(Representation.RAW_POLYNOMIAL_EXTERNAL)) {
			throw new DataAccessException("reprepsentation '" + representation.toString()
			+ "' is not implemented yet!");
		}

	}

	private void checkValueContent(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		ChannelValue channelValue = writeRequest.getChannelValue();
		if(channelValue == null || channelValue.getDataValues() == null) {
			throw new DataAccessException("the values content of the given WriteRequest is empty (null)!");
		}

		Object dataValueObject = channelValue.getDataValues().extract();

		if(!dataValueObject.getClass().isArray()) {
			throw new DataAccessException("the values content of the given WriteRequest must be an array!");
		}

		if(Array.getLength(dataValueObject) <= 0) {
			throw new DataAccessException("the values content of the given WriteRequest is empty (length 0)!");
		}
	}

	private void checkValueType(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		ValueType valueType = writeRequest.getChannelValue().getDataValues().getValueType();
		if(!SUPPORTED_VALUE_TYPES.contains(valueType)) {
			throw new DataAccessException("the value type '" + valueType.toString() + " is not suppored!");
		}
	}

	private void checkFlags(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		if(writeRequest.isGlobalValid()) {
			return;
		}

		ChannelValue channelValue = writeRequest.getChannelValue();
		if(channelValue.getFlags().length != Array.getLength(channelValue.getDataValues().extract())) {
			throw new DataAccessException("invalid flag flags");
		}
	}
}


