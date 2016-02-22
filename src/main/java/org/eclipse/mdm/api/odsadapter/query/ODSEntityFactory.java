/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.List;

import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.MimeType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.factory.BaseEntityFactory;
import org.eclipse.mdm.api.base.model.factory.EntityCore;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSEntityFactory extends BaseEntityFactory {

	private final ODSTransaction transaction;

	public ODSEntityFactory(ODSTransaction transaction) {
		this.transaction = transaction;
	}

	@Override
	protected Core createCore(Class<? extends DataItem> type) {
		return new EntityCore(transaction.getModelManager().getEntityType(type));
	}

	@Override
	protected MimeType getDefaultMimeType(Class<? extends DataItem> type) {
		// TODO this is incomplete, we have to build custom ones for descriptive entity types
		return new MimeType(ODSUtils.DEFAULT_MIMETYPES.get(type.getSimpleName()));
	}

	@Override
	protected <T extends DataItem> T create(T dataItem, DataItem... implicitlyRelated) throws DataAccessException {
		EntityType entityType = transaction.getModelManager().getEntityType(dataItem.getClass());
		InsertStatement insertStatement = new InsertStatement(transaction, entityType);

		insertStatement.add(dataItem.getCore(), implicitlyRelated);
		List<Long> ids = insertStatement.execute();
		dataItem.getCore().setURI(new URI(entityType.getSourceName(), dataItem.getCore().getTypeName(), ids.get(0)));

		return dataItem;
	}

}
