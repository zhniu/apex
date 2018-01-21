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

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * This class implements an Apex event consumer that receives events using Kafka.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexKafkaConsumer implements ApexEventConsumer, Runnable {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexKafkaConsumer.class);

    // The Kafka parameters read from the parameter service
    private KAFKACarrierTechnologyParameters kafkaConsumerProperties;

    // The event receiver that will receive events from this consumer
    private ApexEventReceiver eventReceiver;

    // The Kafka consumer used to receive events using Kafka
    private KafkaConsumer<String, String> kafkaConsumer;

    // The name for this consumer
    private String name = null;

    // The synchronous event cache being used to track synchronous events
    private SynchronousEventCache synchronousEventCache;

    // The consumer thread and stopping flag
    private Thread consumerThread;
    private boolean stopOrderedFlag = false;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#init(java.lang.String,
     * com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters, com.ericsson.apex.service.engine.event.ApexEventReceiver)
     */
    @Override
    public void init(final String consumerName, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
            throws ApexEventException {
        this.eventReceiver = incomingEventReceiver;
        this.name = consumerName;

        // Check and get the Kafka Properties
        if (!(consumerParameters.getCarrierTechnologyParameters() instanceof KAFKACarrierTechnologyParameters)) {
            LOGGER.warn("specified consumer properties of type \"" + consumerParameters.getCarrierTechnologyParameters().getClass().getCanonicalName()
                    + "\" are not applicable to a Kafka consumer");
            throw new ApexEventException("specified consumer properties of type \""
                    + consumerParameters.getCarrierTechnologyParameters().getClass().getCanonicalName() + "\" are not applicable to a Kafka consumer");
        }
        kafkaConsumerProperties = (KAFKACarrierTechnologyParameters) consumerParameters.getCarrierTechnologyParameters();

        // Kick off the Kafka consumer
        kafkaConsumer = new KafkaConsumer<String, String>(kafkaConsumerProperties.getKafkaConsumerProperties());
        kafkaConsumer.subscribe(kafkaConsumerProperties.getConsumerTopicList());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event receiver for " + this.getClass().getName() + ":" + this.name + " subscribed to topics: "
                    + kafkaConsumerProperties.getConsumerTopicList());
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

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getSynchronousEventCache()
     */
    @Override
    public SynchronousEventCache getSynchronousEventCache() {
        return synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#setSynchronousEventCache(com.ericsson.apex.service.engine.event.SynchronousEventCache)
     */
    @Override
    public void setSynchronousEventCache(final SynchronousEventCache synchronousEventCache) {
        this.synchronousEventCache = synchronousEventCache;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // Kick off the Kafka consumer
        kafkaConsumer = new KafkaConsumer<String, String>(kafkaConsumerProperties.getKafkaConsumerProperties());
        kafkaConsumer.subscribe(kafkaConsumerProperties.getConsumerTopicList());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event receiver for " + this.getClass().getName() + ":" + this.name + " subscribed to topics: "
                    + kafkaConsumerProperties.getConsumerTopicList());
        }

        // The endless loop that receives events over Kafka
        while (consumerThread.isAlive() && !stopOrderedFlag) {
            try {
                final ConsumerRecords<String, String> records = kafkaConsumer.poll(kafkaConsumerProperties.getConsumerPollTime());
                for (final ConsumerRecord<String, String> record : records) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("event received for {} for forwarding to Apex engine : {} {}", this.getClass().getName() + ":" + this.name, record.key(),
                                record.value());
                    }
                    eventReceiver.receiveEvent(record.key(), record.value());
                }
            }
            catch (final Exception e) {
                LOGGER.warn("error receiving events on thread {}", consumerThread.getName(), e);
            }
        }

        if (!consumerThread.isInterrupted()) {
            kafkaConsumer.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#stop()
     */
    @Override
    public void stop() {
        stopOrderedFlag = true;

        while (consumerThread.isAlive()) {
            ThreadUtilities.sleep(kafkaConsumerProperties.getConsumerPollTime());
        }
    }
}
