/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.protocols.engdep.messages;

import com.ericsson.apex.core.protocols.Message;
import com.ericsson.apex.core.protocols.engdep.EngDepAction;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class StopEngine is a message that requests that an Apex engine in an engine service be stopped.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class StopPeriodicEvents extends Message {
    private static final long serialVersionUID = -1796422638427413285L;

    /**
     * Instantiates a new StopEngine message.
     *
     * @param engineKey the key of the engine to stop
     */
    public StopPeriodicEvents(final AxArtifactKey engineKey) {
        this(engineKey, null);
    }

    /**
     * Instantiates a new StopEngine message.
     *
     * @param engineKey the key of the engine to stop
     * @param messageData the message data that may give specifics on what way to stop
     */
    public StopPeriodicEvents(final AxArtifactKey engineKey, final String messageData) {
        super(EngDepAction.STOP_PERIODIC_EVENTS, engineKey, messageData);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.protocols.Message#toString()
     */
    @Override
    public String toString() {
        return "StopPeriodicEvents {" + super.toString() + "}[]";
    }
}
