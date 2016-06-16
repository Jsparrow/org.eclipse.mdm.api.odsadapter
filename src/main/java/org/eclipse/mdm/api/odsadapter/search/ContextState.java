/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.search;

enum ContextState {

	ORDERED,
	MEASURED;

	public boolean isOrdered() {
		return ORDERED == this;
	}

	public boolean isMeasured() {
		return MEASURED == this;
	}

}
