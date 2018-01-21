/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.runtime;

import com.ericsson.apex.service.engine.event.ApexEvent;

/**
 * The listener interface for receiving apexEvent events.
 * The class that is interested in processing a apexEvent event implements this interface, and the object
 * created with that class is registered with a component using the component's {@code addApexEventListener} method.
 * When the apexEvent event occurs, that object's appropriate method is invoked.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
public interface ApexEventListener {

    /**
     * Callback method called on implementations of this interface when APEX emits an event.
     *
     * @param apexEvent the apex event emitted by APEX
     */
    void onApexEvent(ApexEvent apexEvent);
}
