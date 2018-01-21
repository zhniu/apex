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

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;

/**
 * This interface is used to call a String Web socket message server or client to send a string.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface WSStringMessager {

    /**
     * Start the string message sender.
     *
     * @param wsStringMessageListener the listener to use for listening for string messages
     * @throws MessagingException the messaging exception
     */
    void start(WSStringMessageListener wsStringMessageListener) throws MessagingException;

    /**
     * Stop the string messaging sender.
     */
    void stop();

    /**
     * Send a string on a web socket.
     *
     * @param stringMessage the string message to send
     */
    void sendString(String stringMessage);
}
