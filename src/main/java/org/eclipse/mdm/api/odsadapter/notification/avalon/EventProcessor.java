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

import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationFilter.ModificationType;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNotification._EventType;
import org.omg.CosNotification.StructuredEvent;
import org.omg.CosNotifyChannelAdmin.ClientType;
import org.omg.CosNotifyChannelAdmin.EventChannel;
import org.omg.CosNotifyChannelAdmin.EventChannelHelper;
import org.omg.CosNotifyChannelAdmin.StructuredProxyPullSupplier;
import org.omg.CosNotifyChannelAdmin.StructuredProxyPullSupplierHelper;
import org.omg.CosNotifyComm.InvalidEventType;
import org.omg.CosNotifyComm.StructuredPullConsumerPOA;
import org.omg.CosNotifyFilter.ConstraintExp;
import org.omg.CosNotifyFilter.Filter;
import org.omg.CosNotifyFilter.FilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highqsoft.avalonCorbaNotification.notification.AvalonNotificationCorbaEvent;
import com.highqsoft.avalonCorbaNotification.notification.AvalonNotificationCorbaEventHelper;

/**
 * Event processor responsible for receiving avalon events from the notification
 * service and redirect them to the manager.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class EventProcessor extends StructuredPullConsumerPOA implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

	private static final String eventDomainName = "AVALON";

	private final ORB orb;
	private final NotificationListener listener;
	private final AvalonNotificationManager manager;
	private final String nameserviceUrl;
	private final String serviceName;

	private EventChannel eventChannel;
	private StructuredProxyPullSupplier proxyPullSupplier;

	private boolean connected = false;

	private ScheduledFuture<?> future;

	/**
	 * Creates a new event processor.
	 * 
	 * @param orb
	 *            CORBA orb to use
	 * @param listener
	 *            notification listener consuming the received events
	 * @param manager
	 *            notification manager responsible for processing the events
	 * @param serviceName
	 *            service name of the CORBA notification service
	 */
	public EventProcessor(ORB orb, NotificationListener listener, AvalonNotificationManager manager,
			String nameserviceUrl, String serviceName) {
		this.orb = orb;
		this.nameserviceUrl = nameserviceUrl;
		this.listener = listener;
		this.manager = manager;
		this.serviceName = String.format("com/highqsoft/avalon/notification/%s.Notification", serviceName);
	}

	/**
	 * Connect the event processor to the notification service.
	 * 
	 * @throws NotificationException
	 *             in case the notification service cannot be connected.
	 */
	public synchronized void connect() throws NotificationException {
		if (isConnected()) {
			return;
		}

		try {
			NamingContextExt nc = NamingContextExtHelper.narrow(orb.string_to_object(nameserviceUrl));

			eventChannel = EventChannelHelper.narrow(nc.resolve(nc.to_name(serviceName)));

			proxyPullSupplier = StructuredProxyPullSupplierHelper.narrow(eventChannel.default_consumer_admin()
					.obtain_notification_pull_supplier(ClientType.STRUCTURED_EVENT, new org.omg.CORBA.IntHolder()));

			proxyPullSupplier.connect_structured_pull_consumer(this._this(orb));
			connected = true;
		} catch (Exception e) {
			throw new NotificationException("Cannot connect to notification service!", e);
		}
	}

	/**
	 * Disconnect the event processor from the notification service.
	 */
	public synchronized void disconnect() {
		if (isConnected()) {
			if (future != null) {
				future.cancel(false);
			}

			proxyPullSupplier = null;

			eventChannel._release();
			eventChannel = null;

			connected = false;
		}
	}

	/**
	 * @return true if the event processor is connected to the notification
	 *         service
	 */
	public synchronized boolean isConnected() {
		return connected;
	}

	/**
	 * Sets the event filter.
	 * 
	 * @param aids
	 *            List with application element IDs to filter for. Empty list
	 *            means no all.
	 * @param modificationTypes
	 *            Collection of modification types to filter for.
	 * @throws NotificationException
	 *             if the filter cannot be set
	 */
	public void setFilter(List<String> aids, Set<ModificationType> modificationTypes) throws NotificationException {
		if (!isConnected()) {
			throw new IllegalStateException("Cannot set filter when disconnected. Please connect first.");
		}

		try {
			FilterFactory filterFactory = eventChannel.default_filter_factory();
			if (filterFactory == null) {
				throw new NotificationException("No default filter factory found!");
			}

			Filter filter = filterFactory.create_filter("EXTENDED_TCL");
			filter.add_constraints(new ConstraintExp[] {
					new ConstraintExp(getEventTypes(modificationTypes), getConstraintFilter(aids)) });
			proxyPullSupplier.add_filter(filter);
		} catch (Exception e) {
			throw new NotificationException("Exception when creating filter.", e);
		}
	}

	/**
	 * Sets the ScheduledFuture that will be used to stop the event processor
	 * task.
	 * 
	 * @param future
	 *            ScheduledFuture
	 */
	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	@Override
	public synchronized void run() {
		if (isConnected()) {
			org.omg.CORBA.BooleanHolder bh = new org.omg.CORBA.BooleanHolder();

			try {
				LOGGER.trace("Looking for structured events....");
				// try to pull an event
				StructuredEvent event = proxyPullSupplier.try_pull_structured_event(bh);
				if (bh.value) {
					AvalonNotificationCorbaEvent ev = AvalonNotificationCorbaEventHelper
							.extract(event.remainder_of_body);
					manager.processNotification(ev.mode, ev.aeId, ev.ieId, ev.userId, ev.timestamp, listener);
				} else {
					LOGGER.trace("No structured events found.");
				}
			} catch (Exception e) {
				manager.processException(e);
			}
		} else {
			LOGGER.warn("Disconnected.");
			manager.processException(new NotificationException("Not connected"));
			if (future != null) {
				future.cancel(false);
			}
		}
	}

	@Override
	public void disconnect_structured_pull_consumer() {
		LOGGER.info("Disconnected!");
		connected = false;
	}

	@Override
	public void offer_change(_EventType[] added, _EventType[] removed) throws InvalidEventType {
		// TODO Auto-generated method stub

	}

	/**
	 * Constructs a constraint filter.
	 * 
	 * @param aids
	 *            Application Element IDs used for filtering. Empty list means
	 *            no filter.
	 * @return Constraint filter containing the given aids
	 */
	private String getConstraintFilter(List<String> aids) {
		if (aids.isEmpty()) {
			return "TRUE";
		} else {
			return aids.stream().map(aid -> String.format("$.filterable_data(%s) == %s", "ApplicationElement", aid))
					.collect(Collectors.joining(" or "));
		}
	}

	/**
	 * Converts ModificationTypes in EventTypes.
	 * 
	 * @param modificationTypes
	 * @return Array with EventTypes
	 */
	private _EventType[] getEventTypes(Set<ModificationType> modificationTypes) {
		if (modificationTypes.isEmpty()) {
			return new _EventType[0];
		} else {
			return modificationTypes.stream().map(s -> new _EventType(eventDomainName, toAvalonString(s)))
					.collect(Collectors.toList()).toArray(new _EventType[0]);
		}
	}

	/**
	 * Converts a {@link ModificationType} enum value to a event type name for
	 * the CORBA notification service.
	 * 
	 * @param t
	 *            a modification type
	 * @return event type name
	 */
	private String toAvalonString(ModificationType t) {
		switch (t) {
		case INSTANCE_CREATED:
			return "INSERT";
		case INSTANCE_MODIFIED:
			return "REPLACE";
		case INSTANCE_DELETED:
			return "DELETE";
		case SECURITY_MODIFIED:
			return "MODIFYRIGHTS";
		case MODEL_MODIFIED:
			throw new IllegalArgumentException(t.name() + " not supported!");
		default:
			throw new IllegalArgumentException("Invalid enum value!");
		}
	}
}
