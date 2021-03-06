/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restserver;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RestServerEndpoint is the end point servlet class for handling REST requests and responses to and from Apex.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
@Path("/{eventInput}")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class RestServerEndpoint {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerEndpoint.class);

    // Statistics on the amount of HTTP messages handled
    private static int getMessagesReceived = 0;
    private static int postEventMessagesReceived = 0;
    private static int putEventMessagesReceived = 0;

    // This map is used to hold all the REST server event inputs. This is used to determine which consumer to send input events to
    private static Map<String, ApexRestServerConsumer> consumerMap = new LinkedHashMap<String, ApexRestServerConsumer>();

    // The ID of this event input. This gets injected from the URL.
    @PathParam("eventInput")
    private String eventInputID = null;

    /**
     * Register an Apex consumer with the REST server end point.
     * @param consumerEventInputID The event input ID that indicates this consumer shoud be used
     * @param consumer The consumer to register
     */
    public static void registerApexRestServerConsumer(final String consumerEventInputID, final ApexRestServerConsumer consumer) {
        consumerMap.put(consumerEventInputID, consumer);
    }

    /**
     * Get statistics on apex REST event handling.
     *
     * @return the response
     */
    @Path("/Status")
    @GET
    public Response serviceGetStats() {
        getMessagesReceived++;
        return Response.status(Response.Status.OK.getStatusCode()).entity("{\n"
                + "\"INPUTS\": \"" + consumerMap.keySet() + "\",\n"
                + "\"STAT\": " + getMessagesReceived + ",\n"
                + "\"POST\": " + postEventMessagesReceived + ",\n"
                + "\"PUT\":  " + putEventMessagesReceived + "\n}"
                ).build();
    }

    /**
     * Service post request, an incoming event over RETS to Apex.
     *
     * @param jsonString the JSON string containing the data coming in on the REST call
     * @return the response event to the request
     */
    @Path("/EventIn")
    @POST
    public Response servicePostRequest(final String jsonString) {
        postEventMessagesReceived++;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event input " + eventInputID + ", received POST of event \"" + jsonString + "\"");
        }

        // Common handler method for POST and PUT requests
        return handleEvent(jsonString);
    }

    /**
     * Service put request, an incoming event over RETS to Apex.
     *
     * @param jsonString the JSON string containing the data coming in on the REST call
     * @return the response event to the request
     */
    @Path("/EventIn")
    @PUT
    public Response servicePutRequest(final String jsonString) {
        putEventMessagesReceived++;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event input \"" + eventInputID + "\", received PUT of event \"" + jsonString + "\"");
        }

        // Common handler method for POST and PUT requests
        return handleEvent(jsonString);
    }

    /**
     * Common event handler for events received on POST and PUT messages.
     * 
     * @param jsonString the JSON string containing the data coming in on the REST call
     * @return the response event to the request
     */
    private Response handleEvent(final String jsonString) {
        // Find the correct consumer for this REST message
        ApexRestServerConsumer eventConsumer = consumerMap.get(eventInputID);
        if (eventConsumer == null) {
            String errorMessage = "event input " + eventInputID + " is not defined in the Apex configuration file";
            LOGGER.warn(errorMessage);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("{'errorMessage', '" + errorMessage + "'}").build();
        }

        return eventConsumer.receiveEvent(jsonString);
    }
}
