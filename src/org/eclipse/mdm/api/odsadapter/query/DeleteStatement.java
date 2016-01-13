/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.asam.ods.AoException;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.DataItem;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

public class DeleteStatement {

	private final static String AE_NAME_TESTSTEP = "TestStep";
	private final static String AE_NAME_MEARESULT = "MeaResult";
	private final static String AE_NAME_MEAQUANTITY = "MeaQuantity";
	private final static String AE_NAME_SUBMATRIX = "SubMatrix";
	private final static String AE_NAME_LOCALCOLUMN = "LocalColumn";
	private final static String AE_NAME_EXTERNAL_COMPONENT = "ExternalComponent";


	private final static String[] AUTO_DELETE_AE_NAMES = {
			AE_NAME_MEAQUANTITY,
			AE_NAME_SUBMATRIX,
			AE_NAME_LOCALCOLUMN,
			AE_NAME_EXTERNAL_COMPONENT
	};


	private final QueryService queryService;
	private final ApplElemAccess applElemAccess;
	private final Entity entity;
	private final boolean useAutoDeleteFeature;

	private final Entity uutEntity;
	private final Entity tsqEntity;
	private final Entity teqEntity;

	private Map<String, Map<String, URI>> map = new LinkedHashMap<String, Map<String,URI>>();
	private List<DeleteStatement> forkedStatements = new ArrayList<DeleteStatement>();

	public <T extends DataItem> DeleteStatement(ApplElemAccess applElemAccess, QueryService queryService,
			Entity entity, boolean useAutoDeleteFeature) {

		this.queryService = queryService;
		this.applElemAccess = applElemAccess;
		this.entity = entity;
		this.useAutoDeleteFeature = useAutoDeleteFeature;

		uutEntity = this.queryService.getEntity(ContextType.UNITUNDERTEST);
		tsqEntity = this.queryService.getEntity(ContextType.TESTSEQUENCE);
		teqEntity = this.queryService.getEntity(ContextType.TESTEQUIPMENT);

		initialize(this.entity);

	}


	public void initialize(Entity entity) {

		map.put(entity.getName(), new HashMap<String, URI>());
		List<Relation> childRelations = entity.getChildRelations();
		for(Relation childRelation : childRelations) {

			Entity childEntity = childRelation.getTarget();

			if(useAutoDeleteFeature && isAutoDelete(childEntity.getName())) {
				continue;
			}
			initialize(childEntity);
		}
	}



	public <T extends DataItem> void addInstance(DataItem dataItem) throws DataAccessException {
		addInstance(dataItem.getURI());
	}



	public <T extends DataItem> void addInstances(List<T> dataItems) throws DataAccessException {
		for(T dataItem : dataItems) {
			addInstance(dataItem.getURI());
		}
	}



