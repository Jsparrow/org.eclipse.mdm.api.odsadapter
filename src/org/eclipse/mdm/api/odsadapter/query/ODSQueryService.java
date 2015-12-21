/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplRel;
import org.asam.ods.ApplicationStructureValue;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSQueryService implements QueryService {

	private final Map<Class<? extends DataItem>, DataItemQueryConfig> dataItemQueryConfigs = new HashMap<>();

	private final Map<String, ODSEntity> entitiesByName = new HashMap<>();
	private final Map<Long, ODSEntity> entitiesByID = new HashMap<>();

	private final DataItemFactory dataItemFactory;

	private final AoSession aoSession;

	private ApplElemAccess applElemAccess;

	public ODSQueryService(AoSession aoSession, DataItemFactory dataItemFactory) throws AoException {
		this.aoSession = aoSession;
		this.dataItemFactory = dataItemFactory;

		loadApplicationModel();

		// NOTE: Relations are expected to have 1:1 cardinality!
		defineDataItemQueryConfiguration(Unit.class, PhysicalDimension.class);
		defineDataItemQueryConfiguration(Quantity.class, Unit.class);
		defineDataItemQueryConfiguration(Test.class, User.class);
		defineDataItemQueryConfiguration(Channel.class, Unit.class, Quantity.class);
	}

	public Query createQuery(Class<? extends DataItem> type) {
		DataItemQueryConfig dataItemQueryConfig = dataItemQueryConfigs.getOrDefault(type, new DataItemQueryConfig(type));
		Entity entity = dataItemQueryConfig.getEntity();

		Query query = createQuery().selectAll(entity);
		for(Class<? extends DataItem> relatedType : dataItemQueryConfig) {
			Entity relatedEntity = getEntity(relatedType);
			if(dataItemFactory.isCached(relatedType)) {
				query.selectID(relatedEntity);
			} else {
				query.selectAll(relatedEntity);
			}
			query.join(entity, relatedEntity);
		}

		return query;
	}

	@Override
	public Query createQuery() {
		return new ODSQuery(this);
	}

	@Override
	public Entity getEntity(Class<? extends DataItem> type) {
		return getEntity(ODSUtils.getAEName(type));
	}

	@Override
	public Entity getEntity(ContextType contextType) {
		if(ContextType.UNITUNDERTEST.equals(contextType)) {
			return getEntity("UnitUnderTest");
		} else if(ContextType.TESTSEQUENCE.equals(contextType)) {
			return getEntity("TestSequence");
		} else if(ContextType.TESTEQUIPMENT.equals(contextType)) {
			return getEntity("TestEquipment");
		}

		throw new IllegalArgumentException("Unknown context type '" + contextType + "' passed.");
	}

	@Override
	public Entity getEntity(String name) {
		Entity entity = entitiesByName.get(name);
		if(entity == null) {
			throw new IllegalArgumentException("Entity with name '" + name + "' not found.");
		}

		return entity;
	}

	Entity getEntity(Long id) {
		Entity entity = entitiesByID.get(id);
		if(entity == null) {
			throw new IllegalArgumentException("Entity with id '" + id + "' not found.");
		}

		return entity;
	}

	ApplElemAccess getApplElemAccess() throws AoException {
		if(applElemAccess == null) {
			applElemAccess = aoSession.getApplElemAccess();
		}

		return applElemAccess;
	}

	DataItemFactory getDataItemFactory() {
		return dataItemFactory;
	}

	@Deprecated
	List<Entity> getImplicitEntities(Class<? extends DataItem> type) {
		return dataItemQueryConfigs.getOrDefault(type, new DataItemQueryConfig(type)).getEntities();
	}

	private void defineDataItemQueryConfiguration(Class<? extends DataItem> type, Class<?>... relatedTypes) {
		dataItemQueryConfigs.put(type, new DataItemQueryConfig(type, collectEntities(relatedTypes)));
	}

	@SuppressWarnings("unchecked")
	private List<Class<? extends DataItem>> collectEntities(Class<?>... types) {
		return Arrays.stream(types).filter(DataItem.class::isAssignableFrom)
				.map(t -> (Class<? extends DataItem>) t).collect(toList());
	}

	private void loadApplicationModel() throws AoException {
		ApplicationStructureValue asv = aoSession.getApplicationStructureValue();

		for(ApplElem applElem : asv.applElems) {
			ODSEntity entity = new ODSEntity(applElem);
			entitiesByName.put(applElem.aeName, entity);
			entitiesByID.put(ODSConverter.fromODSLong(applElem.aid), entity);
		}

		List<Relation> relations = new ArrayList<>();
		for(ApplRel applRel : asv.applRels) {
			Entity source = entitiesByID.get(ODSConverter.fromODSLong(applRel.elem1));
			Entity target = entitiesByID.get(ODSConverter.fromODSLong(applRel.elem2));
			relations.add(new ODSRelation(applRel, source, target));
		}

		relations.stream().collect(groupingBy(Relation::getSource)).forEach((e, r) -> ((ODSEntity) e).setRelations(r));
	}

	private final class DataItemQueryConfig implements Iterable<Class<? extends DataItem>> {

		/*
		 * TODO It might be required to define a join definition -> current implementation produces only INNER joins!
		 */

		private final List<Class<? extends DataItem>> relatedTypes;
		private final Class<? extends DataItem> type;

		private DataItemQueryConfig(Class<? extends DataItem> type, List<Class<? extends DataItem>> relatedTypes) {
			this.relatedTypes = relatedTypes;
			this.type = type;
		}

		private DataItemQueryConfig(Class<? extends DataItem> type) {
			relatedTypes = Collections.emptyList();
			this.type = type;
		}

		private Entity getEntity() {
			return ODSQueryService.this.getEntity(type);
		}

		private List<Entity> getEntities() {
			List<Entity> entities = StreamSupport.stream(spliterator(), false)
					.map(ODSQueryService.this::getEntity).collect(Collectors.toList());
			entities.add(getEntity());
			return entities;
		}

		@Override
		public Iterator<Class<? extends DataItem>> iterator() {
			return relatedTypes.iterator();
		}

	}

}
