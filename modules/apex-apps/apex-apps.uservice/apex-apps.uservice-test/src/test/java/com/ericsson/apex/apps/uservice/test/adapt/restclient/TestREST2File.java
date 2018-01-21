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

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.utilities.TextFileUtils;
import com.ericsson.apex.service.engine.main.ApexMain;


public class TestREST2File {
    
    private static final String BASE_URI = "http://localhost:32801/TestRest2File";
    private HttpServer server;

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private final PrintStream stdout = System.out;
    private final PrintStream stderr = System.err;

    @Before
    public void setUp() throws Exception {
        final ResourceConfig rc = new ResourceConfig(TestEndpoint.class);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);   
        
        while (!server.isStarted()) {
            ThreadUtilities.sleep(50);
        }
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @AfterClass
    public static void deleteTempFiles() {
        new File("src/test/resources/events/EventsOut.json").delete();
    }
    
    @Test
    public void testRESTEventsIn() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/REST2FileJsonEvent.json"};

        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();
        
        String outputEventText = TextFileUtils.getTextFileAsString("src/test/resources/events/EventsOut.json");
        assertTrue(outputEventText.contains("04\",\n" + 
                "  \"version\": \"0.0.1\",\n" + 
                "  \"nameSpace\": \"com.ericsson.apex.sample.events\""));
   }
    
    @Test
    public void testFileEmptyEvents() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/REST2FileJsonEmptyEvents.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("received an enpty event from URL \"http://localhost:32801/TestRest2File/apex/event/GetEmptyEvent\""));
   }

    
    @Test
    public void testFileEventsNoURL() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/REST2FileJsonEventNoURL.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
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

        String[] args = {"src/test/resources/prodcons/REST2FileJsonEventBadURL.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);

        assertTrue(outString.contains("reception of event from URL \"http://localhost:32801/TestRest2File/apex/event/Bad\" failed with status code 404"));
   }

    @Test
    public void testFileEventsBadHTTPMethod() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/REST2FileJsonEventBadHTTPMethod.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("specified HTTP method of \"POST\" is invalid, only HTTP method \"GET\" is supported for event reception on REST client consumer"));
   }

    @Test
    public void testFileEventsBadResponse() throws MessagingException, ApexException, IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String[] args = {"src/test/resources/prodcons/REST2FileJsonEventBadResponse.json"};
        ApexMain apexMain = new ApexMain(args);
        
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();

        String outString = outContent.toString();

        System.setOut(stdout);
        System.setErr(stderr);
        
        assertTrue(outString.contains("reception of event from URL \"http://localhost:32801/TestRest2File/apex/event/GetEventBadResponse\" failed with status code 400 and message \"\""));
   }
}
