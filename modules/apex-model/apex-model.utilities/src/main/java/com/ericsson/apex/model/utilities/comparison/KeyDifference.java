/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities.comparison;

/**
 * This class is used to template key differences for bulk key comparisons in models. It performs a difference check between two keys.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <KEY> the generic type
 */
public class KeyDifference<KEY> {
    // The keys being compared
    private KEY leftKey;
    private KEY rightKey;

    /**
     * Constructor used to set the keys being compared.
     *
     * @param leftKey the left key that is being compared
     * @param rightKey the right key that is being compared
     */
    public KeyDifference(final KEY leftKey, final KEY rightKey) {
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }

    /**
     * Gets the left key.
     *
     * @return the left key
     */
    public KEY getLeftKey() {
        return leftKey;
    }

    /**
     * Gets the right key.
     *
     * @return the right key
     */
    public KEY getRightKey() {
        return rightKey;
    }

    /**
     * Checks if the left and right keys are equal.
     *
     * @return true, if checks if is equal
     */
    public boolean isEqual() {
        return leftKey.equals(rightKey);
    }

    /**
     * Gets a string representation of the difference between the keys.
     *
     * @param diffsOnly if set, then a blank string is returned if the keys are equal
     * @return the difference between the keys as a string
     */
    public String asString(final boolean diffsOnly) {
        StringBuilder builder = new StringBuilder();

        if (leftKey.equals(rightKey)) {
            if (!diffsOnly) {
                builder.append("left key ");
                builder.append(leftKey);
                builder.append(" equals right key ");
                builder.append(rightKey);
                builder.append('\n');
            }
        }
        else {
            builder.append("left key ");
            builder.append(leftKey);
            builder.append(" and right key ");
            builder.append(rightKey);
            builder.append(" differ\n");
        }

        return builder.toString();
    }
}
