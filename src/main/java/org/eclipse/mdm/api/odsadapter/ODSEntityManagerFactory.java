/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.asam.ods.AoException;
import org.asam.ods.AoFactory;
import org.asam.ods.AoFactoryHelper;
import org.asam.ods.AoSession;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.EntityManagerFactory;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODSEntityManagerFactory implements EntityManagerFactory<EntityManager> {

	public static final String PARAM_NAMESERVICE = "nameservice";
	public static final String PARAM_SERVICENAME = "servicename";
	public static final String PARAM_USER = "user";
	public static final String PARAM_PASSWORD = "password";

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSEntityManagerFactory.class);

	private ORB orb = null;

	/**
	 * {@inheritDoc}
	 *
	 * <p><b>Note:</b> Given parameters {@code Map} must contain values for
	 * each of the following keys:
	 *
	 * <ul>
	 * 	<li>{@value #PARAM_NAMESERVICE}</li>
	 * 	<li>{@value #PARAM_SERVICENAME}</li>
	 * 	<li>{@value #PARAM_USER}</li>
	 * 	<li>{@value #PARAM_PASSWORD}</li>
	 * </ul>
	 *
	 * Listed names are available via public fields of this class.
	 * <p>
	 */
	@Override
	public EntityManager connect(Map<String, String> parameters) throws ConnectionException {
		String odsNameService = getParameter(parameters, PARAM_NAMESERVICE);
		String odsServiceName = getParameter(parameters, PARAM_SERVICENAME);
		String odsUser = getParameter(parameters, PARAM_USER);
		String odsPassword = getParameter(parameters, PARAM_PASSWORD);

		return new ODSEntityManager(connect2ODS(odsNameService, odsServiceName, odsUser, odsPassword));
	}

	private AoSession connect2ODS(String odsNameService, String odsServiceName, String odsUser,
			String odsPassword) throws ConnectionException {
		try {
			AoFactory aoFactory = resolveAoFactorysService(odsServiceName, odsNameService);

			LOGGER.info("Connecting to ODS Server ...");

			LOGGER.info("AoFactory name: {}", aoFactory.getName());
			LOGGER.info("AoFactory description: {}", aoFactory.getDescription());
			LOGGER.info("AoFactory interface version: {}", aoFactory.getInterfaceVersion());
			LOGGER.info("AoFactory type: {}", aoFactory.getType());

			String connectionString = "USER=" + odsUser + ",PASSWORD=" + odsPassword  + ",CREATE_COSESSION_ALLOWED=TRUE";
			AoSession aoSession = aoFactory.newSession(connectionString);

			LOGGER.info("Connection to ODS server established.");
			return aoSession;
		} catch(AoException e) {
			throw new ConnectionException("Unablte to connect to ODS server due to: " + e.reason, e);
		}
	}

	private AoFactory resolveAoFactorysService(String odsServiceName, String odsNameService) throws ConnectionException {
		try {
			NamingContextExt nameService = NamingContextExtHelper.narrow(getORB().string_to_object(odsNameService));
			if(nameService == null) {
				throw new ConnectionException("Unable to resolve NameService '" + odsNameService + "'.");
			}

			if(odsServiceName.contains("/")) {
				String[] ncsStrings = odsServiceName.split("/");
				List<NameComponent> ncList = new ArrayList<>();
				for(String ncString : ncsStrings) {
					NameComponent nc = new NameComponent(ncString, "");
					ncList.add(nc);
				}
				NameComponent[] ncs = ncList.toArray(new NameComponent[ncList.size()]);
				org.omg.CORBA.Object o = nameService.resolve(ncs);
				return AoFactoryHelper.narrow(o);
			}

			if(odsServiceName.toUpperCase(Locale.ROOT).endsWith(".ASAM-ODS")) {
				String serviceName = odsServiceName.replace(".ASAM-ODS", "");
				NameComponent nc = new NameComponent(serviceName, "ASAM-ODS");
				org.omg.CORBA.Object o = nameService.resolve(new NameComponent[]{nc});
				return AoFactoryHelper.narrow(o);
			}

			NameComponent nc = new NameComponent(odsServiceName, "");
			org.omg.CORBA.Object o = nameService.resolve(new NameComponent[]{nc});

			return AoFactoryHelper.narrow(o);
		} catch (NotFound | CannotProceed | InvalidName e) {
			throw new ConnectionException("Unable to resolve AoFactory.", e);
		}
	}

	private String getParameter(Map<String, String> parameters, String name) throws ConnectionException {
		String value = parameters.get(name);
		if(value == null || value.isEmpty()) {
			throw new ConnectionException("Connection parameter with name '" + name + "' is either missing or empty.");
		}

		return value;
	}

	private ORB getORB() {
		if(orb == null) {
			Properties props = new Properties(System.getProperties());
			//			props.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
			//			props.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");

			orb = ORB.init(new String[]{}, props);
		}

		return orb;
	}

}
