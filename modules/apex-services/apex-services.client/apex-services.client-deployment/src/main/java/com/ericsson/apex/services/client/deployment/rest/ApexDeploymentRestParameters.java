/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.deployment.rest;

import java.net.URI;

/**
 * This class reads and handles command line parameters to the Apex RESTful services
 * 
 * User: ewatkmi Date: 31 Jul 2017
 */
public class ApexDeploymentRestParameters {
	public static final int DEFAULT_REST_PORT     = 18989;
	public static final int INFINITY_TIME_TO_LIVE = -1;

	// Base URI the HTTP server will listen on
	private static final String DEFAULT_SERVER_URI_ROOT  = "http://localhost:";
	private static final String DEFAULT_REST_PATH        = "/apexservices/";
	private static final String DEFAULT_STATIC_PATH      = "/";

	// Package that will field REST requests
	public static final String[] DEFAULT_PACKAGES = new String[] {
			"com.ericsson.apex.services.client.deployment.rest"
	};

	// The services parameters
	private boolean helpSet    = false;
	private int     restPort   = DEFAULT_REST_PORT;
	private long    timeToLive = INFINITY_TIME_TO_LIVE;

	public String validate() {
		String validationMessage = "";
		validationMessage += validatePort(); 
		validationMessage += validateTimeToLive();
		
		return validationMessage;
	}

	public URI getBaseURI() {
		return URI.create(DEFAULT_SERVER_URI_ROOT + restPort + DEFAULT_REST_PATH);
	}

	public String[] getRESTPackages() {
		return DEFAULT_PACKAGES;
	}
	
	public String getStaticPath() {
		return DEFAULT_STATIC_PATH;
	}
	
	private String validatePort() {
		if (restPort < 1024 || restPort > 65535) {
			return "port must be greater than 1023 and less than 65536\n";
		}
		else {
			return "";
		}
	}

	private String validateTimeToLive() {
		if (timeToLive < -1) {
			return "time to live must be greater than -1 (set to -1 to wait forever)\n";
		}
		else {
			return "";
		}
	}
	
	public boolean isHelpSet() {
		return helpSet;
	}
	public void setHelp(boolean helpSet) {
		this.helpSet = helpSet;
	}
	public int getRESTPort() {
		return restPort;
	}
	public void setRESTPort(final int restPort) {
		this.restPort = restPort;
	}
	public long getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(final long timeToLive) {
		this.timeToLive = timeToLive;
	}
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(this.getClass().getSimpleName())
			.append(": URI=").append(this.getBaseURI())
			.append(", TTL=").append(this.getTimeToLive())
			.append("sec");
		return ret.toString();
	}
}
