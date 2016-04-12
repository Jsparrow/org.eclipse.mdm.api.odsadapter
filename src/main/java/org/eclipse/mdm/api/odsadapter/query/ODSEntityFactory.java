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
	protected EntityCore createRootCore(ContextType contextType, boolean forTemplate) {
		if(forTemplate) {
			return createCore("Tpl" + ODSUtils.CONTEXTTYPES.convert(contextType) + "Root");
		} else {
			return createCore(ODSUtils.CONTEXTTYPES.convert(contextType));
		}
	}

	@Override
	protected EntityCore createComponentCore(ContextType contextType, boolean forTemplate) {
		if(forTemplate) {
			return createCore("Tpl" + ODSUtils.CONTEXTTYPES.convert(contextType) + "Comp");
		} else {
			return createCore("Cat" + ODSUtils.CONTEXTTYPES.convert(contextType) + "Comp");
		}
	}

	@Override
	protected EntityCore createSensorCore(boolean forTemplate) {
		if(forTemplate) {
			return createCore("TplSensor");
		} else {
			return createCore("CatSensor");
		}
	}

	@Override
	protected EntityCore createAttributeCore(ContextType contextType, boolean forTemplate) {
		StringBuilder entityTypeName = new StringBuilder(forTemplate ? "Tpl" : "Cat");
		entityTypeName.append(contextType == null ? "Sensor" : ODSUtils.CONTEXTTYPES.convert(contextType));
		EntityCore entityCore = createCore(modelManager.getEntityType(entityTypeName.append("Attr").toString()));

		if(!forTemplate) {
			Map<String, Value> values = entityCore.getValues();
			values.put(VATTR_ENUMERATION_CLASS, ValueType.STRING.create(VATTR_ENUMERATION_CLASS));
			values.put(VATTR_SCALAR_TYPE, ValueType.ENUMERATION.create(ScalarType.class,VATTR_SCALAR_TYPE));
			values.put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE));
		}

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

	@Override
	protected MimeType createTemplateMimeType(EntityCore entityCore) {
		return new MimeType("application/x-asam.aoany." + entityCore.getURI().getTypeName().toLowerCase(Locale.ROOT));
	}

	private EntityCore createCore(String entityTypeName) {
		return createCore(modelManager.getEntityType(entityTypeName));
	}

	@Deprecated
	private EntityCore createCore(EntityType entityType) {
		return new DefaultEntityCore(entityType);
	}

}
