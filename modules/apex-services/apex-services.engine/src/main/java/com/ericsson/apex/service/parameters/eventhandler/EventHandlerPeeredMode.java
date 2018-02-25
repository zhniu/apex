/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.eventhandler;

/**
 * This enum specifies the peered mode that an event handler may be in.
 * 
 * <p>
 * The following values are defined:
 * <ol>
 * <li>SYNCHRONOUS: The event handler is tied to another event handler for event handling in APEX, used for request-response calls where APEX is the receiver.
 * <li>REQUESTOR: The event handler is tied another event handler for event handling in APEX, used for request-response calls where APEX is the sender.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 *  
 * @author liam.fallon@ericsson.com
 *
 */
public enum EventHandlerPeeredMode {
	SYNCHRONOUS,
	REQUESTOR
}
