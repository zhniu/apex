/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock;

import java.util.List;

import org.java_websocket.WebSocket;

/**
 * This class encapsulate messages and the web socket on which they are handled.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type of message being handled
 */
public final class MessageBlock<MESSAGE> {

    // List of Messages received on a web socket
    private final List<MESSAGE> messages;

    // The web socket on which the messages are handled
    private final WebSocket webSocket;

    /**
     * Instantiates a new message block.
     *
     * @param messages the messages in the message block
     * @param webSocket the web socket used to handle the message block
     */
    public MessageBlock(final List<MESSAGE> messages, final WebSocket webSocket) {
        this.messages = messages;
        this.webSocket = webSocket;
    }

    /**
     * Gets the messages.
     *
     * @return the messages
     */
    public List<MESSAGE> getMessages() {
        return messages;
    }

    /**
     * Gets the web socket.
     *
     * @return the web socket
     */
    public WebSocket getConnection() {
        return webSocket;
    }

}
