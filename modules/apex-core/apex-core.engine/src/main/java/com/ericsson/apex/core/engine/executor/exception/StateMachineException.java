/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.executor.exception;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * This class will be called if an error occurs in an Apex state machine.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class StateMachineException extends ApexException {
    private static final long serialVersionUID = -4245694568321686450L;

    /**
     * Instantiates a new state machine exception.
     *
     * @param message the message
     */
    public StateMachineException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new state machine exception.
     *
     * @param message the message
     * @param e the e
     */
    public StateMachineException(final String message, final Exception e) {
        super(message, e);
    }
}
