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
import org.eclipse.mdm.api.base.model.Entity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODSModelManager implements ModelManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSModelManager.class);

	// entity type name -> query configuration
	private final Map<String, EntityQueryConfig> entityQueryConfigs = new HashMap<>();

	private final Map<String, ODSEntityType> entityTypesByName = new HashMap<>();
	private final Map<Long, ODSEntityType> entityTypesByID = new HashMap<>();

	private final DataItemFactory dataItemFactory;

	private final AoSession aoSession;

	private final ApplElemAccess applElemAccess;

	public ODSModelManager(AoSession aoSession, DataItemFactory dataItemFactory) throws AoException {
		this.aoSession = aoSession;
		this.dataItemFactory = dataItemFactory;
		applElemAccess = aoSession.getApplElemAccess();

		loadApplicationModel();

		// NOTE: Relations are expected to have 1:1 cardinality!
		configureEntityQuery(Unit.class, PhysicalDimension.class);
		configureEntityQuery(Quantity.class, Unit.class);
		configureEntityQuery(Test.class, User.class);

		// TODO Channel has an optional relation to sensors! -> add outer joins to each sensor?!
		configureEntityQuery(Channel.class, Unit.class, Quantity.class);
	}

	public Query createQuery(Class<? extends Entity> type) {
		String typeName = getEntityType(type).getName();
		EntityQueryConfig entityQueryConfig = entityQueryConfigs.getOrDefault(typeName, new EntityQueryConfig(typeName));
		EntityType entityType = entityQueryConfig.getEntityType();

		Query query = createQuery().selectAll(entityType);
		for(Class<? extends Entity> relatedType : entityQueryConfig) {
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
	public EntityType getEntityType(Class<? extends Entity> type) {
		return getEntityType(ODSUtils.getAEName(type));
	}

	@Override
	public EntityType getEntityType(ContextType contextType) {
		if(contextType.isUnitUnderTest()) {
			return getEntityType("UnitUnderTest");
		} else if(contextType.isTestSequence()) {
			return getEntityType("TestSequence");
		} else if(contextType.isTestEquipment()) {
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
	public List<EntityType> getImplicitEntityTypes(Class<? extends Entity> type) {
		String typeName = getEntityType(type).getName();
		return entityQueryConfigs.getOrDefault(typeName, new EntityQueryConfig(typeName)).getEntityTypes();
	}

	@Deprecated
	public List<EntityType> getRelatedTypes(String typeName) {
		return entityQueryConfigs.getOrDefault(typeName, new EntityQueryConfig(typeName)).getRelatedTypes();
	}

	public void close() throws AoException {
		aoSession.close();
	}

	private void configureEntityQuery(Class<? extends Entity> type, Class<?>... relatedTypes) {
		String typeName = getEntityType(type).getName();
		entityQueryConfigs.put(typeName, new EntityQueryConfig(typeName, collectEntityTypes(relatedTypes)));
	}

	@SuppressWarnings("unchecked")
	private List<Class<? extends Entity>> collectEntityTypes(Class<?>... types) {
		return Arrays.stream(types).filter(Entity.class::isAssignableFrom)
				.map(t -> (Class<? extends Entity>) t).collect(toList());
	}

	private void loadApplicationModel() throws AoException {
		LOGGER.debug("Reading the application model...");
		long start = System.currentTimeMillis();
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

		long stop = System.currentTimeMillis();
		LOGGER.debug("{} entity types with {} relations found in {} ms.", entityTypesByName.size(), relations.size(), stop - start);
	}

	private final class EntityQueryConfig implements Iterable<Class<? extends Entity>> {

		/*
		 * TODO It might be required to define a join definition -> current implementation produces only INNER joins!
		 */

		private final List<Class<? extends Entity>> relatedTypes;
		private final String typeName;

		private EntityQueryConfig(String typeName, List<Class<? extends Entity>> relatedTypes) {
			this.relatedTypes = relatedTypes;
			this.typeName = typeName;
		}

		private EntityQueryConfig(String typeName) {
			relatedTypes = Collections.emptyList();
			this.typeName = typeName;
		}

		private EntityType getEntityType() {
			return ODSModelManager.this.getEntityType(typeName);
		}

		private List<EntityType> getEntityTypes() {
			List<EntityType> entityTypes = getRelatedTypes();
			entityTypes.add(getEntityType());
			return entityTypes;
		}

		private List<EntityType> getRelatedTypes() {
			List<EntityType> entityTypes = StreamSupport.stream(spliterator(), false)
					.map(ODSModelManager.this::getEntityType).collect(Collectors.toList());
			return entityTypes;
		}

		@Override
		public Iterator<Class<? extends Entity>> iterator() {
			return relatedTypes.iterator();
		}

	}

}
