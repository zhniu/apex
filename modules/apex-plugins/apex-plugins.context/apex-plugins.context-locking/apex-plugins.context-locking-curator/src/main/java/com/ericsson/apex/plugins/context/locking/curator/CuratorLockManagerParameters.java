/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.locking.curator;

import com.ericsson.apex.context.parameters.LockManagerParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * Bean class for Curator locking parameters.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CuratorLockManagerParameters extends LockManagerParameters {
    // @formatter:off
    /** The default address used to connect to the Zookeeper server. */
    public static final String DEFAULT_ZOOKEEPER_ADDRESS            = "localhost:2181";

    /** The default sleep time to use when connecting to the Zookeeper server. */
    public static final int DEFAULT_ZOOKEEPER_CONNECT_SLEEP_TIME = 1000;

    /** The default number of times to retry failed connections to the Zookeeper server. */
    public static final int DEFAULT_ZOOKEEPER_CONNECT_RETRIES = 3;

    // Curator parameters
    private String zookeeperAddress       = DEFAULT_ZOOKEEPER_ADDRESS;
    private int zookeeperConnectSleepTime = DEFAULT_ZOOKEEPER_CONNECT_SLEEP_TIME;
    private int zookeeperContextRetries   = DEFAULT_ZOOKEEPER_CONNECT_RETRIES;
    // @formatter:on

    /**
     * The Constructor.
     */
    public CuratorLockManagerParameters() {
        super(CuratorLockManagerParameters.class.getCanonicalName());
        ParameterService.registerParameters(CuratorLockManagerParameters.class, this);
    }

    /**
     * Gets the zookeeper address.
     *
     * @return the zookeeper address
     */
    public String getZookeeperAddress() {
        return zookeeperAddress;
    }

    /**
     * Sets the zookeeper address.
     *
     * @param zookeeperAddress the zookeeper address
     */
    public void setZookeeperAddress(final String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
    }

    /**
     * Gets the zookeeper connect sleep time.
     *
     * @return the zookeeper connect sleep time
     */
    public int getZookeeperConnectSleepTime() {
        return zookeeperConnectSleepTime;
    }

    /**
     * Sets the zookeeper connect sleep time.
     *
     * @param zookeeperConnectSleepTime the zookeeper connect sleep time
     */
    public void setZookeeperConnectSleepTime(final int zookeeperConnectSleepTime) {
        this.zookeeperConnectSleepTime = zookeeperConnectSleepTime;
    }

    /**
     * Gets the zookeeper context retries.
     *
     * @return the zookeeper context retries
     */
    public int getZookeeperContextRetries() {
        return zookeeperContextRetries;
    }

    /**
     * Sets the zookeeper context retries.
     *
     * @param zookeeperContextRetries the zookeeper context retries
     */
    public void setZookeeperContextRetries(final int zookeeperContextRetries) {
        this.zookeeperContextRetries = zookeeperContextRetries;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.parameters.LockManagerParameters#toString()
     */
    @Override
    public String toString() {
        return "CuratorLockManagerParameters [zookeeperAddress=" + zookeeperAddress + ", zookeeperConnectSleepTime=" + zookeeperConnectSleepTime
                + ", zookeeperContextRetries=" + zookeeperContextRetries + "]";
    }
}
