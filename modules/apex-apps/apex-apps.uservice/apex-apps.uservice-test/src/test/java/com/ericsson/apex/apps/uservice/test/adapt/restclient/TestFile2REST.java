/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;
import com.google.gson.Gson;


public class TestFile2REST {
    private static final String BASE_URI = "http://localhost:32801/TestFile2Rest";
    private HttpServer server;

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private final PrintStream stdout = System.out;
    private final PrintStream stderr = System.err;
    
    @Before
    public void setUp() throws Exception {
        final ResourceConfig rc = new ResourceConfig(TestRESTClientEndpoint.class);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);   
        
        while (!server.isStarted()) {
            ThreadUtilities.sleep(50);
        }
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testFileEventsPost() throws MessagingException, ApexException, IOException {
        Client client = ClientBuilder.newClient();

        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventPost.json"};
        ApexMain apexMain = new ApexMain(args);
        
        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 100; i++) {
            ThreadUtilities.sleep(100);
            Response response = client.target("http://localhost:32801/TestFile2Rest/apex/event/Stats").request("application/json").get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String responseString = response.readEntity(String.class);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
            if ((double)jsonMap.get("POST") == 100) {
                break;
            }
        }

        apexMain.shutdown();
    }

    @Test
    public void testFileEventsPut() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventPut.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 100; i++) {
            ThreadUtilities.sleep(100);
            Response response = client.target("http://localhost:32801/TestFile2Rest/apex/event/Stats").request("application/json").get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String responseString = response.readEntity(String.class);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
            if ((double)jsonMap.get("PUT") == 100) {
                break;
            }
        }

        apexMain.shutdown();
    }

    @Test
    public void testFileEventsNoURL() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventNoURL.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(200);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("  no URL has been set for event sending on REST client"));
   }

    @Test
    public void testFileEventsBadURL() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventBadURL.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(200);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);

        assertTrue(outString.contains("send of event to URL \"http://localhost:32801/TestFile2Rest/apex/event/Bad\" using HTTP \"POST\" failed with status code 404"));
   }

    @Test
    public void testFileEventsBadHTTPMethod() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventBadHTTPMethod.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(200);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("specified HTTP method of \"DELETE\" is invalid, only HTTP methods \"POST\" and \"PUT\" are supproted for event sending on REST client producer"));
   }

    @Test
    public void testFileEventsBadResponse() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/File2RESTJsonEventPostBadResponse.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(200);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("send of event to URL \"http://localhost:32801/TestFile2Rest/apex/event/PostEventBadResponse\" using HTTP \"POST\" failed with status code 400"));
   }
}
