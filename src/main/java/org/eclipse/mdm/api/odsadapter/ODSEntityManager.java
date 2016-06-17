/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
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
import org.eclipse.mdm.api.base.model.EntityFactory;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.StatusAttachable;
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
import org.eclipse.mdm.api.dflt.model.Status;
import org.eclipse.mdm.api.odsadapter.filetransfer.ODSFileService;
import org.eclipse.mdm.api.odsadapter.filetransfer.ODSFileService.Transfer;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.transaction.ODSTransaction;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

public class ODSEntityManager implements EntityManager {

	private final ODSModelManager modelManager;
	private final EntityLoader entityLoader;

	public ODSEntityManager(ODSModelManager modelManager) throws ConnectionException {
		this.modelManager = modelManager;
		entityLoader = new EntityLoader(modelManager);

		/**
		 * TODO provide context properties from AoSession!
		 */
	}

	@Override
	public Optional<EntityFactory> getEntityFactory() {
		return Optional.of(new ODSEntityFactory(modelManager));
	}

	@Override
	public Optional<ModelManager> getModelManager() {
		return Optional.of(modelManager);
	}

	@Override
	public Optional<SearchService> getSearchService() {
		// TODO
		// java docs: cache this service for ONE request!
		return Optional.of(new ODSSearchService(modelManager, entityLoader));
	}

	@Override
	public Optional<FileService> getFileService() {
		return Optional.of(new ODSFileService(modelManager, Transfer.STREAM));
	}

	@Override
	public Environment loadEnvironment() throws DataAccessException {
		// TODO this should be a default implementation ...
		List<Environment> environments = loadAll(Environment.class);
		if(environments.size() != 1) {
			throw new IllegalStateException(); // TODO
		}

		return environments.get(0);
	}

	@Override // TODO improve!!
	// TODO: rename?!
	public Optional<User> loadLoggedOnUser() throws DataAccessException {
		InstanceElement ieUser = null;
		try {
			ieUser = modelManager.getAoSession().getUser();

			return Optional.of(entityLoader.load(new Key<>(User.class), ODSConverter.fromODSLong(ieUser.getId())));
		} catch(AoException e) {
			throw new DataAccessException(e.reason, e);
		} finally {
			try {
				if(ieUser != null) {
					ieUser.destroy();
				}
			} catch(AoException aoe) {
				throw new DataAccessException(aoe.reason, aoe);
			}
		}
	}

	@Override
	public <T extends Entity> T load(Class<T> entityClass, Long instanceID) throws DataAccessException {
		return entityLoader.load(new Key<>(entityClass), instanceID);
	}

	@Override
	public <T extends Entity> T load(Class<T> entityClass, ContextType contextType, Long instanceID) throws DataAccessException {
		return entityLoader.load(new Key<>(entityClass, contextType), instanceID);
	}

	@Override
	public <T extends Entity> Optional<T> loadParent(Entity child, Class<T> entityClass) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(entityClass);
		EntityType childEntityType = modelManager.getEntityType(child);
		Query query = modelManager.createQuery().selectID(parentEntityType);

		if(child instanceof Channel && ChannelGroup.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(childEntityType, localColumnEntityType).join(localColumnEntityType, parentEntityType);
		} else {
			query.join(childEntityType, parentEntityType);
		}

		Optional<Long> instanceID = query.fetchSingleton(Filter.idOnly(childEntityType, child.getID()))
				.map(r -> r.getRecord(parentEntityType)).map(Record::getID);
		if(instanceID.isPresent()) {
			return Optional.of(entityLoader.load(new Key<>(entityClass), instanceID.get()));
		}

