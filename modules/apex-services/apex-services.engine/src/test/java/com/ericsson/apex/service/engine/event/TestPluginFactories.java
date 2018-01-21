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

import static org.junit.Assert.assertNotNull;

import java.util.Map.Entry;

import org.junit.Test;

import com.ericsson.apex.service.engine.event.impl.EventConsumerFactory;
import com.ericsson.apex.service.engine.event.impl.EventProducerFactory;
import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.parameters.ApexParameterException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @author John Keeney (john.keeney@ericsson.com)
 */
public class TestPluginFactories {

    @Test
    public void testEventConsumerFactory() throws ApexEventException, ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/factoryGoodParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

        for (Entry<String, EventHandlerParameters> ce : parameters.getEventInputParameters().entrySet()) {
            ApexEventConsumer consumer = new EventConsumerFactory()
                    .createConsumer(parameters.getEngineServiceParameters().getName() + "_consumer_" + ce.getKey(), ce.getValue());
            assertNotNull(consumer);
        }

        for (Entry<String, EventHandlerParameters> pe : parameters.getEventOutputParameters().entrySet()) {
            ApexEventProducer producer = new EventProducerFactory()
                    .createProducer(parameters.getEngineServiceParameters().getName() + "_producer_" + pe.getKey(), pe.getValue());
            assertNotNull(producer);
        }
    }
}
