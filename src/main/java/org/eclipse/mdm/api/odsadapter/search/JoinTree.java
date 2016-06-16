package org.eclipse.mdm.api.odsadapter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Join;

final class JoinTree {

	private final Map<String, List<String>> tree = new HashMap<>();
	private final Map<String, JoinNode> joinNodes = new HashMap<>();
	private final Set<String> nodeNames = new HashSet<>();

	public void addNode(EntityType source, EntityType target, boolean viaParent, Join join) {
		addNode(source.getName(), target.getName(), viaParent, join);
	}

	public Map<String, List<String>> getTree() {
		return Collections.unmodifiableMap(tree);
	}

	public Set<String> getNodeNames() {
		return Collections.unmodifiableSet(nodeNames);
	}

	public JoinNode getJoinNode(String target) {
		JoinNode joinNode = joinNodes.get(target);
		if(joinNode == null) {
			throw new IllegalArgumentException("Relation to '" + target + "' not possible.");
		}

		return joinNode;
	}

	private void addNode(String source, String target, boolean viaParent, Join join) {
		if(joinNodes.put(target, new JoinNode(source, target, join)) != null) {
			throw new IllegalArgumentException("It is not allowed to override join nodes.");
		}

		if(viaParent) {
			tree.computeIfAbsent(source, k -> new ArrayList<>()).add(target);
		} else {
			tree.computeIfAbsent(target, k -> new ArrayList<>()).add(source);
		}

		nodeNames.add(source);
		nodeNames.add(target);
	}

	static final class JoinNode {

		final String source;
		final String target;
		final Join join;

		private JoinNode(String source, String target, Join join) {
			this.source = source;
			this.target = target;
			this.join = join;
		}

	}

	static final class JoinConfig {

		final Class<? extends Entity> source;
		final Class<? extends Entity> target;
		final boolean viaParent;

		private JoinConfig(Class<? extends Entity> source, Class<? extends Entity> target, boolean viaParent) {
			this.source = source;
			this.target = target;
			this.viaParent = viaParent;
		}

		static JoinConfig up(Class<? extends Entity> source, Class<? extends Entity> target) {
			return new JoinConfig(source, target, false);
		}

		static JoinConfig down(Class<? extends Entity> source, Class<? extends Entity> target) {
			return new JoinConfig(source, target, true);
		}

	}

}
