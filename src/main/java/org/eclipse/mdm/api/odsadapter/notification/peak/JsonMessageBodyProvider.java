/*
 * Copyright (c) 2017-2018 Peak Solution GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.notification.peak;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import com.google.protobuf.util.JsonFormat.Printer;

/**
 * MessageBodyProvider for handling json payloads.
 * 
 * @since 1.0.0
 * @author Matthias Koller, Peak Solution GmbH
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JsonMessageBodyProvider implements MessageBodyReader<Message>, MessageBodyWriter<Message> {
	private static final Charset charset = Charsets.UTF_8;
	private Printer jsonPrinter = JsonFormat.printer();
	private Parser jsonParser = JsonFormat.parser();

	public JsonMessageBodyProvider() {

	}

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return Message.class.isAssignableFrom(type);
	}

	@Override
	public Message readFrom(final Class<Message> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
			throws IOException {
		try {
			final Method newBuilder = type.getMethod("newBuilder");
			final GeneratedMessage.Builder<?> builder = (GeneratedMessage.Builder<?>) newBuilder.invoke(type);
			jsonParser.merge(new InputStreamReader(entityStream, charset), builder);
			return builder.build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public long getSize(final Message m, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return Message.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(final Message m, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders,
			final OutputStream entityStream) throws IOException {
		entityStream.write(jsonPrinter.print(m).getBytes(charset));
	}
}
