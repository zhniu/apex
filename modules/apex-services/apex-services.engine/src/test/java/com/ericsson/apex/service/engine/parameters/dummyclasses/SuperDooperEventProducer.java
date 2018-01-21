/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * @author John Keeney (john.keeney@ericsson.com)
 */
public class SuperDooperEventProducer implements ApexEventProducer {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SuperDooperEventProducer.class);

    private String name;
    public SuperDooperEventProducer() {
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#init(java.lang.String, com.ericsson.apex.service.parameters.producer.ProducerParameters)
     */
    @Override
    public void init(String name, EventHandlerParameters producerParameters) throws ApexEventException {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getName()
     */
    @Override
    public String getName() {
        return name;
    }


    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getSynchronousEventCache()
     */
    @Override
    public SynchronousEventCache getSynchronousEventCache() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#setSynchronousEventCache(com.ericsson.apex.service.engine.event.SynchronousEventCache)
     */
    @Override
    public void setSynchronousEventCache(SynchronousEventCache synchronousEventCache) {
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#sendEvent(long, java.lang.String, java.lang.Object)
     */
    @Override
    public void sendEvent(long executionId, String eventName, Object event) {
        LOGGER.info("Sending Event: " + this.getClass().getCanonicalName() + ":" + this.name + " ... event (" + eventName + ") : " + event); 
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
    }
}
