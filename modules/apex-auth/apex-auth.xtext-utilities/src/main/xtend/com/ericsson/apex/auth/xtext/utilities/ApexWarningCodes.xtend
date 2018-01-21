/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.utilities

/**
 * A collection of warning messages, also used as code.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
interface ApexWarningCodes {

	/** Warning: an event declaration name does no end with '_decl'. */
	static final String WARN_EVENTDECL_NAME_NODECL = "an event declaration name should end with '_decl'"

	/** Warning: a context album declaration name does no end with '_decl'. */
	static final String WARN_CTXALBUMDECL_NAME_NODECL = "a context album declaration name should end with '_decl'"

	/** Warning: an schema declaration name does no end with '_decl'. */
	static final String WARN_SCHEMADECL_NAME_NODECL = "a schema declaration name should end with '_decl'"
}