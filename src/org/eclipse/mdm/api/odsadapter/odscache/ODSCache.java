/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.odscache;

import java.util.HashMap;
import java.util.Map;

import org.asam.ods.AoException;
import org.asam.ods.AoSession;
import org.asam.ods.ApplElem;
import org.asam.ods.ApplicationAttribute;
import org.asam.ods.ApplicationElement;
import org.asam.ods.ApplicationStructureValue;
import org.asam.ods.DataType;
import org.asam.ods.EnumerationDefinition;
import org.asam.ods.ErrorCode;
import org.asam.ods.NameValue;
import org.asam.ods.SeverityFlag;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * Cache for the ASAM-ODS application model and MDM instances.
 * 
 * @author Christian Rechner
 */
public class ODSCache {

	// private static final Log LOG = Activator.getDefault().getLogger();

	/**
	 * Constant for the SQL_MAX_ROWS context parameter of the ODS-Server.
	 */
	private static String PARAM_MAX_ROWS = "SQL_MAX_ROWS";

	/**
	 * Static cache for the maximum results of a SQL query. Field is static to
	 * avoid multiple initializations of this parameter. The configuration of
	 * the ODS-Server will not change while component is running.
	 */
	private static long sqlMaxRows = 0;

	// cached ODS AoSession (ODS interface)
	private AoSession aoSession = null;

	// cached application elements (ODS interface)
	private Map<String, ApplicationElement> applicationElemCache;

	// cached application attributes (ODS interface)
	private Map<String, ApplicationAttribute[]> applicationAttrCache;

	// cached enumeration definitions (ODS interface)
	private Map<String, EnumerationDefinition> enumDefCache;

	// cached applicationStructureValue (ODS interface)
	private ApplicationStructureValue applicationStructureValue = null;

	// cached applElemns (ODS struct)
	private ApplElem[] applElems = null;

	private Map<String, ApplElem> aeName2applElemMap = null;

	private Map<Long, ApplElem> aid2applElemMap = null;

	// cached unitId - unitName mapping
	private Map<Long, String> units = null;

	/**
	 * Constructor.
	 * 
	 * @param aoSession
	 *            the ODS session
	 */
	public ODSCache(AoSession aoSession) {
		if (aoSession == null) {
			throw new IllegalArgumentException("Parameter aoSession must not be null");
		}
		this.applicationElemCache = new HashMap<String, ApplicationElement>();
		this.applicationAttrCache = new HashMap<String, ApplicationAttribute[]>();
		this.enumDefCache = new HashMap<String, EnumerationDefinition>();
		this.aoSession = aoSession;
	}

	/*******************************************************************************************************************
	 * transaction handling
	 ******************************************************************************************************************/

	/*******************************************************************************************************************
	 * Methods for accessing cached ODS objects.
	 ******************************************************************************************************************/

	/**
	 * Returns the ODS session.
	 * 
	 * @return the ODS session
	 */
	public final AoSession getAoSession() {
		return aoSession;
	}

	/**
	 * Returns the cached ODS application structure value.
	 * 
	 * @return the cached applicationStructureValue
	 * @throws AoException
	 *             if something went wrong
	 */
	public final ApplicationStructureValue getApplicationStructureValue() throws AoException {
		if (applicationStructureValue == null) {
			applicationStructureValue = getAoSession().getApplicationStructureValue();
			// LOG.debug("ApplicationStructureValue loaded");
		}
		return applicationStructureValue;
	}

	/*******************************************************************************************************************
	 * methods for accessing cached ODS structs.
	 ******************************************************************************************************************/

	/**
	 * Returns all structs of ODS <code>ApplElem</code>.
	 * 
	 * @return the <code>ApplElem</code>
	 * @throws AoException
	 *             if something went wrong
	 */
	public final ApplElem[] getApplElems() throws AoException {
		if (this.applElems == null) {
			this.applElems = getApplicationStructureValue().applElems;
		}
		return applElems;
	}

	/**
	 * Returns the map between application element name and application element
	 * structure.
	 * 
	 * @return The map.
	 * @throws AoException
	 *             Error loading application element mapping.
	 */
	private Map<String, ApplElem> getAeName2applElemMap() throws AoException {
		if (this.aeName2applElemMap == null) {
			this.aeName2applElemMap = new HashMap<String, ApplElem>();
			for (ApplElem applElem : getApplElems()) {
				this.aeName2applElemMap.put(applElem.aeName, applElem);
			}
		}
		return this.aeName2applElemMap;
	}

	/**
	 * Returns the map between application element id and application element
	 * structure.
	 * 
	 * @return The map.
	 * @throws AoException
	 *             Error loading application element mapping.
	 */
	private Map<Long, ApplElem> getAid2applElemMap() throws AoException {
		if (this.aid2applElemMap == null) {
			this.aid2applElemMap = new HashMap<Long, ApplElem>();
			for (ApplElem applElem : getApplElems()) {
				this.aid2applElemMap.put(ODSUtils.asJLong(applElem.aid), applElem);
			}
		}
		return this.aid2applElemMap;
	}

