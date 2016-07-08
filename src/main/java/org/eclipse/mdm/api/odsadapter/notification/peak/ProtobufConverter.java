package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationFilter.ModificationType;

import com.peaksolution.ods.notification.protobuf.NotificationProtos.Registration;
import com.peaksolution.ods.notification.protobuf.NotificationProtos.Registration.NotificationMode;

public class ProtobufConverter {
	
	public static Registration from(NotificationFilter filter)
	{
		return Registration.newBuilder()
				.setMode(NotificationMode.PUSH)
				.addAllAid(filter.getEntityTypes().stream().map(e -> e.getId()).collect(Collectors.toList()))
				.addAllType(filter.getTypes().stream().map(t -> ProtobufConverter.from(t)).collect(Collectors.toList()))
				.build();
	}
	
	public static com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType from(ModificationType t)
	{
		switch (t)
		{
			case INSTANCE_CREATED:
				return com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType.NEW;
			case INSTANCE_MODIFIED:
				return com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType.MODIFY;
			case INSTANCE_DELETED:
				return com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType.DELETE;
			case MODEL_MODIFIED: 
				return com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType.MODEL;
			case SECURITY_MODIFIED: 
				return com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType.SECURITY;
			default:
				throw new IllegalArgumentException("Invalid enum value!"); // TODO 
		}
	}
	
	public static ModificationType to(com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType t)
	{
		switch (t)
		{
			case NEW:
				return ModificationType.INSTANCE_CREATED;
			case MODIFY:
				return ModificationType.INSTANCE_MODIFIED;
			case DELETE:
				return ModificationType.INSTANCE_DELETED;
			case MODEL: 
				return ModificationType.MODEL_MODIFIED;
			case SECURITY: 
				return ModificationType.SECURITY_MODIFIED;
			default:
				throw new IllegalArgumentException("Invalid enum value!"); // TODO 
		}
	}
}
