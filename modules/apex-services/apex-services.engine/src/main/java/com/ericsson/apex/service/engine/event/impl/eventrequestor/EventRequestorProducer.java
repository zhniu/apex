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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation of an Apex event producer that sends one or more events to its peered event requestor consumer.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * 
 */
public class EventRequestorProducer implements ApexEventProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventRequestorProducer.class);

	// The name for this producer
	private String name = null;

	// The peer references for this event handler
	private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

	// The number of events sent
	private int eventsSent = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#init(java.lang.String,
	 * com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters)
	 */
	@Override
	public void init(final String producerName, final EventHandlerParameters producerParameters) throws ApexEventException {
		this.name = producerName;

		// Check and get the producer Properties
		if (!(producerParameters.getCarrierTechnologyParameters() instanceof EventRequestorCarrierTechnologyParameters)) {
			String errorMessage = "specified consumer properties are not applicable to event requestor producer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}

		// Check if we are in peered mode
		if (!producerParameters.isPeeredMode(EventHandlerPeeredMode.REQUESTOR)) {
			String errorMessage = "Event Requestor producer (" + this.name + ") must run in peered requestor mode with a Event Requestor consumer";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Get the number of events sent to date
	 * @return the number of events received
	 */
	public int getEventsSent() {
		return eventsSent;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#getPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode)
	 */
	@Override
	public PeeredReference getPeeredReference(EventHandlerPeeredMode peeredMode) {
		return peerReferenceMap.get(peeredMode);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#setPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode, com.ericsson.apex.service.engine.event.PeeredReference)
	 */
	@Override
	public void setPeeredReference(EventHandlerPeeredMode peeredMode, PeeredReference peeredReference) {
		peerReferenceMap.put(peeredMode, peeredReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#sendEvent(long, java.lang. String, java.lang.Object)
	 */
	@Override
	public void sendEvent(final long executionId, final String eventName, final Object eventObject) {
		// Check if this is a synchronized event, if so we have received a reply
		SynchronousEventCache synchronousEventCache = (SynchronousEventCache) peerReferenceMap.get(EventHandlerPeeredMode.SYNCHRONOUS);
		if (synchronousEventCache != null) {
			synchronousEventCache.removeCachedEventToApexIfExists(executionId);
		}

		// Find the peered consumer for this producer
		PeeredReference peeredRequestorReference = peerReferenceMap.get(EventHandlerPeeredMode.REQUESTOR);
		if (peeredRequestorReference != null) {
			// Find the event Response Consumer that will handle this request
			ApexEventConsumer consumer = peeredRequestorReference.getPeeredConsumer();
			if (!(consumer instanceof EventRequestorConsumer)) {
				String errorMessage = "send of event to event consumer \"" + peeredRequestorReference.getPeeredConsumer() + "\" failed,"
						+ " event response consumer is not an instance of EventRequestorConsumer\n" + eventObject;
				LOGGER.warn(errorMessage);
				throw new ApexEventRuntimeException(errorMessage);
			}

			// Use the consumer to handle this event
			EventRequestorConsumer eventRequstConsumer = (EventRequestorConsumer) consumer;
			eventRequstConsumer.processEvent(eventObject);

			eventsSent++;
		}
		else {
			// No peered consumer defined
			String errorMessage = "send of event failed, event response consumer is not defined\n" + eventObject;
			LOGGER.warn(errorMessage);
			throw new ApexEventRuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventProducer#stop()
	 */
	@Override
	public void stop() {
		// For event requestor, all the implementation is in the consumer
	}
}
