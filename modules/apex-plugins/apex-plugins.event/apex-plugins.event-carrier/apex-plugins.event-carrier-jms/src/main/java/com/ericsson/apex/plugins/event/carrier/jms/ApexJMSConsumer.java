/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * This class implements an Apex event consumer that receives events using JMS.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexJMSConsumer implements MessageListener, ApexEventConsumer, Runnable {
    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexJMSConsumer.class);

    // The Apex and JMS parameters read from the parameter service
    private JMSCarrierTechnologyParameters jmsConsumerProperties;

    // The event receiver that will receive events from this consumer
    private ApexEventReceiver eventReceiver;

    // The consumer thread and stopping flag
    private Thread consumerThread;
    private boolean stopOrderedFlag = false;

    // The connection to the JMS server
    private Connection connection;

    // The topic on which we receive events from JMS
    private Topic jmsIncomingTopic;

    // The name for this consumer
    private String name = null;

    // The synchronous event cache being used to track synchronous events
    private SynchronousEventCache synchronousEventCache;

    @Override
    public void init(final String consumerName, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
            throws ApexEventException {
        this.eventReceiver = incomingEventReceiver;

        this.name = consumerName;

        // Check and get the JMS Properties
        if (!(consumerParameters.getCarrierTechnologyParameters() instanceof JMSCarrierTechnologyParameters)) {
            String errorMessage = "specified consumer properties of type \"" + consumerParameters.getCarrierTechnologyParameters().getClass().getCanonicalName()
                    + "\" are not applicable to a JMS consumer";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }
        jmsConsumerProperties = (JMSCarrierTechnologyParameters) consumerParameters.getCarrierTechnologyParameters();

        // Look up the JMS connection factory
        InitialContext jmsContext = null;
        ConnectionFactory connectionFactory = null;
        try {
            jmsContext = new InitialContext(jmsConsumerProperties.getJMSConsumerProperties());
            connectionFactory = (ConnectionFactory) jmsContext.lookup(jmsConsumerProperties.getConnectionFactory());

            // Check if we actually got a connection factory
            if (connectionFactory == null) {
                throw new NullPointerException("JMS context lookup of \"" + jmsConsumerProperties.getConnectionFactory() + "\" returned null");
            }
        }
        catch (Exception e) {
            String errorMessage = "lookup of JMS connection factory  \"" + jmsConsumerProperties.getConnectionFactory()
                    + "\" failed for JMS consumer properties \"" + jmsConsumerProperties.getJMSConsumerProperties() + "\"";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Lookup the topic on which we will receive events
        try {
            jmsIncomingTopic = (Topic) jmsContext.lookup(jmsConsumerProperties.getConsumerTopic());

            // Check if we actually got a topic
            if (jmsIncomingTopic == null) {
                throw new NullPointerException("JMS context lookup of \"" + jmsConsumerProperties.getConsumerTopic() + "\" returned null");
            }
        }
        catch (Exception e) {
            String errorMessage = "lookup of JMS topic  \"" + jmsConsumerProperties.getConsumerTopic() + "\" failed for JMS consumer properties \""
                    + jmsConsumerProperties.getJMSConsumerProperties() + "\"";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Create and start a connection to the JMS server
        try {
            connection = connectionFactory.createConnection(jmsConsumerProperties.getSecurityPrincipal(), jmsConsumerProperties.getSecurityCredentials());
            connection.start();
        }
        catch (Exception e) {
            String errorMessage = "connection to the JMS server failed for JMS properties \"" + jmsConsumerProperties.getJMSConsumerProperties() + "\"";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
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
        // JMS session and message consumer for receiving messages
        Session jmsSession = null;
        MessageConsumer messageConsumer = null;

        // Create a session to the JMS server
        try {
            jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        catch (Exception e) {
            String errorMessage = "failed to create a JMS session towards the JMS server for receiving messages";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Create a message consumer for reception of messages and set this class as a message listener
        try {
            messageConsumer = jmsSession.createConsumer(jmsIncomingTopic);
            messageConsumer.setMessageListener(this);
        }
        catch (Exception e) {
            String errorMessage = "failed to create a JMS message consumer for receiving messages";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Everything is now set up
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "event receiver " + this.getClass().getName() + ":" + this.name + " subscribed to JMS topic: " + jmsConsumerProperties.getConsumerTopic());
        }

        // The endless loop that receives events over JMS
        while (consumerThread.isAlive() && !stopOrderedFlag) {
            ThreadUtilities.sleep(jmsConsumerProperties.getConsumerWaitTime());
        }

        // Close the message consumer
        try {
            messageConsumer.close();
        }
        catch (Exception e) {
            String errorMessage = "failed to close the JMS message consumer for receiving messages";
            LOGGER.warn(errorMessage, e);
        }

        // Close the session
        try {
            jmsSession.close();
        }
        catch (Exception e) {
            String errorMessage = "failed to close the JMS session for receiving messages";
            LOGGER.warn(errorMessage, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @Override
    public void onMessage(final Message jmsMessage) {
        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("event received for {} for forwarding to Apex engine : {} {}", this.getClass().getName() + ":" + this.name,
                        jmsMessage.getJMSMessageID(), jmsMessage.getJMSType());
            }

            eventReceiver.receiveEvent(jmsMessage.getJMSType(), jmsMessage);
        }
        catch (Exception e) {
            String errorMessage = "failed to receive message from JMS";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        stopOrderedFlag = true;

        while (consumerThread.isAlive()) {
            ThreadUtilities.sleep(jmsConsumerProperties.getConsumerWaitTime());
        }

        // Close the connection to the JMS server
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (Exception e) {
            String errorMessage = "close of connection to the JMS server failed";
            LOGGER.warn(errorMessage, e);
        }
    }

}
