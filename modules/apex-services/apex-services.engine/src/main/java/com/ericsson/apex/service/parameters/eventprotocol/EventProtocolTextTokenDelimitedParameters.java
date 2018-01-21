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
 * An event protocol parameter class for token delimited textual event protocols that may be specialized by event protocol plugins that require plugin
 * specific parameters.
 *
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>delimiterToken: the token string that delimits text blocks that contain events.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class EventProtocolTextTokenDelimitedParameters extends EventProtocolParameters implements ApexParameterValidator {
    // The delimiter token for text blocks
    private String delimiterToken = null;

    /**
     * Constructor to create an event protocol parameters instance with the name of a sub class of this class.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public EventProtocolTextTokenDelimitedParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the delimiter token that delimits events in the text.
     *
     * @return the delimiter token
     */
    public String getDelimiterToken() {
        return delimiterToken;
    }


    /**
     * Sets the delimiter token that delimits events in the text.
     *
     * @param delimiterToken the delimiter token
     */
    public void setDelimiterToken(final String delimiterToken) {
        this.delimiterToken = delimiterToken;
    }


    @Override
    public String toString() {
        return "EventProtocolTextCharDelimitedParameters {" + super.toString() + "} [delimiterToken=" + delimiterToken + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        errorMessageBuilder.append(super.validate());

        if (delimiterToken == null || delimiterToken.length() == 0) {
            errorMessageBuilder.append("  text delimiter token not specified or is blank\n");
        }

        return errorMessageBuilder.toString();
    }
}