	public List<URI> execute() throws DataAccessException {
		try {
			List<URI> list = new ArrayList<URI>();

			list.addAll(executeForkedStatements());

			List<Entry<String, Map<String, URI>>> sorted = new ArrayList<>(map.entrySet());
			Collections.reverse(sorted);

			for(Entry<String, Map<String, URI>> entry : sorted) {

				if(entry.getValue().size() <= 0) {
					continue;
				}

				Entity entity = queryService.getEntity(entry.getKey());
				T_LONGLONG aeID = ((ODSEntity)entity).getODSID();
				Collection<URI> instanceURIs = entry.getValue().values();
				T_LONGLONG[] instanceIDs = extractInstanceIDs(instanceURIs);
				applElemAccess.deleteInstances(aeID, instanceIDs);

				list.addAll(instanceURIs);
			};
			return list;

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}



	private void lookupChildren(URI uri) throws DataAccessException {

		map.get(uri.getTypeName()).put(uri.toString(), uri);
		Entity entity = queryService.getEntity(uri.getTypeName());

		lookupInfoRelations(entity, uri);

		for(Relation childRelation : entity.getChildRelations()) {
			Entity childEntity = childRelation.getTarget();

			if(useAutoDeleteFeature && isAutoDelete(childEntity.getName())) {
				continue;
			}

			List<Result> resultList = executeChildrenQuery(entity, uri, childEntity);

			for(Result result : resultList) {
				long childID = result.getRecord(childEntity).getID();
				URI childURI= new URI(uri.getEnvironmentName(), childEntity.getName(), childID);
				lookupChildren(childURI);
			}
		}
	}


	private <T extends DataItem> void addInstance(URI uri) throws DataAccessException {

		Entity incommingEntity = queryService.getEntity(uri.getTypeName());

		if(!incommingEntity.equals(entity)) {
			throw new DataAccessException("unable to at a DataItem with uri '"
					+ uri.toString() + "' form type '" + incommingEntity.getName() + "' "
					+ " to this statement (expected DataItems with type: " + entity.getName() + ")");
		}

		if(map.get(uri.getTypeName()).containsKey(uri.toString())) {
			return;
		}

		lookupChildren(uri);
	}


	private List<Result> executeChildrenQuery(Entity parentEntity, URI parentURI, Entity childEntity)
			throws DataAccessException {
		Query query = queryService.createQuery();
		query.select(childEntity.getIDAttribute());
		query.join(parentEntity, childEntity);
		return query.fetch(Filter.id(parentEntity, parentURI.getID()));
	}



	private Optional<Result> executeInfoQuery(Entity entity, URI uri, Entity infoEntity)
			throws DataAccessException {

		Query query = queryService.createQuery();
		query.selectID(infoEntity);
		query.join(entity, infoEntity);
		return query.fetchSingleton(Filter.id(entity, uri.getID()));

	}



	private T_LONGLONG[] extractInstanceIDs(Collection<URI> uris) {
		List<T_LONGLONG> instanceIDList = new ArrayList<T_LONGLONG>();
		for(URI uri : uris) {
			instanceIDList.add(ODSConverter.toODSLong(uri.getID()));
		}
		return instanceIDList.toArray(new T_LONGLONG[instanceIDList.size()]);
	}



	private boolean isAutoDelete(String aeName) {
		for(String autoDelete : AUTO_DELETE_AE_NAMES) {
			if(autoDelete.equals(aeName)) {
				return true;
			}
		}
		return false;
	}


	private void lookupInfoRelations(Entity entity, URI uri) throws DataAccessException {


		//delete context data (order) at TestStep
		if(entity.getName().equalsIgnoreCase(AE_NAME_TESTSTEP)) {
			fork(entity, uri, uutEntity, tsqEntity, teqEntity);
		}

		//delete context data (result) at MeaResult if the MeaResult has no siblings
		//delete ResultParameterSets at Measurement
		if(entity.getName().equalsIgnoreCase(AE_NAME_MEARESULT)) {

			if(this.entity.equals(queryService.getEntity(Measurement.class))) {
				List<URI> measurementURIs = locateMeasurementSiblings(this.entity, uri);
				if(isDeleteMeasurementContext(measurementURIs)) {
					fork(entity, uri, uutEntity, tsqEntity, teqEntity);
				}
			} else {
				fork(entity, uri, uutEntity, tsqEntity, teqEntity);
			}

			Entity paramSetEntity = queryService.getEntity(ParameterSet.class);
			fork(entity, uri, paramSetEntity);
		}

		//delete ResultParameterSets at Channel
		if(entity.getName().equalsIgnoreCase(AE_NAME_MEAQUANTITY)) {
			Entity paramEntity = queryService.getEntity(ParameterSet.class);
			fork(entity, uri, paramEntity);
		}
	}

	private List<URI> executeForkedStatements() throws DataAccessException {
		List<URI> list = new ArrayList<URI>();
		for(DeleteStatement forkedStatement : forkedStatements) {
			list.addAll(forkedStatement.execute());
		}
		return list;
	}


	private void fork(Entity entity, URI uri, Entity... infoEntities) throws DataAccessException {

		for(Entity infoEntity : infoEntities) {

			Optional<Result> result = executeInfoQuery(entity, uri, infoEntity);

			if(!result.isPresent()) {
				continue;
			}

			Value idValue = result.get().getValue(infoEntity.getIDAttribute());
			URI infoURI = new URI(uri.getEnvironmentName(), infoEntity.getName(), idValue.extract());

			DeleteStatement forkedStatement = new DeleteStatement(applElemAccess,
					queryService, infoEntity, useAutoDeleteFeature);
			forkedStatement.addInstance(infoURI);

			forkedStatements.add(forkedStatement);
		}
	}

	private boolean isDeleteMeasurementContext(List<URI> measurementURIs) {
		Map<String, URI> measurementURIMap = map.get(ODSUtils.getAEName(Measurement.class));
		for(URI measurementURI : measurementURIs) {
			if(!measurementURIMap.containsKey(measurementURI)) {
				return false;
			}
		}
		return true;
	}


	private List<URI> locateMeasurementSiblings(Entity measurementEntity, URI uri) throws DataAccessException {

		List<URI> list = new ArrayList<URI>();

		//locate TestStep
		Entity testStepEntity = queryService.getEntity(TestStep.class);
		Query testStepQuery = queryService.createQuery();
		testStepQuery.select(testStepEntity.getIDAttribute());
		testStepQuery.join(measurementEntity, testStepEntity);
		Optional<Result> oResult = testStepQuery.fetchSingleton(Filter.id(measurementEntity, uri.getID()));

		if(!oResult.isPresent()) {
			throw new DataAccessException("Measurement with no parent TestStep found "
					+ "(should not occur - DB constraint)");
		}

		long testStepID = oResult.get().getRecord(testStepEntity).getID();

		Query measurementsQuery = queryService.createQuery();
		measurementsQuery.select(measurementEntity.getIDAttribute());
		measurementsQuery.join(measurementEntity, testStepEntity);
		List<Result> results = measurementsQuery.fetch(Filter.id(testStepEntity, testStepID));

		for(Result result : results) {
			long measurementID = result.getRecord(measurementEntity).getID();
			URI measurementURI = new URI(uri.getEnvironmentName(), measurementEntity.getName(), measurementID);
			list.add(measurementURI);
		}

		return list;
	}
}
