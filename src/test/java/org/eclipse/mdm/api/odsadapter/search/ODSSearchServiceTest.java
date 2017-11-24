package org.eclipse.mdm.api.odsadapter.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSContextFactory.PARAM_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.adapter.EntityStore;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.BooleanOperator;
import org.eclipse.mdm.api.base.query.BracketOperator;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.base.search.SearchService;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.odsadapter.ODSContextFactory;
import org.eclipse.mdm.api.odsadapter.ODSEntityManager;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;

@Ignore
// FIXME 10.7.2017: this test needs a running ODS Server, that is not suitable for continous build in Jenkins.
// Comment this in for local tests only.
public class ODSSearchServiceTest {

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
	
	final class MeasurementSearchQuery extends BaseEntitySearchQuery {

		/**
		 * Constructor.
		 *
		 * @param modelManager
		 *            Used to load {@link EntityType}s.
		 * @param contextState
		 *            The {@link ContextState}.
		 */
		MeasurementSearchQuery(ODSModelManager modelManager, QueryService queryService, ContextState contextState) {
			super(modelManager, queryService, ParameterSet.class, Project.class);

			// layers
			addJoinConfig(JoinConfig.up(Pool.class, Project.class));
			addJoinConfig(JoinConfig.up(Test.class, Pool.class));
			addJoinConfig(JoinConfig.up(TestStep.class, Test.class));
			addJoinConfig(JoinConfig.up(Measurement.class, TestStep.class));
			addJoinConfig(JoinConfig.up(ParameterSet.class, Measurement.class));
			addJoinConfig(JoinConfig.down(Measurement.class, Channel.class));
			addJoinConfig(JoinConfig.down(Measurement.class, ChannelGroup.class));

			// context
			addJoinConfig(contextState);
		}

	}
		
	private Extractor<FilterItem, Tuple> filterExtractors = new Extractor<FilterItem, Tuple>() {

		@Override
		public Tuple extract(FilterItem f) {
			return tuple(f.isBooleanOperator() ? f.getBooleanOperator() : null,
					f.isCondition() ? f.getCondition().getAttribute().getName() : null,
					f.isCondition() ? f.getCondition().getComparisonOperator() : null,
					f.isCondition() ? f.getCondition().getValue().extract() : null,
					f.isBracketOperator() ?f.getBracketOperator() : null);
		}
	};

	@org.junit.Test
	public void testGetMergedFilter() throws Exception {

		ODSSearchService service = Mockito.spy((ODSSearchService) searchService);

		Mockito.doReturn(ImmutableMap.of(TestStep.class, Arrays.asList("10"))).when(service)
				.fetchIds(Mockito.anyString());

		EntityType testStep = modelManager.getEntityType(TestStep.class);

		assertThat(service.getMergedFilter(Filter.idOnly(testStep, "11"), "query")).hasSize(7)
				.extracting(filterExtractors).containsExactly(tuple(null, null, null, null, BracketOperator.OPEN),
						tuple(null, "Id", ComparisonOperator.EQUAL, "10"),
						tuple(null, null, null, null, BracketOperator.CLOSE),
						tuple(BooleanOperator.AND, null, null, null, null),
						tuple(null, null, null, null, BracketOperator.OPEN),
						tuple(null, "Id", ComparisonOperator.IN_SET, new String[] { "10" }, null),
						tuple(null, null, null, null, BracketOperator.CLOSE));
	}

	@org.junit.Test
	public void testGetMergedFilterNoAttributeFilter() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) searchService);

		Mockito.doReturn(ImmutableMap.of(TestStep.class, Arrays.asList("10"))).when(service)
				.fetchIds(Mockito.anyString());

		assertThat(service.getMergedFilter(Filter.and(), "query")).extracting(filterExtractors)
				.containsExactly(tuple(null, "Id", ComparisonOperator.IN_SET, new String[] { "10" }));
	}

	@org.junit.Test
	public void testGetMergedFilterNoFreetextResult() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) searchService);

		Mockito.doReturn(Collections.emptyMap()).when(service).fetchIds(Mockito.anyString());

		EntityType testStep = modelManager.getEntityType(TestStep.class);

		assertThat(service.getMergedFilter(Filter.idOnly(testStep, "11"), "")).extracting(filterExtractors)
				.containsExactly(tuple(null, "Id", ComparisonOperator.EQUAL, "11"));
	}

	@org.junit.Test
	public void testGetMergedFilterNoAttributeFilterAndNoFreetextResult() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) searchService);

		Mockito.doReturn(Collections.emptyMap()).when(service).fetchIds(Mockito.anyString());

		assertThat(service.getMergedFilter(Filter.and(), null)).isEmpty();

		assertThat(service.getMergedFilter(Filter.and(), "")).isEmpty();
	}
}
