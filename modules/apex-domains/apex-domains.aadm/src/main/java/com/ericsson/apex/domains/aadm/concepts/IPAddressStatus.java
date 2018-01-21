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
 * The Class IPAddressStatus holds the status of an IP address in the AADM domain.
 */
public class IPAddressStatus implements Serializable {
    private static final long serialVersionUID = -7402022458317593252L;

    private final String ipAddress;

    private String imsi;

    /**
     * The Constructor sets up the IP address status instance.
     *
     * @param ipAddress the ip address
     */
    public IPAddressStatus(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the IP address.
     *
     * @return the IP address
     */
    public String getIPAddress() {
        return ipAddress;
    }

    /**
     * Gets the IMSI.
     *
     * @return the imsi
     */
    public String getIMSI() {
        return imsi;
    }

    /**
     * Sets the IMSI.
     *
     * @param incomingImsi the imsi
     */
    public void setIMSI(final String incomingImsi) {
        this.imsi = incomingImsi;
    }

    /**
     * Check set IMSI.
     *
     * @return true, if check set IMSI
     */
    public boolean checkSetIMSI() {
        return (imsi != null);
    }
}
