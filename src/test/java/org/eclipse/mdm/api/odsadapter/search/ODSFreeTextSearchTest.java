package org.eclipse.mdm.api.odsadapter.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ODSFreeTextSearchTest {
	private static final String HOST = "http://localhost:9301";
	private EntityLoader loader;
	private ODSFreeTextSearch fts;
	private Entity ts;

	private static Node elasticSearchNode;

	private static Client client;

	@BeforeClass
	public static void beforeClass() throws Exception {
		File tempDir = File.createTempFile("elasticsearch-temp", Long.toString(System.nanoTime()));
		tempDir.delete();
		tempDir.mkdir();

		String clusterName = UUID.randomUUID().toString();
		elasticSearchNode = NodeBuilder.nodeBuilder().local(true).clusterName(clusterName)
				.settings(Settings.settingsBuilder()
						.put("path.home", File.createTempFile("elasticsearch", "").getParent())
						.put("index.number_of_shards", 1).put("index.number_of_replicas", 0).put("http.port", 9301))
				.node();
		elasticSearchNode.start();
		client = elasticSearchNode.client();
	}

	@SuppressWarnings("unchecked")
	@Before
	public void init() throws DataAccessException {
		ts = mock(TestStep.class);
		loader = mock(EntityLoader.class);
		List<Entity> tsList = new ArrayList<>();
		tsList.add(ts);
		when(loader.loadAll(any(Key.class), anyCollection())).thenAnswer(new Answer<List<Entity>>() {

			@Override
			public List<Entity> answer(InvocationOnMock invocation) throws Throwable {
				Collection<Long> object = (Collection<Long>) invocation.getArguments()[1];

				List<Entity> res = new ArrayList<>();
				for (int i = 0; i < object.size(); i++) {
					res.add(ts);
				}

				return res;
			}
		});

		fts = new ODSFreeTextSearch(HOST, loader, "mdm");
	}

	@Test
	public void noIndex_emptyResult() throws DataAccessException {
		ODSFreeTextSearch ftsOtherIndex = new ODSFreeTextSearch(HOST, loader, "UNKNOWN_INDEX");

		Map<Class<? extends Entity>, List<Entity>> map = ftsOtherIndex.search("VAG_002");
		assertTrue(map.isEmpty());
	}

	@Test
	public void exampleIndex_querySuccessfull() throws Exception {
		createExampleIndex("TestStep", "mdm", "asdf");

		Map<Class<? extends Entity>, List<Entity>> search = fts.search("asdf");
		assertEquals(ts, search.get(TestStep.class).get(0));
	}

	@Test
	public void specialCharacters_correctlyEscaped() throws InterruptedException {
		createExampleIndex("Measurement", "mdm", "hallo\"!§");

		Map<Class<? extends Entity>, List<Entity>> search = fts.search("hallo\"!§");
		assertEquals(ts, search.get(Measurement.class).get(0));
	}

	@Test
	public void nonMdmResults_ignored() throws InterruptedException {
		createExampleIndex("NONMDMStuff", "mdm", "test");

		Map<Class<? extends Entity>, List<Entity>> search = fts.search("test");
		assertTrue(search.isEmpty());
	}

	@Test
	public void twoResults_twoResultsRetuned() throws InterruptedException {
		createExampleIndex("Test", "mdm", "unicorn ASDF", "0");
		createExampleIndex("Test", "mdm", "unicorn XYZ", "1");

		Map<Class<? extends Entity>, List<Entity>> search = fts.search("unicorn");
		assertEquals(2, search.get(org.eclipse.mdm.api.base.model.Test.class).size());
	}

	@Test(expected=IllegalStateException.class)
	public void illegalLoadRequest_niceExceptionIsThrown() throws DataAccessException, InterruptedException {
		loader = mock(EntityLoader.class);
		when(loader.loadAll(any(Key.class), anyCollection())).thenThrow(new DataAccessException(""));
		createExampleIndex("TestStep", "mdm2", "asdf");
		ODSFreeTextSearch fts2 = new ODSFreeTextSearch(HOST, loader, "mdm2");
		
		fts2.search("asdf");
	}
	
	@Test
	public void doubleEscapedSearchString_IsJSONEscaped()
	{
		
	}

	private void createExampleIndex(String type, String name, String value) throws InterruptedException {
		createExampleIndex(type, name, value, "0");
	}

	private void createExampleIndex(String type, String name, String value, String id) throws InterruptedException {
		String json = "{\"attr\":\"" + StringEscapeUtils.escapeJson(value) + "\"}";

		client.prepareIndex(name, type, id).setSource(json).get();

		Thread.sleep(1000); // let the index some time to populate
	}

}
