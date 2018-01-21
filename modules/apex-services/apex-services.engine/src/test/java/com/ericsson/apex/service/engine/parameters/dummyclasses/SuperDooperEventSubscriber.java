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

import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

public class SuperDooperEventSubscriber implements ApexEventConsumer {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SuperDooperEventSubscriber.class);

    private String name;

    public SuperDooperEventSubscriber() {
    }

    @Override
    public void init(String name, EventHandlerParameters consumerParameters, ApexEventReceiver apexEventReceiver) throws ApexEventException {
        this.name = name;
        LOGGER.info("Initialising Apex Consumer: " + this.getClass().getCanonicalName() + ":" + this.name ); 
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SynchronousEventCache getSynchronousEventCache() {
        return null;
    }

    @Override
    public void setSynchronousEventCache(SynchronousEventCache synchronousEventCache) {
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
    }

}
