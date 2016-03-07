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

import org.asam.ods.AoException;
import org.asam.ods.ApplElem;
import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;

public final class ODSEntityType implements EntityType {

	private final Map<EntityType, Map<String, Relation>> relationsByEntityName = new HashMap<>();
	private final Map<EntityType, Relation> relationsByEntity = new HashMap<>();

	private final Map<Relationship, List<Relation>> relationsByType = new EnumMap<>(Relationship.class);
	private final List<Relation> relations = new ArrayList<>();

	private final Map<String, Attribute> attributeByName;

	private final String sourceName;
	private final T_LONGLONG odsID;
	private final String name;

	public ODSEntityType(String sourceName, ApplElem applElem, Map<String, Class<? extends Enum<?>>> enumClasses)
			throws AoException {
		this.sourceName = sourceName;
		name = applElem.aeName;
		odsID = applElem.aid;

		attributeByName = Arrays.stream(applElem.attributes)
				.map(a -> new ODSAttribute(this, a, enumClasses.get(a.aaName)))
				.collect(toMap(Attribute::getName, Function.identity()));
	}

	@Override
	public String getSourceName() {
		return sourceName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Attribute> getAttributes() {
		return new ArrayList<>(attributeByName.values());
	}

	@Override
	public Attribute getAttribute(String name) {
		Attribute attribute = attributeByName.get(name);
		if(attribute == null) {
			throw new IllegalArgumentException("Attribute with name '" + name
					+ "' not found at entity type '" + getName() + "'");
		}
		return attribute;
	}

	public T_LONGLONG getODSID() {
		return odsID;
	}

	@Override
	public List<Relation> getRelations() {
		return Collections.unmodifiableList(relations);
	}

	@Override
	public Optional<Relation> getParentRelation() {
		return getRelations(Relationship.FATHER_CHILD).stream().filter(r -> ((ODSRelation) r).isIncomming(Relationship.FATHER_CHILD)).findAny();
	}

	@Override
	public List<Relation> getChildRelations() {
		return getRelations(Relationship.FATHER_CHILD).stream().filter(r -> ((ODSRelation) r).isOutgoing(Relationship.FATHER_CHILD))
				.collect(Collectors.toList());
	}

	@Override
	public List<Relation> getInfoRelations() {
		return getRelations(Relationship.INFO).stream().filter(r -> ((ODSRelation) r).isIncomming(Relationship.INFO))
				.collect(Collectors.toList());
	}

	@Override
	public List<Relation> getRelations(Relationship relationship) {
		List<Relation> result = relationsByType.get(relationship);
		return result == null ? Collections.emptyList() : Collections.unmodifiableList(result);
	}

	@Override
	public Relation getRelation(EntityType target) {
		Relation relation = relationsByEntity.get(target);
		if(relation == null) {
			throw new IllegalArgumentException("Relation from '" + this + "' to '" + target + "' not found!");
		}

		return relation;
	}

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

	@Override
	public String toString() {
		return getName();
	}

	void setRelations(List<Relation> relations) {
		mapRelations(relations.stream().distinct().filter(this::isCompatible)
				.collect(groupingBy(Relation::getTarget)));
	}

	private void mapRelations(Map<EntityType, List<Relation>> entityRelationsByTarget) {
		for(Entry<EntityType, List<Relation>> entry : entityRelationsByTarget.entrySet()) {
			List<Relation> entityTypeRelations = entry.getValue();
			EntityType target = entry.getKey();

			entityTypeRelations.forEach(this::addRelation);

			if(entityTypeRelations.size() > 1) {
				relationsByEntityName.put(target, entityTypeRelations.stream().collect(toMap(Relation::getName, identity())));
			} else {
				relationsByEntity.put(target, entityTypeRelations.get(0));
			}
		}
	}

	private void addRelation(Relation relation) {
		relationsByType.computeIfAbsent(relation.getRelationship(), k -> new ArrayList<>()).add(relation);
		relations.add(relation);
	}

	private boolean isCompatible(Relation relation) {
		return equals(relation.getSource());
	}

}
