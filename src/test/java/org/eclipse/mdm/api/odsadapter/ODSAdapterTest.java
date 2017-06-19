package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_USER;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.massdata.WriteRequestBuilder;
import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateRoot;
import org.eclipse.mdm.api.dflt.model.TemplateTest;
import org.eclipse.mdm.api.dflt.model.TemplateTestStep;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODSAdapterTest {

	/*
	 * ATTENTION: ==========
	 *
	 * To run this test make sure the target service is running a MDM default
	 * model and any database constraint which enforces a relation of Test to a
	 * parent entity is deactivated!
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSAdapterTest.class);

	private static final String NAME_SERVICE = "corbaloc::1.2@%s:%s/NameService";

	private static final String USER = "sa";
	private static final String PASSWORD = "sa";

	private static EntityManager entityManager;
	private static EntityFactory entityFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws ConnectionException {
		String nameServiceHost = System.getProperty("host");
		String nameServicePort = System.getProperty("port");
		String serviceName = System.getProperty("service");

		if (nameServiceHost == null || nameServiceHost.isEmpty()) {
			throw new IllegalArgumentException("name service host is unknown: define system property 'host'");
		}

		nameServicePort = nameServicePort == null || nameServicePort.isEmpty() ? String.valueOf(2809) : nameServicePort;
		if (nameServicePort == null || nameServicePort.isEmpty()) {
			throw new IllegalArgumentException("name service port is unknown: define system property 'port'");
		}

		if (serviceName == null || serviceName.isEmpty()) {
			throw new IllegalArgumentException("service name is unknown: define system property 'service'");
		}

		Map<String, String> connectionParameters = new HashMap<>();
		connectionParameters.put(PARAM_NAMESERVICE, String.format(NAME_SERVICE, nameServiceHost, nameServicePort));
		connectionParameters.put(PARAM_SERVICENAME, serviceName + ".ASAM-ODS");
		connectionParameters.put(PARAM_USER, USER);
		connectionParameters.put(PARAM_PASSWORD, PASSWORD);

		entityManager = new ODSEntityManagerFactory().connect(connectionParameters);
		entityFactory = entityManager.getEntityFactory()
				.orElseThrow(() -> new IllegalStateException("Entity manager factory not available."));
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (entityManager != null) {
			entityManager.close();
		}
	}

	@org.junit.Test
	public void runtTestScript() throws DataAccessException {
		List<CatalogComponent> catalogComponents = createCatalogComponents();
		List<TemplateRoot> templateRoots = createTemplateRoots(catalogComponents);
		List<TemplateTestStep> templateTestSteps = createTemplateTestSteps(templateRoots);
		TemplateTest templateTest = createTemplateTest(templateTestSteps);
		PhysicalDimension physicalDimension = entityFactory.createPhysicalDimension("any_physical_dimension");
		Unit unit = entityFactory.createUnit("any_unit", physicalDimension);
		Quantity quantity = entityFactory.createQuantity("any_quantity", unit);

		Transaction transaction = entityManager.startTransaction();
		try {
			create(transaction, "catalog components", catalogComponents);
			create(transaction, "template roots", templateRoots);
			create(transaction, "template test steps", templateTestSteps);
			create(transaction, "template test", Collections.singletonList(templateTest));
			create(transaction, "physical dimension", Collections.singletonList(physicalDimension));
			create(transaction, "unit", Collections.singletonList(unit));
			create(transaction, "quantity", Collections.singletonList(quantity));

			transaction.commit();
		} catch (RuntimeException e) {
			transaction.abort();
			fail("Unable to create test data due to: " + e.getMessage());
		}

		List<Project> projects = Collections.emptyList();
		try {
			projects = createTestData(templateTest, quantity);
		} catch (DataAccessException | RuntimeException e) {
			e.printStackTrace();
		}

		transaction = entityManager.startTransaction();
		try {
			// delete in reverse order!
			if (!projects.isEmpty()) {
				delete(transaction, "projects and their children", projects);
			}

			delete(transaction, "quantity", Collections.singletonList(quantity));
			delete(transaction, "unit", Collections.singletonList(unit));
			delete(transaction, "physical dimension", Collections.singletonList(physicalDimension));
			delete(transaction, "template test", Collections.singletonList(templateTest));
			delete(transaction, "template test steps", templateTestSteps);
			delete(transaction, "template roots", templateRoots);
			delete(transaction, "catalog components", catalogComponents);

			transaction.commit();
		} catch (RuntimeException e) {
			transaction.abort();
			fail("Unable to delete test data due to: " + e.getMessage());
		}

		if (projects.isEmpty()) {
			fail("Was unable to create test data.");
		}
	}

	private List<Project> createTestData(TemplateTest templateTest, Quantity quantity) throws DataAccessException {

		Project project = entityFactory.createProject("simple_project");
		Pool pool = entityFactory.createPool("simple_pool", project);

		List<Test> tests = createTests(2, pool, templateTest);

		// create measurement test data
		List<WriteRequest> writeRequests = new ArrayList<>();
		for (Test test : tests) {
			for (TestStep testStep : test.getCommissionedTestSteps()) {
				Optional<TemplateTestStep> templateTestStep = TemplateTestStep.of(testStep);
				ContextRoot[] contextRoots = new ContextRoot[0];
				if (templateTestStep.isPresent()) {
					contextRoots = templateTestStep.get().getTemplateRoots().stream()
							.map(templateRoot -> entityFactory.createContextRoot(templateRoot))
							.toArray(ContextRoot[]::new);
				}
				for (int i = 1; i < 3; i++) {
					Measurement measurement = entityFactory.createMeasurement("measurement_" + i, testStep,
							contextRoots);

					// create channels
					List<Channel> channels = new ArrayList<>();
					for (int j = 0; j < 9; j++) {
						channels.add(entityFactory.createChannel("channel_ " + j, measurement, quantity));
					}

					// create channel group
					ChannelGroup channelGroup = entityFactory.createChannelGroup("group", 10, measurement);
					writeRequests.addAll(createMeasurementData(measurement, channelGroup, channels));
				}
			}
		}

		Transaction transaction = entityManager.startTransaction();
		try {
			create(transaction, "project and pool with tests based on teamplates with measurements and mass data",
					Collections.singleton(project));

			transaction.writeMeasuredValues(writeRequests);
			transaction.commit();
			return Collections.singletonList(project);
		} catch (DataAccessException | RuntimeException e) {
			e.printStackTrace();
			transaction.abort();
		}

		return Collections.emptyList();
	}

	private List<WriteRequest> createMeasurementData(Measurement measurement, ChannelGroup channelGroup,
			List<Channel> channels) {
		// set length of the channel value sequence
		List<WriteRequest> writeRequests = new ArrayList<>();

		// populate channel value write requests - one per channel
		Collections.sort(channels, (c1, c2) -> c1.getName().compareTo(c2.getName()));

		WriteRequestBuilder wrb = WriteRequest.create(channelGroup, channels.get(0), AxisType.X_AXIS);
		writeRequests.add(wrb.implicitLinear(ScalarType.FLOAT, 0, 1).independent().build());

		wrb = WriteRequest.create(channelGroup, channels.get(1), AxisType.Y_AXIS);
		writeRequests.add(wrb.explicit()
				.booleanValues(new boolean[] { true, true, false, true, true, false, true, false, false, false })
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(2), AxisType.Y_AXIS);
		writeRequests.add(wrb.explicit().byteValues(new byte[] { 5, 32, 42, 9, 17, 65, 13, 8, 15, 21 }).build());

		wrb = WriteRequest.create(channelGroup, channels.get(3), AxisType.Y_AXIS);
		writeRequests.add(
				wrb.explicit().integerValues(new int[] { 423, 645, 221, 111, 675, 353, 781, 582, 755, 231 }).build());

		wrb = WriteRequest.create(channelGroup, channels.get(4), AxisType.Y_AXIS);
		writeRequests.add(wrb.explicit()
				.stringValues(new String[] { "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10" }).build());

		LocalDateTime now = LocalDateTime.now();
		wrb = WriteRequest.create(channelGroup, channels.get(5), AxisType.Y_AXIS);
		writeRequests
				.add(wrb.explicit()
						.dateValues(new LocalDateTime[] { now, now.plusDays(1), now.plusDays(2), now.plusDays(3),
								now.plusDays(4), now.plusDays(5), now.plusDays(6), now.plusDays(7), now.plusDays(8),
								now.plusDays(9) })
						.build());

		wrb = WriteRequest.create(channelGroup, channels.get(6), AxisType.Y_AXIS);
		writeRequests.add(wrb.explicit().byteStreamValues(new byte[][] { { 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 9, 10 },
				{ 11 }, { 12, 13, 14 }, { 15, 16 }, { 17, 18, 19, 20 }, { 21, 22 }, { 23 } }).build());

		wrb = WriteRequest.create(channelGroup, channels.get(7), AxisType.Y_AXIS);
		writeRequests.add(wrb.implicitConstant(ScalarType.SHORT, Short.MAX_VALUE).build());

		wrb = WriteRequest.create(channelGroup, channels.get(8), AxisType.Y_AXIS);
		writeRequests.add(wrb.implicitSaw(ScalarType.FLOAT, 0, 1, 4).build());

		return writeRequests;
	}

	private static void delete(Transaction transaction, String key, Collection<? extends Deletable> entities)
			throws DataAccessException {
		LOGGER.info(">>>>>>>>>>>>>>>>> deleting " + key + "...");
		long start = System.currentTimeMillis();
		transaction.delete(entities);
		LOGGER.info(">>>>>>>>>>>>>>>>> " + key + " deleted in " + (System.currentTimeMillis() - start) + " ms");
	}

	private static void create(Transaction transaction, String key, Collection<? extends Entity> entities)
			throws DataAccessException {
		LOGGER.info(">>>>>>>>>>>>>>>>> creating " + key + "...");
		long start = System.currentTimeMillis();
		transaction.create(entities);
		LOGGER.info(">>>>>>>>>>>>>>>>> " + key + " written in " + (System.currentTimeMillis() - start) + " ms");
	}

	private List<Test> createTests(int count, Pool pool, TemplateTest templateTest) {
		return IntStream.range(1, ++count)
				.mapToObj(i -> entityFactory.createTest("simple_test_" + i, pool, templateTest))
				.collect(Collectors.toList());
	}

	private TemplateTest createTemplateTest(List<TemplateTestStep> templateTestSteps) {
		TemplateTest templateTest = entityFactory.createTemplateTest("tpl_test");
		templateTestSteps.forEach(tts -> {
			entityFactory.createTemplateTestStepUsage(UUID.randomUUID().toString(), templateTest, tts);
		});
		return templateTest;
	}

	private List<TemplateTestStep> createTemplateTestSteps(List<TemplateRoot> templateRoots) {
		// make sure each context type is given only once
		templateRoots.stream().collect(Collectors.toMap(TemplateRoot::getContextType, Function.identity()));

		List<TemplateTestStep> templateTestSteps = new ArrayList<>();
		TemplateTestStep templateTestStep1 = entityFactory.createTemplateTestStep("tpl_test_step_1");
		templateRoots.forEach(tr -> templateTestStep1.setTemplateRoot(tr));
		templateTestSteps.add(templateTestStep1);
		TemplateTestStep templateTestStep2 = entityFactory.createTemplateTestStep("tpl_test_step_2");
		templateRoots.forEach(tr -> templateTestStep2.setTemplateRoot(tr));
		templateTestSteps.add(templateTestStep2);

		return templateTestSteps;
	}

	private List<TemplateRoot> createTemplateRoots(List<CatalogComponent> catalogComponents) {
		Map<ContextType, List<CatalogComponent>> groups = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		List<TemplateRoot> templateRoots = new ArrayList<>();
		groups.forEach((contextType, catalogComps) -> {
			TemplateRoot templateRoot = entityFactory.createTemplateRoot(contextType,
					"tpl_" + toLower(contextType.name()) + "_root");
			// create child template components for template root
			catalogComps.forEach(catalogComp -> {
				TemplateComponent templateComponent = entityFactory
						.createTemplateComponent("tpl_" + catalogComp.getName() + "_parent", templateRoot, catalogComp);
				entityFactory.createTemplateComponent("tpl_" + catalogComp.getName() + "_child", templateComponent,
						catalogComp);
			});

			templateRoots.add(templateRoot);
		});

		return templateRoots;
	}

	private List<CatalogComponent> createCatalogComponents() {
		List<CatalogComponent> catalogComponents = new ArrayList<>();
		catalogComponents.add(createCatalogComponent(ContextType.UNITUNDERTEST));
		catalogComponents.add(createCatalogComponent(ContextType.TESTSEQUENCE));
		catalogComponents.add(createCatalogComponent(ContextType.TESTEQUIPMENT));
		return catalogComponents;
	}

	private CatalogComponent createCatalogComponent(ContextType contextType) {
		CatalogComponent catalogComponent = entityFactory.createCatalogComponent(contextType,
				toLower(contextType.name()));

		entityFactory.createCatalogAttribute("string", ValueType.STRING, catalogComponent);
		entityFactory.createCatalogAttribute("date", ValueType.DATE, catalogComponent);
		entityFactory.createCatalogAttribute("long", ValueType.LONG, catalogComponent);
		entityFactory.createCatalogAttribute("file_link", ValueType.FILE_LINK, catalogComponent);
		entityFactory.createCatalogAttribute("file_link_array", ValueType.FILE_LINK_SEQUENCE, catalogComponent);
		entityFactory.createCatalogAttribute("scalar_type", ScalarType.class, catalogComponent);

		return catalogComponent;
	}

	private static String toLower(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

}
