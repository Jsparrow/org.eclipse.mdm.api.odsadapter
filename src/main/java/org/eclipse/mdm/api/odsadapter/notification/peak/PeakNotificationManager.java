package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.util.ArrayList;
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

import org.eclipse.mdm.api.base.model.ContextComponent;
import org.eclipse.mdm.api.base.model.ContextDescribable;
import org.eclipse.mdm.api.base.model.ContextRoot;
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

/**
 * Notification manager for handling notifications form the Peak ODS Server Notification Plugin
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
	
	private final EntityLoader loader;
	
	private final MediaType eventMediaType;
	private final ODSModelManager modelManager;

	private boolean loadContextDescribable;
	
	/**
	 * @param modelManager
	 * @param url URL of the notification plugin
	 * @param eventMediaType MediaType to use.
	 * @param loadContextDescribable if true, the corresponding context describable is loaded if a notification for a context root or context component is received. 
	 * @throws NotificationException Thrown if the manager cannot connect to the notification server.
	 */
	public PeakNotificationManager(ODSModelManager modelManager, String url, String eventMediaType, boolean loadContextDescribable) throws NotificationException {
		this.modelManager = modelManager;
		this.loadContextDescribable = loadContextDescribable;
		
		try
		{
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
		catch (Exception e)
		{
			throw new NotificationException("Could not create " + PeakNotificationManager.class.getName() + "!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mdm.api.base.notification.NotificationManager#register(java.lang.String, org.eclipse.mdm.api.base.notification.NotificationFilter, org.eclipse.mdm.api.base.notification.NotificationListener)
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.mdm.api.base.notification.NotificationManager#deregister(java.lang.String)
	 */
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
	
	/**
	 * Handler for Exceptions during event processing.
	 * @param e Exception which occured during event processing.
	 */
	void processException(Exception e)
	{
		LOGGER.error("Exception during notification processing!", e);
	}
	
	/**
	 * Handler for notifications.
	 * @param n notification to process.
	 * @param notificationListener notification listener for handling the notification.
	 */
	void processNotification(Notification n, NotificationListener notificationListener) {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Processing notification event: " + n);
		}
		
		try {
			User user = loader.load(new Key<>(User.class), n.getUserId());

			EntityType entityType = modelManager.getEntityType(n.getAid());
			List<? extends Entity> entities = loadEntities(entityType, n.getIidList());; 

			if (LOGGER.isTraceEnabled())
			{
				LOGGER.trace("Notification event with: entityType=" + entityType + ", entities="  + entities + ", user=" + user);
			}
			
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
			processException(new NotificationException("Could not process notification!", e));
		}
	}

	/**
	 * @param entityType entity type of the entities to load.
	 * @param ids IDs of the entities to load.
	 * @return loaded entities.
	 * @throws DataAccessException Throw if the entities cannot be loaded.
	 */
	private List<? extends Entity> loadEntities(EntityType entityType, List<Long> ids) throws DataAccessException {
		
		if (ids.isEmpty())
		{
			return Collections.emptyList();
		}
		
		EntityConfig<?> config = getEntityConfig(entityType);
		
		if (config == null || isLoadContextDescribable(config))
		{
			// entityType not modelled in MDM, try to load its ContextDescribable if it is a ContextRoot/ContextComponent
			final EntityType testStep = modelManager.getEntityType(TestStep.class);
			final EntityType measurement = modelManager.getEntityType(Measurement.class);
			
			if (hasRelationTo(entityType, testStep, measurement))
			{
				return loadEntityForContextRoot(entityType, ids);
			}
			else if (hasRelationTo(entityType, 
					modelManager.getEntityType("UnitUnderTest"), 
					modelManager.getEntityType("TestSequence"), 
					modelManager.getEntityType("TestEquipment")))
			{
				return loadEntityForContextComponent(entityType, ids);
			}
			else
			{
				LOGGER.debug("Cannot load entitis for entityType " + entityType + " and ids " + ids);
				return Collections.emptyList();
			}
		}
		else
		{
			return loader.loadAll(config.getKey(), ids);
		}
	}

	/**
	 * Loads the ContextDescribables to the given context root instances
	 * @param contextRoot entityType of the context root
	 * @param ids IDs of the context roots.
	 * @return the loaded ContextDescribables
	 * @throws DataAccessException Throw if the ContextDescribables cannot be loaded.
	 */
	private List<ContextDescribable> loadEntityForContextRoot(EntityType contextRoot, List<Long> ids) throws DataAccessException {
	
		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);
	
		List<Long> testStepIDs = modelManager.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextRoot.getIDAttribute(), Longs.toArray(ids))))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());
	
		List<Long> measurementIDs = modelManager.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextRoot.getIDAttribute(), Longs.toArray(ids))))
				.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());
		
		List<ContextDescribable> list = new ArrayList<>();
		list.addAll(loader.loadAll(new Key<>(TestStep.class), testStepIDs));
		list.addAll(loader.loadAll(new Key<>(Measurement.class), measurementIDs));
		
		return list;
	}

	/**
	 * Loads the ContextDescribables to the given context component instances
	 * @param contextComponent entityType of the context component
	 * @param ids IDs of the contextComponents to load.
	 * @return the loaded ContextDescribables
	 * @throws DataAccessException Throw if the ContextDescribables cannot be loaded.
	 */
	private List<ContextDescribable> loadEntityForContextComponent(EntityType contextComponent, List<Long> ids) throws DataAccessException {
		
		// ContextComponent can only have one parent
		final EntityType contextRoot = contextComponent.getParentRelations().get(0).getTarget();

		final EntityType testStep = modelManager.getEntityType(TestStep.class);
		final EntityType measurement = modelManager.getEntityType(Measurement.class);
		
		List<Long> testStepIDs = modelManager.createQuery().selectID(testStep)
				.join(testStep.getRelation(contextRoot), Join.OUTER)
				.join(contextRoot.getRelation(contextComponent), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextComponent.getIDAttribute(), Longs.toArray(ids))))
				.stream().map(r -> r.getRecord(testStep)).map(Record::getID).collect(Collectors.toList());
		
		List<Long> measurementIDs = modelManager.createQuery().selectID(measurement)
				.join(measurement.getRelation(contextRoot), Join.OUTER)
				.join(contextRoot.getRelation(contextComponent), Join.OUTER)
				.fetch(Filter.and().add(Operation.IN_SET.create(contextComponent.getIDAttribute(), Longs.toArray(ids))))
				.stream().map(r -> r.getRecord(measurement)).map(Record::getID).collect(Collectors.toList());
		
		List<ContextDescribable> list = new ArrayList<>();
		list.addAll(loader.loadAll(new Key<>(TestStep.class), testStepIDs));
		list.addAll(loader.loadAll(new Key<>(Measurement.class), measurementIDs));
		return list;
	}

	/**
	 * @param entityConfig
	 * @return true, if the entityConfig belongs to a context root or context component and the option loadContextDescribable
	 */
	private boolean isLoadContextDescribable(EntityConfig<?> entityConfig) {
		return loadContextDescribable && (entityConfig.getEntityClass().isAssignableFrom(ContextRoot.class) || entityConfig.getEntityClass().isAssignableFrom(ContextComponent.class));
	}

	/**
	 * Checks if a relation between sourceEntityType and at least one entity type in targetEntityType exists.
	 * @param sourceEntityType source entity type.
	 * @param targetEntityTypes list of target enitity types.
	 * @return true, if relation between source entity type and at least one target entity type exists.
	 */
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

	/**
	 * @param entityType entity type the {@link EntityConfig} is requested for
	 * @return {@link EntityConfig} or null if not config was found for the specified entity type
	 */
	private EntityConfig<?> getEntityConfig(EntityType entityType) {
		try
		{
			 return modelManager.getEntityConfig(entityType);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
	
}
