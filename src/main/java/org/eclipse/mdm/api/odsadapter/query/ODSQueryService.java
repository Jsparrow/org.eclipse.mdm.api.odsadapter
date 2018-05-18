/*
 * Copyright (c) 2017-2018 Peak Solution GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

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
