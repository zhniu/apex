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
 * @param <KEY> the generic type
 * @param <VALUE> the generic type
 */
public class KeyedMapDifference<KEY, VALUE> {
    // Three maps to hold the comparison result
    private Map<KEY, VALUE> leftOnly = new TreeMap<KEY, VALUE>();
    private Map<KEY, VALUE> rightOnly = new TreeMap<KEY, VALUE>();
    private Map<KEY, VALUE> identicalValues = new TreeMap<KEY, VALUE>();
    private Map<KEY, List<VALUE>> differentValues = new TreeMap<KEY, List<VALUE>>();

    /**
     * Gets the entries that were found only in the left map.
     *
     * @return the entries only in the left map
     */
    public Map<KEY, VALUE> getLeftOnly() {
        return leftOnly;
    }

    /**
     * Gets the entries that were found only in the right map.
     *
     * @return the entries only in the right map
     */
    public Map<KEY, VALUE> getRightOnly() {
        return rightOnly;
    }

    /**
     * Gets the entries that were identical (keys and values the same) in both maps.
     *
     * @return the identical entries
     */
    public Map<KEY, VALUE> getIdenticalValues() {
        return identicalValues;
    }

    /**
     * Gets the entries that had the same key but different values in both maps.
     *
     * @return the entries that were different. There are two values in the list of values for each entry. The first value is the value that was in the left map
     *         and the second value is the value that was in the right map.
     */
    public Map<KEY, List<VALUE>> getDifferentValues() {
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
            for (Entry<KEY, VALUE> leftEntry : leftOnly.entrySet()) {
                builder.append("key=");
                builder.append(leftEntry.getKey());
                if (!keysOnly) {
                    builder.append(",value=");
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
            for (Entry<KEY, VALUE> rightEntry : rightOnly.entrySet()) {
                builder.append("key=");
                builder.append(rightEntry.getKey());
                if (!keysOnly) {
                    builder.append(",value=");
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
            for (Entry<KEY, List<VALUE>> differentEntry : differentValues.entrySet()) {
                builder.append("key=");
                builder.append(differentEntry.getKey());
                if (!keysOnly) {
                    builder.append(",values={");
                    boolean first = true;
                    for (VALUE differentEntryValue : differentEntry.getValue()) {
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
            for (Entry<KEY, VALUE> identicalEntry : identicalValues.entrySet()) {
                builder.append("key=");
                builder.append(identicalEntry.getKey());
                if (!keysOnly) {
                    builder.append(",value=");
                    builder.append(identicalEntry.getValue());
                }
                builder.append('\n');
            }
        }

        return builder.toString();
    }
}
