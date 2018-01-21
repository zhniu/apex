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
 * The Class GetEngineInfo is a message that requests information on Apex engines and the policies they are running.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class GetEngineInfo extends Message {
    private static final long serialVersionUID = 5885214410842753037L;

    /**
     * Instantiates a new GetEngineInfo message.
     *
     * @param engineKey the key the engine for which the runtime information is requested
     */
    public GetEngineInfo(final AxArtifactKey engineKey) {
        this(engineKey, null);
    }

    /**
     * Instantiates a new GetEngineInfo message.
     *
     * @param engineKey the key the engine for which the runtime information is requested
     * @param messageData the message data that may give specifics on what information to return
     */
    public GetEngineInfo(final AxArtifactKey engineKey, final String messageData) {
        super(EngDepAction.GET_ENGINE_INFO, engineKey, messageData);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.protocols.Message#toString()
     */
    @Override
    public String toString() {
        return "GetEngineInfo {" + super.toString() + "}[]";
    }
}
