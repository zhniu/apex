/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

/**
 * A run time exception used to report parsing and parameter input errors.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexEditorParameterException extends IllegalArgumentException {
    private static final long serialVersionUID = 6520231162404452427L;

    /**
     * Create an ApexEditorParameterException with a message.
     *
     * @param message the message
     */
    public ApexEditorParameterException(final String message) {
        super(message);
    }

    /**
     * Create an ApexEditorParameterException with a message and an exception.
     *
     * @param message the message
     * @param t the t
     */
    public ApexEditorParameterException(final String message, final Throwable t) {
        super(message, t);
    }
}
