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


package org.eclipse.mdm.api.odsadapter.query;

import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;

public class ODSQueryService implements QueryService {

	private ODSModelManager modelManager;
	
	public ODSQueryService(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query createQuery() {
		return new ODSQuery(modelManager.getApplElemAccess());
	}
}
