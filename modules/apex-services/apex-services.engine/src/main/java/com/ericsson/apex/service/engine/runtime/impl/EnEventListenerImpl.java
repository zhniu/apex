/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.runtime.impl;

import com.ericsson.apex.core.engine.engine.EnEventListener;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.impl.enevent.ApexEvent2EnEventConverter;
import com.ericsson.apex.service.engine.runtime.ApexEventListener;

/**
 * The Class EnEventListenerImpl is used by the Apex engine implementation to listen for events coming from the core APEX engine. This listener converts the
 * {@link EnEvent} instances into {@link ApexEvent} instances using an {@link ApexEvent2EnEventConverter} instance and forwards the events to an
 * {@link ApexEventListener} instance for outputting to listening applications. The {@link ApexEventListener} is implemented in the external application
 * communicating with Apex.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class EnEventListenerImpl implements EnEventListener {
    // Listener for ApexEvents
    private ApexEventListener apexEventListener = null;

    // Converter for Engine events to Apex Events
    private ApexEvent2EnEventConverter apexEnEventConverter = null;

    /**
     * Instantiates a new listener implementation.
     *
     * @param apexEventListener the apex event listener
     * @param apexEnEventConverter the ApexEvent to enEvent converter
     */
    public EnEventListenerImpl(final ApexEventListener apexEventListener, final ApexEvent2EnEventConverter apexEnEventConverter) {
        this.apexEventListener = apexEventListener;
        this.apexEnEventConverter = apexEnEventConverter;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.engine.EnEventListener#onEnEvent(com.ericsson.apex.core.engine.event.EnEvent)
     */
    @Override
    public void onEnEvent(final EnEvent enEvent) throws ApexException {
        for (ApexEvent apexEvent : apexEnEventConverter.toApexEvent(enEvent)) {
            apexEventListener.onApexEvent(apexEvent);
        }
    }
}
