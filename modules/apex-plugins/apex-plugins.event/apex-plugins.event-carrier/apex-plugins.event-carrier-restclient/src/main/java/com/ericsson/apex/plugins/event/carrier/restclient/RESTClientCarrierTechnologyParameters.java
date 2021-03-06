/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restclient;

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for REST as an event carrier technology with Apex as a REST client.
 *
 * The parameters for this plugin are:
 * <ol>
 * <li>url: The URL that the Apex Rest client will connect to over REST for event reception or event sending. This parameter is mandatory.
 * <li>httpMethod: The HTTP method to use when sending events over REST, legal values are POST (default) and PUT. When receiving events, the REST client plugin
 * always uses the HTTP GET method.
 * </ol>
 * 
 * @author Joss Armstrong (joss.armstrong@ericsson.com)
 */
public class RESTClientCarrierTechnologyParameters extends CarrierTechnologyParameters {

    /** The label of this carrier technology. */
    public static final String RESTCLIENT_CARRIER_TECHNOLOGY_LABEL = "RESTCLIENT";

    /** The producer plugin class for the REST carrier technology. */
    public static final String RESTCLIENT_EVENT_PRODUCER_PLUGIN_CLASS = ApexRestClientProducer.class.getCanonicalName();

    /** The consumer plugin class for the REST carrier technology. */
    public static final String RESTCLIENT_EVENT_CONSUMER_PLUGIN_CLASS = ApexRestClientConsumer.class.getCanonicalName();

    /** The default HTTP method for output of events. */
    public static final String DEFAULT_PRODUCER_HTTP_METHOD = "POST";

    /** The HTTP method for input of events. */
    public static final String CONSUMER_HTTP_METHOD = "GET";

    private String url = null;
    private String httpMethod = null;

    /**
     * Constructor to create a REST carrier technology parameters instance and register the instance with the parameter service.
     */
    public RESTClientCarrierTechnologyParameters() {
        super(RESTClientCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the web socket carrier technology
        this.setLabel(RESTCLIENT_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(RESTCLIENT_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(RESTCLIENT_EVENT_CONSUMER_PLUGIN_CLASS);

    }

    /**
     * Gets the URL for the REST request.
     *
     * @return the URL
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the URL for the REST request.
     *
     * @param incomingURL the URL
     */
    public void setURL(final String incomingURL) {
        this.url = incomingURL;
    }

    /**
     * Gets the HTTP method to use for the REST request.
     *
     * @return the HTTP method
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * Sets the HTTP method to use for the REST request.
     *
     * @param httpMethod the HTTP method
     */
    public void setHttpMethod(final String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		return "RESTClientCarrierTechnologyParameters [url=" + url + ", httpMethod=" + httpMethod + "]";
	}

 
    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        final StringBuilder errorMessageBuilder = new StringBuilder();

        // Check if the URL has been set for event output
        if (getURL() == null) {
            errorMessageBuilder.append("  no URL has been set for event sending on REST client");
        }

        return errorMessageBuilder.toString();
    }
}
