/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * Exceptions from the Apex editor.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexEditorException extends ApexException {
    private static final long serialVersionUID = 4867385591967018254L;

    /**
     * Instantiates a new apex editor exception.
     *
     * @param message the message on the exception
     */
    public ApexEditorException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex editor exception.
     *
     * @param message the message on the exception
     * @param object the object that the exception was thrown on
     */
    public ApexEditorException(final String message, final Object object) {
        super(message, object);
    }

    /**
     * Instantiates a new apex editor exception.
     *
     * @param message the message on the exception
     * @param e the exception that caused this Apex exception
     */
    public ApexEditorException(final String message, final Exception e) {
        super(message, e);
    }

    /**
     * Instantiates a new apex editor exception.
     *
     * @param message the message on the exception
     * @param e the exception that caused this Apex exception
     * @param object the object that the exception was thrown on
     */
    public ApexEditorException(final String message, final Exception e, final Object object) {
        super(message, e, object);
    }

}
