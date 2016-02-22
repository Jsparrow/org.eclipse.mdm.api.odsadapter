/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;

enum ContextState {

	ORDERED(TestStep.class),
	MEASURED(Measurement.class);

	private final Class<? extends ContextDescribable> type;

	private ContextState(Class<? extends ContextDescribable> contextDescribableType) {
		type = contextDescribableType;
	}

	Class<? extends ContextDescribable> getType() {
		return type;
	}

}
