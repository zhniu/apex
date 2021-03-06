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

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for REST as an event carrier technology with Apex as a REST client.
 *
 * The parameters for this plugin are:
 * <ol>
 * <li>standalone: A flag indicating if APEX should start a standalone HTTP server to process REST requests (true) or whether it should use an underlying
 * servlet infrastructure such as Apache Tomcat (False). This parameter is legal only on REST server event inputs.
 * <li>host: The host name to use when setting up a standalone HTTP server. This parameter is legal only on REST server event inputs in standalone mode.
 * <li>port: The port to use when setting up a standalone HTTP server. This parameter is legal only on REST server event inputs in standalone mode.
 * </ol>
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class RESTServerCarrierTechnologyParameters extends CarrierTechnologyParameters {
    // @formatter:off
    private static final int MIN_USER_PORT =  1024;
    private static final int MAX_USER_PORT = 65535;

    /** The label of this carrier technology. */
    public static final String RESTSERVER_CARRIER_TECHNOLOGY_LABEL = "RESTSERVER";

    /** The producer plugin class for the REST carrier technology. */
    public static final String RESTSERVER_EVENT_PRODUCER_PLUGIN_CLASS = ApexRestServerProducer.class.getCanonicalName();

    /** The consumer plugin class for the REST carrier technology. */
    public static final String RESTSERVER_EVENT_CONSUMER_PLUGIN_CLASS = ApexRestServerConsumer.class.getCanonicalName();

    // REST server parameters
    private boolean standalone = false;
    private String  host       = null;
    private int     port       = -1;
    // @formatter:on

    /**
     * Constructor to create a REST carrier technology parameters instance and register the instance with the parameter service.
     */
    public RESTServerCarrierTechnologyParameters() {
        super(RESTServerCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the web socket carrier technology
        this.setLabel(RESTSERVER_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(RESTSERVER_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(RESTSERVER_EVENT_CONSUMER_PLUGIN_CLASS);
    }

    /**
     * Check if the REST server is running in standalone mode or is using an underlying servlet infrastructure to manage requests.
     * @return true if in standalone mode
     */
    public boolean isStandalone() {
        return standalone;
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

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        final StringBuilder errorMessageBuilder = new StringBuilder();

        errorMessageBuilder.append(super.validate());

        // Check if host is defined, it is only defined on REST server consumers
        if (standalone) {
            if (host != null) {
                if (host.trim().length() == 0) {
                    errorMessageBuilder.append("  host not specified, must be host as a string\n");
                }
            }

            // Check if port is defined, it is only defined on REST server consumers
            if (port != -1) {
                if (port < MIN_USER_PORT || port > MAX_USER_PORT) {
                    errorMessageBuilder.append("  port [" + port + "] invalid, must be specified as 1024 <= port <= 6535\n");
                }
            }
        }
        else {
            if (host != null || port != -1) {
                errorMessageBuilder.append("  host and port are specified only in standalone mode\n");
            }
        }

        return errorMessageBuilder.toString();
    }
}
