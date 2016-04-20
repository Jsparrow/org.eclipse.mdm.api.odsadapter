/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.Optional;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

final class MeasurementSearchQuery extends BaseEntitySearchQuery {

	public MeasurementSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Measurement.class, Test.class, Optional.of(contextState));

		addDependency(Test.class, TestStep.class, false, Join.INNER);
		addDependency(User.class, Test.class, true, Join.INNER);
		addDependency(TestStep.class, Measurement.class, false, Join.INNER);
		addDependency(Channel.class, Measurement.class, true, Join.INNER);
	}

}
