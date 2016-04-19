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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElemAccess;
import org.asam.ods.ApplicationStructure;
import org.asam.ods.BaseStructure;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.EntityCore;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.URI;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateRoot;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ODSTransaction implements Transaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSTransaction.class);

	private final ODSModelManager modelManager;
	private final AoSession aoSession;

	private final int id;

	private final List<EntityCore> coresToApply = new ArrayList<>();

	private ApplicationStructure applicationStructure;
	private BaseStructure baseStructure;

	private ApplElemAccess applElemAccess;

	private CatalogManager catalogManager;

	public ODSTransaction(ODSModelManager modelManager) throws AoException {
		this.modelManager = modelManager;

		aoSession = modelManager.getAoSession().createCoSession();
		id = aoSession.getId();

		// TODO track duration

		aoSession.startTransaction();
		LOGGER.debug("Transaction '{}' started.", id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> void create(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return;
		} else if(entities.stream().filter(e -> e.getURI().getID() > 0).findAny().isPresent()) {
			throw new IllegalArgumentException("At least one given entity is already persisted.");
		}

		try {
			Map<Class<?>, List<T>> entitiesByClassType = entities.stream().collect(Collectors.groupingBy(e -> e.getClass()));

			List<CatalogComponent> catalogComponents = (List<CatalogComponent>) entitiesByClassType.get(CatalogComponent.class);
			if(catalogComponents != null) {
				getCatalogManager().createCatalogComponents(catalogComponents);
			}

			List<CatalogSensor> catalogSensors = (List<CatalogSensor>) entitiesByClassType.get(CatalogSensor.class);
			if(catalogSensors != null) {
				// TODO create CatalogSensors....
				throw new DataAccessException("NOT IMPLEMENTED");
			}

			List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType.get(CatalogAttribute.class);
			if(catalogAttributes != null) {
				getCatalogManager().createCatalogAttributes(catalogAttributes);
			}

			executeStatements(et -> new InsertStatement(this, et), entities);
		} catch(AoException e) {
			throw new DataAccessException(e.reason, e); // TODO

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> void update(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return;
		} else if(entities.stream().filter(e -> e.getURI().getID() < 1).findAny().isPresent()) {
			throw new IllegalArgumentException("At least one given entity is not yet persisted.");
		}

		// TODO if entity instanceof Versionable -> VersionState.EDITING || VersionState.VALID (OLD STATE NOT CURRENT!)!
		// -> if old state is EDITING -> anything may be modified
		// -> if old state is VALID -> only the VersionState is allowed to be changed to ARCHIVED!

		Map<Class<?>, List<T>> entitiesByClassType = entities.stream().collect(Collectors.groupingBy(e -> e.getClass()));
		List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType.get(CatalogAttribute.class);
		if(catalogAttributes != null) {
			getCatalogManager().updateCatalogAttributes(catalogAttributes);
		}

		try {
			executeStatements(et -> new UpdateStatement(this, et), entities);
		} catch(AoException e) {
			throw new DataAccessException(e.reason, e); // TODO
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Deletable> List<URI> delete(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return Collections.emptyList();
		}

		// TODO if entity instanceof Versionable -> VersionState.EDITING (OLD STATE NOT CURRENT!)!

		List<T> filteredEntities = entities.stream().filter(e -> e.getURI().getID() > 0).collect(Collectors.toList());

		try {
			Map<Class<?>, List<T>> entitiesByClassType = filteredEntities.stream().collect(Collectors.groupingBy(e -> e.getClass()));

			List<CatalogComponent> catalogComponents = (List<CatalogComponent>) entitiesByClassType.get(CatalogComponent.class);
			if(catalogComponents != null) {
				getCatalogManager().deleteCatalogComponents(catalogComponents);
			}

			List<CatalogSensor> catalogSensors = (List<CatalogSensor>) entitiesByClassType.get(CatalogSensor.class);
			if(catalogSensors != null) {
				// TODO delete CatalogSensors....
				throw new DataAccessException("NOT IMPLEMENTED");
			}

			List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType.get(CatalogAttribute.class);
			if(catalogAttributes != null) {
				getCatalogManager().deleteCatalogAttributes(catalogAttributes);
			}

			List<TemplateRoot> templateRoots = (List<TemplateRoot>) entitiesByClassType.get(TemplateRoot.class);
			if(templateRoots != null) {
				delete(templateRoots.stream().map(TemplateRoot::getTemplateComponents)
						.collect(ArrayList::new, List::addAll, List::addAll));
			}

			List<TemplateComponent> templateComponents = (List<TemplateComponent>) entitiesByClassType.get(TemplateComponent.class);
			if(templateComponents != null) {
				delete(templateComponents.stream().map(TemplateComponent::getTemplateComponents)
						.collect(ArrayList::new, List::addAll, List::addAll));
			}

			/*
			 * TODO: this implementation is correct as long as entities are all of the same type!
			 * this has to be fixed!
			 */

			return executeStatements(et -> new DeleteStatement(this, et, true), filteredEntities);
		} catch (AoException e) {
			throw new DataAccessException(e.reason, e); // TODO
		}
	}

	@Override
	public void writeMeasuredValues(Collection<WriteRequest> writeRequests) throws DataAccessException {
		if(writeRequests.isEmpty()) {
			return;
		}

		try {
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
		} catch(AoException e) {
			throw new DataAccessException(e.reason, e); // TODO
		}
	}

	@Override
	public void commit() throws DataAccessException {
		try {
			/*
			 * TODO upload of files has to be done BEFORE creating / updating instances
			 */
			aoSession.commitTransaction();

			// commit succeed -> apply changes in entity cores
			coresToApply.forEach(EntityCore::apply);

			if(catalogManager != null) {
				modelManager.reloadApplicationModel();
			}
			/*
			 * TODO trigger an delete of removed file links and log in case of errors (Delete- or Update-Statements)
			 */

			// TODO add statistics to logging (how many created / updated / deleted)
			LOGGER.debug("Transaction '{}' committed.", id);
			closeSession();
		} catch(AoException e) {
			throw new DataAccessException("Unable to commit transaction '" + id + "' due to: " + e.reason, e);
		}
	}

	@Override
	public void abort() {
		try {
			aoSession.abortTransaction();

			/*
			 * TODO in case of uploaded files trigger an delete operation to remove them from the server
			 * (log in case of errors)
			 */

			// TODO add statistics to logging (how many discarded created / updated / deleted)
			LOGGER.debug("Transaction '{}' aborted.", id);
		} catch(AoException e) {
			LOGGER.error("Unable to abort transaction '" + id + "' due to: " + e.reason, e);
		} finally {
			closeSession();
		}
	}

	void addCore(EntityCore core) {
		coresToApply.add(core);
	}

	// TODO is it possible to simplify this?!
	Query createQuery() throws AoException {
		return modelManager.createQuery(getApplElemAccess());
	}

	ODSModelManager getModelManager() {
		return modelManager;
	}

	ApplicationStructure getApplicationStructure() throws AoException {
		if(applicationStructure == null) {
			applicationStructure = aoSession.getApplicationStructure();
		}

		return applicationStructure;
	}

	BaseStructure getBaseStructure() throws AoException {
		if(baseStructure == null) {
			baseStructure = aoSession.getBaseStructure();
		}

		return baseStructure;
	}

	ApplElemAccess getApplElemAccess() throws AoException {
		if(applElemAccess == null) {
			applElemAccess = aoSession.getApplElemAccess();
		}

		return applElemAccess;
	}

	CatalogManager getCatalogManager() {
		if(catalogManager == null) {
			catalogManager = new CatalogManager(this);
		}

		return catalogManager;
	}

	private <T extends Entity> List<URI> executeStatements(Function<EntityType, BaseStatement> statementFactory, Collection<T> entities)
			throws AoException, DataAccessException {
		List<URI> uris = new ArrayList<>();
		Map<EntityType, List<Entity>> entitiesByType = entities.stream().collect(Collectors.groupingBy(modelManager::getEntityType));
		for(Entry<EntityType, List<Entity>> entry : entitiesByType.entrySet()) {
			uris.addAll(statementFactory.apply(entry.getKey()).execute(entry.getValue()));
		}

		return uris;
	}

	private void closeSession() {
		try {
			if(applElemAccess != null) {
				applElemAccess._release();
			}

			if(applicationStructure != null) {
				applicationStructure._release();
			}

			if(baseStructure != null) {
				baseStructure._release();
			}

			// TODO: clear catalog manager...

			aoSession.close();
			LOGGER.debug("Transaction '{}' closed.", id);
		} catch(AoException e) {
			LOGGER.error("Unable to close transaction '" + id + "' due to: " + e.reason, e);
		} finally {
			aoSession._release();
		}
	}

}
