/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.JSONEventProtocolParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextTokenDelimitedParameters;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SuperTokenDelimitedEventProtocolParameters extends EventProtocolTextTokenDelimitedParameters {
    /** The label of this carrier technology. */
    public static final String SUPER_TOKEN_EVENT_PROTOCOL_LABEL = "SUPER_TOK_DEL";

    // Constants for text delimiter tokeb
    private static final String SUPER_TOKEN_DELIMITER = "SuperToken";

    /**
     * Constructor to create a JSON event protocol parameter instance and register the instance with the parameter service.
     */
    public SuperTokenDelimitedEventProtocolParameters() {
        super(JSONEventProtocolParameters.class.getCanonicalName());
        ParameterService.registerParameters(SuperTokenDelimitedEventProtocolParameters.class, this);

        // Set the event protocol properties for the JSON carrier technology
        this.setLabel(SUPER_TOKEN_EVENT_PROTOCOL_LABEL);

        // Set the starting and ending delimiters for text blocks of JSON events
        this.setDelimiterToken(SUPER_TOKEN_DELIMITER);

        // Set the event protocol plugin class
        this.setEventProtocolPluginClass(SuperTokenDelimitedEventConverter.class.getCanonicalName());
    }
}
