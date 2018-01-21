/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.eventprotocol;

import com.ericsson.apex.service.parameters.ApexParameterValidator;

/**
 * An event protocol parameter class for character delimited textual event protocols that may be specialized by event protocol plugins that require plugin
 * specific parameters.
 *
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>startChar: starting character delimiter for text blocks containing an event.
 * <li>endChar: ending character delimiter for text blocks containing an event.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class EventProtocolTextCharDelimitedParameters extends EventProtocolParameters implements ApexParameterValidator {
    // The starting and ending character delimiter
    private char startChar = '\0';
    private char endChar = '\0';

    /**
     * Constructor to create an event protocol parameters instance with the name of a sub class of this class.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public EventProtocolTextCharDelimitedParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the start character that delimits the start of text blocks.
     *
     * @return the start char
     */
    public char getStartChar() {
        return startChar;
    }

    /**
     * Sets the start character that delimits the start of text blocks.
     *
     * @param startChar the start character
     */
    public void setStartChar(final char startChar) {
        this.startChar = startChar;
    }

    /**
     * Gets the end character that delimits the end of text blocks.
     *
     * @return the end character
     */
    public char getEndChar() {
        return endChar;
    }

    /**
     * Sets the end character that delimits the end of text blocks.
     *
     * @param endChar the end character
     */
    public void setEndChar(final char endChar) {
        this.endChar = endChar;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters#toString()
     */
    @Override
    public String toString() {
        return "EventProtocolTextCharDelimitedParameters {" + super.toString() + "} [startChar=" + startChar + ", endChar=" + endChar + "]";
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        errorMessageBuilder.append(super.validate());

        if (startChar == '\0') {
            errorMessageBuilder.append("  text character delimited start character has not been specified\n");
        }

        if (endChar == '\0') {
            errorMessageBuilder.append("  text character delimited end character has not been specified\n");
        }

        return errorMessageBuilder.toString();
    }
}
