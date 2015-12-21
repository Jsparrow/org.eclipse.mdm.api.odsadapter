/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.eclipse.mdm.api.base.query.Entity;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.base.query.Relationship;

public final class ODSEntity implements Entity {

	private final Map<Entity, Map<String, Relation>> relationsByEntityName = new HashMap<>();
	private final Map<Entity, Relation> relationsByEntity = new HashMap<>();

	private final Map<Relationship, List<Relation>> relationsByType = new HashMap<>();
	private final List<Relation> relations = new ArrayList<>();

	private final Map<String, Attribute> attributeByName;
	private final ApplElem applElem;

	public ODSEntity(ApplElem applElem) throws AoException {
		this.applElem = applElem;

		/**
		 * TODO ODS server produces wrong result rows each time
		 * an attribute whose type is a sequence type is selected.
		 */
		//		attributeByName = Arrays.stream(applElem.attributes).map(a -> new ODSAttribute(a, this))
		//				.filter(a -> !a.getType().isSequence()).collect(toMap(Attribute::getName, Function.identity()));

		attributeByName = Arrays.stream(applElem.attributes).map(a -> new ODSAttribute(a, this))
				.collect(toMap(Attribute::getName, Function.identity()));
	}

	@Override
	public String getName() {
		return applElem.aeName;
	}

	@Override
	public List<Attribute> getAttributes() {
		return new ArrayList<>(attributeByName.values());
	}

	@Override
	public Attribute getAttribute(String name) {
		Attribute attribute = attributeByName.get(name);
		if(attribute == null) {
			throw new IllegalArgumentException("attribute with name '" + name
					+ "' not found at entity '" + getName() + "'");
		}
		return attribute;
	}

	public T_LONGLONG getODSID() {
		return applElem.aid;
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
		List<Relation> relations = relationsByType.get(relationship);
		return relations == null ? Collections.emptyList() : Collections.unmodifiableList(relations);
	}

	@Override
	public Relation getRelation(Entity target) {
		Relation relation = relationsByEntity.get(target);
		if(relation == null) {
			throw new IllegalArgumentException("Relation from '" + this + "' to '" + target + "' not found!");
		}

		return relation;
	}

	@Override
	public Relation getRelation(Entity target, String name) {
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
				.collect(groupingBy(Relation::getTarget, toList())));
	}

	private void mapRelations(Map<Entity, List<Relation>> entityRelationsByTarget) {
		for(Entry<Entity, List<Relation>> entry : entityRelationsByTarget.entrySet()) {
			List<Relation> relations = entry.getValue();
			Entity target = entry.getKey();

			relations.stream().forEach(this::addRelation);

			if(relations.size() > 1) {
				relationsByEntityName.put(target, relations.stream().collect(toMap(Relation::getName, identity())));
			} else {
				relationsByEntity.put(target, relations.get(0));
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
