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
 * This class compares two keys and returns their differences. It is used in bulk comparisons in models where maps of keys are being compared. The
 * {@link KeyComparer} that is returned does the actual comparison
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <K> the type of key being compared
 */
public class KeyComparer<K> {

    /**
     * Compare two keys and return their differences.
     *
     * @param leftKey The left key of the comparison
     * @param rightKey The right key of the comparison
     * @return The difference between the keys
     */
    public KeyDifference<K> compareKeys(final K leftKey, final K rightKey) {
        return new KeyDifference<>(leftKey, rightKey);
    }
}
