/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.eventrequestor;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * This class implements an Apex event consumer that receives events from its peered event requestor producer.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventRequestorConsumer implements ApexEventConsumer, Runnable {
	// Get a reference to the logger
	private static final Logger LOGGER = LoggerFactory.getLogger(EventRequestorConsumer.class);

	// The amount of time to wait in milliseconds between checks that the consumer thread has stopped
	private static final long EVENT_REQUESTOR_WAIT_SLEEP_TIME = 50;

	// The event receiver that will receive events from this consumer
	private ApexEventReceiver eventReceiver;

	// The name for this consumer
	private String name = null;

	// The peer references for this event handler
	private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

	// Temporary request holder for incoming event send requests
	private final BlockingQueue<Object> incomingEventRequestQueue = new LinkedBlockingQueue<>();

	// The consumer thread and stopping flag
	private Thread consumerThread;
	private boolean stopOrderedFlag = false;

	// The number of events received to date
	private int eventsReceived = 0;

	@Override
	public void init(final String consumerName, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
			throws ApexEventException {
		this.eventReceiver = incomingEventReceiver;
		this.name = consumerName;

		// Check and get the event requestor consumer properties
		if (!(consumerParameters.getCarrierTechnologyParameters() instanceof EventRequestorCarrierTechnologyParameters)) {
			String errorMessage = "specified consumer properties are not applicable to event Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}
		
		// Check if we are in peered mode
		if (!consumerParameters.isPeeredMode(EventHandlerPeeredMode.REQUESTOR)) {
			String errorMessage = "event Requestor consumer (" + this.name + ") must run in peered requestor mode with a event Requestor producer";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}

	}

	/**
	 * Receive an incoming event send request from the peered event Requestor producer and queue it
	 * @param eventObject the incoming event to process
	 * @throws ApexEventRuntimeException on queueing errors
	 */
	public void processEvent(final Object eventObject) {
		// Push the event onto the queue for handling
		try {
			incomingEventRequestQueue.add(eventObject);
		}
		catch (final Exception e) {
			String errorMessage = "could not queue request \"" + eventObject + "\" on event Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventRuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#start()
	 */
	@Override
	public void start() {
		// Configure and start the event reception thread
		final String threadName = this.getClass().getName() + ":" + this.name;
		consumerThread = new ApplicationThreadFactory(threadName).newThread(this);
		consumerThread.setDaemon(true);
		consumerThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Get the number of events received to date
	 * @return the number of events received
	 */
	public int getEventsReceived() {
		return eventsReceived;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// The endless loop that receives events using REST calls
		while (consumerThread.isAlive() && !stopOrderedFlag) {
			try {
				// Take the next event from the queue
				final Object eventObject = incomingEventRequestQueue.poll(EVENT_REQUESTOR_WAIT_SLEEP_TIME, TimeUnit.MILLISECONDS);
				if (eventObject == null) {
					// Poll timed out, wait again
					continue;
				}
				
				// Send the event into Apex
				eventReceiver.receiveEvent(eventObject);

				eventsReceived++;
			}
			catch (final InterruptedException e) {
				LOGGER.debug("Thread interrupted, Reason {}", e.getMessage());
				Thread.currentThread().interrupt();
			}
			catch (final Exception e) {
				LOGGER.warn("error receiving events on thread {}", consumerThread.getName(), e);
			}
		}
	}
	
    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventConsumer#stop()
     */
    @Override
    public void stop() {
        stopOrderedFlag = true;

        while (consumerThread.isAlive()) {
            ThreadUtilities.sleep(EVENT_REQUESTOR_WAIT_SLEEP_TIME);
        }
    }
}
