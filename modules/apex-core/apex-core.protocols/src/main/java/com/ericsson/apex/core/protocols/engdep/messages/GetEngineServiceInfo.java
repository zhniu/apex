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
 * The Class GetEngineServiceInfo is a message that requests information on what is in an Apex engine service.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class GetEngineServiceInfo extends Message {
    private static final long serialVersionUID = 5885214410842753037L;

    /**
     * Instantiates a new GetEngineServiceInfo message.
     *
     * @param nullKey not used, set to null
     */
    public GetEngineServiceInfo(final AxArtifactKey nullKey) {
        this(nullKey, null);
    }

    /**
     * Instantiates a new GetEngineServiceInfo message.
     *
     * @param nullKey not used, set to null
     * @param messageData the message data that may give specifics on what information to return
     */
    public GetEngineServiceInfo(final AxArtifactKey nullKey, final String messageData) {
        super(EngDepAction.GET_ENGINE_SERVICE_INFO, nullKey, messageData);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.protocols.Message#toString()
     */
    @Override
    public String toString() {
        return "GetEngineServiceInfo {" + super.toString() + "}[]";
    }
}
