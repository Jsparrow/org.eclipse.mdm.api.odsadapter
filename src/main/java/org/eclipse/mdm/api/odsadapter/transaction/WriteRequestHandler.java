/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.DefaultCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public final class WriteRequestHandler {

	private static final String AE_LC_ATTR_INDEPENDENT = "IndependentFlag";
	private static final String AE_LC_ATTR_REPRESENTATION = "SequenceRepresentation";
	private static final String AE_LC_ATTR_PARAMETERS = "GenerationParameters";
	private static final String AE_LC_ATTR_RAWDATATYPE = "RawDatatype";
	private static final String AE_LC_ATTR_AXISTYPE = "axistype";
	private static final String AE_LC_ATTR_VALUES = "Values";
	private static final String AE_LC_ATTR_FLAGS = "Flags";
	private static final String AE_LC_ATTR_GLOBAL_FLAG = "GlobalFlag";

	private final List<Core> cores = new ArrayList<>();
	private final EntityType localColumnEntityType;
	private final InsertStatement insertStatement;

	public WriteRequestHandler(ODSTransaction transaction) {
		localColumnEntityType = transaction.getModelManager().getEntityType("LocalColumn");
		insertStatement = new InsertStatement(transaction, localColumnEntityType);
	}

	public void addRequest(WriteRequest writeRequest) throws DataAccessException {
		cores.add(createCore(writeRequest));
	}

	public void execute() throws AoException, DataAccessException {
		insertStatement.executeWithCores(cores);
	}

	private Core createCore(WriteRequest writeRequest) throws DataAccessException {
		Core core = new DefaultCore(localColumnEntityType);

		core.getPermanentStore().set(writeRequest.getChannelGroup());
		core.getMutableStore().set(writeRequest.getChannel());

		Map<String, Value> values = core.getValues();
		values.get(Entity.ATTR_NAME).set(writeRequest.getChannel().getName());
		values.get(Entity.ATTR_MIMETYPE).set("application/x-asam.aolocalcolumn");
		values.get(AE_LC_ATTR_INDEPENDENT).set(ODSConverter.toODSValidFlag(writeRequest.isIndependent()));
		values.get(AE_LC_ATTR_RAWDATATYPE).set(writeRequest.getRawScalarType());
		values.get(AE_LC_ATTR_REPRESENTATION).set(writeRequest.getSequenceRepresentation());
		values.get(AE_LC_ATTR_AXISTYPE).set(writeRequest.getAxisType());
		values.get(AE_LC_ATTR_PARAMETERS).set(writeRequest.getGenerationParameters());

		if(writeRequest.hasValues()) {
			ValueType valueType = writeRequest.getRawScalarType().toValueType();
			String unitName = writeRequest.getChannel().getUnit().getName();
			values.put(AE_LC_ATTR_VALUES, valueType.create(AE_LC_ATTR_VALUES, unitName, true, writeRequest.getValues()));

			//flags
			if(writeRequest.areAllValid()) {
				values.get(AE_LC_ATTR_GLOBAL_FLAG).set((short) 15);
				// TODO PEAK ODS server issue!
				short[] flags = new short[Array.getLength(writeRequest.getValues())];
				Arrays.fill(flags, (short) 15);
				values.get(AE_LC_ATTR_FLAGS).set(flags);
			} else {
				short[] flags = ODSConverter.toODSValidFlagSeq(writeRequest.getFlags());
				values.get(AE_LC_ATTR_FLAGS).set(flags);
			}
		} else if(writeRequest.hasExternalComponents()) {
			// TODO
			throw new DataAccessException("Not implemented yet.");
		} else {
			// TODO this indicates a not valid write request!!
			throw new DataAccessException("");
		}


		// TODO remove...
		//		if(writeRequest.hasMeasuredValues()) {
		//			MeasuredValues measuredValues = writeRequest.getMeasuredValues();
		//			values.put(AE_LC_ATTR_VALUES, measuredValues.createMeasuredValuesValue(AE_LC_ATTR_VALUES));
		//
		//			//flags
		//			if(writeRequest.areAllValid()) {
		//				values.get(AE_LC_ATTR_GLOBAL_FLAG).set((short) 15);
		//			} else {
		//				short[] flags = ODSConverter.toODSValidFlagSeq(measuredValues.getFlags());
		//				values.get(AE_LC_ATTR_FLAGS).set(flags);
		//			}
		//
		//		} else if(writeRequest.hasExternalComponents()) {
		//			// TODO
		//			throw new DataAccessException("Not implemented yet.");
		//		} else {
		//			// TODO this indicates a not valid write request!!
		//			throw new DataAccessException("");
		//		}

		return core;
	}

}


