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
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.MimeType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.factory.EntityFactory;
import org.eclipse.mdm.api.base.model.factory.EntityCore;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSEntityFactory extends EntityFactory {

	private final ODSTransaction transaction;

	public ODSEntityFactory(ODSTransaction transaction) {
		this.transaction = transaction;
	}

	@Override
	protected Core createCore(Class<? extends Entity> type) {
		return new EntityCore(transaction.getModelManager().getEntityType(type));
	}

	@Override
	protected MimeType getDefaultMimeType(Class<? extends Entity> type) {
		// TODO this is incomplete, we have to build custom ones for descriptive entity types
		return new MimeType(ODSUtils.DEFAULT_MIMETYPES.get(type.getSimpleName()));
	}

	@Override
	protected <T extends Entity> T create(T entity, Entity... implicitlyRelated) throws DataAccessException {
		EntityType entityType = transaction.getModelManager().getEntityType(entity.getClass());
		InsertStatement insertStatement = new InsertStatement(transaction, entityType);

		insertStatement.add(entity.getCore(), implicitlyRelated);
		List<Long> ids = insertStatement.execute();
		entity.getCore().setURI(new URI(entityType.getSourceName(), entity.getCore().getTypeName(), ids.get(0)));

		return entity;
	}

}
