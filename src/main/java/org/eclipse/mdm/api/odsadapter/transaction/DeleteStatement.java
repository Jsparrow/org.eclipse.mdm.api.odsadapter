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
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteStatement {

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

	private final ODSTransaction transaction;

	private final EntityType entityType;
	private final boolean useAutoDeleteFeature;

	private final EntityType uutEntityType;
	private final EntityType tsqEntityType;
	private final EntityType teqEntityType;

	private Map<String, Map<String, URI>> map = new LinkedHashMap<>();
	private List<DeleteStatement> forkedStatements = new ArrayList<>();

	private final boolean isRootStatement;

	public DeleteStatement(ODSTransaction transaction, EntityType entityType, boolean useAutoDeleteFeature) {
		this.transaction = transaction;
		this.entityType = entityType;
		this.useAutoDeleteFeature = useAutoDeleteFeature;

		uutEntityType = transaction.getModelManager().getEntityType(ContextType.UNITUNDERTEST);
		tsqEntityType = transaction.getModelManager().getEntityType(ContextType.TESTSEQUENCE);
		teqEntityType = transaction.getModelManager().getEntityType(ContextType.TESTEQUIPMENT);

		initialize(this.entityType);
		isRootStatement = true;
	}

	private DeleteStatement(DeleteStatement deleteStatement, EntityType entityType) {
		transaction = deleteStatement.transaction;
		useAutoDeleteFeature = deleteStatement.useAutoDeleteFeature;
		this.entityType = entityType;

		uutEntityType = deleteStatement.uutEntityType;
		tsqEntityType = deleteStatement.tsqEntityType;
		teqEntityType = deleteStatement.teqEntityType;

		initialize(entityType);
		isRootStatement = false;
	}

	public void initialize(EntityType entityType) {
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

	public <T extends Entity> void addInstances(Collection<T> entities) throws DataAccessException {
		for(T entity : entities) {
			addInstance(entity.getURI());
		}
	}

	public List<URI> execute() throws DataAccessException {
		try {
			long start = System.currentTimeMillis();
			List<URI> list = executeForkedStatements();

			List<Entry<String, Map<String, URI>>> sorted = new ArrayList<>(map.entrySet());
			Collections.reverse(sorted);
			for(Entry<String, Map<String, URI>> entry : sorted) {
				if(!entry.getValue().isEmpty()) {
					T_LONGLONG aeID = ((ODSEntityType) transaction.getModelManager().getEntityType(entry.getKey())).getODSID();
					Collection<URI> instanceURIs = entry.getValue().values();
					transaction.getApplElemAccess().deleteInstances(aeID, extractInstanceIDs(instanceURIs));

					list.addAll(instanceURIs);
				}
			}
			long stop = System.currentTimeMillis();

			if(isRootStatement) {
				LOGGER.debug("{} instances deleted in {} ms.", list.size(), stop - start);
			}

			return list;

		} catch(AoException aoe) {
			throw new DataAccessException(aoe.reason, aoe);
		}
	}

	private void lookupChildren(URI uri) throws DataAccessException {
		map.get(uri.getTypeName()).put(uri.toString(), uri);
		EntityType parentEntityType = transaction.getModelManager().getEntityType(uri.getTypeName());

		lookupInfoRelations(parentEntityType, uri);

		for(Relation childRelation : parentEntityType.getChildRelations()) {
			EntityType childEntityType = childRelation.getTarget();

			if(useAutoDeleteFeature && isAutoDelete(childEntityType.getName())) {
				continue;
			}

			for(Result result : executeChildrenQuery(parentEntityType, uri, childEntityType)) {
				lookupChildren(result.getRecord(childEntityType).createURI());
			}
		}
	}

	private void addInstance(URI uri) throws DataAccessException {
		if(!uri.getTypeName().equals(entityType.getName())) {
			throw new DataAccessException("Unable to add entity with uri '" + uri
					+ "' to this statement (expected entities of type: " + entityType + ").");
		}

		if(!map.get(uri.getTypeName()).containsKey(uri.toString())) {
			lookupChildren(uri);
		}
	}

	private List<Result> executeChildrenQuery(EntityType parentEntityType, URI parentURI, EntityType childEntityType)
			throws DataAccessException {
		Query query = transaction.getModelManager().createQuery();
		query.select(childEntityType.getIDAttribute());
		query.join(parentEntityType, childEntityType);
		return query.fetch(Filter.idOnly(parentEntityType, parentURI.getID()));
	}

	private Optional<Result> executeInfoQuery(EntityType entityType, URI uri, EntityType infoEntityType) throws DataAccessException {
		return transaction.getModelManager().createQuery()
				.selectID(infoEntityType)
				.join(entityType, infoEntityType)
				.fetchSingleton(Filter.idOnly(entityType, uri.getID()));

	}

	private T_LONGLONG[] extractInstanceIDs(Collection<URI> uris) {
		return uris.stream().map(u -> ODSConverter.toODSLong(u.getID()))
				.collect(Collectors.toList()).toArray(new T_LONGLONG[uris.size()]);
	}

	private boolean isAutoDelete(String aeName) {
		for(String autoDelete : AUTO_DELETE_AE_NAMES) {
			if(autoDelete.equals(aeName)) {
				return true;
			}
		}
		return false;
	}

	private void lookupInfoRelations(EntityType currentEntityType, URI uri) throws DataAccessException {
		if(AE_NAME_TESTSTEP.equals(currentEntityType.getName())) {
			//delete context data (order) at TestStep
			fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
		}

		if(AE_NAME_MEARESULT.equals(currentEntityType.getName())) {
			//delete context data (result) at MeaResult if the MeaResult has no siblings
			if(entityType.equals(transaction.getModelManager().getEntityType(Measurement.class))) {
				// delete request was initialized for a measurement
				if(isDeleteMeasurementContext(locateMeasurementSiblings(entityType, uri))) {
					fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
				}
			} else {
				// delete request was initialized for a parent
				fork(currentEntityType, uri, uutEntityType, tsqEntityType, teqEntityType);
			}

			//delete ResultParameterSets at Measurement
			EntityType parameterSetEntityType = transaction.getModelManager().getEntityType(ParameterSet.class);
			fork(currentEntityType, uri, parameterSetEntityType);
		}

		if(AE_NAME_MEAQUANTITY.equals(currentEntityType.getName())) {
			//delete ResultParameterSets at Channel
			EntityType parameterSetEntityType = transaction.getModelManager().getEntityType(ParameterSet.class);
			fork(currentEntityType, uri, parameterSetEntityType);
		}
	}

	private List<URI> executeForkedStatements() throws DataAccessException {
		List<URI> list = new ArrayList<>();
		for(DeleteStatement forkedStatement : forkedStatements) {
			list.addAll(forkedStatement.execute());
		}
		return list;
	}

	private void fork(EntityType currentEntityType, URI uri, EntityType... infoEntityTypes) throws DataAccessException {
		for(EntityType infoEntityType : infoEntityTypes) {
			Optional<Result> result = executeInfoQuery(currentEntityType, uri, infoEntityType);
			if(!result.isPresent()) {
				continue;
			}

			DeleteStatement forkedStatement = new DeleteStatement(this, infoEntityType);
			forkedStatement.addInstance(result.get().getRecord(infoEntityType).createURI());
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

	private List<URI> locateMeasurementSiblings(EntityType measurementEntityType, URI uri) throws DataAccessException {
		//locate TestStep
		EntityType testStepEntityType = transaction.getModelManager().getEntityType(TestStep.class);
		Query testStepQuery = transaction.getModelManager().createQuery();
		testStepQuery.select(testStepEntityType.getIDAttribute());
		testStepQuery.join(measurementEntityType, testStepEntityType);
		Optional<Result> oResult = testStepQuery.fetchSingleton(Filter.idOnly(measurementEntityType, uri.getID()));
		if(!oResult.isPresent()) {
			throw new DataAccessException("Measurement with no parent TestStep found "
					+ "(should not occur - DB constraint)");
		}

		Long testStepID = oResult.get().getRecord(testStepEntityType).getID();

		Query measurementsQuery = transaction.getModelManager().createQuery();
		measurementsQuery.select(measurementEntityType.getIDAttribute());
		measurementsQuery.join(measurementEntityType, testStepEntityType);
		List<Result> results = measurementsQuery.fetch(Filter.idOnly(testStepEntityType, testStepID));

		return results.stream().map(r -> r.getRecord(measurementEntityType)).map(Record::createURI)
				.collect(Collectors.toList());
	}

}
