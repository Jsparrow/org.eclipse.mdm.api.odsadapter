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
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.model.BaseEntity;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Core.EntityStore;
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
		if (entityManager != null) {
			entityManager.close();
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
	
	/**
	 * changes a relation between instances. There needs to exist a MeaResult of id 1101 and a ParameterSet of id
	 * 1102 which will be related after running this test.
	 * If these don't exist, please leave the following line commented, or the test will fail.  
	 * @throws Exception 
	 */
	//@org.junit.Test
	public void changeRelation() throws Exception {
		String idmea = "1101";
		String idparamset = "1002";

		EntityType etmeasurement = modelManager.getEntityType(Measurement.class);
		EntityType etparamset = modelManager.getEntityType(ParameterSet.class);
		Transaction transaction;

		transaction = entityManager.startTransaction();

		try {
			List<Measurement> mealist;
			mealist = searchService.fetch(Measurement.class, Filter.idOnly(etmeasurement, idmea));
			assertEquals(1, mealist.size());
			Measurement mea = mealist.get(0);

			List<ParameterSet> paramsetlist = new ArrayList<>();
			ParameterSet paramset = entityManager.load(ParameterSet.class, idparamset);
			paramsetlist.add(paramset);

			// FIXME: the API should expose the getCore method so that we don't
			// need to use introspection
			// to access the core.
			Method GET_CORE_METHOD;
			try {
				GET_CORE_METHOD = BaseEntity.class.getDeclaredMethod("getCore");
				GET_CORE_METHOD.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalStateException(
						"Unable to load 'getCore()' in class '" + BaseEntity.class.getSimpleName() + "'.", e);
			}
			Core core = (Core) GET_CORE_METHOD.invoke(paramset);
			// Note: we can only set relations in the mutable store (which
			// doesn't include parent-child-relations)
			EntityStore store = core.getMutableStore();
			store.set(mea);
			assertEquals(core.getMutableStore().get(Measurement.class), mea);

			transaction.update(paramsetlist);
			transaction.commit();

			// reload from database and check if relation is consistent with
			// expectations
			// first we need to build our own SearchQuery, because the
			// predefined ones don't include ParameterSet
			MeasurementSearchQuery mq = new MeasurementSearchQuery((ODSModelManager) modelManager,
					queryService,
					ContextState.MEASURED);
			List<EntityType> fetchList = new ArrayList<>();
			fetchList.add(etmeasurement);
			List<Result> fetch = mq.fetchComplete(fetchList, Filter.idOnly(etparamset, idparamset));
			assertEquals(fetch.size(), 1);
			Record record = fetch.get(0).getRecord(etmeasurement);
			assertNotNull(record);
			assertEquals(record.getID(), idmea);

		} catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
			transaction.abort();
			throw e;
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
