/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_ENUMERATION_NAME;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SCALAR_TYPE;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SEQUENCE;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.mdm.api.base.adapter.DefaultCore;
import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EnumRegistry;
import org.eclipse.mdm.api.base.model.Enumeration;
import org.eclipse.mdm.api.base.model.EnumerationValue;
import org.eclipse.mdm.api.base.model.Interpolation;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.SequenceRepresentation;
import org.eclipse.mdm.api.base.model.TypeSpecification;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.model.VersionState;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;

/**
 * ODS implementation of the {@link EntityFactory}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class ODSEntityFactory extends EntityFactory {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Set<Class<? extends EnumerationValue>> ENUM_CLASSES = new HashSet<>();

	static {
		ENUM_CLASSES.add(ScalarType.class);
		ENUM_CLASSES.add(VersionState.class);
		ENUM_CLASSES.add(Interpolation.class);
		ENUM_CLASSES.add(AxisType.class);
		ENUM_CLASSES.add(SequenceRepresentation.class);
		ENUM_CLASSES.add(TypeSpecification.class);
	}

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSModelManager modelManager;
	private final User loggedInUser;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to create {@link Core}s.
	 * @param loggedInUser
	 *            The logged in {@link User}.
	 */
	public ODSEntityFactory(ODSModelManager modelManager, User loggedInUser) {
		this.modelManager = modelManager;
		this.loggedInUser = loggedInUser;
	}

	// ======================================================================
	// Protected methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Optional<User> getLoggedInUser() {
		return Optional.of(loggedInUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass) {
		return createCore(new Key<>(entityClass));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <T extends Entity> Core createCore(Class<T> entityClass, ContextType contextType) {
		return createCore(new Key<>(entityClass, contextType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <T extends Entity> Core createCore(String name, Class<T> entityClass) {
		EntityConfig<?> entityConfig = modelManager.getEntityConfig(modelManager.getEntityType(name));
		if (!entityClass.equals(entityConfig.getEntityClass())) {
			throw new IllegalArgumentException("Incompatible entity class expected '" + entityClass.getName()
					+ "' but got '" + entityConfig.getEntityClass().getName() + "'");
		}
		Core core = new DefaultCore(entityConfig.getEntityType());
		core.getValues().get(Entity.ATTR_MIMETYPE).set(entityConfig.getMimeType());

		return core;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateEnum(Enumeration<?> enumerationObj) {
		EnumRegistry er = EnumRegistry.getInstance();
		// check if enum is properly registered
        if (er.get(enumerationObj.getName())==null) {
		  throw new IllegalArgumentException("Given enum class '" + enumerationObj.getName() + "' is not supported.");
        }
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Creates a configured {@link Core} for given {@link Key}.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used as identifier to resolve the {@link EntityConfig}.
	 * @return The created {@code Core} is returned.
	 */
	private <T extends Entity> Core createCore(Key<T> key) {
		EntityConfig<T> entityConfig = modelManager.findEntityConfig(key);
		Core core = new DefaultCore(entityConfig.getEntityType());
		core.getValues().get(Entity.ATTR_MIMETYPE).set(entityConfig.getMimeType());

		if (CatalogAttribute.class.equals(entityConfig.getEntityClass())) {
			core.getValues().put(VATTR_ENUMERATION_NAME, ValueType.STRING.create(VATTR_ENUMERATION_NAME));
			core.getValues().put(VATTR_SCALAR_TYPE, ValueType.ENUMERATION.create(EnumRegistry.getInstance().get("ScalarType"), VATTR_SCALAR_TYPE));
			core.getValues().put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE));
		}

		return core;
	}
}
