package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_USER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplicationElement;
import org.asam.ods.TS_UnionSeq;
import org.asam.ods.TS_ValueSeq;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.eclipse.mdm.api.base.notification.NotificationManager;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Test notification service. 
 * 
 * Needs a running ODS and Notification Server.
 * Assumes a existing Test with name defined in {@link ODSNotificationTest#PARENT_TEST}.
 * 
 * @author mko
 *
 */
public class ODSNotificationTest {

	// TODO name service:  corbaloc::1.2@<SERVER_IP>:<SERVER_PORT>/NameService
	private static final String NAME_SERVICE = "corbaloc::1.2@127.0.0.1:2809/NameService";

	// TODO service name: <SERVICE_NAME>.ASAM-ODS
	private static final String SERVICE_NAME = "MDMNVH.ASAM-ODS";

	private static final String USER = "sa";
	private static final String PASSWORD = "sa";

	private static final String PARENT_TEST = "PBN_UNECE_R51_13022014_1349";

	
	private static final String NOTIFICATION_URL = "http://localhost:8080/api";
	private static final String NOTIFICATION_REGISTRATION_NAME = "mdm";
	private static final String NOTIFICATION_USER = "sa";
	private static final String NOTIFICATION_PASSWORD = "sa";
	
	private static EntityManager entityManager;
	private static NotificationManager notificationManager;

	@BeforeClass
	public static void setUpBeforeClass() throws ConnectionException {
		Map<String, String> connectionParameters = new HashMap<>();
		connectionParameters.put(PARAM_NAMESERVICE, NAME_SERVICE);
		connectionParameters.put(PARAM_SERVICENAME, SERVICE_NAME);
		connectionParameters.put(PARAM_USER, USER);
		connectionParameters.put(PARAM_PASSWORD, PASSWORD);

		entityManager = new ODSEntityManagerFactory().connect(connectionParameters);
		
		Map<String, String> notificationParameters = new HashMap<>();
		notificationParameters.put(ODSNotificationManagerFactory.PARAM_SERVER_TYPE, ODSNotificationManagerFactory.SERVER_TYPE_PEAK);
		notificationParameters.put(ODSNotificationManagerFactory.PARAM_URL, NOTIFICATION_URL);
		notificationParameters.put(ODSEntityManagerFactory.PARAM_USER, NOTIFICATION_USER);
		notificationParameters.put(ODSEntityManagerFactory.PARAM_PASSWORD, NOTIFICATION_PASSWORD);
		notificationParameters.put(ODSNotificationManagerFactory.PARAM_EVENT_MEDIATYPE, "application/json");
		
		notificationManager = new ODSNotificationManagerFactory().create((ODSEntityManager) entityManager, notificationParameters);
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (entityManager != null)
		{
			entityManager.close();
		}
		
		if (notificationManager != null)
		{
			notificationManager.deregister(NOTIFICATION_REGISTRATION_NAME);
		}
	}
	
	@org.junit.Test
	public void test() throws NotificationException, DataAccessException, InterruptedException
	{ 
		String testStepName = USER + "_TestStep";
		
		NotificationListener l = Mockito.mock(NotificationListener.class);

		notificationManager.register(NOTIFICATION_REGISTRATION_NAME, new NotificationFilter(), l);

		try
		{	
			createTestStep(PARENT_TEST, testStepName);
			
			// make sure notification has some time to be pushed
			Thread.sleep(1000L);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArgumentCaptor<List<TestStep>> testStepCaptor = ArgumentCaptor.forClass((Class) List.class);
			ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
			
			verify(l, times(1)).instanceCreated(testStepCaptor.capture(), userCaptor.capture());
			
			assertThat(testStepCaptor.getValue().size(), is(1));
			assertThat(testStepCaptor.getValue().get(0).getName(), is(testStepName));
			assertThat(userCaptor.getValue().getName(), is(USER));
		}
		finally
		{
			notificationManager.deregister(NOTIFICATION_REGISTRATION_NAME);
			deleteTestStep(testStepName);
		}
	}

	
	@org.junit.Test
	public void testContextRoot() throws NotificationException, DataAccessException, InterruptedException, IOException
	{ 		
		NotificationListener l = Mockito.mock(NotificationListener.class);
	
		notificationManager.register(NOTIFICATION_REGISTRATION_NAME, new NotificationFilter(), l);
	
		try
		{	
			updateUUT(11, "application/x-asam.aounitundertest.unitundertest");
	
			// make sure notification has some time to be pushed
			Thread.sleep(1000L);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArgumentCaptor<List<Entity>> entityCaptor = ArgumentCaptor.forClass((Class) List.class);
			ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
			
			verify(l, times(1)).instanceModified(entityCaptor.capture(), userCaptor.capture());
			
			assertThat(entityCaptor.getValue().size(), is(1));
			assertThat(entityCaptor.getValue().get(0).getName(), is("PBN_UNECE_R51_Left_AccV"));
			assertThat(userCaptor.getValue().getName(), is(USER));
		}
		finally
		{
			notificationManager.deregister(NOTIFICATION_REGISTRATION_NAME);
		}
	}
	
	@org.junit.Test
	public void testContextComponent() throws NotificationException, DataAccessException, InterruptedException, IOException
	{ 		
		NotificationListener l = Mockito.mock(NotificationListener.class);
	
		notificationManager.register(NOTIFICATION_REGISTRATION_NAME, new NotificationFilter(), l);
	
		try
		{	
			updateUUTP(34, "test");
	
			// make sure notification has some time to be pushed
			Thread.sleep(1000L);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArgumentCaptor<List<Entity>> entityCaptor = ArgumentCaptor.forClass((Class) List.class);
			ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
			
			verify(l, times(1)).instanceModified(entityCaptor.capture(), userCaptor.capture());
			
			assertThat(entityCaptor.getValue().size(), is(1));
			assertThat(entityCaptor.getValue().get(0).getName(), is("PBN_UNECE_R51_Left_SteadyV"));
			assertThat(userCaptor.getValue().getName(), is(USER));
		}
		finally
		{
			notificationManager.deregister(NOTIFICATION_REGISTRATION_NAME);
		}
	}
	
	private void deleteTestStep(String name) throws DataAccessException {
		Transaction transaction = entityManager.startTransaction();

		List<TestStep> testSteps = entityManager.loadAll(TestStep.class, name);

		transaction.delete(testSteps);
		
		transaction.commit();
	}
	
	private void updateUUT(int uutId, String newValue) throws DataAccessException {
		
		AoSession session = null;
		try
		{
			session = ((ODSModelManager) entityManager.getModelManager().get()).getAoSession().createCoSession();
			
			ApplicationElement aeUUT = session.getApplicationStructure().getElementsByBaseType("AoUnitUnderTest")[0];
		      
			final ApplElemAccess aea = session.getApplElemAccess();
	
			TS_UnionSeq uId = new TS_UnionSeq();
			uId.longlongVal(new T_LONGLONG[] { new T_LONGLONG(0, uutId)});
	
			TS_UnionSeq uManufacturer = new TS_UnionSeq();
			uManufacturer.stringVal(new String[] { newValue });
	
			AIDNameValueSeqUnitId[] val = new AIDNameValueSeqUnitId[] {
					new AIDNameValueSeqUnitId(new AIDName(aeUUT.getId(), "Id"), new T_LONGLONG(), new TS_ValueSeq(uId, new short[] { (short) 15 }))  ,
					new AIDNameValueSeqUnitId(new AIDName(aeUUT.getId(), "Mimetype"), new T_LONGLONG(), new TS_ValueSeq(uManufacturer, new short[] { (short) 15 }))  
			};
			session.startTransaction();
			aea.updateInstances(val);
			session.commitTransaction();
			
		}
		catch (AoException e)
		{
			throw new DataAccessException(e.reason, e);
		}
		finally
		{
			if (session != null)
			{
				try {
					session.close();
				} catch (AoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateUUTP(int tyreId, String newValue) throws DataAccessException {
		
		AoSession session = null;
		try
		{
			session = ((ODSModelManager) entityManager.getModelManager().get()).getAoSession().createCoSession();
			
			ApplicationElement aeTyre = session.getApplicationStructure().getElementByName("tyre");
		      
			final ApplElemAccess aea = session.getApplElemAccess();
	
			TS_UnionSeq uId = new TS_UnionSeq();
			uId.longlongVal(new T_LONGLONG[] { new T_LONGLONG(0, tyreId)});
	
			TS_UnionSeq uManufacturer = new TS_UnionSeq();
			uManufacturer.stringVal(new String[] { newValue });
	
			AIDNameValueSeqUnitId[] val = new AIDNameValueSeqUnitId[] {
					new AIDNameValueSeqUnitId(new AIDName(aeTyre.getId(), "Id"), new T_LONGLONG(), new TS_ValueSeq(uId, new short[] { (short) 15 }))  ,
					new AIDNameValueSeqUnitId(new AIDName(aeTyre.getId(), "manufacturer"), new T_LONGLONG(), new TS_ValueSeq(uManufacturer, new short[] { (short) 15 }))  
			};
			session.startTransaction();
			aea.updateInstances(val);
			session.commitTransaction();
			
		}
		catch (AoException e)
		{
			throw new DataAccessException(e.reason, e);
		}
		finally
		{
			if (session != null)
			{
				try {
					session.close();
				} catch (AoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void createTestStep(String parentName, String name) throws DataAccessException {
		Transaction transaction = entityManager.startTransaction();

		List<Test> tests = entityManager.loadAll(Test.class, parentName);

		assertThat("Parent test not found!", !tests.isEmpty());
		
		Optional<EntityFactory> entityFactory = entityManager.getEntityFactory();
		if (!entityFactory.isPresent())
		{
			throw new IllegalStateException("Entity factory not present!");
		}

		TestStep testStep = entityFactory.get().createTestStep(name, tests.get(0));
		testStep.setSortIndex(0);
		
		transaction.create(Arrays.asList(testStep));
		
		transaction.commit();
	}
}