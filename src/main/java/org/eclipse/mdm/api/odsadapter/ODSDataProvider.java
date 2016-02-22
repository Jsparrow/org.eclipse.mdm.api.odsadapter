/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

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
import org.eclipse.mdm.api.base.BaseDataProvider;
import org.eclipse.mdm.api.base.DataProviderException;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.MeasuredValues;
import org.eclipse.mdm.api.base.model.Parameter;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.Parameterizable;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.factory.BaseEntityFactory;
import org.eclipse.mdm.api.base.model.factory.EntityCore;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.odsadapter.massdata.ReadRequestHandler;
import org.eclipse.mdm.api.odsadapter.massdata.WriteRequestHandler;
import org.eclipse.mdm.api.odsadapter.query.DataItemFactory;
import org.eclipse.mdm.api.odsadapter.query.DeleteStatement;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.query.ODSTransaction;
import org.eclipse.mdm.api.odsadapter.query.UpdateStatement;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSDataProvider implements BaseDataProvider, DataItemFactory {

	/**
	 * TODO another write method: Copyable copy(Copyable copyable, String name);
	 * TODO another write method Versionable newVersion(Versionable);
	 */

	private final Map<Class<? extends DataItem>, DataItemCache<? extends DataItem>> dataItemCacheByType = new HashMap<>();
	private final Set<Class<? extends DataItem>> cachedTypes = new HashSet<>();

	private final Map<String, ODSTransaction> transactions = new HashMap<>();

	private final SearchService searchService;
	private final ODSModelManager modelManager;

	private final Environment env;
	private final User loggedOnUser;

	public ODSDataProvider(AoSession aoSession) throws DataProviderException {
		try {
			modelManager = new ODSModelManager(aoSession, this);
			searchService = new ODSSearchService(modelManager, this);

			cachedTypes.add(User.class);
			cachedTypes.add(PhysicalDimension.class);
			cachedTypes.add(Unit.class);
			cachedTypes.add(Quantity.class);

			env = resolveEnvironment();
			loggedOnUser = resolveLoggedOnUser(aoSession);

			/**
			 * TODO provide context properties from AoSession!
			 */
		} catch (AoException e) {
			throw new DataProviderException("Unable to load application model due to: " + e.reason);
		} catch (DataAccessException e) {
			throw new DataProviderException("Unable to initialize data provider.", e);
		}
	}

	@Override
	public Optional<BaseEntityFactory> getEntityFactory(String transactionID) throws DataAccessException {
		ODSTransaction transaction = transactions.get(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		}

		return Optional.of(new ODSEntityFactory(transaction));
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
	public Environment getEnvironment() {
		return env;
	}

	@Override
	public Optional<User> getLoggedOnUser() {
		return Optional.of(loggedOnUser);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataItem> Optional<T> findByURI(URI uri) throws DataAccessException {
		Class<T> type = (Class<T>) ODSUtils.getClass(uri.getTypeName());
		EntityType entityType = modelManager.getEntityType(type);
		if(isCached(type)) {
			return Optional.of(getCached(type, uri.getID()));
		}

		Optional<Result> result = modelManager.createQuery(type).fetchSingleton(Filter.id(entityType, uri.getID()));
		return result.isPresent() ? Optional.of(createDataItem(type, result.get())) : Optional.empty();
	}

	@Override
	public <T extends DataItem> Optional<T> findParent(DataItem child, Class<T> parentType) throws DataAccessException {
		// TODO add a check to ensure that a parent relation really exists?!
		EntityType parentEntityType = modelManager.getEntityType(parentType);
		Query query = modelManager.createQuery(parentType);

		EntityType childEntityType = modelManager.getEntityType(child);
		query.join(parentEntityType, childEntityType);
		Optional<Result> result = query.fetchSingleton(Filter.id(childEntityType, child.getURI().getID()));
		return result.isPresent() ? Optional.of(createDataItem(parentType, result.get())) : Optional.empty();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataItem> List<T> get(Class<T> type) throws DataAccessException {
		if(!isCached(type) || !dataItemCacheByType.containsKey(type)) {
			List<T> dataItems = createDataItemResults(type, modelManager.createQuery(type).fetch());
			if(isCached(type)) {
				dataItemCacheByType.put(type, new DataItemCache<>(dataItems));
			}

			return dataItems;
		}

		return (List<T>) dataItemCacheByType.get(type).getAll();
	}

	@Override
	public <T extends DataItem> List<T> getChildren(DataItem parent, Class<T> type) throws DataAccessException {
		// TODO add a check to ensure a child relation really exists?!
		EntityType parentEntityType = modelManager.getEntityType(parent);
		Query query = modelManager.createQuery(type).join(parentEntityType, modelManager.getEntityType(type));
		return createDataItemResults(type, query.fetch(Filter.id(parentEntityType, parent.getURI().getID())));
	}

	@Override
	public Map<ContextType, ContextRoot> getContexts(ContextDescribable contextDescribable, ContextType... contextTypes)
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
		for(ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {
			EntityType entityContextRootType = modelManager.getEntityType(contextType);
			query.selectAll(entityContextRootType).join(parentEntityType, entityContextRootType);

			List<EntityType> genericEntityTypes = new ArrayList<>();
			for(Relation relation : entityContextRootType.getChildRelations()) {
				query.selectAll(relation.getTarget());
				query.join(relation, Join.OUTER);
				genericEntityTypes.add(relation.getTarget());
			}
			genericModelMap.put(entityContextRootType, genericEntityTypes);

			/*
			 * TODO: Add relations to sensors... (1:n relations)
			 */
		}

		List<Result> results = query.fetch(Filter.id(parentEntityType, contextDescribable.getURI().getID()));
		return generateGenericDataItemStructure(results, genericModelMap);
	}

	@Override
	public List<ParameterSet> getParameterSets(Parameterizable parameterizable) throws DataAccessException {
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
		List<Result> results = query.fetch(Filter.id(parameterizableEntityType, parameterizable.getURI().getID()));

		Map<Long, ParameterSet> parameterSetsByID = new HashMap<>();
		List<ParameterSet> parameterSets = new ArrayList<>();
		for (Result result : results) {
			Record parameterSetRecord = result.removeRecord(parameterSetEntityType);
			Long id = parameterSetRecord.getID();
			ParameterSet parameterSet = parameterSetsByID.get(id);
			if(parameterSet == null) {
				parameterSet = createDataItem(ParameterSet.class, new EntityCore(parameterSetRecord));
				parameterSetsByID.put(id, parameterSet);
				parameterSets.add(parameterSet);
			}

			Record parameterRecord = result.getRecord(parameterEntityType);
			if(parameterRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
				Core parameterCore = new EntityCore(parameterRecord);
				Record unitRecord = result.getRecord(unitEntityType);
				if(unitRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
					parameterCore.setInfoRelation(getCached(Unit.class, unitRecord.getID()));
				}

				parameterSet.getCore().addChild(createDataItem(Parameter.class, parameterCore));
			}
		}

		return parameterSets;
	}

	public void close() throws DataProviderException {
		try {
			modelManager.close();
		} catch (AoException e) {
			throw new DataProviderException("TODO", e); // TODO
		} finally {
			for(ODSTransaction transaction : transactions.values()) {
				try {
					transaction.abortTransaction();
				} catch(AoException e) {
					// TODO log...
				}
			}
		}
	}

	@Override
	public boolean isCached(Class<? extends DataItem> type) {
		return cachedTypes.contains(type);
	}

	@SuppressWarnings("unchecked")
	private <T extends DataItem> T getCached(Class<? extends DataItem> type, Long id) throws DataAccessException {
		DataItemCache<T> dataItemCache = (DataItemCache<T>) dataItemCacheByType.get(type);
		if(dataItemCache == null) {
			get(type);
			dataItemCache = (DataItemCache<T>) dataItemCacheByType.get(type);
		}

		DataItem dataItem = dataItemCache.get(id);
		if(dataItem == null) {
			throw new DataAccessException("Data item with id '" + id + "' of type '" + type.getSimpleName() + "'not found.");
		}

		return (T) dataItem;
	}

	private Environment resolveEnvironment() throws DataAccessException {
		return createDataItem(Environment.class, modelManager.createQuery(Environment.class).fetchSingleton().get());
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

	private Map<ContextType, ContextRoot> generateGenericDataItemStructure(List<Result> results, Map<EntityType,
			List<EntityType>> map) throws DataAccessException {
		if(results.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<ContextType, ContextRoot> contextResults = new EnumMap<>(ContextType.class);
		Result result = results.get(0);
		for(Entry<EntityType, List<EntityType>> entry : map.entrySet()) {
			Record rootRecord = result.getRecord(entry.getKey());
			Core contextRootCore = new EntityCore(rootRecord);
			ContextRoot contextRoot = createDataItem(ContextRoot.class, contextRootCore);

			for(EntityType contextComp : entry.getValue()) {
				Record contextCompRecord = result.getRecord(contextComp);
				if(contextCompRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
					contextRootCore.addChild(createDataItem(ContextComponent.class, new EntityCore(contextCompRecord)));
				}
			}

			contextResults.put(contextRoot.getContextType(), contextRoot);
		}
		return contextResults;
	}

	private <T extends DataItem> List<T> createDataItemResults(Class<T> type, List<Result> results)
			throws DataAccessException {
		List<T> dataItems = new ArrayList<>();
		for(Result result : results) {
			dataItems.add(createDataItem(type, result));
		}

		return dataItems;
	}

	@Override
	public <T extends DataItem> T createDataItem(Class<T> type, Result result) throws DataAccessException {
		Record record = result.removeRecord(modelManager.getEntityType(type));
		Core core = new EntityCore(record);
		for(Record relatedRecord : result) {
			Class<? extends DataItem> clazz = ODSUtils.getClass(relatedRecord.getEntityType().getName());

			if(isCached(clazz)) {
				core.setInfoRelation(getCached(clazz, relatedRecord.getID()));
			} else {
				core.setInfoRelation(createDataItem(clazz, new EntityCore(relatedRecord)));
			}
		}

		return createDataItem(type, core);
	}

	private <T extends DataItem> T createDataItem(Class<T> type, Core core) throws DataAccessException {
		try {
			return type.getDeclaredConstructor(Core.class).newInstance(core);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public <T extends DataItem> void update(String transactionID, List<T> dataItems) throws DataAccessException {
		ODSTransaction transaction = transactions.get(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		} else if(dataItems.isEmpty()) {
			return;
		}

		Map<EntityType, List<DataItem>> dataItemGroups = dataItems.stream().collect(Collectors.groupingBy(transaction.getModelManager()::getEntityType));
		for(Entry<EntityType, List<DataItem>> entry : dataItemGroups.entrySet()) {
			UpdateStatement updateStatement = new UpdateStatement(transaction, entry.getKey());
			entry.getValue().stream().forEach(d -> updateStatement.add(d.getCore()));
			updateStatement.execute();
		}
	}

	@Override
	public <T extends Deletable> List<URI> delete(String transactionID, List<T> dataItems) throws DataAccessException {
		ODSTransaction transaction = transactions.get(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		} else if(dataItems.isEmpty()) {
			return Collections.emptyList();
		}

		// TODO do we have to group data items by their type?!?
		// - group by entity type
		// - sort according hierarchy
		//		- project before pool
		//		- pool before test etc.
		// - process each group in an own delete statement
		// 		- filter next group and remove all instances that have been deleted in the previous delete statement(s)

		//		List<URI> removedURIs = new ArrayList<>();
		//		Map<Entity, List<DataItem>> dataItemGroups = dataItems.stream().collect(Collectors.groupingBy(modelManager::getEntity));
		//		for(Entry<Entity, List<DataItem>> entry : dataItemGroups.entrySet()) {
		//			List<URI> affectedURIs = entry.getValue().stream().map(DataItem::getURI).collect(Collectors.toList());
		//			removedURIs.stream().forEach(u -> affectedURIs.removeIf(u2 -> u.getTypeName().equals(u2.getTypeName()) && u.getID().longValue() == u2.getID().longValue()));
		//			DeleteStatement deleteStatement = new DeleteStatement(modelManager, entry.getKey(), true);
		//			deleteStatement.addInstances(entry.getValue());
		//			removedURIs.addAll(deleteStatement.execute());
		//		}

		EntityType rootEntityType = transaction.getModelManager().getEntityType(dataItems.get(0).getClass());
		DeleteStatement deleteStatement = new DeleteStatement(transaction, rootEntityType, true);
		deleteStatement.addInstances(dataItems);
		return deleteStatement.execute();
	}

	@Override
	public List<MeasuredValues> readMeasuredValues(ReadRequest readRequest) throws DataAccessException {
		return new ReadRequestHandler(modelManager).execute(readRequest);
	}

	@Override
	public void writeMeasuredValues(String transactionID, List<WriteRequest> writeRequests) throws DataAccessException {
		ODSTransaction transaction = transactions.get(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		}

		if(writeRequests.isEmpty()) {
			return;
		}

		Map<ScalarType, List<WriteRequest>> writeRequestsByRawType = writeRequests.
				stream().collect(Collectors.groupingBy(WriteRequest::getRawScalarType));

		for(List<WriteRequest> writeRequestGroup : writeRequestsByRawType.values()) {
			WriteRequestHandler writeRequestHandler = new WriteRequestHandler(transaction);
			List<Channel> channels = new ArrayList<>();

			for(WriteRequest writeRequest : writeRequestGroup) {
				Channel channel = writeRequest.getChannel();
				channel.setScalarType(writeRequest.getCalculatedScalarType());
				// TODO it might be required to change relation to another unit?!??
				channels.add(channel);
				writeRequestHandler.addRequest(writeRequest);
			}

			update(transactionID, channels);

			writeRequestHandler.execute();
		}
	}

	@Override
	public String startTransaction() throws DataAccessException {
		try {
			ODSTransaction transaction = new ODSTransaction(modelManager);
			transactions.put(transaction.getID(), transaction);
			return transaction.getID();
		} catch (AoException e) {
			throw new DataAccessException("Unable to initialize a transaction due to: " + e.reason);
		}
	}

	@Override
	public void commitTransaction(String transactionID) throws DataAccessException {
		ODSTransaction transaction = transactions.get(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		}

		try {
			transaction.commitTransaction();
			transactions.remove(transactionID);
		} catch (AoException e) {
			throw new DataAccessException("Unable to commit transaction due to: " + e.reason);
		}
	}

	@Override
	public void abortTransaction(String transactionID) throws DataAccessException {
		ODSTransaction transaction = transactions.remove(transactionID);
		if(transaction == null) {
			// TODO this is an implementation error -> runtime based exception should be thrown
			throw new DataAccessException("Transaction with given ID not found.");
		}

		try {
			transaction.abortTransaction();
		} catch (AoException e) {
			throw new DataAccessException("Unable to commit transaction due to: " + e.reason);
		}
	}

	private static final class DataItemCache<T extends DataItem> {

		private final Map<Long, T> dataItemsByID;
		private final List<T> dataItems;

		private DataItemCache(List<T> dataItems) {
			this.dataItems = new ArrayList<>(dataItems);
			dataItemsByID = dataItems.stream().collect(Collectors.toMap(d -> d.getURI().getID(), Function.identity()));
		}

		private T get(Long id) {
			// TODO create deep copy!!
			return dataItemsByID.get(id);
		}

		private List<T> getAll() {
			// TODO create deep copy for each data item!!
			return new ArrayList<>(dataItems);
		}

	}

}