		return Optional.empty();
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> entityClass, String pattern) throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass), pattern);
	}

	@Override
	public <T extends StatusAttachable> List<T> loadAll(Class<T> entityClass, Status status, String pattern) throws DataAccessException {
		EntityType entityType = modelManager.getEntityType(entityClass);
		EntityType statusEntityType = modelManager.getEntityType(status.getTypeName());

		List<Long> instanceIDs = modelManager.createQuery()
				.join(entityType, statusEntityType).selectID(entityType)
				.fetch(Filter.and()
						.id(statusEntityType, status.getID())
						.name(entityType, pattern))
				.stream().map(r -> r.getRecord(entityType)).map(Record::getID).collect(Collectors.toList());

		return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	}

	@Override
	public List<Status> loadAllStatus(Class<? extends StatusAttachable> entityClass, String pattern) throws DataAccessException {
		return entityLoader.loadAll(new Key<>(Status.class, entityClass), pattern);
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> entityClass, ContextType contextType, String pattern) throws DataAccessException {
		return entityLoader.loadAll(new Key<>(entityClass, contextType), pattern);
	}

	@Override
	public <T extends Entity> List<T> loadChildren(Entity parent, Class<T> entityClass, String pattern) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType childEntityType = modelManager.getEntityType(entityClass);
		Query query = modelManager.createQuery();

		if(parent instanceof ChannelGroup && Channel.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(childEntityType, localColumnEntityType).join(localColumnEntityType, parentEntityType);
		} else {
			query.join(childEntityType, parentEntityType);
		}

		List<Long> instanceIDs = query.selectID(childEntityType)
				.fetch(Filter.and()
						.id(parentEntityType, parent.getID())
						.name(childEntityType, pattern))
				.stream().map(r -> r.getRecord(childEntityType)).map(Record::getID).collect(Collectors.toList());
		return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	}

	@Override
	public <T extends StatusAttachable> List<T> loadChildren(Entity parent, Class<T> entityClass, Status status, String pattern) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType childEntityType = modelManager.getEntityType(entityClass);
		EntityType statusEntityType = modelManager.getEntityType(status.getTypeName());

		List<Long> instanceIDs = modelManager.createQuery()
				.join(childEntityType, parentEntityType, statusEntityType)
				.selectID(childEntityType)
				.fetch(Filter.and()
						.id(parentEntityType, parent.getID())
						.id(statusEntityType, status.getID())
						.name(childEntityType, pattern))
				.stream().map(r -> r.getRecord(childEntityType)).map(Record::getID).collect(Collectors.toList());

		return entityLoader.loadAll(new Key<>(entityClass), instanceIDs);
	}

	@Override
	public List<ContextType> loadContextTypes(ContextDescribable contextDescribable) throws DataAccessException {
		EntityType contextDescribableEntityType = modelManager.getEntityType(contextDescribable);
		Query query = modelManager.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for(ContextType contextType : ContextType.values()) {
			EntityType entityType = modelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), Join.OUTER).selectID(entityType);
		}

		Optional<Result> result = query.fetchSingleton(Filter.idOnly(contextDescribableEntityType, contextDescribable.getID()));
		if(result.isPresent()) {
			List<ContextType> contextTypes = new ArrayList<>();
			for(Entry<ContextType, EntityType> entry : contextRootEntityTypes.entrySet()) {
				Optional<Long> instanceID = result.map(r -> r.getRecord(entry.getValue())).map(Record::getID);
				if(instanceID.isPresent()) {
					contextTypes.add(entry.getKey());
				}
			}

			return contextTypes;
		}

		return Collections.emptyList();

	}

	@Override
	public Map<ContextType, ContextRoot> loadContexts(ContextDescribable contextDescribable, ContextType... contextTypes)
			throws DataAccessException {
		EntityType contextDescribableEntityType = modelManager.getEntityType(contextDescribable);
		Query query = modelManager.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for(ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {
			EntityType entityType = modelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), Join.OUTER).selectID(entityType);
		}

		Optional<Result> result = query.fetchSingleton(Filter.idOnly(contextDescribableEntityType, contextDescribable.getID()));
		if(result.isPresent()) {
			Map<ContextType, ContextRoot> contextRoots = new EnumMap<>(ContextType.class);
			for(Entry<ContextType, EntityType> entry : contextRootEntityTypes.entrySet()) {
				Optional<Long> instanceID = result.map(r -> r.getRecord(entry.getValue())).map(Record::getID);
				if(instanceID.isPresent()) {
					contextRoots.put(entry.getKey(), entityLoader.load(new Key<>(ContextRoot.class, entry.getKey()), instanceID.get()));
				}
			}

			return contextRoots;
		}

		return Collections.emptyMap();
	}

	@Override
	public List<MeasuredValues> readMeasuredValues(ReadRequest readRequest) throws DataAccessException {
		return new ReadRequestHandler(modelManager).execute(readRequest);
	}

	@Override
	public Transaction startTransaction() throws DataAccessException {
		try {
			return new ODSTransaction(modelManager);
		} catch (AoException e) {
			throw new DataAccessException("Unable to start transaction due to: " + e.reason, e);
		}
	}

	@Override
	public void close() throws ConnectionException {
		try {
			modelManager.close();
		} catch (AoException e) {
			throw new ConnectionException("Unable to close the connection to the data source due to: " + e.reason, e);
		}
	}

}
