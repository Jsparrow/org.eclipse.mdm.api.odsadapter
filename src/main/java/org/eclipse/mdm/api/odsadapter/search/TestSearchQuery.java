/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import java.util.Optional;

import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

final class TestSearchQuery extends BaseEntitySearchQuery {

	public TestSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Test.class, Test.class, Optional.of(contextState));

		addDependency(TestStep.class, Test.class, true, Join.INNER);
		addDependency(Measurement.class, TestStep.class, true, Join.INNER);
		addDependency(User.class, Test.class, true, Join.INNER);
	}

}
