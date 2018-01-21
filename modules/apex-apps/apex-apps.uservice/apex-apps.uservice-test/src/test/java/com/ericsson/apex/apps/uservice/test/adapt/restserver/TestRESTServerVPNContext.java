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

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;


public class TestRESTServerVPNContext {
    private static int eventsSent = 0;
    
    @Ignore
    @Test
    public void testRESTServerPut() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventContextJava.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        Response response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupLinkContext("L09", true)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupLinkContext("L10", true)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("A", "L09 L10", 300,  50)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("B", "L09 L10", 300, 299)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("C", "L09 L10", 300, 300)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("D", "L09 L10", 300, 400)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ThreadUtilities.sleep(100000);
        
        apexMain.shutdown();
    }

    @Ignore
    @Test
    public void testRESTServerPutAvro() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/RESTServerJsonEventContextAvro.json"};
        ApexMain apexMain = new ApexMain(args);

        Client client = ClientBuilder.newClient();

        Response response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupLinkContext("L09", true)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupLinkContext("L10", true)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("A", "L09 L10", 300,  50)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("B", "L09 L10", 300, 299)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("C", "L09 L10", 300, 300)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = client.target("http://localhost:23324/apex/FirstConsumer/EventIn").request("application/json").put(Entity.json(setupCustomerContext("D", "L09 L10", 300, 400)));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ThreadUtilities.sleep(100000);
        
        apexMain.shutdown();
    }

    private String setupLinkContext(final String link, final Boolean isUp) {
        String eventString = 
                "{\n" +
                        "\"nameSpace\": \"com.ericsson.apex.domains.vpn.events\",\n" +
                        "\"name\": \"VPNLinkCtxtTriggerEvent\",\n" +
                        "\"version\": \"0.0.1\",\n" +
                        "\"source\": \"REST_" + eventsSent++ + "\",\n" +
                        "\"target\": \"apex\",\n" +
                        "\"Link\": \"" + link + "\",\n" +
                        "\"LinkUp\": " + isUp + "\n" +
                        "}";

        return eventString;
    }
    
    private String setupCustomerContext(final String customerName, final String linkList, final int slaDT, final int ytdDT) {
        String eventString = 
                "{\n" +
                        "\"nameSpace\": \"com.ericsson.apex.domains.vpn.events\",\n" +
                        "\"name\": \"VPNCustomerCtxtTriggerEvent\",\n" +
                        "\"version\": \"0.0.1\",\n" +
                        "\"source\": \"REST_" + eventsSent++ + "\",\n" +
                        "\"target\": \"apex\",\n" +
                        "\"CustomerName\": \"" + customerName + "\",\n" +
                        "\"LinkList\": \"" + linkList + "\",\n" +
                        "\"SlaDT\": \"" + slaDT + "\",\n" +
                        "\"YtdDT\": " + ytdDT + "\n" +
                        "}";

        return eventString;
    }
}
