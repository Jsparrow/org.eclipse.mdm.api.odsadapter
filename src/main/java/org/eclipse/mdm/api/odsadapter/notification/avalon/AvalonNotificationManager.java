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


package org.eclipse.mdm.api.odsadapter.notification.avalon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.asam.ods.T_LONGLONG;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationFilter.ModificationType;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.eclipse.mdm.api.base.notification.NotificationService;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.notification.NotificationEntityLoader;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highqsoft.avalonCorbaNotification.notification.MODE_DELETE;
import com.highqsoft.avalonCorbaNotification.notification.MODE_INSERT;
import com.highqsoft.avalonCorbaNotification.notification.MODE_MODIFYRIGHTS;
import com.highqsoft.avalonCorbaNotification.notification.MODE_REPLACE;

/**
 * Notification manager for handling notifications from the Avalon Notification
 * Service
 * 
 * ModificationType.MODEL_MODIFIED is not supported!
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class AvalonNotificationManager implements NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AvalonNotificationManager.class);

	private final Map<String, EventProcessor> eventProcessors = new HashMap<>();

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private final ODSModelManager modelManager;
	private final String serviceName;
	private final String nameServiceURL;
	private long pollingInterval = 500L;
	private final NotificationEntityLoader loader;

	private final ORB orb = ORB.init(new String[] {}, System.getProperties());

	/**
	 * Creates a new AvalonNotificationManager.
	 * 
	 * @param modelManager
	 *            ODSModelManager used to laod entities.
	 * @param serviceName
	 *            name of the notification service.
	 * @param nameServiceURL
	 *            URL of the name service.
	 * @param loadContextDescribable
	 *            if true, notifications for {@link ContextRoot} and
	 *            {@link ContextComponent} will load their parent
	 *            {@link ContextDescribable}.
	 * @param pollingInterval
	 *            polling interval in milleseconds
	 */
	public AvalonNotificationManager(ODSModelManager modelManager, QueryService queryService, String serviceName, String nameServiceURL,
			boolean loadContextDescribable, long pollingInterval) {
		this.modelManager = modelManager;
		this.serviceName = serviceName;
		this.nameServiceURL = nameServiceURL;
		this.pollingInterval = pollingInterval;
		loader = new NotificationEntityLoader(modelManager, queryService, loadContextDescribable);
	}

	@Override
	public void register(String registration, NotificationFilter filter, final NotificationListener listener)
			throws NotificationException {
		try {
			EventProcessor consumer = new EventProcessor(orb, listener, this, nameServiceURL, serviceName);

			List<String> aids = filter.getEntityTypes().stream().map(EntityType::getId).collect(Collectors.toList());

			Set<ModificationType> modes = filter.getTypes().stream()
					.filter(m -> !ModificationType.MODEL_MODIFIED.equals(m)).collect(Collectors.toSet());

			consumer.connect();
			consumer.setFilter(aids, modes);

			ScheduledFuture<?> future = executor.scheduleAtFixedRate(consumer, 0, pollingInterval,
					TimeUnit.MILLISECONDS);
			consumer.setFuture(future);

			eventProcessors.put(registration, consumer);
		} catch (Exception e) {
			throw new NotificationException("Exception creating notification listener registration!", e);
		}
	}

	@Override
	public void deregister(String registration) {
		EventProcessor processor = eventProcessors.get(registration);
		if (processor == null) {
			return;
		}
		processor.disconnect();
		eventProcessors.remove(registration);
	}

	@Override
	public void close(boolean isDeregisterAll) throws NotificationException {
		LOGGER.info("Closing NotificationManager...");

		eventProcessors.keySet().forEach(registration -> {
			LOGGER.debug(new StringBuilder().append("Disconnecting registration '").append(registration).append("'.").toString());
			deregister(registration);
		});

		try {
			executor.shutdown();
			boolean terminated = executor.awaitTermination(10, TimeUnit.SECONDS);
			if (!terminated) {
				throw new NotificationException("Could not close all registrations!");
			}
		} catch (InterruptedException e) {
			throw new NotificationException("Could not close all registrations!", e);
		}
	}

	void processException(Exception e) {
		LOGGER.error("Exception during notification processing!", e);
	}

	void processNotification(short mode, T_LONGLONG aeId, T_LONGLONG ieId, T_LONGLONG userId, String timestamp,
			NotificationListener notificationListener) {

		try {
			User user = loader.load(new Key<>(User.class), Long.toString(ODSConverter.fromODSLong(userId)));
			LOGGER.debug("User loaded");

			EntityType entityType = modelManager.getEntityTypeById(Long.toString(ODSConverter.fromODSLong(aeId)));
			List<String> ids = Arrays.asList(Long.toString(ODSConverter.fromODSLong(ieId)));

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace(new StringBuilder().append("Notification event with: entityType=").append(entityType).append(", user=").append(user).toString());
			}

			switch (mode) {
			case MODE_INSERT.value:
				notificationListener.instanceCreated(loader.loadEntities(entityType, ids), user);
				break;
			case MODE_REPLACE.value:
				notificationListener.instanceModified(loader.loadEntities(entityType, ids), user);
				break;
			case MODE_DELETE.value:
				notificationListener.instanceDeleted(entityType, ids, user);
				break;
			case MODE_MODIFYRIGHTS.value:
				notificationListener.securityModified(entityType, ids, user);
				break;
			default:
				processException(new NotificationException("Invalid notification type!"));
			}
		} catch (DataAccessException e) {
			processException(new NotificationException("Cannot load data for notification!", e));
		}
	}
}
