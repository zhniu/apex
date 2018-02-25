/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.restserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;
import com.google.gson.Gson;


public class TestRESTServer {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private final PrintStream stdout = System.out;
    private final PrintStream stderr = System.err;

    private static int eventsSent = 0;
    
    @Test
    public void testRESTServerPut() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEvent.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 20; i++) {
            ThreadUtilities.sleep(100);
            
            Response response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(getEvent()));

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String responseString = response.readEntity(String.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
            assertEquals("com.ericsson.apex.sample.events", jsonMap.get("nameSpace"));
            assertEquals("Test slogan for External Event0", jsonMap.get("TestSlogan"));
        }

        apexMain.shutdown();
    }

    @Test
    public void testRESTServerPost() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEvent.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 20; i++) {
            ThreadUtilities.sleep(100);
            
            Response response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").post(Entity.json(getEvent()));

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String responseString = response.readEntity(String.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
            assertEquals("com.ericsson.apex.sample.events", jsonMap.get("nameSpace"));
            assertEquals("Test slogan for External Event0", jsonMap.get("TestSlogan"));
        }

        apexMain.shutdown();
    }

    @Test
    public void testRESTServerMultiInputs() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventMultiIn.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 20; i++) {
            ThreadUtilities.sleep(100);
            
            Response firstResponse = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").post(Entity.json(getEvent()));

            assertEquals(Response.Status.OK.getStatusCode(), firstResponse.getStatus());
            String firstResponseString = firstResponse.readEntity(String.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> firstJsonMap = new Gson().fromJson(firstResponseString, Map.class);
            assertEquals("com.ericsson.apex.sample.events", firstJsonMap.get("nameSpace"));
            assertEquals("Test slogan for External Event0", firstJsonMap.get("TestSlogan"));
            
            Response secondResponse = client.target("http://localhost:23324/apex/SecondConsumer/EventIn").request("application/json").post(Entity.json(getEvent()));

            assertEquals(Response.Status.OK.getStatusCode(), secondResponse.getStatus());
            String secondResponseString = secondResponse.readEntity(String.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> secondJsonMap = new Gson().fromJson(secondResponseString, Map.class);
            assertEquals("com.ericsson.apex.sample.events", secondJsonMap.get("nameSpace"));
            assertEquals("Test slogan for External Event0", secondJsonMap.get("TestSlogan"));
        }

        apexMain.shutdown();
    }

    @Test
    public void testRESTServerProducerStandalone() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventProducerStandalone.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("the parameters \"host\", \"port\", and \"standalone\" are illegal on REST Server producer"));
    }

    @Test
    public void testRESTServerProducerHost() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventProducerHost.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("  host and port are specified only in standalone mode"));
    }

    @Test
    public void testRESTServerProducerPort() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventProducerPort.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("  host and port are specified only in standalone mode"));
    }

    @Test
    public void testRESTServerConsumerStandaloneNoHost() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventConsumerStandaloneNoHost.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("the parameters \"host\" and \"port\" must be defined for REST Server consumer (FirstConsumer) in standalone mode"));
    }

    @Test
    public void testRESTServerConsumerStandaloneNoPort() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventConsumerStandaloneNoPort.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("the parameters \"host\" and \"port\" must be defined for REST Server consumer (FirstConsumer) in standalone mode"));
    }

    @Test
    public void testRESTServerProducerNotSync() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventProducerNotSync.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("REST Server producer (FirstProducer) must run in synchronous mode with a REST Server consumer"));
    }

    @Test
    public void testRESTServerConsumerNotSync() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventConsumerNotSync.json"};

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(200);
        apexMain.shutdown();
        
        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("event output for peered mode \"SYNCHRONOUS\": peer \"FirstConsumer\" for event handler \"FirstProducer\" does not exist or is not defined as being synchronous"));
    }

    @Test
    public void testRESTServerDivideByZero() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventDivideByZero.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        // Wait for the required amount of events to be received or for 10 seconds
        for (int i = 0; i < 20; i++) {
            ThreadUtilities.sleep(100);
            
            Response response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(getEvent()));

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            String responseString = response.readEntity(String.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = new Gson().fromJson(responseString, Map.class);
            assertEquals("com.ericsson.apex.sample.events", jsonMap.get("nameSpace"));
            assertEquals("Test slogan for External Event0", jsonMap.get("TestSlogan"));
            assertTrue(((String)jsonMap.get("exceptionMessage")).contains("caused by: / by zero"));
            
        }

        
        apexMain.shutdown();
    }

    private String getEvent() {
        Random rand = new Random();
        int nextMatchCase = rand.nextInt(4);
        String nextEventName = "Event0" + rand.nextInt(2) + "00";

        String eventString = 
                "{\n" +
                        "\"nameSpace\": \"com.ericsson.apex.sample.events\",\n" +
                        "\"name\": \"" + nextEventName + "\",\n" +
                        "\"version\": \"0.0.1\",\n" +
                        "\"source\": \"REST_" + eventsSent++ + "\",\n" +
                        "\"target\": \"apex\",\n" +
                        "\"TestSlogan\": \"Test slogan for External Event0\",\n" +
                        "\"TestMatchCase\": " + nextMatchCase + ",\n" +
                        "\"TestTimestamp\": " + System.currentTimeMillis() + ",\n" +
                        "\"TestTemperature\": 9080.866\n" +
                        "}";

        return eventString;
    }
}
