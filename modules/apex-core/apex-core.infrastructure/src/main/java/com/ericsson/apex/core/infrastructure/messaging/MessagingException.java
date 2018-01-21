/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging;

/**
 * This class will be called if an error occurs in Java handling.
 *
 * @author Liam Fallon
 */
public class MessagingException extends Exception {
    private static final long serialVersionUID = -6375859029774312663L;

    /**
     * Instantiates a new messaging exception.
     *
     * @param message the message
     */
    public MessagingException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new messaging exception.
     *
     * @param message the message
     * @param e the e
     */
    public MessagingException(final String message, final Exception e) {
        super(message, e);
    }
}
