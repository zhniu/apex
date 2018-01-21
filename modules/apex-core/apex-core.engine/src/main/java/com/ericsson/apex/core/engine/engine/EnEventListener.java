/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.engine;

import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * This interface is used by users of an Apex engine to receive action events being emitted by the engine.
 *
 * @author Liam Fallon
 *
 */
public interface EnEventListener {

    /**
     * This method is called when an Apex engine emits an event.
     *
     * @param enEvent the engine event
     * @throws ApexException the apex exception
     */
    void onEnEvent(EnEvent enEvent) throws ApexException;
}
