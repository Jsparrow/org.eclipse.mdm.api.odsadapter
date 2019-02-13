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


package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.notification.NotificationFilter;
import org.eclipse.mdm.api.base.notification.NotificationFilter.ModificationType;

import com.peaksolution.ods.notification.protobuf.NotificationProtos.Registration;
import com.peaksolution.ods.notification.protobuf.NotificationProtos.Registration.NotificationMode;

/**
 * Helper class for converting between protobuf and mdm types.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
public class ProtobufConverter {

	/**
	 * Convert a notification filter to a registration.
	 * 
	 * @param filter
	 *            notification filter.
	 * @return registration corresponding to the given filter.
	 */
	public static Registration from(NotificationFilter filter) {
		return Registration.newBuilder().setMode(NotificationMode.PUSH)
				.addAllAid(
						filter.getEntityTypes().stream().map(e -> Long.valueOf(e.getId())).collect(Collectors.toList()))
				.addAllType(filter.getTypes().stream().map(ProtobufConverter::from).collect(Collectors.toList()))
				.build();
	}

	/**
	 * @param t
	 *            mdm modification type.
	 * @return protobuf notification type.
	 */
	public static com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType from(
			ModificationType t) {
		switch (t) {
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

	/**
	 * @param t
	 *            protobuf notification type
	 * @return mdm notification type
	 */
	public static ModificationType to(
			com.peaksolution.ods.notification.protobuf.NotificationProtos.ModificationType t) {
		switch (t) {
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
