/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.InstanceElement;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.EntityManager;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.EntityFactory;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.Parameter;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.Parameterizable;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.DefaultEntityCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.odsadapter.query.DataItemFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.transaction.ODSTransaction;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSEntityManager implements EntityManager, DataItemFactory {

	/**
	 * TODO another write method: Copyable copy(Copyable copyable, String name);
	 * TODO another write method Versionable newVersion(Versionable);
	 */

	private final Map<Class<? extends Entity>, EntityCache<? extends Entity>> entityCacheByType = new HashMap<>();
	private final Set<Class<? extends Entity>> cachedTypes = new HashSet<>();

	private final SearchService searchService;
	private final ODSModelManager modelManager;
	private final ODSEntityFactory entityFactory;

	private final User loggedOnUser;

	public ODSEntityManager(AoSession aoSession) throws ConnectionException {
		try {
			modelManager = new ODSModelManager(aoSession, this);
			searchService = new ODSSearchService(modelManager, this);
			entityFactory = new ODSEntityFactory(modelManager);

			cachedTypes.add(User.class);
			cachedTypes.add(PhysicalDimension.class);
			cachedTypes.add(Unit.class);
			cachedTypes.add(Quantity.class);

			loggedOnUser = resolveLoggedOnUser(aoSession);

			/**
			 * TODO provide context properties from AoSession!
			 */
		} catch (AoException e) {
			throw new ConnectionException("Unable to load application model due to: " + e.reason);
		} catch (DataAccessException e) {
			throw new ConnectionException("Unable to initialize the entity manager.", e);
		}
	}

	@Override
	public Optional<EntityFactory> getEntityFactory() {
		return Optional.of(entityFactory);
	}

	@Override
	public Optional<ModelManager> getModelManager() {
		return Optional.of(modelManager);
	}

	@Override
	public Optional<SearchService> getSearchService() {
		return Optional.of(searchService);
	}

	@Override
	public Environment loadEnvironment() throws DataAccessException {
		// this one could be declared as cached...
		return createEntity(Environment.class, modelManager.createQuery(Environment.class).fetchSingleton().get());
	}

	@Override
	public Optional<User> loadLoggedOnUser() {
		return Optional.of(loggedOnUser);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> Optional<T> load(URI uri) throws DataAccessException {
		Class<T> type = (Class<T>) ODSUtils.getClass(uri.getTypeName());
		EntityType entityType = modelManager.getEntityType(type);
		if(isCached(type)) {
			return Optional.of(getCached(type, uri.getID()));
		}

		Optional<Result> result = modelManager.createQuery(type).fetchSingleton(Filter.idOnly(entityType, uri.getID()));
		return result.isPresent() ? Optional.of(createEntity(type, result.get())) : Optional.empty();
	}

	@Override
	public <T extends Entity> Optional<T> loadParent(Entity child, Class<T> parentType) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parentType);
		EntityType childEntityType = modelManager.getEntityType(child);
		Query query = modelManager.createQuery(parentType);

		if(child instanceof Channel && ChannelGroup.class.equals(parentType)) {
			// this covers the gap between channel and channel group via local column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(parentEntityType, localColumnEntityType).join(localColumnEntityType, childEntityType);
		} else {
			// TODO add a check to ensure that a parent relation really exists?!
			query.join(parentEntityType, childEntityType);
		}

		Optional<Result> result = query.fetchSingleton(Filter.idOnly(childEntityType, child.getURI().getID()));
		return result.isPresent() ? Optional.of(createEntity(parentType, result.get())) : Optional.empty();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> loadAll(Class<T> type) throws DataAccessException {
		if(!isCached(type) || !entityCacheByType.containsKey(type)) {
			List<T> entities = createEntities(type, modelManager.createQuery(type).fetch());
			if(isCached(type)) {
				entityCacheByType.put(type, new EntityCache<>(entities));
			}

			return entities;
		}

		return (List<T>) entityCacheByType.get(type).getAll();
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> type, String pattern) throws DataAccessException {
		EntityType entityType = modelManager.getEntityType(type);
		return createEntities(type, modelManager.createQuery(type).fetch(Filter.nameOnly(entityType, pattern)));
	}

	@Override
	public <T extends Entity> List<T> loadChildren(Entity parent, Class<T> type, String pattern) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType childEntityType = modelManager.getEntityType(type);
		Query query = modelManager.createQuery(type);

		if(parent instanceof ChannelGroup && Channel.class.equals(type)) {
			// this covers the gap between channel and channel group via local column
			EntityType localColumnEntityType = modelManager.getEntityType("LocalColumn");
			query.join(childEntityType, localColumnEntityType).join(localColumnEntityType, parentEntityType);
		} else {
			// TODO add a check to ensure a child relation really exists?!
			query.join(childEntityType, parentEntityType);
		}

		return createEntities(type, query.fetch(Filter.and()
				.id(parentEntityType, parent.getURI().getID())
				.name(childEntityType, pattern)));
	}

	@Override
	public Map<ContextType, ContextRoot> loadContexts(ContextDescribable contextDescribable, ContextType... contextTypes)
			throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(contextDescribable);
		Query query = modelManager.createQuery();

		/*
		 * TODO - very important:
		 * current query assumes 1:1 relations between context root and context components
		 *
		 * BUT
		 * because template components may be derived from the same catalog component the following query is incomplete
		 * => we need mapping over multiple results to cover 1:n relations between context root and context components
		 */

		Map<EntityType, List<EntityType>> genericModelMap = new HashMap<>();
		ContextType[] types = contextTypes.length == 0 || contextTypes.length > 3 ? ContextType.values() : contextTypes;
		for(ContextType contextType : types) {
			EntityType entityContextRootType = modelManager.getEntityType(contextType);
			query.selectAll(entityContextRootType)
			.join(parentEntityType.getRelation(entityContextRootType), Join.OUTER);

			List<EntityType> genericEntityTypes = new ArrayList<>();
			for(Relation relation : entityContextRootType.getChildRelations()) {
				query.selectAll(relation.getTarget()).join(relation, Join.OUTER);
				genericEntityTypes.add(relation.getTarget());
			}
			genericModelMap.put(entityContextRootType, genericEntityTypes);

			/*
			 * TODO: Add relations to sensors... (1:n relations)
			 */
		}

		List<Result> results = query.fetch(Filter.idOnly(parentEntityType, contextDescribable.getURI().getID()));
		return generateGenericEntityStructure(results, genericModelMap);
	}

	@Override
	public List<ParameterSet> loadParameterSets(Parameterizable parameterizable, String pattern) throws DataAccessException {
		EntityType parameterizableEntityType = modelManager.getEntityType(parameterizable);
		EntityType parameterSetEntityType = modelManager.getEntityType(ParameterSet.class);
		EntityType parameterEntityType = modelManager.getEntityType(Parameter.class);
		EntityType unitEntityType = modelManager.getEntityType(Unit.class);

		Query query = modelManager.createQuery()
				.selectAll(parameterSetEntityType, parameterEntityType)
				.selectID(unitEntityType)
				.join(parameterizableEntityType, parameterSetEntityType)
				.join(parameterSetEntityType.getRelation(parameterEntityType), Join.OUTER)
				.join(parameterEntityType.getRelation(unitEntityType), Join.OUTER);

		List<Result> results = query.fetch(Filter.and()
				.id(parameterizableEntityType, parameterizable.getURI().getID())
				.name(parameterSetEntityType, pattern));

		Map<Long, EntityCore> parameterSetCoresByID = new HashMap<>();
		List<ParameterSet> parameterSets = new ArrayList<>();
		for (Result result : results) {
			Record parameterSetRecord = result.removeRecord(parameterSetEntityType);
			Long id = parameterSetRecord.getID();
			EntityCore parameterSetCore = parameterSetCoresByID.get(id);
			if(parameterSetCore == null) {
				parameterSetCore = new DefaultEntityCore(parameterSetRecord);
				parameterSetCoresByID.put(id, parameterSetCore);
				parameterSets.add(createEntity(ParameterSet.class, parameterSetCore));
			}

			Record parameterRecord = result.getRecord(parameterEntityType);
			if(parameterRecord.getValues().get(Entity.ATTR_ID).isValid()) {
				EntityCore parameterCore = new DefaultEntityCore(parameterRecord);
				Record unitRecord = result.getRecord(unitEntityType);
				if(unitRecord.getValues().get(Entity.ATTR_ID).isValid()) {
					parameterCore.setInfoRelation(getCached(Unit.class, unitRecord.getID()));
				}

				parameterSetCore.addChild(createEntity(Parameter.class, parameterCore));
			}
		}

		return parameterSets;
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
			throw new ConnectionException("TODO", e);
			// TODO
		}
	}

	@Override
	public boolean isCached(Class<? extends Entity> type) {
		return cachedTypes.contains(type);
	}

	@SuppressWarnings("unchecked")
	private <T extends Entity> T getCached(Class<? extends Entity> type, Long id) throws DataAccessException {
		EntityCache<T> entityCache = (EntityCache<T>) entityCacheByType.get(type);
		if(entityCache == null) {
			loadAll(type);
			entityCache = (EntityCache<T>) entityCacheByType.get(type);
		}

		Entity entity = entityCache.get(id);
		if(entity == null) {
			throw new DataAccessException("Entity with id '" + id + "' of type '" + type.getSimpleName() + "'not found.");
		}

		return (T) entity;
	}

	private User resolveLoggedOnUser(AoSession aoSession) throws DataAccessException {
		InstanceElement ieUser = null;
		try {
			ieUser = aoSession.getUser();
			return getCached(User.class, ODSConverter.fromODSLong(ieUser.getId()));
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
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

	private Map<ContextType, ContextRoot> generateGenericEntityStructure(List<Result> results, Map<EntityType,
			List<EntityType>> map) throws DataAccessException {
		if(results.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<ContextType, ContextRoot> contextResults = new EnumMap<>(ContextType.class);
		Result result = results.get(0);
		for(Entry<EntityType, List<EntityType>> entry : map.entrySet()) {
			Record rootRecord = result.getRecord(entry.getKey());
			EntityCore contextRootCore = new DefaultEntityCore(rootRecord);
			ContextRoot contextRoot = createEntity(ContextRoot.class, contextRootCore);

			for(EntityType contextComp : entry.getValue()) {
				Record contextCompRecord = result.getRecord(contextComp);
				if(contextCompRecord.getValues().get(Entity.ATTR_ID).isValid()) {
					contextRootCore.addChild(createEntity(ContextComponent.class, new DefaultEntityCore(contextCompRecord)));
				}
			}

			contextResults.put(contextRoot.getContextType(), contextRoot);
		}
		return contextResults;
	}

	private <T extends Entity> List<T> createEntities(Class<T> type, List<Result> results)
			throws DataAccessException {
		List<T> entities = new ArrayList<>();
		for(Result result : results) {
			entities.add(createEntity(type, result));
		}

		return entities;
	}

	@Override
	public <T extends Entity> T createEntity(Class<T> type, Result result) throws DataAccessException {
		Record record = result.removeRecord(modelManager.getEntityType(type));
		EntityCore core = new DefaultEntityCore(record);
		for(Record relatedRecord : result) {
			Class<? extends Entity> clazz = ODSUtils.getClass(relatedRecord.getEntityType().getName());

			if(isCached(clazz)) {
				core.setInfoRelation(getCached(clazz, relatedRecord.getID()));
			} else {
				core.setInfoRelation(createEntity(clazz, new DefaultEntityCore(relatedRecord)));
			}
		}

		return createEntity(type, core);
	}

	private <T extends Entity> T createEntity(Class<T> type, EntityCore core) throws DataAccessException {
		Constructor<?> entityConstructor = null;
		boolean isAccessible = false;
		try {
			entityConstructor = type.getDeclaredConstructor(EntityCore.class);
			isAccessible = entityConstructor.isAccessible();
			entityConstructor.setAccessible(true);
			return (T) entityConstructor.newInstance(core);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			if(entityConstructor != null) {
				entityConstructor.setAccessible(isAccessible);
			}
		}
	}

	private static final class EntityCache<T extends Entity> {

		private final Map<Long, T> entitiesByID;
		private final List<T> entities;

		private EntityCache(List<T> entities) {
			this.entities = new ArrayList<>(entities);
			entitiesByID = entities.stream().collect(Collectors.toMap(d -> d.getURI().getID(), Function.identity()));
		}

		private T get(Long id) {
			// TODO create deep copy!!
			return entitiesByID.get(id);
		}

		private List<T> getAll() {
			// TODO create deep copy for each entity!!
			return new ArrayList<>(entities);
		}

	}

}
