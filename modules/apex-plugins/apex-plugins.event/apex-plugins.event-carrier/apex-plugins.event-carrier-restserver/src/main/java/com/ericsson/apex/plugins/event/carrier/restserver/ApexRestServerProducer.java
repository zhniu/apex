/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restserver;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation of an Apex event producer that sends events using REST.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * 
 */
public class ApexRestServerProducer implements ApexEventProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexRestServerProducer.class);

    // The REST carrier properties
    private RESTServerCarrierTechnologyParameters restProducerProperties;

    // The name for this producer
    private String name = null;

    // The peer references for this event handler
    private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

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
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof RESTServerCarrierTechnologyParameters)) {
            String errorMessage = "specified producer properties are not applicable to REST Server producer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        restProducerProperties = (RESTServerCarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();

        // Check if host and port are defined
        if (restProducerProperties.getHost() != null || restProducerProperties.getPort() != -1 || restProducerProperties.isStandalone()) {
            String errorMessage = "the parameters \"host\", \"port\", and \"standalone\" are illegal on REST Server producer (" + this.name + ")";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Check if we are in synchronous mode
        if (!producerParameters.isPeeredMode(EventHandlerPeeredMode.SYNCHRONOUS)) {
            String errorMessage = "REST Server producer (" + this.name + ") must run in synchronous mode with a REST Server consumer";
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(name + ": event " + executionId + ':' + eventName + " recevied from Apex, event=" + event);
        }

        // If we are not synchronized, then exit
		SynchronousEventCache synchronousEventCache = (SynchronousEventCache) peerReferenceMap.get(EventHandlerPeeredMode.SYNCHRONOUS);
        if (synchronousEventCache == null) {
            return;
        }

        // We see all events on the receiver, even those that are not replies to events sent by the synchronized consumer of this producer, ignore those
        // events
        if (!synchronousEventCache.existsEventToApex(executionId)) {
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(name + ": event " + executionId + ':' + eventName + " is a reply to a REST server call from " + name);
        }

        // Add the event to the received event cache
        synchronousEventCache.cacheSynchronizedEventFromApex(executionId, event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
    }
}
