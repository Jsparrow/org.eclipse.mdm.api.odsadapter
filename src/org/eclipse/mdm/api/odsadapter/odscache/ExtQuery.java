/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.odscache;

import java.util.ArrayList;
import java.util.List;

import org.asam.ods.AIDName;
import org.asam.ods.AggrFunc;
import org.asam.ods.AoException;
import org.asam.ods.ApplElem;
import org.asam.ods.JoinDef;
import org.asam.ods.QueryStructureExt;
import org.asam.ods.ResultSetExt;
import org.asam.ods.SelAIDNameUnitId;
import org.asam.ods.SelItem;
import org.asam.ods.SelOrder;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * Helper class to easily construct ODS extended queries.
 * 
 * @author Christian Rechner
 */
public class ExtQuery {

	// private static final Log LOG = Activator.getDefault().getLogger();

	private final ODSCache cache;

	private final List<SelAIDNameUnitId> anuSeq = new ArrayList<SelAIDNameUnitId>();

	/**
	 * Constructor.
	 * 
	 * @param cache
	 *            the ODS cache
	 */
	public ExtQuery(ODSCache cache) {
		this.cache = cache;
	}

	/*******************************************************************************************************************
	 * methods used to add "anuSeq"
	 ******************************************************************************************************************/

	/**
	 * Adds a select statement to fetch an attribute value from given
	 * application element. No aggregate function is used.
	 * 
	 * @param aeName
	 *            the name of the application element
	 * @param aaName
	 *            the name of the application attribute
	 * @throws AoException
	 *             if something went wrong
	 */
	public void addSelect(String aeName, String aaName) throws AoException {
		addSelect(aeName, aaName, AggrFunc.NONE);
	}

	/**
	 * Adds a select statement to fetch an attributes value from given
	 * application element. It is possible to specify an aggregate function.
	 * 
	 * @param aeName
	 *            the name of the application element
	 * @param aaName
	 *            the name of the application attribute
	 * @param aggr
	 *            the aggregate function
	 * @throws AoException
	 *             if something went wrong
	 */
	public void addSelect(String aeName, String aaName, AggrFunc aggr) throws AoException {
		// find ApplElem
		ApplElem applElem = cache.getApplElem(aeName);

		// create SelAIDNameUnitId
		SelAIDNameUnitId sanu = new SelAIDNameUnitId();
		sanu.attr = new AIDName();
		sanu.attr.aaName = aaName;
		sanu.attr.aid = applElem.aid;
		sanu.aggregate = aggr;
		sanu.unitId = ODSUtils.asODSLongLong(0);

		// add attribute
		anuSeq.add(sanu);
	}

	/*******************************************************************************************************************
	 * methods used to create QueryStructureExt and execute the query
	 ******************************************************************************************************************/

	/**
	 * Creates a new QueryStructureExt and returns it initialized with
	 * initialized internal lists.
	 * 
	 * @return QueryStructureExt is returned.
	 */
	private QueryStructureExt createQueryStructureExt() {
		QueryStructureExt qse = new QueryStructureExt();

		// create anuSeq
		qse.anuSeq = anuSeq.toArray(new SelAIDNameUnitId[anuSeq.size()]);

		// create condSeq
		qse.condSeq = new SelItem[0];

		// create groupBy
		qse.groupBy = new AIDName[0];

		// create joinSeq
		qse.joinSeq = new JoinDef[0];

		// create orderBy
		qse.orderBy = new SelOrder[0];

		return qse;
	}

	/**
	 * Execute the query and return the result.
	 * 
	 * @return an instance of <code>ExtQueryResult</code>
	 * @throws AoException
	 *             if something went wrong
	 */
	public ExtQueryResult execute() throws AoException {
		ResultSetExt[] rse = cache.getAoSession().getApplElemAccess().getInstancesExt(createQueryStructureExt(), 0);
		return new ExtQueryResult(cache, rse);
	}

}
