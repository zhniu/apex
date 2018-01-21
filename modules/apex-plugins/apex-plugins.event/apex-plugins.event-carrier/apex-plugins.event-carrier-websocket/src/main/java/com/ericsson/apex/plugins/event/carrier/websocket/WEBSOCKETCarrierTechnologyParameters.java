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

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for Kafka as an event carrier technology.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class WEBSOCKETCarrierTechnologyParameters extends CarrierTechnologyParameters {
    // @formatter:off
    private static final int MIN_USER_PORT =  1024;
    private static final int MAX_USER_PORT = 65535;

    /** The label of this carrier technology. */
    public static final String WEB_SCOKET_CARRIER_TECHNOLOGY_LABEL = "WEBSOCKET";

    /** The producer plugin class for the web socket carrier technology. */
    public static final String WEB_SCOKET_EVENT_PRODUCER_PLUGIN_CLASS = ApexWebSocketProducer.class.getCanonicalName();

    /** The consumer plugin class for the web socket carrier technology. */
    public static final String KWEB_SCOKET_EVENT_CONSUMER_PLUGIN_CLASS = ApexWebSocketConsumer.class.getCanonicalName();

    // Default parameter values
    private static final String DEFAULT_HOST = "localhost";
    private static final int    DEFAULT_PORT = -1;

    // Web socket parameters
    private boolean wsClient = true;
    private String  host     = DEFAULT_HOST;
    private int     port     = DEFAULT_PORT;
    // @formatter:on

    /**
     * Constructor to create a web socket carrier technology parameters instance and register the instance with the parameter service.
     */
    public WEBSOCKETCarrierTechnologyParameters() {
        super(WEBSOCKETCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the web socket carrier technology
        this.setLabel(WEB_SCOKET_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(WEB_SCOKET_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(KWEB_SCOKET_EVENT_CONSUMER_PLUGIN_CLASS);
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Checks if is ws client.
     *
     * @return true, if checks if is ws client
     */
    public boolean isWsClient() {
        return wsClient;
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

        if (wsClient && (host == null || host.trim().length() == 0)) {
            errorMessageBuilder.append("  host not specified, must be host as a string\n");
        }

        if (port < MIN_USER_PORT || port > MAX_USER_PORT) {
            errorMessageBuilder.append("  port [" + port + "] invalid, must be specified as 1024 <= port <= 6535\n");
        }

        return errorMessageBuilder.toString();
    }
}
