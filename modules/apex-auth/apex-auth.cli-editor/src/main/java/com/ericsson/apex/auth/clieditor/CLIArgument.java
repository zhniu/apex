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

import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class holds the definition of an argument of a CLI command.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CLIArgument implements Comparable<CLIArgument> {
    private final String argumentName;
    private final boolean nullable;
    private final String description;

    /**
     * This Constructor constructs a non nullable command line argument with a blank name and description.
     */
    public CLIArgument() {
        this("", false, "");
    }

    /**
     * This Constructor constructs a non nullable command line argument with the given name and description.
     *
     * @param incomingArgumentName the argument name
     */
    public CLIArgument(final String incomingArgumentName) {
        this(incomingArgumentName, false, "");
    }

    /**
     * This Constructor constructs a command line argument with the given name, nullability, and description.
     *
     * @param argumentName the argument name
     * @param nullable the nullable
     * @param description the description
     */
    public CLIArgument(final String argumentName, final boolean nullable, final String description) {
        this.argumentName = argumentName;
        this.nullable = nullable;
        this.description = description;
    }

    /**
     * Gets the argument name.
     *
     * @return the argument name
     */
    public String getArgumentName() {
        return argumentName;
    }

    /**
     * Checks if the argument is nullable.
     *
     * @return true, if checks if the argument is nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Gets the argument description.
     *
     * @return the argument description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the argument help.
     *
     * @return the argument help
     */
    public String getHelp() {
        final StringBuilder builder = new StringBuilder();
        builder.append(argumentName);
        builder.append(nullable ? ": (O) " : ": (M) ");
        builder.append(description);
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CLIArgument [argumentName=" + argumentName + ", nullable=" + nullable + ", description=" + description + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final CLIArgument otherArgument) {
        Assertions.argumentNotNull(otherArgument, "comparison object may not be null");

        if (this == otherArgument) {
            return 0;
        }
        if (getClass() != otherArgument.getClass()) {
            return this.hashCode() - otherArgument.hashCode();
        }

        final CLIArgument other = otherArgument;

        if (!argumentName.equals(other.argumentName)) {
            return argumentName.compareTo(other.argumentName);
        }
        if (nullable != other.nullable) {
            return (this.hashCode() - other.hashCode());
        }
        return description.compareTo(otherArgument.description);
    }
}
