/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;

/**
 * This exception is raised if a runtime error occurs in an Apex policy or in Apex policy handling.
 *
 * @author Liam Fallon
 */
public class PolicyRuntimeException extends ApexRuntimeException {
    private static final long serialVersionUID = -8507246953751956974L;

    /**
     * Instantiates a new apex policy runtime exception with a message.
     *
     * @param message the message
     */
    public PolicyRuntimeException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex policy runtime exception with a message and a caused by exception.
     *
     * @param message the message
     * @param e the exception that caused this exception to be thrown
     */
    public PolicyRuntimeException(final String message, final Exception e) {
        super(message, e);
    }
}
