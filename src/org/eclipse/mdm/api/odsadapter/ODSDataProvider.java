/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.InstanceElement;
import org.eclipse.mdm.api.base.BaseDataProvider;
import org.eclipse.mdm.api.base.DataProviderException;
import org.eclipse.mdm.api.base.marker.ContextDescribable;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ChannelInfo;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.utils.DataItemCache;
import org.eclipse.mdm.api.odsadapter.odscache.ODSCache;
import org.eclipse.mdm.api.odsadapter.query.ODSQueryService;
import org.eclipse.mdm.api.odsadapter.query.QueryProvider;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSDataProvider implements BaseDataProvider {

	private final ODSCache odsCache;
	private final DataItemCache dataItemCache = new DataItemCache();
	private final ODSQueryService queryService;
	private final QueryProvider queryProvider;
	private final Environment env;
	private final User loggedOnUser;
		
	public ODSDataProvider(ODSCache odsCache) throws DataAccessException {
		this.odsCache = odsCache;

		this.queryService = new ODSQueryService(odsCache);
		this.queryProvider = new QueryProvider(this.queryService, this.dataItemCache);

		this.env = resolveEnvironment();
		initialize();
		
		this.loggedOnUser = resolveLoggedOnUser();
	}
	
	@Override
	public QueryService getQueryService() {
		return this.queryService;
	}
	
	@Override
	public Environment getEnvironment() throws DataAccessException {
		return this.env;
	}
	
	@Override
	public User getLoggedOnUser() throws DataAccessException {
		return this.loggedOnUser;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataItem> T findByURI(URI uri) throws DataAccessException {
		return (T) findByID(ODSUtils.getClass(uri.getTypeName()), uri.getId());
	}
	
	@Override
	public <T extends DataItem> T findByID(Class<T> type, Long id) throws DataAccessException {
		Entity entity = queryService.getEntity(type);
		if(this.dataItemCache.isCached(entity)) {
			return this.dataItemCache.getCached(entity, id);
		}

		List<Result> results = this.queryProvider.getQuery(type).fetch(Filter.id(entity, id));
		List<T> dataItems = createDataItemResults(type, entity, results);
		return dataItems.isEmpty() ? null : dataItems.get(0);
	}
	
	@Override
	public <T extends DataItem> List<T> list(Class<T> type) throws DataAccessException {
		Entity entity = this.queryService.getEntity(type);
		
		if(this.dataItemCache.isCached(entity)) {
			return this.dataItemCache.getAllCached(entity);
		}
				
		Query query = this.queryProvider.getQuery(type);	
		
		
		List<Result> results = query.fetch();		
		return createDataItemResults(type, entity, results);
	}

	@Override
	public <T extends DataItem> List<T> listChildren(DataItem parent, Class<T> type) 
		throws DataAccessException {
		
		Entity parentEntity = this.queryService.getEntity(parent.getURI().getTypeName());
		Entity entity = this.queryService.getEntity(type);
					
		Query query = this.queryProvider.getQuery(type);
		
		if(parent instanceof ChannelGroup && type.equals(Channel.class)) {
			
			Entity measurementEntity = this.queryService.getEntity(Measurement.class);
			Relation channelGroup2Measurement = this.queryService.getRelation(parentEntity, measurementEntity);
			Relation measurement2Channel = this.queryService.getRelation(measurementEntity, entity);
			
			query.join(channelGroup2Measurement);
			query.join(measurement2Channel);
						
		} else {
			Relation relation = this.queryService.getRelation(parentEntity, entity);			
			query.join(relation);
		}		
		
		List<Result> results = query.fetch(Filter.id(parentEntity, parent.getURI().getId()));				
		return createDataItemResults(type, entity, results);		
	}
	
	
	@Override
	public Map<ContextType, ContextRoot> getContextData(ContextDescribable contextDescribable, ContextType... contextTypes) throws DataAccessException {
		
		DataItem dataItem = (DataItem) contextDescribable;
		Entity parentEntity = queryService.getEntity(dataItem.getURI().getTypeName());
		Query query = this.queryService.createQuery();
		
		Map<Entity, List<Entity>> genericModelMap = new HashMap<Entity, List<Entity>>();
		for(ContextType contextType : contextTypes.length == 0 ? ContextType.values() : contextTypes) {				
			Entity entityContextRoot = queryService.getEntity(ODSUtils.getAEName(contextType));
			query.selectAll(entityContextRoot);
			query.join(queryService.getRelation(parentEntity, entityContextRoot));
			
			List<Entity> genericEntities = new ArrayList<>();
			for(Relation relation : queryService.getRelations(entityContextRoot, Relationship.FATHER_CHILD)) {
				query.selectAll(relation.getTarget());
				query.join(relation, Join.OUTER);
				genericEntities.add(relation.getTarget());
			}
			genericModelMap.put(entityContextRoot, genericEntities);
			
			/**
			 * TODO: Add relations to sensors...
			 */
		}
		
		List<Result> results = query.fetch(Filter.id(parentEntity, dataItem.getURI().getId()));
		return generateGenericDataItemStructure(results, genericModelMap);
	}
	
	public void close() throws DataProviderException {
		try { 
			AoSession aoSession = odsCache.getAoSession();
			this.odsCache.clearODSCache();		
			aoSession.close();
		} catch(AoException aoe) {
			throw new DataProviderException(aoe.reason, aoe);
		} finally {
			queryService.clear();
			dataItemCache.clear();
		}
	}
	
	private Environment resolveEnvironment() throws DataAccessException {
		Entity entity = this.queryService.getEntity(Environment.class);
	    Result result = queryProvider.getQuery(Environment.class).fetchSingleton();		
	    Map<String, Value> values = result.getRecord(entity).getValues();
	    return createDataItemInstance(Environment.class, entity, values);
	}
		
	private User resolveLoggedOnUser() throws DataAccessException {
		
		InstanceElement ieUser = null;
		try {
			ieUser = this.odsCache.getAoSession().getUser();
			return this.dataItemCache.getCached(queryService.getEntity(User.class), ODSUtils.asJLong(ieUser.getId()));			
		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		} finally {
			ODSUtils.destroyInstanceElement(ieUser);
		}
	}
	
	private Map<ContextType, ContextRoot> generateGenericDataItemStructure(List<Result> results, 
			Map<Entity, List<Entity>> map) throws DataAccessException {
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
	
	private <T extends DataItem> List<T> createDataItemResults(Class<T> type, Entity entity, List<Result> results)
		throws DataAccessException {
		
		List<T> dataItems = new ArrayList<T>();
		
		for(Result result : results) {
			Map<Class<? extends DataItem>, DataItem> references = new HashMap<Class<? extends DataItem>, DataItem>();			
			Record record = result.removeRecord(entity);
			
			for(Record relatedRecord : result) {
				Class<? extends DataItem> clazz = ODSUtils.getClass(relatedRecord.getEntity().getName());
				Entity relatedEntity = relatedRecord.getEntity();
				
				if(this.dataItemCache.isCached(relatedEntity)) {
					references.put(clazz, dataItemCache.getCached(relatedEntity, relatedRecord.getID()));
				} else {
					references.put(clazz, createDataItemInstance(clazz,relatedRecord.getEntity(), relatedRecord.getValues()));
				}
			}			
			dataItems.add(createDataItemInstance(type, entity, record.getValues(), references));			
		}		
		return dataItems;
	}
	
	private <T extends DataItem> T createDataItemInstance(Class<T> type, Entity entity,	Map<String, Value> values) 
		throws DataAccessException {
		return createDataItemInstance(type, entity, values, new HashMap<Class<? extends DataItem>, DataItem>());
	}
	
	private <T extends DataItem> T createDataItemInstance(Class<T> type, Entity entity, 
		Map<String, Value> values, Map<Class<? extends DataItem>, DataItem> references) throws DataAccessException {
		
		Constructor<T> constructor = null;
		try {
			
			String typeName = entity.getName();	
						
			if(type.equals(Environment.class)) {			
				constructor = type.getDeclaredConstructor(Map.class, Map.class);
				constructor.setAccessible(true);			
				return constructor.newInstance(values, ODSUtils.getContext(this.odsCache));
			} else {		
				long id = values.remove(DataItem.ATTR_ID).getValue();			
				URI uri = new URI(getEnvironment().getName(), typeName, id);
				constructor = type.getDeclaredConstructor(Map.class, URI.class, Map.class);
				constructor.setAccessible(true);
				return constructor.newInstance(values, uri, references);		
			}
			
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

	private void initialize() throws DataAccessException {
		// define default queries for cached data items with dependencies below
		queryProvider.addDefaultQuery(Unit.class, PhysicalDimension.class);
		queryProvider.addDefaultQuery(Quantity.class, Unit.class);
		
		// load cached entries.
		this.dataItemCache.cache(queryService.getEntity(Unit.class), list(Unit.class));
		this.dataItemCache.cache(queryService.getEntity(Quantity.class), list(Quantity.class));
		this.dataItemCache.cache(queryService.getEntity(PhysicalDimension.class), list(PhysicalDimension.class));
		this.dataItemCache.cache(queryService.getEntity(User.class), list(User.class));
		
		// define further queries, especially those depending on cached data items
		queryProvider.addDefaultQuery(Test.class, User.class);		
		queryProvider.addDefaultQuery(Channel.class, Unit.class, Quantity.class, ChannelInfo.class);		
	}
	
}
