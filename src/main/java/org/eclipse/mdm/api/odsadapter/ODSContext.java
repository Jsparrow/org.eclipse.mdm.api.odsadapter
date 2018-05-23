/*
 * Copyright (c) 2017 Peak Solution GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.mdm.api.odsadapter;

import com.highqsoft.corbafileserver.generated.CORBAFileServerIF;
import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.file.FileService;
import org.eclipse.mdm.api.base.notification.NotificationService;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.search.SearchService;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService;
import org.eclipse.mdm.api.odsadapter.filetransfer.Transfer;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.notification.ODSNotificationServiceFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.query.ODSQueryService;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * ODSContext encapsulates a session to the ASAM ODS CORBA API and 
 * provides the ODS specific services implementations.
 *
 * @since 1.0.0
 */
public class ODSContext implements ApplicationContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSContext.class);
	
	private Map<String, String> parameters;
	private final Transfer transfer = Transfer.SOCKET;
	
	private CORBAFileServerIF fileServer;
	private ODSModelManager modelManager;
	private ODSQueryService queryService;
	private EntityLoader entityLoader;
	private ODSEntityManager entityManager;
	private ODSSearchService searchService;
	
	/**
	 * Creates a new ODS application context.
	 * @param orb the CORBA ORB used to connect to the ODS API
	 * @param aoSession
	 * @param fileServer
	 * @param parameters
	 * @throws AoException
	 */
	public ODSContext(ORB orb, AoSession aoSession, CORBAFileServerIF fileServer, Map<String, String> parameters) throws AoException {
		this.fileServer = fileServer;
		this.parameters = parameters;
		
		this.modelManager = new ODSModelManager(orb, aoSession);
		this.queryService = new ODSQueryService(this.modelManager);
		this.entityManager = new ODSEntityManager(this);
		this.entityLoader = new EntityLoader(modelManager, queryService);
		this.searchService = new ODSSearchService(this, queryService, entityLoader);
		LOGGER.debug("ODSContext initialized.");
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<EntityManager> getEntityManager() {
		return Optional.of(entityManager);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<EntityFactory> getEntityFactory() {
		try {
			return Optional.of(new ODSEntityFactory(modelManager, entityManager.loadLoggedOnUser().get()));
		} catch (DataAccessException e) {
			throw new IllegalStateException("Unable to load instance of the logged in user.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<ModelManager> getModelManager() {
		return Optional.of(modelManager);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<QueryService> getQueryService() {
		// TODO
		// java docs: cache this service for ONE request!
		return Optional.of(new ODSQueryService(modelManager));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<SearchService> getSearchService() {
		return Optional.of(searchService);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<FileService> getFileService() {
		if (fileServer == null) {
			return Optional.empty();
		}
		return Optional.of(new CORBAFileService(this, transfer));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<NotificationService> getNotificationService() {
		try {
			return Optional.of(new ODSNotificationServiceFactory().create(this, parameters));
		} catch (ConnectionException e) {
			throw new IllegalStateException("Unable to create notification manager.", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * @returns the string "ods"
	 */
	@Override
	public String getAdapterType() {
		return "ods";
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			modelManager.close();
		} catch (AoException e) {
			LOGGER.warn("Unable to close sesssion due to: " + e.reason, e);
		}
	}
	
	/**
	 * Returns the {@link ODSModelManager} used for this context.
	 * @return the {@link ODSModelManager}
	 */
	public ODSModelManager getODSModelManager() {
		return modelManager;
	}

	/**
	 * Returns the {@link AoSession} used for this context.
	 * @return {@link AoSession} used for this context.
	 */
	public AoSession getAoSession() {
		return modelManager.getAoSession();
	}

	/**
	 * Returns the ORB used for this context
	 * @return ORB used for this context
	 */
	public ORB getORB() {
		return modelManager.getORB();
	}

	/**
	 * Returns the {@link CORBAFileServerIF}.
	 *
	 * @return The {@code CORBAFileServerIF} is returned or null, if missing.
	 */
	public CORBAFileServerIF getFileServer() {
		return fileServer;
	}

	/**
	 * Returns a new {@link ODSContext} with a new ODS co-session.
	 *
	 * @return The created {@code ODSContext} is returned.
	 * @throws AoException
	 *             Thrown on errors.
	 */
	public ODSContext newContext() throws AoException {
		return new ODSContext(modelManager.getORB(), getAoSession().createCoSession(), fileServer, parameters);
	}
}
