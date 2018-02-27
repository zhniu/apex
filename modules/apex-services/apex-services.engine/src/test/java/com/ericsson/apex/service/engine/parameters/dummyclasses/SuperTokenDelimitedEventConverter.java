/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import java.util.List;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventProtocolConverter;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

public final class SuperTokenDelimitedEventConverter implements ApexEventProtocolConverter {

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.String, java.lang.Object)
     */
    @Override
    public List<ApexEvent> toApexEvent(String eventName, Object eventOfOtherType) throws ApexException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#fromApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
     */
    @Override
    public String fromApexEvent(ApexEvent apexEvent) throws ApexException {
        return null;
    }
    
    @Override
    public void init(EventProtocolParameters parameters) {
    }
}
