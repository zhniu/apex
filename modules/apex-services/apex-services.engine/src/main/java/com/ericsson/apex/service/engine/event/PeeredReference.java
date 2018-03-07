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

import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * This class holds a reference to an event consumer and producer that have been peered.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PeeredReference {
    // The consumer putting events into APEX
    private final ApexEventConsumer peeredConsumer;

    // The synchronous producer taking events out of APEX
    private final ApexEventProducer peeredProducer;

    /**
     * Create a peered consumer/producer reference
     * 
     * @param peeredMode the peered mode for which to return the reference
     * @param consumer the consumer that is receiving event
     * @param producer the producer that is sending events
     */
    public PeeredReference(final EventHandlerPeeredMode peeredMode, final ApexEventConsumer consumer, final ApexEventProducer producer) {
        this.peeredConsumer = consumer;
        this.peeredProducer = producer;

        // Set the peered reference on the producer and consumer
        peeredConsumer.setPeeredReference(peeredMode, this);
        peeredProducer.setPeeredReference(peeredMode, this);
    }

    /**
     * Gets the synchronous consumer putting events into the cache.
     *
     * @return the source synchronous consumer
     */
    public ApexEventConsumer getPeeredConsumer() {
        return peeredConsumer;
    }

    /**
     * Gets the synchronous producer taking events from the cache.
     *
     * @return the synchronous producer that is taking events from the cache
     */
    public ApexEventProducer getPeeredProducer() {
        return peeredProducer;
    }
}
