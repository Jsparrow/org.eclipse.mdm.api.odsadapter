/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.adapter.DefaultCore;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

/**
 * Writes mass data specified in {@link WriteRequest}s.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class WriteRequestHandler {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final String AE_LC_ATTR_INDEPENDENT = "IndependentFlag";
	private static final String AE_LC_ATTR_REPRESENTATION = "SequenceRepresentation";
	private static final String AE_LC_ATTR_PARAMETERS = "GenerationParameters";
	private static final String AE_LC_ATTR_RAWDATATYPE = "RawDatatype";
	private static final String AE_LC_ATTR_AXISTYPE = "axistype";
	private static final String AE_LC_ATTR_VALUES = "Values";
	private static final String AE_LC_ATTR_FLAGS = "Flags";
	private static final String AE_LC_ATTR_GLOBAL_FLAG = "GlobalFlag";

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final List<Core> cores = new ArrayList<>();
	private final EntityType localColumnEntityType;
	private final InsertStatement insertStatement;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param transaction
	 *            The owning {@link ODSTransaction}.
	 */
	public WriteRequestHandler(ODSTransaction transaction) {
		localColumnEntityType = transaction.getModelManager().getEntityType("LocalColumn");
		insertStatement = new InsertStatement(transaction, localColumnEntityType);
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Adds given {@link WriteRequest} to be processed.
	 *
	 * @param writeRequest
	 *            The {@code WriteRequest}.
	 */
	public void addRequest(WriteRequest writeRequest) {
		cores.add(createCore(writeRequest));
	}

	/**
	 * Imports given mass data configurations.
	 *
	 * @throws AoException
	 *             Thrown if the execution fails.
	 * @throws DataAccessException
	 *             Thrown if the execution fails.
	 * @throws IOException
	 *             Thrown if a file transfer operation fails.
	 */
	public void execute() throws AoException, DataAccessException, IOException {
		insertStatement.executeWithCores(cores);
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Reads given {@link WriteRequest} and prepares a corresponding
	 * {@link Core} for import.
	 *
	 * @param writeRequest
	 *            The mass data configuration.
	 * @return The created {@code Core} is returned.
	 */
	private Core createCore(WriteRequest writeRequest) {
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

		if (writeRequest.hasValues()) {
			ValueType<?> valueType = writeRequest.getRawScalarType().toValueType();
			String unitName = writeRequest.getChannel().getUnit().getName();
			values.put(AE_LC_ATTR_VALUES,
					valueType.create(AE_LC_ATTR_VALUES, unitName, true, writeRequest.getValues()));

			if (writeRequest.getSequenceRepresentation().isImplicit()) {
				// PEAK ODS server: expects values written as generation
				// parameters
				Object genParamValues = writeRequest.getValues();
				double[] genParamD = new double[Array.getLength(genParamValues)];
				IntStream.range(0, genParamD.length)
						.forEach(i -> genParamD[i] = ((Number) Array.get(genParamValues, i)).doubleValue());
				values.get(AE_LC_ATTR_PARAMETERS).set(genParamD);
			}

			// flags
			if (writeRequest.areAllValid()) {
				values.get(AE_LC_ATTR_GLOBAL_FLAG).set((short) 15);
				// PEAK ODS server issue: though global flag is true a flags
				// array is expected
				short[] flags = new short[Array.getLength(writeRequest.getValues())];
				Arrays.fill(flags, (short) 15);
				values.get(AE_LC_ATTR_FLAGS).set(flags);
			} else {
				short[] flags = ODSConverter.toODSValidFlagSeq(writeRequest.getFlags());
				values.get(AE_LC_ATTR_FLAGS).set(flags);
			}
		} else if (writeRequest.hasExternalComponents()) {
			// TODO
			throw new UnsupportedOperationException("NOT YET IMPLEMENTED.");
		} else {
			throw new IllegalStateException("Given write request neither has measured values nor external components");
		}

		return core;
	}

}
