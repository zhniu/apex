/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventProtocolConverter;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * This factory class uses the Apex event protocol parameters to create and return an instance of the correct Apex event protocol converter plugin for the
 * specified event protocol.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventProtocolFactory {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EventProtocolFactory.class);

    /**
     * Create an event converter that converts between an {@link com.ericsson.apex.service.engine.event.ApexEvent} and the specified event protocol.
     *
     * @param name the name of the event protocol
     * @param eventProtocolParameters the event protocol parameters defining what to convert from and to
     * @return The event converter for converting events to and from Apex format
     */
    public ApexEventProtocolConverter createConverter(final String name, final EventProtocolParameters eventProtocolParameters) {
        // Get the class for the event protocol plugin using reflection
        final String eventProtocolPluginClass = eventProtocolParameters.getEventProtocolPluginClass();
        Object eventProtocolPluginObject = null;
        try {
            eventProtocolPluginObject = Class.forName(eventProtocolPluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            String errorMessage = "could not create an Apex event protocol converter for \"" + name + "\" for the protocol \""
                    + eventProtocolParameters.getLabel() + "\", specified event protocol converter plugin class \"" + eventProtocolPluginClass + "\" not found";
            LOGGER.error(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Check the class is an event consumer
        if (!(eventProtocolPluginObject instanceof ApexEventProtocolConverter)) {
            String errorMessage = "could not create an Apex event protocol converter for \"" + name + "\" for the protocol \""
                    + eventProtocolParameters.getLabel() + "\", specified event protocol converter plugin class \"" + eventProtocolPluginClass
                    + "\" is not an instance of \"" + ApexEventProtocolConverter.class.getCanonicalName() + "\"";
            LOGGER.error(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }
        ((ApexEventProtocolConverter) eventProtocolPluginObject).init(eventProtocolParameters);
        return (ApexEventProtocolConverter) eventProtocolPluginObject;
    }
}
