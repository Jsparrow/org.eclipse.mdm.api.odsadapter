/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.Optional;

import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Join;

final class TestStepSearchQuery extends AbstractDataItemSearchQuery {

	public TestStepSearchQuery(ODSQueryService queryService, ContextState contextState) {
		super(queryService, TestStep.class, Test.class /* TODO: Change to Project */, Optional.of(contextState));

		// TODO addDependency(Project.class, Pool.class, false, Join.INNER);
		// TODO addDependency(Pool.class, Test.class, false, Join.INNER);
		// TODO State addDependency(TestStep.class, Test.class, true, Join.INNER);

		addDependency(Test.class, TestStep.class, false, Join.INNER);
		addDependency(User.class, Test.class, true, Join.INNER);
		addDependency(Measurement.class, TestStep.class, true, Join.INNER);
	}

}
