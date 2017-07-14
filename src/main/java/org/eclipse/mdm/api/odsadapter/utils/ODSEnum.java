/*
 * Copyright (c) 2017 Florian Schmitt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import org.eclipse.mdm.api.base.model.EnumerationValue;

/**
 * Special enumeration class for ODS enumerations
 *
 */
public class ODSEnum extends EnumerationValue {
	/**
	 * Constructor
	 * 
	 * @param name: the text representation of the enumeration value
	 * @param ordinal: the numeric representation of the enumeration value 
	 */
	public ODSEnum (String name, int ordinal)
	{
		super(name,ordinal);
	}

}
