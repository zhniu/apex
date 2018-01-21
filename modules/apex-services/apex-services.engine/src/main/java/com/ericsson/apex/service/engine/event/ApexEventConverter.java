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

import java.util.List;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * The Interface ApexEventConverter is used for applications that want to convert arbitrary event types to and from Apex events. Application implement this
 * interface to convert their events to and from Apex events.The Apex service can then use this interface to transparently transfer events into and out of an
 * Apex system.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface ApexEventConverter {

    /**
     * Convert an event of arbitrary type into an Apex event.
     *
     * @param eventOfOtherType the event of some other type to convert
     * @return the apex event
     * @throws ApexException thrown on conversion errors
     */
    List<ApexEvent> toApexEvent(Object eventOfOtherType) throws ApexException;

    /**
     * Convert an Apex event into an event of arbitrary type {@code OTHER_EVENT_TYPE}.
     *
     * @param apexEvent the apex event to convert
     * @return the event converted into the other type
     * @throws ApexException thrown on conversion errors
     */
    Object fromApexEvent(ApexEvent apexEvent) throws ApexException;
}
