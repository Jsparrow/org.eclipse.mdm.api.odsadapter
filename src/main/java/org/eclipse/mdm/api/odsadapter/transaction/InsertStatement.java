/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.asam.ods.AIDName;
import org.asam.ods.AIDNameValueSeqUnitId;
import org.asam.ods.AoException;
import org.asam.ods.ElemId;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.adapter.Attribute;
import org.eclipse.mdm.api.base.adapter.Core;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.FileLink;
import org.eclipse.mdm.api.base.model.Sortable;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.odsadapter.query.ODSEntityFactory;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insert statement is used to write new entities and their children.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class InsertStatement extends BaseStatement {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertStatement.class);

	private final Map<Class<? extends Entity>, List<Entity>> childrenMap = new HashMap<>();
	private final List<Core> cores = new ArrayList<>();
	private final Map<String, List<Value>> insertMap = new HashMap<>();

	private final List<FileLink> fileLinkToUpload = new ArrayList<>();

	private final Map<String, SortIndexTestSteps> sortIndexTestSteps = new HashMap<>();
	private boolean loadSortIndex;

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
	 */
	InsertStatement(ODSTransaction transaction, EntityType entityType) {
		super(transaction, entityType);

		loadSortIndex = getModelManager().getEntityType(TestStep.class).equals(getEntityType());
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Collection<Entity> entities) throws AoException, DataAccessException, IOException {
		entities.stream().map(ODSEntityFactory::extract).forEach(this::readEntityCore);
		execute();
	}

	/**
	 * Executes this statement for given {@link Core}s.
	 *
	 * @param cores
	 *            The processed {@code Core}s.
	 * @throws AoException
	 *             Thrown if the execution fails.
	 * @throws DataAccessException
	 *             Thrown if the execution fails.
	 * @throws IOException
	 *             Thrown if a file transfer operation fails.
	 */
	public void executeWithCores(Collection<Core> cores) throws AoException, DataAccessException, IOException {
		cores.forEach(this::readEntityCore);
		execute();
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Uploads new {@link FileLink}s, adjusts sort indices for new
	 * {@link TestStep} entities and writes collected insertion data at once.
	 * Once new entities are written their children are created by delegation to
	 * the {@link ODSTransaction}.
	 *
	 * @throws AoException
	 *             Thrown if the execution fails.
	 * @throws DataAccessException
	 *             Thrown if the execution fails.
	 * @throws IOException
	 *             Thrown if a file transfer operation fails.
	 */
	private void execute() throws AoException, DataAccessException, IOException {
		List<AIDNameValueSeqUnitId> anvsuList = new ArrayList<>();
		T_LONGLONG aID = getEntityType().getODSID();

		// TODO tracing progress in this method...

		if (loadSortIndex && !sortIndexTestSteps.isEmpty()) {
			adjustMissingSortIndices();
		}

		if (!fileLinkToUpload.isEmpty()) {
			getTransaction().getUploadService().uploadParallel(fileLinkToUpload, null);
		}

		for (Entry<String, List<Value>> entry : insertMap.entrySet()) {
			Attribute attribute = getEntityType().getAttribute(entry.getKey());

			AIDNameValueSeqUnitId anvsu = new AIDNameValueSeqUnitId();
			anvsu.attr = new AIDName(aID, entry.getKey());
			anvsu.unitId = ODSConverter.toODSLong(0);
			anvsu.values = ODSConverter.toODSValueSeq(attribute, entry.getValue());
			anvsuList.add(anvsu);
		}

		long start = System.currentTimeMillis();
		ElemId[] elemIds = getApplElemAccess()
				.insertInstances(anvsuList.toArray(new AIDNameValueSeqUnitId[anvsuList.size()]));
		for (int i = 0; i < elemIds.length; i++) {
			cores.get(i).setID(Long.toString(ODSConverter.fromODSLong(elemIds[i].iid)));
		}
		long stop = System.currentTimeMillis();

		LOGGER.debug("{} " + getEntityType() + " instances created in {} ms.", elemIds.length, stop - start);

		for (List<Entity> children : childrenMap.values()) {
			getTransaction().create(children);
		}
	}

	/**
	 * Reads given {@link Core} and prepares its data to be written:
	 *
	 * <ul>
	 * <li>collect new {@link FileLink}s</li>
	 * <li>trace missing sort indices of TestSteps</li>
	 * <li>collect property {@link Value}s</li>
	 * <li>collect foreign key {@code Value}s</li>
	 * <li>collect child entities for recursive creation</li>
	 * </ul>
	 *
	 * @param core
	 *            The {@code Core}.
	 */
	private void readEntityCore(Core core) {
		if (!core.getTypeName().equals(getEntityType().getName())) {
			throw new IllegalArgumentException("Entity core '" + core.getTypeName()
					+ "' is incompatible with current insert statement for entity type '" + getEntityType() + "'.");
		}

		cores.add(core);

		if (loadSortIndex) {
			if ((Integer) core.getValues().get(Sortable.ATTR_SORT_INDEX).extract() < 0) {
				sortIndexTestSteps.computeIfAbsent(core.getPermanentStore().get(Test.class).getID(),
						k -> new SortIndexTestSteps()).testStepCores.add(core);
			}
		}

		// add all entity values
		for (Value value : core.getAllValues().values()) {
			insertMap.computeIfAbsent(value.getName(), k -> new ArrayList<>()).add(value);
		}

		// collect file links
		fileLinkToUpload.addAll(core.getAddedFileLinks());

		// define "empty" values for informative relations
		for (Relation relation : getEntityType().getInfoRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// define "empty" values for parent relations
		for (Relation relation : getEntityType().getParentRelations()) {
			insertMap.computeIfAbsent(relation.getName(), k -> new ArrayList<>()).add(relation.createValue());
		}

		// replace "empty" relation values with corresponding instance IDs
		setRelationIDs(core.getMutableStore().getCurrent());
		setRelationIDs(core.getPermanentStore().getCurrent());

		for (Entry<Class<? extends Deletable>, List<? extends Deletable>> entry : core.getChildrenStore().getCurrent()
				.entrySet()) {
			childrenMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
		}

		getTransaction().addModified(core);
		getTransaction().addCreated(core);
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
			List<Value> relationValues = insertMap.get(relation.getName());
			if (relationValues == null) {
				throw new IllegalStateException("Relation '" + relation + "' is incompatible with insert statement "
						+ "for entity type '" + getEntityType() + "'");
			}
			relationValues.get(relationValues.size() - 1).set(relatedEntity.getID());
		}
	}

	/**
	 * Adjusts missing sort indices for {@link TestStep}s by querying last used
	 * max sort index.
	 *
	 * @throws DataAccessException
	 *             Thrown if unable to query used sort indices.
	 */
	private void adjustMissingSortIndices() throws DataAccessException {
		EntityType testStep = getEntityType();
		EntityType test = getModelManager().getEntityType(Test.class);
		Relation parentRelation = testStep.getRelation(test);

		Query query = getQueryService().createQuery().select(parentRelation.getAttribute())
				.select(testStep.getAttribute(Sortable.ATTR_SORT_INDEX), Aggregation.MAXIMUM)
				.group(parentRelation.getAttribute());

		Filter filter = Filter.idsOnly(parentRelation, sortIndexTestSteps.keySet());
		for (Result result : query.fetch(filter)) {
			Record record = result.getRecord(testStep);
			int sortIndex = (Integer) record.getValues().get(Sortable.ATTR_SORT_INDEX).extract();
			sortIndexTestSteps.remove(record.getID(parentRelation).get()).setIndices(sortIndex + 1);
		}

		// start at 1 for all remaining
		sortIndexTestSteps.values().forEach(tss -> tss.setIndices(0));
	}

	// ======================================================================
	// Inner classes
	// ======================================================================

	/**
	 * Utility class to write missing sort index of new {@link TestStep}s.
	 */
	private static final class SortIndexTestSteps {

		// ======================================================================
		// Instance variables
		// ======================================================================

		private List<Core> testStepCores = new ArrayList<>();

		// ======================================================================
		// Private methods
		// ======================================================================

		/**
		 * Assigns sort indices to {@link Core}s starting at given index.
		 *
		 * @param startIndex
		 *            The start index.
		 */
		private void setIndices(int startIndex) {
			int index = startIndex;
			for (Core core : testStepCores) {
				core.getValues().get(Sortable.ATTR_SORT_INDEX).set(index++);
			}
		}

	}

}
