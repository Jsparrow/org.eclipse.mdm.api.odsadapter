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

		loadApplicationModel();
		loadEntityConfigurations();
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

		ApplicationStructureValue asv = aoSession.getApplicationStructureValue();
		Map<Long, String> units = getUnitMapping(asv);

		// create entity types (incl. attributes)
		Map<Long, ODSEntityType> entityTypesByID = new HashMap<>();
		String sourceName = aoSession.getName();
		for(ApplElem applElem : asv.applElems) {
			Long odsID = ODSConverter.fromODSLong(applElem.aid);
			Map<String, Class<? extends Enum<?>>> entityEnumMap = enumClassMap.getOrDefault(odsID, new HashMap<>());

			ODSEntityType entityType = new ODSEntityType(sourceName, applElem, units, entityEnumMap);
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

	private Map<Long, String> getUnitMapping(ApplicationStructureValue asv) throws AoException {
		ApplElem unitElem = Stream.of(asv.applElems).filter(ae -> ae.beName.equals("AoUnit")).findAny()
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

		// Environment
		entityConfigRepository.register(create(new Key<>(Environment.class), "Environment", "application/x-asam.aoenvironment"));

		// PhysicalDimension
		entityConfigRepository.register(create(new Key<>(PhysicalDimension.class), "PhysDimension", "application/x-asam.aophysicaldimension"));

		// User
		entityConfigRepository.register(create(new Key<>(User.class), "User", "application/x-asam.aouser"));

		// Measurement
		entityConfigRepository.register(create(new Key<>(Measurement.class), "MeaResult", "application/x-asam.aomeasurement"));

		// ChannelGroup
		entityConfigRepository.register(create(new Key<>(ChannelGroup.class), "SubMatrix", "application/x-asam.aosubmatrix"));

		// Unit
		EntityConfig<Unit> unitConfig = create(new Key<>(Unit.class), "Unit", "application/x-asam.aounit");
		unitConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(PhysicalDimension.class)));
		entityConfigRepository.register(unitConfig);

		// Quantity
		EntityConfig<Quantity> quantityConfig = create(new Key<>(Quantity.class), "Quantity", "application/x-asam.aoquantity");
		quantityConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		entityConfigRepository.register(quantityConfig);

		// Channel
		EntityConfig<Channel> channelConfig = create(new Key<>(Channel.class), "MeaQuantity", "application/x-asam.aomeasurementquantity");
		channelConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		channelConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Quantity.class)));
		entityConfigRepository.register(channelConfig);

		// ValueList
		EntityConfig<ValueListValue> valueListValueConfig = create(new Key<>(ValueListValue.class), "ValueListValue", "application/x-asam.aoparameter.valuelistvalue");
		valueListValueConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<ValueList> valueListConfig = create(new Key<>(ValueList.class), "ValueList", "application/x-asam.aoparameterset.valuelist");
		valueListConfig.addChild(valueListValueConfig);
		entityConfigRepository.register(valueListConfig);

		// ParameterSet
		EntityConfig<Parameter> parameterConfig = create(new Key<>(Parameter.class), "ResultParameter", "application/x-asam.aoparameter.resultparameter");
		parameterConfig.addOptional(entityConfigRepository.findRoot(new Key<>(Unit.class)));
		EntityConfig<ParameterSet> parameterSetConfig = create(new Key<>(ParameterSet.class), "ResultParameterSet", "application/x-asam.aoparameterset.resultparameterset");
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
		EntityConfig<TemplateTestStep> templateTestStepConfig = create(new Key<>(TemplateTestStep.class), "TplTestStep", "application/x-asam.aoany.tplteststep");
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.UNITUNDERTEST)));
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTSEQUENCE)));
		templateTestStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, ContextType.TESTEQUIPMENT)));
		templateTestStepConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestStepConfig);

		// Status TestStep
		//		entityConfigRepository.register(create(new Key<>(Status.class, TestStep.class), "StatusTestStep", "application/x-asam.aoany.statusteststep")); // TODO <-- correct?!

		// TestStep
		EntityConfig<TestStep> testStepConfig = create(new Key<>(TestStep.class), "TestStep", "application/x-asam.aosubtest.teststep");
		//		testStepConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Status.class, TestStep.class)));
		testStepConfig.addOptional(entityConfigRepository.findRoot(new Key<>(TemplateTestStep.class)));
		testStepConfig.setComparator(Sortable.COMPARATOR);
		entityConfigRepository.register(testStepConfig);

		// TemplateTest
		EntityConfig<TemplateTestStepUsage> templateTestStepUsageConfig = create(new Key<>(TemplateTestStepUsage.class), "TplTestStepUsage", "application/x-asam.aoany.tplteststepusage");
		templateTestStepUsageConfig.addMandatory(templateTestStepConfig);
		templateTestStepUsageConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<TemplateTest> templateTestConfig = create(new Key<>(TemplateTest.class), "TplTest", "application/x-asam.aoany.tpltest");
		templateTestConfig.addChild(templateTestStepUsageConfig);
		templateTestConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateTestConfig);

		// Status Test
		//		entityConfigRepository.register(create(new Key<>(Status.class, Test.class), "StatusTest", "application/x-asam.aoany.statustest")); // TODO <-- correct?!

		// Test
		EntityConfig<Test> testConfig = create(new Key<>(Test.class), "Test", "application/x-asam.aotest");
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
		String odsName = ODSUtils.CONTEXTTYPES.convert(contextType);
		String odsNameLC = odsName.toLowerCase();
		EntityConfig<ContextRoot> contextRootConfig = create(new Key<>(ContextRoot.class, contextType), odsName, "application/x-asam.ao" + odsNameLC + "." + odsNameLC);
		contextRootConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(TemplateRoot.class, contextType)));
		for(Relation contextComponentRelation : contextRootConfig.getEntityType().getChildRelations()) {
			EntityType contextComponentEntityType = contextComponentRelation.getTarget();
			EntityConfig<ContextComponent> contextComponentConfig = create(new Key<>(ContextComponent.class, contextType), contextComponentEntityType.getName(), "application/x-asam.ao" + odsNameLC + "part." + contextComponentEntityType.getName());
			contextComponentConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateComponent.class, contextType)));
			contextRootConfig.addChild(contextComponentConfig);
			if(contextType.isTestEquipment()) {
				for(Relation contextSensorRelation : contextComponentEntityType.getChildRelations()) {
					EntityType contextSensorEntityType = contextSensorRelation.getTarget();
					EntityConfig<ContextSensor> contextSensorConfig = create(new Key<>(ContextSensor.class), contextSensorEntityType.getName(), "application/x-asam.ao" + odsNameLC + "part." + contextComponentEntityType.getName());
					contextSensorConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(TemplateSensor.class)));
					contextComponentConfig.addChild(contextSensorConfig);
				}
			}
		}
		entityConfigRepository.register(contextRootConfig);
	}

	private void registerTemplateRoot(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.convert(contextType);
		EntityConfig<TemplateAttribute> templateAttributeConfig = create(new Key<>(TemplateAttribute.class, contextType), "Tpl" + odsName + "Attr", "TODO"); // TODO MIMETYPE
		templateAttributeConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class, contextType)));
		templateAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
		EntityConfig<TemplateComponent> templateComponentConfig = create(new Key<>(TemplateComponent.class, contextType), "Tpl" + odsName + "Comp", "TODO");  // TODO MIMETYPE
		templateComponentConfig.addChild(templateAttributeConfig);
		templateComponentConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(CatalogComponent.class, contextType)));
		templateComponentConfig.addChild(templateComponentConfig);
		templateComponentConfig.setComparator(Sortable.COMPARATOR);
		if(contextType.isTestEquipment()) {
			EntityConfig<TemplateAttribute> templateSensorAttributeConfig = create(new Key<>(TemplateAttribute.class), "TplSensorAttr", "TODO");  // TODO MIMETYPE
			templateSensorAttributeConfig.setComparator(TemplateAttribute.COMPARATOR);
			templateSensorAttributeConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogAttribute.class)));
			EntityConfig<TemplateSensor> templateSensorConfig = create(new Key<>(TemplateSensor.class), "TplSensor", "TODO");  // TODO MIMETYPE
			templateSensorConfig.addChild(templateSensorAttributeConfig);
			templateSensorConfig.addMandatory(entityConfigRepository.findRoot(new Key<>(Quantity.class)));
			templateSensorConfig.addInherited(entityConfigRepository.findImplicit(new Key<>(CatalogSensor.class)));
			templateSensorConfig.setComparator(Sortable.COMPARATOR);
			templateComponentConfig.addChild(templateSensorConfig);
		}
		EntityConfig<TemplateRoot> templateRootConfig = create(new Key<>(TemplateRoot.class, contextType), "Tpl" + odsName + "Root", "TODO"); // TODO MIMETYPE
		templateRootConfig.addChild(templateComponentConfig);
		templateRootConfig.setComparator(Versionable.COMPARATOR);
		entityConfigRepository.register(templateRootConfig);
	}

	private void registerCatalogComponent(ContextType contextType) {
		String odsName = ODSUtils.CONTEXTTYPES.convert(contextType);
		EntityConfig<CatalogAttribute> catalogAttributeConfig = create(new Key<>(CatalogAttribute.class, contextType), "Cat" + odsName + "Attr", "TODO");  // TODO MIMETYPE
		catalogAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
		catalogAttributeConfig.setComparator(Sortable.COMPARATOR);
		EntityConfig<CatalogComponent> catalogComponentConfig = create(new Key<>(CatalogComponent.class, contextType), "Cat" + odsName + "Comp", "TODO");  // TODO MIMETYPE
		catalogComponentConfig.addChild(catalogAttributeConfig);
		if(contextType.isTestEquipment()) {
			EntityConfig<CatalogAttribute> catalogSensorAttributeConfig = create(new Key<>(CatalogAttribute.class), "CatSensorAttr", "TODO");  // TODO MIMETYPE
			catalogSensorAttributeConfig.addOptional(entityConfigRepository.findRoot(new Key<>(ValueList.class)));
			EntityConfig<CatalogSensor> catalogSensorConfig = create(new Key<>(CatalogSensor.class), "CatSensor", "TODO"); // TODO MIMETYPE
			catalogSensorConfig.addChild(catalogSensorAttributeConfig);
			catalogComponentConfig.addChild(catalogSensorConfig);
		}
		entityConfigRepository.register(catalogComponentConfig);
	}

	private <T extends Entity> EntityConfig<T> create(Key<T> key, String typeName, String mimeType) {
		EntityConfig<T> entityConfig = new EntityConfig<>(key);
		entityConfig.setEntityType(getEntityType(typeName));
		entityConfig.setMimeType(mimeType);
		return entityConfig;
	}

}
