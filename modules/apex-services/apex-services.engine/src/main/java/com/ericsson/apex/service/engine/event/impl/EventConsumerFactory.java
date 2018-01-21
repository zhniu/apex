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

import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * This factory class creates event consumers of various technology types for Apex engines.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventConsumerFactory {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EventConsumerFactory.class);

    /**
     * Empty constructor with no generic overloading.
     */
    public EventConsumerFactory() {
    }

    /**
     * Create an event consumer of the required type for the specified consumer technology.
     *
     * @param name the name of the consumer
     * @param consumerParameters The parameters for the Apex engine, we use the technology type of the required consumer
     * @return the event consumer
     * @throws ApexEventException on errors creating the Apex event consumer
     */
    public ApexEventConsumer createConsumer(final String name, final EventHandlerParameters consumerParameters) throws ApexEventException {
        // Get the carrier technology parameters
        CarrierTechnologyParameters technologyParameters = consumerParameters.getCarrierTechnologyParameters();

        // Get the class for the event consumer using reflection
        final String consumerPluginClass = technologyParameters.getEventConsumerPluginClass();
        Object consumerPluginObject = null;
        try {
            consumerPluginObject = Class.forName(consumerPluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            String errorMessage = "could not create an Apex event consumer for \"" + name + "\" for the carrier technology \"" + technologyParameters.getLabel()
                    + "\", specified event consumer plugin class \"" + consumerPluginClass + "\" not found";
            LOGGER.error(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Check the class is an event consumer
        if (!(consumerPluginObject instanceof ApexEventConsumer)) {
            String errorMessage = "could not create an Apex event consumer \"" + name + "\" for the carrier technology \"" + technologyParameters.getLabel()
                    + "\", specified event consumer plugin class \"" + consumerPluginClass + "\" is not an instance of \""
                    + ApexEventConsumer.class.getCanonicalName() + "\"";
            LOGGER.error(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        return (ApexEventConsumer) consumerPluginObject;
    }
}
