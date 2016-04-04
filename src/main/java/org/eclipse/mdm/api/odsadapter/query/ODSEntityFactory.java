/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_ENUMERATION_CLASS;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SCALAR_TYPE;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SEQUENCE;

import java.util.Locale;
import java.util.Map;

import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.MimeType;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DefaultEntityCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.dflt.model.DefaultEntityFactory;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public final class ODSEntityFactory extends DefaultEntityFactory {

	private final ModelManager modelManager;

	public ODSEntityFactory(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	@Override
	protected EntityCore createCore(Class<? extends Entity> type) {
		return createCore(modelManager.getEntityType(type));
	}

	@Override
	protected EntityCore createCatalogComponentCore(ContextType contextType) {
		return createCore(modelManager.getEntityType("Cat" + ODSUtils.CONTEXTTYPES.convert(contextType) + "Comp"));
	}

	@Override
	protected EntityCore createCatalogAttributeCore(ContextType contextType, boolean forComponent) {
		EntityCore entityCore;
		if(forComponent) {
			entityCore = createCore(modelManager.getEntityType("Cat" + ODSUtils.CONTEXTTYPES.convert(contextType) + "Attr"));
		} else {
			entityCore = createCore(modelManager.getEntityType("CatSensorAttr"));
		}

		Map<String, Value> values = entityCore.getValues();
		values.put(VATTR_ENUMERATION_CLASS, ValueType.STRING.create(VATTR_ENUMERATION_CLASS));
		values.put(VATTR_SCALAR_TYPE, ValueType.ENUMERATION.create(ScalarType.class,VATTR_SCALAR_TYPE));
		values.put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE));

		return entityCore;
	}

	@Override
	protected MimeType getDefaultMimeType(Class<? extends Entity> type) {
		// TODO this is incomplete, we have to build custom ones for descriptive entity types
		return new MimeType(ODSUtils.DEFAULT_MIMETYPES.get(type.getSimpleName()));
	}

	@Override
	protected MimeType createCatalogMimeType(EntityCore entityCore) {
		return new MimeType("application/x-asam.aoany." + entityCore.getURI().getTypeName().toLowerCase(Locale.ROOT));
	}

	private EntityCore createCore(EntityType entityType) {
		return new DefaultEntityCore(entityType);
	}

}
