/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.enevent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventConverter;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;

/**
 * The Class ApexEvent2EnEventConverter converts externally facing {@link ApexEvent} instances to and from instances of {@link EnEvent} that are used internally
 * in the Apex engine core.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class ApexEvent2EnEventConverter implements ApexEventConverter {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexEvent2EnEventConverter.class);

    // The Apex engine with its event definitions
    private final ApexEngine apexEngine;

    /**
     * Set up the event converter.
     *
     * @param apexEngine The engine to use to create events to be converted
     */
    public ApexEvent2EnEventConverter(final ApexEngine apexEngine) {
        this.apexEngine = apexEngine;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.Object)
     */
    @Override
    public List<ApexEvent> toApexEvent(final Object event) throws ApexException {
        // Check the Engine event
        if (event == null) {
            LOGGER.warn("event processing failed, engine event is null");
            throw new ApexEventException("event processing failed, engine event is null");
        }

        // Cast the event to an Engine event event, if our conversion is correctly configured, this cast should always work
        EnEvent enEvent = null;
        try {
            enEvent = (EnEvent) event;
        }
        catch (Exception e) {
            String errorMessage = "error transferring event \"" + event + "\" to the Apex engine";
            LOGGER.debug(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Create the Apex event
        final AxEvent axEvent = enEvent.getAxEvent();
        final ApexEvent apexEvent = new ApexEvent(axEvent.getKey().getName(), axEvent.getKey().getVersion(), axEvent.getNameSpace(), axEvent.getSource(),
                axEvent.getTarget());

        // Copy the ExecutionID from the EnEvent into the ApexEvent
        apexEvent.setExecutionID(enEvent.getExecutionID());

        // Copy he exception message to the Apex event if it is set
        if (enEvent.getExceptionMessage() != null) {
            apexEvent.setExceptionMessage(enEvent.getExceptionMessage());
        }

        // Set the data on the apex event
        apexEvent.putAll(enEvent);

        // Return the event in a single element
        ArrayList<ApexEvent> eventList = new ArrayList<ApexEvent>();
        eventList.add(apexEvent);
        return eventList;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#fromApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
     */
    @Override
    public EnEvent fromApexEvent(final ApexEvent apexEvent) throws ApexException {
        // Check the Apex model
        if (apexEngine == null) {
            LOGGER.warn("event processing failed, apex engine is null");
            throw new ApexEventException("event processing failed, apex engine is null");
        }

        // Get the event definition
        final AxEvent eventDefinition = ModelService.getModel(AxEvents.class).get(apexEvent.getName());
        if (eventDefinition == null) {
            LOGGER.warn("event processing failed, event \"" + apexEvent.getName() + "\" not found in apex model");
            throw new ApexEventException("event processing failed, event \"" + apexEvent.getName() + "\" not found in apex model");
        }

        // Create the internal engine event
        final EnEvent enEvent = apexEngine.createEvent(eventDefinition.getKey());

        // Set the data on the engine event
        enEvent.putAll(apexEvent);

        //copy the ExecutionID from the ApexEvent into the EnEvent
        enEvent.setExecutionID(apexEvent.getExecutionID());

        return enEvent;
    }
}
