/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.protocol.jms;

import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.JSONEventProtocolParameters;

/**
 * Event protocol parameters for JMS Text messages as an event protocol.
 * <p>
 * Text messages received and sent over JMS in ~Text format are assumed to be in a JSON format that Apex can understand. Therefore this plugin is a subclass of
 * the built in JSON event protocol plugin.
 * <p>
 * On reception of a JMS {@link javax.jms.TextMessage} message, the JMS Text plugin unmarshals the message the JMS text message and passes it to its JSON
 * superclass unmarshaling for processing.
 * <p>
 * When sending an Apex event, the plugin uses its underlying JSON superclass to marshal the event to a JSON string and passes that string to the JSON carrier
 * plugin for sending.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JMSTextEventProtocolParameters extends JSONEventProtocolParameters {
    /** The label of this event protocol. */
    public static final String JMS_TEXT_EVENT_PROTOCOL_LABEL = "JMSTEXT";

    /**
     * Constructor to create a JSON event protocol parameter instance and register the instance with the parameter service.
     */
    public JMSTextEventProtocolParameters() {
        super(JMSTextEventProtocolParameters.class.getCanonicalName(), JMS_TEXT_EVENT_PROTOCOL_LABEL);

        // Set the event protocol properties for the JMS Text event protocol
        this.setLabel(JMS_TEXT_EVENT_PROTOCOL_LABEL);

        // Set the event protocol plugin class
        this.setEventProtocolPluginClass(Apex2JMSTextEventConverter.class.getCanonicalName());
    }
}
