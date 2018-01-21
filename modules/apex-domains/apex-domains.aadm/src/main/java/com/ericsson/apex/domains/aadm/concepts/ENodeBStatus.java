/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.aadm.concepts;

import java.io.Serializable;

/**
 * The Class ENodeBStatus holds the status of an eNodeB in the AADM domain.
 */
public class ENodeBStatus implements Serializable {
    private static final long serialVersionUID = 2852523814242234172L;

    private final String eNodeB;

    private long dosCount = 0;
    private boolean beingProbed = false;

    /**
     * The Constructor initiates the status of the eNodeB.
     *
     * @param eNodeB the e node B
     */
    public ENodeBStatus(final String eNodeB) {
        this.eNodeB = eNodeB;
    }

    /**
     * Gets the eNodeB name.
     *
     * @return the eNodeB name
     */
    public String getENodeB() {
        return eNodeB;
    }

    /**
     * Gets the number of Denial Of Service incidents on the eNodeB.
     *
     * @return the number of Denial Of Service incidents on the eNodeB
     */
    public long getDOSCount() {
        return dosCount;
    }

    /**
     * Sets the number of Denial Of Service incidents on the eNodeB.
     *
     * @param incomingDosCount the number of Denial Of Service incidents on the eNodeB
     */
    public void setDOSCount(final long incomingDosCount) {
        this.dosCount = incomingDosCount;
    }

    /**
     * Increment DOS count.
     *
     * @return the long
     */
    public long incrementDOSCount() {
        return ++dosCount;
    }

    /**
     * Decrement DOS count.
     *
     * @return the long
     */
    public long decrementDOSCount() {
        return --dosCount;
    }

    /**
     * Gets the being probed.
     *
     * @return the being probed
     */
    public boolean getBeingProbed() {
        return beingProbed;
    }

    /**
     * Sets the being probed.
     *
     * @param beingProbed the being probed
     */
    public void setBeingProbed(final boolean beingProbed) {
        this.beingProbed = beingProbed;
    }
}
