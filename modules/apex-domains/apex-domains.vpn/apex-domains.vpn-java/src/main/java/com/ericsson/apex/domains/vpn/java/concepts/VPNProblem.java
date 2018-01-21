/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.java.concepts;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The Class VPNProblem keeps tracks of problems on VPN links in the VPN domain.
 */
public class VPNProblem implements Serializable {
    private static final long serialVersionUID = 2852523814242234172L;

    private String linkName;
    private long startTime;
    private long endTime;

    private Set<VPNCustomer> affectedCustomers = new LinkedHashSet<>();

    /**
     * The Default Constructor creates a VPN problem with its link name not set.
     */
    public VPNProblem() {
    }

    /**
     * This Constructor creates a VPN problem with its link name set to the given value.
     *
     * @param linkName the problem name
     */
    public VPNProblem(final String linkName) {
        this.linkName = linkName;
    }

    /**
     * Gets the link name on which the problem was observed.
     *
     * @return the link name on which the problem was observed
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * Sets the link name on which the problem was observed.
     *
     * @param linkName the link name on which the problem was observed
     */
    public void setLinkName(final String linkName) {
        this.linkName = linkName;
    }

    /**
     * Gets the start time of the problem.
     *
     * @return the start time of the problem
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the problem.
     *
     * @param startTime the start time of the problem
     */
    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the problem.
     *
     * @return the end time of the problem
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the problem.
     *
     * @param endTime the end time of the problem
     */
    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the customers affected by the problem.
     *
     * @return the customers affected by the problem
     */
    public Set<VPNCustomer> getAffectedCustomers() {
        if (affectedCustomers == null) {
            affectedCustomers = new LinkedHashSet<>();
        }
        return affectedCustomers;
    }

    /**
     * Gets the customers affected by the problem as a string.
     *
     * @return the customers affected by the problem as a string
     */
    public String getAffectedCustomersAsString() {
        if (affectedCustomers == null || affectedCustomers.size() == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (final VPNCustomer customer : affectedCustomers) {
            if (first) {
                first = false;
            }
            else {
                builder.append(" ");
            }
            builder.append(customer.getName());
        }
        return builder.toString();
    }

    /**
     * Sets the customers affected by the problem.
     *
     * @param affectedCustomers the customers affected by the problem
     */
    public void setAffectedCustomers(final Set<VPNCustomer> affectedCustomers) {
        this.affectedCustomers = affectedCustomers;
    }

    /**
     * Check if the customers affected by the problem is set.
     *
     * @return true, if the customers affected by the problem is set
     */
    public boolean checkSetAffectedCustomers() {
        return (affectedCustomers != null);
    }

    /**
     * Unset the customers affected by the problem.
     */
    public void unsetAffectedCustomers() {
        affectedCustomers = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VPNProblem [linkName=" + linkName + ", startTime=" + startTime + ", endTime=" + endTime + ", affectedCustomers=" + affectedCustomers
                + "]";
    }
}
