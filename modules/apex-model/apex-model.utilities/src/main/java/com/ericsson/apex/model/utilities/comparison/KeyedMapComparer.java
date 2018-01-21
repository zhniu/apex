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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Compare two maps and returns their differences. The types of the keys and the values in the two maps being comapred must be the same. The class returns
 * entries that are only in the left map, only in the right map, entries that have identical keys and different values and entries that have different keys and
 * different values in a {@link KeyedMapDifference} instance.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <KEY> the type of the keys in the maps being compared
 * @param <VALUE> the type of the values in the maps being compared
 */
public class KeyedMapComparer<KEY, VALUE> {
    /**
     * Compare two maps and return their differences in a {@link KeyedMapDifference} instance.
     *
     * @param leftMap The left map to be compared
     * @param rightMap The right map to be compared
     * @return The common, left only, and right only maps in a {@link KeyedMapDifference} instance
     */
    public KeyedMapDifference<KEY, VALUE> compareMaps(final Map<KEY, VALUE> leftMap, final Map<KEY, VALUE> rightMap) {
        KeyedMapDifference<KEY, VALUE> result = new KeyedMapDifference<KEY, VALUE>();

        // Get the keys that are only in the left map
        Set<KEY> leftOnlyKeys = new TreeSet<KEY>(leftMap.keySet());
        leftOnlyKeys.removeAll(rightMap.keySet());

        // Get the keys that are only in the right map
        Set<KEY> rightOnlyKeys = new TreeSet<KEY>(rightMap.keySet());
        rightOnlyKeys.removeAll(leftMap.keySet());

        // Find the keys common across both maps
        Set<KEY> commonKeys = new TreeSet<KEY>(rightMap.keySet());
        commonKeys.addAll(leftMap.keySet());
        commonKeys.removeAll(leftOnlyKeys);
        commonKeys.removeAll(rightOnlyKeys);

        // Now save the left values
        for (KEY key : leftOnlyKeys) {
            result.getLeftOnly().put(key, leftMap.get(key));
        }

        // Now save the right values
        for (KEY key : rightOnlyKeys) {
            result.getRightOnly().put(key, rightMap.get(key));
        }

        // Save the common values to two maps, an identical and different map
        for (KEY key : commonKeys) {
            // Check if the values are identical in each map
            VALUE leftValue = leftMap.get(key);
            VALUE rightValue = rightMap.get(key);

            // Store as appropriate
            if (leftValue.equals(rightValue)) {
                result.getIdenticalValues().put(key, leftValue);
            }
            else {
                // Store the two values
                List<VALUE> valueList = new ArrayList<VALUE>();
                valueList.add(leftValue);
                valueList.add(rightValue);

                result.getDifferentValues().put(key, valueList);
            }
        }

        return result;
    }
}
