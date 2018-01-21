/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.adaptive.concepts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AutoLearn is used as a Java context for Adaptive auto-learning of trends towards a fixed value in the adaptive domain.
 */
public class AutoLearn implements Serializable {
    private static final long serialVersionUID = 3825970380434170754L;

    private List<Double> avDiffs = null;

    private List<Long> counts = null;

    /**
     * The Constructor creates an AutoLearn concept.
     */
    public AutoLearn() {
    }

    /**
     * Checks if the Autolearn instance is initialized.
     *
     * @return true, if the Autolearn instance is initialized
     */
    public boolean isInitialized() {
        return (avDiffs != null && counts != null);
    }

    /**
     * initializes the auto learning algorithm with the number of convergent variables to use.
     *
     * @param size the number of convergent variables to use
     */
    public void init(final int size) {
        if (avDiffs == null || avDiffs.size() == 0) {
            avDiffs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                avDiffs.add(i, Double.NaN);
            }
        }

        if (counts == null || counts.size() == 0) {
            counts = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                counts.add(i, 0L);
            }
        }
    }

    /**
     * Gets the average difference values of the algorithm.
     *
     * @return the average difference values of the algorithm
     */
    public List<Double> getAvDiffs() {
        return avDiffs;
    }

    /**
     * Sets the average difference values of the algorithm.
     *
     * @param avDiffs the average difference values of the algorithm
     */
    public void setAvDiffs(final List<Double> avDiffs) {
        this.avDiffs = avDiffs;
    }

    /**
     * Check if the average difference values of the algorithm are set.
     *
     * @return true, if check set av diffs
     */
    public boolean checkSetAvDiffs() {
        return ((avDiffs != null) && (!avDiffs.isEmpty()));
    }

    /**
     * Unset the average difference values of the algorithm.
     */
    public void unsetAvDiffs() {
        avDiffs = null;
    }

    /**
     * Gets the count values of the algorithm.
     *
     * @return the count values of the algorithm
     */
    public List<Long> getCounts() {
        return counts;
    }

    /**
     * Sets the count values of the algorithm.
     *
     * @param counts the count values of the algorithm
     */
    public void setCounts(final List<Long> counts) {
        this.counts = counts;
    }

    /**
     * Check if the count values of the algorithm are set.
     *
     * @return true, if the count values of the algorithm are set
     */
    public boolean checkSetCounts() {
        return ((counts != null) && (!counts.isEmpty()));
    }

    /**
     * Unset the count values of the algorithm.
     */
    public void unsetCounts() {
        counts = null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AutoLearn [avDiffs=" + avDiffs + ", counts=" + counts + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((avDiffs == null) ? 0 : avDiffs.hashCode());
        result = prime * result + ((counts == null) ? 0 : counts.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AutoLearn other = (AutoLearn) obj;
        if (avDiffs == null) {
            if (other.avDiffs != null) {
                return false;
            }
        }
        else if (!avDiffs.equals(other.avDiffs)) {
            return false;
        }
        if (counts == null) {
            if (other.counts != null) {
                return false;
            }
        }
        else if (!counts.equals(other.counts)) {
            return false;
        }
        return true;
    }
}
