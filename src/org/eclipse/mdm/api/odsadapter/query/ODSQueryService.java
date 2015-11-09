/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplRel;
import org.asam.ods.ApplicationStructureValue;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.odsadapter.odscache.ODSCache;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSQueryService implements QueryService {
	
	private final Map<String, Entity> entitiesByName = new HashMap<>();
	private final Map<Entity, List<Relation>> relationsByEntity = new HashMap<>();
	private final Map<Entity, Map<Entity, Relation>> relationByEntities = new HashMap<>();
	private final Map<Entity, Map<Relationship, List<Relation>>> relationsByType = new HashMap<>();

	private final ODSCache odsCache;
	
	public ODSQueryService(ODSCache odsCache) {
		this.odsCache = odsCache;
		
		try {
			ApplicationStructureValue asv = odsCache.getAoSession().getApplicationStructureValue();
			
			Map<Long, Entity> entitiesByID = new HashMap<>();
			for(ApplElem applElem : asv.applElems) {
				ODSEntity entity = new ODSEntity(applElem); 
				entitiesByName.put(applElem.aeName, entity);
				entitiesByID.put(ODSUtils.asJLong(applElem.aid), entity);
			}

			for(ApplRel applRel : asv.applRels) {
				Entity source = entitiesByID.get(ODSUtils.asJLong(applRel.elem1));
				Entity target = entitiesByID.get(ODSUtils.asJLong(applRel.elem2));
				Relation relation = new ODSRelation(applRel, source, target);
				
				List<Relation> entityRelationList = relationsByEntity.get(source);
				if(entityRelationList == null) {
					entityRelationList = new ArrayList<>();
					relationsByEntity.put(source, entityRelationList);
				}
				entityRelationList.add(relation);
				
				Map<Entity, Relation> entityRelationMap = relationByEntities.get(source);
				if(entityRelationMap == null) {
					entityRelationMap = new HashMap<>();
					relationByEntities.put(source, entityRelationMap);
				}
				entityRelationMap.put(target, relation);
				
				Map<Relationship, List<Relation>> entityRelationsByType = relationsByType.get(source);
				if(entityRelationsByType == null) {
					entityRelationsByType = new HashMap<>();
					relationsByType.put(source, entityRelationsByType);
				}

				Relationship relationship = ODSUtils.RELATIONSHIPS.revert(applRel.arRelationType);
				List<Relation> relationsByType = entityRelationsByType.get(relationship);
				if(relationsByType == null) {
					relationsByType = new ArrayList<>();
					entityRelationsByType.put(relationship, relationsByType);
				}
				relationsByType.add(relation);
			} 
			
		} catch (AoException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public Query createQuery() {
		return new ODSQuery(odsCache);
	}
	
	@Override
	public Entity getEntity(Class<? extends DataItem> type) {
		return getEntity(ODSUtils.getAEName(type));
	}

	@Override
	public Attribute getAttribute(Class<? extends DataItem> type, String name) {
		return getEntity(type).getAttribute(name);
	}
	
	@Override
	public Entity getEntity(String name) {
		Entity entity = entitiesByName.get(name);
		if(entity == null) {
			throw new IllegalArgumentException("Entity with name '" + name + "' not found.");
		}
		
		return entity;
	}

	@Override
	public Relation getRelation(Entity source, Entity target) {
		Relation relation = relationByEntities.get(source).get(target);
		if(relation == null) {
			throw new IllegalArgumentException("relation from '" + source + "' to '" + target + "' does not exist!");
		}
		return relation;
	}

	@Override
	public List<Relation> getRelations(Entity source, Relationship relationship) {
		Map<Relationship, List<Relation>> entityRelations = relationsByType.get(source);
		if(entityRelations == null) {
			throw new IllegalArgumentException("Relations for passed entity '" + source + "' not found.");
		}
				
		List<Relation> relations = entityRelations.get(relationship);
		if(relations == null) {
			throw new IllegalArgumentException("Relations of type '" + relationship + "' not found for entity '" + source + "'.");
		}
		
		return relations;
	}

	@Override
	public List<Relation> getRelations(Entity entity) {
		List<Relation> relations = relationsByEntity.get(entity);
		if(relations == null) {
			throw new IllegalArgumentException("Relations for passed entity '" + entity + "' not found.");
		}
		
		return relations;
	}

	@Override
	public SearchQuery getSearchQuery(String name) {		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		entitiesByName.clear();
		relationsByEntity.clear();
		relationByEntities.clear();
		relationsByType.clear();
	}

}
