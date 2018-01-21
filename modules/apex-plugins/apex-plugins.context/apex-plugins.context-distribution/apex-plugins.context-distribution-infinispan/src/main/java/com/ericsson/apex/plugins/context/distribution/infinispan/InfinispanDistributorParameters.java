/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.distribution.infinispan;

import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * Distributor parameters for the Infinspan Distributor.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class InfinispanDistributorParameters extends DistributorParameters {
    // @formatter:off

    /** The default Infinispan configuration file location. */
    public static final String  DEFAULT_INFINISPAN_DISTRIBUTION_CONFIG_FILE = "/infinispan/infinispan.xml";

    /** The default Infinispan jgroups configuration file location. */
    public static final String  DEFAULT_INFINISPAN_DISTRIBUTION_JGROUPS_FILE = null;

    /** The default Infinispan IP stack is IPV4. */
    public static final boolean DEFAULT_INFINISPAN_JAVA_NET_PREFER_IPV4_STACK = true;

    /** The default Infinispan bind address is localhost. */
    public static final String  DEFAULT_INFINSPAN_JGROUPS_BIND_ADDRESS = "localhost";

    // Infinspan configuration file names
    private String configFile         = DEFAULT_INFINISPAN_DISTRIBUTION_CONFIG_FILE;
    private String jgroupsFile        = DEFAULT_INFINISPAN_DISTRIBUTION_JGROUPS_FILE;
    private boolean preferIPv4Stack   = DEFAULT_INFINISPAN_JAVA_NET_PREFER_IPV4_STACK;
    private String jGroupsBindAddress = DEFAULT_INFINSPAN_JGROUPS_BIND_ADDRESS;
    // @formatter:on

    /**
     * The Constructor.
     */
    public InfinispanDistributorParameters() {
        super(InfinispanDistributorParameters.class.getCanonicalName());
        ParameterService.registerParameters(InfinispanDistributorParameters.class, this);
        ParameterService.registerParameters(DistributorParameters.class, this);
    }

    /**
     * Gets the config file.
     *
     * @return the config file
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * Sets the config file.
     *
     * @param configFile the config file
     */
    public void setConfigFile(final String configFile) {
        this.configFile = configFile;
    }

    /**
     * Gets the jgroups file.
     *
     * @return the jgroups file
     */
    public String getJgroupsFile() {
        return jgroupsFile;
    }

    /**
     * Sets the jgroups file.
     *
     * @param jgroupsFile the jgroups file
     */
    public void setJgroupsFile(final String jgroupsFile) {
        this.jgroupsFile = jgroupsFile;
    }

    /**
     * Prefer I pv 4 stack.
     *
     * @return true, if prefer I pv 4 stack
     */
    public boolean preferIPv4Stack() {
        return preferIPv4Stack;
    }

    /**
     * Sets the prefer I pv 4 stack.
     *
     * @param preferIPv4Stack the prefer I pv 4 stack
     */
    public void setPreferIPv4Stack(final boolean preferIPv4Stack) {
        this.preferIPv4Stack = preferIPv4Stack;
    }

    /**
     * Getj groups bind address.
     *
     * @return the j groups bind address
     */
    public String getjGroupsBindAddress() {
        return jGroupsBindAddress;
    }

    /**
     * Setj groups bind address.
     *
     * @param jGroupsBindAddress the j groups bind address
     */
    public void setjGroupsBindAddress(final String jGroupsBindAddress) {
        this.jGroupsBindAddress = jGroupsBindAddress;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.context.parameters.DistributorParameters#toString()
     */
    @Override
    public String toString() {
        return "InfinispanDistributorParameters [configFile=" + configFile + ", jgroupsFile=" + jgroupsFile + ", preferIPv4Stack=" + preferIPv4Stack
                + ", jGroupsBindAddress=" + jGroupsBindAddress + "]";
    }
}
