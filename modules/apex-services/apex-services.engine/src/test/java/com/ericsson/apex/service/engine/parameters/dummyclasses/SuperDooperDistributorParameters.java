/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * Distributor parameters for the Super Dooper Distributor
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @version 
 */
public class SuperDooperDistributorParameters extends DistributorParameters {
	// Constants for SuperDooper configuration file locations
	public static final String  DEFAULT_SUPER_DOOPER_DISTRIBUTION_CONFIG_FILE   = "superDooper/superDooper.xml";
	public static final String  DEFAULT_SUPER_DOOPER_DISTRIBUTION_JGROUPS_FILE  = "superDooper/jgroups-superDooper-apex.xml";
	public static final boolean DEFAULT_SUPER_DOOPER_JAVA_NET_PREFER_IPV4_STACK = true;
	public static final String  DEFAULT_INFINSPAN_JGROUPS_BIND_ADDRESS        = "localhost";

	// SuperDooper configuration file names
	private String  configFile         = DEFAULT_SUPER_DOOPER_DISTRIBUTION_CONFIG_FILE;
	private String  jgroupsFile        = DEFAULT_SUPER_DOOPER_DISTRIBUTION_JGROUPS_FILE;
	private boolean preferIPv4Stack    = DEFAULT_SUPER_DOOPER_JAVA_NET_PREFER_IPV4_STACK;
	private String  jGroupsBindAddress = DEFAULT_INFINSPAN_JGROUPS_BIND_ADDRESS;

	public SuperDooperDistributorParameters() {
		super(SuperDooperDistributorParameters.class.getCanonicalName());
		ParameterService.registerParameters(SuperDooperDistributorParameters.class, this);
		ParameterService.registerParameters(DistributorParameters          .class, this);
	}
	
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String getJgroupsFile() {
		return jgroupsFile;
	}
	public void setJgroupsFile(String jgroupsFile) {
		this.jgroupsFile = jgroupsFile;
	}
	public boolean preferIPv4Stack() {
		return preferIPv4Stack;
	}
	public void setPreferIPv4Stack(boolean preferIPv4Stack) {
		this.preferIPv4Stack = preferIPv4Stack;
	}
	public String getjGroupsBindAddress() {
		return jGroupsBindAddress;
	}
	public void setjGroupsBindAddress(String jGroupsBindAddress) {
		this.jGroupsBindAddress = jGroupsBindAddress;
	}
	@Override
	public String toString() {
		return "SuperDooperDistributorParameters [configFile=" + configFile + ", jgroupsFile=" + jgroupsFile
				+ ", preferIPv4Stack=" + preferIPv4Stack + ", jGroupsBindAddress=" + jGroupsBindAddress + "]";
	}
}
