/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.query;

import java.util.Optional;

import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Join;

final class ChannelSearchQuery extends AbstractDataItemSearchQuery {

	public ChannelSearchQuery(ODSQueryService queryService, ContextState contextState) {
		super(queryService, Channel.class, Test.class /* TODO: Change to Project */, Optional.of(contextState));

		// TODO addDependency(Project.class, Pool.class, false, Join.INNER);
		// TODO addDependency(Pool.class, Test.class, false, Join.INNER);

		addDependency(Test.class, TestStep.class, false, Join.INNER);
		addDependency(User.class, Test.class, true, Join.INNER);
		addDependency(TestStep.class, Measurement.class, false, Join.INNER);
		addDependency(Measurement.class, Channel.class, false, Join.INNER);
		addDependency(Unit.class, Channel.class, true, Join.INNER);
		addDependency(Quantity.class, Channel.class, true, Join.INNER);
	}

}
