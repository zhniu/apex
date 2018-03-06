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
 * This class is a base exception from which all Apex exceptions are sub classes.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexException extends Exception {
    private static final long serialVersionUID = -8507246953751956974L;

    // The object on which the exception was thrown
    private transient Object object = null;

    /**
     * Instantiates a new apex exception.
     *
     * @param message the message on the exception
     */
    public ApexException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new apex exception.
     *
     * @param message the message on the exception
     * @param object the object that the exception was thrown on
     */
    public ApexException(final String message, final Object object) {
        super(message);
        this.object = object;
    }

    /**
     * Instantiates a new apex exception.
     *
     * @param message the message on the exception
     * @param e the exception that caused this Apex exception
     */
    public ApexException(final String message, final Exception e) {
        super(message, e);
    }

    /**
     * Instantiates a new apex exception.
     *
     * @param message the message on the exception
     * @param e the exception that caused this Apex exception
     * @param object the object that the exception was thrown on
     */
    public ApexException(final String message, final Exception e, final Object object) {
        super(message, e);
        this.object = object;
    }

    /**
     * Get the message from this exception and its causes.
     *
     * @return the cascaded messages from this exception and the exceptions that caused it
     */
    public String getCascadedMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getMessage());

        for (Throwable t = this; t != null; t = t.getCause()) {
            builder.append("\ncaused by: ");
            builder.append(t.getMessage());
        }

        return builder.toString();
    }

    /**
     *
     * Get the object on which the exception was thrown.
     *
     * @return The object on which the exception was thrown
     */
    public Object getObject() {
        return object;
    }
}
