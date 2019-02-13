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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.adapter.EntityStore;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.FilesAttachable;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insert statement is used to update entities and their children.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class UpdateStatement extends BaseStatement {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStatement.class);

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<Class<? extends Entity>, List<Entity>> childrenToCreate = new HashMap<>();
	private final Map<Class<? extends Entity>, List<Entity>> childrenToUpdate = new HashMap<>();
	private final Map<Class<? extends Deletable>, List<Deletable>> childrenToRemove = new HashMap<>();
	private Map<String, List<Value>> updateMap = new HashMap<>();

	private final List<FileLink> fileLinkToUpload = new ArrayList<>();
	private final List<String> nonUpdatableRelationNames;
	private final boolean ignoreChildren;
	private final boolean isFilesAttachable;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param transaction
	 *            The owning {@link ODSTransaction}.
	 * @param entityType
	 *            The associated {@link EntityType}.
	 * @param ignoreChildren
	 *            If {@code true}, then child entities won't be processed.
	 */
	UpdateStatement(ODSTransaction transaction, EntityType entityType, boolean ignoreChildren) {
		super(transaction, entityType);

		nonUpdatableRelationNames = entityType.getInfoRelations().stream().map(Relation::getName)
				.collect(Collectors.toList());
		this.ignoreChildren = ignoreChildren;

		EntityConfig<?> entityConfig = getModelManager().getEntityConfig(getEntityType());
		isFilesAttachable = FilesAttachable.class.isAssignableFrom(entityConfig.getEntityClass());
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Collection<Entity> entities) throws AoException, IOException {
		entities.forEach(entity -> readEntityCore(ODSEntityFactory.extract(entity)));

		// TODO tracing progress in this method...

		List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
		T_LONGLONG aID = getEntityType().getODSID();

		if (!fileLinkToUpload.isEmpty()) {
			getTransaction().getUploadService().uploadParallel(fileLinkToUpload, null);
		}

		for (Entry<String, List<Value>> entry : updateMap.entrySet()) {
			if (nonUpdatableRelationNames.contains(entry.getKey())) {
				// skip "empty" informative relation sequence
				continue;
			}

			Attribute attribute = getEntityType().getAttribute(entry.getKey());

			AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
			anvsu.attr = new AIDName(aID, entry.getKey());
			anvsu.unitId = ODSConverter.toODSLong(0);
			anvsu.values = ODSConverter.toODSValueSeq(attribute, entry.getValue());
			anvsuList.add(anvsu);
		}

		long start = System.currentTimeMillis();
		getApplElemAccess().updateInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		long stop = System.currentTimeMillis();

		LOGGER.debug(new StringBuilder().append("{} ").append(getEntityType()).append(" instances updated in {} ms.").toString(), entities.size(), stop - start);

		// delete first to make sure naming collisions do not occur!
		childrenToRemove.values().forEach(children -> getTransaction().delete(children));
		childrenToCreate.values().forEach(children -> getTransaction().create(children));
		childrenToUpdate.values().forEach(children -> getTransaction().update(children));
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Reads given {@link Core} and prepares its data to be written:
	 *
	 * <ul>
	 * <li>collect new and removed {@link FileLink}s</li>
	 * <li>collect property {@link Value}s</li>
	 * <li>collect foreign key {@code Value}s</li>
	 * <li>collect child entities for recursive create/update/delete</li>
	 * </ul>
	 *
	 * @param core
	 *            The {@code Core}.
	 * @throws DataAccessException
	 *             Thrown in case of errors.
	 */
	private void readEntityCore(Core core) {
		if (!core.getTypeName().equals(getEntityType().getName())) {
			throw new IllegalArgumentException(new StringBuilder().append("Entity core '").append(core.getTypeName()).append("' is incompatible with current update statement for entity type '").append(getEntityType()).append("'.").toString());
		}

		// add all entity values
		core.getAllValues().values().forEach(value -> updateMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value));

		// collect file links
		fileLinkToUpload.addAll(core.getAddedFileLinks());
		List<FileLink> fileLinksToRemove = core.getRemovedFileLinks();
		if (isFilesAttachable && !fileLinksToRemove.isEmpty()) {
			getTransaction().getUploadService().addToRemove(fileLinksToRemove);
		}

		updateMap.computeIfAbsent(getEntityType().getIDAttribute().getName(), k -> new ArrayList<>())
				.add(getEntityType().getIDAttribute().createValue(core.getID()));

		// define "empty" values for ALL informative relations
		getEntityType().getInfoRelations().forEach(relation -> updateMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue()));

		// preserve "empty" relation values for removed related entities
		EntityStore mutableStore = core.getMutableStore();
		mutableStore.getRemoved().stream().map(e -> getModelManager().getEntityType(e))
				.map(getEntityType()::getRelation).map(Relation::getName).forEach(nonUpdatableRelationNames::remove);

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(mutableStore.getCurrent());

		collectChildEntities(core);

		getTransaction().addModified(core);
	}

	/**
	 * Collects child entities for recursive processing.
	 *
	 * @param core
	 *            The {@link Core}.
	 */
	private void collectChildEntities(Core core) {
		if (ignoreChildren) {
			return;
		}

		for (Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : core.getChildrenStore().getCurrent()
				.entrySet()) {
			Map<Boolean, List<Entity>> partition = entry.getValue()
					.stream()
					.collect(Collectors.partitioningBy(e -> ODSUtils.isValidID(e.getID())));
			List<Entity> virtualEntities = partition.get(Boolean.FALSE);
			if (virtualEntities != null && !virtualEntities.isEmpty()) {
				childrenToCreate.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(virtualEntities);
			}
			List<Entity> existingEntities = partition.get(Boolean.TRUE);
			if (existingEntities != null && !existingEntities.isEmpty()) {
				childrenToUpdate.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(existingEntities);
			}
		}

		for (Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : core.getChildrenStore().getRemoved()
				.entrySet()) {
			List<Deletable> toDelete = entry.getValue().stream().filter(e -> ODSUtils.isValidID(e.getID()))
					.collect(Collectors.toList());
			childrenToRemove.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(toDelete);
		}
	}

	/**
	 * Overwrites empty foreign key {@link Value} containers.
	 *
	 * @param relatedEntities
	 *            The related {@link Entity}s.
	 */
	private void setRelationIDs(Collection<Entity> relatedEntities) {
		for (Entity relatedEntity : relatedEntities) {
			if (!ODSUtils.isValidID(relatedEntity.getID())) {
				throw new IllegalArgumentException("Related entity must be a persited entity.");
			}

			Relation relation = getEntityType().getRelation(getModelManager().getEntityType(relatedEntity));
			List<Value> relationValues = updateMap.get(relation.getName());
			if (relationValues == null) {
				throw new IllegalStateException(new StringBuilder().append("Relation '").append(relation).append("' is incompatible with update statement for entity type '").append(getEntityType()).append("'").toString());
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getID());
			nonUpdatableRelationNames.remove(relation.getName());
		}
	}

}
