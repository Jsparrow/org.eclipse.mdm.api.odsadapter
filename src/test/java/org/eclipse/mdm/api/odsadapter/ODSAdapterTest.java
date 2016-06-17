package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_USER;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.ReadRequest;
import org.eclipse.mdm.api.base.massdata.ReadRequestIterable;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.massdata.WriteRequestBuilder;
import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.EntityFactory;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.PhysicalDimension;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.DefaultEntityFactory;
import org.eclipse.mdm.api.dflt.model.Status;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ODSAdapterTest {

	/*
	 * ATTENTION:
	 * ==========
	 *
	 * To run this test make sure the target service is running a
	 * MDM default model and any database constraint which enforces
	 * a relation of Test to a parent entity is deactivated!
	 */

	// TODO name service:  corbaloc::1.2@<SERVER_IP>:<SERVER_PORT>/NameService
	private static final String NAME_SERVICE = "corbaloc::1.2@<SERVER_IP>:2809/NameService";

	// TODO service name: <SERVICE_NAME>.ASAM-ODS
	private static final String SERVICE_NAME = "<SERVICE_NAME>.ASAM-ODS";

	private static final String USER = "sa";
	private static final String PASSWORD = "sa";

	private static EntityManager entityManager;

	@BeforeClass
	public static void setUpBeforeClass() throws ConnectionException {
		Map<String, String> connectionParameters = new HashMap<>();
		connectionParameters.put(PARAM_NAMESERVICE, NAME_SERVICE);
		connectionParameters.put(PARAM_SERVICENAME, SERVICE_NAME);
		connectionParameters.put(PARAM_USER, USER);
		connectionParameters.put(PARAM_PASSWORD, PASSWORD);

		entityManager = new ODSEntityManagerFactory().connect(connectionParameters);
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		entityManager.close();
	}

	@org.junit.Test
	public void runtTestScript() throws DataAccessException {
		try {
			createTestData();
			readMeasurementData();
			updateTestData();
		} catch(DataAccessException e) {
			org.junit.Assert.fail("unable to write data");
		} finally {
			deleteTestData();
		}
	}

	private void createTestData() throws DataAccessException {
		Transaction transaction = entityManager.startTransaction();

		Status testStatus = entityManager.loadAllStatus(Test.class).get(0);
		Status testStepStatus = entityManager.loadAllStatus(TestStep.class).get(0);

		try {
			int numberOfTests = 2; 			// number of tests
			int numberOfTestSteps = 3; 		// number of test steps per test
			int numberOfMeasurements = 1; 	// number of measurements per test step
			int numberOfChannels = 9;		// number of channels per measurement
			Quantity quantity = getQuantity();

			DefaultEntityFactory entityFactory = (DefaultEntityFactory)entityManager.getEntityFactory().get();

			List<Test> tests = new ArrayList<>();
			List<WriteRequest> writeRequests = new ArrayList<>();
			for(int i = 0; i < numberOfTests; i++) {
				tests.add(entityFactory.createTest(USER + "_Test_" + i, testStatus, entityManager.loadLoggedOnUser().get()));
			}


			// create test steps for each test
			for (Test test : tests) {
				List<TestStep> testSteps = new ArrayList<>();
				for(int i = 0; i < numberOfTestSteps; i++) {
					testSteps.add(entityFactory.createTestStep(USER + "_TestStep_" + i, test, testStepStatus));
				}

				// create measurements for each test step
				for (TestStep testStep : testSteps) {
					List<Measurement> measurements = new ArrayList<>();
					for(int i = 0; i < numberOfMeasurements; i++) {
						measurements.add(entityFactory.createMeasurement(USER + "_Measurement_" + i, testStep));
					}

					// create channels and channel group for each measurement
					for (Measurement measurement : measurements) {
						// create channels
						List<Channel> channels = new ArrayList<>();
						for(int i = 0; i < numberOfChannels; i++) {
							channels.add(entityFactory.createChannel(USER + "_Channel_ " + i, measurement, quantity));
						}

						// create channel group
						ChannelGroup channelGroup = entityFactory.createChannelGroup(USER + "_ChannelGroup_ ", 10, measurement);
						writeRequests.addAll(createMeasurementData(measurement, channelGroup, channels));
					}
				}
			}

			transaction.create(tests);
			transaction.writeMeasuredValues(writeRequests);
			transaction.commit();
		} catch(DataAccessException e) {
			e.printStackTrace();
			transaction.abort();
			throw e;
		}
	}

	private void updateTestData() throws DataAccessException {
		// update description and responsible person
		List<Test> tests = entityManager.loadAll(Test.class, USER + "_Test_*");
		for(Test test : tests) {
			test.setDescription("new description");
			test.setResponsiblePerson(entityManager.loadAll(User.class).get(0));
		}

		Transaction transaction = entityManager.startTransaction();
		try {
			transaction.update(tests);
			transaction.commit();
		} catch(DataAccessException e) {
			e.printStackTrace();
			transaction.abort();
			throw e;
		}
	}

	private List<WriteRequest> createMeasurementData(Measurement measurement, ChannelGroup channelGroup, List<Channel> channels) {
		// set length of the channel value sequence
		List<WriteRequest> writeRequests = new ArrayList<>();

		// populate channel value write requests - one per channel
		Collections.sort(channels, (c1, c2) -> c1.getName().compareTo(c2.getName()));

		WriteRequestBuilder wrb = WriteRequest.create(channelGroup, channels.get(0), AxisType.X_AXIS);
		writeRequests.add(wrb
				.implicitLinear(ScalarType.FLOAT, 0, 1)
				.independent()
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(1), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.booleanValues(new boolean[] { true,true,false,true,true,false,true,false,false,false })
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(2), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.byteValues(new byte[] { 5,32,42,9,17,65,13,8,15,21 })
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(3), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.integerValues(new int[] { 423,645,221,111,675,353,781,582,755,231 })
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(4), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.stringValues(new String[] { "s1","s2","s3","s4","s5","s6","s7","s8","s9","s10" })
				.build());

		LocalDateTime now = LocalDateTime.now();
		wrb = WriteRequest.create(channelGroup, channels.get(5), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.dateValues(new LocalDateTime[] { now,now.plusDays(1),now.plusDays(2),
						now.plusDays(3),now.plusDays(4),now.plusDays(5), now.plusDays(6),now.plusDays(7),
						now.plusDays(8),now.plusDays(9) })
				.independent().build());

		wrb = WriteRequest.create(channelGroup, channels.get(6), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.explicit()
				.byteStreamValues(new byte[][] {{1,2},{3,4,5},{6,7,8},{9,10},{11},{12,13,14},
					{15,16},{17,18,19,20},{21,22},{23} })
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(7), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.implicitConstant(ScalarType.SHORT, Short.MAX_VALUE)
				.build());

		wrb = WriteRequest.create(channelGroup, channels.get(8), AxisType.Y_AXIS);
		writeRequests.add(wrb
				.implicitSaw(ScalarType.FLOAT, 0, 1, 4)
				.build());

		return writeRequests;
	}

	private void readMeasurementData() throws DataAccessException {
		List<Measurement> measurements = entityManager.loadAll(Measurement.class, USER + "_Measurement_*");
		ChannelGroup channelGroup = entityManager.loadChildren(measurements.get(0), Measurement.CHILD_TYPE_CHANNELGROUP).get(0);

		ReadRequestIterable readRequestIterable = ReadRequest.create(channelGroup)
				.allChannels()
				.requestSize(3)
				.createIterable();

		for(ReadRequest readRequest : readRequestIterable) {
			System.out.println(entityManager.readMeasuredValues(readRequest));
		}

		ReadRequest readRequest = ReadRequest.create(channelGroup).allChannels().allValues();
		System.out.println(entityManager.readMeasuredValues(readRequest));
	}

	private void deleteTestData() throws DataAccessException {
		Transaction transaction = entityManager.startTransaction();
		try {
			transaction.delete(entityManager.loadAll(Test.class, USER + "_Test_*"));
			transaction.commit();
		} catch(DataAccessException e) {
			e.printStackTrace();
			transaction.abort();
			throw e;
		}
	}

	private Quantity getQuantity() throws DataAccessException {
		List<Quantity> quantities = entityManager.loadAll(Quantity.class);
		if(quantities.isEmpty()) {
			Quantity quantity = getEntityFactory().createQuantity("default", getUnit());

			Transaction transaction = entityManager.startTransaction();
			try {
				transaction.create(Arrays.asList(quantity));
				transaction.commit();
			} catch(DataAccessException e) {
				e.printStackTrace();
				transaction.abort();
				throw e;
			}
		}

		return quantities.get(0);
	}

	private Unit getUnit() throws DataAccessException {
		List<Unit> units = entityManager.loadAll(Unit.class);
		if(units.isEmpty()) {
			Unit unit = getEntityFactory().createUnit("-", getPhysicalDimension());

			Transaction transaction = entityManager.startTransaction();
			try {
				transaction.create(Arrays.asList(unit));
				transaction.commit();
			} catch(DataAccessException e) {
				e.printStackTrace();
				transaction.abort();
				throw e;
			}
		}

		return units.get(0);
	}

	private PhysicalDimension getPhysicalDimension() throws DataAccessException {
		List<PhysicalDimension> physicalDimensions = new ArrayList<>();
		if(physicalDimensions.isEmpty()) {
			PhysicalDimension physicalDimension = getEntityFactory().createPhysicalDimension("dimensionless");

			Transaction transaction = entityManager.startTransaction();
			try {
				transaction.create(Arrays.asList(physicalDimension));
				transaction.commit();
			} catch(DataAccessException e) {
				e.printStackTrace();
				transaction.abort();
				throw e;
			}
		}

		return physicalDimensions.get(0);
	}

	private EntityFactory getEntityFactory() {
		return entityManager.getEntityFactory().orElseThrow(() -> new IllegalStateException("Entity factory is not available."));
	}

}
