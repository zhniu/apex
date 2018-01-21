/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging.impl.ws.server;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class is the web socket server specific implementation for Apex.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
abstract class WebSocketServerImpl extends WebSocketServer {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MessageServerImpl.class);

    /**
     * Constructor of this class.
     *
     * @param address host address of the local machine.
     */
    protected WebSocketServerImpl(final InetSocketAddress address) {
        super(address);
        LOGGER.entry(address.getAddress().getHostAddress() + ":" + address.getPort());
        LOGGER.exit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.server.WebSocketServer#onOpen(org.java_websocket.WebSocket , org.java_websocket.handshake.ClientHandshake)
     */
    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        LOGGER.entry(conn, handshake);
        LOGGER.debug("A client connection opened from machine {}.", conn.getRemoteSocketAddress().getAddress().getHostAddress());
        LOGGER.exit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.server.WebSocketServer#onClose(org.java_websocket. WebSocket, int, java.lang.String, boolean)
     */
    @Override
    public void onClose(final WebSocket conn, final int code, final String reason, final boolean remote) {
        LOGGER.entry(conn, code, remote);
        LOGGER.debug("A client  connection from machine {} closing with code {}.", conn.getRemoteSocketAddress().getAddress().getHostAddress(), code);
        LOGGER.exit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.server.WebSocketServer#onError(org.java_websocket.WebSocket, java.lang.Exception)
     */
    @Override
    public void onError(final WebSocket conn, final Exception ex) {
        // some errors like port binding failed may not be assignable to a specific web socket
        LOGGER.error("server error occurred", ex);
    }
}
