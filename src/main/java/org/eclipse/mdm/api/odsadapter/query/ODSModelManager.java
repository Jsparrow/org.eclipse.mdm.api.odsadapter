/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter.query;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.asam.ods.AIDName;
import org.asam.ods.AggrFunc;
import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplRel;
import org.asam.ods.ApplicationStructure;
import org.asam.ods.ApplicationStructureValue;
import org.asam.ods.ElemResultSetExt;
import org.asam.ods.EnumerationAttributeStructure;
import org.asam.ods.EnumerationDefinition;
import org.asam.ods.JoinDef;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.SelAIDNameUnitId;
import org.asam.ods.SelItem;
import org.asam.ods.SelOrder;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextSensor;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EnumRegistry;
import org.eclipse.mdm.api.base.model.Enumeration;
import org.eclipse.mdm.api.base.model.Environment;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Parameter;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Sortable;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.dflt.model.Classification;
import org.eclipse.mdm.api.dflt.model.Domain;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.dflt.model.ProjectDomain;
import org.eclipse.mdm.api.dflt.model.Status;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateRoot;
import org.eclipse.mdm.api.dflt.model.TemplateSensor;
import org.eclipse.mdm.api.dflt.model.TemplateTest;
import org.eclipse.mdm.api.dflt.model.TemplateTestStep;
import org.eclipse.mdm.api.dflt.model.TemplateTestStepUsage;
import org.eclipse.mdm.api.dflt.model.ValueList;
import org.eclipse.mdm.api.dflt.model.ValueListValue;
import org.eclipse.mdm.api.dflt.model.Versionable;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfigRepository;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSEnum;
import org.eclipse.mdm.api.odsadapter.utils.ODSEnumerations;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ODS implementation of the {@link ModelManager} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class ODSModelManager implements ModelManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSModelManager.class);

	private final Map<String, EntityType> entityTypesByName = new HashMap<>();

	
	private final ORB orb;

	private final Lock write;
	private final Lock read;

	private EntityConfigRepository entityConfigRepository;

	private ApplElemAccess applElemAccess;
	private AoSession aoSession;

	/**
	 * Constructor.
	 *
	 * @param orb
	 *            Used to activate CORBA service objects.
	 * @param aoSession
	 *            The underlying ODS session.
	 * @throws AoException
	 *             Thrown on errors.
	 */
	public ODSModelManager(ORB orb, AoSession aoSession) throws AoException {
		this.aoSession = aoSession;
		this.orb = orb;
		applElemAccess = aoSession.getApplElemAccess();

		// setup locks
		ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
		write = reentrantReadWriteLock.writeLock();
		read = reentrantReadWriteLock.readLock();

		initialize();
	}

	/**
	 * Returns the {@link ORB}.
	 *
	 * @return The {@code ORB} is returned.
	 */
	public ORB getORB() {
		return orb;
	}

	/**
	 * Returns the non root {@link EntityConfig} for given {@link Key}.
	 *
	 * @param <T>
	 *            The concrete entity type.
	 * @param key
	 *            Used as identifier.
	 * @return The non root {@code EntityConfig} is returned.
	 */
	public <T extends Entity> EntityConfig<T> findEntityConfig(Key<T> key) {
		read.lock();

		try {
			return entityConfigRepository.find(key);
		} finally {
			read.unlock();
		}
	}

	/**
	 * Returns the root {@link EntityConfig} for given {@link Key}.
	 *
	 * @param <T>
	 *            The concrete entity type.
	 * @param key
	 *            Used as identifier.
	 * @return The root {@code EntityConfig} is returned.
	 */
	public <T extends Entity> EntityConfig<T> getEntityConfig(Key<T> key) {
		read.lock();

		try {
			return entityConfigRepository.findRoot(key);
		} finally {
			read.unlock();
		}
	}

	/**
	 * Returns the {@link EntityConfig} associated with given
	 * {@link EntityType}.
	 *
	 * @param entityType
	 *            Used as identifier.
	 * @return The {@code EntityConfig} is returned.
	 */
	public EntityConfig<?> getEntityConfig(EntityType entityType) {
		read.lock();

		try {
			return entityConfigRepository.find(entityType);
		} finally {
			read.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EntityType> listEntityTypes() {
		read.lock();

		try {
			return Collections.unmodifiableList(new ArrayList<>(entityTypesByName.values()));
		} finally {
			read.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getEntityType(Class<? extends Entity> entityClass) {
		read.lock();

		try {
			return getEntityConfig(new Key<>(entityClass)).getEntityType();
		} finally {
			read.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getEntityType(Class<? extends Entity> entityClass, ContextType contextType) {
		read.lock();

		try {
			return getEntityConfig(new Key<>(entityClass, contextType)).getEntityType();
		} finally {
			read.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityType getEntityType(String name) {
		read.lock();

		try {
			EntityType entityType = entityTypesByName.get(name);
			if (entityType == null) {
				throw new IllegalArgumentException(new StringBuilder().append("Entity with name '").append(name).append("' not found.").toString());
			}

			return entityType;
		} finally {
			read.unlock();
		}
	}

	@Override
	public EntityType getEntityTypeById(String id) {
		Optional<EntityType> entityType = listEntityTypes().stream().filter(et -> et.getId().equals(id)).findFirst();
		if (!entityType.isPresent()) {
			throw new IllegalArgumentException(new StringBuilder().append("Entity with id '").append(id).append("' not found.").toString());
		}

		return entityType.get();
	}

	/**
	 * Returns the {@link AoSession} of this model manager.
	 *
	 * @return The {@code AoSession} is returned.
	 */
	public AoSession getAoSession() {
		write.lock();

		try {
			return aoSession;
		} finally {
			write.unlock();
		}
	}

	/**
	 * Returns the {@link ApplElemAccess} of this model manager.
	 *
	 * @return The {@code ApplElemAccess} is returned.
	 */
	public ApplElemAccess getApplElemAccess() {
		read.lock();

		try {
			return applElemAccess;
		} finally {
			read.unlock();
		}
	}

	/**
	 * Closes the ODS connection.
	 *
	 * @throws AoException
	 *             Thrown on errors.
	 */
	public void close() throws AoException {
		read.lock();

		try {
			applElemAccess._release();
			aoSession.close();
		} finally {
			read.unlock();
			aoSession._release();
		}
	}

	/**
	 * Reloads the complete session context.
	 */
	public void reloadApplicationModel() {
		write.lock();

		AoSession aoSessionOld = aoSession;
		ApplElemAccess applElemAccessOld = applElemAccess;
		try {
			entityTypesByName.clear();

			aoSession = aoSession.createCoSession();
			applElemAccess = aoSession.getApplElemAccess();
			initialize();
		} catch (AoException e) {
			LOGGER.error("Unable to reload the application model due to: " + e.reason, e);
		} finally {
			write.unlock();
		}

		try {
			applElemAccessOld._release();
			aoSessionOld.close();
		} catch (AoException e) {
			LOGGER.debug("Unable to close replaced session due to: " + e.reason, e);
		} finally {
			aoSessionOld._release();
		}
	}

	/**
	 * Initializes this model manager by caching the application model and
	 * loading the {@link EntityConfig}s.
	 *
	 * @throws AoException
	 *             Thrown on errors.
	 */
	private void initialize() throws AoException {
		loadApplicationModel();
		loadEntityConfigurations();
	}

	/**
	 * Caches the whole application model as provided by the ODS session.
	 *
	 * @throws AoException
	 *             Thrown on errors.
	 */
	private void loadApplicationModel() throws AoException {
		LOGGER.debug("Reading the application model...");

		Map<String, Map<String, Enumeration<?>>> enumClassMap = loadODSEnumerations();

		long start = System.currentTimeMillis();
		ApplicationStructureValue applicationStructureValue = aoSession.getApplicationStructureValue();
		Map<String, String> units = getUnitMapping(applicationStructureValue.applElems);

		// create entity types (incl. attributes)
		Map<String, ODSEntityType> entityTypesByID = new HashMap<>();
		String sourceName = aoSession.getName();
		for (ApplElem applElem : applicationStructureValue.applElems) {
			String odsID = Long.toString(ODSConverter.fromODSLong(applElem.aid));
			Map<String, Enumeration<?>> entityEnumMap = enumClassMap.getOrDefault(odsID, new HashMap<>());

			ODSEntityType entityType = new ODSEntityType(sourceName, applElem, units, entityEnumMap);
			entityTypesByName.put(applElem.aeName, entityType);
			entityTypesByID.put(odsID, entityType);
		}

		// create relations
		List<Relation> relations = new ArrayList<>();
		for (ApplRel applRel : applicationStructureValue.applRels) {
			EntityType source = entityTypesByID.get(Long.toString(ODSConverter.fromODSLong(applRel.elem1)));
			EntityType target = entityTypesByID.get(Long.toString(ODSConverter.fromODSLong(applRel.elem2)));
			relations.add(new ODSRelation(applRel, source, target));
		}

		// assign relations to their source entity types
		relations.stream().collect(groupingBy(Relation::getSource))
				.forEach((e, r) -> ((ODSEntityType) e).setRelations(r));

		long stop = System.currentTimeMillis();
		LOGGER.debug("{} entity types with {} relations found in {} ms.", entityTypesByName.size(), relations.size(),
				stop - start);
	}

	private Map<String, Map<String, Enumeration<?>>> loadODSEnumerations() throws AoException {
		LOGGER.debug("Loading ODS enumerations...");
		long t1 = System.currentTimeMillis();

		// enumeration mappings (aeID -> (aaName -> enumClass))
		Map<String, Map<String, Enumeration<?>>> enumClassMap = new HashMap<>();
		EnumRegistry er = EnumRegistry.getInstance();
		ApplicationStructure applicationStructure = aoSession.getApplicationStructure();
		for (EnumerationAttributeStructure eas : aoSession.getEnumerationAttributes()) {

			EnumerationDefinition bubu = applicationStructure.getEnumerationDefinition(eas.enumName);
			for (String itemName : bubu.listItemNames()) {
				final int intValue = bubu.getItem(itemName);
				LOGGER.debug("{}:{}:{}", eas.enumName, itemName, intValue);
			}

			// make sure the enumeration is found
			if (ODSEnumerations.getEnumObj(eas.enumName) == null) {
				Enumeration<ODSEnum> enumdyn = new Enumeration<>(ODSEnum.class, eas.enumName);
				EnumerationDefinition enumdef = applicationStructure.getEnumerationDefinition(eas.enumName);
				for (String itemName : enumdef.listItemNames()) {
					final int intValue = enumdef.getItem(itemName);
					enumdyn.addValue(new ODSEnum(itemName, intValue));
				}
				er.add(eas.enumName, enumdyn);
			}

			enumClassMap.computeIfAbsent(Long.toString(ODSConverter.fromODSLong(eas.aid)), k -> new HashMap<>())
					.put(eas.aaName, ODSEnumerations.getEnumObj(eas.enumName));
		}
		LOGGER.debug("Loading enumerations took {}ms", System.currentTimeMillis() - t1);
		return enumClassMap;
	}

	/**
	 * Loads all available {@link Unit} names mapped by their instance IDs.
	 *
	 * @param applElems
	 *            The application element meta data instances.
	 * @return The unit names mapped by the corresponding instance IDs.
	 * @throws AoException
	 *             Thrown if unable to load the unit mappings.
	 */
	private Map<String, String> getUnitMapping(ApplElem[] applElems) throws AoException {
		ApplElem unitElem = Stream.of(applElems).filter(ae -> "AoUnit".equals(ae.beName)).findAny()
				.orElseThrow(() -> new IllegalStateException("Application element 'Unit' is not defined."));

		QueryStructureExt qse = new QueryStructureExt();
		qse.anuSeq = new SelAIDNameUnitId[] {
				new SelAIDNameUnitId(new AIDName(unitElem.aid, "Id"), new T_LONGLONG(), AggrFunc.NONE),
				new SelAIDNameUnitId(new AIDName(unitElem.aid, "Name"), new T_LONGLONG(), AggrFunc.NONE) };
		qse.condSeq = new SelItem[0];
		qse.groupBy = new AIDName[0];
		qse.joinSeq = new JoinDef[0];
		qse.orderBy = new SelOrder[0];

		Map<String, String> units = new HashMap<>();
		ElemResultSetExt unitResultSetExt = getApplElemAccess().getInstancesExt(qse, 0)[0].firstElems[0];
		for (int i = 0; i < unitResultSetExt.values[0].value.flag.length; i++) {
			String unitID = Long
					.toString(ODSConverter.fromODSLong(unitResultSetExt.values[0].value.u.longlongVal()[i]));
			String unitName = unitResultSetExt.values[1].value.u.stringVal()[i];
			units.put(unitID, unitName);
		}

		return units;
	}

	/**
	 * Loads the {@link EntityConfig}s.
	 */
	private void loadEntityConfigurations() {
		LOGGER.debug("Loading entity configurations...");
		long start = System.currentTimeMillis();

		entityConfigRepository = new EntityConfigRepository();

		// Environment | Project | Pool | PhysicalDimension | User | Measurement
		// | ChannelGroup
		entityConfigRepository.register(create(new Key<>(Environment.class), "Environment", false));
		entityConfigRepository.register(create(new Key<>(Project.class), "Project", false));
		entityConfigRepository.register(create(new Key<>(Pool.class), "StructureLevel", true));
		entityConfigRepository.register(create(new Key<>(PhysicalDimension.class), "PhysDimension", false));
		entityConfigRepository.register(create(new Key<>(User.class), "User", false));
		entityConfigRepository.register(create(new Key<>(ChannelGroup.class), "SubMatrix", false));
		entityConfigRepository.register(create(new Key<>(Status.class), "Status", true));

		// Project Domain
		EntityConfig<ProjectDomain> projectDomainEntityConfig = create(new Key<>(ProjectDomain.class), "ProjectDomain", true);
		entityConfigRepository.register(projectDomainEntityConfig);

		// Domain
		EntityConfig<Domain> domainEntityConfig = create(new Key<>(Domain.class), "Domain", true);
		entityConfigRepository.register(domainEntityConfig);

		// Classification
		EntityConfig<Classification> classificationEntityConfig = create(new Key<>(Classification.class), "Classification", true);
		classificationEntityConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ProjectDomain.class)));
		classificationEntityConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Domain.class)));
		classificationEntityConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Status.class)));
		entityConfigRepository.register(classificationEntityConfig);

		EntityConfig<Measurement> measurementEntityConfig = create(new Key<>(Measurement.class), "MeaResult", false);
		measurementEntityConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Classification.class)));
		entityConfigRepository.register(measurementEntityConfig);

		// Unit
		EntityConfig<Unit> unitConfig = create(new Key<>(Unit.class), "Unit", false);
		unitConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(PhysicalDimension.class)));
		entityConfigRepository.register(unitConfig);

		// Quantity
		EntityConfig<Quantity> quantityConfig = create(new Key<>(Quantity.class), "Quantity", false);
		quantityConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		entityConfigRepository.register(quantityConfig);

		// Channel
		EntityConfig<Channel> channelConfig = create(new Key<>(Channel.class), "MeaQuantity", false);
		channelConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		channelConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Quantity.class)));
		entityConfigRepository.register(channelConfig);

		// ValueList
		EntityConfig<ValueListValue> valueListValueConfig = create(new Key<>(ValueListValue.class), "ValueListValue",
				true);
		valueListValueConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<ValueList> valueListConfig = create(new Key<>(ValueList.class), "ValueList", true);
		valueListConfig.addChild(valueListValueConfig);
		entityConfigRepository.register(valueListConfig);

		// ParameterSet
		EntityConfig<Parameter> parameterConfig = create(new Key<>(Parameter.class), "ResultParameter", true);
		parameterConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		EntityConfig<ParameterSet> parameterSetConfig = create(new Key<>(ParameterSet.class), "ResultParameterSet",
				true);
		parameterSetConfig.addChild(parameterConfig);
		entityConfigRepository.register(parameterSetConfig);

		// CatalogComponents
		registerCatalogComponent(ContextType.UNITUNDERTEST);
		registerCatalogComponent(ContextType.TESTSEQUENCE);
		registerCatalogComponent(ContextType.TESTEQUIPMENT);

		// TemplateRoots
		registerTemplateRoot(ContextType.UNITUNDERTEST);
		registerTemplateRoot(ContextType.TESTSEQUENCE);
		registerTemplateRoot(ContextType.TESTEQUIPMENT);

		// TemplateTestStep
		EntityConfig<TemplateTestStep> templateTestStepConfig = create(new Key<>(TemplateTestStep.class), "TplTestStep",
				true);
		templateTestStepConfig
				.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.UNITUNDERTEST)));
		templateTestStepConfig
				.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTSEQUENCE)));
		templateTestStepConfig
				.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTEQUIPMENT)));
		templateTestStepConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestStepConfig);

		// Status TestStep
		// TODO check MIME type genration
		// entityConfigRepository.register(create(new Key<>(Status.class,
		// TestStep.class), "StatusTestStep", true));

		// TestStep
		EntityConfig<TestStep> testStepConfig = create(new Key<>(TestStep.class), "TestStep", true);
		// testStepConfig.addMandatory(entityConfigRepository.findRoot(new
		// Key<>(Status.class, TestStep.class)));
		testStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateTestStep.class)));
		testStepConfig.setComparator(Sortable.COMPARATOR);
		entityConfigRepository.register(testStepConfig);

		// TemplateTest
		EntityConfig<TemplateTestStepUsage> templateTestStepUsageConfig = create(new Key<>(TemplateTestStepUsage.class),
				"TplTestStepUsage", true);
		templateTestStepUsageConfig.addMandatory(templateTestStepConfig);
		templateTestStepUsageConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<TemplateTest> templateTestConfig = create(new Key<>(TemplateTest.class), "TplTest", true);
		templateTestConfig.addChild(templateTestStepUsageConfig);
		templateTestConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestConfig);

		// Status Test
		// TODO check MIME type genration
		// entityConfigRepository.register(create(new Key<>(Status.class,
		// Test.class), "StatusTest", true));

		// Test
		EntityConfig<Test> testConfig = create(new Key<>(Test.class), "Test", true);
		testConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(User.class)));
		// testConfig.addMandatory(entityConfigRepository.findRoot(new
		// Key<>(Status.class, Test.class)));
		testConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateTest.class)));
		entityConfigRepository.register(testConfig);

		// ContextRoots
		registerContextRoot(ContextType.UNITUNDERTEST);
		registerContextRoot(ContextType.TESTSEQUENCE);
		registerContextRoot(ContextType.TESTEQUIPMENT);

		LOGGER.debug("Entity configurations loaded in {} ms.", System.currentTimeMillis() - start);
	}

	/**
	 * Loads the {@link EntityConfig}s required for {@link ContextRoot} with
	 * given {@link ContextType}.
	 *
	 * @param contextType
	 *            The {@code ContextType}.
	 */
	private void registerContextRoot(ContextType contextType) {
		EntityConfig<ContextRoot> contextRootConfig = create(new Key<>(ContextRoot.class, contextType),
				ODSUtils.CONTEXTTYPES.get(contextType), true);
		contextRootConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, contextType)));
		contextRootConfig.getEntityType().getChildRelations().stream().map(Relation::getTarget).forEach(contextComponentEntityType -> {
			EntityConfig<ContextComponent> contextComponentConfig = create(
					new Key<>(ContextComponent.class, contextType), contextComponentEntityType.getName(), true);
			contextComponentConfig
					.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateComponent.class, contextType)));
			contextRootConfig.addChild(contextComponentConfig);
			if (contextType.isTestEquipment()) {
				contextComponentEntityType.getChildRelations().stream().map(Relation::getTarget).forEach(contextSensorEntityType -> {
					EntityConfig<ContextSensor> contextSensorConfig = create(new Key<>(ContextSensor.class),
							contextSensorEntityType.getName(), true);
					contextSensorConfig
							.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateSensor.class)));
					contextComponentConfig.addChild(contextSensorConfig);
				});
			}
		});
		entityConfigRepository.register(contextRootConfig);
	}

	/**
	 * Loads the {@link EntityConfig}s required for {@link TemplateRoot} with
	 * given {@link ContextType}.
	 *
	 * @param contextType
	 *            The {@code ContextType}.
	 */
	private void registerTemplateRoot(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.get(contextType);
		EntityConfig<TemplateAttribute> templateAttributeConfig = create(
				new Key<>(TemplateAttribute.class, contextType), new StringBuilder().append("Tpl").append(odsName).append("Attr").toString(), true);
		templateAttributeConfig
				.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class, contextType)));
		templateAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
		EntityConfig<TemplateComponent> templateComponentConfig = create(
				new Key<>(TemplateComponent.class, contextType), new StringBuilder().append("Tpl").append(odsName).append("Comp").toString(), true);
		templateComponentConfig.addChild(templateAttributeConfig);
		templateComponentConfig
				.addMandatory(entityConfigRepository.findRoot(new Key<>(CatalogComponent.class, contextType)));
		templateComponentConfig.addChild(templateComponentConfig);
		templateComponentConfig.setComparator(Sortable.COMPARATOR);
		if (contextType.isTestEquipment()) {
			EntityConfig<TemplateAttribute> templateSensorAttributeConfig = create(new Key<>(TemplateAttribute.class),
					"TplSensorAttr", true);
			templateSensorAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
			templateSensorAttributeConfig
					.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class)));
			EntityConfig<TemplateSensor> templateSensorConfig = create(new Key<>(TemplateSensor.class), "TplSensor",
					true);
			templateSensorConfig.addChild(templateSensorAttributeConfig);
			templateSensorConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Quantity.class)));
			templateSensorConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogSensor.class)));
			templateSensorConfig.setComparator(Sortable.COMPARATOR);
			templateComponentConfig.addChild(templateSensorConfig);
		}
		EntityConfig<TemplateRoot> templateRootConfig = create(new Key<>(TemplateRoot.class, contextType),
				new StringBuilder().append("Tpl").append(odsName).append("Root").toString(), true);
		templateRootConfig.addChild(templateComponentConfig);
		templateRootConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateRootConfig);
	}

	/**
	 * Loads the {@link EntityConfig}s required for {@link CatalogComponent}
	 * with given {@link ContextType}.
	 *
	 * @param contextType
	 *            The {@code ContextType}.
	 */
	private void registerCatalogComponent(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.get(contextType);
		EntityConfig<CatalogAttribute> catalogAttributeConfig = create(new Key<>(CatalogAttribute.class, contextType),
				new StringBuilder().append("Cat").append(odsName).append("Attr").toString(), true);
		catalogAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
		catalogAttributeConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<CatalogComponent> catalogComponentConfig = create(new Key<>(CatalogComponent.class, contextType),
				new StringBuilder().append("Cat").append(odsName).append("Comp").toString(), true);
		catalogComponentConfig.addChild(catalogAttributeConfig);
		if (contextType.isTestEquipment()) {
			EntityConfig<CatalogAttribute> catalogSensorAttributeConfig = create(new Key<>(CatalogAttribute.class),
					"CatSensorAttr", true);
			catalogSensorAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
			EntityConfig<CatalogSensor> catalogSensorConfig = create(new Key<>(CatalogSensor.class), "CatSensor", true);
			catalogSensorConfig.addChild(catalogSensorAttributeConfig);
			catalogComponentConfig.addChild(catalogSensorConfig);
		}
		entityConfigRepository.register(catalogComponentConfig);
	}

	/**
	 * Creates a new {@link EntityConfig}.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param key
	 *            Used as identifier.
	 * @param typeName
	 *            Name of the associated {@link EntityType}.
	 * @param appendName
	 *            Flag indicates whether to append the entity types base name to
	 *            the MIME type.
	 * @return The created {@code EntityConfig} is returned.
	 */
	private <T extends Entity> EntityConfig<T> create(Key<T> key, String typeName, boolean appendName) {
		EntityConfig<T> entityConfig = new EntityConfig<>(key);
		ODSEntityType entityType = (ODSEntityType) getEntityType(typeName);
		entityConfig.setEntityType(entityType);
		entityConfig.setMimeType(buildDefaultMimeType(entityType, appendName));
		return entityConfig;
	}

	/**
	 * Creates a default MIME type for given {@link EntityType}.
	 *
	 * @param entityType
	 *            The {@code EntityType}.
	 * @param appendName
	 *            Flag indicates whether to append the entity types base name to
	 *            the MIME type.
	 * @return The created MIME type {@code String} is returned.
	 */
	private String buildDefaultMimeType(ODSEntityType entityType, boolean appendName) {
		StringBuilder sb = new StringBuilder();
		sb.append("application/x-asam.");
		sb.append(StringUtils.lowerCase(entityType.getBaseName(), Locale.ROOT));
		if (appendName) {
			sb.append('.').append(StringUtils.lowerCase(entityType.getName(), Locale.ROOT));
		}
		return sb.toString();
	}

}
