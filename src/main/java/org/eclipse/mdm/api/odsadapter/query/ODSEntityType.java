/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.asam.ods.ApplElem;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;

/**
 * ODS implementation of the {@link EntityType} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class ODSEntityType implements EntityType {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<EntityType, Map<String, Relation>> relationsByEntityName = new HashMap<>();
	private final Map<EntityType, Relation> relationsByEntity = new HashMap<>();

	private final Map<Relationship, List<Relation>> relationsByType = new EnumMap<>(Relationship.class);
	private final List<Relation> relations = new ArrayList<>();

	private final Map<String, Attribute> attributeByName;

	private final String sourceName;
	private final T_LONGLONG odsID;
	private final String baseName;
	private final String name;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param sourceName Name of the data source.
	 * @param applElem The ODS meta data for this entity type.
	 * @param units The unit {@code Map} for unit mapping of attributes.
	 * @param enumClasses The enumeration class {@code Map} for enum mapping
	 * 		of attributes.
	 */
	ODSEntityType(String sourceName, ApplElem applElem, Map<Long, String> units,
			Map<String, Class<? extends Enum<?>>> enumClasses) {
		this.sourceName = sourceName;
		baseName = applElem.beName;
		name = applElem.aeName;
		odsID = applElem.aid;

		attributeByName = Arrays.stream(applElem.attributes)
				.map(a -> new ODSAttribute(this, a, units.get(ODSConverter.fromODSLong(a.unitId)),
						enumClasses.get(a.aaName)))
				.collect(toMap(Attribute::getName, Function.identity()));
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the ODS base name this entity type is derived from.
	 *
	 * @return The base name is returned.
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Attribute> getAttributes() {
		return new ArrayList<>(attributeByName.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute getAttribute(String name) {
		Attribute attribute = attributeByName.get(name);
		if(attribute == null) {
			Optional<Relation> relation = getRelations().stream().filter(r -> r.getName().equals(name)).findAny();
			return relation.map(Relation::getAttribute)
					.orElseThrow(() -> new IllegalArgumentException("Attribute with name '" + name
							+ "' not found at entity type '" + getName() + "'."));
		}
		return attribute;
	}

	/**
	 * Returns the ODS type ID of this entity type.
	 *
	 * @return The ODS type ID is returned.
	 */
	public T_LONGLONG getODSID() {
		return odsID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return ODSConverter.fromODSLong(odsID);
	}

	@Override
	public List<Relation> getRelations() {
		return Collections.unmodifiableList(relations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Relation> getParentRelations() {
		return getRelations(Relationship.FATHER_CHILD).stream()
				.filter(r -> ((ODSRelation) r).isOutgoing(Relationship.FATHER_CHILD))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Relation> getChildRelations() {
		return getRelations(Relationship.FATHER_CHILD).stream()
				.filter(r -> ((ODSRelation) r).isIncoming(Relationship.FATHER_CHILD))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Relation> getInfoRelations() {
		return getRelations(Relationship.INFO).stream()
				.filter(r -> ((ODSRelation) r).isOutgoing(Relationship.INFO))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Relation> getRelations(Relationship relationship) {
		List<Relation> result = relationsByType.get(relationship);
		return result == null ? Collections.emptyList() : Collections.unmodifiableList(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Relation getRelation(EntityType target) {
		Relation relation = relationsByEntity.get(target);
		if(relation == null) {
			// multiple relations to target exist, try to use a default
			Map<String, Relation> relationsByName = relationsByEntityName.get(target);
			if(relationsByName == null) {
				throw new IllegalArgumentException("Relations to '" + target + "' not found!");
			}

			relation = relationsByName.get(target.getName());
		}
		return relation == null ? getParentRelation(target) : relation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Relation getRelation(EntityType target, String name) {
		Map<String, Relation> relationsByName = relationsByEntityName.get(target);
		if(relationsByName == null) {
			throw new IllegalArgumentException("Relations to '" + target + "' not found!");
		}

		Relation relation = relationsByName.get(name);
		if(relation == null) {
			throw new IllegalArgumentException("Relation to '" + target + "' with name '" + name + "' not found!");
		}
		return relation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if(object instanceof ODSEntityType) {
			return getName().equals(((EntityType) object).getName());
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

	// ======================================================================
	// Package methods
	// ======================================================================

	/**
	 * Adds given {@link Relation}s.
	 *
	 * @param relations {@code Relation}s which will be added.
	 */
	void setRelations(List<Relation> relations) {
		Map<EntityType, List<Relation>> entityRelationsByTarget = relations.stream().distinct()
				.filter(r -> equals(r.getSource()))
				.collect(groupingBy(Relation::getTarget));

		for(Entry<EntityType, List<Relation>> entry : entityRelationsByTarget.entrySet()) {
			List<Relation> entityTypeRelations = entry.getValue();
			EntityType target = entry.getKey();

			entityTypeRelations.forEach(this::addRelation);

			if(entityTypeRelations.size() > 1) {
				relationsByEntityName.put(target,
						entityTypeRelations.stream().collect(toMap(Relation::getName, identity())));
			} else {
				relationsByEntity.put(target, entityTypeRelations.get(0));
			}
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Tries to find a parent {@link Relation} to given target {@link
	 * EntityType}.
	 *
	 * @param target The target {@code EntityType}.
	 * @return The requested parent {@code Relation} is returned.
	 * @throws IllegalArgumentException Thrown if no such {@code Relation}
	 * 		exists.
	 */
	private Relation getParentRelation(EntityType target) {
		return getParentRelations().stream().filter(et -> et.getTarget().equals(target)).findAny()
				.orElseThrow(() -> new IllegalArgumentException("Relation to entity type '" + target
						+ "' does not exist."));
	}

	/**
	 * Adds given {@link Relation}.
	 *
	 * @param relation {@code Relation} which will be added.
	 */
	private void addRelation(Relation relation) {
		relationsByType.computeIfAbsent(relation.getRelationship(), k -> new ArrayList<>()).add(relation);
		relations.add(relation);
	}

}
