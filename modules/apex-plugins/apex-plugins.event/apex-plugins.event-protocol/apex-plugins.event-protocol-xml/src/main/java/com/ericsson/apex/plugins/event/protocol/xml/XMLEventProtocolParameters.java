/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.protocol.xml;

import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextTokenDelimitedParameters;

/**
 * Event protocol parameters for XML as an event protocol.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class XMLEventProtocolParameters extends EventProtocolTextTokenDelimitedParameters {
    /** The label of this carrier technology. */
    public static final String XML_EVENT_PROTOCOL_LABEL = "XML";

    // Constants for the text delimiter token
    private static final String XML_TEXT_DELIMITER_TOKEN = "<?xml";

    /**
     * Constructor to create a JSON event protocol parameter instance and register the instance with the parameter service.
     */
    public XMLEventProtocolParameters() {
        super(XMLEventProtocolParameters.class.getCanonicalName());

        // Set the event protocol properties for the XML event protocol
        this.setLabel(XML_EVENT_PROTOCOL_LABEL);

        // Set the starting and ending delimiters for text blocks of XML events
        this.setDelimiterToken(XML_TEXT_DELIMITER_TOKEN);

        // Set the event protocol plugin class
        this.setEventProtocolPluginClass(Apex2XMLEventConverter.class.getCanonicalName());
    }
}
