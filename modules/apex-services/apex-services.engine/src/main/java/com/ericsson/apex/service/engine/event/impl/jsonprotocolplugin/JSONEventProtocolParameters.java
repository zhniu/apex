/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin;

import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextCharDelimitedParameters;

/**
 * Event protocol parameters for JSON as an event protocol.
 *
 * The parameters for this plugin are:
 * <ol>
 * <li>nameAlias: The field in a JSON event to use as an alias for the event name. This parameter is optional.
 * <li>versionAlias: The field in a JSON event to use as an alias for the event version. This parameter is optional.
 * <li>nameSpaceAlias: The field in a JSON event to use as an alias for the event name space. This parameter is optional.
 * <li>sourceAlias: The field in a JSON event to use as an alias for the event source. This parameter is optional.
 * <li>targetAlias: The field in a JSON event to use as an alias for the event target. This parameter is optional.
 * </ol>
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JSONEventProtocolParameters extends EventProtocolTextCharDelimitedParameters {
    /** The label of this event protocol. */
    public static final String JSON_EVENT_PROTOCOL_LABEL = "JSON";

    // Constants for text block delimiters
    private static final char JSON_TEXT_BLOCK_START_DELIMITER = '{';
    private static final char JSON_TEXT_BLOCK_END_DELIMITER = '}';

    // Aliases for Apex event header fields
    // @formatter:off
    private final String nameAlias      = null;
    private final String versionAlias   = null;
    private final String nameSpaceAlias = null;
    private final String sourceAlias    = null;
    private final String targetAlias    = null;
    // @formatter:on

    /**
     * Constructor to create a JSON event protocol parameter instance and register the instance with the parameter service.
     */
    public JSONEventProtocolParameters() {
        this(JSONEventProtocolParameters.class.getCanonicalName(), JSON_EVENT_PROTOCOL_LABEL);
    }

    /**
     * Constructor to create an event protocol parameters instance with the name of a sub class of this class.
     *
     * @param parameterClassName the class name of a sub class of this class
     * @param eventProtocolLabel the name of the event protocol for this plugin
     */
    public JSONEventProtocolParameters(final String parameterClassName, final String eventProtocolLabel) {
        super(parameterClassName);

        // Set the event protocol properties for the JSON event protocol
        this.setLabel(eventProtocolLabel);

        // Set the starting and ending delimiters for text blocks of JSON events
        this.setStartChar(JSON_TEXT_BLOCK_START_DELIMITER);
        this.setEndChar(JSON_TEXT_BLOCK_END_DELIMITER);

        // Set the event protocol plugin class
        this.setEventProtocolPluginClass(Apex2JSONEventConverter.class.getCanonicalName());
    }

    /**
     * Gets the name alias.
     *
     * @return the name alias
     */
    public String getNameAlias() {
        return nameAlias;
    }

    /**
     * Gets the version alias.
     *
     * @return the version alias
     */
    public String getVersionAlias() {
        return versionAlias;
    }

    /**
     * Gets the name space alias.
     *
     * @return the name space alias
     */
    public String getNameSpaceAlias() {
        return nameSpaceAlias;
    }

    /**
     * Gets the source alias.
     *
     * @return the source alias
     */
    public String getSourceAlias() {
        return sourceAlias;
    }

    /**
     * Gets the target alias.
     *
     * @return the target alias
     */
    public String getTargetAlias() {
        return targetAlias;
    }

}
