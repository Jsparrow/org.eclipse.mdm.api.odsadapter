package org.eclipse.mdm.api.odsadapter.notification.peak;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;

import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peaksolution.ods.notification.protobuf.NotificationProtos.Notification;

/**
 * Event processor responsible for receiving notification events from the notification server and redirect them to the manager.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class EventProcessor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

	private EventInput eventInput;
	private NotificationListener listener; 
	private PeakNotificationManager odsNotificationManager;
	private MediaType eventMediaType;
	private boolean closeInvoked = false;
	
	public EventProcessor(EventInput eventInput, NotificationListener listener, PeakNotificationManager odsNotificationManager, MediaType eventMediaType) {
		this.eventInput = eventInput;
		this.listener = listener;
		this.odsNotificationManager = odsNotificationManager;
		this.eventMediaType = eventMediaType;
	}
	
	@Override
	public void run() {

		while (!eventInput.isClosed()) {

			final InboundEvent inboundEvent = eventInput.read();

			if (inboundEvent == null) {
				if (!closeInvoked)
				{
					odsNotificationManager.processException(new NotificationException("Inbound event input stream closed!"));
				}
				return;
			}

			try
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.trace("Received event: " + inboundEvent);
				}
				Notification n = inboundEvent.readData(Notification.class, eventMediaType);
				odsNotificationManager.processNotification(n, getListener());
			}
			catch (ProcessingException e)
			{
				odsNotificationManager.processException(new NotificationException("Cannot deserialize notification event!", e));
				return;
			}
		}
	}

	public void stop() {
		closeInvoked = true;
		// EventInput is closed by the server side after invoking DELETE /events/{registrationName}. Otherwise we run into a deadlock with eventInput#read()
	}
	
	public NotificationListener getListener() {
		return listener;
	}	
}
