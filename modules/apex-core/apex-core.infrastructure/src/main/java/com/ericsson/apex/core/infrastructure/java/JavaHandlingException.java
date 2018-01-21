/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.java;

/**
 * This class will be called if an error occurs in Java handling.
 *
 * @author Liam Fallon
 */
public class JavaHandlingException extends Exception {
    private static final long serialVersionUID = -6375859029774312663L;

    /**
     * Instantiates a new Java handling exception.
     *
     * @param message the message
     */
    public JavaHandlingException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Java handling exception.
     *
     * @param e the exception to wrap
     */
    public JavaHandlingException(final Exception e) {
        super(e);
    }

    /**
     * Instantiates a new Java handling exception.
     *
     * @param message the message
     * @param e the exception to wrap
     */
    public JavaHandlingException(final String message, final Exception e) {
        super(message, e);
    }
}
