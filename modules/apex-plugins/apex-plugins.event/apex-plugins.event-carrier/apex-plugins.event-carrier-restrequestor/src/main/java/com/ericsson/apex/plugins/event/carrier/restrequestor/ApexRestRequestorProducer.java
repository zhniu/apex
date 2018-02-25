/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restrequestor;

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
 * Concrete implementation of an Apex event producer that sends events using REST.
 * 
 * @author Joss Armstrong (joss.armstrong@ericsson.com)
 * 
 */
public class ApexRestRequestorProducer implements ApexEventProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApexRestRequestorProducer.class);

	// The REST carrier properties
	private RESTRequestorCarrierTechnologyParameters restProducerProperties;

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

		// Check and get the REST Properties
		if (!(producerParameters.getCarrierTechnologyParameters() instanceof RESTRequestorCarrierTechnologyParameters)) {
			String errorMessage = "specified consumer properties are not applicable to REST requestor producer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}
		restProducerProperties = (RESTRequestorCarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();

		// Check if we are in synchronous mode
		if (!producerParameters.isPeeredMode(EventHandlerPeeredMode.REQUESTOR)) {
			String errorMessage = "REST Requestor producer (" + this.name + ") must run in peered requestor mode with a REST Requestor consumer";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}
		
        // Check if the HTTP URL has been set
        if (restProducerProperties.getURL() != null) {
            String errorMessage = "URL may not be specified on REST Requestor producer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
		
        // Check if the HTTP method has been set
        if (restProducerProperties.getHttpMethod() != null) {
            String errorMessage = "HTTP method may not be specified on REST Requestor producer (" + this.name + ")";
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
	public void sendEvent(final long executionId, final String eventName, final Object event) {
        // Check if this is a synchronized event, if so we have received a reply
		SynchronousEventCache synchronousEventCache = (SynchronousEventCache) peerReferenceMap.get(EventHandlerPeeredMode.SYNCHRONOUS);
        if (synchronousEventCache != null) {
            synchronousEventCache.removeCachedEventToApexIfExists(executionId);
        }

        // Find the peered consumer for this producer
		PeeredReference peeredRequestorReference = peerReferenceMap.get(EventHandlerPeeredMode.REQUESTOR);
		if (peeredRequestorReference != null) {
			// Find the REST Response Consumer that will handle this request
			ApexEventConsumer consumer = peeredRequestorReference.getPeeredConsumer();
			if (!(consumer instanceof ApexRestRequestorConsumer)) {
				String errorMessage = "send of event to URL \"" + restProducerProperties.getURL() + "\" failed,"
						+ " REST response consumer is not an instance of ApexRestRequestorConsumer\n" + event;
				LOGGER.warn(errorMessage);
				throw new ApexEventRuntimeException(errorMessage);
			}

			// Use the consumer to handle this event
			ApexRestRequestorConsumer restRequstConsumer = (ApexRestRequestorConsumer) consumer;
			restRequstConsumer.processRestRequest(new ApexRestRequest(executionId, eventName, event));
			
			eventsSent++;
		}
		else {
			// No synchronous consumer defined
			String errorMessage = "send of event to URL \"" + restProducerProperties.getURL() + "\" failed,"
					+ " REST response consumer is not defined\n" + event;
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
		// For REST requestor, all the implementation is in the consumer
	}
}
