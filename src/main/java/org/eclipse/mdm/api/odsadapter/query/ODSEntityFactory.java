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

import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DefaultCore;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.DefaultEntityFactory;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

public final class ODSEntityFactory extends DefaultEntityFactory {

	private final ODSModelManager modelManager;

	public ODSEntityFactory(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}

	//	@Override
	//	public Test createTest(String name, User responsiblePerson) {
	//		throw new UnsupportedOperationException("Test requires a status."); // TODO this is ugly, isn't it?
	//	}
	//
	//	@Override
	//	public TestStep createTestStep(String name, Test test) {
	//		throw new UnsupportedOperationException("Test step requires a status."); // TODO this is ugly, isn't it?
	//	}

	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass) {
		return createCore(new Key<>(entityClass));
	}

	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass, ContextType contextType) {
		return createCore(new Key<>(entityClass, contextType));
	}

	private <T extends Entity> Core createCore(Key<T> key) {
		EntityConfig<T> entityConfig = modelManager.findEntityConfig(key);
		Core core = new DefaultCore(entityConfig.getEntityType());
		core.getValues().get(Entity.ATTR_MIMETYPE).set(entityConfig.getMimeType());

		if(CatalogAttribute.class.equals(entityConfig.getEntityClass())) {
			core.getValues().put(VATTR_ENUMERATION_CLASS, ValueType.STRING.create(VATTR_ENUMERATION_CLASS));
			core.getValues().put(VATTR_SCALAR_TYPE, ValueType.ENUMERATION.create(ScalarType.class, VATTR_SCALAR_TYPE));
			core.getValues().put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE));
		}

		return core;
	}

}
