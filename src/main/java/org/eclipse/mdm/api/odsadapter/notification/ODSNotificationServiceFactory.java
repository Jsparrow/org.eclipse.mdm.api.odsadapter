/*
 * Copyright (c) 2017-2018 Peak Solution GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.notification;

import java.util.Map;

import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.adapter.ModelManager;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationService;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.dflt.ApplicationContext;
import org.eclipse.mdm.api.odsadapter.notification.peak.PeakNotificationManager;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating a notification service.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class ODSNotificationServiceFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ODSNotificationServiceFactory.class);

	public static final String PARAM_SERVER_TYPE = "freetext.notificationType";
	public static final String PARAM_URL = "freetext.notificationUrl";
	public static final String PARAM_POLLING_INTERVAL = "freetext.pollingInterval";

	public static final String SERVER_TYPE_PEAK = "peak";
	public static final String SERVER_TYPE_AVALON = "avalon";

	public static final String PARAM_NAMESERVICE_URL = "nameserviceURL";
	
	public NotificationService create(ApplicationContext context, Map<String, String> parameters) throws ConnectionException {
		String type = getParameter(parameters, PARAM_SERVER_TYPE);

		ModelManager mm = context.getModelManager()
				.orElseThrow(() -> new ServiceNotProvidedException(ModelManager.class));
		
		QueryService queryService = context.getQueryService()
				.orElseThrow(() -> new ServiceNotProvidedException(QueryService.class));
		
		if (!ODSModelManager.class.isInstance(mm)) {
			throw new ConnectionException("ModelManager is not a ODSModelManager!");
		}

		if (SERVER_TYPE_PEAK.equalsIgnoreCase(type)) {
			String url = getParameter(parameters, PARAM_URL);
			
			LOGGER.info("Connecting to Peak Notification Server ...");
			LOGGER.info("URL: {}", url);

			try {
				return new PeakNotificationManager((ODSModelManager) mm, queryService, url, true);
			} catch (NotificationException e) {
				throw new ConnectionException("Could not connect to notification service!", e);
			}
		} else if (SERVER_TYPE_AVALON.equalsIgnoreCase(type)) {

			throw new IllegalArgumentException("Avalon notification service is not supported yet.");
//			String serviceName = getParameter(parameters, ODSContextFactory.PARAM_SERVICENAME);
//			serviceName = serviceName.replace(".ASAM-ODS", "");
//			String nameServiceURL = getParameter(parameters, ODSContextFactory.PARAM_NAMESERVICE);
//
//			LOGGER.info("Connecting to Avalon Notification Server ...");
//			LOGGER.info("Name service URL: {}", nameServiceURL);
//			LOGGER.info("Service name: {}", serviceName);
//
//			long pollingInterval = 500L;
//			try {
//				pollingInterval = Long.parseLong(getParameter(parameters, PARAM_POLLING_INTERVAL));
//			} catch (NumberFormatException | ConnectionException e) {
//				LOGGER.warn("Could not parse parse parameter pollingInterval. Using default value: " + pollingInterval,
//						e);
//			}
//
//			return new AvalonNotificationManager((ODSModelManager) mm, queryService, serviceName, nameServiceURL, true,
//					pollingInterval);
		} else {
			throw new ConnectionException("Invalid server type. Expected on of: 'peak'");
		}

	}

	private String getParameter(Map<String, String> parameters, String name) throws ConnectionException {
		String value = parameters.get(name);
		if (value == null || value.isEmpty()) {
			throw new ConnectionException("Connection parameter with name '" + name + "' is either missing or empty.");
		}

		return value;
	}
}
