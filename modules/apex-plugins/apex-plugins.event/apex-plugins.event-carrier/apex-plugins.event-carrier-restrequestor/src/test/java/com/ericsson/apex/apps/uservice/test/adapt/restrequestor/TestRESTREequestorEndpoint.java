/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.restrequestor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/apex")
public class TestRESTREequestorEndpoint {

	private static Object counterLock = new Object();
	private static int postMessagesReceived = 0;
	private static int putMessagesReceived = 0;
	private static int statMessagesReceived = 0;
	private static int getMessagesReceived = 0;
	private static int deleteMessagesReceived = 0;

	private static String EVENT_STRING = 
			"{\n" +
					"\"nameSpace\": \"com.ericsson.apex.sample.events\",\n" +
					"\"name\": \"Event0100\",\n" +
					"\"version\": \"0.0.1\",\n" +
					"\"source\": \"REST_" + getMessagesReceived + "\",\n" +
					"\"target\": \"apex\",\n" +
					"\"TestSlogan\": \"Test slogan for External Event0\",\n" +
					"\"TestMatchCase\": 2,\n" +
					"\"TestTimestamp\": " + System.currentTimeMillis() + ",\n" +
					"\"TestTemperature\": 9080.866\n" +
					"}";

	public static void resetCounters() {
		postMessagesReceived = 0;
		putMessagesReceived = 0;
		statMessagesReceived = 0;
		getMessagesReceived = 0;
		deleteMessagesReceived = 0;
	}
	
	@Path("/event/Stats")
	@GET
	public Response serviceGetStats() {
		synchronized (counterLock) {
			statMessagesReceived++;
		}
		return Response.status(200).entity("{\"GET\": "
				+ getMessagesReceived
				+ ",\"STAT\": "
				+ statMessagesReceived 
				+ ",\"POST\": "
				+ postMessagesReceived
				+ ",\"PUT\": "
				+ putMessagesReceived
				+ ",\"DELETE\": "
				+ deleteMessagesReceived
				+ "}").build();
	}

	@Path("/event/GetEvent")
	@GET
	public Response serviceGetEvent() {
		synchronized (counterLock) {
			getMessagesReceived++;
		}

		return Response.status(200).entity(EVENT_STRING).build();
	}

	@Path("/event/GetEmptyEvent")
	@GET
	public Response serviceGetEmptyEvent() {
		return Response.status(200).build();
	}

	@Path("/event/GetEventBadResponse")
	@GET
	public Response serviceGetEventBadResponse() {
		return Response.status(400).build();
	}

	@Path("/event/PostEvent")
	@POST
	public Response servicePostRequest(final String jsonString) {
		synchronized (counterLock) {
			postMessagesReceived++;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = new Gson().fromJson(jsonString, Map.class);
		assertTrue(jsonMap.containsKey("name"));
		assertEquals("0.0.1", jsonMap.get("version"));
		assertEquals("com.ericsson.apex.sample.events", jsonMap.get("nameSpace"));
		assertEquals("Act", jsonMap.get("source"));
		assertEquals("Outside", jsonMap.get("target"));

		return Response.status(200).entity(EVENT_STRING).build();
	}

	@Path("/event/PostEventBadResponse")
	@POST
	public Response servicePostRequestBadResponse(final String jsonString) {
		return Response.status(400).build();
	}

	@Path("/event/PutEvent")
	@PUT
	public Response servicePutRequest(final String jsonString) {
		synchronized (counterLock) {
			putMessagesReceived++;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = new Gson().fromJson(jsonString, Map.class);
		assertTrue(jsonMap.containsKey("name"));
		assertEquals("0.0.1", jsonMap.get("version"));
		assertEquals("com.ericsson.apex.sample.events", jsonMap.get("nameSpace"));
		assertEquals("Act", jsonMap.get("source"));
		assertEquals("Outside", jsonMap.get("target"));

		return Response.status(200).entity(EVENT_STRING).build();
	}
	
	@Path("/event/DeleteEvent")
	@DELETE
	public Response serviceDeleteRequest(final String jsonString) {
		synchronized (counterLock) {
			deleteMessagesReceived++;
		}

		return Response.status(200).entity(EVENT_STRING).build();
	}

	@Path("/event/DeleteEventBadResponse")
	@DELETE
	public Response serviceDeleteRequestBadResponse(final String jsonString) {
		return Response.status(400).build();
	}
}
