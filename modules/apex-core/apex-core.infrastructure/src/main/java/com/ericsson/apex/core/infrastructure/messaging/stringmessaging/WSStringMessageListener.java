/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging.stringmessaging;

/**
 * This interface is used to call back the owner of a String Web socket message server or client.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface WSStringMessageListener {

    /**
     * Receive a string coming off a web socket.
     *
     * @param stringMessage the string message
     */
    void receiveString(String stringMessage);
}
