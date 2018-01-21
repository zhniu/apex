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
 * The run time interface for APEX engine users.
 * APEX engine implementations expose this interface and external users use it to send events to the engine.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com), John Keeney (john.keeney@ericsson.com)
 */
public interface EngineServiceEventInterface {
    /**
     * This method forwards an event to the APEX engine.
     *
     * @param event is an instance {@link ApexEvent}
     */
    void sendEvent(ApexEvent event);
}
