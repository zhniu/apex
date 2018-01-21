/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.event;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;

/**
 * This class will be called if an error occurs in Apex event handling.
 *
 * @author Liam Fallon
 */
public class EnException extends ApexRuntimeException {
    private static final long serialVersionUID = -8507246953751956974L;

    /**
     * Instantiates a new engine event exception.
     *
     * @param message the message
     */
    public EnException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new engine event exception.
     *
     * @param message the message
     * @param e the e
     */
    public EnException(final String message, final Exception e) {
        super(message, e);
    }
}
