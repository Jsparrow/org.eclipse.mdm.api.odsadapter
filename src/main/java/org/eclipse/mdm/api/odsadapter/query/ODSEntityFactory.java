/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import org.eclipse.mdm.api.base.model.BaseEntityFactory;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.MimeType;
import org.eclipse.mdm.api.base.query.DefaultEntityCore;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSEntityFactory extends BaseEntityFactory {

	private final ModelManager modelManager;

	public ODSEntityFactory(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	@Override
	protected EntityCore createCore(Class<? extends Entity> type) {
		return new DefaultEntityCore(modelManager.getEntityType(type));
	}

	@Override
	protected MimeType getDefaultMimeType(Class<? extends Entity> type) {
		// TODO this is incomplete, we have to build custom ones for descriptive entity types
		return new MimeType(ODSUtils.DEFAULT_MIMETYPES.get(type.getSimpleName()));
	}

}
