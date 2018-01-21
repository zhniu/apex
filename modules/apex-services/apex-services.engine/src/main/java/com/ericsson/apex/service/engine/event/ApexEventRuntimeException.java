/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;

/**
 * This exception will be called if a runtime error occurs in Apex event handling.
 *
 * @author Liam Fallon
 */
public class ApexEventRuntimeException extends ApexRuntimeException {
    private static final long serialVersionUID = -8507246953751956974L;

    /**
     * Instantiates a new apex runtime event exception with a message.
     *
     * @param message the message
     */
    public ApexEventRuntimeException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex runtime event exception with a message and a caused by exception.
     *
     * @param message the message
     * @param e the exception that caused this exception to be thrown
     */
    public ApexEventRuntimeException(final String message, final Exception e) {
        super(message, e);
    }
}
