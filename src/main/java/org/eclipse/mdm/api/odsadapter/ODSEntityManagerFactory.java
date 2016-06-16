/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.Map;

import javax.ejb.Stateful;

import org.asam.ods.AoException;
import org.asam.ods.AoFactory;
import org.asam.ods.AoFactoryHelper;
import org.asam.ods.AoSession;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.EntityManagerFactory;
import org.eclipse.mdm.api.dflt.EntityManager;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highqsoft.corbafileserver.generated.CORBAFileServerIF;
import com.highqsoft.corbafileserver.generated.CORBAFileServerIFHelper;

@Stateful
public class ODSEntityManagerFactory implements EntityManagerFactory<EntityManager> {

	public static final String AUTH_TEMPLATE = "USER=%s,PASSWORD=%s,CREATE_COSESSION_ALLOWED=TRUE";

	public static final String PARAM_NAMESERVICE = "nameservice";
	public static final String PARAM_SERVICENAME = "servicename";
	public static final String PARAM_USER = "user";
	public static final String PARAM_PASSWORD = "password";

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSEntityManagerFactory.class);

	private final ORB orb = ORB.init(new String[]{}, System.getProperties());

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
		try(NameService nameService = new NameService(orb, getParameter(parameters, PARAM_NAMESERVICE))) {
			String nameOfService = getParameter(parameters, PARAM_SERVICENAME).replace(".ASAM-ODS", "");

			AoFactory aoFactory = AoFactoryHelper.narrow(nameService.resolve(nameOfService, "ASAM-ODS"));
			LOGGER.info("Connecting to ODS Server ...");

			LOGGER.info("AoFactory name: {}", aoFactory.getName());
			LOGGER.info("AoFactory description: {}", aoFactory.getDescription());
			LOGGER.info("AoFactory interface version: {}", aoFactory.getInterfaceVersion());
			LOGGER.info("AoFactory type: {}", aoFactory.getType());

			AoSession aoSession = aoFactory.newSession(String.format(AUTH_TEMPLATE,
					getParameter(parameters, PARAM_USER),
					getParameter(parameters, PARAM_PASSWORD)));
			LOGGER.info("Connection to ODS server established.");

			CORBAFileServerIF fileServer = CORBAFileServerIFHelper.narrow(nameService.resolve(nameOfService, "CORBA-FT"));

			return new ODSEntityManager(new ODSModelManager(aoSession, fileServer));
		} catch(AoException e) {
			e.printStackTrace();
			throw new ConnectionException("Unablte to connect to ODS server due to: " + e.reason, e);
		}
	}

	private String getParameter(Map<String, String> parameters, String name) throws ConnectionException {
		String value = parameters.get(name);
		if(value == null || value.isEmpty()) {
			throw new ConnectionException("Connection parameter with name '" + name + "' is either missing or empty.");
		}

		return value;
	}

	private static final class NameService implements AutoCloseable {

		private final String path;

		private NamingContextExt namingContext;
		private ORB orb;

		public NameService(ORB orb, String path) {
			this.path = path;
			this.orb = orb;
		}

		public Object resolve(String id, String kind) throws  ConnectionException {
			try {
				return getNameService().resolve(new NameComponent[] { new NameComponent(id, kind) });
			} catch (NotFound | CannotProceed | InvalidName e) {
				throw new ConnectionException("Unable to resolve service '" + id + "." + kind + "'.", e);
			}
		}

		private NamingContextExt getNameService() throws ConnectionException {
			if(namingContext == null) {
				namingContext = NamingContextExtHelper.narrow(orb.string_to_object(path));
				if(namingContext == null) {
					throw new ConnectionException("Unable to resolve NameService '" + path + "'.");
				}
			}

			return namingContext;
		}

		@Override
		public void close() throws ConnectionException {
			getNameService()._release();
		}

	}

}
