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
import org.eclipse.mdm.api.base.EntityManager;
import org.eclipse.mdm.api.base.EntityManagerFactory;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class ODSEntityManagerFactory implements EntityManagerFactory<EntityManager> {

	public static final String PARAM_NAMESERVICE = "nameservice";
	public static final String PARAM_SERVICENAME = "servicename";
	public static final String PARAM_USER = "user";
	public static final String PARAM_PASSWORD = "password";

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
			NamingContextExt nameService = resolveNameService(odsNameService);
			AoFactory aoFactory = resolveAoFactorysService(odsServiceName, nameService);

			String connectionString = "USER=" + odsUser + ",PASSWORD=" + odsPassword  + ",CREATE_COSESSION_ALLOWED=TRUE";
			AoSession aoSession = aoFactory.newSession(connectionString);

			return aoSession;
		} catch(AoException aoe) {
			throw new ConnectionException(aoe.reason, aoe);
		}
	}

	private NamingContextExt resolveNameService(String nameServiceString) {
		org.omg.CORBA.Object nsObject = getORB().string_to_object(nameServiceString);
		NamingContextExt nsReference = NamingContextExtHelper.narrow(nsObject);

		return nsReference;
	}

	private AoFactory resolveAoFactorysService(String odsServiceName, NamingContextExt nameService)
			throws ConnectionException {
		try {
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
		} catch (NotFound e) {
			throw new ConnectionException(e.getMessage(), e);
		} catch (CannotProceed e) {
			throw new ConnectionException(e.getMessage(), e);
		} catch (InvalidName e) {
			throw new ConnectionException(e.getMessage(), e);
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
