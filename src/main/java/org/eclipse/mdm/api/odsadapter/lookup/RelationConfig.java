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


package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.adapter.Relation;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

/**
 * Utility class to collect {@link EntityRecord} referencing entities of the
 * same type specified by an instance of this class.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class RelationConfig {

	// ======================================================================
	// Instance variables
	// ======================================================================

	final Map<String, List<EntityRecord<?>>> dependants = new HashMap<>();
	final EntityConfig<?> entityConfig;
	final Relation relation;
	final boolean mandatory;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param source
	 *            The source {@link EntityType}.
	 * @param entityConfig
	 *            The target {@link EntityConfig}.
	 * @param mandatory
	 *            Flag indicates whether a related must exist or not.
	 */
	public RelationConfig(EntityType source, EntityConfig<?> entityConfig, boolean mandatory) {
		this.entityConfig = entityConfig;
		relation = source.getRelation(entityConfig.getEntityType());
		this.mandatory = mandatory;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Given {@link EntityRecord} depends on {@link Entity}s which are loaded by
	 * processing this relation config. Therefore given {@code EntityRecord}
	 * cached and satisfied as soon as the corresponding entities are loaded.
	 *
	 * @param entityRecord
	 *            The dependant {@code EntityRecord}.
	 * @param record
	 *            The {@link Record} associated with given {@link EntityRecord}.
	 */
	public void add(EntityRecord<?> entityRecord, Record record) {
		Optional<String> relatedEntityID = record.getID(relation);
		if (relatedEntityID.isPresent()) {
			dependants.computeIfAbsent(relatedEntityID.get(), k -> new ArrayList<>()).add(entityRecord);
		} else if (mandatory) {
			throw new IllegalStateException("Mandatory relation unsatisfied for relation "+relation.getName());
		}
	}

}
