/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

/**
 * A run time exception used to report parsing and command input errors.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CLIException extends IllegalArgumentException {
    private static final long serialVersionUID = 6520231162404452427L;

    /**
     * Create a CLIException with a message.
     *
     * @param message the message
     */
    public CLIException(final String message) {
        super(message);
    }

    /**
     * Create a CLIException with a message and an exception.
     *
     * @param message the message
     * @param t the t
     */
    public CLIException(final String message, final Throwable t) {
        super(message, t);
    }
}
