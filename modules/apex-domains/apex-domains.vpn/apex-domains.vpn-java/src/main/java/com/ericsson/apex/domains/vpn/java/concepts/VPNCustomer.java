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
 * The Class VPNCustomer keeps track of customers using VPNs in the VPN domain.
 */
public class VPNCustomer implements Serializable {
    private static final long serialVersionUID = 2852523814242234172L;

    private String customerName;
    private int slaDT = 0;
    private int ytdDT = 0;

    private Set<VPNLink> linksInUse = new LinkedHashSet<>();

    /**
     * The Constructor.
     */
    public VPNCustomer() {
    }

    /**
     * The Constructor creates a Customer.
     *
     * @param name the name
     */
    public VPNCustomer(final String name) {
        customerName = name;
    }

    /**
     * Gets the customer name.
     *
     * @return the customer name
     */
    public String getName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     *
     * @param name the customer name
     */
    public void setName(final String name) {
        customerName = name;
    }

    /**
     * Gets the down time allowance in the Service Level Agreement.
     *
     * @return the down time allowance in the Service Level Agreement
     */
    public int getSlaDT() {
        return slaDT;
    }

    /**
     * Sets the down time allowance in the Service Level Agreement.
     *
     * @param slaDT the down time allowance in the Service Level Agreement
     */
    public void setSlaDT(final int slaDT) {
        this.slaDT = slaDT;
    }

    /**
     * Gets the actual down time of this year to date.
     *
     * @return the the actual down time of this year to date
     */
    public int getYtdDT() {
        return ytdDT;
    }

    /**
     * Sets the actual down time of this year to date.
     *
     * @param ytdDT the the actual down time of this year to date
     */
    public void setYtdDT(final int ytdDT) {
        this.ytdDT = ytdDT;
    }

    /**
     * Gets the links in use by the customer.
     *
     * @return the links in use by the customer
     */
    public Set<VPNLink> getLinksInUse() {
        if (linksInUse == null) {
            linksInUse = new LinkedHashSet<>();
        }
        return linksInUse;
    }

    /**
     * Gets the links in use by the customer as a string.
     *
     * @return the links in use by the customer as a string
     */
    public String getLinksInUseAsString() {
        if (linksInUse == null || linksInUse.size() == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (final VPNLink link : linksInUse) {
            if (first) {
                first = false;
            }
            else {
                builder.append(" ");
            }
            builder.append(link.getName());
        }
        return builder.toString();
    }

    /**
     * Sets the links in use by the customer.
     *
     * @param linksInUse the links in use by the customer
     */
    public void setLinksInUse(final Set<VPNLink> linksInUse) {
        this.linksInUse = linksInUse;
    }

    /**
     * Check if the links in use by the customer are set.
     *
     * @return true, if the links in use by the customer are set
     */
    public boolean checkSetLinksInUse() {
        return (linksInUse != null);
    }

    /**
     * Unset the links in use by the customer.
     */
    public void unsetLinksInUse() {
        linksInUse = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VPNCustomer [customerName=" + customerName + ", slaDT=" + slaDT + ", ytdDT=" + ytdDT + ", linksInUse=" + linksInUse + "]";
    }
}
