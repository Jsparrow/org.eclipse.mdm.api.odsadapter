/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;


import java.util.ArrayList;
import java.util.List;

import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.Column;
import org.asam.ods.ElemId;
import org.asam.ods.NameValueSeqUnit;
import org.asam.ods.T_LONGLONG;
import org.asam.ods.ValueMatrix;
import org.asam.ods.ValueMatrixMode;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelValue;
import org.eclipse.mdm.api.base.model.ChannelValuesReadRequest;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ChannelReadRequest {

	private final ApplElemAccess applElemAccess;
	private final QueryService queryService;

	public ChannelReadRequest(ApplElemAccess applElemAccess, QueryService queryService) {
		this.applElemAccess = applElemAccess;
		this.queryService = queryService;
	}

	public List<ChannelValue> send(ChannelValuesReadRequest readRequest) throws DataAccessException {
		ValueMatrix vm = null;
		Column[] columns = null;

		try {
			vm = getODSValueMatrix(readRequest, ODSUtils.MATRIXMODES.convert(readRequest.getMatrixMode()));
			if(readRequest.getNumberOfValues() == 0) {
				readRequest.setNumberOfValues(vm.getRowCount());
			}

			columns = getODSColumns(readRequest.getChannels(), vm);
			NameValueSeqUnit[] nvsus = vm.getValue(columns, readRequest.getStartIndex(), readRequest.getRequestSize());

			List<ChannelValue> channelValues = new ArrayList<>();
			for (NameValueSeqUnit nvsu : nvsus) {
				Value values = ODSConverter.fromODSValueSeq(nvsu.valName, nvsu.unit, nvsu.value);
				boolean[] flags = shortFlags2booleanFlags(nvsu.value.flag);
				channelValues.add(new ChannelValue(values, flags));
			}

			return channelValues;
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

		List<Column> columnList = new ArrayList<Column>();
		for(Channel channel : channels) {
			columnList.add(uniqueColumn(channel.getName(), vm.getColumns(channel.getName())));
		}
		return columnList.toArray(new Column[columnList.size()]);
	}

	private ValueMatrix getODSValueMatrix(ChannelValuesReadRequest readRequest, ValueMatrixMode vmMode) throws AoException, DataAccessException {
		DataItem dataItem = readRequest.getMatrixSource();
		if(dataItem != null) {
			T_LONGLONG iid = ODSConverter.toODSLong(dataItem.getURI().getID());
			T_LONGLONG aid  = ((ODSEntity)queryService.getEntity(dataItem.getURI().getTypeName())).getODSID();
			return applElemAccess.getValueMatrixInMode(new ElemId(aid, iid), vmMode);
		}

		throw new DataAccessException("unable to create ODS ValueMatrix");
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

	private boolean[] shortFlags2booleanFlags(short[] flags) {
		boolean[] booleanFlags = new boolean[flags.length];
		for(int i=0; i< booleanFlags.length; i++) {
			booleanFlags[i] = flags[i] == 15;
		}
		return booleanFlags;
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
		try {
			if(columns != null) {
				for(int i=0; i<columns.length; i++) {
					columns[i].destroy();
					columns[i]._release();
					columns[i] = null;
				}
			}
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

}

