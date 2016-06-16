/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

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

final class ReadRequestHandler {

	private final ODSModelManager modelManager;

	public ReadRequestHandler(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public List<MeasuredValues> execute(ReadRequest readRequest) throws DataAccessException {
		ValueMatrix vm = null;
		Column[] columns = null;

		try {
			vm = getODSValueMatrix(readRequest);
			columns = getODSColumns(readRequest, vm);
			NameValueSeqUnit[] nvsus = vm.getValue(columns, readRequest.getStartIndex(), readRequest.getRequestSize());
			return ODSConverter.fromODSMeasuredValuesSeq(nvsus);
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			unremoteColumns(columns);
			unremoteValueMatrix(vm);
		}
	}

	private Column[] getODSColumns(ReadRequest readRequest, ValueMatrix vm) throws AoException, DataAccessException {
		if(readRequest.isLoadAllChannels()) {
			// TODO should it be possible to overwrite the unit of some channels?!
			// -> this results in a performance issue since we need to call getName()
			// on each column for mapping!
			return vm.getColumns("*");
		}

		List<Column> columnList = new ArrayList<>();
		try {
			for(Entry<Channel, Unit> entry : readRequest.getChannels().entrySet()) {
				Channel channel = entry.getKey();
				Unit unit = entry.getValue();
				Column column = uniqueColumn(channel.getName(), vm.getColumns(channel.getName()));
				if(!unit.nameMatches(channel.getUnit().getName())) {
					column.setUnit(unit.getName());
				}
				columnList.add(column);
			}
			return columnList.toArray(new Column[columnList.size()]);
		} catch(AoException e) {
			unremoteColumns(columnList.toArray(new Column[columnList.size()]));
			// TODO logging
			throw new DataAccessException("Unable to load column due to: " + e.reason, e);
		}
	}

	private ValueMatrix getODSValueMatrix(ReadRequest readRequest) throws AoException, DataAccessException {
		Entity entity = readRequest.getChannelGroup();
		T_LONGLONG iid = ODSConverter.toODSLong(entity.getID());
		T_LONGLONG aid  = ((ODSEntityType) modelManager.getEntityType(entity)).getODSID();
		return modelManager.getApplElemAccess().getValueMatrixInMode(new ElemId(aid, iid), ValueMatrixMode.CALCULATED);
	}


	private Column uniqueColumn(String columnName, Column[] columns) throws DataAccessException {
		if(columns.length <= 0) {
			throw new DataAccessException("no column with name '" + columnName
					+ "' found at generated ValueMatrix (expected 1)!");
		}

		if(columns.length > 1) {
			unremoteColumns(columns);
			throw new DataAccessException("mulitple columns with name '" + columnName
					+ "' found at generated ValueMatrix (expected 1)!");
		}

		return columns[0];
	}

	private void unremoteValueMatrix(ValueMatrix vm) throws DataAccessException {
		if(vm == null) {
			return;
		}

		try {
			vm.destroy();
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			vm._release();
		}
	}

	private void unremoteColumns(Column[] columns) throws DataAccessException  {
		if(columns == null) {
			return;
		}

		for (Column column : columns) {
			try {
				column.destroy();
			} catch(AoException e) {
				// TODO logging
			} finally {
				column._release();
			}
		}
	}

}

