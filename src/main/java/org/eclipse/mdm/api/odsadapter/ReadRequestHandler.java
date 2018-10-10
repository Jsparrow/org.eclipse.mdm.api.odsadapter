/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.asam.ods.AoException;
import org.asam.ods.Column;
import org.asam.ods.ElemId;
import org.asam.ods.NameValueSeqUnit;
import org.asam.ods.T_LONGLONG;
import org.asam.ods.ValueMatrix;
import org.asam.ods.ValueMatrixMode;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

/**
 * Reads mass data specified in {@link ReadRequest}s.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class ReadRequestHandler {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSModelManager modelManager;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to gain access to value matrices.
	 */
	public ReadRequestHandler(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Loads {@link MeasuredValues} as defined in given {@link ReadRequest}.
	 *
	 * @param readRequest
	 *            The {@code MeasuredValues} request configuration.
	 * @return The loaded {@code MeasuredValues} are returned.
	 * @throws DataAccessException
	 *             Thrown if unable to load {@code
	 * 		MeasuredValues}.
	 */
	public List<MeasuredValues> execute(ReadRequest readRequest) throws DataAccessException {
		ValueMatrix valueMatrix = null;
		Column[] columns = null;

		try {
			valueMatrix = getValueMatrix(readRequest);
			columns = getODSColumns(readRequest, valueMatrix);
			NameValueSeqUnit[] nvsus = valueMatrix.getValue(columns, readRequest.getStartIndex(),
					readRequest.getRequestSize());
			return ODSConverter.fromODSMeasuredValuesSeq(nvsus);
		} catch (AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			releaseColumns(columns);
			releaseValueMatrix(valueMatrix);
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Loads all for each defined {@link Channel} in given {@link ReadRequest}
	 * and loads the corresponding {@link Column} using given
	 * {@link ValueMatrix}.
	 *
	 * @param readRequest
	 *            Defines required {@code Column}s.
	 * @param valueMatrix
	 *            Used to load required {@code Column}s.
	 * @return {@code Column} configured in given {@code ReadRequest} are
	 *         returned with defined {@link Unit} setup.
	 * @throws AoException
	 *             Throw if unable to load all available {@code Column}s.
	 * @throws DataAccessException
	 *             Thrown on wrong {@code ReadRequest} setup.
	 */
	private Column[] getODSColumns(ReadRequest readRequest, ValueMatrix valueMatrix)
			throws AoException, DataAccessException {
		if (readRequest.isLoadAllChannels()) {
			// TODO should it be possible to overwrite the unit of some
			// channels?!
			// -> this results in a performance issue since we need to call
			// getName()
			// on each column for mapping!
			return valueMatrix.getColumns("*");
		}

		List<Column> columnList = new ArrayList<>();
		try {
			for (Entry<Channel, Unit> entry : readRequest.getChannels().entrySet()) {
				Channel channel = entry.getKey();
				Unit unit = entry.getValue();
				Column[] columns = valueMatrix.getColumns(channel.getName());
				if (columns == null || columns.length != 1) {
					releaseColumns(columns);
					throw new DataAccessException("Column with name '" + channel.getName() + "' not found.");
				}
				Column column = columns[0];
				if (!unit.nameEquals(channel.getUnit().getName())) {
					column.setUnit(unit.getName());
				}
				columnList.add(column);
			}
			return columnList.toArray(new Column[columnList.size()]);
		} catch (AoException e) {
			releaseColumns(columnList.toArray(new Column[columnList.size()]));
			throw new DataAccessException("Unable to load column due to: " + e.reason, e);
		}
	}

	/**
	 * Returns the {@link ValueMatrix} CORBA service object associated with
	 * given {@link ReadRequest}.
	 *
	 * @param readRequest
	 *            The {@code ReadRequest}.
	 * @return The {@code ValueMatrix} is returned.
	 * @throws AoException
	 *             Thrown if unable to load the {@code ValueMatrix}.
	 */
	private ValueMatrix getValueMatrix(ReadRequest readRequest) throws AoException {
		Entity entity = readRequest.getChannelGroup();
		T_LONGLONG iid = ODSConverter.toODSID(entity.getID());
		T_LONGLONG aid = ((ODSEntityType) modelManager.getEntityType(entity)).getODSID();
		return modelManager.getApplElemAccess().getValueMatrixInMode(new ElemId(aid, iid), ValueMatrixMode.CALCULATED);
	}

	/**
	 * Releases given {@link ValueMatrix} CORBA object.
	 *
	 * @param valueMatrix
	 *            Will be released.
	 */
	private void releaseValueMatrix(ValueMatrix valueMatrix) {
		if (valueMatrix == null) {
			return;
		}

		try {
			valueMatrix.destroy();
		} catch (AoException aoe) {
			// ignore
		} finally {
			valueMatrix._release();
		}
	}

	/**
	 * Releases each CORBA {@link Column} object.
	 *
	 * @param columns
	 *            Will be released.
	 */
	private void releaseColumns(Column[] columns) {
		if (columns == null) {
			return;
		}

		for (Column column : columns) {
			try {
				column.destroy();
			} catch (AoException e) {
				// ignore
			} finally {
				column._release();
			}
		}
	}

}
