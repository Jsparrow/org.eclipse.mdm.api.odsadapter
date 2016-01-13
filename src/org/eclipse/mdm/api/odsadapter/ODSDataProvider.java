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
import java.util.Arrays;
import java.util.Collections;
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
import org.asam.ods.ApplElemAccess;
import org.asam.ods.InstanceElement;
import org.asam.ods.NameValue;
import org.asam.ods.NameValueIterator;
import org.eclipse.mdm.api.base.BaseDataProvider;
import org.eclipse.mdm.api.base.DataProviderException;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ChannelInfo;
import org.eclipse.mdm.api.base.model.ChannelValue;
import org.eclipse.mdm.api.base.model.ChannelValuesReadRequest;
import org.eclipse.mdm.api.base.model.ChannelValuesWriteRequest;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Parameter;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.Parameterizable;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.odsadapter.query.ChannelReadRequest;
import org.eclipse.mdm.api.odsadapter.query.ChannelWriteRequest;
import org.eclipse.mdm.api.odsadapter.query.DataItemFactory;
import org.eclipse.mdm.api.odsadapter.query.DeleteStatement;
import org.eclipse.mdm.api.odsadapter.query.InsertStatement;
import org.eclipse.mdm.api.odsadapter.query.ODSQueryService;
import org.eclipse.mdm.api.odsadapter.query.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.query.UpdateStatement;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSDataProvider implements BaseDataProvider, DataItemFactory {

	/**
	 * TODO another write method: Copyable copy(Copyable copyable, String name);
	 */

	private final Map<Class<? extends DataItem>, DataItemCache<? extends DataItem>> dataItemCacheByType = new HashMap<>();
	private final Set<Class<? extends DataItem>> cachedTypes = new HashSet<>();

	private final AoSession aoSession;

	private final SearchService searchService;
	private final ODSQueryService queryService;

	private final Environment env;
	private final User loggedOnUser;

	private boolean transactionActive = false;

	public ODSDataProvider(AoSession aoSession) throws DataProviderException {
		this.aoSession = aoSession;

		try {
			queryService = new ODSQueryService(aoSession, this);
			searchService = new ODSSearchService(queryService, this);

			cachedTypes.add(User.class);
			cachedTypes.add(PhysicalDimension.class);
			cachedTypes.add(Unit.class);
			cachedTypes.add(Quantity.class);

			env = resolveEnvironment();
			loggedOnUser = resolveLoggedOnUser();

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
	public Optional<SearchService> getSearchService() {
		return Optional.of(searchService);
	}

	@Override
	public Optional<QueryService> getQueryService() {
		return Optional.of(queryService);
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
	public <T extends DataItem> Optional<T> findByID(Class<T> type, Long id) throws DataAccessException {
		return findByURI(new URI(getEnvironment().getName(), queryService.getEntity(type).getName(), id));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataItem> Optional<T> findByURI(URI uri) throws DataAccessException {
		Class<T> type = (Class<T>) ODSUtils.getClass(uri.getTypeName());
		Entity entity = queryService.getEntity(type);
		if(isCached(type)) {
			return Optional.of(getCached(type, uri.getID()));
		}

		Optional<Result> result = queryService.createQuery(type).fetchSingleton(Filter.id(entity, uri.getID()));
		return result.isPresent() ? Optional.of(createDataItem(type, result.get())) : Optional.empty();
	}

	@Override
	public <T extends DataItem> Optional<T> findParent(DataItem child, Class<T> parentType) throws DataAccessException {
		Entity parentEntity = queryService.getEntity(parentType);
		Query query = queryService.createQuery(parentType);

		Entity childEntity = queryService.getEntity(child.getURI().getTypeName());
		query.join(parentEntity, childEntity);
		Optional<Result> result = query.fetchSingleton(Filter.id(childEntity, child.getURI().getID()));
		return result.isPresent() ? Optional.of(createDataItem(parentType, result.get())) : Optional.empty();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataItem> List<T> get(Class<T> type) throws DataAccessException {
		if(!isCached(type) || !dataItemCacheByType.containsKey(type)) {
			List<T> dataItems = createDataItemResults(type, queryService.createQuery(type).fetch());
			if(isCached(type)) {
				dataItemCacheByType.put(type, new DataItemCache<>(dataItems));
			}

			return dataItems;
		}

		return (List<T>) dataItemCacheByType.get(type).getAll();
	}

	@Override
	public <T extends DataItem> List<T> getChildren(DataItem parent, Class<T> type) throws DataAccessException {
		Entity parentEntity = queryService.getEntity(parent.getURI().getTypeName());
		Entity entity = queryService.getEntity(type);

		Entity target = entity;
		if(parent instanceof ChannelGroup && Channel.class == type) {
			// there is no direct relation between channel and channel group,
			// therefore we go via the channel info
			target = queryService.getEntity(ChannelInfo.class);
		}

		Query query = queryService.createQuery(type).join(parentEntity, target);
		return createDataItemResults(type, query.fetch(Filter.id(parentEntity, parent.getURI().getID())));
	}

	@Override
	public Map<ContextType, ContextRoot> getContext(ContextDescribable contextDescribable, ContextType... contextTypes)
			throws DataAccessException {
		Entity parentEntity = queryService.getEntity(contextDescribable.getURI().getTypeName());
		Query query = queryService.createQuery();

		Map<Entity, List<Entity>> genericModelMap = new HashMap<Entity, List<Entity>>();
		for(ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {
			Entity entityContextRoot = queryService.getEntity(contextType);
			query.selectAll(entityContextRoot).join(parentEntity, entityContextRoot);

			List<Entity> genericEntities = new ArrayList<>();
			for(Relation relation : entityContextRoot.getChildRelations()) {
				query.selectAll(relation.getTarget());
				query.join(relation, Join.OUTER);
				genericEntities.add(relation.getTarget());
			}
			genericModelMap.put(entityContextRoot, genericEntities);

			/**
			 * TODO: Add relations to sensors... (1:n relations)
			 */
		}

		List<Result> results = query.fetch(Filter.id(parentEntity, contextDescribable.getURI().getID()));
		return generateGenericDataItemStructure(results, genericModelMap);
	}

	@Override
	public List<ParameterSet> getParameterSets(Parameterizable parameterizable) throws DataAccessException {
		Entity dataItemEntity = queryService.getEntity(parameterizable.getClass());
		Entity parameterSetEntity = queryService.getEntity(ParameterSet.class);
		Entity parameterEntity = queryService.getEntity(Parameter.class);
		Entity unitEntity = queryService.getEntity(Unit.class);
		/**
		 * TODO relation from Parameter to Unit is missing!
		 */

		Query query = queryService.createQuery().selectAll(parameterSetEntity, parameterEntity).selectID(unitEntity);

		query.join(dataItemEntity, parameterSetEntity).join(parameterSetEntity.getRelation(parameterEntity), Join.OUTER)
		.join(parameterEntity.getRelation(unitEntity), Join.OUTER);
		List<Result> results = query.fetch(Filter.id(dataItemEntity, parameterizable.getURI().getID()));

		Map<Long, ParameterSet> parameterSetsByID = new HashMap<>();
		List<ParameterSet> parameterSets = new ArrayList<>();
		for (Result result : results) {
			Record parameterSetRecord = result.removeRecord(parameterSetEntity);
			Long id = parameterSetRecord.getID();
			ParameterSet parameterSet = parameterSetsByID.get(id);
			if(parameterSet == null) {
				parameterSet = createDataItemInstance(ParameterSet.class, parameterSetEntity, parameterSetRecord.getValues());
				parameterSetsByID.put(id, parameterSet);
				parameterSets.add(parameterSet);
			}

			Record parameterRecord = result.getRecord(parameterEntity);
			if(parameterRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
				Record unitRecord = result.getRecord(unitEntity);
				Map<Class<? extends DataItem>, DataItem> references = new HashMap<>();
				if(unitRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
					references.put(Unit.class, getCached(Unit.class, unitRecord.getID()));
				}

				parameterSet.getParameters().add(createDataItemInstance(Parameter.class, parameterEntity,
						parameterRecord.getValues(), references));
			}
		}

		return parameterSets;
	}

	public void close() throws DataProviderException {
		try {
			aoSession.close();
		} catch(AoException aoe) {
			throw new DataProviderException(aoe.reason, aoe);
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
			throw new IllegalArgumentException("Data item with id '" + id + "' of type '" + type.getSimpleName() + "'not found.");
		}

		return (T) dataItem;
	}

	private Environment resolveEnvironment() throws DataAccessException {
		return createDataItem(Environment.class, queryService.createQuery(Environment.class).fetchSingleton().get());
	}

	private User resolveLoggedOnUser() throws DataAccessException {
		InstanceElement ieUser = null;
		try {
			ieUser = aoSession.getUser();
			// TODO use findByURI!
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

	private Map<ContextType, ContextRoot> generateGenericDataItemStructure(List<Result> results, Map<Entity,
			List<Entity>> map) throws DataAccessException {
		if(results.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<ContextType, ContextRoot> contextResults = new HashMap<>();
		Result result = results.get(0);
		for(Entry<Entity, List<Entity>> keyValue : map.entrySet()) {
			Record rootRecord = result.getRecord(keyValue.getKey());
			ContextRoot contextRoot = createDataItemInstance(ContextRoot.class, keyValue.getKey(), rootRecord.getValues());

			for(Entity contextComp : keyValue.getValue()) {
				Record contextCompRecord = result.getRecord(contextComp);
				if(contextCompRecord.getValues().get(DataItem.ATTR_ID).isValid()) {
					contextRoot.getContextComponents().add(createDataItemInstance(ContextComponent.class, contextComp,
							contextCompRecord.getValues()));
				}
			}

			contextResults.put(ContextType.valueOf(ContextType.class, keyValue.getKey().getName().toUpperCase()), contextRoot);
		}
		return contextResults;
	}

	private <T extends DataItem> List<T> createDataItemResults(Class<T> type, List<Result> results)
			throws DataAccessException {
		List<T> dataItems = new ArrayList<T>();
		for(Result result : results) {
			dataItems.add(createDataItem(type, result));
		}

		return dataItems;
	}

	@Override
	public <T extends DataItem> T createDataItem(Class<T> type, Result result) throws DataAccessException {
		Entity typeEntity = queryService.getEntity(type);

		Map<Class<? extends DataItem>, DataItem> references = new HashMap<Class<? extends DataItem>, DataItem>();
		Record record = result.removeRecord(typeEntity);

		for(Record relatedRecord : result) {
			Class<? extends DataItem> clazz = ODSUtils.getClass(relatedRecord.getEntity().getName());

			if(isCached(clazz)) {
				references.put(clazz, getCached(clazz, relatedRecord.getID()));
			} else {
				references.put(clazz, createDataItemInstance(clazz, relatedRecord.getEntity(), relatedRecord.getValues()));
			}
		}

		return createDataItemInstance(type, typeEntity, record.getValues(), references);
	}

	@Deprecated
	private <T extends DataItem> T createDataItemInstance(Class<T> type, Entity entity,	Map<String, Value> values)
			throws DataAccessException {
		return createDataItemInstance(type, entity, values, new HashMap<Class<? extends DataItem>, DataItem>());
	}

	private <T extends DataItem> T createDataItemInstance(Class<T> type, Entity entity,
			Map<String, Value> values, Map<Class<? extends DataItem>, DataItem> references) throws DataAccessException {

		Constructor<T> constructor = null;
		try {

			String typeName = entity.getName();

			Long id = values.remove(DataItem.ATTR_ID).extract();
			URI uri;
			if(Environment.class == type) {
				//				constructor = type.getDeclaredConstructor(Map.class, Map.class);
				//				constructor.setAccessible(true);
				//				return constructor.newInstance(values, getContext(aoSession));
				uri = new URI(values.get(DataItem.ATTR_NAME).extract(), Environment.class.getSimpleName(), id);
			} else {
				uri = new URI(getEnvironment().getName(), typeName, id);
			}

			constructor = type.getDeclaredConstructor(Map.class, URI.class, Map.class);
			constructor.setAccessible(true);
			return constructor.newInstance(values, uri, references);
		} catch (InstantiationException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			if(constructor != null) {
				constructor.setAccessible(false);
			}
		}
	}

	public static Map<String, Value> getContext(AoSession aoSession) throws DataAccessException {
		try {
			NameValueIterator nvi  = aoSession.getContext("*");
			Map<String, Value> contextValues = new HashMap<>();
			for(NameValue nameValue : nvi.nextN(nvi.getCount())) {
				Value value = ODSConverter.fromODS(nameValue);
				contextValues.put(value.getName(), value);
			}
			return contextValues;
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	public <T extends DataItem> List<T> create(Class<T> type, List<List<Value>> valueSets, List<DataItem> references) throws DataAccessException {

		try {

			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}


			List<T> dataItemList = new ArrayList<T>();

			Entity entity = queryService.getEntity(type);
			InsertStatement is = new InsertStatement(aoSession.getApplElemAccess(), entity);

			List<Attribute> attributes = entity.getAttributes();
			List<Map<String, Value>> instanceValueSets = new ArrayList<Map<String,Value>>();
			Map<Class<? extends DataItem>, DataItem> referenceMap = new HashMap<Class<? extends DataItem>, DataItem>();

			String defaultMimeType = ODSUtils.getDefaultMimeType(type.getSimpleName());

			for(List<Value> valueSet : valueSets) {

				Map<String, Value> initialValues = ODSUtils.createInitialValueMap(attributes);
				for(Value newValue : valueSet) {
					initialValues.replace(newValue.getName(), newValue);
				}

				String mimeTypeAttributeName = entity.getMimeTypeAttribute().getName();
				Value mimeTypeValue = initialValues.get(mimeTypeAttributeName);

				if(!mimeTypeValue.isValid()) {
					Value defaultMimeTypeValue = ValueType.STRING.newValue(mimeTypeAttributeName, defaultMimeType);
					initialValues.replace(mimeTypeAttributeName, defaultMimeTypeValue);
				}

				is.next();
				is.insertValues(initialValues.values());

				for(DataItem reference : references) {
					String referenceAEName = ODSUtils.getAEName(reference.getClass());
					Entity referenceEntity = queryService.getEntity(reference.getClass());

					is.insertValue(ValueType.LONG.newValue(referenceAEName, reference.getURI().getID()));

					//skipping parent relation for reference map
					Optional<Relation> parentRelation = entity.getParentRelation();
					if(parentRelation.isPresent() && parentRelation.get().getTarget().equals(referenceEntity)) {
						continue;
					}
					referenceMap.put(reference.getClass(), reference);
				}
				instanceValueSets.add(initialValues);
			}

			List<Long> instanceIDs = is.execute();

			int i = 0;
			for(Map<String, Value> instanceValueSet : instanceValueSets) {
				String idAttribuetName = entity.getIDAttribute().getName();
				Value idValue = ValueType.LONG.newValue(idAttribuetName, instanceIDs.get(i));
				instanceValueSet.replace(idAttribuetName, idValue);
				T dataItem = createDataItemInstance(type, entity, instanceValueSet, referenceMap);
				dataItemList.add(dataItem);
				i++;
			}

			return dataItemList;
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}

	}

	public <T extends DataItem> List<T> create(Class<T> type, List<String> names, DataItem... references) throws DataAccessException {
		List<List<Value>> valueSets = new ArrayList<List<Value>>();
		Entity entity = queryService.getEntity(type);

		for(String name : names) {
			List<Value> list = new ArrayList<Value>();
			String nameAttributeName = entity.getNameAttribute().getName();
			list.add(ValueType.STRING.newValue(nameAttributeName, name));
			valueSets.add(list);
		}
		return create(type, valueSets, Arrays.asList(references));
	}

	public <T extends DataItem> List<T> create(Class<T> type, List<String> names) throws DataAccessException {
		List<List<Value>> valueSets = new ArrayList<List<Value>>();
		Entity entity = queryService.getEntity(type);

		for(String name : names) {
			List<Value> list = new ArrayList<Value>();
			String nameAttributeName = entity.getNameAttribute().getName();
			list.add(ValueType.STRING.newValue(nameAttributeName, name));
			valueSets.add(list);
		}
		return create(type, valueSets, new ArrayList<DataItem>());
	}

	public <T extends DataItem> void update(List<T> dataItems) throws DataAccessException {
		/**
		 * TODO: because lines below compile it is required to group passed data items by their entity type!!
		 */
		//		Test t = null;
		//		TestStep ts = null;
		//
		//		odsDataProvider.update(Arrays.asList(t, ts));


		try {

			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}

			if(dataItems == null || dataItems.size() <= 0) {
				return;
			}

			Entity entity = queryService.getEntity(dataItems.get(0).getClass());
			long id = dataItems.get(0).getURI().getID();
			UpdateStatement us = new UpdateStatement(aoSession.getApplElemAccess(), entity, id);

			for(DataItem dataItem : dataItems) {
				us.next(dataItem.getURI().getID());
				us.setValues(dataItem.getValues().values());
				for(DataItem reference : dataItem.getRelatedDataItems().values()) {

					if(ChannelInfo.class == reference.getClass()) {
						continue;
					}

					long referenceId = reference.getURI().getID();
					String referenceAEName = ODSUtils.getAEName(reference.getClass());
					us.setValue(ValueType.LONG.newValue(referenceAEName, referenceId));
				}
			}

			us.execute();

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	public <T extends Deletable>  List<URI> delete(T dataItem) throws DataAccessException {
		return delete(Collections.singletonList(dataItem));
	}

	public <T extends Deletable> List<URI> delete(List<T> dataItems) throws DataAccessException {

		/**
		 * TODO: because lines below compile it is required to group passed data items by their entity type!!
		 */
		//		Test t = null;
		//		TestStep ts = null;
		//
		//		odsDataProvider.delete(Arrays.asList(t, ts));

		try {

			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}

			ApplElemAccess applElemAccess = aoSession.getApplElemAccess();

			List<URI> list = new ArrayList<URI>();
			for(T dataItem : dataItems) {
				list.add(dataItem.getURI());
			}

			Entity rootEntity = queryService.getEntity(dataItems.get(0).getClass());
			DeleteStatement deleteStatement = new DeleteStatement(applElemAccess, queryService,
					rootEntity, true);
			deleteStatement.addInstances(dataItems);
			return deleteStatement.execute();
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	public List<ChannelValue> readChannelValues(ChannelValuesReadRequest readRequest) throws DataAccessException {
		try {
			ChannelReadRequest channelReadRequest = new ChannelReadRequest(aoSession.getApplElemAccess(), queryService);

			return channelReadRequest.send(readRequest);
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	public List<Channel> writeChannelValues(ChannelValuesWriteRequest writeRequest) throws DataAccessException {
		return writeChannelValues(Collections.singletonList(writeRequest));
	}

	public List<Channel> writeChannelValues(List<ChannelValuesWriteRequest> writeRequests) throws DataAccessException {

		try {
			List<Channel> channels = new ArrayList<Channel>();

			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}

			if(writeRequests.size() <= 0) {
				return new ArrayList<Channel>();
			}

			Entity lcEntity = queryService.getEntity(ChannelInfo.class);

			ApplElemAccess applElemAccess = aoSession.getApplElemAccess();

			ChannelWriteRequest request = new ChannelWriteRequest(applElemAccess, lcEntity);

			for(ChannelValuesWriteRequest writeRequest : writeRequests) {
				ValueType channelValueType = writeRequest.getChannelValue().getDataValues().getValueType().toSingleType();
				int typeEnumValue = ODSUtils.valueType2DataTypeEnum(channelValueType);

				Channel channel = writeRequest.getChannel();
				channel.getValue(Channel.ATTR_VALUE_TYPE).set(typeEnumValue);
				channels.add(channel);

				request.addWriteRequest(writeRequest);
			}
			update(channels);
			List<URI> channelURIs = request.send();

			for(URI uri : channelURIs) {
				System.out.println(uri);
			}

			return channels;

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}

	}

	public void startTransaction() throws DataAccessException {
		try {
			if(transactionActive) {
				throw new DataAccessException("transaction is already active");
			}
			transactionActive = true;
			aoSession.startTransaction();
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	public void commitTransaction() throws DataAccessException {
		try {
			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}
			aoSession.commitTransaction();
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			transactionActive = false;
		}
	}


	public void abortTransaction() throws DataAccessException {
		try {
			if(!transactionActive) {
				throw new DataAccessException("no transaction active");
			}
			aoSession.abortTransaction();
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			transactionActive = false;
		}
	}

	public BaseDataProvider fork() throws DataProviderException {
		try {
			return new ODSDataProvider(aoSession.createCoSession());
		} catch(AoException aoe) {
			throw new DataProviderException(aoe.reason, aoe);
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
			return dataItemsByID.get(id);
		}

		private List<T> getAll() {
			return new ArrayList<>(dataItems);
		}

	}

}
