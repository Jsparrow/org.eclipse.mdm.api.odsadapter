/*
 * Copyright (c) 2016-2018 Gigatronik Ingolstadt GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.search.SearchQuery;

/**
 * This class spans a dependency tree for conditional join statements is
 * {@link SearchQuery}.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class JoinTree {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<String, List<String>> tree = new HashMap<>();
	private final Map<String, JoinNode> joinNodes = new HashMap<>();
	private final Set<String> nodeNames = new HashSet<>();

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the tree configuration. It mapps a source entity type names to
	 * supported target entity type names.
	 *
	 * @return The returned {@code Map} is unmodifiable.
	 */
	public Map<String, List<String>> getTree() {
		return Collections.unmodifiableMap(tree);
	}

	/**
	 * Returns a {@code List} with distinct entity type names covered by this
	 * join tree.
	 *
	 * @return Returned {@code List} is unmodifiable.
	 */
	public Set<String> getNodeNames() {
		return Collections.unmodifiableSet(nodeNames);
	}

	/**
	 * Returns the {@link JoinNode} for given target entity type name.
	 *
	 * @param target
	 *            The target entity type name.
	 * @return The {@code JoinNode} for given target entity type name is
	 *         returned.
	 * @throws IllegalArgumentException
	 *             Thrown if no such {@code JoinNode} exists.
	 */
	public JoinNode getJoinNode(String target) {
		JoinNode joinNode = joinNodes.get(target);
		if (joinNode == null) {
			throw new IllegalArgumentException("Relation to '" + target + "' not possible.");
		}

		return joinNode;
	}

	/**
	 * Adds given dependency setup to this join tree.
	 *
	 * @param source
	 *            The source entity type name.
	 * @param target
	 *            The target entity type name.
	 * @param viaParent
	 *            If true, then source is the considered parent of the target.
	 * @param joinType
	 *            Either inner or outer joinType.
	 * @throws IllegalArgumentException
	 *             Thrown if given setup overrides an existing one (a target
	 *             entity type is allowed to be joined only once).
	 */
	public void addNode(EntityType source, EntityType target, boolean viaParent, JoinType joinType) {
		String sourceName = source.getName();
		String targetName = target.getName();

		if (joinNodes.put(targetName, new JoinNode(sourceName, targetName, joinType)) != null) {
			throw new IllegalArgumentException("It is not allowed to override join nodes.");
		}

		if (viaParent) {
			tree.computeIfAbsent(sourceName, k -> new ArrayList<>()).add(targetName);
		} else {
			tree.computeIfAbsent(targetName, k -> new ArrayList<>()).add(sourceName);
		}

		nodeNames.add(sourceName);
		nodeNames.add(targetName);
	}

	// ======================================================================
	// Inner classes
	// ======================================================================

	/**
	 * A simple joinType node setup.
	 */
	static final class JoinNode {

		// ======================================================================
		// Instance variables
		// ======================================================================

		final String source;
		final String target;
		final JoinType joinType;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param source
		 *            The source entity type name.
		 * @param target
		 *            The target entity type name.
		 * @param joinType
		 *            Either inner or outer {@link JoinType}.
		 */
		private JoinNode(String source, String target, JoinType joinType) {
			this.source = source;
			this.target = target;
			this.joinType = joinType;
		}

	}

	/**
	 * A simple joinType configuration setup.
	 */
	static final class JoinConfig {

		// ======================================================================
		// Instance variables
		// ======================================================================

		final Class<? extends Entity> source;
		final Class<? extends Entity> target;
		final boolean viaParent;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param source
		 *            The source entity type name.
		 * @param target
		 *            The target entity type name.
		 * @param viaParent
		 *            If true, then source is the considered parent of the
		 *            target.
		 */
		private JoinConfig(Class<? extends Entity> source, Class<? extends Entity> target, boolean viaParent) {
			this.source = source;
			this.target = target;
			this.viaParent = viaParent;
		}

		// ======================================================================
		// Package methods
		// ======================================================================

		/**
		 * Creates a new {@link JoinConfig} where given source is considered as
		 * the child of given target.
		 *
		 * @param source
		 *            The source entity type name.
		 * @param target
		 *            The target entity type name.
		 * @return The created {@code JoinConfig} is returned.
		 */
		static JoinConfig up(Class<? extends Entity> source, Class<? extends Entity> target) {
			return new JoinConfig(source, target, false);
		}

		/**
		 * Creates a new {@link JoinConfig} where given source is considered as
		 * the parent of given target.
		 *
		 * @param source
		 *            The source entity type name.
		 * @param target
		 *            The target entity type name.
		 * @return The created {@code JoinConfig} is returned.
		 */
		static JoinConfig down(Class<? extends Entity> source, Class<? extends Entity> target) {
			return new JoinConfig(source, target, true);
		}

	}

}
