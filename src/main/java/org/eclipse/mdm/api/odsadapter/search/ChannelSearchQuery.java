/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.SearchQuery;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;

/**
 * {@link SearchQuery} implementation for {@link Channel} as source entity
 * type.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class ChannelSearchQuery extends BaseEntitySearchQuery {

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param modelManager Used to load {@link EntityType}s.
	 * @param contextState The {@link ContextState}.
	 */
	ChannelSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Channel.class, Project.class);

		// layers
		addJoinConfig(JoinConfig.up(Pool.class, Project.class));
		addJoinConfig(JoinConfig.up(Test.class, Pool.class));
		addJoinConfig(JoinConfig.up(TestStep.class, Test.class));
		addJoinConfig(JoinConfig.up(Measurement.class, TestStep.class));
		addJoinConfig(JoinConfig.up(Channel.class, Measurement.class));

		// context
		addJoinConfig(contextState);

		// TODO join to sensor tables.... || this will break the joins to context data
		// multiple outer join to the same table...
	}

}
