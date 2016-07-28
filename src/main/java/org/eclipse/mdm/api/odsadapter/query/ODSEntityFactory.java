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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Interpolation;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.SequenceRepresentation;
import org.eclipse.mdm.api.base.model.TypeSpecification;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.model.VersionState;
import org.eclipse.mdm.api.base.query.DefaultCore;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

public final class ODSEntityFactory extends EntityFactory {

	private static final Set<Class<? extends Enum<?>>> ENUM_CLASSES = new HashSet<>();

	static {
		ENUM_CLASSES.add(ScalarType.class);
		ENUM_CLASSES.add(VersionState.class);
		ENUM_CLASSES.add(Interpolation.class);
		ENUM_CLASSES.add(AxisType.class);
		ENUM_CLASSES.add(SequenceRepresentation.class);
		ENUM_CLASSES.add(TypeSpecification.class);
	}

	private final ODSModelManager modelManager;
	private final User loggedInUser;

	public ODSEntityFactory(ODSModelManager modelManager, User loggedInUser) {
		this.modelManager = modelManager;
		this.loggedInUser = loggedInUser;
	}

	@Override
	protected Optional<User> getLoggedInUser() {
		return Optional.ofNullable(loggedInUser);
	}

	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass) {
		return createCore(new Key<>(entityClass));
	}

	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass, ContextType contextType) {
		return createCore(new Key<>(entityClass, contextType));
	}

	@Override
	protected <T extends Entity> Core createCore(String name, Class<T> entityClass) {
		EntityConfig<?> entityConfig = modelManager.getEntityConfig(modelManager.getEntityType(name));
		if(!entityClass.equals(entityConfig.getEntityClass())) {
			throw new IllegalArgumentException("Incompatible entity class expected '" + entityClass.getName()
			+ "' but got '" + entityConfig.getEntityClass().getName() + "'");
		}
		Core core = new DefaultCore(entityConfig.getEntityType());
		core.getValues().get(Entity.ATTR_MIMETYPE).set(entityConfig.getMimeType());

		return core;
	}

	@Override
	protected void validateEnum(Class<? extends Enum<?>> enumClass) {
		if(ENUM_CLASSES.contains(enumClass)) {
			// given enumeration class is a default one, which is always supported
			return;
		}

		// TODO check here for other enumeration classes introduced by modules

		throw new IllegalArgumentException("Given enum class '" + enumClass.getSimpleName() + "' is not supported.");
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
