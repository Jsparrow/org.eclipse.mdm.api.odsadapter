/*
 * Copyright (c) 2016 Peak Solution GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_USER;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.SearchService;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.EntityFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Join test
 *
 * @since 1.0.0
 * @author jst, Peak Solution GmbH
 */
public class JoinTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(JoinTest.class);

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

		if(nameServiceHost == null || nameServiceHost.isEmpty()) {
			throw new IllegalArgumentException("name service host is unknown: define system property 'host'");
		}

		nameServicePort = nameServicePort == null || nameServicePort.isEmpty() ? String.valueOf(2809) :  nameServicePort;
		if(nameServicePort == null || nameServicePort.isEmpty()) {
			throw new IllegalArgumentException("name service port is unknown: define system property 'port'");
		}

		if(serviceName == null || serviceName.isEmpty()) {
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
		if(entityManager != null) {
			entityManager.close();
		}
	}

	
	@org.junit.Test
	public void findTestFromTestStepId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);
		
		
		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, 37L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestFromMeasurementId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Measurement.class);
		
		
		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, 65L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestFromChannelGroupId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(ChannelGroup.class);
		
		
		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, 80L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestFromChannelId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Channel.class);
		
		
		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, 302L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	
	
	@org.junit.Test
	public void findTestStepFromTestId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Test.class);
		
		
		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, 28L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestStepFromMeasurementId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Measurement.class);
		
		
		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, 65L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestStepFromChannelGroupId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(ChannelGroup.class);
		
		
		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, 80L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findTestStepFromChannelId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);
		
		
		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, 302L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	

	
	@org.junit.Test
	public void findMeasurementFromTestId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Test.class);
		
		
		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, 28L));
		
		assertEquals(9, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findMeasurementFromTestStepId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);
		
		
		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, 37L));
		
		assertEquals(9, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findMeasurementFromChannelGroupId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);
		
		
		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, 80L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findMeasurementFromChannelId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);
		
		
		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, 302L));
		
		assertEquals(1, list.size());
		System.out.println(list.size());
	}


	
	@org.junit.Test
	public void findChannelGroupFromTestId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Test.class);
		
		
		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, 28L));
		
		assertEquals(14, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findChannelGroupFromTestStepId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);
		
		
		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, 37L));
		
		assertEquals(14, list.size());
		System.out.println(list.size());
	}	
	
	@org.junit.Test
	public void findChannelGroupFromMeasurementId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);
		
		
		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, 65L));
		
		assertEquals(2, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelGroupFromChannelId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);
		
		
		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, 302L));
		
		assertEquals(2, list.size());
		System.out.println(list.size());
	}
	
	
	
	@org.junit.Test
	public void findChannelFromTestId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();
		
		EntityType et = modelManager.getEntityType(Test.class);
		
		
		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, 28L));
		
		assertEquals(43, list.size());
		System.out.println(list.size());
	}
	
	@org.junit.Test
	public void findChannelFromTestStepId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);
		
		
		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, 37L));
		
		assertEquals(43, list.size());
		System.out.println(list.size());
	}	
	
	@org.junit.Test
	public void findChannelFromMeasurementId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);
		
		
		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, 65L));
		
		assertEquals(2, list.size());
		System.out.println(list.size());
	}	
	
	@org.junit.Test
	public void findChannelFromChannelGroupId() throws DataAccessException {
		ModelManager modelManager = entityManager.getModelManager().get();
		SearchService searchService = entityManager.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);
		
		
		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, 80L));
		
		assertEquals(2, list.size());
		System.out.println(list.size());
	}	
	
	
}
