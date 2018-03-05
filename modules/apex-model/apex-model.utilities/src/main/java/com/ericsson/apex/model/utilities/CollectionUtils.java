/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities;

import java.util.List;
import java.util.ListIterator;

/**
 * This is common utility class with static methods for handling collections.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class CollectionUtils {
    /**
     * Private constructor used to prevent sub class instantiation.
     */
    private CollectionUtils() {
    }

    /**
     * Compare two lists, checks for equality, then for equality on members.
     *
     * @param <T> The type of the lists being compared
     * @param leftList The leftmost List
     * @param rightList The rightmost list
     * @return an integer indicating how different the lists are
     */
    public static <T> int compareLists(final List<? extends Comparable<T>> leftList, final List<? extends Comparable<T>> rightList) {
        // Check for nulls
        if (leftList == null && rightList == null) {
            return 0;
        }
        if (leftList != null && rightList == null) {
            return -1;
        }
        if (leftList == null) {
            return 1;
        }

        // Check for equality
        if (leftList.equals(rightList)) {
            return 0;
        }
        
        return compareListEntries(leftList, rightList);
    }

    /**
     * Compare two lists for equality on members.
     *
     * @param <T> The type of the lists being compared
     * @param leftList The leftmost List
     * @param rightList The rightmost list
     * @return an integer indicating how different the lists are
     */
    private static <T> int compareListEntries(final List<? extends Comparable<T>> leftList, final List<? extends Comparable<T>> rightList) {
        
        // Iterate down the lists till we find a difference
        final ListIterator<?> leftIterator = leftList.listIterator();
        final ListIterator<?> rightIterator = rightList.listIterator();

        while (true) {
            // Check the iterators
            if (!leftIterator.hasNext() && !rightIterator.hasNext()) {
                return 0;
            }
            if (leftIterator.hasNext() && !rightIterator.hasNext()) {
                return -1;
            }
            if (!leftIterator.hasNext() && rightIterator.hasNext()) {
                return 1;
            }

            // Get the next objects
            @SuppressWarnings("unchecked")
            final T leftObject = (T) leftIterator.next();
            @SuppressWarnings("unchecked")
            final T rightObject = (T) rightIterator.next();

            // Compare the objects
            @SuppressWarnings("unchecked")
            final int comparisonResult = ((Comparable<T>) leftObject).compareTo(rightObject);

            // Check the comparison result
            if (comparisonResult != 0) {
                return comparisonResult;
            }
        }
	}
}
