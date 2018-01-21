/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.filecarrierplugin.producer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.FILECarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * Concrete implementation of an Apex event producer that sends events to a file.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexFileEventProducer implements ApexEventProducer {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexFileEventProducer.class);

    // The name for this producer
    private String producerName = null;

    // The output stream to write events to
    private PrintStream eventOutputStream;

    // The event cache managing outstanding events
    private SynchronousEventCache synchronousEventCache;

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#init()
     */
    @Override
    public void init(final String name, final EventHandlerParameters producerParameters) throws ApexEventException {
        producerName = name;

        // Get and check the Apex parameters from the parameter service
        if (producerParameters == null) {
            final String errorMessage = "Producer parameters for ApexFileProducer \"" + producerName + "\" is null";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Check and get the file Properties
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof FILECarrierTechnologyParameters)) {
            final String errorMessage = "specified producer properties for ApexFileProducer \"" + producerName + "\" are not applicable to a FILE producer";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        final FILECarrierTechnologyParameters fileCarrierTechnologyParameters = (FILECarrierTechnologyParameters) producerParameters
                .getCarrierTechnologyParameters();

        // Now we create a writer for events
        try {
            if (fileCarrierTechnologyParameters.isStandardError()) {
                eventOutputStream = System.err;
            }
            else if (fileCarrierTechnologyParameters.isStandardIO()) {
                eventOutputStream = System.out;
            }
            else {
                eventOutputStream = new PrintStream(new FileOutputStream(fileCarrierTechnologyParameters.getFileName()), true);
            }
        }
        catch (final IOException e) {
            final String errorMessage = "ApexFileProducer \"" + producerName + "\" failed to open file for writing: \""
                    + fileCarrierTechnologyParameters.getFileName() + "\"";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getName()
     */
    @Override
    public String getName() {
        return producerName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getSynchronousEventCache()
     */
    @Override
    public SynchronousEventCache getSynchronousEventCache() {
        return synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#setSynchronousEventCache(com.ericsson.apex.service.engine.event.SynchronousEventCache)
     */
    @Override
    public void setSynchronousEventCache(final SynchronousEventCache synchronousEventCache) {
        this.synchronousEventCache = synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#sendEvent(long, java.lang.String, java.lang.Object)
     */
    @Override
    public void sendEvent(final long executionId, final String eventName, final Object event) {
        // Check if this is a synchronized event, if so we have received a reply
        if (synchronousEventCache != null) {
            synchronousEventCache.removeCachedEventToApexIfExists(executionId);
        }

        // Cast the event to a string, if our conversion is correctly configured, this cast should always work
        String stringEvent = null;
        try {
            stringEvent = (String) event;
        }
        catch (Exception e) {
            String errorMessage = "error in ApexFileProducer \"" + producerName + "\" while transferring event \"" + event + "\" to the output stream";
            LOGGER.debug(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        eventOutputStream.println(stringEvent);
        eventOutputStream.flush();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        eventOutputStream.close();
    }
}
