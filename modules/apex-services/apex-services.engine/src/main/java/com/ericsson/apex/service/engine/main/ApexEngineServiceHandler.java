/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.main;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.engdep.EngDepMessagingService;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.runtime.EngineService;

/**
 * The Class ApexEngineServiceHandler holds the reference to the Apex engine service and the EngDep service for that engine. It
 * also acts as an event receiver for asynchronous and synchronous events.
 */
public class ApexEngineServiceHandler {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexEngineServiceHandler.class);

    // The Apex engine service, the Apex engine itself
    private final EngineService apexEngineService;

    // The interface between the Apex engine and Apex policy deployment for the Apex engine
    private final EngDepMessagingService engDepService;

    /**
     * Instantiates a new engine holder with its engine service and EngDep service.
     *
     * @param apexEngineService the apex engine service
     * @param engDepService the EngDep service
     */
    ApexEngineServiceHandler(final EngineService apexEngineService, final EngDepMessagingService engDepService) {
        this.apexEngineService = apexEngineService;
        this.engDepService = engDepService;
    }

    /**
     * This method forwards an event to the Apex service.
     * @param apexEvent The event to forward to Apex
     */
    public void forwardEvent(final ApexEvent apexEvent) {
        try {
            // Send the event to the engine runtime
            apexEngineService.getEngineServiceEventInterface().sendEvent(apexEvent);
        }
        catch (Exception e) {
            String errorMessage = "error transferring event \"" + apexEvent.getName() + "\" to the Apex engine";
            LOGGER.debug(errorMessage, e);
            throw new ApexActivatorRuntimeException(errorMessage, e);
        }
    }

    /**
     * Terminate the Apex engine.
     *
     * @throws ApexException on termination errors
     */
    public void terminate() throws ApexException {
        // Shut down engine management
        if (engDepService != null) {
            engDepService.stop();
        }

        // Shut down each engine instance
        if (apexEngineService != null) {
            apexEngineService.stop();
        }
    }
}
