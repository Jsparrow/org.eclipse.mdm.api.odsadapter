/*
 * Copyright (c) 2017 Florian Schmitt and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.core.EntityStore;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.search.SearchService;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.RelationSearchQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
// FIXME 10.7.2017: this test needs a running ODS Server, that is not suitable for continous build in Jenkins.
// Comment this in for local tests only.
public class RelationTest {

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
	private static ModelManager modelManager;
	private static QueryService queryService;
	private static SearchService searchService;
	
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

		context  = new ODSContextFactory().connect(connectionParameters);
		entityManager = context.getEntityManager()
				.orElseThrow(() -> new ServiceNotProvidedException(EntityManager.class));
		modelManager = context.getModelManager()
				.orElseThrow(() -> new ServiceNotProvidedException(ModelManager.class));
		queryService = context.getQueryService()
				.orElseThrow(() -> new ServiceNotProvidedException(QueryService.class));
		searchService = context.getSearchService()
				.orElseThrow(() -> new IllegalStateException("Search service not available."));
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (context != null) {
			context.close();
		}
	}
	
	
	
	/**
	 * changes a relation between instances. There needs to exist a MeaResult of id 1101 and a ParameterSet of id
	 * 1102 which will be related after running this test.
	 * If these don't exist, please leave the following line commented, or the test will fail.  
	 * @throws Exception 
	 */
	@org.junit.Test
	public void changeRelation() throws Exception {
		String idmea = "1101";
		String idparamset = "1002";

		EntityType etmeasurement = modelManager.getEntityType(Measurement.class);
		EntityType etparamset = modelManager.getEntityType(ParameterSet.class);
		Transaction transaction;

		transaction = entityManager.startTransaction();

		try {
			List<Measurement> mealist;
			// we use the searchService to fetch a measurement. Please note, that we
			// use the per default defined method to fetch the measurement.
			// This does not load any related ParameterSets! But
			// we don't care at this point, because we just want to change 
			// the related ParameterSet to a new one, not look at
			// the existing relations.
			mealist = searchService.fetch(Measurement.class, Filter.idOnly(etmeasurement, idmea));
			assertEquals(1, mealist.size());
			Measurement mea = mealist.get(0);

			// load the parameter set which we want to relate to our measurment.
			// again we don't care for any preexisting relations.
			List<ParameterSet> paramsetlist = new ArrayList<>();
			ParameterSet paramset = entityManager.load(ParameterSet.class, idparamset);
			paramsetlist.add(paramset);
			// Note: we can only set relations in the mutable store (which
			// doesn't include parent-child-relations)
			EntityStore store = ODSEntityFactory.getMutableStore(paramset);
			store.set(mea);
			assertEquals(store.get(Measurement.class), mea);

			transaction.update(paramsetlist);
			transaction.commit();

			// reload from database and check if relation is consistent with
			// expectations
			// first we need to build our own SearchQuery, because the
			// predefined ones don't include ParameterSet as stated above.
			RelationSearchQuery mq = new RelationSearchQuery((ODSModelManager) modelManager,
					queryService);
			// the SearchQuery defines how to join measurement and parameterset,
			// but we also have to declare that we want to fetch the related
			// measurement
			List<EntityType> fetchList = new ArrayList<>();
			fetchList.add(etmeasurement);
			List<Result> fetch = mq.fetchComplete(fetchList, Filter.idOnly(etparamset, idparamset));
			assertEquals(fetch.size(), 1);
			
			// now we can check that the new relation is there as expected
			Record record = fetch.get(0).getRecord(etmeasurement);
			assertNotNull(record);
			assertEquals(record.getID(), idmea);

		} catch (RuntimeException e) {
			transaction.abort();
			throw e;
		}

	}
	}
