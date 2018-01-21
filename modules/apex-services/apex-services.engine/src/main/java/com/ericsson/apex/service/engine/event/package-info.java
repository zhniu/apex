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
 * Provides a generic externally-facing {@link ApexEvent} class that can be sent into an APEX engine and processed by an APEX engine.
 * It provides the producer {@link ApexEventProducer} producer and {@link ApexEventConsumer} consumer interfaces that APEX uses to
 * send events to and receive events from other systems. It also provides the {@link ApexEventConverter} interface that can be implemented by plugins that wish
 * to convert some external event format into the APEX event format. It also provides a periodic event generator that can be used to send periodic events into
 * an APEX engine for triggering of policies to carry out housekeeping tasks.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
package com.ericsson.apex.service.engine.event;
