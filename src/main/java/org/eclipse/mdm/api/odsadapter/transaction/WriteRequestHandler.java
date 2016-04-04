/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.DefaultEntityCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class WriteRequestHandler {

	private static final String AE_LC_ATTR_INDEPENDENT = "IndependentFlag";
	private static final String AE_LC_ATTR_REPRESENTATION = "SequenceRepresentation";
	private static final String AE_LC_ATTR_PARAMETERS = "GenerationParameters";
	private static final String AE_LC_ATTR_RAWDATATYPE = "RawDatatype";
	private static final String AE_LC_ATTR_AXISTYPE = "axistype";
	private static final String AE_LC_ATTR_VALUES = "Values";
	private static final String AE_LC_ATTR_FLAGS = "Flags";
	private static final String AE_LC_ATTR_GLOBAL_FLAG = "GlobalFlag";

	private final EntityType localColumnEntityType;
	private final InsertStatement insertStatement;
	private List<URI> channelURIs = new ArrayList<>();

	public WriteRequestHandler(ODSTransaction transaction) {
		localColumnEntityType = transaction.getModelManager().getEntityType("LocalColumn");
		insertStatement = new InsertStatement(transaction, localColumnEntityType);
	}

	public void addRequest(WriteRequest writeRequest) throws DataAccessException {
		channelURIs.add(writeRequest.getChannel().getURI());
		insertStatement.add(createCore(writeRequest));
	}

	public List<URI> execute() throws DataAccessException {
		insertStatement.execute();
		return channelURIs;
	}

	private EntityCore createCore(WriteRequest writeRequest) throws DataAccessException {
		EntityCore entityCore = new DefaultEntityCore(localColumnEntityType);

		entityCore.setImplicitRelation(writeRequest.getChannelGroup(), true);
		entityCore.setInfoRelation(writeRequest.getChannel());

		Map<String, Value> values = entityCore.getValues();
		values.get(Entity.ATTR_NAME).set(writeRequest.getChannel().getName());
		values.get(Entity.ATTR_MIMETYPE).set(ODSUtils.DEFAULT_MIMETYPES.get(localColumnEntityType.getName()));
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

		return entityCore;
	}

}


