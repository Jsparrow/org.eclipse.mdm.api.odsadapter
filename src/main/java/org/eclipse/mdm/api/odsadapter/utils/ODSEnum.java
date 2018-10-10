/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


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
