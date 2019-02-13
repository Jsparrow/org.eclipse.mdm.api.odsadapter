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

import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_USER;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.search.SearchService;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JoinType test
 *
 * @since 1.0.0
 * @author jst, Peak Solution GmbH
 */
@Ignore
// FIXME 10.7.2017: this test needs a running ODS Server, that is not suitable for continous build in Jenkins.
// Comment this in for local tests only.
public class JoinTest {

	private static final String NAME_SERVICE = "corbaloc::1.2@%s:%s/NameService";

	private static final String USER = "sa";
	private static final String PASSWORD = "sa";

	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws ConnectionException {
		String nameServiceHost = System.getProperty("host");
		String nameServicePort = System.getProperty("port");
		String serviceName = System.getProperty("service");

		if (nameServiceHost == null || StringUtils.isEmpty(nameServiceHost)) {
			throw new IllegalArgumentException("name service host is unknown: define system property 'host'");
		}

		nameServicePort = nameServicePort == null || StringUtils.isEmpty(nameServicePort) ? String.valueOf(2809) : nameServicePort;
		if (nameServicePort == null || StringUtils.isEmpty(nameServicePort)) {
			throw new IllegalArgumentException("name service port is unknown: define system property 'port'");
		}

		if (serviceName == null || StringUtils.isEmpty(serviceName)) {
			throw new IllegalArgumentException("service name is unknown: define system property 'service'");
		}

		Map<String, String> connectionParameters = new HashMap<>();
		connectionParameters.put(PARAM_NAMESERVICE, String.format(NAME_SERVICE, nameServiceHost, nameServicePort));
		connectionParameters.put(PARAM_SERVICENAME, serviceName + ".ASAM-ODS");
		connectionParameters.put(PARAM_USER, USER);
		connectionParameters.put(PARAM_PASSWORD, PASSWORD);

		context  = new ODSContextFactory().connect(connectionParameters);
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (context != null) {
			context.close();
		}
	}

	@org.junit.Test
	public void findTestFromTestStepId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);

		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, "37"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestFromMeasurementId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);

		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, "65"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestFromChannelGroupId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);

		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, "80"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestFromChannelId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);

		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, "302"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestStepFromTestId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Test.class);

		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, "28"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestStepFromMeasurementId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);

		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, "65"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestStepFromChannelGroupId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);

		List<Test> list = searchService.fetch(Test.class, Filter.idOnly(et, "80"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findTestStepFromChannelId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);

		List<TestStep> list = searchService.fetch(TestStep.class, Filter.idOnly(et, "302"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findMeasurementFromTestId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Test.class);

		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, "28"));

		assertEquals(9, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findMeasurementFromTestStepId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);

		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, "37"));

		assertEquals(9, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findMeasurementFromChannelGroupId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);

		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, "80"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findMeasurementFromChannelId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);

		List<Measurement> list = searchService.fetch(Measurement.class, Filter.idOnly(et, "302"));

		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelGroupFromTestId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Test.class);

		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, "28"));

		assertEquals(14, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelGroupFromTestStepId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);

		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, "37"));

		assertEquals(14, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelGroupFromMeasurementId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);

		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, "65"));

		assertEquals(2, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelGroupFromChannelId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Channel.class);

		List<ChannelGroup> list = searchService.fetch(ChannelGroup.class, Filter.idOnly(et, "302"));

		assertEquals(2, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelFromTestId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Test.class);

		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, "28"));

		assertEquals(43, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelFromTestStepId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(TestStep.class);

		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, "37"));

		assertEquals(43, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelFromMeasurementId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(Measurement.class);

		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, "65"));

		assertEquals(2, list.size());
		System.out.println(list.size());
	}

	@org.junit.Test
	public void findChannelFromChannelGroupId() {
		ModelManager modelManager = context.getModelManager().get();
		SearchService searchService = context.getSearchService().get();

		EntityType et = modelManager.getEntityType(ChannelGroup.class);

		List<Channel> list = searchService.fetch(Channel.class, Filter.idOnly(et, "80"));

		assertEquals(2, list.size());
		System.out.println(list.size());
	}

}
