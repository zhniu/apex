/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.monitoring.rest;

/**
 * A run time exception used to report parsing and parameter input errors
 * 
 * User: ewatkmi Date: 31 Jul 2017
 */
public class ApexMonitoringRestParameterException extends IllegalArgumentException {
	private static final long serialVersionUID = 6520231162404452427L;

	/**
	 * Create an ApexServicesRestParameterException with a message.
	 * @param message the message
	 */
	public ApexMonitoringRestParameterException(String message) {
		super(message);
	}

	/**
	 * Create an ApexServicesRestParameterException with a message and an exception.
	 * @param message the message
	 * @param throwable The exception that caused the exception
	 */
	public ApexMonitoringRestParameterException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
