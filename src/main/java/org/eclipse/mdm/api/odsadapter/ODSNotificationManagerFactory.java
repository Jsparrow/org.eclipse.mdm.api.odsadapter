package org.eclipse.mdm.api.odsadapter;

import java.util.Map;
import java.util.Optional;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import org.eclipse.mdm.api.base.BaseEntityManager;
import org.eclipse.mdm.api.base.ConnectionException;
import org.eclipse.mdm.api.base.NotificationManagerFactory;
import org.eclipse.mdm.api.base.model.BaseEntityFactory;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationManager;
import org.eclipse.mdm.api.base.query.ModelManager;
import org.eclipse.mdm.api.odsadapter.notification.avalon.AvalonNotificationManager;
import org.eclipse.mdm.api.odsadapter.notification.peak.PeakNotificationManager;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating a notification manager.
 * 
 * Currently only supports creating a notification manager for server type
 * 'peak'.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
@Stateful
@LocalBean
public class ODSNotificationManagerFactory implements NotificationManagerFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ODSNotificationManagerFactory.class);

	public static final String PARAM_SERVER_TYPE = "serverType";
	public static final String PARAM_URL = "url";
	public static final String PARAM_EVENT_MEDIATYPE = "eventMimetype";
	public static final String PARAM_POLLING_INTERVAL = "pollingInterval";

	public static final String SERVER_TYPE_PEAK = "peak";
	public static final String SERVER_TYPE_AVALON = "avalon";

	public static final String PARAM_NAMESERVICE_URL = "nameserviceURL";

	public NotificationManager create(BaseEntityManager<? extends BaseEntityFactory> entityManager,
			Map<String, String> parameters) throws ConnectionException {
		String type = getParameter(parameters, PARAM_SERVER_TYPE);

		Optional<ModelManager> mm = entityManager.getModelManager();
		if (!mm.isPresent()) {
			throw new ConnectionException("EntityManager has no ModelManager!");
		}
		if (!ODSModelManager.class.isInstance(mm.get())) {
			throw new ConnectionException("ModelManager is not a ODSModelManager!");
		}

		if (SERVER_TYPE_PEAK.equalsIgnoreCase(type)) {
			String url = getParameter(parameters, PARAM_URL);
			String eventMediaType = getParameter(parameters, PARAM_EVENT_MEDIATYPE);

			LOGGER.info("Connecting to Peak Notification Server ...");
			LOGGER.info("URL: {}", url);
			LOGGER.info("Event MediaType: {}", eventMediaType);

			try {
				return new PeakNotificationManager((ODSModelManager) mm.get(), url, eventMediaType, true);
			} catch (NotificationException e) {
				throw new ConnectionException("Could not connect to notification service!", e);
			}
		} else if (SERVER_TYPE_AVALON.equalsIgnoreCase(type)) {

			String serviceName = getParameter(parameters, ODSEntityManagerFactory.PARAM_SERVICENAME);
			serviceName = serviceName.replace(".ASAM-ODS", "");
			String nameServiceURL = getParameter(parameters, ODSEntityManagerFactory.PARAM_NAMESERVICE);

			LOGGER.info("Connecting to Avalon Notification Server ...");
			LOGGER.info("Name service URL: {}", nameServiceURL);
			LOGGER.info("Service name: {}", serviceName);

			long pollingInterval = 500L;
			try {
				pollingInterval = Long.parseLong(getParameter(parameters, PARAM_POLLING_INTERVAL));
			} catch (NumberFormatException | ConnectionException e) {
				LOGGER.warn("Could not parse parse parameter pollingInterval. Using default value: " + pollingInterval,
						e);
			}

			return new AvalonNotificationManager((ODSModelManager) mm.get(), serviceName, nameServiceURL, true,
					pollingInterval);
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
