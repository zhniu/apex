/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * Concrete implementation of an Apex event producer that sends events using REST.
 * 
 * @author Joss Armstrong (joss.armstrong@ericsson.com)
 * 
 */
public class ApexRestClientProducer implements ApexEventProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexRestClientProducer.class);

    // The HTTP client that makes a REST call with an event from Apex
    private Client client;

    // The REST carrier properties
    private RESTClientCarrierTechnologyParameters restProducerProperties;

    // The name for this producer
    private String name = null;

    // The event cache managing outstanding events
    private SynchronousEventCache synchronousEventCache;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#init(java.lang.String,
     * com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters)
     */
    @Override
    public void init(final String producerName, final EventHandlerParameters producerParameters) throws ApexEventException {
        this.name = producerName;

        // Check and get the REST Properties
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof RESTClientCarrierTechnologyParameters)) {
            String errorMessage = "specified consumer properties are not applicable to REST client producer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        restProducerProperties = (RESTClientCarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();

        // Check if the HTTP method has been set
        if (restProducerProperties.getHttpMethod() == null) {
            restProducerProperties.setHttpMethod(RESTClientCarrierTechnologyParameters.DEFAULT_PRODUCER_HTTP_METHOD);
        }

        if (!restProducerProperties.getHttpMethod().equalsIgnoreCase("POST") && !restProducerProperties.getHttpMethod().equalsIgnoreCase("PUT")) {
            String errorMessage = "specified HTTP method of \"" + restProducerProperties.getHttpMethod()
            + "\" is invalid, only HTTP methods \"POST\" and \"PUT\" are supproted for event sending on REST client producer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Initialize the HTTP client
        client = ClientBuilder.newClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getSynchronousEventCache()
     */
    @Override
    public SynchronousEventCache getSynchronousEventCache() {
        return synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#setSynchronousEventCache(com.ericsson.apex.service.engine.event.SynchronousEventCache)
     */
    @Override
    public void setSynchronousEventCache(final SynchronousEventCache synchronousEventCache) {
        this.synchronousEventCache = synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#sendEvent(long, java.lang. String, java.lang.Object)
     */
    @Override
    public void sendEvent(final long executionId, final String eventName, final Object event) {
        // Check if this is a synchronized event, if so we have received a reply
        if (synchronousEventCache != null) {
            synchronousEventCache.removeCachedEventToApexIfExists(executionId);
        }

        // Send the event as a REST request
        Response response = sendEventAsRESTRequest((String) event);

        // Check that the request worked
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            String errorMessage = "send of event to URL \"" + restProducerProperties.getURL() + "\" using HTTP \""
                    + restProducerProperties.getHttpMethod() + "\" failed with status code " + response.getStatus() + " and message \""
                    + response.readEntity(String.class) + "\", event:\n" + event;
            LOGGER.warn(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("event sent from engine using {} to URL {} with HTTP {} : {} ", this.name, restProducerProperties.getURL(),
                    restProducerProperties.getHttpMethod(), event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        // Close the HTTP session
        client.close();
    }

    /**
     * Send the event as a JSON string as a REST request.
     *
     * @param event the event to send
     * @return the response tot he JSON request
     */
    public Response sendEventAsRESTRequest(final String event) {
        // We have already checked that it is a PUT or POST request
        if (restProducerProperties.getHttpMethod().equalsIgnoreCase("POST")) {
            return client.target(restProducerProperties.getURL()).request("application/json").post(Entity.json(event));
        }
        else {
            return client.target(restProducerProperties.getURL()).request("application/json").put(Entity.json(event));
        }
    }
}
