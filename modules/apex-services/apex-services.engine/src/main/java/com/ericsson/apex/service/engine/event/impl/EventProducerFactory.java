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

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * This factory class creates event producers for the defined technology type for Apex engines.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventProducerFactory {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EventProducerFactory.class);

    /**
     * Empty constructor with no generic overloading.
     */
    public EventProducerFactory() {
    }

    /**
     * Create an event producer of the required type for the specified producer technology.
     *
     * @param name the name of the producer
     * @param producerParameters The Apex parameters containing the configuration for the producer
     * @return the event producer
     * @throws ApexEventException on errors creating the Apex event producer
     */
    public ApexEventProducer createProducer(final String name, final EventHandlerParameters producerParameters) throws ApexEventException {
        // Get the carrier technology parameters
        CarrierTechnologyParameters technologyParameters = producerParameters.getCarrierTechnologyParameters();

        // Get the class for the event producer using reflection
        final String producerPluginClass = technologyParameters.getEventProducerPluginClass();
        Object producerPluginObject = null;
        try {
            producerPluginObject = Class.forName(producerPluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            String errorMessage = "could not create an Apex event producer for Producer \"" + name + "\" for the carrier technology \""
                    + technologyParameters.getLabel() + "\", specified event producer plugin class \"" + producerPluginClass + "\" not found";
            LOGGER.error(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Check the class is an event producer
        if (!(producerPluginObject instanceof ApexEventProducer)) {
            String errorMessage = "could not create an Apex event producer for Producer \"" + name + "\" for the carrier technology \""
                    + technologyParameters.getLabel() + "\", specified event producer plugin class \"" + producerPluginClass + "\" is not an instance of \""
                    + ApexEventProducer.class.getCanonicalName() + "\"";
            LOGGER.error(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        return (ApexEventProducer) producerPluginObject;
    }
}
