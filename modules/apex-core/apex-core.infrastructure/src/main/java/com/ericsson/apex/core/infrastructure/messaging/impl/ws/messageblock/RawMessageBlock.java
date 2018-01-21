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

import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;

/**
 * A container for a raw message block and the connection on which it is handled.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
public final class RawMessageBlock {
    // The raw message
    private final ByteBuffer message;

    // The web socket on which the message is handled
    private final WebSocket webSocket;

    /**
     * Constructor, instantiate the bean.
     *
     * @param message {@link ByteBuffer} message from the web socket
     * @param webSocket {@link WebSocket} the web socket on which the message is handled
     */
    public RawMessageBlock(final ByteBuffer message, final WebSocket webSocket) {
        this.message = message;
        this.webSocket = webSocket;
    }

    /**
     * A getter method for message.
     *
     * @return the message
     */
    public ByteBuffer getMessage() {
        return message;
    }

    /**
     * A getter method for the web socket.
     *
     * @return the web socket
     */
    public WebSocket getConn() {
        return webSocket;
    }
}
