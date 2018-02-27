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

import java.util.EnumMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * This class implements an Apex event consumer that receives events from a REST server.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexRestClientConsumer implements ApexEventConsumer, Runnable {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexRestClientConsumer.class);

    // The amount of time to wait in milliseconds between checks that the consumer thread has stopped
    private static final long REST_CLIENT_WAIT_SLEEP_TIME = 50;

    // The REST parameters read from the parameter service
    private RESTClientCarrierTechnologyParameters restConsumerProperties;

    // The event receiver that will receive events from this consumer
    private ApexEventReceiver eventReceiver;

    // The HTTP client that makes a REST call to get an input event for Apex
    private Client client;

    // The name for this consumer
    private String name = null;

    // The peer references for this event handler
    private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

    // The consumer thread and stopping flag
    private Thread consumerThread;
    private boolean stopOrderedFlag = false;

    // The number of events read to date
    private int eventsRead = 0;

    @Override
    public void init(final String consumerName, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
            throws ApexEventException {
        this.eventReceiver = incomingEventReceiver;
        this.name = consumerName;

        // Check and get the REST Properties
        if (!(consumerParameters.getCarrierTechnologyParameters() instanceof RESTClientCarrierTechnologyParameters)) {
            String errorMessage = "specified consumer properties are not applicable to REST client consumer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        restConsumerProperties = (RESTClientCarrierTechnologyParameters) consumerParameters.getCarrierTechnologyParameters();

        // Check if the HTTP method has been set
        if (restConsumerProperties.getHttpMethod() == null) {
            restConsumerProperties.setHttpMethod(RESTClientCarrierTechnologyParameters.CONSUMER_HTTP_METHOD);
        }

        if (!restConsumerProperties.getHttpMethod().equalsIgnoreCase(RESTClientCarrierTechnologyParameters.CONSUMER_HTTP_METHOD)) {
            String errorMessage = "specified HTTP method of \"" + restConsumerProperties.getHttpMethod()
                    + "\" is invalid, only HTTP method \"GET\" is supported for event reception on REST client consumer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Initialize the HTTP client
        client = ClientBuilder.newClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#start()
     */
    @Override
    public void start() {
        // Configure and start the event reception thread
        final String threadName = this.getClass().getName() + ":" + this.name;
        consumerThread = new ApplicationThreadFactory(threadName).newThread(this);
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getName()
     */
    @Override
    public String getName() {
        return name;
    }

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode)
	 */
	@Override
	public PeeredReference getPeeredReference(EventHandlerPeeredMode peeredMode) {
		return peerReferenceMap.get(peeredMode);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#setPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode, com.ericsson.apex.service.engine.event.PeeredReference)
	 */
	@Override
	public void setPeeredReference(EventHandlerPeeredMode peeredMode, PeeredReference peeredReference) {
		peerReferenceMap.put(peeredMode, peeredReference);
	}

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // The RequestRunner thread runs the get request for the event
        Thread requestRunnerThread = null;

        // The endless loop that receives events using REST calls
        while (consumerThread.isAlive() && !stopOrderedFlag) {
            // Create a new request if one is not in progress
            if (requestRunnerThread == null || !requestRunnerThread.isAlive()) {
                requestRunnerThread = new Thread(new RequestRunner());
                requestRunnerThread.start();
            }

            ThreadUtilities.sleep(REST_CLIENT_WAIT_SLEEP_TIME);
        }

        client.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventConsumer#stop()
     */
    @Override
    public void stop() {
        stopOrderedFlag = true;

        while (consumerThread.isAlive()) {
            ThreadUtilities.sleep(REST_CLIENT_WAIT_SLEEP_TIME);
        }
    }

    /**
     * This class is used to start a thread for each request issued.
     * 
     * @author Liam Fallon (liam.fallon@ericsson.com)
     */
    private class RequestRunner implements Runnable {
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                Response response = client.target(restConsumerProperties.getURL()).request("application/json").get();

                // Check that the event request worked
                if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                    String errorMessage = "reception of event from URL \"" + restConsumerProperties.getURL() + "\" failed with status code "
                            + response.getStatus() + " and message \"" + response.readEntity(String.class) + "\"";
                    throw new ApexEventRuntimeException(errorMessage);
                }

                // Get the event we received
                String eventJSONString = response.readEntity(String.class);

                // Check there is content
                if (eventJSONString == null || eventJSONString.trim().length() == 0) {
                    String errorMessage = "received an empty event from URL \"" + restConsumerProperties.getURL() + "\"";
                    throw new ApexEventRuntimeException(errorMessage);
                }

                // Send the event into Apex
                eventReceiver.receiveEvent(null, eventJSONString);
                eventsRead++;
            }
            catch (final Exception e) {
                LOGGER.warn("error receiving events on thread {}", consumerThread.getName(), e);
            }
        }
    }
}
