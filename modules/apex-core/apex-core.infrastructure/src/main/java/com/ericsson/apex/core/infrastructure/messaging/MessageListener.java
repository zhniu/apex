/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging;

import com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.MessageBlock;

/**
 * The listener interface for receiving message events. The class that is interested in processing a message event implements this interface.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> of message of any given type that is being listened for and handled
 */
public interface MessageListener<MESSAGE> {

    /**
     * This method is called when a message block is received on a web socket and is to be forwarded to a listener.
     *
     * @param data the message data containing a message
     */
    void onMessage(MessageBlock<MESSAGE> data);

    /**
     * This method is called when a string message is received on a web socket and is to be forwarded to a listener.
     *
     * @param messageString the message string
     */
    void onMessage(String messageString);
}
