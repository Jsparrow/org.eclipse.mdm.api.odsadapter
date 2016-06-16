/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;

final class TestStepSearchQuery extends BaseEntitySearchQuery {

	public TestStepSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, TestStep.class);

		// layers
		addJoinConfig(JoinConfig.up(TestStep.class, Test.class));
		addJoinConfig(JoinConfig.down(TestStep.class, Measurement.class));

		// context
		addJoinConfig(contextState);
	}

}
