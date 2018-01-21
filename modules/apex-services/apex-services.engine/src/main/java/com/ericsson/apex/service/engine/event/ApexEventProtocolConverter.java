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

import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * The Interface ApexEventProtocolConverter extends ApexEventConverter to allow EventProtocolParameters conversion parameters to be passed to the converter.
 *
 * @author John Keeney (john.keeney@ericsson.com)
 */
public interface ApexEventProtocolConverter extends ApexEventConverter {

    /**
     * Initialise the converter instance with the parameters for the EventProtocol.
     *
     * @param parameters the parameters for the EventProtocol
     */
    void init(EventProtocolParameters parameters);
}
