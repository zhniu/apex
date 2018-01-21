/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/


/**
 * Defines the Apex engine Java API.
 * The API is used to set up, control, send events to, and receive events from an APEX engine.
 * The {@link ApexEngine} interface is used to control the execution of a single APEX engine thread and to send events to that APEX engine thread.
 * The {@link EnEventListener} interface is used to listen for events being emitted by an APEX engine thread.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
package com.ericsson.apex.core.engine.engine;
