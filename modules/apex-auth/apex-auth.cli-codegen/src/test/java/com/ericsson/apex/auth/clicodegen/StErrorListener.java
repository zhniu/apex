/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clicodegen;

import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

/**
 * Customized ST error listener.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class StErrorListener implements STErrorListener {

    /** Counts errors of the listener. */
    private int errorCount;

    /* (non-Javadoc)
     * @see org.stringtemplate.v4.STErrorListener#IOError(org.stringtemplate.v4.misc.STMessage)
     */
    @Override
    public void IOError(final STMessage msg) {
        switch (msg.error) {
        default:
            this.registerErrors(msg);
        }
    }

    /* (non-Javadoc)
     * @see org.stringtemplate.v4.STErrorListener#compileTimeError(org.stringtemplate.v4.misc.STMessage)
     */
    @Override
    public void compileTimeError(final STMessage msg) {
        switch (msg.error) {
        default:
            this.registerErrors(msg);
        }
    }

    /* (non-Javadoc)
     * @see org.stringtemplate.v4.STErrorListener#internalError(org.stringtemplate.v4.misc.STMessage)
     */
    @Override
    public void internalError(final STMessage msg) {
        switch (msg.error) {
        default:
            this.registerErrors(msg);
        }
    }

    /* (non-Javadoc)
     * @see org.stringtemplate.v4.STErrorListener#runTimeError(org.stringtemplate.v4.misc.STMessage)
     */
    @Override
    public void runTimeError(final STMessage msg) {
        switch (msg.error) {
        case NO_SUCH_PROPERTY:
        case ARGUMENT_COUNT_MISMATCH:
        case ANON_ARGUMENT_MISMATCH:
            break;
        default:
            this.registerErrors(msg);
        }
    }

    /**
     * Registers an error with the local error listener and increases the error count.
     * 
     * @param msg error message
     */
    protected void registerErrors(final STMessage msg) {
        setErrorCount(getErrorCount() + 1);
        System.err.println("STG/ST (" + msg.error + ") " + msg.arg + " -> " + msg.cause);
    }

    /**
     * Gets the error count.
     *
     * @return the error count
     */
    protected int getErrorCount() {
        return errorCount;
    }

    /**
     * Sets the error count.
     *
     * @param errorCount the new error count
     */
    protected void setErrorCount(final int errorCount) {
        this.errorCount = errorCount;
    }
}
