/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

abstract class BaseStatement {

	private static final Method GET_CORE_METHOD;

	static {
		try {
			GET_CORE_METHOD = BaseEntity.class.getDeclaredMethod("getCore");
			GET_CORE_METHOD.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException("Unable to load 'getCore()' in class '" + BaseEntity.class.getSimpleName() + "'.", e);
		}
	}

	private final ODSTransaction transaction;
	private final ODSEntityType entityType;

	protected BaseStatement(ODSTransaction transaction, EntityType entityType) {
		this.transaction = transaction;
		this.entityType = (ODSEntityType) entityType;
	}

	public abstract void execute(Collection<Entity> entities) throws AoException, DataAccessException;

	protected Core extract(Entity entity) {
		try {
			return (Core) GET_CORE_METHOD.invoke(entity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException("Given entity of type '" + entity.getClass().getSimpleName() +
					"' does not extend '" + BaseEntity.class.getName() + "'", e);
		}
	}

	protected ODSTransaction getTransaction() {
		return transaction;
	}

	protected ODSModelManager getModelManager() {
		return transaction.getModelManager();
	}

	protected ApplElemAccess getApplElemAccess() throws AoException {
		return transaction.getModelManager().getApplElemAccess();
	}

	protected ODSEntityType getEntityType() {
		return entityType;
	}

}
