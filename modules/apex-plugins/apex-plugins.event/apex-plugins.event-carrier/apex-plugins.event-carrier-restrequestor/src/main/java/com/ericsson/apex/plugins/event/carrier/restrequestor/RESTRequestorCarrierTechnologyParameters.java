/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restrequestor;

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for REST as an event carrier technology with Apex issuing a REST request and receiving a REST response.
 *
 * The parameters for this plugin are:
 * <ol>
 * <li>url: The URL that the Apex Rest Requestor will connect to over REST for REST request sending. This parameter is mandatory.
 * <li>httpMethod: The HTTP method to use when making requests over REST, legal values are GET (default), POST, PUT, and DELETE.
 * <li>restRequestTimeout: The time in milliseconds to wait for a REST request to complete.
 * </ol>
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class RESTRequestorCarrierTechnologyParameters extends CarrierTechnologyParameters {
    /** The supported HTTP methods. */
    public enum HTTP_METHOD {
    		GET,
    		PUT,
    		POST,
    		DELETE
    }
    
    /** The label of this carrier technology. */
    public static final String RESTREQUESTOR_CARRIER_TECHNOLOGY_LABEL = "RESTREQUESTOR";

    /** The producer plugin class for the REST carrier technology. */
    public static final String RESTREQUSTOR_EVENT_PRODUCER_PLUGIN_CLASS = ApexRestRequestorProducer.class.getCanonicalName();

    /** The consumer plugin class for the REST carrier technology. */
    public static final String RESTREQUSTOR_EVENT_CONSUMER_PLUGIN_CLASS = ApexRestRequestorConsumer.class.getCanonicalName();

    /** The default HTTP method for request events. */
    public static final HTTP_METHOD DEFAULT_REQUESTOR_HTTP_METHOD = HTTP_METHOD.GET;

    /** The default timeout for REST requests. */
	public static final long DEFAULT_REST_REQUEST_TIMEOUT = 500;

    private String url = null;
    private HTTP_METHOD httpMethod = null;

    /**
     * Constructor to create a REST carrier technology parameters instance and register the instance with the parameter service.
     */
    public RESTRequestorCarrierTechnologyParameters() {
        super(RESTRequestorCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the web socket carrier technology
        this.setLabel(RESTREQUESTOR_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(RESTREQUSTOR_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(RESTREQUSTOR_EVENT_CONSUMER_PLUGIN_CLASS);
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
    public HTTP_METHOD getHttpMethod() {
        return httpMethod;
    }

    /**
     * Sets the HTTP method to use for the REST request.
     *
     * @param httpMethod the HTTP method
     */
    public void setHttpMethod(final HTTP_METHOD httpMethod) {
        this.httpMethod = httpMethod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

	@Override
	public String toString() {
		return "RESTRequestorCarrierTechnologyParameters [url=" + url + ", httpMethod=" + httpMethod + "]";
	}

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        return "";
    }
}
