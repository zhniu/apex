/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

/**
 * This class represents an argument used on a command and its value.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CLIArgumentValue {
    private final CLIArgument cliArgument;
    private boolean specified;
    private String value;

    /**
     * The Constructor creates an argument value for the given argument, has not been set, and has no value.
     *
     * @param cliArgument the argument for which this object is a value
     */
    public CLIArgumentValue(final CLIArgument cliArgument) {
        this.cliArgument = cliArgument;
        specified = false;
        value = null;
    }

    /**
     * Gets the argument for which this object is a value.
     *
     * @return the argument for which this object is a value
     */
    public CLIArgument getCliArgument() {
        return cliArgument;
    }

    /**
     * Checks if the argument value is specified.
     *
     * @return true, if the argument value is specified
     */
    public boolean isSpecified() {
        return specified;
    }

    /**
     * Gets the argument value.
     *
     * @return the argument value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the argument value.
     *
     * @param value the argument value
     */
    public void setValue(final String value) {
        this.value = value;
        specified = true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CLIArgumentValue [cliArgument=" + cliArgument + ", specified=" + specified + ", value=" + value + "]";
    }
}
