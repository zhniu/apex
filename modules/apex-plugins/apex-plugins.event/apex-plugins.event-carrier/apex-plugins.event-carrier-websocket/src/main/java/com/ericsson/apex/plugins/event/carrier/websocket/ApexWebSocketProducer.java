/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.websocket;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageClient;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageServer;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessager;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation of an Apex event producer that sends events using a web socket.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexWebSocketProducer implements ApexEventProducer, WSStringMessageListener {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexWebSocketProducer.class);

    // The Web Socket properties
    private WEBSOCKETCarrierTechnologyParameters webSocketProducerProperties;

    // The web socket messager, may be WS a server or a client
    private WSStringMessager wsStringMessager;

    // The name for this producer
    private String name = null;

    // The peer references for this event handler
    private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

    @Override
    public void init(final String producerName, final EventHandlerParameters producerParameters) throws ApexEventException {
        this.name = producerName;

        // Check and get the web socket Properties
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof WEBSOCKETCarrierTechnologyParameters)) {
            LOGGER.warn("specified producer properties for " + this.name + "are not applicable to a web socket producer");
            throw new ApexEventException("specified producer properties are not applicable to a web socket producer");
        }
        webSocketProducerProperties = (WEBSOCKETCarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();

        // Check if this is a server or a client Web Socket
        if (webSocketProducerProperties.isWsClient()) {
            // Create a WS client
            wsStringMessager = new WSStringMessageClient(webSocketProducerProperties.getHost(), webSocketProducerProperties.getPort());
        }
        else {
            wsStringMessager = new WSStringMessageServer(webSocketProducerProperties.getPort());
        }

        // Start reception of event strings on the web socket
        try {
            wsStringMessager.start(this);
        }
        catch (final MessagingException e) {
            LOGGER.warn("could not start web socket producer (" + this.name + ")");
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
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#sendEvent(long, java.lang.String, java.lang.Object)
     */
    @Override
    public void sendEvent(final long executionId, final String eventName, final Object event) {
        // Check if this is a synchronized event, if so we have received a reply
		SynchronousEventCache synchronousEventCache = (SynchronousEventCache) peerReferenceMap.get(EventHandlerPeeredMode.SYNCHRONOUS);
        if (synchronousEventCache != null) {
            synchronousEventCache.removeCachedEventToApexIfExists(executionId);
        }

        wsStringMessager.sendString((String) event);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        if (wsStringMessager != null) {
            wsStringMessager.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener#receiveString(java.lang.String)
     */
    @Override
    public void receiveString(final String messageString) {
        LOGGER.warn("received message \"" + messageString + "\" on web socket producer (" + this.name
                + ") , no messages should be received on a web socket producer");
    }
}
