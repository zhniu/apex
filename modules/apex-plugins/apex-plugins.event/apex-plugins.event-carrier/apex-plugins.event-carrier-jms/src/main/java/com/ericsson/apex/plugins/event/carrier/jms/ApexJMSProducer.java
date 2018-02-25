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

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProducer;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.engine.event.SynchronousEventCache;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * Concrete implementation of an Apex event producer that sends events using JMS.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexJMSProducer implements ApexEventProducer {

    // Get a reference to the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApexJMSProducer.class);

    // The JMS parameters read from the parameter service
    private JMSCarrierTechnologyParameters jmsProducerProperties;

    // The connection to the JMS server
    private Connection connection;

    // The topic on which we send events to JMS
    private Topic jmsOutgoingTopic;

    // The JMS session on which we will send events
    private Session jmsSession;

    // The producer on which we will send events
    private MessageProducer messageProducer;

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

        // Check and get the JMS Properties
        if (!(producerParameters.getCarrierTechnologyParameters() instanceof JMSCarrierTechnologyParameters)) {
            LOGGER.warn("specified producer properties are not applicable to a JMS producer (" + this.name + ")");
            throw new ApexEventException("specified producer properties are not applicable to a JMS producer (" + this.name + ")");
        }
        jmsProducerProperties = (JMSCarrierTechnologyParameters) producerParameters.getCarrierTechnologyParameters();

        // Look up the JMS connection factory
        InitialContext jmsContext = null;
        ConnectionFactory connectionFactory = null;
        try {
            jmsContext = new InitialContext(jmsProducerProperties.getJMSProducerProperties());
            connectionFactory = (ConnectionFactory) jmsContext.lookup(jmsProducerProperties.getConnectionFactory());

            // Check if we actually got a connection factory
            if (connectionFactory == null) {
                throw new NullPointerException(
                        "JMS context lookup of \"" + jmsProducerProperties.getConnectionFactory() + "\" returned null for producer (" + this.name + ")");
            }
        }
        catch (Exception e) {
            String errorMessage = "lookup of JMS connection factory  \"" + jmsProducerProperties.getConnectionFactory()
                    + "\" failed for JMS producer properties \"" + jmsProducerProperties.getJMSConsumerProperties() + "\" for producer (" + this.name + ")";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Lookup the topic on which we will send events
        try {
            jmsOutgoingTopic = (Topic) jmsContext.lookup(jmsProducerProperties.getProducerTopic());

            // Check if we actually got a topic
            if (jmsOutgoingTopic == null) {
                throw new NullPointerException(
                        "JMS context lookup of \"" + jmsProducerProperties.getProducerTopic() + "\" returned null for producer (" + this.name + ")");
            }
        }
        catch (Exception e) {
            String errorMessage = "lookup of JMS topic  \"" + jmsProducerProperties.getProducerTopic() + "\" failed for JMS producer properties \""
                    + jmsProducerProperties.getJMSProducerProperties() + "\" for producer (" + this.name + ")";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Create and start a connection to the JMS server
        try {
            connection = connectionFactory.createConnection(jmsProducerProperties.getSecurityPrincipal(), jmsProducerProperties.getSecurityCredentials());
            connection.start();
        }
        catch (Exception e) {
            String errorMessage = "connection to JMS server failed for JMS properties \"" + jmsProducerProperties.getJMSConsumerProperties()
                    + "\" for producer (" + this.name + ")";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Create a JMS session for sending events
        try {
            jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        catch (Exception e) {
            String errorMessage = "creation of session to JMS server failed for JMS properties \"" + jmsProducerProperties.getJMSConsumerProperties()
                    + "\" for producer (" + this.name + ")";
            LOGGER.warn(errorMessage, e);
            throw new ApexEventException(errorMessage, e);
        }

        // Create a JMS message producer for sending events
        try {
            messageProducer = jmsSession.createProducer(jmsOutgoingTopic);
        }
        catch (Exception e) {
            String errorMessage = "creation of producer for sending events to JMS server failed for JMS properties \""
                    + jmsProducerProperties.getJMSConsumerProperties() + "\"";
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
     * @see com.ericsson.apex.service.engine.event.ApexEventProducer#sendEvent(long, java.lang.String, java.lang.Object)
     */
    @Override
    public void sendEvent(final long executionId, final String eventname, final Object eventObject) {
        // Check if this is a synchronized event, if so we have received a reply
		SynchronousEventCache synchronousEventCache = (SynchronousEventCache) peerReferenceMap.get(EventHandlerPeeredMode.SYNCHRONOUS);
        if (synchronousEventCache != null) {
            synchronousEventCache.removeCachedEventToApexIfExists(executionId);
        }

        // Check if the object to be sent is serializable
        if (!Serializable.class.isAssignableFrom(eventObject.getClass())) {
            String errorMessage = "could not send event \"" + eventname + "\" on JMS message producer " + this.name + ", object of type \""
                    + eventObject.getClass().getCanonicalName() + "\" is not serializable";
            LOGGER.warn(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        // The JMS message to send is constructed using the JMS session
        Message jmsMessage = null;

        // Check the type of JMS message to send
        if (jmsProducerProperties.isObjectMessageSending()) {
            // We should send a JMS Object Message
            try {
                jmsMessage = jmsSession.createObjectMessage((Serializable) eventObject);
            }
            catch (Exception e) {
                String errorMessage = "could not send event \"" + eventname + "\" on JMS message producer " + this.name
                        + ", could not create JMS Object Message for object \"" + eventObject;
                LOGGER.warn(errorMessage);
                throw new ApexEventRuntimeException(errorMessage);
            }
        }
        else {
            // We should send a JMS Text Message
            try {
                jmsMessage = jmsSession.createTextMessage(eventObject.toString());
            }
            catch (Exception e) {
                String errorMessage = "could not send event \"" + eventname + "\" on JMS message producer " + this.name
                        + ", could not create JMS Text Message for object \"" + eventObject;
                LOGGER.warn(errorMessage);
                throw new ApexEventRuntimeException(errorMessage);
            }
        }

        try {
            messageProducer.send(jmsMessage);
        }
        catch (Exception e) {
            String errorMessage = "could not send event \"" + eventname + "\" on JMS message producer " + this.name + ", send failed for object \""
                    + eventObject;
            LOGGER.warn(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.producer.ApexEventProducer#stop()
     */
    @Override
    public void stop() {
        // Close the message producer
        try {
            messageProducer.close();
        }
        catch (Exception e) {
            String errorMessage = "failed to close JMS message producer " + this.name + " for sending messages";
            LOGGER.warn(errorMessage, e);
        }

        // Close the session
        try {
            jmsSession.close();
        }
        catch (Exception e) {
            String errorMessage = "failed to close the JMS session for  " + this.name + " for sending messages";
            LOGGER.warn(errorMessage, e);
        }

        // Close the connection to the JMS server
        try {
            connection.close();
        }
        catch (Exception e) {
            String errorMessage = "close of connection to the JMS server for  " + this.name + " failed";
            LOGGER.warn(errorMessage, e);
        }
    }
}
