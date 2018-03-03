/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.eventrequestor;

import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * This class holds the parameters that allows an output event to to be sent back into APEX as one or multiple input events, there are no user defined parameters.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventRequestorCarrierTechnologyParameters extends CarrierTechnologyParameters {
    // @formatter:off
    /** The label of this carrier technology. */
    public static final String EVENT_REQUESTOR_CARRIER_TECHNOLOGY_LABEL    = "EVENT_REQUESTOR";

    /** The producer plugin class for the EVENT_REQUESTOR carrier technology. */
    public static final String EVENT_REQUESTOR_EVENT_PRODUCER_PLUGIN_CLASS = EventRequestorProducer.class.getCanonicalName();

    /** The consumer plugin class for the EVENT_REQUESTOR carrier technology. */
    public static final String EVENT_REQUESTOR_EVENT_CONSUMER_PLUGIN_CLASS = EventRequestorConsumer.class.getCanonicalName();
    // @formatter:on

	/**
     * Constructor to create an event requestor carrier technology parameters instance and register the instance with the parameter service.
     */
    public EventRequestorCarrierTechnologyParameters() {
        super(EventRequestorCarrierTechnologyParameters.class.getCanonicalName());

        // Set the carrier technology properties for the EVENT_REQUESTOR carrier technology
        this.setLabel(EVENT_REQUESTOR_CARRIER_TECHNOLOGY_LABEL);
        this.setEventProducerPluginClass(EVENT_REQUESTOR_EVENT_PRODUCER_PLUGIN_CLASS);
        this.setEventConsumerPluginClass(EVENT_REQUESTOR_EVENT_CONSUMER_PLUGIN_CLASS);
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
