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

/**
 * The Class VPNLink is used to keep track of VPN links in the VPN domain.
 */
public class VPNLink implements Serializable {
    private static final long serialVersionUID = -260094918747241348L;

    private String linkName;
    private boolean active = true;

    /**
     * The Default Constructor creates a VPN link with no link name.
     */
    public VPNLink() {
    }

    /**
     * This Constructor creates a VPN link with the given name.
     *
     * @param name the VPN link name
     */
    public VPNLink(final String name) {
        linkName = name;
    }

    /**
     * Gets the VPN link name.
     *
     * @return the VPN link name
     */
    public String getName() {
        return linkName;
    }

    /**
     * Sets the VPN link name.
     *
     * @param name the VPN link name
     */
    public void setName(final String name) {
        linkName = name;
    }

    /**
     * Gets whether the VPN link is active.
     *
     * @return true if the VPN link is active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Sets the whether the VPN link is active.
     *
     * @param flag true if the VPN link is active
     */
    public void setActive(final boolean flag) {
        active = flag;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VPNLink [linkName=" + linkName + ", active=" + active + "]";
    }
}
