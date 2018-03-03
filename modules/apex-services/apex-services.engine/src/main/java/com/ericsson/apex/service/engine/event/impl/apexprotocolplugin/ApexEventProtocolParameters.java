/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.apexprotocolplugin;

import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * Event protocol parameters for JSON as an event protocol, there are no user defined parameters.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexEventProtocolParameters extends EventProtocolParameters {
    /** The label of this event protocol. */
    public static final String APEX_EVENT_PROTOCOL_LABEL = "APEX";

    /**
     * Constructor to create a JSON event protocol parameter instance and register the instance with the parameter service.
     */
    public ApexEventProtocolParameters() {
        this(ApexEventProtocolParameters.class.getCanonicalName(), APEX_EVENT_PROTOCOL_LABEL);
    }

    /**
     * Constructor to create an event protocol parameters instance with the name of a sub class of this class.
     *
     * @param parameterClassName the class name of a sub class of this class
     * @param eventProtocolLabel the name of the event protocol for this plugin
     */
    public ApexEventProtocolParameters(final String parameterClassName, final String eventProtocolLabel) {
        super(parameterClassName);

        // Set the event protocol properties for the JSON event protocol
        this.setLabel(eventProtocolLabel);

        // Set the event protocol plugin class
        this.setEventProtocolPluginClass(Apex2ApexEventConverter.class.getCanonicalName());
    }
}
