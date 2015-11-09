/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static org.eclipse.mdm.api.base.model.ChannelInfo.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mdm.api.base.model.ChannelInfo;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.utils.DataItemCache;

public final class QueryProvider {
	
	private Map<Class<? extends DataItem>, Query> queriesByType = new HashMap<Class<? extends DataItem>, Query>();
	private final QueryService queryService;
	private final DataItemCache dataItemCache;
	
	public QueryProvider(QueryService queryService, DataItemCache dataItemCache) {
		this.queryService = queryService;
		this.dataItemCache = dataItemCache;
	}
	
	public Query getQuery(Class<? extends DataItem> type) {
		Query query = this.queriesByType.get(type);
		if(query == null) {
			return this.queryService.createQuery().selectAll(this.queryService.getEntity(type));
		}
		return query.clone();	
	}
	
	public void addDefaultQuery(Class<? extends DataItem> source, Class<?>... targets) {
		this.queriesByType.put(source, createQuery(source, targets));
	}

	@SuppressWarnings("unchecked")
	private Query createQuery(Class<? extends DataItem> source, Class<?>... targets) {
		Entity sourceEntity = this.queryService.getEntity(source);
		Query query = this.queryService.createQuery().selectAll(sourceEntity);
		
		for(Class<?> target : targets) {
			Entity targetEntity = this.queryService.getEntity((Class<? extends DataItem>)target);
			if(this.dataItemCache.isCached(targetEntity)) {
				query.selectID(targetEntity);
			} else {
				if(target.equals(ChannelInfo.class)) {
					query.select(targetEntity, ATTR_ID, ATTR_NAME, ATTR_MIMETYPE, ATTR_INDEPENDENT, 
							ATTR_SEQUENCE_REPRESENTATION, ATTR_AXISTYPE);
				} else {
					query.selectAll(targetEntity);
				}
			}
			query.join(this.queryService.getRelation(sourceEntity, targetEntity));
		}
		
		return query;		
	}
	
}
