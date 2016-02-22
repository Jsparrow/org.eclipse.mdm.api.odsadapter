/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.massdata;


import java.util.ArrayList;
import java.util.List;

import org.asam.ods.AoException;
import org.asam.ods.Column;
import org.asam.ods.ElemId;
import org.asam.ods.NameValueSeqUnit;
import org.asam.ods.T_LONGLONG;
import org.asam.ods.ValueMatrix;
import org.asam.ods.ValueMatrixMode;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public final class ReadRequestHandler {

	private final ODSModelManager modelManager;

	public ReadRequestHandler(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public List<MeasuredValues> execute(ReadRequest readRequest) throws DataAccessException {
		ValueMatrix vm = null;
		Column[] columns = null;

		try {
			vm = getODSValueMatrix(readRequest);
			if(readRequest.getNumberOfValues() == 0) {
				readRequest.setNumberOfValues(vm.getRowCount());
			}

			columns = getODSColumns(readRequest.getChannels(), vm);
			NameValueSeqUnit[] nvsus = vm.getValue(columns, readRequest.getStartIndex(), readRequest.getRequestSize());
			return ODSConverter.fromODSMeasuredValuesSeq(nvsus);
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			unremoteColumns(columns);
			unremoteValueMatrix(vm);
		}
	}

	private Column[] getODSColumns(List<Channel> channels, ValueMatrix vm) throws AoException, DataAccessException {
		if(channels.isEmpty()) {
			return vm.getColumns("*");
		}

		List<Column> columnList = new ArrayList<>();
		for(Channel channel : channels) {
			columnList.add(uniqueColumn(channel.getName(), vm.getColumns(channel.getName())));
		}
		return columnList.toArray(new Column[columnList.size()]);
	}

	private ValueMatrix getODSValueMatrix(ReadRequest readRequest) throws AoException, DataAccessException {
		DataItem dataItem = readRequest.getChannelGroup();
		T_LONGLONG iid = ODSConverter.toODSLong(dataItem.getURI().getID());
		T_LONGLONG aid  = ((ODSEntityType) modelManager.getEntityType(dataItem)).getODSID();
		return modelManager.getApplElemAccess().getValueMatrixInMode(new ElemId(aid, iid), ValueMatrixMode.CALCULATED);
	}


	private Column uniqueColumn(String columnName, Column[] columns) throws DataAccessException {
		if(columns.length <= 0) {
			throw new DataAccessException("no column with name '" + columnName
					+ "' found at generated ValueMatrix (expected 1)!");
		}

		if(columns.length > 1) {
			throw new DataAccessException("mulitple columns with name '" + columnName
					+ "' found at generated ValueMatrix (expected 1)!");
		}

		return columns[0];
	}

	private void unremoteValueMatrix(ValueMatrix vm) throws DataAccessException {
		try {
			if(vm != null) {
				vm.destroy();
				vm._release();
			}
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	private void unremoteColumns(Column[] columns) throws DataAccessException  {
		if(columns == null) {
			return;
		}

		for(int i = 0; i < columns.length; i++) {
			try {
				columns[i].destroy();
				columns[i]._release();
				columns[i] = null;
			} catch(AoException aoe) {
				// TODO log instead of throwing
				throw new DataAccessException(aoe.reason, aoe);
			}
		}
	}

}

