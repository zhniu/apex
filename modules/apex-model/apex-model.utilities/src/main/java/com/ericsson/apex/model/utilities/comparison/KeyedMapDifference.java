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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This class holds the result of a difference check between two keyed maps. Four results are returned in the class. The {@code leftOnly} result is the entries
 * that appear only in the left map. the {@code rightOnly} result is the entries that appear only in the right map. The {@code differentValues} result are the
 * entries that have the same key but different values in the maps being compared. The {@code identicalValues} result are the entries with identical keys and
 * values in both maps being compared.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <K> the generic type
 * @param <V> the generic type
 */
public class KeyedMapDifference<K, V> {
	private static final String KEY = "key=";
    private static final String VALUE = ",value=";
	
	// Three maps to hold the comparison result
    private Map<K, V> leftOnly = new TreeMap<>();
    private Map<K, V> rightOnly = new TreeMap<>();
    private Map<K, V> identicalValues = new TreeMap<>();
    private Map<K, List<V>> differentValues = new TreeMap<>();

    /**
     * Gets the entries that were found only in the left map.
     *
     * @return the entries only in the left map
     */
    public Map<K, V> getLeftOnly() {
        return leftOnly;
    }

    /**
     * Gets the entries that were found only in the right map.
     *
     * @return the entries only in the right map
     */
    public Map<K, V> getRightOnly() {
        return rightOnly;
    }

    /**
     * Gets the entries that were identical (keys and values the same) in both maps.
     *
     * @return the identical entries
     */
    public Map<K, V> getIdenticalValues() {
        return identicalValues;
    }

    /**
     * Gets the entries that had the same key but different values in both maps.
     *
     * @return the entries that were different. There are two values in the list of values for each entry. The first value is the value that was in the left map
     *         and the second value is the value that was in the right map.
     */
    public Map<K, List<V>> getDifferentValues() {
        return differentValues;
    }

    /**
     * Return a string representation of the differences.
     *
     * @param diffsOnly if set, then a blank string is returned if the maps are equal
     * @param keysOnly if set, then a terse string that prints only the keys is returned, otherwise both keys and values are printed
     * @return the string
     */
    public String asString(final boolean diffsOnly, final boolean keysOnly) {
        StringBuilder builder = new StringBuilder();

        if (leftOnly.isEmpty()) {
            if (!diffsOnly) {
                builder.append("*** all left keys in right\n");
            }
        }
        else {
            builder.append("*** list of keys on left only\n");
            for (Entry<K, V> leftEntry : leftOnly.entrySet()) {
                builder.append(KEY);
                builder.append(leftEntry.getKey());
                if (!keysOnly) {
                    builder.append(VALUE);
                    builder.append(leftEntry.getValue());
                }
                builder.append('\n');
            }
        }

        if (leftOnly.isEmpty()) {
            if (!diffsOnly) {
                builder.append("*** all right keys in left\n");
            }
        }
        else {
            builder.append("*** list of keys on right only\n");
            for (Entry<K, V> rightEntry : rightOnly.entrySet()) {
                builder.append(KEY);
                builder.append(rightEntry.getKey());
                if (!keysOnly) {
                    builder.append(VALUE);
                    builder.append(rightEntry.getValue());
                }
                builder.append('\n');
            }
        }

        if (differentValues.isEmpty()) {
            if (!diffsOnly) {
                builder.append("*** all values in left and right are identical\n");
            }
        }
        else {
            builder.append("*** list of differing entries between left and right\n");
            for (Entry<K, List<V>> differentEntry : differentValues.entrySet()) {
                builder.append(KEY);
                builder.append(differentEntry.getKey());
                if (!keysOnly) {
                    builder.append(",values={");
                    boolean first = true;
                    for (V differentEntryValue : differentEntry.getValue()) {
                        builder.append(differentEntryValue);
                        if (first) {
                            first = false;
                        }
                        else {
                            builder.append(',');
                        }
                    }
                    builder.append("}");
                }
                builder.append('\n');
            }
        }

        if (!diffsOnly) {
            builder.append("*** list of identical entries in left and right\n");
            for (Entry<K, V> identicalEntry : identicalValues.entrySet()) {
                builder.append(KEY);
                builder.append(identicalEntry.getKey());
                if (!keysOnly) {
                    builder.append(VALUE);
                    builder.append(identicalEntry.getValue());
                }
                builder.append('\n');
            }
        }

        return builder.toString();
    }
}
