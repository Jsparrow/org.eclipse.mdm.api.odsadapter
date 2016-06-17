/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DeleteStatement extends BaseStatement {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteStatement.class);

	private static final String AE_NAME_TESTSTEP = "TestStep";
	private static final String AE_NAME_MEARESULT = "MeaResult";
	private static final String AE_NAME_MEAQUANTITY = "MeaQuantity";
	private static final String AE_NAME_SUBMATRIX = "SubMatrix";
	private static final String AE_NAME_LOCALCOLUMN = "LocalColumn";
	private static final String AE_NAME_EXTERNAL_COMPONENT = "ExternalComponent";

	private static final String[] AUTO_DELETE_AE_NAMES = {
			AE_NAME_MEAQUANTITY,
			AE_NAME_SUBMATRIX,
			AE_NAME_LOCALCOLUMN,
			AE_NAME_EXTERNAL_COMPONENT
	};

	private final boolean useAutoDeleteFeature;
	private final EntityType uutEntityType;
	private final EntityType tsqEntityType;
	private final EntityType teqEntityType;

	private Map<String, Map<String, URI>> map = new LinkedHashMap<>();
	private List<DeleteStatement> forkedStatements = new ArrayList<>();

	DeleteStatement(ODSTransaction transaction, EntityType entityType, boolean useAutoDeleteFeature) {
		super(transaction, entityType);
		this.useAutoDeleteFeature = useAutoDeleteFeature;
		uutEntityType = getModelManager().getEntityType(ContextRoot.class, ContextType.UNITUNDERTEST);
		tsqEntityType = getModelManager().getEntityType(ContextRoot.class, ContextType.TESTSEQUENCE);
		teqEntityType = getModelManager().getEntityType(ContextRoot.class, ContextType.TESTEQUIPMENT);

		initialize(getEntityType());
	}

	private DeleteStatement(DeleteStatement deleteStatement, EntityType entityType) {
		super(deleteStatement.getTransaction(), entityType);
		useAutoDeleteFeature = deleteStatement.useAutoDeleteFeature;

		uutEntityType = deleteStatement.uutEntityType;
		tsqEntityType = deleteStatement.tsqEntityType;
		teqEntityType = deleteStatement.teqEntityType;

		initialize(entityType);
	}

	@Override
	public List<URI> execute(Collection<Entity> entities) throws AoException, DataAccessException {
		for(Entity entity : entities) {
			addInstance(new URI(entity.getSourceName(), entity.getTypeName(), entity.getID()));
		}

		long start = System.currentTimeMillis();
		List<URI> uris = execute();
		long stop = System.currentTimeMillis();

		LOGGER.debug("{} instances deleted in {} ms.", uris.size(), stop - start);

		return uris;
	}

	private List<URI> execute() throws AoException {
		List<URI> list = executeForkedStatements();

		List<Entry<String, Map<String, URI>>> sorted = new ArrayList<>(map.entrySet());
		Collections.reverse(sorted);
		for(Entry<String, Map<String, URI>> entry : sorted) {
			if(!entry.getValue().isEmpty()) {
				T_LONGLONG aeID = ((ODSEntityType) getModelManager().getEntityType(entry.getKey())).getODSID();
				Collection<URI> instanceURIs = entry.getValue().values();
				getApplElemAccess().deleteInstances(aeID, extractInstanceIDs(instanceURIs));

				list.addAll(instanceURIs);
			}
		}

		return list;
	}

	private T_LONGLONG[] extractInstanceIDs(Collection<URI> uris) {
		return uris.stream().map(u -> ODSConverter.toODSLong(u.getID()))
				.collect(Collectors.toList()).toArray(new T_LONGLONG[uris.size()]);
	}

	private List<URI> executeForkedStatements() throws AoException {
		List<URI> list = new ArrayList<>();
		for(DeleteStatement forkedStatement : forkedStatements) {
			list.addAll(forkedStatement.execute());
		}
		return list;
	}

	private void addInstance(URI uri) throws AoException, DataAccessException {
		if(!uri.getTypeName().equals(getEntityType().getName())) {
			throw new DataAccessException("Unable to add entity with uri '" + uri
					+ "' to this statement (expected entities of type: " + getEntityType() + ").");
		}

		if(!map.get(uri.getTypeName()).containsKey(uri.toString())) {
			lookupChildren(uri);
		}
	}

	private void lookupChildren(URI uri) throws AoException, DataAccessException {
		map.get(uri.getTypeName()).put(uri.toString(), uri);
		EntityType parentEntityType = getModelManager().getEntityType(uri.getTypeName());

		lookupInfoRelations(parentEntityType, uri);

		for(Relation childRelation : parentEntityType.getChildRelations()) {
			EntityType childEntityType = childRelation.getTarget();

			if(useAutoDeleteFeature && isAutoDelete(childEntityType.getName())) {
				continue;
			}

			for(Result result : executeChildrenQuery(parentEntityType, uri, childEntityType)) {
				lookupChildren(create(result.getRecord(childEntityType)));
			}
		}
	}

	private List<Result> executeChildrenQuery(EntityType parentEntityType, URI parentURI, EntityType childEntityType)
			throws AoException, DataAccessException {
		return getTransaction().getModelManager().createQuery().select(childEntityType.getIDAttribute())
				.join(parentEntityType, childEntityType).fetch(Filter.idOnly(parentEntityType, parentURI.getID()));
	}

	private void lookupInfoRelations(EntityType currentEntityType, URI uri) throws AoException, DataAccessException {
		if(AE_NAME_TESTSTEP.equals(currentEntityType.getName())) {
			//delete context data (order) at TestStep
			fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
		}

		if(AE_NAME_MEARESULT.equals(currentEntityType.getName())) {
			//delete context data (result) at MeaResult if the MeaResult has no siblings
			if(getEntityType().equals(getModelManager().getEntityType(Measurement.class))) {
				// delete request was initialized for a measurement
				if(isDeleteMeasurementContext(locateMeasurementSiblings(getEntityType(), uri))) {
					fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
				}
			} else {
				// delete request was initialized for a parent
				fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
			}

			//delete ResultParameterSets at Measurement
			EntityType parameterSetEntityType = getModelManager().getEntityType(ParameterSet.class);
			fork(currentEntityType, uri, parameterSetEntityType);
		}

		if(AE_NAME_MEAQUANTITY.equals(currentEntityType.getName())) {
			//delete ResultParameterSets at Channel
			EntityType parameterSetEntityType = getModelManager().getEntityType(ParameterSet.class);
			fork(currentEntityType, uri, parameterSetEntityType);
		}
	}

	private boolean isDeleteMeasurementContext(List<URI> measurementURIs) {
		// TODO replace measurementEntityType ?
		EntityType measurementEntityType = getTransaction().getModelManager().getEntityType(Measurement.class);
		Map<String, URI> measurementURIMap = map.get(measurementEntityType.getName());
		for(URI measurementURI : measurementURIs) {
			if(!measurementURIMap.containsKey(measurementURI)) {
				return false;
			}
		}
		return true;
	}

	private List<URI> locateMeasurementSiblings(EntityType measurementEntityType, URI uri) throws AoException, DataAccessException {
		//locate TestStep
		EntityType testStepEntityType = getModelManager().getEntityType(TestStep.class);
		Query testStepQuery = getTransaction().getModelManager().createQuery();
		testStepQuery.select(testStepEntityType.getIDAttribute());
		testStepQuery.join(measurementEntityType, testStepEntityType);
		Optional<Result> oResult = testStepQuery.fetchSingleton(Filter.idOnly(measurementEntityType, uri.getID()));
		if(!oResult.isPresent()) {
			throw new DataAccessException("Measurement with no parent TestStep found "
					+ "(should not occur - DB constraint)");
		}

		Long testStepID = oResult.get().getRecord(testStepEntityType).getID();

		Query measurementsQuery = getTransaction().getModelManager().createQuery();
		measurementsQuery.select(measurementEntityType.getIDAttribute());
		measurementsQuery.join(measurementEntityType, testStepEntityType);
		List<Result> results = measurementsQuery.fetch(Filter.idOnly(testStepEntityType, testStepID));

		return results.stream().map(r -> r.getRecord(measurementEntityType)).map(DeleteStatement::create)
				.collect(Collectors.toList());
	}

	private void fork(EntityType currentEntityType, URI uri, EntityType... infoEntityTypes)
			throws AoException, DataAccessException {
		for(EntityType infoEntityType : infoEntityTypes) {
			Optional<Result> result = executeInfoQuery(currentEntityType, uri, infoEntityType);
			if(!result.isPresent()) {
				continue;
			}

			DeleteStatement forkedStatement = new DeleteStatement(this, infoEntityType);
			forkedStatement.addInstance(create(result.get().getRecord(infoEntityType)));
			forkedStatements.add(forkedStatement);
		}
	}

	private Optional<Result> executeInfoQuery(EntityType entityType, URI uri, EntityType infoEntityType)
			throws AoException, DataAccessException {
		return getTransaction().getModelManager().createQuery().selectID(infoEntityType).join(entityType, infoEntityType)
				.fetchSingleton(Filter.idOnly(entityType, uri.getID()));
	}

	private void initialize(EntityType entityType) {
		if(map.containsKey(entityType.getName())) {
			return;
		}
		map.put(entityType.getName(), new HashMap<>());
		List<Relation> childRelations = entityType.getChildRelations();
		for(Relation childRelation : childRelations) {
			EntityType childEntityType = childRelation.getTarget();
			if(useAutoDeleteFeature && isAutoDelete(childEntityType.getName())) {
				continue;
			}

			initialize(childEntityType);
		}
	}

	private boolean isAutoDelete(String aeName) {
		for(String autoDelete : AUTO_DELETE_AE_NAMES) {
			if(autoDelete.equals(aeName)) {
				return true;
			}
		}
		return false;
	}
	
	private static URI create(Record record) {
		return new URI(record.getEntityType().getSourceName(), record.getEntityType().getName(), record.getID());
	}

}
