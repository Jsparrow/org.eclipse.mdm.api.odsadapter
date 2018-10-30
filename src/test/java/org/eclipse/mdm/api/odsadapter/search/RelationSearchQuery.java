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


package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.ParameterSet;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.dflt.model.Pool;
import org.eclipse.mdm.api.dflt.model.Project;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;

/**
 * This class is used as a helper to the tests in org.eclipse.mdm.api.odsadapter.RelationTest
 * It needs to be here, because BaseEntitySearchQuery is declared as protected
 * in the org.eclipse.mdm.api.odsadapter.search package. 
 * It makes some sense to leave it protected in that way, because implementing a BaseEntitySearchQuery
 * requires understanding of the underlying data model which   
 * The RelationTest requires that the related parametersets to a measurement are
 * loaded. This class provides a JoinConfig which allows this to happen by defining
 * the way the entities in question have to be joined. 
 * If we wouldn't join the ParameterSet in, the related
 * entities would not be loaded, and it would be impossible to determine whether there
 * are any related ParameterSets or not.
 *
 */
public final class RelationSearchQuery extends BaseEntitySearchQuery {

	/**
	 * Constructor.
	 *
	 * @param modelManager
	 *            Used to load {@link EntityType}s.
	 * @param contextState
	 *            The {@link ContextState}.
	 */
	public RelationSearchQuery(ODSModelManager modelManager, QueryService queryService) {
		super(modelManager, queryService, ParameterSet.class, Project.class);

		// layers
		addJoinConfig(JoinConfig.up(Pool.class, Project.class));
		addJoinConfig(JoinConfig.up(Test.class, Pool.class));
		addJoinConfig(JoinConfig.up(TestStep.class, Test.class));
		addJoinConfig(JoinConfig.up(Measurement.class, TestStep.class));
		addJoinConfig(JoinConfig.up(ParameterSet.class, Measurement.class));
		addJoinConfig(JoinConfig.down(Measurement.class, Channel.class));
		addJoinConfig(JoinConfig.down(Measurement.class, ChannelGroup.class));

	}

}