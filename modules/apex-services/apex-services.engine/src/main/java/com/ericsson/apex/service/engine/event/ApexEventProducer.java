/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event;

import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * This interface is used by technology specific producers and publishers that are handling events output by Apex. Users specify the producer technology to use
 * in the Apex configuration and Apex uses a factory to start the appropriate producer plugin that implements this interface for its output. The technology
 * specific implementation details are hidden behind this interface.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface ApexEventProducer {

    /**
     * Initialize the producer.
     *
     * @param name a name for this producer
     * @param producerParameters the parameters to initialise this producer
     * @throws ApexEventException exception on errors initializing an event producer
     */
    void init(String name, EventHandlerParameters producerParameters) throws ApexEventException;

    /**
     * Get the peered reference object for this producer.
     * 
     * @param peeredMode the peered mode for which to return the reference
     * @return the peered reference object for this producer
     */
    PeeredReference getPeeredReference(EventHandlerPeeredMode peeredMode);

    /**
     * Set the peered reference object for this producer.
     * 
     * @param peeredMode the peered mode for which to return the reference
     * @param peeredReference the peered reference object for this producer
     */
    void setPeeredReference(EventHandlerPeeredMode peeredMode, PeeredReference peeredReference);

    /**
     * Send an event to the producer.
     *
     * @param executionId the unique ID that produced this event
     * @param eventName The name of the event
     * @param event The converted event as an object
     */
    void sendEvent(long executionId, String eventName, Object event);

    /**
     * Get the name of this event producer.
     * 
     * @return the event producer name
     */
    String getName();

    /**
     * Stop the event producer.
     */
    void stop();
}
