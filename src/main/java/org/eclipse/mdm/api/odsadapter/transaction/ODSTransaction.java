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


package org.eclipse.mdm.api.odsadapter.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.eclipse.mdm.api.base.Transaction;
import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.massdata.WriteRequest;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.dflt.model.TemplateAttribute;
import org.eclipse.mdm.api.odsadapter.ODSContext;
import org.eclipse.mdm.api.odsadapter.filetransfer.Transfer;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ODS implementation of the {@link Transaction} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class ODSTransaction implements Transaction {

	// TODO: it should be possible to a attach a progress listener
	// -> progress notification updates while uploading files
	// -> any other useful informations?!
	// -> splitting of tasks into subtasks may be required...

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSTransaction.class);

	// this one is stored in case of application model modifications
	private final ODSContext parentContext;
	
	// this one is used to access the application model and execute queries
	// instance is decoupled from its parent
	private final ODSContext context;
	
	// only for logging
	private final String id = UUID.randomUUID().toString();

	// need to write version == instanceID -> update after create
	private final List<ContextRoot> contextRoots = new ArrayList<>();

	// reset instance IDs on abort
	private final List<Core> created = new ArrayList<>();

	// apply changes
	private final List<Core> modified = new ArrayList<>();

	private final Entity entity;
	private final Transfer transfer;

	private UploadService uploadService;

	private CatalogManager catalogManager;

	/**
	 * Constructor.
	 *
	 * @param parentModelManager
	 *            Used to access the persistence.
	 * @param entity
	 *            Used for security checks
	 * @param transfer
	 *            The file transfer type.
	 * @throws AoException
	 *             Thrown if unable to start a co-session.
	 */
	public ODSTransaction(ODSContext parentContext, Entity entity, Transfer transfer) throws AoException {
		this.parentContext = parentContext;
		this.entity = entity;
		this.transfer = transfer;
		context = parentContext.newContext();
		context.getAoSession().startTransaction();
		LOGGER.debug("Transaction '{}' started.", id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> void create(Collection<T> entities) {
		if (entities.isEmpty()) {
			return;
		} else if (entities.stream().filter(e -> ODSUtils.isValidID(e.getID())).findAny().isPresent()) {
			throw new IllegalArgumentException("At least one given entity is already persisted.");
		}

		try {
			Map<Class<?>, List<T>> entitiesByClassType = entities.stream()
					.collect(Collectors.groupingBy(Entity::getClass));

			List<CatalogComponent> catalogComponents = (List<CatalogComponent>) entitiesByClassType
					.get(CatalogComponent.class);
			if (catalogComponents != null) {
				getCatalogManager().createCatalogComponents(catalogComponents);
			}

			List<CatalogSensor> catalogSensors = (List<CatalogSensor>) entitiesByClassType.get(CatalogSensor.class);
			if (catalogSensors != null) {
				// TODO anehmer on 2017-11-16: avalon 4.3b throws an exception in
				// AoSession.commitTransaction() if multiple
				// catalog sensors have been deleted and leaves the application
				// model in a broken state. This is also stated in the documentation. This
				// comment should be removed later.
				getCatalogManager().createCatalogSensors(catalogSensors);
			}

			List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType
					.get(CatalogAttribute.class);
			if (catalogAttributes != null) {
				getCatalogManager().createCatalogAttributes(catalogAttributes);
			}

			List<TemplateAttribute> templateAttributes = (List<TemplateAttribute>) entitiesByClassType
					.get(TemplateAttribute.class);
			if (templateAttributes != null) {
				List<TemplateAttribute> filtered = getFileLinkTemplateAttributes(templateAttributes);
				if (!filtered.isEmpty()) {
					getUploadService().upload(filtered, null);
				}
			}

			List<TestStep> testSteps = (List<TestStep>) entitiesByClassType.get(TestStep.class);
			if (testSteps != null) {
				create(testSteps.stream().map(ContextRoot::of).collect(ArrayList::new, List::addAll, List::addAll));
			}

			List<Measurement> measurements = (List<Measurement>) entitiesByClassType.get(Measurement.class);
			if (measurements != null) {
				// use set here, since measurement sibling point to the same
				// context roots
				create(measurements.stream().map(ContextRoot::of).collect(HashSet::new, Set::addAll, Set::addAll));
			}

			executeStatements(et -> new InsertStatement(this, et), entities);

			List<ContextRoot> roots = (List<ContextRoot>) entitiesByClassType.get(ContextRoot.class);
			if (roots != null) {
				roots.forEach(contextRoot -> contextRoot.setVersion(contextRoot.getID()));

				// this will restore the ASAM path of each context root
				executeStatements(et -> new UpdateStatement(this, et, true), roots);
				contextRoots.addAll(roots);
			}
		} catch (AoException e) {
			throw new DataAccessException("Unable to write new entities due to: " + e.reason, e);
		} catch (IOException e) {
			throw new DataAccessException("Unable to write new entities due to: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Entity> void update(Collection<T> entities) {
		if (entities.isEmpty()) {
			return;
		} else if (entities.stream().filter(e -> !ODSUtils.isValidID(e.getID())).findAny().isPresent()) {
			throw new IllegalArgumentException("At least one given entity is not yet persisted.");
		}

		try {
			Map<Class<?>, List<T>> entitiesByClassType = entities.stream()
					.collect(Collectors.groupingBy(Entity::getClass));
			List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType
					.get(CatalogAttribute.class);
			if (catalogAttributes != null) {
				getCatalogManager().updateCatalogAttributes(catalogAttributes);
			}

			List<TemplateAttribute> templateAttributes = (List<TemplateAttribute>) entitiesByClassType
					.get(TemplateAttribute.class);
			if (templateAttributes != null) {
				List<TemplateAttribute> filtered = getFileLinkTemplateAttributes(templateAttributes);
				if (!filtered.isEmpty()) {
					getUploadService().upload(filtered, null);
				}
			}
			executeStatements(et -> new UpdateStatement(this, et, false), entities);
		} catch (AoException e) {
			throw new DataAccessException("Unable to update entities due to: " + e.reason, e);
		} catch (IOException e) {
			throw new DataAccessException("Unable to update entities due to: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Deletable> void delete(Collection<T> entities) {
		if (entities.isEmpty()) {
			return;
		}

		List<T> filteredEntities = entities.stream().filter(e -> ODSUtils.isValidID(e.getID()))
				.collect(Collectors.toList());

		try {
			Map<Class<?>, List<T>> entitiesByClassType = filteredEntities.stream()
					.collect(Collectors.groupingBy(Deletable::getClass));

			List<CatalogComponent> catalogComponents = (List<CatalogComponent>) entitiesByClassType
					.get(CatalogComponent.class);
			if (catalogComponents != null) {
				getCatalogManager().deleteCatalogComponents(catalogComponents);
			}

			List<CatalogSensor> catalogSensors = (List<CatalogSensor>) entitiesByClassType.get(CatalogSensor.class);
			if (catalogSensors != null) {
				// TODO anehmer on 2017-11-16: avalon 4.3b throws an exception in
				// AoSession.commitTransaction() if multiple
				// catalog sensors have been deleted and leaves the application
				// model in a broken state. This is also stated in the documentation. This
				// comment should be removed later.
				getCatalogManager().deleteCatalogSensors(catalogSensors);
			}

			List<CatalogAttribute> catalogAttributes = (List<CatalogAttribute>) entitiesByClassType
					.get(CatalogAttribute.class);
			if (catalogAttributes != null) {
				getCatalogManager().deleteCatalogAttributes(catalogAttributes);
			}

			/*
			 * TODO: for any template that has to be deleted it is required to
			 * ensure there are no links to it...
			 */

			executeStatements(et -> new DeleteStatement(this, et, true), filteredEntities);
		} catch (AoException e) {
			throw new DataAccessException("Unable to delete entities due to: " + e.reason, e);
		} catch (IOException e) {
			throw new DataAccessException("Unable to delete entities due to: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeMeasuredValues(Collection<WriteRequest> writeRequests) {
		if (writeRequests.isEmpty()) {
			return;
		}

		try {
			Map<ScalarType, List<WriteRequest>> writeRequestsByRawType = writeRequests.stream()
					.collect(Collectors.groupingBy(WriteRequest::getRawScalarType));

			for (List<WriteRequest> writeRequestGroup : writeRequestsByRawType.values()) {
				WriteRequestHandler writeRequestHandler = new WriteRequestHandler(this);
				List<Channel> channels = new ArrayList<>();

				writeRequestGroup.forEach(writeRequest -> {
					Channel channel = writeRequest.getChannel();
					channel.setScalarType(writeRequest.getCalculatedScalarType());
					// TODO it might be required to change relation to another
					// unit?!??
					channels.add(channel);
					writeRequestHandler.addRequest(writeRequest);
				});

				update(channels);
				writeRequestHandler.execute();
			}
		} catch (AoException e) {
			throw new DataAccessException("Unable to write measured values due to: " + e.reason, e);
		} catch (IOException e) {
			throw new DataAccessException("Unable to write measured values due to: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		try {
			context.getAoSession().commitTransaction();

			// commit succeed -> apply changes in entity cores
			modified.forEach(Core::apply);

			// remove deleted remote files
			if (uploadService != null) {
				uploadService.commit();
			}

			if (catalogManager != null) {
				// application model has been modified -> reload
				parentContext.getODSModelManager().reloadApplicationModel();
			}

			LOGGER.debug("Transaction '{}' committed.", id);
			closeSession();
		} catch (AoException e) {
			throw new DataAccessException(new StringBuilder().append("Unable to commit transaction '").append(id).append("' due to: ").append(e.reason).toString(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void abort() {
		try {
			if (uploadService != null) {
				uploadService.abort();
			}

			// reset version, since creation failed or was aborted
			contextRoots.forEach(cr -> cr.setVersion(null));

			// reset instance IDs
			String virtualID = "0";
			created.forEach(c -> c.setID(virtualID));

			context.getAoSession().abortTransaction();

			LOGGER.debug("Transaction '{}' aborted.", id);
		} catch (AoException e) {
			LOGGER.error(new StringBuilder().append("Unable to abort transaction '").append(id).append("' due to: ").append(e.reason).toString(), e);
		} finally {
			closeSession();
		}
	}

	/**
	 * Once {@link #abort()} is called instance ID of given {@link Core} will be
	 * reset to {@code 0} which indicates a virtual {@link Entity}, not yet
	 * persisted, entity.
	 *
	 * @param core
	 *            The {@code Core} of a newly written {@code Entity}.
	 */
	void addCreated(Core core) {
		created.add(core);
	}

	/**
	 * Once {@link #commit()} is {@link Core#apply()} will be called to apply
	 * modified {@link Value} contents and removed related entities.
	 *
	 * @param core
	 *            The {@code Core} of an updated {@code Entity}.
	 */
	void addModified(Core core) {
		modified.add(core);
	}

	/**
	 * Returns the {@link ODSContext}.
	 *
	 * @return The {@code ODSContext} is returned.
	 */
	ODSContext getContext() {
		return context;
	}

	/**
	 * Returns the {@link ODSModelManager}.
	 *
	 * @return The {@code ODSModelManager} is returned.
	 */
	ODSModelManager getModelManager() {
		return context.getODSModelManager();
	}
	
	/**
	 * Returns the {@link UploadService}.
	 *
	 * @return The {@code UploadService} is returned.
	 * @throws DataAccessException
	 *             Thrown if file transfer is not possible.
	 */
	UploadService getUploadService() {
		if (uploadService == null) {
			if (context.getFileServer() == null) {
				throw new DataAccessException("CORBA file server is not available.");
			}

			// upload service starts a periodic session refresh task -> lazy
			// instantiation
			uploadService = new UploadService(context, entity, transfer);
		}

		return uploadService;
	}

	/**
	 * Returns the {@link CatalogManager}.
	 *
	 * @return The {@code CatalogManager} is returned.
	 */
	private CatalogManager getCatalogManager() {
		if (catalogManager == null) {
			catalogManager = new CatalogManager(this);
		}

		return catalogManager;
	}

	/**
	 * Collects {@link TemplateAttribute}s with a valid default {@link Value} of
	 * type {@link ValueType#FILE_LINK} or {@link ValueType#FILE_LINK_SEQUENCE}.
	 *
	 * @param templateAttributes
	 *            The processed {@code TemplateAttribute}s.
	 * @return Returns {@link TemplateAttribute} which have {@link FileLink}s
	 *         stored as default {@code Value}.
	 */
	private List<TemplateAttribute> getFileLinkTemplateAttributes(List<TemplateAttribute> templateAttributes) {
		return templateAttributes.stream().filter(ta -> {
			Value value = ta.getDefaultValue();
			return value.getValueType().isFileLinkType() && value.isValid();
		}).collect(Collectors.toList());
	}

	/**
	 * Executes statements for given entities by using given statement factory.
	 *
	 * @param <T>
	 *            The entity type.
	 * @param statementFactory
	 *            Used to create a new statement for a given {@link EntityType}.
	 * @param entities
	 *            The processed {@code Entity}s.
	 * @throws AoException
	 *             Thrown if the execution fails.
	 * @throws DataAccessException
	 *             Thrown if the execution fails.
	 * @throws IOException
	 *             Thrown if a file transfer operation fails.
	 */
	private <T extends Entity> void executeStatements(Function<EntityType, BaseStatement> statementFactory,
			Collection<T> entities) throws AoException, IOException {
		Map<EntityType, List<Entity>> entitiesByType = entities.stream()
				.collect(Collectors.groupingBy(context.getODSModelManager()::getEntityType));
		for (Entry<EntityType, List<Entity>> entry : entitiesByType.entrySet()) {
			statementFactory.apply(entry.getKey()).execute(entry.getValue());
		}
	}

	/**
	 * Closes the co-session of this transaction.
	 */
	private void closeSession() {		
		if (catalogManager != null) {
			catalogManager.clear();
		}

		context.close();
		LOGGER.debug("Transaction '{}' closed.", id);
	}

}
