/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.deployment;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * The Class ApexDeploymentException is an exception that may be thrown on deployment errors in Apex.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexDeploymentException extends ApexException {
    private static final long serialVersionUID = 1816909564890470707L;

    /**
     * Instantiates a new apex deployment exception.
     *
     * @param message the message
     */
    public ApexDeploymentException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex deployment exception.
     *
     * @param message the message
     * @param e the e
     */
    public ApexDeploymentException(final String message, final Exception e) {
        super(message, e);
    }
}
