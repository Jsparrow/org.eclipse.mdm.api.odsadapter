/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Bidirectional mapping of configured values.
 *
 * @param <T>
 *            Mapped object type one.
 * @param <S>
 *            Mapped object type two.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class BiDiMapper<T, S> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<Object, Object> map = new HashMap<>();

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the mapping for given value.
	 *
	 * @param input
	 *            The value.
	 * @return The corresponding mapped value is returned.
	 */
	@SuppressWarnings("unchecked")
	public S convert(T input) {
		return (S) map.get(input);
	}

	/**
	 * Returns the mapping for given value.
	 *
	 * @param input
	 *            The value.
	 * @return The corresponding mapped value is returned.
	 */
	@SuppressWarnings("unchecked")
	public T revert(S input) {
		return (T) map.get(input);
	}

	// ======================================================================
	// Package methods
	// ======================================================================

	/**
	 * Adds a new bidirectional mapping for given values.
	 *
	 * @param t
	 *            Not allowed to be null.
	 * @param s
	 *            Not allowed to be null.
	 */
	void addMappings(T t, S s) {
		map.put(t, s);
		map.put(s, t);
	}

}
