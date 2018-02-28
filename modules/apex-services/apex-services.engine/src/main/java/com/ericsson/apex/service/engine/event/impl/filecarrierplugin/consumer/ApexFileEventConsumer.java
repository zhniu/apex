/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.FILECarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation an Apex event consumer that reads events from a file. This consumer also implements ApexEventProducer and therefore can be used as a
 * synchronous consumer.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexFileEventConsumer implements ApexEventConsumer, Runnable {
    
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexFileEventConsumer.class);

    // The input stream to read events from
    private InputStream eventInputStream;

    // The text block reader that will read text blocks from the contents of the file
    private TextBlockReader textBlockReader;

    // The event receiver that will receive asynchronous events from this consumer
    private ApexEventReceiver eventReceiver = null;

    // The consumer thread and stopping flag
    private Thread consumerThread;

    // The name for this consumer
    private String consumerName = null;

    // The specific carrier technology parameters for this consumer
    private FILECarrierTechnologyParameters fileCarrierTechnologyParameters;

    // The peer references for this event handler
    private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

    // Holds the next identifier for event execution.
    private static AtomicLong nextExecutionID = new AtomicLong(0L);

    /**
     * Private utility to get the next candidate value for a Execution ID. This value will always be unique in a single JVM
     * 
     * @return the next candidate value for a Execution ID
     */
    private static synchronized long getNextExecutionID() {
        return nextExecutionID.getAndIncrement();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.consumer.ApexEventConsumer#init(com.ericsson.apex.apps.uservice.consumer.ApexEventReceiver)
     */
    @Override
    public void init(final String name, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
            throws ApexEventException {
        this.eventReceiver = incomingEventReceiver;
        this.consumerName = name;

        // Get and check the Apex parameters from the parameter service
        if (consumerParameters == null) {
            final String errorMessage = "Consumer parameters for ApexFileConsumer \"" + consumerName + "\" is null";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Check and get the file Properties
        if (!(consumerParameters.getCarrierTechnologyParameters() instanceof FILECarrierTechnologyParameters)) {
            String errorMessage = "specified consumer properties for ApexFileConsumer \"" + consumerName + "\" are not applicable to a File consumer";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        fileCarrierTechnologyParameters = (FILECarrierTechnologyParameters) consumerParameters.getCarrierTechnologyParameters();

        // Open the file producing events
        try {
            if (fileCarrierTechnologyParameters.isStandardIO()) {
                eventInputStream = System.in;
            }
            else {
                eventInputStream = new FileInputStream(fileCarrierTechnologyParameters.getFileName());
            }

            // Get an event composer for our event source
            textBlockReader = new TextBlockReaderFactory().getTaggedReader(eventInputStream, consumerParameters.getEventProtocolParameters());
        }
        catch (final IOException e) {
            String errorMessage = "ApexFileConsumer \"" + consumerName + "\" failed to open file for reading: \""
                    + fileCarrierTechnologyParameters.getFileName() + "\"";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }
        
        if (fileCarrierTechnologyParameters.getStartDelay() > 0) {
        		ThreadUtilities.sleep(fileCarrierTechnologyParameters.getStartDelay());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getName()
     */
    @Override
    public String getName() {
        return consumerName;
    }

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode)
	 */
	@Override
	public PeeredReference getPeeredReference(EventHandlerPeeredMode peeredMode) {
		return peerReferenceMap.get(peeredMode);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#setPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode, com.ericsson.apex.service.engine.event.PeeredReference)
	 */
	@Override
	public void setPeeredReference(EventHandlerPeeredMode peeredMode, PeeredReference peeredReference) {
		peerReferenceMap.put(peeredMode, peeredReference);
	}

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#start()
     */
    @Override
    public void start() {
        // Configure and start the event reception thread
        final String threadName = this.getClass().getName() + " : " + consumerName;
        consumerThread = new ApplicationThreadFactory(threadName).newThread(this);
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // Check that we have been initialized in async or sync mode
        if (eventReceiver == null) {
            LOGGER.warn("\"{}\" has not been initilaized for either asynchronous or synchronous event handling", consumerName);
            return;
        }

        // Read the events from the file while there are still events in the file
        try {
            // Read all the text blocks
            TextBlock textBlock;
            do {
                // Read the text block
                textBlock = textBlockReader.readTextBlock();

                // Process the event from the text block if there is one there
                if (textBlock.getText() != null) {
                    eventReceiver.receiveEvent(getNextExecutionID(), textBlock.getText());
                }
            }
            while (!textBlock.isEndOfText());
        }
        catch (final Exception e) {
            LOGGER.warn("\"" + consumerName + "\" failed to read event from file: \"" + fileCarrierTechnologyParameters.getFileName() + "\"", e);
        }
        finally {
            try {
                eventInputStream.close();
            }
            catch (final IOException e) {
                LOGGER.warn("ApexFileConsumer \"" + consumerName + "\" failed to close file: \"" + fileCarrierTechnologyParameters.getFileName() + "\"", e);
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        try {
            eventInputStream.close();
        }
        catch (final IOException e) {
            LOGGER.warn("ApexFileConsumer \"" + consumerName + "\" failed to close file for reading: \"" + fileCarrierTechnologyParameters.getFileName() + "\"",
                    e);
        }

        if (consumerThread.isAlive() && !consumerThread.isInterrupted()) {
            consumerThread.interrupt();
        }
    }
}
