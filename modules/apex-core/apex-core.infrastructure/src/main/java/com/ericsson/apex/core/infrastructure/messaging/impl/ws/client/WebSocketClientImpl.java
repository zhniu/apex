/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging.impl.ws.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class implements {@link WebSocketClient} specific methods in order to act as a Java Web Socket client.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
abstract class WebSocketClientImpl extends WebSocketClient {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(WebSocketClientImpl.class);

    /**
     * Constructs a WebSocketClient instance and sets it to the connect to the specified URI. The channel does not attempt to connect automatically. You must
     * call {@link connect} first to initiate the socket connection.
     *
     * @param serverUri the URI of the web socket server to connect to
     */
    WebSocketClientImpl(final URI serverUri) {
        super(serverUri);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.client.WebSocketClient#onOpen(org.java_websocket.handshake.ServerHandshake)
     */
    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Connection opened to server {} --> {}", this.getURI(), handshakedata.getHttpStatusMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.client.WebSocketClient#onClose(int, java.lang.String, boolean)
     */
    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Connection closed to server {} --> code \"{}\", reason \"{}\"", this.getURI(), code, reason);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.client.WebSocketClient#onError(java.lang.Exception)
     */
    @Override
    public void onError(final Exception ex) {
        LOGGER.info("Failed to make a connection to the server {} ", getURI());
        LOGGER.catching(ex);
    }
}
