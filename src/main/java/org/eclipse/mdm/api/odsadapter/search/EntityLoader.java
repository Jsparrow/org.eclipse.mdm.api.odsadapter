/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.Collection;
import java.util.List;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;

public interface EntityLoader {

	// ======================================================================
	// Public methods
	// ======================================================================

	<T extends Entity> List<T> loadAll(Class<T> type, Collection<Long> instanceIDs) throws DataAccessException;

}
