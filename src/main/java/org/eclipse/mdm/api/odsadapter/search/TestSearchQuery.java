/*
 * Copyright (c) 2016 Peak Solution GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
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
 * {@link SearchQuery} implementation for {@link Test} as source entity type.
 *
 * @since 1.0.0
 * @author jst, Peak Solution GmbH
 */
final class TestSearchQuery extends BaseEntitySearchQuery {

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to load {@link EntityType}s.
	 * @param contextState
	 *            The {@link ContextState}.
	 */
	TestSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Test.class, Project.class);

		// layers
		addJoinConfig(JoinConfig.up(Pool.class, Project.class));
		addJoinConfig(JoinConfig.up(Test.class, Pool.class));
		addJoinConfig(JoinConfig.down(Test.class, TestStep.class));
		addJoinConfig(JoinConfig.down(TestStep.class, Measurement.class));
		addJoinConfig(JoinConfig.down(Measurement.class, ChannelGroup.class));
		addJoinConfig(JoinConfig.down(Measurement.class, Channel.class));

		// context
		addJoinConfig(contextState);
	}

}
