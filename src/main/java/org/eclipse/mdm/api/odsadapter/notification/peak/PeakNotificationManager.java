package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.User;
import org.eclipse.mdm.api.base.notification.NotificationException;
import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationListener;
import org.eclipse.mdm.api.base.notification.NotificationManager;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Join;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.lookup.EntityLoader;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig.Key;
import org.eclipse.mdm.api.odsadapter.query.ODSModelManager;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import com.peaksolution.ods.notification.protobuf.NotificationProtos.Notification;

public class PeakNotificationManager implements NotificationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(PeakNotificationManager.class);

	private final Client client;
	private final WebTarget endpoint;
	
	private final Map<String, EventProcessor> processors = new HashMap<>();
	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	private final EntityLoader loader;
	
	private final MediaType eventMediaType;
	private final ODSModelManager modelManager;
	
	public PeakNotificationManager(ODSModelManager modelManager, String url, String notificationUser, String notficationPassword, String eventMediaType) throws NotificationException {
		this.modelManager = modelManager;
		loader = new EntityLoader(modelManager);
		
		if (Strings.isNullOrEmpty(eventMediaType) || MediaType.APPLICATION_JSON.equalsIgnoreCase(eventMediaType))
		{
			this.eventMediaType = MediaType.APPLICATION_JSON_TYPE;
		}
		else
		{
			this.eventMediaType = ProtobufMessageBodyProvider.APPLICATION_PROTOBUF_TYPE;
		}
		
		client = ClientBuilder.newBuilder()
	    		.register(SseFeature.class)
	    		.register(ProtobufMessageBodyProvider.class)
	    		.register(JsonMessageBodyProvider.class)
	    		.build();
	    
	    endpoint = client.target(url)
				.path("events");
	}
	
	@Override
	public void register(String registration, NotificationFilter filter, NotificationListener listener) throws NotificationException
	{
		Response response = endpoint.path(registration)
			.request()
			.post(javax.ws.rs.client.Entity.entity(ProtobufConverter.from(filter), ProtobufMessageBodyProvider.APPLICATION_PROTOBUF_TYPE));

		if (response.getStatusInfo().getStatusCode() == Status.CONFLICT.getStatusCode())
		{
			LOGGER.info("A registration with the name already exists: " + response.readEntity(String.class));
			LOGGER.info("Trying to reregister...");
			deregister(registration);
			register(registration, filter, listener);
			return;
			// TODO should we automatically deregister and try again or simply throw an exception?
//			throw new NotificationException("A registration with the name already exists: " + response.readEntity(String.class));
		}
		
		if (response.getStatusInfo().getStatusCode() != Status.OK.getStatusCode())
		{
			throw new NotificationException("Could not create registration at notification service: " + response.readEntity(String.class));
		}

		try
		{
			EventInput eventInput = endpoint.path(registration)
					.request()
					.get(EventInput.class);
			
			EventProcessor processor = new EventProcessor(eventInput, listener, this, eventMediaType);
	
			executor.submit(processor);
	
			processors.put(registration, processor);
		}
		catch (Exception e)
		{
			try
			{
				deregister(registration);
			}
			catch (Exception ex)
			{
				LOGGER.error("Exception upon deregistering!");
			}
			throw new NotificationException("Could not create event input stream!", e);
		}
			
	}

	@Override
	public void deregister(String registration)
	{
		if (processors.containsKey(registration))
		{
			EventProcessor processor = processors.get(registration);
			processor.stop();
			processors.remove(registration);	
		}
		
		endpoint.path(registration)
			.request()
			.delete();
	}
	
	void processException(Exception e)
	{
		LOGGER.error("Exception during notification processing!", e);
	}
	
	void processNotification(Notification n, NotificationListener notificationListener) {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Processing notification event COMPILING WORKS: " + n);
		}
		
		EntityType entityType = modelManager.getEntityType(n.getAid());
		LOGGER.debug("Entity type resolved");

		
		try {
			User user = loader.load(new Key<>(User.class), n.getUserId());
			LOGGER.debug("User loaded");


			List<? extends Entity> entities = loadEntities(entityType, n.getIidList());
			LOGGER.debug(entities.size() + " entities found: " + entities);

			switch (n.getType())
			{
			case NEW:
				notificationListener.instanceCreated(entities, user);
				break;
			case MODIFY:
				notificationListener.instanceModified(entities, user);
				break;
			case DELETE:
				notificationListener.instanceDeleted(entities, user);
				break;
			case MODEL:
				notificationListener.modelModified(entityType, user);
				break;
			case SECURITY:
				notificationListener.securityModified(entities, user);
				break;
			default:
				processException(new NotificationException("Invalid notification type!"));
			}
		} catch (Exception e) {
			processException(new NotificationException("Cannot load data for notification!", e));
		}
	}

	public List<? extends Entity> loadEntities(EntityType entityType, List<Long> ids) throws DataAccessException {
		
		if (ids.isEmpty())
		{
			return Collections.emptyList();
		}
		
		EntityConfig<?> config = ((ODSModelManager) modelManager).getEntityConfig(entityType);
		
		Class<? extends Entity> entityClazz = config.getEntityClass();
		
		if (entityClazz == null)
		{
			final EntityType testStep = modelManager.getEntityType(TestStep.class);
			final EntityType measurement = modelManager.getEntityType(Measurement.class);
			final EntityConfig<?> testStepConfig = ((ODSModelManager) modelManager).getEntityConfig(testStep);
			final EntityConfig<?> measurementConfig = ((ODSModelManager) modelManager).getEntityConfig(measurement);
			
			if (hasRelationTo(entityType, testStep, measurement))
			{
				// entityType is a ContextRoot
				EntityType contextRoot = entityType;

				List<Long> testStepIDs = modelManager.createQuery().selectID(testStep)
						.join(testStep.getRelation(contextRoot), Join.OUTER)
						.fetch(Filter.and().add(Operation.IN_SET.create(entityType.getIDAttribute(), Longs.toArray(ids))))
						.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());

				List<Long> measurementIDs = modelManager.createQuery().selectID(measurement)
						.join(testStep.getRelation(contextRoot), Join.OUTER)
						.fetch(Filter.and().add(Operation.IN_SET.create(entityType.getIDAttribute(), Longs.toArray(ids))))
						.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());
				
				List<Entity> list = new ArrayList<>();
				list.addAll(loader.loadAll(testStepConfig.getKey(), testStepIDs));
				list.addAll(loader.loadAll(measurementConfig.getKey(), measurementIDs));
				
				return list;
				
			}
			else 
			{
				List<Relation> toParent = entityType.getParentRelations();
				List<String> contextRoots = Arrays.asList("UnitUnderTest", "TestSequence", "TestEquipment");

				if (!toParent.isEmpty() && contextRoots.contains(toParent.get(0).getTarget().getName()))
				{
					EntityType contextRoot = toParent.get(0).getTarget();
					EntityType contextComponent = entityType;
					
					// entity is a ContextComponent
					
					List<Long> testStepIDs = modelManager.createQuery().selectID(testStep)
							.join(testStep.getRelation(contextRoot), Join.OUTER)
							.join(contextRoot.getRelation(contextComponent), Join.OUTER)
							.fetch(Filter.and().add(Operation.IN_SET.create(entityType.getIDAttribute(), Longs.toArray(ids))))
							.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());
					
					List<Long> measurementIDs = modelManager.createQuery().selectID(measurement)
							.join(measurement.getRelation(contextRoot), Join.OUTER)
							.join(contextRoot.getRelation(contextComponent), Join.OUTER)
							.fetch(Filter.and().add(Operation.IN_SET.create(entityType.getIDAttribute(), Longs.toArray(ids))))
							.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());
					
					List<Entity> list = new ArrayList<>();
					list.addAll(loader.loadAll(testStepConfig.getKey(), testStepIDs));
					list.addAll(loader.loadAll(measurementConfig.getKey(), measurementIDs));
					return list;
				}
				else
				{
					return Collections.emptyList();
				}
			}
		}
		else
		{
			return loader.loadAll(config.getKey(), ids);
		}
	}

	private boolean hasRelationTo(EntityType sourceEntityType, EntityType... targetEntityTypes) {
		for (EntityType e : targetEntityTypes)
		{
			try
			{
				sourceEntityType.getRelation(e);
				return true;
			}
			catch (IllegalArgumentException ex)
			{
				return false;
			}
		}

		return false;
	}
	
}
