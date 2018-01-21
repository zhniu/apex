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

/**
 * This interface is used by technology specific consumers and listeners that are are listening for or collecting events for input into Apex.
 * Users specify the consumer technology to use in the Apex configuration and Apex uses a factory to start the appropriate consumer plugin
 * that implements this interface for its input.
 * The technology specific implementation details are hidden behind this interface.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface ApexEventConsumer {
    /**
     * Initialize the consumer.
     *
     * @param name a name for this consumer
     * @param consumerParameters the parameters to initialize this consumer
     * @param apexEventReceiver the apex event receiver that should be used to pass events received by the consumer into Apex
     * @throws ApexEventException container exception on errors initializing event handling
     */
    void init(String name, EventHandlerParameters consumerParameters, ApexEventReceiver apexEventReceiver) throws ApexEventException;

    /**
     * Start the consumer, start input of events into Apex.
     */
    void start();

    /**
     * Get the synchronous event cache to use to cache outstanding synchronous events sent to Apex in synchronous mode.
     * 
     * @return the synchronous event cache to use to cache outstanding synchronous events sent to Apex
     */
    SynchronousEventCache getSynchronousEventCache();

    /**
     * Set the synchronous event cache to use to cache outstanding synchronous events sent to Apex in synchronous mode.
     * 
     * @param synchronousEventCache the synchronous event cache to use to cache outstanding synchronous events sent to Apex
     */
    void setSynchronousEventCache(SynchronousEventCache synchronousEventCache);

    /**
     * Get the name of this event consumer.
     * @return the event consumer name
     */
    String getName();

    /**
     * Stop the event consumer.
     */
    void stop();
}
