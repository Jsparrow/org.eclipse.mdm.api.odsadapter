/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_ENUMERATION_CLASS;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SCALAR_TYPE;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SEQUENCE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.InstanceElement;
import org.eclipse.mdm.api.base.ConnectionException;
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
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.Condition;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.DefaultEntityCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateRoot;
import org.eclipse.mdm.api.dflt.model.TemplateTest;
import org.eclipse.mdm.api.dflt.model.TemplateTestStep;
import org.eclipse.mdm.api.dflt.model.TemplateTestStepUsage;
import org.eclipse.mdm.api.dflt.model.ValueList;
import org.eclipse.mdm.api.dflt.model.ValueListValue;
import org.eclipse.mdm.api.dflt.model.Versionable;
import org.eclipse.mdm.api.odsadapter.query.DataItemFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.EntityLoader;
import org.eclipse.mdm.api.odsadapter.search.ODSSearchService;
import org.eclipse.mdm.api.odsadapter.transaction.ODSTransaction;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODSEntityManager implements EntityManager, DataItemFactory, EntityLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSEntityManager.class);

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
			throw new ConnectionException("Unable to load application model due to: " + e.reason, e);
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

	//	@Override
	//	public List<Status> loadStatus(Class<? extends StatusAttachable> type) throws DataAccessException {
	//		return loadStatusByPattern(type, "*");
	//	}
	//
	//	@Override
	//	public Optional<Status> loadStatus(Class<? extends StatusAttachable> type, String name) throws DataAccessException {
	//		List<Status> status = loadStatusByPattern(type, name);
	//		if(status.size() > 1) {
	//			throw new DataAccessException("Multiple status entities with name '" + name + "' found.");
	//		}
	//
	//		return status.isEmpty() ? Optional.empty() : Optional.of(status.get(0));
	//	}
	//
	//	private List<Status> loadStatusByPattern(Class<? extends StatusAttachable> type, String pattern) throws DataAccessException {
	//		EntityType statusEntityType;
	//
	//		if(Test.class.equals(type)) {
	//			statusEntityType = modelManager.getEntityType("StatusTest");
	//		} else if(TestStep.class.equals(type)) {
	//			// StatusTestStep
	//			statusEntityType = modelManager.getEntityType("StatusTestStep");
	//		} else {
	//			throw new IllegalArgumentException("Given type '" + type.getSimpleName() + "' is not supported.");
	//		}
	//
	//		List<Status> status = new ArrayList<>();
	//		for(Result result : modelManager.createQuery().selectAll(statusEntityType).fetch(Filter.nameOnly(statusEntityType, pattern))) {
	//			status.add(createEntity(Status.class, new DefaultEntityCore(result.getRecord(statusEntityType))));
	//		}
	//
	//		return status;
	//	}

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
		if(isCached(type)) {
			EntityCache<T> cache = (EntityCache<T>) entityCacheByType.get(type);
			if(cache == null) {
				cache = new EntityCache<T>(createEntities(type, modelManager.createQuery(type).fetch()));
				entityCacheByType.put(type, cache);
			}

			return cache.getAll();
		} else {
			return loadAll(type, "*");
		}
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> type, String pattern) throws DataAccessException {
		return loadAll(type, Filter.nameOnly(modelManager.getEntityType(type), pattern));
	}

	//	@Override
	//	public <T extends StatusAttachable> List<T> loadAll(Class<T> type, Status status, String pattern) throws DataAccessException {
	//		EntityType entityType = modelManager.getEntityType(type);
	//		EntityType statusEntityType = modelManager.getEntityType(status.getURI().getTypeName());
	//
	//		// query instance IDs
	//		List<Long> instanceIDs = modelManager.createQuery()
	//				.join(entityType, statusEntityType).selectID(entityType)
	//				.fetch(Filter.and()
	//						.id(statusEntityType, status.getURI().getID())
	//						.name(entityType, pattern))
	//				.stream().map(r -> r.getRecord(entityType)).map(Record::getID).collect(Collectors.toList());
	//
	//		if(instanceIDs.isEmpty()) {
	//			return Collections.emptyList();
	//		}
	//
	//		return loadAll(type, Filter.idsOnly(entityType, instanceIDs));
	//	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> type, Collection<Long> instanceIDs) throws DataAccessException {
		return loadAll(type, Filter.idsOnly(modelManager.getEntityType(type), instanceIDs));
	}

	@SuppressWarnings("unchecked")
	private <T extends Entity> List<T> loadAll(Class<T> type, Filter filter) throws DataAccessException {
		/*
		 * TODO delegate to custom implementations ....
		 *
		 * 1. custom load methods for known types
		 * 2. custom types known by modules
		 * 3. try default query
		 */

		//		if(StatusAttachable.class.isAssignableFrom(type)) {
		//			return (List<T>) loadAllStatusAttachables((Class<StatusAttachable>) type, filter);
		//		} else
		if(ValueList.class.equals(type)) {
			return (List<T>) loadValueLists(filter);
		} else if(TemplateTestStep.class.equals(type)) {
			return (List<T>) loadTemplateTestSteps(filter);
		} else if(TemplateTest.class.equals(type)) {
			return (List<T>) loadTemplateTests(filter);
		}

		// TODO: do we have to throw an exception in case of an illegal
		// type like ParameterSet or Parameter or children of types above?

		return createEntities(type, modelManager.createQuery(type).fetch(filter));
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> type, ContextType contextType) throws DataAccessException {
		return loadAll(type, contextType, "*");
	}

	@Override
	public <T extends Entity> List<T> loadAll(Class<T> type, ContextType contextType, String pattern) throws DataAccessException {
		EntityType entityType;
		if(CatalogComponent.class.equals(type)) {
			entityType = getEntityType(contextType, "Cat", "Comp");
		} else if(TemplateRoot.class.equals(type)) {
			entityType = getEntityType(contextType, "Tpl", "Root");
		} else {
			throw new IllegalArgumentException("Given type '" + type.getSimpleName() + "' is not supported.");
		}

		return loadAll(type, contextType, Filter.nameOnly(entityType, pattern));
	}

	@SuppressWarnings("unchecked")
	private <T extends Entity> List<T> loadAll(Class<T> type, ContextType contextType, Filter filter) throws DataAccessException {
		if(CatalogComponent.class.equals(type)) {
			return (List<T>) loadCatalogComponents(contextType, filter);
		} else if(TemplateRoot.class.equals(type)) {
			return (List<T>) loadTemplateRoots(contextType, filter);
		}

		throw new IllegalArgumentException("Given type '" + type.getSimpleName() + "' is not supported.");
	}

	@Override
	public <T extends Entity> List<T> loadChildren(Entity parent, Class<T> type, String pattern) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType childEntityType = modelManager.getEntityType(type);
		Query query = modelManager.createQuery(type);

		/*
		 * TODO exceptional case for parameter sets and context root
		 */

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

	//	@Override
	//	public <T extends StatusAttachable> List<T> loadChildren(Entity parent, Class<T> type, Status status, String pattern) throws DataAccessException {
	//		EntityType parentEntityType = modelManager.getEntityType(parent);
	//		EntityType childEntityType = modelManager.getEntityType(type);
	//		EntityType statusEntityType = modelManager.getEntityType(status.getURI().getTypeName());
	//
	//		// query instance IDs
	//		List<Long> instanceIDs = modelManager.createQuery()
	//				.join(childEntityType, parentEntityType, statusEntityType)
	//				.selectID(childEntityType)
	//				.fetch(Filter.and()
	//						.id(parentEntityType, parent.getURI().getID())
	//						.id(statusEntityType, status.getURI().getID())
	//						.name(childEntityType, pattern))
	//				.stream().map(r -> r.getRecord(childEntityType)).map(Record::getID).collect(Collectors.toList());
	//
	//		if(instanceIDs.isEmpty()) {
	//			return Collections.emptyList();
	//		}
	//
	//		return loadAll(type, Filter.idsOnly(childEntityType, instanceIDs));
	//	}

	@Override
	public <T extends Versionable> Optional<T> loadLatestValid(Class<T> type, String name) throws DataAccessException {
		/*
		 * TODO: default implementation should work (slow)
		 */
		return EntityManager.super.loadLatestValid(type, name);
	}

	//	@Deprecated // TODO: replace with a generic implementation...
	//	private <T extends StatusAttachable> List<T> loadAllStatusAttachables(Class<T> type, Filter filter) throws DataAccessException {
	//		EntityType statusEntityType;
	//
	//		if(Test.class.equals(type)) {
	//			statusEntityType = modelManager.getEntityType("StatusTest");
	//		} else if(TestStep.class.equals(type)) {
	//			statusEntityType = modelManager.getEntityType("StatusTestStep");
	//		} else {
	//			throw new IllegalArgumentException(); // TODO
	//		}
	//
	//		EntityType entityType = modelManager.getEntityType(type);
	//		Relation statusRelation = entityType.getRelation(statusEntityType);
	//
	//		Map<Long, List<EntityCore>> requireStatus = new HashMap<>();
	//		List<T> statusAttachables = new ArrayList<>();
	//		for(Result result : modelManager.createQuery(type).select(statusRelation.getAttribute()).fetch(filter)) {
	//			Record record = result.removeRecord(modelManager.getEntityType(type));
	//			EntityCore core = new DefaultEntityCore(record);
	//			for(Record relatedRecord : result) {
	//				Class<? extends Entity> clazz = ODSUtils.getClass(relatedRecord.getEntityType().getName());
	//
	//				if(isCached(clazz)) {
	//					core.getMutableStore().set(getCached(clazz, relatedRecord.getID()));
	//				} else {
	//					core.getMutableStore().set(createEntity(clazz, new DefaultEntityCore(relatedRecord)));
	//				}
	//			}
	//
	//			Optional<Long> statusID = record.getID(statusRelation);
	//			if(statusID.isPresent()) {
	//				requireStatus.computeIfAbsent(statusID.get(), k -> new ArrayList<>()).add(core);
	//			}
	//
	//			statusAttachables.add(createEntity(type, core));
	//		}
	//
	//		if(!requireStatus.isEmpty()) {
	//			for(Result result : modelManager.createQuery().selectAll(statusEntityType).fetch(Filter.idsOnly(statusEntityType, requireStatus.keySet()))) {
	//				Status status = createEntity(Status.class, new DefaultEntityCore(result.getRecord(statusEntityType)));
	//
	//				for(EntityCore core : requireStatus.get(status.getURI().getID())) {
	//					core.getMutableStore().set(status);
	//				}
	//			}
	//		}
	//
	//		return statusAttachables;
	//	}

	// TODO this is one query per application element
	private Optional<ContextRoot> loadContextRoot(Entity parent, ContextType contextType) throws DataAccessException {
		EntityType parentEntityType = modelManager.getEntityType(parent);
		EntityType contextRootEntityType = modelManager.getEntityType(contextType);
		EntityType templateRootEntityType = getEntityType(contextType, "Tpl", "Root");
		Relation templateRootrelation = contextRootEntityType.getRelation(templateRootEntityType);

		Optional<Result> result = modelManager.createQuery()
				.selectAll(contextRootEntityType).select(templateRootrelation.getAttribute())
				.fetchSingleton(Filter.idOnly(parentEntityType, parent.getURI().getID()));

		if(result.isPresent()) {
			Record record = result.get().getRecord(contextRootEntityType);
			EntityCore entityCore = new DefaultEntityCore(record);
			ContextRoot contextRoot = createEntity(ContextRoot.class, entityCore);

			Optional<Long> templateRootID = record.getID(templateRootrelation);
			if(!templateRootID.isPresent()) {
				// TODO log missing info relation to template root....
			}

			// TODO this should work!
			//			//TemplateRoot templateRoot = (TemplateRoot) load(new URI("", templateRootEntityType.getName(), templateRootID.get())).get();
			TemplateRoot templateRoot = loadAll(TemplateRoot.class, contextType, Filter.idOnly(templateRootEntityType, templateRootID.get())).get(0);
			// context root (base model) -> template root (default model) [this reference is HIDDEN in the API!]
			entityCore.getMutableStore().set(templateRoot);

			// load context components / one query per type
			for(Relation relation : contextRootEntityType.getChildRelations()) {
				EntityType contextComponentEntityType = relation.getTarget();
				Relation contextRootRelation = contextComponentEntityType.getRelation(contextRootEntityType);

				Query query = modelManager.createQuery().selectAll(contextComponentEntityType);
				List<Long> ids = new ArrayList<>();
				for(Result result2 : query.fetch(Filter.and().add(createRelationCondition(contextRootRelation, Collections.singletonList(contextRoot.getURI().getID()))))) {
					EntityCore contextComponentCore = new DefaultEntityCore(result2.getRecord(contextComponentEntityType));
					ContextComponent contextComponent = createEntity(ContextComponent.class, contextComponentCore);
					ids.add(contextComponent.getURI().getID());
					entityCore.getChildrenStore().add(contextComponent);
					contextComponentCore.getPermanentStore().setParent(contextRoot, false);
					Optional<TemplateComponent> templateComponent = templateRoot.getTemplateComponent(contextComponent.getName());
					if(templateComponent.isPresent()) {
						contextComponentCore.getMutableStore().set(templateComponent.get());

						// hide value containers that are not defined in the template
						contextComponentCore.hideValues(missingTemplateAttributeNames(templateComponent.get()));
					} else {
						// TODO log missing info relation context component -> template component (names do not match!)
					}
				}



				if(contextType.isTestEquipment() && !ids.isEmpty()) {
					// TODO load sensors

					//					for(Relation sensorRelation : contextComponentEntityType.getChildRelations()) {
					//						EntityType contextSensorEntityType = sensorRelation.getTarget();
					//						Relation contextComponentRelation = contextSensorEntityType.getRelation(contextComponentEntityType);
					//
					//						Query sensorsQuery = modelManager.createQuery().selectAll(contextSensorEntityType);
					//						sensorsQuery.fetch(Filter.and().add(createRelationCondition(contextComponentRelation, ids)));
					//					}
				}
			}

			// TODO: iterate over context components and exclude missing attributes

			return Optional.of(contextRoot);
		}

		return Optional.empty();
	}

	private List<String> missingTemplateAttributeNames(TemplateComponent templateComponent) {
		List<TemplateAttribute> templateAttributes = templateComponent.getTemplateAttributes();
		List<CatalogAttribute> catalogAttributes = templateComponent.getCatalogComponent().getCatalogAttributes();

		if(templateAttributes.size() < catalogAttributes.size()) {
			List<String> catalogAttributeNames = catalogAttributes.stream().map(CatalogAttribute::getName).collect(Collectors.toList());
			templateAttributes.forEach(ta -> catalogAttributeNames.remove(ta.getName()));
			return catalogAttributeNames;
		}

		return Collections.emptyList();
	}

	@Override
	@Deprecated // TODO move to loadChildren
	public Map<ContextType, ContextRoot> loadContexts(ContextDescribable contextDescribable, ContextType... contextTypes)
			throws DataAccessException {
		Map<ContextType, ContextRoot> contextRoots = new EnumMap<>(ContextType.class);
		for(ContextType contextType : adjustContextTypes(contextTypes)) {
			loadContextRoot(contextDescribable, contextType).ifPresent(cr -> contextRoots.put(contextType, cr));
		}
		return contextRoots;
	}

	@Override
	@Deprecated // TODO move to loadChildren
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
		Map<Long, ParameterSet> parameterSetByID = new HashMap<>();
		List<ParameterSet> parameterSets = new ArrayList<>();
		for (Result result : results) {
			Record parameterSetRecord = result.removeRecord(parameterSetEntityType);
			Long id = parameterSetRecord.getID();
			EntityCore parameterSetCore = parameterSetCoresByID.get(id);
			ParameterSet parameterSet = parameterSetByID.get(id);
			if(parameterSetCore == null) {
				parameterSetCore = new DefaultEntityCore(parameterSetRecord);
				parameterSetCoresByID.put(id, parameterSetCore);
				parameterSet = createEntity(ParameterSet.class, parameterSetCore);
				parameterSets.add(parameterSet);
				parameterSetByID.put(id, parameterSet);
			}

			Record parameterRecord = result.getRecord(parameterEntityType);
			if(parameterRecord.getValues().get(Entity.ATTR_ID).isValid()) {
				EntityCore parameterCore = new DefaultEntityCore(parameterRecord);
				Record unitRecord = result.getRecord(unitEntityType);
				if(unitRecord.getValues().get(Entity.ATTR_ID).isValid()) {
					parameterCore.getMutableStore().set(getCached(Unit.class, unitRecord.getID()));
					parameterCore.getPermanentStore().setParent(parameterSet, false);
				}

				parameterSetCore.getChildrenStore().add(createEntity(Parameter.class, parameterCore));
			}
		}

		return parameterSets;
	}

	private List<ValueList> loadValueLists(Filter filter) throws DataAccessException {
		// load value list entities
		EntityType valueListEntityType = modelManager.getEntityType(ValueList.class);
		Map<Long, EntityCore> valueListCores = new HashMap<>();
		Map<Long, ValueList> valueLists = new HashMap<>();
		for(Result result : modelManager.createQuery().selectAll(valueListEntityType).fetch(filter)) {
			Record record = result.getRecord(valueListEntityType);
			EntityCore valueListCore = new DefaultEntityCore(record);
			valueListCores.put(record.getID(), valueListCore);
			valueLists.put(record.getID(), createEntity(ValueList.class, valueListCore));
		}

		// load value list value entities
		EntityType valueListValueEntityType = modelManager.getEntityType(ValueListValue.class);
		Relation valueListRelation = valueListValueEntityType.getRelation(valueListEntityType);
		Query query = modelManager.createQuery().selectAll(valueListValueEntityType)
				.select(valueListRelation.getAttribute());

		if(valueLists.isEmpty()) {
			/*
			 * TODO: make it clean....
			 */
			return Collections.emptyList();
		}

		Filter valueListValueFilter = Filter.and();
		if(!filter.isEmtpty()) {
			valueListValueFilter.add(createRelationCondition(valueListRelation, valueLists.keySet()));
		}
		for(Result result : query.fetch(valueListValueFilter)) {
			Record record = result.getRecord(valueListValueEntityType);
			EntityCore valueListValueCore = new DefaultEntityCore(record);
			ValueListValue valueListValue = createEntity(ValueListValue.class, valueListValueCore);
			Optional<Long> valueListID = record.getID(valueListRelation);
			if(valueListID.isPresent()) {
				EntityCore valueListCore = valueListCores.get(valueListID.get());
				valueListCore.getChildrenStore().add(valueListValue);
				valueListValueCore.getPermanentStore().setParent(valueLists.get(valueListID.get()), false);
			} else {
				logEntityWithoutParent(valueListValue);
			}
		}

		return new ArrayList<>(valueLists.values());
	}

	private List<CatalogComponent> loadCatalogComponents(ContextType contextType, Filter filter) throws DataAccessException {
		// load catalog component entities
		EntityType catalogComponentEntityType = getEntityType(contextType, "Cat", "Comp");
		Map<Long, CatalogComponent> catalogComponents = new HashMap<>();
		Map<Long, EntityCore> catalogComponentCores = new HashMap<>();
		for (Result result : modelManager.createQuery().selectAll(catalogComponentEntityType).fetch(filter)) {
			Record record = result.getRecord(catalogComponentEntityType);
			EntityCore catalogComponentCore = new DefaultEntityCore(record);
			catalogComponentCores.put(record.getID(), catalogComponentCore);
			catalogComponents.put(record.getID(), createEntity(CatalogComponent.class, catalogComponentCore));
		}

		if(catalogComponents.isEmpty()) {
			/*
			 * TODO: make it clean....
			 */
			return Collections.emptyList();
		}

		// load catalog attribute entities
		EntityType catalogAttributeEntityType = getEntityType(contextType, "Cat", "Attr");
		EntityType valueListEntityType = modelManager.getEntityType(ValueList.class);
		Relation catalogComponentRelation = catalogAttributeEntityType.getRelation(catalogComponentEntityType);
		Relation valueListRelation = catalogAttributeEntityType.getRelation(valueListEntityType);
		Query query = modelManager.createQuery().selectAll(catalogAttributeEntityType)
				.select(catalogComponentRelation.getAttribute())
				.select(valueListRelation.getAttribute());

		Filter catalogAttributesFilter = Filter.and();
		if(!filter.isEmtpty()) {
			catalogAttributesFilter.add(createRelationCondition(catalogComponentRelation, catalogComponents.keySet()));
		}
		Map<Long, List<CatalogAttribute>> requireValueList = new HashMap<>();
		for(Result result : query.fetch(catalogAttributesFilter)) {
			Record record = result.getRecord(catalogAttributeEntityType);
			Optional<Long> catalogComponentID = record.getID(catalogComponentRelation);
			Optional<Long> valueListID = record.getID(valueListRelation);
			EntityCore catalogAttributeCore = new DefaultEntityCore(result.getRecord(catalogAttributeEntityType));
			if(catalogComponentID.isPresent()) {
				EntityCore catalogComponentCore = catalogComponentCores.get(catalogComponentID.get());
				adjustCatalogAttributeCore(catalogComponentCore, catalogAttributeCore);
				CatalogAttribute catalogAttribute = createEntity(CatalogAttribute.class, catalogAttributeCore);
				catalogComponentCore.getChildrenStore().add(catalogAttribute);
				catalogAttributeCore.getPermanentStore().setParent(catalogComponents.get(catalogComponentID.get()), false);
				valueListID.ifPresent(id -> requireValueList.computeIfAbsent(id, k -> new ArrayList<>()).add(catalogAttribute));
			} else {
				// TODO ... missing parent ...
			}
		}

		if(contextType.isTestEquipment()) {
			// TODO load sensors
			// TODO load sensor attributes
			// TODO: add sensor attributes to 'requireValueList' if required!
		}

		if(!requireValueList.isEmpty()) {
			// TODO load value list only if at least one is required!

			/*
			 * TODO: make it clean....
			 */

			for(ValueList valueList : loadAll(ValueList.class, requireValueList.keySet())) {
				for(CatalogAttribute catalogAttribute : requireValueList.get(valueList.getURI().getID())) {
					catalogAttribute.setValueList(valueList);
				}
			}
		}

		return new ArrayList<>(catalogComponents.values());
	}

	private List<TemplateRoot> loadTemplateRoots(ContextType contextType, Filter filter) throws DataAccessException {
		// load template root entities
		EntityType templateRootEntityType = getEntityType(contextType, "Tpl", "Root");
		Map<Long, EntityCore> templateRootCores = new HashMap<>();
		Map<Long, TemplateRoot> templateRoots = new HashMap<>();
		for (Result result : modelManager.createQuery().selectAll(templateRootEntityType).fetch(filter)) {
			Record record = result.getRecord(templateRootEntityType);
			EntityCore entityCore = new DefaultEntityCore(record);
			templateRootCores.put(record.getID(), entityCore);
			templateRoots.put(record.getID(), createEntity(TemplateRoot.class, entityCore));
		}

		if(templateRoots.isEmpty()) {
			/*
			 * TODO: make it clean....
			 */
			return Collections.emptyList();
		}

		// load template component entities
		EntityType templateComponentEntityType = getEntityType(contextType, "Tpl", "Comp");
		EntityType catalogComponentEntityType = getEntityType(contextType, "Cat", "Comp");
		Relation templateRootParentRelation = templateComponentEntityType.getRelation(templateRootEntityType);
		Relation templateComponentParentRelation = templateComponentEntityType.getRelation(templateComponentEntityType);
		Relation catalogComponentRelation = templateComponentEntityType.getRelation(catalogComponentEntityType);
		Query query = modelManager.createQuery().selectAll(templateComponentEntityType)
				.select(templateRootParentRelation.getAttribute())
				.select(templateComponentParentRelation.getAttribute())
				.select(catalogComponentRelation.getAttribute())
				// template components with children have to be processed before their children!
				.order(templateComponentEntityType.getIDAttribute()); // TODO: try to remove the order by statement...

		Filter templateComponentFilter = Filter.or();
		if(!filter.isEmtpty()) {
			// load immediate children of template roots loaded above
			templateComponentFilter.add(createRelationCondition(templateRootParentRelation, templateRoots.keySet()));
			// load ALL template components which have another template component as parent
			templateComponentFilter.add(Operation.IS_NULL.create(templateRootParentRelation.getAttribute(), 0L));
			// TODO: it is also possible to load children recursively
			// which will result in multiple queries against the same
			// table (one query per level!)
		}

		Map<Long, TemplateComponent> templateComponents = new HashMap<>();
		Map<Long, EntityCore> templateComponentCores = new HashMap<>();
		Map<Long, List<EntityCore>> requireCatalogComponent = new HashMap<>();
		for(Result result : query.fetch(templateComponentFilter)) {
			Record record = result.getRecord(templateComponentEntityType);
			Optional<Long> templateRootID = record.getID(templateRootParentRelation);
			Optional<Long> templateComponentID = record.getID(templateComponentParentRelation);
			Long catalogComponentID = record.getID(catalogComponentRelation).get();
			EntityCore entityCore = new DefaultEntityCore(record);

			TemplateComponent templateComponent = createEntity(TemplateComponent.class, entityCore);
			if(templateRootID.isPresent()) {
				templateRootCores.get(templateRootID.get()).getChildrenStore().add(templateComponent);
				entityCore.getPermanentStore().setParent(templateRoots.get(templateRootID.get()), true);
			} else if(templateComponentID.isPresent()) {
				EntityCore templateComponentCore = templateComponentCores.get(templateComponentID.get());
				if(templateComponentCore == null) {
					// this template component's parent was not loaded -> skip
					continue;
				}
				templateComponentCore.getChildrenStore().add(templateComponent);
				entityCore.getPermanentStore().setParent(templateComponents.get(templateComponentID.get()), true);
			} else {
				throw new IllegalStateException("Template component does not have a parent.");
			}

			// do not move lines below for proper filtering
			templateComponentCores.put(record.getID(), entityCore);
			templateComponents.put(record.getID(), templateComponent);
			requireCatalogComponent.computeIfAbsent(catalogComponentID, id -> new ArrayList<>()).add(entityCore);
		}

		Map<Long, List<EntityCore>> requireCatalogAttribute = new HashMap<>();
		if(!templateComponents.isEmpty()) {

			EntityType templateAttributeEntityType = getEntityType(contextType, "Tpl", "Attr");
			EntityType catalogAttributeEntityType = getEntityType(contextType, "Cat", "Attr");
			templateComponentParentRelation =  templateAttributeEntityType.getRelation(templateComponentEntityType);
			Relation catalogAttributeRelation = templateAttributeEntityType.getRelation(catalogAttributeEntityType);

			query = modelManager.createQuery().selectAll(templateAttributeEntityType)
					.select(templateComponentParentRelation.getAttribute())
					.select(catalogAttributeRelation.getAttribute());

			Filter templateAttributesFilter = Filter.and();
			if(!filter.isEmtpty()) {
				templateAttributesFilter.add(createRelationCondition(templateComponentParentRelation, templateComponents.keySet()));
			}
			for(Result result : query.fetch(templateAttributesFilter)) {
				Record record = result.getRecord(templateAttributeEntityType);
				Long templateComponentID = record.getID(templateComponentParentRelation)
						.orElseThrow(() -> new IllegalStateException("Template attribute does not have a parent."));
				Long catalogAttributeID = record.getID(catalogAttributeRelation).orElseThrow(() -> new IllegalStateException( /* TODO */ ));
				EntityCore entityCore = new DefaultEntityCore(record);
				TemplateAttribute templateAttribute = createEntity(TemplateAttribute.class, entityCore);
				entityCore.getPermanentStore().setParent(templateComponents.get(templateComponentID), true);
				templateComponentCores.get(templateComponentID).getChildrenStore().add(templateAttribute);
				requireCatalogAttribute.computeIfAbsent(catalogAttributeID, id -> new ArrayList<>()).add(entityCore);
			}

			if(contextType.isTestEquipment()) {
				// TODO load sensors
				// TODO load sensor attributes
				// TODO: add sensor attributes to 'requireCatalogAttribute' if required!
			}

		}

		if(!requireCatalogComponent.isEmpty()) {
			// TODO load catalog components only if at least one is required!
			List<CatalogComponent> catalogComponents = loadAll(CatalogComponent.class, contextType, Filter.idsOnly(catalogComponentEntityType, requireCatalogComponent.keySet()));
			for(CatalogComponent catalogComponent : catalogComponents) {
				for(EntityCore entityCore : requireCatalogComponent.get(catalogComponent.getURI().getID())) {
					entityCore.getMutableStore().set(catalogComponent);
				}

				// avoid micro optimization here and simply iterate over all
				for(CatalogAttribute catalogAttribute : catalogComponent.getCatalogAttributes()) {
					List<EntityCore> entityCores = requireCatalogAttribute.get(catalogAttribute.getURI().getID());
					if(entityCores == null) {
						// catalog attribute is not used -> skip
						continue;
					}
					for(EntityCore entityCore : entityCores) {
						entityCore.getMutableStore().set(catalogAttribute);
					}
				}

				if(contextType.isTestEquipment()) {
					// TODO: assign catalog sensors and attributes to template sensors and attributes
				}
			}
		}

		// TODO should we make sure that all ino relations (tplComp -> catComp; tplAttr -> catAttr, etc) have been set?!

		return new ArrayList<>(templateRoots.values());
	}

	private List<TemplateTestStep> loadTemplateTestSteps(Filter filter) throws DataAccessException {
		EntityType templateTestStepEntityType = modelManager.getEntityType(TemplateTestStep.class);
		EntityType templateRootUUTEntityType = getEntityType(ContextType.UNITUNDERTEST, "Tpl", "Root");
		EntityType templateRootTSQEntityType = getEntityType(ContextType.TESTSEQUENCE, "Tpl", "Root");
		EntityType templateRootTEQEntityType = getEntityType(ContextType.TESTEQUIPMENT, "Tpl", "Root");

		Relation uutTemplateRootRelation = templateTestStepEntityType.getRelation(templateRootUUTEntityType);
		Relation tsqTemplateRootRelation = templateTestStepEntityType.getRelation(templateRootTSQEntityType);
		Relation teqTemplateRootRelation = templateTestStepEntityType.getRelation(templateRootTEQEntityType);

		Query query = modelManager.createQuery().selectAll(templateTestStepEntityType)
				.select(uutTemplateRootRelation.getAttribute())
				.select(tsqTemplateRootRelation.getAttribute())
				.select(teqTemplateRootRelation.getAttribute());

		List<TemplateTestStep> templateTestSteps = new ArrayList<>();
		Map<Long, List<TemplateTestStep>> requireTemplateRootUUT = new HashMap<>();
		Map<Long, List<TemplateTestStep>> requireTemplateRootTSQ = new HashMap<>();
		Map<Long, List<TemplateTestStep>> requireTemplateRootTEQ = new HashMap<>();
		for(Result result : query.fetch(filter)) {
			Record record = result.getRecord(templateTestStepEntityType);
			TemplateTestStep templateTestStep = createEntity(TemplateTestStep.class, new DefaultEntityCore(record));
			record.getID(uutTemplateRootRelation).ifPresent(id -> requireTemplateRootUUT.computeIfAbsent(id, k -> new ArrayList<>()).add(templateTestStep));
			record.getID(tsqTemplateRootRelation).ifPresent(id -> requireTemplateRootTSQ.computeIfAbsent(id, k -> new ArrayList<>()).add(templateTestStep));
			record.getID(teqTemplateRootRelation).ifPresent(id -> requireTemplateRootTEQ.computeIfAbsent(id, k -> new ArrayList<>()).add(templateTestStep));
			templateTestSteps.add(templateTestStep);
		}

		if(!requireTemplateRootUUT.isEmpty()) {
			// TODO load template roots only if at least one is required!
			for(TemplateRoot templateRoot : loadAll(TemplateRoot.class, ContextType.UNITUNDERTEST, Filter.idsOnly(templateRootUUTEntityType, requireTemplateRootUUT.keySet()))) {
				for(TemplateTestStep templateTestStep : requireTemplateRootUUT.get(templateRoot.getURI().getID())) {
					templateTestStep.setTemplateRoot(templateRoot);
				}
			}
		}

		if(!requireTemplateRootTSQ.isEmpty()) {
			// TODO load template roots only if at least one is required!
			for(TemplateRoot templateRoot : loadAll(TemplateRoot.class, ContextType.TESTSEQUENCE, Filter.idsOnly(templateRootTSQEntityType, requireTemplateRootTSQ.keySet()))) {
				for(TemplateTestStep templateTestStep : requireTemplateRootTSQ.get(templateRoot.getURI().getID())) {
					templateTestStep.setTemplateRoot(templateRoot);
				}
			}
		}

		if(!requireTemplateRootTEQ.isEmpty()) {
			// TODO load template roots only if at least one is required!
			for(TemplateRoot templateRoot : loadAll(TemplateRoot.class, ContextType.TESTEQUIPMENT, Filter.idsOnly(templateRootTEQEntityType, requireTemplateRootTEQ.keySet()))) {
				for(TemplateTestStep templateTestStep : requireTemplateRootTEQ.get(templateRoot.getURI().getID())) {
					templateTestStep.setTemplateRoot(templateRoot);
				}
			}
		}

		return templateTestSteps;
	}

	private List<TemplateTest> loadTemplateTests(Filter filter) throws DataAccessException {
		EntityType templateTestEntityType = modelManager.getEntityType(TemplateTest.class);

		Map<Long, EntityCore> templateTestCores = new HashMap<>();
		Map<Long, TemplateTest> templateTests = new HashMap<>();
		for(Result result : modelManager.createQuery().selectAll(templateTestEntityType).fetch(filter)) {
			Record record = result.getRecord(templateTestEntityType);
			EntityCore entityCore = new DefaultEntityCore(record);
			templateTestCores.put(record.getID(), entityCore);
			templateTests.put(record.getID(), createEntity(TemplateTest.class, entityCore));
		}

		if(templateTests.isEmpty()) {
			return Collections.emptyList();
		}

		EntityType templateTestStepUsageEntityType = modelManager.getEntityType(TemplateTestStepUsage.class);
		EntityType templateTestStepEntityType = modelManager.getEntityType(TemplateTestStep.class);
		Relation templateTestRelation = templateTestStepUsageEntityType.getRelation(templateTestEntityType);
		Relation templateTestStepRelation = templateTestStepUsageEntityType.getRelation(templateTestStepEntityType);

		Query query = modelManager.createQuery().selectAll(templateTestStepUsageEntityType)
				.select(templateTestRelation.getAttribute())
				.select(templateTestStepRelation.getAttribute());

		Filter templateTestStepUsageFilter = Filter.and();
		if(!filter.isEmtpty()) {
			templateTestStepUsageFilter.add(createRelationCondition(templateTestRelation, templateTests.keySet()));
		}

		Map<Long, List<EntityCore>> requireTestStep = new HashMap<>();
		for(Result result : query.fetch(templateTestStepUsageFilter)) {
			Record record = result.getRecord(templateTestStepUsageEntityType);
			EntityCore entityCore = new DefaultEntityCore(record);
			Long templateTestID = record.getID(templateTestRelation).get();
			Long templateTestStepID = record.getID(templateTestStepRelation).get();

			entityCore.getPermanentStore().setParent(templateTests.get(templateTestID), false);
			templateTestCores.get(templateTestID).getChildrenStore().add(createEntity(TemplateTestStepUsage.class, entityCore));
			requireTestStep.computeIfAbsent(templateTestStepID, k -> new ArrayList<>()).add(entityCore);
		}

		if(!requireTestStep.isEmpty()) {
			for(TemplateTestStep templateTestStep : loadAll(TemplateTestStep.class, requireTestStep.keySet())) {
				for(EntityCore entityCore : requireTestStep.get(templateTestStep.getURI().getID())) {
					entityCore.getMutableStore().set(templateTestStep);
				}
			}
		}

		return new ArrayList<>(templateTests.values());
	}

	private void adjustCatalogAttributeCore(EntityCore catalogComponentCore, EntityCore catalogAttributeCore) {
		EntityType entityType = modelManager.getEntityType((String) catalogComponentCore.getValues().get(Entity.ATTR_NAME).extract());
		Attribute attribute = entityType.getAttribute(catalogAttributeCore.getValues().get(Entity.ATTR_NAME).extract());

		Map<String, Value> values = catalogAttributeCore.getValues();
		Value enumerationClass = ValueType.STRING.create(VATTR_ENUMERATION_CLASS);
		values.put(VATTR_ENUMERATION_CLASS, enumerationClass);
		if(attribute.getValueType().isEnumerationType()) {
			enumerationClass.set(attribute.getEnumClass().getSimpleName());
		}

		Value scalarType = ValueType.ENUMERATION.create(ScalarType.class, VATTR_SCALAR_TYPE);
		scalarType.set(ScalarType.valueOf(attribute.getValueType().toSingleType().name()));
		values.put(VATTR_SCALAR_TYPE, scalarType);

		values.put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE, attribute.getValueType().isSequence()));

		// TODO relation to Unit
		//		Unit unit = null;
		//		if(unit != null) {
		//			catalogAttributeCore.setInfoRelation(unit);
		//		}
	}

	private EntityType getEntityType(ContextType contextType, String prefix, String postfix) {
		return modelManager.getEntityType(prefix + ODSUtils.CONTEXTTYPES.convert(contextType) + postfix);
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
			throw new ConnectionException("Unable to close the connection to the data source due to: " + e.reason, e);
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

	private <T extends Entity> List<T> createEntities(Class<T> type, List<Result> results)
			throws DataAccessException {
		List<T> entities = new ArrayList<>();
		for(Result result : results) {
			entities.add(createEntity(type, result));
		}

		return entities;
	}

	@Deprecated
	private <T extends Entity> T createEntity(Class<T> type, Result result) throws DataAccessException {
		Record record = result.removeRecord(modelManager.getEntityType(type));
		EntityCore core = new DefaultEntityCore(record);
		for(Record relatedRecord : result) {
			Class<? extends Entity> clazz = ODSUtils.getClass(relatedRecord.getEntityType().getName());

			if(isCached(clazz)) {
				core.getMutableStore().set(getCached(clazz, relatedRecord.getID()));
			} else {
				core.getMutableStore().set(createEntity(clazz, new DefaultEntityCore(relatedRecord)));
			}
		}

		return createEntity(type, core);
	}

	@SuppressWarnings("unchecked")
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

	private Condition createRelationCondition(Relation relation, Collection<Long> ids) {
		List<Long> distinctIDs = ids.stream().distinct().collect(Collectors.toList());
		long[] unboxedIDs = new long[distinctIDs.size()];
		int i = 0;
		for(Long id : distinctIDs) {
			unboxedIDs[i++] = id;
		}

		return Operation.IN_SET.create(relation.getAttribute(), unboxedIDs);
	}

	private void logEntityWithoutParent(Entity entity) {
		LOGGER.warn("Entity with missing parent '" + entity + "' found, therefore skipped.");
	}

	// TODO: all for none & no duplicates
	private List<ContextType> adjustContextTypes(ContextType... contextTypes) {
		if(contextTypes.length == 0) {
			return Arrays.asList(ContextType.values());
		}

		return Arrays.stream(contextTypes).distinct().collect(Collectors.toList());
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
