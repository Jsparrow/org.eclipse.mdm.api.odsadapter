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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * Aggregation test
 *
 * @since 1.0.0
 * @author maf, Peak Solution GmbH
 */
@Ignore
// FIXME 11.01.2018: this test needs a running ODS Server, that is not suitable for continous build in Jenkins.
// Comment this in for local tests only.
public class AggregationTest {

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
	public void testQueryIdAndNameNoAggregation() {
		ModelManager modelManager = context.getModelManager().get();
		QueryService queryService = context.getQueryService().get();

		EntityType unitEntityType = modelManager.getEntityType(Unit.class);
		Attribute idAttribute = unitEntityType.getAttribute("Id");
		Attribute nameAttribute = unitEntityType.getAttribute("Name");
		
		List<Result> listRes = queryService.createQuery()
				.select(idAttribute)
				.select(nameAttribute)
				.fetch();

		assertThat(listRes.size()).isGreaterThanOrEqualTo(1);
		assertThat(listRes.get(0).getValue(idAttribute).getValueType()).isEqualTo(ValueType.STRING);
		// Test retrieving attribute value with Aggregation.NONE (should yield the same result as with no aggregation):
		assertThat(listRes.get(0).getValue(idAttribute, Aggregation.NONE).getValueType()).isEqualTo(ValueType.STRING);
		assertThat(listRes.get(0).getValue(nameAttribute).getValueType()).isEqualTo(ValueType.STRING);
	}
	
	@org.junit.Test
	public void testQueryIdWithAggregation() {
		ModelManager modelManager = context.getModelManager().get();
		QueryService queryService = context.getQueryService().get();

		EntityType unitEntityType = modelManager.getEntityType(Unit.class);
		Attribute idAttribute = unitEntityType.getAttribute("Id");
		
		List<Result> listRes = queryService.createQuery()
				.select(idAttribute, Aggregation.MAXIMUM) // should be a string in result, just like non-aggregated Id attribute
				.select(idAttribute, Aggregation.AVERAGE) // should be a numeric value in result
				.fetch();

		assertThat(listRes.size()).isGreaterThanOrEqualTo(1);
		assertThat(listRes.get(0).getValue(idAttribute, Aggregation.MAXIMUM).getValueType()).isEqualTo(ValueType.STRING);
		assertThat(listRes.get(0).getValue(idAttribute, Aggregation.AVERAGE).getValueType()).isEqualTo(ValueType.LONG);
	}
	
	@org.junit.Test
	public void testQueryFactorWithAggregation() {
		ModelManager modelManager = context.getModelManager().get();
		QueryService queryService = context.getQueryService().get();

		EntityType unitEntityType = modelManager.getEntityType(Unit.class);
		Attribute factorAttribute = unitEntityType.getAttribute("Factor");
		
		List<Result> listRes = queryService.createQuery()
				.select(factorAttribute, Aggregation.COUNT)
				.group(factorAttribute)
				.fetch();
		
		assertThat(listRes.size()).isGreaterThanOrEqualTo(1);
		assertThat(listRes.get(0).getValue(factorAttribute, Aggregation.COUNT).getValueType()).isEqualTo(ValueType.INTEGER);
	}
	
	@org.junit.Test
	public void testQueryFactorWithAndWithoutAggregation() {
		ModelManager modelManager = context.getModelManager().get();
		QueryService queryService = context.getQueryService().get();

		EntityType unitEntityType = modelManager.getEntityType(Unit.class);
		Attribute factorAttribute = unitEntityType.getAttribute("Factor");
		
		List<Result> listRes = queryService.createQuery()
				.select(factorAttribute)
				.select(factorAttribute, Aggregation.SUM)
				.group(factorAttribute)
				.fetch();

		
		assertThat(listRes.size()).isGreaterThanOrEqualTo(1);
		assertThat(listRes.get(0).getValue(factorAttribute).getValueType()).isEqualTo(ValueType.DOUBLE);
		assertThat(listRes.get(0).getValue(factorAttribute, Aggregation.SUM).getValueType()).isEqualTo(ValueType.DOUBLE);
	}

}
