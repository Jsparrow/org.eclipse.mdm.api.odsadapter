/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.FilesAttachable;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityType;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delete statement is used to delete entities with their children.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class DeleteStatement extends BaseStatement {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final List<String> AUTO_DELETABLE = Arrays.asList("MeaQuantity", "SubMatrix", "LocalColumn",
			"ExternalComponent");
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteStatement.class);

	private final boolean useAutoDelete;

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
	 * @param useAutoDelete
	 *            If {@code true} child relations of {@link Measurement}
	 *            entities are not followed.
	 */
	DeleteStatement(ODSTransaction transaction, EntityType entityType, boolean useAutoDelete) {
		super(transaction, entityType);
		this.useAutoDelete = useAutoDelete;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Collection<Entity> entities) throws AoException, DataAccessException {
		if (entities.stream().filter(e -> !e.getTypeName().equals(getEntityType().getName())).findAny().isPresent()) {
			throw new IllegalArgumentException("At least one given entity is of incompatible type.");
		}

		long start = System.currentTimeMillis();
		int amount = delete(getEntityType(), entities.stream().map(Entity::getID).collect(Collectors.toSet()), false);
		LOGGER.debug("{} instances deleted in {} ms.", amount, System.currentTimeMillis() - start);
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Recursively follows child relations of given entities and deletes all
	 * child entities before deleting parent entities.
	 *
	 * @param entityType
	 *            {@link EntityType} of the deleted entities.
	 * @param instanceIDs
	 *            Instance IDs of entities which have to be deleted.
	 * @param ignoreSiblings
	 *            Is it required to check whether {@link Measurement} siblings
	 *            share a common {@link ContextRoot}s.
	 * @return Returns the total number of deleted instances.
	 * @throws AoException
	 *             Thrown if unable to delete entities.
	 * @throws DataAccessException
	 *             Thrown if unable to query child entities.
	 */
	private int delete(EntityType entityType, Collection<String> instanceIDs, boolean ignoreSiblings)
			throws AoException, DataAccessException {
		if (instanceIDs.isEmpty()) {
			return 0;
		}

		Query query = getQueryService().createQuery().selectID(entityType);
		for (Relation relation : entityType.getChildRelations()) {
			if (useAutoDelete && AUTO_DELETABLE.contains(relation.getTarget().getName())) {
				continue;
			}

			if (!relation.getTarget().equals(relation.getSource())) {
				query.join(relation, JoinType.OUTER).selectID(relation.getTarget());
			}
		}

		// select attributes containing file links only for entity types
		// implementing FilesAttachable
		EntityConfig<?> entityConfig = getModelManager().getEntityConfig(entityType);
		if (FilesAttachable.class.isAssignableFrom(entityConfig.getEntityClass())) {
			entityType.getAttributes().stream().filter(a -> a.getValueType().isFileLinkType()).forEach(query::select);
		}

		EntityType testStep = getModelManager().getEntityType(TestStep.class);
		EntityType measurement = getModelManager().getEntityType(Measurement.class);
		EntityType channel = getModelManager().getEntityType(Channel.class);

		EntityType unitUnderTest = getModelManager().getEntityType(ContextRoot.class, ContextType.UNITUNDERTEST);
		EntityType testSequence = getModelManager().getEntityType(ContextRoot.class, ContextType.TESTSEQUENCE);
		EntityType testEquipment = getModelManager().getEntityType(ContextRoot.class, ContextType.TESTEQUIPMENT);

		// type in this list must be deleted AFTER this this instances have been
		// deleted
		// informative relation is considered as a child relation
		List<EntityType> delayedDelete = new ArrayList<>();

		// join context roots
		if (measurement.equals(entityType) || testStep.equals(entityType)) {
			query.join(entityType.getRelation(unitUnderTest), JoinType.OUTER).selectID(unitUnderTest);
			query.join(entityType.getRelation(testSequence), JoinType.OUTER).selectID(testSequence);
			query.join(entityType.getRelation(testEquipment), JoinType.OUTER).selectID(testEquipment);
			delayedDelete.addAll(Arrays.asList(unitUnderTest, testSequence, testEquipment));
		}

		// join parameter sets
		if (measurement.equals(entityType) || channel.equals(entityType)) {
			EntityType parameterSet = getModelManager().getEntityType(ParameterSet.class);
			query.join(entityType.getRelation(parameterSet), JoinType.OUTER).selectID(parameterSet);
		}

		Filter filter = Filter.or().ids(entityType, instanceIDs);
		entityType.getParentRelations().stream().filter(r -> r.getTarget().equals(entityType))
				.forEach(relation -> filter.ids(relation, instanceIDs));

		// query child IDs
		Map<EntityType, Set<String>> children = new HashMap<>();
		for (Result result : query.fetch(filter)) {
			// load children of other types
			result.stream().filter(r -> r.getID() != null && r.getID().length() > 0).forEach(r -> {
				children.computeIfAbsent(r.getEntityType(), k -> new HashSet<>()).add(r.getID());
			});

			// collect file links to remove
			List<FileLink> fileLinks = new ArrayList<>();
			for (Value value : result.getRecord(entityType).getValues().values()) {
				if (value.getValueType().isFileLink()) {
					fileLinks.add(value.extract());
				} else if (value.getValueType().isFileLinkSequence()) {
					fileLinks.addAll(Arrays.asList((FileLink[]) value.extract()));
				}
			}

			if (!fileLinks.isEmpty()) {
				getTransaction().getUploadService().addToRemove(fileLinks);
			}
		}

		// omit context roots with references to not removed measurements
		if (!ignoreSiblings && measurement.equals(entityType)) {
			for (EntityType contextRoot : Arrays.asList(unitUnderTest, testSequence, testEquipment)) {
				Set<String> contextRootIDs = children.getOrDefault(contextRoot, Collections.emptySet());
				if (contextRootIDs.isEmpty()) {
					continue;
				}

				Query contextQuery = getQueryService().createQuery();
				contextQuery.selectID(contextRoot, measurement);
				contextQuery.join(contextRoot, measurement);

				for (Result result : contextQuery.fetch(Filter.idsOnly(contextRoot, contextRootIDs))) {
					if (instanceIDs.contains(result.getRecord(measurement).getID())) {
						continue;
					}

					// context root references a not removed measurement
					contextRootIDs.remove(result.getRecord(contextRoot).getID());
				}
			}
		}

		int amount = 0;
		// delete real children
		List<Entry<EntityType, Set<String>>> consideredChildren = new ArrayList<>();
		for (Entry<EntityType, Set<String>> entry : children.entrySet()) {
			EntityType childType = entry.getKey();
			Set<String> childInstanceIDs = entry.getValue();
			if (entityType.equals(childType)) {
				childInstanceIDs.removeAll(instanceIDs);
			} else if (delayedDelete.contains(entry.getKey())) {
				consideredChildren.add(entry);
				continue;
			}
			amount += delete(entry.getKey(), childInstanceIDs, true);
		}

		getApplElemAccess().deleteInstances(((ODSEntityType) entityType).getODSID(), toODSIDs(instanceIDs));

		// delete considered children (informative relation)
		for (Entry<EntityType, Set<String>> entry : consideredChildren) {
			amount += delete(entry.getKey(), entry.getValue(), true);
		}

		return amount + instanceIDs.size();
	}

	/**
	 * Converts given {@code Collection} of instance IDs to ODS a
	 * {@link T_LONGLONG} array.
	 *
	 * @param instanceIDs
	 *            The instance IDs.
	 * @return The corresponding ODS {@code T_LONGLONG[]} is returned.
	 */
	private T_LONGLONG[] toODSIDs(Collection<String> instanceIDs) {
		return instanceIDs.stream().map(ODSConverter::toODSID).toArray(T_LONGLONG[]::new);
	}

}
