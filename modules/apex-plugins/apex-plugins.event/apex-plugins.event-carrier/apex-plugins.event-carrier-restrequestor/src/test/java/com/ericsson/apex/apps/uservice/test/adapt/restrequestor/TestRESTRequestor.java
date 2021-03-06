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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;
import com.google.gson.Gson;

public class TestRESTRequestor {
	private static final String BASE_URI = "http://localhost:32801/TestRESTRequestor";
	private static HttpServer server;

	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	private final PrintStream stdout = System.out;
	private final PrintStream stderr = System.err;

	@BeforeClass
	public static void setUp() throws Exception {
		final ResourceConfig rc = new ResourceConfig(TestRESTREequestorEndpoint.class);
		server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);   

		while (!server.isStarted()) {
			ThreadUtilities.sleep(50);
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.shutdownNow();
		
		new File("src/test/resources/events/EventsOut.json").delete();
		new File("src/test/resources/events/EventsOutMulti0.json").delete();
		new File("src/test/resources/events/EventsOutMulti1.json").delete();
	}

	@Before
	public void resetCounters() {
		TestRESTREequestorEndpoint.resetCounters();
	}

	@Test
	public void testRESTRequestorGet() throws MessagingException, ApexException, IOException {
		Client client = ClientBuilder.newClient();

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FileGet.json"};
		ApexMain apexMain = new ApexMain(args);

		// Wait for the required amount of events to be received or for 10 seconds
		Double getsSoFar = 0.0;
		for (int i = 0; i < 40; i++) {
			ThreadUtilities.sleep(100);

			Response response = client.target("http://localhost:32801/TestRESTRequestor/apex/event/Stats").request("application/json").get();

			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String responseString = response.readEntity(String.class);

			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
			getsSoFar = Double.valueOf(jsonMap.get("GET").toString());

			if (getsSoFar >= 50.0) {
				break;
			}
		}

		apexMain.shutdown();
		client.close();

		assertEquals(Double.valueOf(50.0), getsSoFar);
	}

	@Test
	public void testRESTRequestorPut() throws MessagingException, ApexException, IOException {
		Client client = ClientBuilder.newClient();

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FilePut.json"};
		ApexMain apexMain = new ApexMain(args);

		// Wait for the required amount of events to be received or for 10 seconds
		Double putsSoFar = 0.0;
		for (int i = 0; i < 40; i++) {
			ThreadUtilities.sleep(100);

			Response response = client.target("http://localhost:32801/TestRESTRequestor/apex/event/Stats").request("application/json").get();

			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String responseString = response.readEntity(String.class);

			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
			putsSoFar = Double.valueOf(jsonMap.get("PUT").toString());

			if (putsSoFar >= 50.0) {
				break;
			}
		}

		apexMain.shutdown();
		client.close();

		assertEquals(Double.valueOf(50.0), putsSoFar);
	}

	@Test
	public void testRESTRequestorPost() throws MessagingException, ApexException, IOException {
		Client client = ClientBuilder.newClient();

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FilePost.json"};
		ApexMain apexMain = new ApexMain(args);

		// Wait for the required amount of events to be received or for 10 seconds
		Double postsSoFar = 0.0;
		for (int i = 0; i < 40; i++) {
			ThreadUtilities.sleep(100);

			Response response = client.target("http://localhost:32801/TestRESTRequestor/apex/event/Stats").request("application/json").get();

			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String responseString = response.readEntity(String.class);

			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
			postsSoFar = Double.valueOf(jsonMap.get("POST").toString());

			if (postsSoFar >= 50.0) {
				break;
			}
		}

		apexMain.shutdown();
		client.close();

		assertEquals(Double.valueOf(50.0), postsSoFar);
	}

	@Test
	public void testRESTRequestorDelete() throws MessagingException, ApexException, IOException {
		Client client = ClientBuilder.newClient();

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FileDelete.json"};
		ApexMain apexMain = new ApexMain(args);

		// Wait for the required amount of events to be received or for 10 seconds
		Double deletesSoFar = 0.0;
		for (int i = 0; i < 40; i++) {
			ThreadUtilities.sleep(100);

			Response response = client.target("http://localhost:32801/TestRESTRequestor/apex/event/Stats").request("application/json").get();

			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String responseString = response.readEntity(String.class);

			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
			deletesSoFar = Double.valueOf(jsonMap.get("DELETE").toString());

			if (deletesSoFar >= 50.0) {
				break;
			}
		}

		apexMain.shutdown();
		client.close();

		assertEquals(Double.valueOf(50.0), deletesSoFar);
	}

	@Test
	public void testRESTRequestorMultiInputs() throws MessagingException, ApexException, IOException {
		Client client = ClientBuilder.newClient();

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FileGetMulti.json"};
		ApexMain apexMain = new ApexMain(args);

		// Wait for the required amount of events to be received or for 10 seconds
		Double getsSoFar = 0.0;
		for (int i = 0; i < 40; i++) {
			ThreadUtilities.sleep(100);

			Response response = client.target("http://localhost:32801/TestRESTRequestor/apex/event/Stats").request("application/json").get();

			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String responseString = response.readEntity(String.class);

			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
			getsSoFar = Double.valueOf(jsonMap.get("GET").toString());

			if (getsSoFar >= 8.0) {
				break;
			}
		}

		apexMain.shutdown();
		client.close();

		assertEquals(Double.valueOf(8.0), getsSoFar);

		ThreadUtilities.sleep(1000);
	}

	@Test
	public void testRESTRequestorProducerAlone() throws MessagingException, ApexException, IOException {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FileGetProducerAlone.json"};

		ApexMain apexMain = new ApexMain(args);
		ThreadUtilities.sleep(200);
		apexMain.shutdown();

		String outString = outContent.toString();

		System.setOut(stdout);
		System.setErr(stderr);

		assertTrue(outString.contains("REST Requestor producer (RestRequestorProducer) must run in peered requestor mode with a REST Requestor consumer"));
	}

	@Test
	public void testRESTRequestorConsumerAlone() throws MessagingException, ApexException, IOException {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		String[] args = {"src/test/resources/prodcons/File2RESTRequest2FileGetConsumerAlone.json"};

		ApexMain apexMain = new ApexMain(args);
		ThreadUtilities.sleep(200);
		apexMain.shutdown();

		String outString = outContent.toString();

		System.setOut(stdout);
		System.setErr(stderr);

		assertTrue(outString.contains("event input for peered mode \"REQUESTOR\": peer \"RestRequestorProducer\" for event handler \"RestRequestorConsumer\" does not exist or is not defined as being synchronous"));
	}
}
