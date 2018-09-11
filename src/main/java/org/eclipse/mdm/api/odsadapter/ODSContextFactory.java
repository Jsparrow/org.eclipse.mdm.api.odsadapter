/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.asam.ods.AoException;
import org.asam.ods.AoFactory;
import org.asam.ods.AoFactoryHelper;
import org.asam.ods.AoSession;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.dflt.ApplicationContextFactory;
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

/**
 * ASAM ODS implementation of the {@link ApplicationContextFactory} interface.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class ODSContextFactory implements ApplicationContextFactory {

	public static final String PARAM_NAMESERVICE = "nameservice";

	public static final String PARAM_SERVICENAME = "servicename";

	public static final String PARAM_USER = "user";

	public static final String PARAM_PASSWORD = "password";

	private static final String PARAM_FOR_USER = "for_user";

	private static final Logger LOGGER = LoggerFactory.getLogger(ODSContextFactory.class);

	private final ORB orb = ORB.init(new String[] {}, System.getProperties());

	public ODSContextFactory() {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * <b>Note:</b> Given parameters {@code Map} must contain values for each of
	 * the following keys:
	 *
	 * <ul>
	 * <li>{@value #PARAM_NAMESERVICE}</li>
	 * <li>{@value #PARAM_SERVICENAME}</li>
	 * <li>{@value #PARAM_USER}</li>
	 * <li>{@value #PARAM_PASSWORD}</li>
	 * </ul>
	 *
	 * Listed names are available via public fields of this class.
	 */
	@Override
	public ApplicationContext connect(Map<String, String> parameters) throws ConnectionException {
		AoSession aoSession = null;
		try (ServiceLocator serviceLocator = new ServiceLocator(orb, getParameter(parameters, PARAM_NAMESERVICE))) {
			String nameOfService = getParameter(parameters, PARAM_SERVICENAME).replace(".ASAM-ODS", "");

			AoFactory aoFactory = serviceLocator.resolveFactory(nameOfService);
			String aoFactoryName = aoFactory.getName();
			LOGGER.info("Connecting to ODS Server (name: {}, description: {}, interface version: {}, type: {}) ...", 
					aoFactoryName, aoFactory.getDescription(), aoFactory.getInterfaceVersion(), aoFactory.getType());

			// Create a parameters map without password (which should not be visible from this point onwards),
			// leaving the original map untouched:
			Map<String, String> parametersWithoutPassword = new HashMap<>(parameters);
			if (LOGGER.isDebugEnabled()) {
				parametersWithoutPassword.put(PARAM_PASSWORD, "****");
				LOGGER.debug("Connecting to ODS using the connection parameters: {}", sessionParametersAsString(parametersWithoutPassword));
			}
			parametersWithoutPassword.remove(PARAM_PASSWORD);
			
			aoSession = aoFactory.newSession(sessionParametersAsString(parameters));

			LOGGER.info("Connection to ODS server '{}' established.", aoFactoryName);

			CORBAFileServerIF fileServer = serviceLocator.resolveFileServer(nameOfService);

			return new ODSContext(orb, aoSession, fileServer, parametersWithoutPassword);
		} catch (AoException e) {
			closeSession(aoSession);
			throw new ConnectionException("Unable to connect to ODS server due to: " + e.reason, e);
		}
	}

	private String sessionParametersAsString(Map<String, String> parameters) throws ConnectionException {
		ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder()
				.put("USER", getParameter(parameters, PARAM_USER))
				.put("PASSWORD", getParameter(parameters, PARAM_PASSWORD))
				.put("CREATE_COSESSION_ALLOWED", "TRUE");

		String forUserName = parameters.get(PARAM_FOR_USER);
		if(!Strings.isNullOrEmpty(forUserName)) {
			builder.put("FOR_USER", forUserName);
		}
		return Joiner.on(",").withKeyValueSeparator("=").join(builder.build());
	}

	/**
	 * Closes given {@link AoSession} with catching and logging errors.
	 *
	 * @param aoSession
	 *            The {@code AoSession} that shall be closed.
	 */
	private static void closeSession(AoSession aoSession) {
		if (aoSession == null) {
			return;
		}

		try {
			aoSession.close();
		} catch (AoException e) {
			LOGGER.warn("Unable to close sesssion due to: " + e.reason, e);
		}
	}

	/**
	 * Reads the property identified by given property name.
	 *
	 * @param parameters
	 *            The properties {@code Map}.
	 * @param name
	 *            The property name.
	 * @return The property value is returned.
	 * @throws ConnectionException
	 *             Thrown if property does not exist or is empty.
	 */
	private static String getParameter(Map<String, String> parameters, String name) throws ConnectionException {
		String value = parameters.get(name);
		if (value == null || value.isEmpty()) {
			throw new ConnectionException("Connection parameter with name '" + name + "' is either missing or empty.");
		}

		return value;
	}

	/**
	 * Used to resolve CORBA service object by ID and kind.
	 */
	private static final class ServiceLocator implements AutoCloseable {

		private NamingContextExt namingContext;

		/**
		 * Constructor.
		 *
		 * @param orb
		 *            The {@link ORB} singleton instance.
		 * @param path
		 *            The naming context path.
		 * @throws ConnectionException
		 *             Thrown if unable to resolve the naming context.
		 */
		public ServiceLocator(ORB orb, String path) throws ConnectionException {
			namingContext = NamingContextExtHelper.narrow(orb.string_to_object(path));
			if (namingContext == null) {
				throw new ConnectionException("Unable to resolve NameService '" + path + "'.");
			}
		}

		/**
		 * Resolves and returns the {@link AoFactory} service for given ID.
		 *
		 * @param id
		 *            Used as identifier.
		 * @return The {@code AoFactory} is returned.
		 * @throws ConnectionException
		 *             Thrown if unable to resolve the {@code
		 * 		AoFactory}.
		 */
		public AoFactory resolveFactory(String id) throws ConnectionException {
			return AoFactoryHelper.narrow(resolve(id, "ASAM-ODS"));
		}

		/**
		 * Resolves and returns the {@link CORBAFileServerIF} service for given
		 * ID.
		 *
		 * @param id
		 *            Used as identifier.
		 * @return The {@code CORBAFileServerIF} or null, if none found, is
		 *         returned.
		 */
		public CORBAFileServerIF resolveFileServer(String id) {
			try {
				return CORBAFileServerIFHelper.narrow(resolve(id, "CORBA-FT"));
			} catch (ConnectionException e) {
				LOGGER.warn(e.getMessage());
				return null;
			}
		}

		/**
		 * Resolves a CORBA service object for given id and kind.
		 *
		 * @param id
		 *            Used as identifier.
		 * @param kind
		 *            Used as qualifier.
		 * @return The resolved CORBA service object is returned.
		 * @throws ConnectionException
		 *             Thrown in case of errors.
		 */
		public Object resolve(String id, String kind) throws ConnectionException {
			try {
				return namingContext.resolve(new NameComponent[] { new NameComponent(id, kind) });
			} catch (NotFound | CannotProceed | InvalidName e) {
				throw new ConnectionException("Unable to resolve service '" + id + "." + kind + "'.", e);
			}
		}

		@Override
		public void close() throws ConnectionException {
			namingContext._release();
		}

	}

}