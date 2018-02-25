/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.kafka;

import java.util.EnumMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation of an Apex event producer that sends events using Kafka.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexKafkaProducer implements ApexEventProducer {

    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexKafkaProducer.class);

    // The Kafka parameters read from the parameter service
    private KAFKACarrierTechnologyParameters kafkaProducerProperties;

    // The Kafka Producer used to send events using Kafka
    private Producer<String, Object> kafkaProducer;

    //The name for this producer
    private String name = null;

    // The peer references for this event handler
    private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

    @Override
    public void init(final String producerName, final EventHandlerParameters producerParameters) throws ApexEventException {
        this.name = producerName;

        // Check and get the Kafka Properties
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof KAFKACarrierTechnologyParameters)) {
            LOGGER.warn("specified producer properties are not applicable to a Kafka producer (" + this.name + ")");
            throw new ApexEventException("specified producer properties are not applicable to a Kafka producer (" + this.name + ")");
        }
        kafkaProducerProperties = (KAFKACarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();
    }

    /* (non-Javadoc)
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

        // Kafka producer must be started in the same thread as it is stopped, so we must start it here
        if (kafkaProducer == null) {
            // Kick off the Kafka producer
            kafkaProducer = new KafkaProducer<String, Object>(kafkaProducerProperties.getKafkaProducerProperties());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("event producer " + this.name + " is ready to send to topics: "
                        + kafkaProducerProperties.getProducerTopic());
            }
        }

        kafkaProducer.send(new ProducerRecord<String, Object>(kafkaProducerProperties.getProducerTopic(), name, event));
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("event sent from engine using {} to topic {} : {} ", this.name,
                    kafkaProducerProperties.getProducerTopic(), event);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        if (kafkaProducer != null) {
            kafkaProducer.flush();
            kafkaProducer.close();
        }
    }
}
