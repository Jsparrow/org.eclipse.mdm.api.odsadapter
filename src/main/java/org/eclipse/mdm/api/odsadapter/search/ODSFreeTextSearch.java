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


package org.eclipse.mdm.api.odsadapter.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class handles the requests which are sent to the FreeTextSearch
 * 
 * @author Christian Weyermann
 *
 */
public class ODSFreeTextSearch {

	/**
	 * This is the payload which needs to be added to the post to add a
	 */
	private static final String ES_POSTDATA = "{\"query\":{\"simple_query_string\":{\"query\":\"%s\",\"default_operator\":\"or\",\"lenient\":\"true\",\"fields\":[\"name^2\",\"_all\"]}}}";

	/**
	 * mainly logs requests on INFO
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ODSFreeTextSearch.class);

	/**
	 * Used to finally load the Entites
	 */
	private EntityLoader loader;

	/**
	 * The URL is hard coded
	 */
	private String url;

	/**
	 * The client is created once and reused
	 */
	private HttpClient client;

	/**
	 * This will start up the FreeText Search. No upfron querries are done. Thus
	 * this can be called as often as desired without any major performance loss
	 * 
	 * @param entityLoader
	 * @param sourceName
	 * @throws DataAccessException
	 */
	public ODSFreeTextSearch(EntityLoader entityLoader, String sourceName, String host) throws DataAccessException {
		this.loader = entityLoader;

		url = host + "/" + sourceName.toLowerCase() + "/_search?fields=_type,_id,_index&size=50";

		client = new HttpClient();
	}

	/**
	 * A search which is compatible to the Search as defined in the rest of the
	 * API.
	 * 
	 * @param inputQuery
	 * @return never null, but maybe empty
	 */
	public Map<Class<? extends Entity>, List<Entity>> search(String inputQuery) {
		Map<Class<? extends Entity>, List<Entity>> result = new HashMap<>();

		Map<Class<? extends Entity>, List<String>> instances = searchIds(inputQuery);
		instances.keySet().forEach(type -> convertIds2Entities(result, instances, type));
		return result;
	}

	/**
	 * A search which is compatible to the Search as defined in the rest of the
	 * API.
	 * 
	 * @param inputQuery
	 * @return never null, but maybe empty
	 */
	public Map<Class<? extends Entity>, List<String>> searchIds(String inputQuery) {
		Map<Class<? extends Entity>, List<String>> instanceIds = new HashMap<>();

		JsonElement root = queryElasticSearch(inputQuery);
		if (root != null) {
			JsonArray hits = root.getAsJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();

			hits.forEach(e -> put(e, instanceIds));

		}
		return instanceIds;
	}

	/**
	 * Converts all instances to entities
	 * 
	 * @param convertedMap
	 *            it will
	 * @param map
	 * @param type
	 */
	private void convertIds2Entities(Map<Class<? extends Entity>, List<Entity>> convertedMap,
			Map<Class<? extends Entity>, List<String>> map, Class<? extends Entity> type) {
		try {
			List<Entity> list = new ArrayList<>();
			list.addAll(loader.loadAll(new Key<>(type), map.get(type)));

			convertedMap.put(type, list);
		} catch (DataAccessException e) {
			throw new IllegalStateException("Cannot load ids from ODS. This means no results are available", e);
		}

	}

	/**
	 * Puts all the hits in elasticsearch
	 * 
	 * @param hit
	 *            a hit as given from ElasticSearch
	 * @param map
	 *            the map of all ids for the class of the entity
	 */
	private void put(JsonElement hit, Map<Class<? extends Entity>, List<String>> map) {
		JsonObject object = hit.getAsJsonObject();

		String type = object.get("_type").getAsString();
		Class<? extends Entity> clazz = getClass4Type(type);

		if (clazz != null) {
			if (!map.containsKey(clazz)) {
				List<String> list = new ArrayList<>();
				map.put(clazz, list);
			}

			List<String> list = map.get(clazz);
			list.add((String) object.get("_id").getAsString());
		}
	}

	/**
	 * 
	 * @param type
	 *            the type as given by elasticsearch
	 * @return the class of each element
	 */
	private Class<? extends Entity> getClass4Type(String type) {
		Class<? extends Entity> clazz;
		switch (type) {
		case "TestStep":
			clazz = TestStep.class;
			break;
		case "Measurement":
			clazz = Measurement.class;
			break;
		case "Test":
			clazz = Test.class;
			break;
		default:
			clazz = null;
		}
		return clazz;
	}

	/**
	 * This method actually querries ElasticSearch.
	 * 
	 * @param inputQuery
	 * @return
	 */
	private JsonElement queryElasticSearch(String inputQuery) {
		PostMethod post = new PostMethod(url);

		String requestJson = buildRequestJson(inputQuery);

		LOGGER.info("POST: " + url);
		LOGGER.info("Asking: " + requestJson);
		byte[] json = requestJson.getBytes();
		post.setRequestEntity(new ByteArrayRequestEntity(json, "application/json"));

		JsonElement result = execute(post);
		LOGGER.info("Answered:" + result);

		return result;
	}

	/**
	 * Actually builds the json
	 * 
	 * @param inputQuery
	 * @return
	 */
	private String buildRequestJson(String inputQuery) {
		String query = StringEscapeUtils.escapeJson(inputQuery);
		return String.format(ES_POSTDATA, query);
	}

	/**
	 * Executes the HTTP method and expects a json in the return payload, which
	 * is then returned
	 * 
	 * @param method
	 * @return
	 */
	private JsonElement execute(HttpMethod method) {
		try {
			int status = client.executeMethod(method);
			if (status == 404) {
				return null;
			}

			checkError(status);
			return buildResponseJson(method);
		} catch (IOException e) {
			throw new IllegalStateException("Problems querying ElasticSearch.", e);
		}
	}

	/**
	 * Reads out the http method and builds the JSON via GSON
	 * 
	 * @param method
	 * @return
	 * @throws IOException
	 */
	private JsonElement buildResponseJson(HttpMethod method) throws IOException {
		JsonElement res = null;

		InputStream in = method.getResponseBodyAsStream();
		try (InputStreamReader reader = new InputStreamReader(in)) {
			res = new JsonParser().parse(reader);
		}

		return res;
	}

	/**
	 * If an error occured an appropriate exception is thrown.
	 * 
	 * @param status
	 */
	private void checkError(int status) {
		String text = String.format("ElasticSearch answered %d. ", status);

		if (status / 100 != 2) {
			throw new IllegalStateException(text);
		}
	}
}