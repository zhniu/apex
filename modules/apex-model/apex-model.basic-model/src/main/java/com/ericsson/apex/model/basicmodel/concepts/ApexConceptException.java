/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

/**
 * This class is an exception thrown on Apex Concept errors.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexConceptException extends ApexException {
    private static final long serialVersionUID = -8507246953751956974L;

    /**
     * Instantiates a new apex concept exception.
     *
     * @param message the message on the exception
     */
    public ApexConceptException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex concept exception.
     *
     * @param message the message on the exception
     * @param e the exception that caused this Apex exception
     */
    public ApexConceptException(final String message, final Exception e) {
        super(message, e);
    }
}
