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
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.search.JoinTree.JoinConfig;

final class MeasurementSearchQuery extends BaseEntitySearchQuery {

	public MeasurementSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Measurement.class);

		// layers
		addJoinConfig(JoinConfig.up(TestStep.class, Test.class));
		addJoinConfig(JoinConfig.up(Measurement.class, TestStep.class));
		addJoinConfig(JoinConfig.down(Measurement.class, Channel.class));

		// context
		addJoinConfig(contextState);
	}

}
