/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event;

/**
 * This interface is used by an Apex event consumer {@link ApexEventConsumer} consumer to pass a received event to Apex.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface ApexEventReceiver {
    /**
     * Receive an event from a consumer for processing.
     *
     * @param executionId the unique ID for execution of this event
     * @param event the event to receive
     * @throws ApexEventException on exceptions receiving an event into Apex
     */
    void receiveEvent(long executionId, Object event) throws ApexEventException;

    /**
     * Receive an event from a consumer for processing.
     *
     * @param event the event to receive
     * @throws ApexEventException on exceptions receiving an event into Apex
     */
    void receiveEvent(Object event) throws ApexEventException;
}
