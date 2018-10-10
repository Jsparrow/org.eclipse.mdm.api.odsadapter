/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.ApplicationStructure;
import org.asam.ods.ElemId;
import org.asam.ods.InstanceElement;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.adapter.EntityType;
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
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.odsadapter.filetransfer.Transfer;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
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
	private final Transfer transfer = Transfer.SOCKET;
	
	private final ODSContext context;
	private final ODSModelManager odsModelManager;
	private final QueryService queryService;
	private final EntityLoader entityLoader;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param context
	 *            The {@link ODSContext}.
	 */
	public ODSEntityManager(ODSContext context) {
		this.context = context;
		this.odsModelManager = context.getODSModelManager();
		this.queryService = context.getQueryService()
				.orElseThrow(() -> new ServiceNotProvidedException(QueryService.class));
		entityLoader = new EntityLoader(odsModelManager, queryService);
	}

	// ======================================================================
	// Public methods
	// ======================================================================


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
			ieUser = odsModelManager.getAoSession().getUser();
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
		EntityType parentEntityType = odsModelManager.getEntityType(entityClass);
		EntityType childEntityType = odsModelManager.getEntityType(child);
		Query query = queryService.createQuery().selectID(parentEntityType);

		if (child instanceof Channel && ChannelGroup.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local
			// column
			EntityType localColumnEntityType = odsModelManager.getEntityType("LocalColumn");
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
		EntityType parentEntityType = odsModelManager.getEntityType(parent);
		EntityType childEntityType = odsModelManager.getEntityType(entityClass);
		Query query = queryService.createQuery();

		if (parent instanceof ChannelGroup && Channel.class.equals(entityClass)) {
			// this covers the gap between channel and channel group via local
			// column
			EntityType localColumnEntityType = odsModelManager.getEntityType("LocalColumn");
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
		EntityType contextDescribableEntityType = odsModelManager.getEntityType(contextDescribable);
		Query query = queryService.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for (ContextType contextType : ContextType.values()) {
			EntityType entityType = odsModelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), JoinType.OUTER).selectID(entityType);
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
		EntityType contextDescribableEntityType = odsModelManager.getEntityType(contextDescribable);
		Query query = queryService.createQuery();

		Map<ContextType, EntityType> contextRootEntityTypes = new EnumMap<>(ContextType.class);
		for (ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {
			EntityType entityType = odsModelManager.getEntityType(ContextRoot.class, contextType);
			contextRootEntityTypes.put(contextType, entityType);
			query.join(contextDescribableEntityType.getRelation(entityType), JoinType.OUTER).selectID(entityType);
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
		return new ReadRequestHandler(odsModelManager).execute(readRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction startTransaction() throws DataAccessException {
		try {
			return new ODSTransaction(context, loadEnvironment(), transfer);
		} catch (AoException e) {
			throw new DataAccessException("Unable to start transaction due to: " + e.reason, e);
		}
	}

	/**
	 * Retrives the ASAM paths for the given entities. The ASAM paths are prefixed with a servicename, in the form
	 * <code>corbaloc:[iop|ssliop]:1.2@HOSTNAME:PORT/NameService/MDM.ASAM-ODS/</code> 
	 * @returns returns a map with the ASAM paths to the given entities. If a entity is not found in the ODS server
	 * the entity is not included in the result map.
	 * @throws DataAccessException if links could not be loaded for the given entities
	 * @throws IllegalArgumentException if a the source or typeName of an entity is invalid
	 * @see org.eclipse.mdm.api.base.BaseEntityManager#getLinks(Collection)
	 */
	@Override
	public Map<Entity, String> getLinks(Collection<Entity> entities) throws DataAccessException {
		
		Map<Entity, String> linkMap = new HashMap<>();
		
		ApplicationStructure appStructure;
		try {
			appStructure = odsModelManager.getAoSession().getApplicationStructure();
		} catch (AoException e) {
			throw new DataAccessException("Could not load application structure! Reason: " + e.reason, e);
		}
		
		String serverRoot = context.getParameters().get(ODSContextFactory.PARAM_NAMESERVICE) 
				+ "/" + context.getParameters().get(ODSContextFactory.PARAM_SERVICENAME);
		
		Map<String, List<Entity>> entitiesByTypeName = entities.stream()
				.filter(e -> e.getTypeName() != null)
				.collect(Collectors.groupingBy(Entity::getTypeName));
		
		for (Map.Entry<String, List<Entity>> entry : entitiesByTypeName.entrySet()) {
			ODSEntityType et = (ODSEntityType) odsModelManager.getEntityType(entry.getKey());
			
			List<ElemId> elemIds = entry.getValue().stream()
				.map(e -> new ElemId(et.getODSID(), ODSConverter.toODSLong(Long.parseLong(e.getID()))))
				.collect(Collectors.toList());
			
			try {
				InstanceElement[] instances = appStructure.getInstancesById(elemIds.toArray(new ElemId[0]));
				
				for (InstanceElement ie : instances) {
					String id = Long.toString(ODSConverter.fromODSLong(ie.getId()));
					String asamPath = serverRoot + ie.getAsamPath();
					entry.getValue().stream()
							.filter(e -> e.getID().equals(id))
							.findFirst()
							.ifPresent(e -> linkMap.put(e, asamPath));
				}
			} catch (AoException e) {
				LOGGER.debug("Could not load links for entities: " + entities + ". Reason: " + e.reason, e);
			}
		}
		
		return linkMap;
	}
}
