/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * This exception is invoked if an exception occurs in model handling.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexModelException extends ApexException {
    private static final long serialVersionUID = -4245694568321686450L;

    /**
     * Instantiates a new apex model handling exception.
     *
     * @param message the message
     */
    public ApexModelException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex model handling exception.
     *
     * @param message the message
     * @param e the e
     */
    public ApexModelException(final String message, final Exception e) {
        super(message, e);
    }
}
