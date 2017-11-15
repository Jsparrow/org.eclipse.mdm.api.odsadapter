/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.io.IOException;
import java.util.Collection;

import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

/**
 * A base implementation for execution statements (CREATE, UPDATE, DELETE).
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
abstract class BaseStatement {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSTransaction transaction;
	private final ODSEntityType entityType;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param transaction
	 *            The owning {@link ODSTransaction}.
	 * @param entityType
	 *            The associated {@link EntityType}.
	 */
	protected BaseStatement(ODSTransaction transaction, EntityType entityType) {
		this.transaction = transaction;
		this.entityType = (ODSEntityType) entityType;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Executes this statement for given {@link Entity}s.
	 *
	 * @param entities
	 *            The processed {@code Entity}s.
	 * @throws AoException
	 *             Thrown if the execution fails.
	 * @throws DataAccessException
	 *             Thrown if the execution fails.
	 * @throws IOException
	 *             Thrown if a file transfer operation fails.
	 */
	public abstract void execute(Collection<Entity> entities) throws AoException, DataAccessException, IOException;

	// ======================================================================
	// Protected methods
	// ======================================================================

	/**
	 * Returns the {@link ODSTransaction}.
	 *
	 * @return The {@code ODSTransaction} is returned.
	 */
	protected ODSTransaction getTransaction() {
		return transaction;
	}

	/**
	 * Returns the {@link ODSModelManager}.
	 *
	 * @return The {@code ODSModelManager} is returned.
	 */
	protected ODSModelManager getModelManager() {
		return transaction.getModelManager();
	}

	/**
	 * Returns the {@link QueryService}.
	 *
	 * @return The {@code QueryService} is returned.
	 */
	protected QueryService getQueryService() {
		return transaction.getContext().getQueryService()
				.orElseThrow(() -> new ServiceNotProvidedException(QueryService.class));
	}
	
	/**
	 * Returns the {@link ApplElemAccess}.
	 *
	 * @return The {@code ApplElemAccess} is returned.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	protected ApplElemAccess getApplElemAccess() throws AoException {
		return transaction.getContext().getODSModelManager().getApplElemAccess();
	}


	
	/**
	 * Returns the associated {@link EntityType}.
	 *
	 * @return The associated {@code EntityType} is returned.
	 */
	protected ODSEntityType getEntityType() {
		return entityType;
	}

}
