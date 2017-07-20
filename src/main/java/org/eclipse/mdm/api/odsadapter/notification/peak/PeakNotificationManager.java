package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.eclipse.mdm.api.base.notification.NotificationManager;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.notification.NotificationEntityLoader;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.peaksolution.ods.notification.protobuf.NotificationProtos.Notification;

/**
 * Notification manager for handling notifications from the Peak ODS Server
 * Notification Plugin
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class PeakNotificationManager implements NotificationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(PeakNotificationManager.class);

	private final Client client;
	private final WebTarget endpoint;

	private final Map<String, EventProcessor> processors = new HashMap<>();

	private final ExecutorService executor = Executors.newCachedThreadPool();

	private final MediaType eventMediaType;
	private final ODSModelManager modelManager;

	private final NotificationEntityLoader loader;

	/**
	 * @param modelManager
	 * @param url
	 *            URL of the notification plugin
	 * @param eventMediaType
	 *            MediaType to use.
	 * @param loadContextDescribable
	 *            if true, the corresponding context describable is loaded if a
	 *            notification for a context root or context component is
	 *            received.
	 * @throws NotificationException
	 *             Thrown if the manager cannot connect to the notification
	 *             server.
	 */
	public PeakNotificationManager(ODSModelManager modelManager, String url, String eventMediaType,
			boolean loadContextDescribable) throws NotificationException {
		this.modelManager = modelManager;
		loader = new NotificationEntityLoader(modelManager, loadContextDescribable);

		try {
			if (Strings.isNullOrEmpty(eventMediaType) || MediaType.APPLICATION_JSON.equalsIgnoreCase(eventMediaType)) {
				this.eventMediaType = MediaType.APPLICATION_JSON_TYPE;
			} else {
				this.eventMediaType = ProtobufMessageBodyProvider.APPLICATION_PROTOBUF_TYPE;
			}

			client = ClientBuilder.newBuilder().register(SseFeature.class).register(ProtobufMessageBodyProvider.class)
					.register(JsonMessageBodyProvider.class).build();

			endpoint = client.target(url).path("events");
		} catch (Exception e) {
			throw new NotificationException("Could not create " + PeakNotificationManager.class.getName() + "!", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.mdm.api.base.notification.NotificationManager#register(java.
	 * lang.String, org.eclipse.mdm.api.base.notification.NotificationFilter,
	 * org.eclipse.mdm.api.base.notification.NotificationListener)
	 */
	@Override
	public void register(String registration, NotificationFilter filter, NotificationListener listener)
			throws NotificationException {
		Response response = endpoint.path(registration).request().post(javax.ws.rs.client.Entity
				.entity(ProtobufConverter.from(filter), ProtobufMessageBodyProvider.APPLICATION_PROTOBUF_TYPE));

		if (response.getStatusInfo().getStatusCode() == Status.CONFLICT.getStatusCode()) {
			LOGGER.info("A registration with the name already exists: " + response.readEntity(String.class));
			LOGGER.info("Trying to reregister...");
			deregister(registration);
			register(registration, filter, listener);
			return;
		}

		if (response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode()) {
			throw new NotificationException(
					"Could not create registration at notification service: " + response.readEntity(String.class));
		}

		try {
			EventInput eventInput = endpoint.path(registration).request().get(EventInput.class);

			EventProcessor processor = new EventProcessor(eventInput, listener, this, eventMediaType);

			executor.submit(processor);

			processors.put(registration, processor);
		} catch (Exception e) {
			try {
				deregister(registration);
			} catch (Exception ex) {
				LOGGER.error("Exception upon deregistering!");
			}
			throw new NotificationException("Could not create event input stream!", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.mdm.api.base.notification.NotificationManager#deregister(java
	 * .lang.String)
	 */
	@Override
	public void deregister(String registration) {
		if (processors.containsKey(registration)) {
			close(registration);
		}

		endpoint.path(registration).request().delete();
	}

	@Override
	public void close(boolean isDeregisterAll) throws NotificationException {
		LOGGER.info("Closing NotificationManager...");

		for (String registration : processors.keySet()) {
			if (isDeregisterAll) {
				LOGGER.debug("Deregistering '" + registration + "'.");
				deregister(registration);
			} else {
				LOGGER.debug("Disconnecting '" + registration + "'.");
				close(registration);
			}
		}
	}

	private void close(String registration) {
		if (processors.containsKey(registration)) {
			EventProcessor processor = processors.get(registration);
			processor.stop();
			processors.remove(registration);
		}
	}

	/**
	 * Handler for Exceptions during event processing.
	 * 
	 * @param e
	 *            Exception which occured during event processing.
	 */
	void processException(Exception e) {
		LOGGER.error("Exception during notification processing!", e);
	}

	/**
	 * Handler for notifications.
	 * 
	 * @param n
	 *            notification to process.
	 * @param notificationListener
	 *            notification listener for handling the notification.
	 */
	void processNotification(Notification n, NotificationListener notificationListener) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Processing notification event: " + n);
		}

		try {
			User user = loader.load(new Key<>(User.class), Long.toString(n.getUserId()));

			EntityType entityType = modelManager.getEntityType(Long.toString(n.getAid()));

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Notification event with: entityType=" + entityType + ", user=" + user);
			}

			switch (n.getType()) {
			case NEW:
				notificationListener.instanceCreated(loader.loadEntities(entityType, n.getIidList().stream().map(id -> id.toString()).collect(Collectors.toList())), user);
				break;
			case MODIFY:
				notificationListener.instanceModified(loader.loadEntities(entityType, n.getIidList().stream().map(id -> id.toString()).collect(Collectors.toList())), user);
				break;
			case DELETE:
				notificationListener.instanceDeleted(entityType,
						n.getIidList().stream().map(id -> id.toString()).collect(Collectors.toList()), user);
				break;
			case MODEL:
				notificationListener.modelModified(entityType, user);
				break;
			case SECURITY:
				notificationListener.securityModified(entityType,
						n.getIidList().stream().map(id -> id.toString()).collect(Collectors.toList()), user);
				break;
			default:
				processException(new NotificationException("Invalid notification type!"));
			}
		} catch (Exception e) {
			processException(new NotificationException("Could not process notification!", e));
		}
	}

}
