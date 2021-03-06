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

import java.util.Properties;

import javax.naming.Context;

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for JMS as an event carrier technology.
 * <p>
 * The parameters for this plugin are:
 * <ol>
 * <li>initialContextFactory: JMS uses a naming {@link Context} object to look up the locations of JMS servers and JMS topics. An Initial Context Factory is
 * used to when creating a {@link Context} object that can be used for JMS lookups. The value of this parameter is passed to the {@link Context} with the label
 * {@link Context#INITIAL_CONTEXT_FACTORY}. Its value must be the full canonical path to a class that implements the
 * {@code javax.naming.spi.InitialContextFactory} interface. The parameter defaults to the string value
 * {@code org.jboss.naming.remote.client.InitialContextFactory}.
 * <li>providerURL: The location of the server to use for naming context lookups. The value of this parameter is passed to the {@link Context} with the label
 * {@link Context#PROVIDER_URL}. Its value must be a URL that identifies the JMS naming server. The parameter defaults to the string value
 * {@code remote://localhost:4447}.
 * <li>securityPrincipal: The user name to use for JMS access. The value of this parameter is passed to the {@link Context} with the label
 * {@link Context#SECURITY_PRINCIPAL}. Its value must be the user name of a user defined on the JMS server. The parameter defaults to the string value
 * {@code userid}.
 * <li>securityCredentials:The password to use for JMS access. The value of this parameter is passed to the {@link Context} with the label
 * {@link Context#SECURITY_CREDENTIALS}. Its value must be the password of a suer defined on the JMS server. The parameter defaults to the string value
 * {@code password}.
 * <li>connectionFactory: JMS uses a {@link javax.jms.ConnectionFactory} instance to create connections towards a JMS server. The connection factory to use is
 * held in the JMS {@link Context} object. This parameter specifies the label to use to look up the {@link javax.jms.ConnectionFactory} instance from the JMS
 * {@link Context}.
 * <li>producerTopic: JMS uses a {@link javax.jms.Topic} instance to for sending and receiving messages. The topic to use for sending events to JMS from an Apex
 * producer is held in the JMS {@link Context} object. This parameter specifies the label to use to look up the {@link javax.jms.Topic} instance in the JMS
 * {@link Context} for the JMS server. The topic must, of course, also be defined on the JMS server. The parameter defaults to the string value
 * {@code apex-out}.
 * <li>consumerTopic: The topic to use for receiving events from JMS in an Apex consumer is held in the JMS {@link Context} object. This parameter specifies the
 * label to use to look up the {@link javax.jms.Topic} instance in the JMS {@link Context} for the JMS server. The topic must, of course, also be defined on the
 * JMS server. The parameter defaults to the string value {@code apex-in}.
 * <li>consumerWaitTime: The amount of milliseconds a JMS consumer should wait between checks of its thread execution status. The parameter defaults to the long
 * value {@code 100}.
 * <li>objectMessageSending: A flag that indicates whether Apex producers should send JMS messages as {@link javax.jms.ObjectMessage} instances for java objects
 * (value {@code true}) or as {@link javax.jms.TextMessage} instances for strings (value {@code false}) . The parameter defaults to the boolean value
 * {@code true}.
 * </ol>
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JMSCarrierTechnologyParameters extends CarrierTechnologyParameters {
    /** The label of this carrier technology. */
    public static final String JMS_CARRIER_TECHNOLOGY_LABEL = "JMS";

    /** The producer plugin class for the JMS carrier technology. */
    public static final String JMS_EVENT_PRODUCER_PLUGIN_CLASS = ApexJMSProducer.class.getCanonicalName();

    /** The consumer plugin class for the JMS carrier technology. */
    public static final String JMS_EVENT_CONSUMER_PLUGIN_CLASS = ApexJMSConsumer.class.getCanonicalName();

    // @formatter:off

    // Default parameter values
    private static final String  DEFAULT_CONNECTION_FACTORY        = "jms/RemoteConnectionFactory";
    private static final String  DEFAULT_INITIAL_CONTEXT_FACTORY   = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String  DEFAULT_PROVIDER_URL              = "remote://localhost:4447";
    private static final String  DEFAULT_SECURITY_PRINCIPAL        = "userid";
    private static final String  DEFAULT_SECURITY_CREDENTIALS      = "password";
    private static final String  DEFAULT_CONSUMER_TOPIC            = "apex-in";
    private static final String  DEFAULT_PRODUCER_TOPIC            = "apex-out";
    private static final int     DEFAULT_CONSUMER_WAIT_TIME        = 100;
    private static final boolean DEFAULT_TO_OBJECT_MESSAGE_SENDING = true;

    // Parameter property map tokens
    private static final String PROPERTY_INITIAL_CONTEXT_FACTORY  = Context.INITIAL_CONTEXT_FACTORY;
    private static final String PROPERTY_PROVIDER_URL             = Context.PROVIDER_URL;
    private static final String PROPERTY_SECURITY_PRINCIPAL       = Context.SECURITY_PRINCIPAL;
    private static final String PROPERTY_SECURITY_CREDENTIALS     = Context.SECURITY_CREDENTIALS;

    // JMS carrier parameters
    private String  connectionFactory     = DEFAULT_CONNECTION_FACTORY;
    private String  initialContextFactory = DEFAULT_INITIAL_CONTEXT_FACTORY;
    private String  providerURL           = DEFAULT_PROVIDER_URL;
    private String  securityPrincipal     = DEFAULT_SECURITY_PRINCIPAL;
    private String  securityCredentials   = DEFAULT_SECURITY_CREDENTIALS;
    private String  producerTopic         = DEFAULT_PRODUCER_TOPIC;
    private String  consumerTopic         = DEFAULT_CONSUMER_TOPIC;
    private int     consumerWaitTime      = DEFAULT_CONSUMER_WAIT_TIME;
    private boolean objectMessageSending  = DEFAULT_TO_OBJECT_MESSAGE_SENDING;
    // @formatter:on

    /**
     * Constructor to create a jms carrier technology parameters instance and register the instance with the parameter service.
     */
    public JMSCarrierTechnologyParameters() {
        super(JMSCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the JMS carrier technology
        this.setLabel(JMS_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(JMS_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(JMS_EVENT_CONSUMER_PLUGIN_CLASS);
    }

    /**
     * Gets the JMS producer properties.
     *
     * @return the JMS producer properties
     */
    public Properties getJMSProducerProperties() {
        final Properties jmsProperties = new Properties();

        jmsProperties.put(PROPERTY_INITIAL_CONTEXT_FACTORY, initialContextFactory);
        jmsProperties.put(PROPERTY_PROVIDER_URL, providerURL);
        jmsProperties.put(PROPERTY_SECURITY_PRINCIPAL, securityPrincipal);
        jmsProperties.put(PROPERTY_SECURITY_CREDENTIALS, securityCredentials);

        return jmsProperties;
    }

    /**
     * Gets the jms consumer properties.
     *
     * @return the jms consumer properties
     */
    public Properties getJMSConsumerProperties() {
        final Properties jmsProperties = new Properties();

        jmsProperties.put(PROPERTY_INITIAL_CONTEXT_FACTORY, initialContextFactory);
        jmsProperties.put(PROPERTY_PROVIDER_URL, providerURL);
        jmsProperties.put(PROPERTY_SECURITY_PRINCIPAL, securityPrincipal);
        jmsProperties.put(PROPERTY_SECURITY_CREDENTIALS, securityCredentials);

        return jmsProperties;
    }

    /**
     * Gets the connection factory.
     *
     * @return the connection factory
     */
    public String getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Gets the initial context factory.
     *
     * @return the initial context factory
     */
    public String getInitialContextFactory() {
        return initialContextFactory;
    }

    /**
     * Gets the provider URL.
     *
     * @return the provider URL
     */
    public String getProviderURL() {
        return providerURL;
    }

    /**
     * Gets the security principal.
     *
     * @return the security principal
     */
    public String getSecurityPrincipal() {
        return securityPrincipal;
    }

    /**
     * Gets the security credentials.
     *
     * @return the security credentials
     */
    public String getSecurityCredentials() {
        return securityCredentials;
    }

    /**
     * Gets the producer topic.
     *
     * @return the producer topic
     */
    public String getProducerTopic() {
        return producerTopic;
    }

    /**
     * Gets the consumer topic.
     *
     * @return the consumer topic
     */
    public String getConsumerTopic() {
        return consumerTopic;
    }

    /**
     * Gets the consumer wait time.
     *
     * @return the consumer wait time
     */
    public long getConsumerWaitTime() {
        return consumerWaitTime;
    }

    /**
     * Sets the connection factory.
     *
     * @param connectionFactory the connection factory
     */
    public void setConnectionFactory(final String connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Sets the initial context factory.
     *
     * @param initialContextFactory the initial context factory
     */
    public void setInitialContextFactory(final String initialContextFactory) {
        this.initialContextFactory = initialContextFactory;
    }

    /**
     * Sets the provider URL.
     *
     * @param providerURL the provider URL
     */
    public void setProviderURL(final String providerURL) {
        this.providerURL = providerURL;
    }

    /**
     * Sets the security principal.
     *
     * @param securityPrincipal the security principal
     */
    public void setSecurityPrincipal(final String securityPrincipal) {
        this.securityPrincipal = securityPrincipal;
    }

    /**
     * Sets the security credentials.
     *
     * @param securityCredentials the security credentials
     */
    public void setSecurityCredentials(final String securityCredentials) {
        this.securityCredentials = securityCredentials;
    }

    /**
     * Sets the producer topic.
     *
     * @param producerTopic the producer topic
     */
    public void setProducerTopic(final String producerTopic) {
        this.producerTopic = producerTopic;
    }

    /**
     * Sets the consumer topic.
     *
     * @param consumerTopic the consumer topic
     */
    public void setConsumerTopic(final String consumerTopic) {
        this.consumerTopic = consumerTopic;
    }

    /**
     * Sets the consumer wait time.
     *
     * @param consumerWaitTime the consumer wait time
     */
    public void setConsumerWaitTime(final int consumerWaitTime) {
        this.consumerWaitTime = consumerWaitTime;
    }

    /**
     * Checks if is object message sending.
     *
     * @return true, if checks if is object message sending
     */
    public boolean isObjectMessageSending() {
        return objectMessageSending;
    }

    /**
     * Sets the object message sending.
     *
     * @param objectMessageSending the object message sending
     */
    public void setObjectMessageSending(final boolean objectMessageSending) {
        this.objectMessageSending = objectMessageSending;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        final StringBuilder errorMessageBuilder = new StringBuilder();

        errorMessageBuilder.append(super.validate());

        if (initialContextFactory == null || initialContextFactory.trim().length() == 0) {
            errorMessageBuilder.append("  initialContextFactory not specified, must be specified as a string that is a class"
                    + " that implements the interface org.jboss.naming.remote.client.InitialContextFactory\n");
        }

        if (providerURL == null || providerURL.trim().length() == 0) {
            errorMessageBuilder.append("  providerURL not specified, must be specified as a URL string that specifies the location of "
                    + "configuration information for the service provider to use such as remote://localhost:4447\n");
        }

        if (securityPrincipal == null || securityPrincipal.trim().length() == 0) {
            errorMessageBuilder.append(
                    "  securityPrincipal not specified, must be specified the identity of the principal for authenticating the caller to the service\n");
        }

        if (securityCredentials == null || securityCredentials.trim().length() == 0) {
            errorMessageBuilder.append("  securityCredentials not specified, must be specified as "
                    + "the credentials of the principal for authenticating the caller to the service\n");
        }

        if (producerTopic == null || producerTopic.trim().length() == 0) {
            errorMessageBuilder.append("  producerTopic not specified, must be a string that identifies the JMS topic on which Apex will send events\n");
        }

        if (consumerTopic == null || consumerTopic.trim().length() == 0) {
            errorMessageBuilder.append("  consumerTopic not specified, must be a string that identifies the JMS topic on which Apex will recieve events\n");
        }

        if (consumerWaitTime < 0) {
            errorMessageBuilder.append("  consumerWaitTime [" + consumerWaitTime + "] invalid, must be specified as consumerWaitTime >= 0\n");
        }

        return errorMessageBuilder.toString();
    }
}
