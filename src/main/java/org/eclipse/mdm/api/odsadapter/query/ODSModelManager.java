/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import org.asam.ods.AIDName;
import org.asam.ods.AggrFunc;
import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplRel;
import org.asam.ods.ApplicationStructureValue;
import org.asam.ods.ElemResultSetExt;
import org.asam.ods.EnumerationAttributeStructure;
import org.asam.ods.JoinDef;
import org.asam.ods.NameValue;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.ResultSetExt;
import org.asam.ods.SelAIDNameUnitId;
import org.asam.ods.SelItem;
import org.asam.ods.SelOrder;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextSensor;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
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
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
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
import org.eclipse.mdm.api.odsadapter.utils.ODSEnumerations;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highqsoft.corbafileserver.generated.CORBAFileServerIF;

public class ODSModelManager implements ModelManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSModelManager.class);

	private final Map<String, EntityType> entityTypesByName = new HashMap<>();

	private final CORBAFileServerIF fileServer;
	private final ORB orb;

	private final Lock write;
	private final Lock read;

	private EntityConfigRepository entityConfigRepository;

	// TODO quick fix => REMOVE THSI AS SOON AS POSSIBLE!
	@Deprecated private boolean isAthos;

	private ApplElemAccess applElemAccess;
	private AoSession aoSession;

	{
		ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
		write = reentrantReadWriteLock.writeLock();
		read = reentrantReadWriteLock.readLock();
	}

	public ODSModelManager(ORB orb, AoSession aoSession, CORBAFileServerIF fileServer) throws AoException {
		this.fileServer = fileServer;
		this.aoSession = aoSession;
		this.orb = orb;
		applElemAccess = aoSession.getApplElemAccess();

		try {
			NameValue nv = aoSession.getContextByName("ATHOS_VERSION");
			isAthos = nv != null;
		} catch(AoException e) {
			isAthos = false;
		}

		initialize();
	}

	public ODSModelManager newSession() throws AoException {
		return new ODSModelManager(orb, getAoSession().createCoSession(), fileServer);
	}

	public CORBAFileServerIF getFileServer() {
		return fileServer;
	}

	public boolean isAthos() {
		return isAthos;
	}

	public ORB getORB() {
		return orb;
	}

	/*
	 * ################################################################################################################
	 */

	public <T extends Entity> EntityConfig<T> findEntityConfig(Key<T> key) {
		read.lock();

		try {
			return entityConfigRepository.find(key);
		} finally {
			read.unlock();
		}
	}

	public <T extends Entity> EntityConfig<T> getEntityConfig(Key<T> key) {
		read.lock();

		try {
			return entityConfigRepository.findRoot(key);
		} finally {
			read.unlock();
		}
	}

	public EntityConfig<?> getEntityConfig(EntityType entityType) {
		read.lock();

		try {
			return entityConfigRepository.find(entityType);
		} finally {
			read.unlock();
		}
	}

	/*
	 * ################################################################################################################
	 */

	@Override
	public Query createQuery() {
		read.lock();

		try {
			return new ODSQuery(getApplElemAccess());
		} finally {
			read.unlock();
		}
	}

	@Override
	public List<EntityType> listEntityTypes() {
		read.lock();

		try {
			return new ArrayList<>(entityTypesByName.values());
		} finally {
			read.unlock();
		}
	}

	@Override
	public EntityType getEntityType(Class<? extends Entity> entityClass) {
		read.lock();

		try{
			return getEntityConfig(new Key<>(entityClass)).getEntityType();
		} finally {
			read.unlock();
		}
	}

	@Override
	public EntityType getEntityType(Class<? extends Entity> entityClass, ContextType contextType) {
		read.lock();

		try{
			return getEntityConfig(new Key<>(entityClass, contextType)).getEntityType();
		} finally {
			read.unlock();
		}
	}

	@Override
	public EntityType getEntityType(String name) {
		read.lock();

		try {
			EntityType entityType = entityTypesByName.get(name);
			if(entityType == null) {
				throw new IllegalArgumentException("Entity with name '" + name + "' not found.");
			}

			return entityType;
		} finally {
			read.unlock();
		}
	}

	public AoSession getAoSession() {
		write.lock();

		try {
			return aoSession;
		} finally {
			write.unlock();
		}
	}

	public ApplElemAccess getApplElemAccess() {
		read.lock();

		try {
			return applElemAccess;
		} finally {
			read.unlock();
		}
	}

	public void close() throws AoException {
		read.lock();

		try {
			applElemAccess._release();
			aoSession.close();
		} finally {
			read.unlock();
		}
		aoSession._release();
	}

	public void reloadApplicationModel() {
		write.lock();

		AoSession aoSessionOld = aoSession;
		ApplElemAccess applElemAccessOld = applElemAccess;
		try {
			entityTypesByName.clear();

			aoSession = aoSession.createCoSession();
			applElemAccess = aoSession.getApplElemAccess();
			initialize();
		} catch(AoException e) {
			LOGGER.error("Unable to reload the application model due to: " + e.reason, e);
		} finally {
			write.unlock();
		}

		try {
			applElemAccessOld._release();
			aoSessionOld.close();
		} catch(AoException e) {
			LOGGER.debug("Unable to close replaced session due to: " + e.reason, e);
		} finally {
			aoSessionOld._release();
		}
	}

	private void initialize() throws AoException {
		loadApplicationModel();
		loadEntityConfigurations();
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

		ApplicationStructureValue applicationStructureValue = aoSession.getApplicationStructureValue();
		Map<Long, String> units = getUnitMapping(applicationStructureValue.applElems);

		// create entity types (incl. attributes)
		Map<Long, ODSEntityType> entityTypesByID = new HashMap<>();
		String sourceName = aoSession.getName();
		for(ApplElem applElem : applicationStructureValue.applElems) {
			Long odsID = ODSConverter.fromODSLong(applElem.aid);
			Map<String, Class<? extends Enum<?>>> entityEnumMap = enumClassMap.getOrDefault(odsID, new HashMap<>());

			ODSEntityType entityType = new ODSEntityType(sourceName, applElem, units, entityEnumMap);
			entityTypesByName.put(applElem.aeName, entityType);
			entityTypesByID.put(odsID, entityType);
		}

		// create relations
		List<Relation> relations = new ArrayList<>();
		for(ApplRel applRel : applicationStructureValue.applRels) {
			EntityType source = entityTypesByID.get(ODSConverter.fromODSLong(applRel.elem1));
			EntityType target = entityTypesByID.get(ODSConverter.fromODSLong(applRel.elem2));
			relations.add(new ODSRelation(applRel, source, target));
		}

		// assign relations to their source entity types
		relations.stream().collect(groupingBy(Relation::getSource)).forEach((e, r) -> ((ODSEntityType) e).setRelations(r));

		long stop = System.currentTimeMillis();
		LOGGER.debug("{} entity types with {} relations found in {} ms.", entityTypesByName.size(), relations.size(), stop - start);
	}

	private Map<Long, String> getUnitMapping(ApplElem[] applElems) throws AoException {
		ApplElem unitElem = Stream.of(applElems).filter(ae -> ae.beName.equals("AoUnit")).findAny()
				.orElseThrow(() -> new IllegalStateException("Application element 'Unit' is not defined."));

		QueryStructureExt qse = new QueryStructureExt();
		qse.anuSeq = new SelAIDNameUnitId[] {
				new SelAIDNameUnitId(new AIDName(unitElem.aid, "Id"), new T_LONGLONG(), AggrFunc.NONE),
				new SelAIDNameUnitId(new AIDName(unitElem.aid, "Name"), new T_LONGLONG(), AggrFunc.NONE)};
		qse.condSeq = new SelItem[0];
		qse.groupBy = new AIDName[0];
		qse.joinSeq = new JoinDef[0];
		qse.orderBy = new SelOrder[0];

		Map<Long, String> units = new HashMap<>();
		for(ResultSetExt resultSetExt : getApplElemAccess().getInstancesExt(qse, 0)) {
			ElemResultSetExt unitResultSetExt = resultSetExt.firstElems[0];
			for(int i = 0; i < unitResultSetExt.values[0].value.flag.length; i++) {
				Long unitID = ODSConverter.fromODSLong(unitResultSetExt.values[0].value.u.longlongVal()[i]);
				String unitName = unitResultSetExt.values[1].value.u.stringVal()[i];
				units.put(unitID, unitName);
			}
		}

		return units;
	}

	private void loadEntityConfigurations() {
		LOGGER.debug("Loading entity configurations...");
		long start = System.currentTimeMillis();

		entityConfigRepository = new EntityConfigRepository();

		// Environment | PhysicalDimension | User | Measurement | ChannelGroup
		entityConfigRepository.register(create(new Key<>(Environment.class), "Environment", false));
		entityConfigRepository.register(create(new Key<>(PhysicalDimension.class), "PhysDimension", false));
		entityConfigRepository.register(create(new Key<>(User.class), "User", false));
		entityConfigRepository.register(create(new Key<>(Measurement.class), "MeaResult", false));
		entityConfigRepository.register(create(new Key<>(ChannelGroup.class), "SubMatrix", false));

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
		EntityConfig<ValueListValue> valueListValueConfig = create(new Key<>(ValueListValue.class), "ValueListValue", true);
		valueListValueConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<ValueList> valueListConfig = create(new Key<>(ValueList.class), "ValueList", true);
		valueListConfig.addChild(valueListValueConfig);
		entityConfigRepository.register(valueListConfig);

		// ParameterSet
		EntityConfig<Parameter> parameterConfig = create(new Key<>(Parameter.class), "ResultParameter", true);
		parameterConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		EntityConfig<ParameterSet> parameterSetConfig = create(new Key<>(ParameterSet.class), "ResultParameterSet", true);
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
		EntityConfig<TemplateTestStep> templateTestStepConfig = create(new Key<>(TemplateTestStep.class), "TplTestStep", true);
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.UNITUNDERTEST)));
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTSEQUENCE)));
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTEQUIPMENT)));
		templateTestStepConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestStepConfig);

		// Status TestStep
		//		entityConfigRepository.register(create(new Key<>(Status.class, TestStep.class), "StatusTestStep", true)); // TODO <-- correct?!

		// TestStep
		EntityConfig<TestStep> testStepConfig = create(new Key<>(TestStep.class), "TestStep", false); // TODO true if project and structurelevel defined!
		//		testStepConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Status.class, TestStep.class)));
		testStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateTestStep.class)));
		testStepConfig.setComparator(Sortable.COMPARATOR);
		entityConfigRepository.register(testStepConfig);

		// TemplateTest
		EntityConfig<TemplateTestStepUsage> templateTestStepUsageConfig = create(new Key<>(TemplateTestStepUsage.class), "TplTestStepUsage", true);
		templateTestStepUsageConfig.addMandatory(templateTestStepConfig);
		templateTestStepUsageConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<TemplateTest> templateTestConfig = create(new Key<>(TemplateTest.class), "TplTest", true);
		templateTestConfig.addChild(templateTestStepUsageConfig);
		templateTestConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestConfig);

		// Status Test
		//		entityConfigRepository.register(create(new Key<>(Status.class, Test.class), "StatusTest", true)); // TODO <-- correct?!

		// Test
		EntityConfig<Test> testConfig = create(new Key<>(Test.class), "Test", false);// TODO true if project and structurelevel defined!
		testConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(User.class)));
		//		testConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Status.class, Test.class)));
		testConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateTest.class)));
		entityConfigRepository.register(testConfig);

		// ContextRoots
		registerContextRoot(ContextType.UNITUNDERTEST);
		registerContextRoot(ContextType.TESTSEQUENCE);
		registerContextRoot(ContextType.TESTEQUIPMENT);

		LOGGER.debug("Entity configurations loaded in {} ms.", System.currentTimeMillis() - start);
	}

	private void registerContextRoot(ContextType contextType) {
		EntityConfig<ContextRoot> contextRootConfig = create(new Key<>(ContextRoot.class, contextType), ODSUtils.CONTEXTTYPES.convert(contextType), true);
		contextRootConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, contextType)));
		for(Relation contextComponentRelation : contextRootConfig.getEntityType().getChildRelations()) {
			EntityType contextComponentEntityType = contextComponentRelation.getTarget();
			EntityConfig<ContextComponent> contextComponentConfig = create(new Key<>(ContextComponent.class, contextType), contextComponentEntityType.getName(), true);
			contextComponentConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateComponent.class, contextType)));
			contextRootConfig.addChild(contextComponentConfig);
			if(contextType.isTestEquipment()) {
				for(Relation contextSensorRelation : contextComponentEntityType.getChildRelations()) {
					EntityType contextSensorEntityType = contextSensorRelation.getTarget();
					EntityConfig<ContextSensor> contextSensorConfig = create(new Key<>(ContextSensor.class), contextSensorEntityType.getName(), true);
					contextSensorConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateSensor.class)));
					contextComponentConfig.addChild(contextSensorConfig);
				}
			}
		}
		entityConfigRepository.register(contextRootConfig);
	}

	private void registerTemplateRoot(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.convert(contextType);
		EntityConfig<TemplateAttribute> templateAttributeConfig = create(new Key<>(TemplateAttribute.class, contextType), "Tpl" + odsName + "Attr", true);
		templateAttributeConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class, contextType)));
		templateAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
		EntityConfig<TemplateComponent> templateComponentConfig = create(new Key<>(TemplateComponent.class, contextType), "Tpl" + odsName + "Comp", true);
		templateComponentConfig.addChild(templateAttributeConfig);
		templateComponentConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(CatalogComponent.class, contextType)));
		templateComponentConfig.addChild(templateComponentConfig);
		templateComponentConfig.setComparator(Sortable.COMPARATOR);
		if(contextType.isTestEquipment()) {
			EntityConfig<TemplateAttribute> templateSensorAttributeConfig = create(new Key<>(TemplateAttribute.class), "TplSensorAttr", true);
			templateSensorAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
			templateSensorAttributeConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class)));
			EntityConfig<TemplateSensor> templateSensorConfig = create(new Key<>(TemplateSensor.class), "TplSensor", true);
			templateSensorConfig.addChild(templateSensorAttributeConfig);
			templateSensorConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Quantity.class)));
			templateSensorConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogSensor.class)));
			templateSensorConfig.setComparator(Sortable.COMPARATOR);
			templateComponentConfig.addChild(templateSensorConfig);
		}
		EntityConfig<TemplateRoot> templateRootConfig = create(new Key<>(TemplateRoot.class, contextType), "Tpl" + odsName + "Root", true);
		templateRootConfig.addChild(templateComponentConfig);
		templateRootConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateRootConfig);
	}

	private void registerCatalogComponent(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.convert(contextType);
		EntityConfig<CatalogAttribute> catalogAttributeConfig = create(new Key<>(CatalogAttribute.class, contextType), "Cat" + odsName + "Attr", true);
		catalogAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
		catalogAttributeConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<CatalogComponent> catalogComponentConfig = create(new Key<>(CatalogComponent.class, contextType), "Cat" + odsName + "Comp", true);
		catalogComponentConfig.addChild(catalogAttributeConfig);
		if(contextType.isTestEquipment()) {
			EntityConfig<CatalogAttribute> catalogSensorAttributeConfig = create(new Key<>(CatalogAttribute.class), "CatSensorAttr", true);
			catalogSensorAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
			EntityConfig<CatalogSensor> catalogSensorConfig = create(new Key<>(CatalogSensor.class), "CatSensor", true);
			catalogSensorConfig.addChild(catalogSensorAttributeConfig);
			catalogComponentConfig.addChild(catalogSensorConfig);
		}
		entityConfigRepository.register(catalogComponentConfig);
	}

	private <T extends Entity> EntityConfig<T> create(Key<T> key, String typeName, boolean appendName) {
		EntityConfig<T> entityConfig = new EntityConfig<>(key);
		ODSEntityType entityType = (ODSEntityType) getEntityType(typeName);
		entityConfig.setEntityType(entityType);
		entityConfig.setMimeType(buildDefaultMimeType(entityType, appendName));
		return entityConfig;
	}

	private String buildDefaultMimeType(ODSEntityType entityType, boolean appendName) {
		StringBuilder sb = new StringBuilder();
		sb.append("application/x-asam.");
		sb.append(entityType.getBaseName().toLowerCase(Locale.ROOT));
		if(appendName) {
			sb.append('.').append(entityType.getName().toLowerCase(Locale.ROOT));
		}
		return sb.toString();
	}

}
