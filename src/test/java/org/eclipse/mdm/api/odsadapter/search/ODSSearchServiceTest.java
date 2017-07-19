package org.eclipse.mdm.api.odsadapter.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_NAMESERVICE;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_PASSWORD;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_SERVICENAME;
import static org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory.PARAM_USER;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.FilterItem;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Operator;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.odsadapter.ODSEntityManagerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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

	private static EntityManager entityManager;
	private static ModelManager modelManager;

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
		modelManager = entityManager.getModelManager()
				.orElseThrow(() -> new IllegalStateException("No ModelManager available!"));
	}

	@AfterClass
	public static void tearDownAfterClass() throws ConnectionException {
		if (entityManager != null) {
			entityManager.close();
		}
	}

	private Extractor<FilterItem, Tuple> filterExtractors = new Extractor<FilterItem, Tuple>() {

		@Override
		public Tuple extract(FilterItem f) {
			return tuple(f.isOperator() ? f.getOperator() : null,
					f.isCondition() ? f.getCondition().getAttribute().getName() : null,
					f.isCondition() ? f.getCondition().getOperation() : null,
					f.isCondition() ? f.getCondition().getValue().extract() : null);
		}
	};

	@Test
	public void testGetMergedFilter() throws Exception {

		ODSSearchService service = Mockito.spy((ODSSearchService) entityManager.getSearchService()
				.orElseThrow(() -> new IllegalStateException("No SearchService available!")));

		Mockito.doReturn(ImmutableMap.of(TestStep.class, Arrays.asList("10"))).when(service)
				.fetchIds(Mockito.anyString());

		EntityType testStep = modelManager.getEntityType(TestStep.class);

		assertThat(service.getMergedFilter(Filter.idOnly(testStep, "11"), "query")).hasSize(7)
				.extracting(filterExtractors).containsExactly(tuple(Operator.OPEN, null, null, null),
						tuple(null, "Id", Operation.EQUAL, "10"), tuple(Operator.CLOSE, null, null, null),
						tuple(Operator.AND, null, null, null), tuple(Operator.OPEN, null, null, null),
						tuple(null, "Id", Operation.IN_SET, new String[] { "10" }),
						tuple(Operator.CLOSE, null, null, null));
	}

	@Test
	public void testGetMergedFilterNoAttributeFilter() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) entityManager.getSearchService().get());

		Mockito.doReturn(ImmutableMap.of(TestStep.class, Arrays.asList("10"))).when(service)
				.fetchIds(Mockito.anyString());

		assertThat(service.getMergedFilter(Filter.and(), "query")).extracting(filterExtractors)
				.containsExactly(tuple(null, "Id", Operation.IN_SET, new String[] { "10" }));
	}

	@Test
	public void testGetMergedFilterNoFreetextResult() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) entityManager.getSearchService().get());

		Mockito.doReturn(Collections.emptyMap()).when(service).fetchIds(Mockito.anyString());

		EntityType testStep = modelManager.getEntityType(TestStep.class);

		assertThat(service.getMergedFilter(Filter.idOnly(testStep, "11"), "")).extracting(filterExtractors)
				.containsExactly(tuple(null, "Id", Operation.EQUAL, "11"));
	}

	@Test
	public void testGetMergedFilterNoAttributeFilterAndNoFreetextResult() throws Exception {
		ODSSearchService service = Mockito.spy((ODSSearchService) entityManager.getSearchService().get());

		Mockito.doReturn(Collections.emptyMap()).when(service).fetchIds(Mockito.anyString());

		assertThat(service.getMergedFilter(Filter.and(), null)).isEmpty();

		assertThat(service.getMergedFilter(Filter.and(), "")).isEmpty();
	}
}
