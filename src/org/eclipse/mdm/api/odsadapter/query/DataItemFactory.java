/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Result;

@Deprecated
public interface DataItemFactory {

	boolean isCached(Class<? extends DataItem> entity);

	@Deprecated
	<T extends DataItem> T createDataItem(Class<T> type, Result result) throws DataAccessException;

}