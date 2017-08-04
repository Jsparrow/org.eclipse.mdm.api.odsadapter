/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.InstanceElement;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.FileService;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.odsadapter.filetransfer.CORBAFileService;
import org.eclipse.mdm.api.odsadapter.filetransfer.Transfer;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.transaction.ODSTransaction;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ASAM ODS implementation of the {@link EntityManager} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class ODSEntityManager implements EntityManager {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSEntityManager.class);

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSModelManager modelManager;
	private final EntityLoader entityLoader;

	private final Transfer transfer = Transfer.SOCKET;
	private String esHost;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            The {@link ODSModelManager}.
	 */
	public ODSEntityManager(ODSModelManager modelManager, String esHost) {
		this.modelManager = modelManager;
		this.esHost = esHost;
		entityLoader = new EntityLoader(modelManager);
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<EntityFactory> getEntityFactory() {
		try {
			return Optional.of(new ODSEntityFactory(modelManager, loadLoggedOnUser().get()));
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
	public Optional<SearchService> getSearchService() {
		// TODO
		// java docs: cache this service for ONE request!
		return Optional.of(new ODSSearchService(modelManager, entityLoader, esHost));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<FileService> getFileService() {
		if (modelManager.getFileServer() == null) {
			return Optional.empty();
		}
		return Optional.of(new CORBAFileService(modelManager, transfer));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Environment loadEnvironment() throws DataAccessException {
		List<Environment> environments = loadAll(Environment.class);
		if (environments.size() != 1) {
			throw new DataAccessException("Unable to laod the environment entity.");
		}

		return environments.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> loadLoggedOnUser() throws DataAccessException {
		InstanceElement ieUser = null;
		try {
			ieUser = modelManager.getAoSession().getUser();
			return Optional.of(
					entityLoader.load(new Key<>(User.class), Long.toString(ODSConverter.fromODSLong(ieUser.getId()))));
		} catch (AoException e) {
			throw new DataAccessException("Unable to load the logged in user entity due to: " + e.reason, e);
		} finally {
			try {
				if (ieUser != null) {
					ieUser.destroy();
					ieUser._release();
				}
			} catch (AoException aoe) {
				LOGGER.warn("Unable to destroy the CORBA resource due to: " + aoe.reason, aoe);
				ieUser._release();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> load(Class<T> entityClass, Collection<String> instanceIDs) throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> load(Class<T> entityClass, ContextType contextType, Collection<String> instanceIDs)
			throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass, contextType), instanceIDs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> Optional<T> loadParent(Entity child, Class<T> entityClass) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(entityClass);
		EntityType childEntityType = modelManager.getEntityType(child);
		Query query = modelManager.createQuery().selectID(parentEntityType);

		if (child instanceof Channel && ChannelGroup.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local
			// column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(childEntityType, localColumnEntityType).join(localColumnEntityType, parentEntityType);
		} else {
			query.join(childEntityType, parentEntityType);
		}

		Optional<String> instanceID = query.fetchSingleton(Filter.idOnly(childEntityType, child.getID()))
				.map(r -> r.getRecord(parentEntityType)).map(Record::getID);
		if (instanceID.isPresent()) {
			return Optional.of(entityLoader.load(new Key<>(entityClass), instanceID.get()));
		}

		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> loadAll(Class<T> entityClass, String pattern) throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass), pattern);
	}

	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public <T extends StatusAttachable> List<T> loadAll(Class<T> entityClass,
	// Status status, String pattern)
	// throws DataAccessException {
	// EntityType entityType = modelManager.getEntityType(entityClass);
	// EntityType statusEntityType =
	// modelManager.getEntityType(status.getTypeName());
	//
	// List<String> instanceIDs = modelManager.createQuery()
	// .join(entityType, statusEntityType).selectID(entityType)
	// .fetch(Filter.and()
	// .id(statusEntityType, status.getID())
	// .name(entityType, pattern))
	// .stream().map(r ->
	// r.getRecord(entityType)).map(Record::getID).collect(Collectors.toList());
	//
	// return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	// }
	//
	// @Override
	// public List<Status> loadAllStatus(Class<? extends StatusAttachable>
	// entityClass, String pattern)
	// throws DataAccessException {
	// return entityLoader.loadAll(new Key<>(Status.class, entityClass),
	// pattern);
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> loadAll(Class<T> entityClass, ContextType contextType, String pattern)
			throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass, contextType), pattern);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> List<T> loadChildren(Entity parent, Class<T> entityClass, String pattern)
			throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType childEntityType = modelManager.getEntityType(entityClass);
		Query query = modelManager.createQuery();

		if (parent instanceof ChannelGroup && Channel.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local
			// column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(childEntityType, localColumnEntityType).join(localColumnEntityType, parentEntityType);
		} else {
			query.join(childEntityType, parentEntityType);
		}

		List<String> instanceIDs = query.selectID(childEntityType)
				.fetch(Filter.and().id(parentEntityType, parent.getID()).name(childEntityType, pattern)).stream()
				.map(r -> r.getRecord(childEntityType)).map(Record::getID).collect(Collectors.toList());
		return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	}

	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public <T extends StatusAttachable> List<T> loadChildren(Entity parent,
	// Class<T> entityClass, Status status,
	// String pattern) throws DataAccessException {
	// EntityType parentEntityType = modelManager.getEntityType(parent);
	// EntityType childEntityType = modelManager.getEntityType(entityClass);
	// EntityType statusEntityType =
	// modelManager.getEntityType(status.getTypeName());
	//
	// List<String> instanceIDs = modelManager.createQuery()
	// .join(childEntityType, parentEntityType, statusEntityType)
	// .selectID(childEntityType)
	// .fetch(Filter.and()
	// .id(parentEntityType, parent.getID())
	// .id(statusEntityType, status.getID())
	// .name(childEntityType, pattern))
	// .stream().map(r ->
	// r.getRecord(childEntityType)).map(Record::getID).collect(Collectors.toList());
	//
	// return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContextType> loadContextTypes(ContextDescribable contextDescribable) throws DataAccessException {
		EntityType contextDescribableEntityType = modelManager.getEntityType(contextDescribable);
		Query query = modelManager.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for (ContextType contextType : ContextType.values()) {
			EntityType entityType = modelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), Join.OUTER).selectID(entityType);
		}

		Optional<Result> result = query
				.fetchSingleton(Filter.idOnly(contextDescribableEntityType, contextDescribable.getID()));
		if (result.isPresent()) {
			List<ContextType> contextTypes = new ArrayList<>();
			for (Entry<ContextType, EntityType> entry : contextRootEntityTypes.entrySet()) {
				Optional<String> instanceID = result.map(r -> r.getRecord(entry.getValue())).map(Record::getID);
				if (instanceID.isPresent()) {
					contextTypes.add(entry.getKey());
				}
			}

			return contextTypes;
		}

		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<ContextType, ContextRoot> loadContexts(ContextDescribable contextDescribable,
			ContextType... contextTypes) throws DataAccessException {
		EntityType contextDescribableEntityType = modelManager.getEntityType(contextDescribable);
		Query query = modelManager.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for (ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {
			EntityType entityType = modelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), Join.OUTER).selectID(entityType);
		}

		Optional<Result> result = query
				.fetchSingleton(Filter.idOnly(contextDescribableEntityType, contextDescribable.getID()));
		if (result.isPresent()) {
			Map<ContextType, ContextRoot> contextRoots = new EnumMap<>(ContextType.class);
			for (Entry<ContextType, EntityType> entry : contextRootEntityTypes.entrySet()) {
				String instanceID = result.get().getRecord(entry.getValue()).getID();
				if (ODSUtils.isValidID(instanceID)) {
					contextRoots.put(entry.getKey(),
							entityLoader.load(new Key<>(ContextRoot.class, entry.getKey()), instanceID));
				}
			}

			return contextRoots;
		}

		return Collections.emptyMap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MeasuredValues> readMeasuredValues(ReadRequest readRequest) throws DataAccessException {
		return new ReadRequestHandler(modelManager).execute(readRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction startTransaction() throws DataAccessException {
		try {
			return new ODSTransaction(modelManager, loadEnvironment(), transfer);
		} catch (AoException e) {
			throw new DataAccessException("Unable to start transaction due to: " + e.reason, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws ConnectionException {
		try {
			modelManager.close();
		} catch (AoException e) {
			throw new ConnectionException("Unable to close the connection to the data source due to: " + e.reason, e);
		}
	}

}
