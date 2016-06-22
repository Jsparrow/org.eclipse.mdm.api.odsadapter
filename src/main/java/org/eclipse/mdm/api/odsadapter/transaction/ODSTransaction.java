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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.dflt.model.TemplateComponent;
import org.eclipse.mdm.api.dflt.model.TemplateRoot;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ODSTransaction implements Transaction {

	// TODO: it should be possible to a attach a listener
	// -> progress notification updates while uploading files
	// -> any other useful informations?!
	// -> splitting of tasks into subtasks may be required...

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSTransaction.class);

	// this one is stored in case of application model modifications
	private final ODSModelManager parentModelManager;

	// this one is used to access the application model and execute queries
	// instance is decoupled from its parent
	private final ODSModelManager modelManager;

	private final String id = UUID.randomUUID().toString();

	private final List<Core> modifiedCores = new ArrayList<>();

	private CatalogManager catalogManager;

	public ODSTransaction(ODSModelManager parentModelManager) throws AoException {
		this.parentModelManager = parentModelManager;
		modelManager = parentModelManager.newSession();

		// TODO track duration

		modelManager.getAoSession().startTransaction();
		LOGGER.debug("Transaction '{}' started.", id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> void create(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return;
		} else if(entities.stream().filter(e -> e.getID() > 0).findAny().isPresent()) {
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
		} else if(entities.stream().filter(e -> e.getID() < 1).findAny().isPresent()) {
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
	public <T extends Deletable> void delete(Collection<T> entities) throws DataAccessException {
		if(entities.isEmpty()) {
			return;
		}

		// TODO if entity instanceof Versionable -> VersionState.EDITING (OLD STATE NOT CURRENT!)!

		List<T> filteredEntities = entities.stream().filter(e -> e.getID() > 0).collect(Collectors.toList());

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

			executeStatements(et -> new DeleteStatement(this, et, true), filteredEntities);
			//			executeStatements(et -> new DeleteStatement(this, et, true), filteredEntities);
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
			modelManager.getAoSession().commitTransaction();

			// commit succeed -> apply changes in entity cores
			modifiedCores.forEach(Core::apply);

			if(catalogManager != null) {
				// application model has been modified -> reload
				parentModelManager.reloadApplicationModel();
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
			modelManager.getAoSession().abortTransaction();

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

	void addCore(Core core) {
		modifiedCores.add(core);
	}

	ODSModelManager getModelManager() {
		return modelManager;
	}

	private CatalogManager getCatalogManager() {
		if(catalogManager == null) {
			catalogManager = new CatalogManager(this);
		}

		return catalogManager;
	}

	private <T extends Entity> void executeStatements(Function<EntityType, BaseStatement> statementFactory, Collection<T> entities)
			throws AoException, DataAccessException {
		Map<EntityType, List<Entity>> entitiesByType = entities.stream().collect(Collectors.groupingBy(modelManager::getEntityType));
		for(Entry<EntityType, List<Entity>> entry : entitiesByType.entrySet()) {
			statementFactory.apply(entry.getKey()).execute(entry.getValue());
		}
	}

	private void closeSession() {
		try {
			if(catalogManager != null) {
				catalogManager.clear();
			}

			modelManager.close();
			LOGGER.debug("Transaction '{}' closed.", id);
		} catch(AoException e) {
			LOGGER.error("Unable to close transaction '" + id + "' due to: " + e.reason, e);
		}
	}

}
