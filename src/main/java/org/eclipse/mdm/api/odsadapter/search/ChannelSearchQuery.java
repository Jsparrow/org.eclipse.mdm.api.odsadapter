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
import org.eclipse.mdm.api.base.model.Quantity;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;

final class ChannelSearchQuery extends BaseEntitySearchQuery {

	public ChannelSearchQuery(ODSModelManager modelManager, ContextState contextState) {
		super(modelManager, Channel.class, Test.class, Optional.of(contextState));

		addDependency(Test.class, TestStep.class, false, Join.INNER);
		addDependency(User.class, Test.class, true, Join.INNER);
		addDependency(TestStep.class, Measurement.class, false, Join.INNER);
		addDependency(Measurement.class, Channel.class, false, Join.INNER);
		addDependency(Unit.class, Channel.class, true, Join.INNER);
		addDependency(Quantity.class, Channel.class, true, Join.INNER);

		// TODO join to sensor tables.... || this will break the joins to context data
		// multiple outer join to the same table...
	}

}