	/**
	 * Returns an ODS <code>ApplElem</code> by given aid.
	 * 
	 * @param aid
	 *            the id of the application element
	 * @return the <code>ApplElem</code>
	 * @throws AoException
	 *             application element by given id not found
	 */
	public final ApplElem getApplElem(long aid) throws AoException {
		ApplElem applElem = getAid2applElemMap().get(aid);
		if (applElem != null) {
			return applElem;
		}
		throw new AoException(ErrorCode.AO_INVALID_ELEMENT, SeverityFlag.ERROR, 0, "AE [aid=" + aid + "] not found");
	}

	/**
	 * Returns an ODS <code>ApplElem</code> by given name.
	 * 
	 * @param aeName
	 *            the name of the application element
	 * @return the <code>ApplElem</code>
	 * @throws AoException
	 *             application element with given name not found
	 */
	public final ApplElem getApplElem(String aeName) throws AoException {
		ApplElem applElem = getAeName2applElemMap().get(aeName);
		if (applElem != null) {
			return applElem;
		}
		throw new AoException(ErrorCode.AO_NOT_FOUND, SeverityFlag.ERROR, 0, "AE [aeName=" + aeName + "] not found!");
	}
	/*******************************************************************************************************************
	 * methods for cached units access
	 ******************************************************************************************************************/

	/**
	 * Returns the mapping between the unit ids and unit names.
	 * 
	 * @return the map
	 * @throws AoException
	 *             error loading unit mapping
	 */
	private Map<Long, String> getUnitMapping() throws AoException {
		if (units == null) {
			units = new HashMap<Long, String>();

			ExtQuery query = createExtQuery();
			query.addSelect("Unit", "Id");
			query.addSelect("Unit", "Name");
			ExtQueryResult res = query.execute();

			while (res.next()) {
				long unitId = res.getLongLongVal("Unit", "Id");
				String unitName = res.getStringVal("Unit", "Name");
				units.put(unitId, unitName);
			}

		}
		return units;
	}

	/**
	 * Returns the unit name by given unit id. If unitId is 0, return an empty
	 * string.
	 * 
	 * @param unitId
	 *            the unit id, must be >0
	 * @return the unit name
	 * @throws AoException
	 *             unit with given id not found
	 */
	public String getUnitNameById(long unitId) throws AoException {
		if (unitId == 0) {
			return "";
		}

		String unitName = getUnitMapping().get(unitId);
		if (unitName != null) {
			return unitName;
		}

		throw new AoException(ErrorCode.AO_NOT_FOUND, SeverityFlag.ERROR, 0, "Unit [id=" + unitId + "] not found!");
	}

	/*******************************************************************************************************************
	 * Methods for creating fast query, insert and update wrappers
	 ******************************************************************************************************************/

	/**
	 * Creates an empty <code>ExtQuery</code>
	 * 
	 * @return the MDM extendend query
	 */
	public final ExtQuery createExtQuery() {
		return new ExtQuery(this);
	}

	/**
	 * Clears the ODS cache.
	 */
	public void clearODSCache() {
		applicationElemCache.clear();
		applicationAttrCache.clear();
		enumDefCache.clear();
		applicationStructureValue = null;
		applElems = null;
		this.aeName2applElemMap = null;
		this.aid2applElemMap = null;
		units = null;
	}

	/**
	 * Returns cached result limit for extended queries. Value is > 0 or -1 if
	 * unable to retrieve the value identified by it's name.
	 * 
	 * Method is synchronized to avoid multiple initializations.
	 * 
	 * @return result limit for extended queries.
	 */
	protected synchronized long getSQLMaxRows() {
		if (sqlMaxRows == 0) {
			try {
				NameValue nv = getAoSession().getContextByName(PARAM_MAX_ROWS);
				if (nv.value.flag == 15 && nv.value.u.discriminator() == DataType.DT_STRING) {
					sqlMaxRows = Long.parseLong(nv.value.u.stringVal());
				} else {
					sqlMaxRows = -1;
				}
			} catch (AoException aoe) {
				sqlMaxRows = -1;
				// LOG.warn("Unable to retrieve context value '" +
				// PARAM_MAX_ROWS + "' due to: " + aoe.reason, aoe);
			} catch (NumberFormatException nfe) {
				sqlMaxRows = -1;
				// LOG.warn("Unable to retrieve context value '" +
				// PARAM_MAX_ROWS + "' due to: " + nfe.getMessage(), nfe);
			}
		}

		return sqlMaxRows;
	}

}
