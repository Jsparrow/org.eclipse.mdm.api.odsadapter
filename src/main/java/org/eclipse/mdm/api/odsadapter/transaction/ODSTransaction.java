package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElemAccess;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

public final class ODSTransaction implements Transaction {

	private final ODSModelManager modelManager;
	private final ApplElemAccess applElemAccess;
	private final AoSession aoSession;

	public ODSTransaction(ODSModelManager modelManager) throws AoException {
		this.modelManager = modelManager;

		aoSession = modelManager.getAoSession().createCoSession();
		applElemAccess = aoSession.getApplElemAccess();
	}

	@Override
	public <T extends Entity> void create(Collection<T> entities) throws DataAccessException {

		Map<EntityType, List<Entity>> entitiesByType = entities.stream().collect(Collectors.groupingBy(modelManager::getEntityType));
		for(Entry<EntityType, List<Entity>> entry : entitiesByType.entrySet()) {
			InsertStatement insertStatement = new InsertStatement(this, entry.getKey());
			entry.getValue().forEach(e -> insertStatement.add(e));
			insertStatement.execute();
		}
	}

	@Override
	public <T extends Entity> void update(Collection<T> entities) throws DataAccessException {
		Map<EntityType, List<Entity>> entitiesByType = entities.stream().collect(Collectors.groupingBy(modelManager::getEntityType));
		for(Entry<EntityType, List<Entity>> entry : entitiesByType.entrySet()) {
			UpdateStatement updateStatement = new UpdateStatement(this, entry.getKey());
			entry.getValue().forEach(e -> updateStatement.add(e));
			updateStatement.execute();
		}
	}

	@Override
	public <T extends Deletable> List<URI> delete(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return Collections.emptyList();
		}
		/*
		 * TODO: this implementation is correct as long as entities are all of the same type!
		 * this has to be fixed!
		 */
		EntityType rootEntityType = modelManager.getEntityType(entities.iterator().next().getClass());
		DeleteStatement deleteStatement = new DeleteStatement(this, rootEntityType, true);
		deleteStatement.addInstances(entities);
		return deleteStatement.execute();
	}

	@Override
	public void writeMeasuredValues(Collection<WriteRequest> writeRequests) throws DataAccessException {
		if(writeRequests.isEmpty()) {
			return;
		}

		Map<ScalarType, List<WriteRequest>> writeRequestsByRawType = writeRequests.stream()
				.collect(Collectors.groupingBy(WriteRequest::getRawScalarType));

		for(List<WriteRequest> writeRequestGroup : writeRequestsByRawType.values()) {
			WriteRequestHandler writeRequestHandler = new WriteRequestHandler(this);
			List<Channel> channels = new ArrayList<>();

			for(WriteRequest writeRequest : writeRequestGroup) {
				Channel channel = writeRequest.getChannel();
				channel.setScalarType(writeRequest.getCalculatedScalarType());
				// TODO it might be required to change relation to another unit?!??
				channels.add(channel);
				writeRequestHandler.addRequest(writeRequest);
			}

			update(channels);
			writeRequestHandler.execute();
		}
	}

	@Override
	public void commit() throws DataAccessException {
		try {
			aoSession.commitTransaction();
		} catch(AoException e) {

		} finally {
			closeSession();
		}
	}

	@Override
	public void abort() throws DataAccessException {
		try {
			aoSession.abortTransaction();
		} catch(AoException e) {

		} finally {
			closeSession();
		}
	}

	void modified(EntityType entityType) {
		// TODO
		// clear the cache of this entity type
	}

	void deleted(EntityType entityType) {
		// TODO
		// clear the cache of this entity type
		// and clear the caches of related entity types!
	}

	ODSModelManager getModelManager() {
		return modelManager;
	}

	ApplElemAccess getApplElemAccess() {
		return applElemAccess;
	}

	private void closeSession() throws DataAccessException {
		try {
			applElemAccess._release();
			aoSession.close();
		} catch(AoException e) {
			throw new DataAccessException("Unable to cloase transaction session due to: " + e.reason, e);
		} finally {
			aoSession._release();
		}
	}

}
