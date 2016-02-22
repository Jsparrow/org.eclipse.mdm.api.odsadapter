/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplRel;
import org.asam.ods.ApplicationStructureValue;
import org.asam.ods.EnumerationAttributeStructure;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSEnumerations;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class ODSModelManager implements ModelManager {

	// unfortunately we can not share this scheduler with other instances since
	// we have to shutdown the scheduler executor service and at this point we
	// do not know when it is safe to so
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final Map<Class<? extends DataItem>, DataItemQueryConfig> dataItemQueryConfigs = new HashMap<>();

	private final Map<String, ODSEntityType> entityTypesByName = new HashMap<>();
	private final Map<Long, ODSEntityType> entityTypesByID = new HashMap<>();

	private final DataItemFactory dataItemFactory;

	private final ScheduledFuture<?> sessionRefresh;

	private final AoSession aoSession;

	private final ApplElemAccess applElemAccess;

	public ODSModelManager(AoSession aoSession, DataItemFactory dataItemFactory) throws AoException {
		this.aoSession = aoSession;
		this.dataItemFactory = dataItemFactory;
		applElemAccess = aoSession.getApplElemAccess();

		loadApplicationModel();

		// NOTE: Relations are expected to have 1:1 cardinality!
		configureDataItemQuery(Unit.class, PhysicalDimension.class);
		configureDataItemQuery(Quantity.class, Unit.class);
		configureDataItemQuery(Test.class, User.class);

		// TODO Channel has an optional relation to sensors!!! -> add outer joins to each sensor?!
		configureDataItemQuery(Channel.class, Unit.class, Quantity.class);

		sessionRefresh = scheduler.scheduleAtFixedRate(this::refreshSession, 5, 5, TimeUnit.MINUTES);
	}

	public Query createQuery(Class<? extends DataItem> type) {
		DataItemQueryConfig dataItemQueryConfig = dataItemQueryConfigs.getOrDefault(type, new DataItemQueryConfig(type));
		EntityType entityType = dataItemQueryConfig.getEntityType();

		Query query = createQuery().selectAll(entityType);
		for(Class<? extends DataItem> relatedType : dataItemQueryConfig) {
			EntityType relatedEntityType = getEntityType(relatedType);
			if(dataItemFactory.isCached(relatedType)) {
				query.selectID(relatedEntityType);
			} else {
				query.selectAll(relatedEntityType);
			}
			query.join(entityType, relatedEntityType);
		}

		return query;
	}

	@Override
	public Query createQuery() {
		return new ODSQuery(this);
	}

	@Override
	public EntityType getEntityType(Class<? extends DataItem> type) {
		return getEntityType(ODSUtils.getAEName(type));
	}

	@Override
	public EntityType getEntityType(ContextType contextType) {
		if(ContextType.UNITUNDERTEST.equals(contextType)) {
			return getEntityType("UnitUnderTest");
		} else if(ContextType.TESTSEQUENCE.equals(contextType)) {
			return getEntityType("TestSequence");
		} else if(ContextType.TESTEQUIPMENT.equals(contextType)) {
			return getEntityType("TestEquipment");
		}

		throw new IllegalArgumentException("Unknown context type '" + contextType + "' passed.");
	}

	@Override
	public EntityType getEntityType(String name) {
		EntityType entityType = entityTypesByName.get(name);
		if(entityType == null) {
			throw new IllegalArgumentException("Entity with name '" + name + "' not found.");
		}

		return entityType;
	}

	EntityType getEntityType(Long id) {
		EntityType entityType = entityTypesByID.get(id);
		if(entityType == null) {
			throw new IllegalArgumentException("Entity with id '" + id + "' not found.");
		}

		return entityType;
	}

	public AoSession getAoSession() {
		return aoSession;
	}

	@Deprecated
	public ApplElemAccess getApplElemAccess() {
		return applElemAccess;
	}

	@Deprecated
	public List<EntityType> getImplicitEntityTypes(Class<? extends DataItem> type) {
		return dataItemQueryConfigs.getOrDefault(type, new DataItemQueryConfig(type)).getEntityTypes();
	}

	public void close() throws AoException {
		try {
			sessionRefresh.cancel(true);
			aoSession.close();
		} finally {
			System.err.println("SESSION CLOSED: " + aoSession.toString());
			scheduler.shutdown();
		}
	}

	private void configureDataItemQuery(Class<? extends DataItem> type, Class<?>... relatedTypes) {
		dataItemQueryConfigs.put(type, new DataItemQueryConfig(type, collectEntityTypes(relatedTypes)));
	}

	@SuppressWarnings("unchecked")
	private List<Class<? extends DataItem>> collectEntityTypes(Class<?>... types) {
		return Arrays.stream(types).filter(DataItem.class::isAssignableFrom)
				.map(t -> (Class<? extends DataItem>) t).collect(toList());
	}

	private void loadApplicationModel() throws AoException {
		// enumeration mappings (aeID -> (aaName -> enumClass))
		Map<Long, Map<String, Class<? extends Enum<?>>>> enumClassMap = new HashMap<>();
		for(EnumerationAttributeStructure eas : aoSession.getEnumerationAttributes()) {
			enumClassMap.computeIfAbsent(ODSConverter.fromODSLong(eas.aid), k -> new HashMap<>())
			.put(eas.aaName, ODSEnumerations.getEnumClass(eas.enumName));
		}

		// create entity types (incl. attributes)
		ApplicationStructureValue asv = aoSession.getApplicationStructureValue();
		String sourceName = aoSession.getName();
		for(ApplElem applElem : asv.applElems) {
			Long odsID = ODSConverter.fromODSLong(applElem.aid);
			ODSEntityType entityType = new ODSEntityType(sourceName, applElem, enumClassMap.getOrDefault(odsID, new HashMap<>()));
			entityTypesByName.put(applElem.aeName, entityType);
			entityTypesByID.put(odsID, entityType);
		}

		// create relations
		List<Relation> relations = new ArrayList<>();
		for(ApplRel applRel : asv.applRels) {
			EntityType source = entityTypesByID.get(ODSConverter.fromODSLong(applRel.elem1));
			EntityType target = entityTypesByID.get(ODSConverter.fromODSLong(applRel.elem2));
			relations.add(new ODSRelation(applRel, source, target));
		}

		// assign relations to their source entity types
		relations.stream().collect(groupingBy(Relation::getSource)).forEach((e, r) -> ((ODSEntityType) e).setRelations(r));
	}

	private void refreshSession() {
		try {
			// TODO add logging...
			System.err.println("SESSION REFRESHED: " + aoSession.toString());
			aoSession.getName();
		} catch(AoException e) {
			sessionRefresh.cancel(false);
		}
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

		private EntityType getEntityType() {
			return ODSModelManager.this.getEntityType(type);
		}

		private List<EntityType> getEntityTypes() {
			List<EntityType> entityTypes = StreamSupport.stream(spliterator(), false)
					.map(ODSModelManager.this::getEntityType).collect(Collectors.toList());
			entityTypes.add(getEntityType());
			return entityTypes;
		}

		@Override
		public Iterator<Class<? extends DataItem>> iterator() {
			return relatedTypes.iterator();
		}

	}

}
