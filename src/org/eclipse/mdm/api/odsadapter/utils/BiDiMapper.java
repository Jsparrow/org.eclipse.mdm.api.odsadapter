/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.util.HashMap;
import java.util.Map;

public final class BiDiMapper<T, S> {

	private final Map<Object, Object> map = new HashMap<>();

	@SuppressWarnings("unchecked")
	public S convert(T input) {
		return (S) map.get(input);
	}

	@SuppressWarnings("unchecked")
	public T revert(S input) {
		return (T) map.get(input);
	}

	void addMappings(T t, S s) {
		map.put(t, s);
		map.put(s, t);
	}

}
