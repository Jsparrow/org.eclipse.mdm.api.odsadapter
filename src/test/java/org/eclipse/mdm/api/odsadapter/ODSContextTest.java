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

package org.eclipse.mdm.api.odsadapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_USER;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.InstanceElement;
import org.assertj.core.api.Condition;
import org.assertj.core.api.Fail;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mockito.Mockito;

@Ignore
//FIXME 26.01.2018: this test needs a running ODS Server, that is not suitable for continous build in Jenkins.
//Comment this in for local tests only.
public class ODSContextTest {

	/*
	 * ATTENTION: ==========
	 *
	 * To run this test make sure the target service is running a MDM default
	 * model and any database constraint which enforces a relation of Test to a
	 * parent entity is deactivated!
	 */
	private static final String NAME_SERVICE = "corbaloc::1.2@%s:%s/NameService";

	private static final String USER = "sa";
	private static final String PASSWORD = "sa";

	private static ApplicationContext context;
	private static EntityManager entityManager;

	private static String nameServiceHost = System.getProperty("host");
	private static String nameServicePort = System.getProperty("port");
	private static String serviceName = System.getProperty("service");

	
	@BeforeClass
	public static void setUpBeforeClass() throws ConnectionException {

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

		context  = new ODSContextFactory().connect(connectionParameters);
		entityManager = context.getEntityManager()
				.orElseThrow(() -> new ServiceNotProvidedException(EntityManager.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (context != null) {
			context.close();
		}
	}
	
	@org.junit.Test
	public void testGetAdapterType() {
		assertThat(context.getAdapterType()).isEqualTo("ods");
	}
	
	@org.junit.Test
	public void testGetLink() {
		// we assume a test with ID 4 exists.
		Test test = entityManager.load(Test.class, "4");
		
		Map<Entity, String> asamPaths = entityManager.getLinks(Arrays.asList(test));
		
		assertThat(asamPaths)
			.hasSize(1)
			.containsOnlyKeys(test)
			.hasEntrySatisfying(test, new Condition<String>(s -> s.startsWith(getLinkPrefix()), ""));
	}
	
	@org.junit.Test
	public void testGetLinkAndLoadOdsInstance() {
		// we assume a test with ID 4 exists.
		long testId = 4L;
		Test test = entityManager.load(Test.class, Long.toString(testId));
		
		Map<Entity, String> asamPaths = entityManager.getLinks(Arrays.asList(test));
		
		assertThat(asamPaths)
			.hasSize(1)
			.containsOnlyKeys(test)
			.hasEntrySatisfying(test, new Condition<String>(s -> s.startsWith(getLinkPrefix()), ""));
		// We try to load the instance from the ODS Server with the AsamPath, but without the service prefix
		String asamPathWithoutService = asamPaths.get(test).replace(getLinkPrefix(), "");
		
		try {
			InstanceElement ie = ((ODSContext) context).getAoSession().getApplicationStructure().getInstanceByAsamPath(asamPathWithoutService);
			assertThat(ODSConverter.fromODSLong(ie.getId())).isEqualTo(testId);
		} catch (AoException e) {
			Fail.fail("Instance with AsamPath '" + asamPathWithoutService + "' could not be loaded. Reason: " + e.reason, e);
		}
	}
	
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void testGetLinksWithInvalidType() {
		// we assume a test with ID 4 exists.
		Test test = entityManager.load(Test.class, "4");
		BaseEntity entity = Mockito.mock(BaseEntity.class);
		// Mock an entity with a non existing TypeName
		when(entity.getTypeName()).thenReturn("invalidType");
		
		entityManager.getLinks(Arrays.asList(test, entity));
	}
	
	@org.junit.Test
	public void testGetLinksWithEnitityWithMissingId() {
		// we assume a test with ID 4 exists.
		Test test = entityManager.load(Test.class, "4");
		
		// Mock an entity with a non existing ID
		TestStep testStep = Mockito.mock(TestStep.class);
		when(testStep.getID()).thenReturn("-1");
		
		Map<Entity, String> asamPaths = entityManager.getLinks(Arrays.asList(test, testStep));
		
		assertThat(asamPaths)
			.hasSize(1)
			.containsOnlyKeys(test)
			.hasEntrySatisfying(test, new Condition<String>(s -> s.startsWith(getLinkPrefix()), ""));
	}
	
	private String getLinkPrefix() {
		return String.format("corbaloc::1.2@%s:%s/NameService/%s.ASAM-ODS", nameServiceHost, nameServicePort, serviceName);
	}
}
