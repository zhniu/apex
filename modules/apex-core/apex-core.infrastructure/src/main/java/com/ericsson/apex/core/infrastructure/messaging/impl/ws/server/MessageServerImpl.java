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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.MessageHolder;
import com.ericsson.apex.core.infrastructure.messaging.util.MessagingUtils;

/**
 * A messaging server implementation using web socket.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type of message being passed
 */
public class MessageServerImpl<MESSAGE> extends InternalMessageBusServer<MESSAGE> {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MessageServerImpl.class);

    // The Web Socket protocol for URIs and URLs
    private static final String PROTOCOL = "ws://";

    // URI of this server
    private final String connectionURI;

    // Indicates if the web socket server is started or not
    private boolean isStarted = false;

    /**
     * Instantiates a new web socket messaging server for Apex.
     *
     * @param address the address of the server machine on which to start the server
     */
    public MessageServerImpl(final InetSocketAddress address) {
        // Call the super class to create the web socket and set up received message forwarding
        super(address);
        LOGGER.entry(address);

        // Compose the Web Socket URI
        connectionURI = PROTOCOL + address.getHostString() + ":" + address.getPort();
        LOGGER.debug("Server connection URI: {}", connectionURI);

        LOGGER.exit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.server.WebSocketServer#start()
     */
    @Override
    public void startConnection() {
        // Start reception of connections on the web socket
        start();
        isStarted = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.java_websocket.server.WebSocketServer#stop()
     */
    @Override
    public void stopConnection() {
        // Stop message listening using our super class
        stopListener();

        // Stop the web socket server
        try {
            // Close all connections on this web socket server
            for (final WebSocket connection : connections()) {
                connection.closeConnection(0, "");
            }
            stop();
        }
        catch (final IOException ioe) {
            LOGGER.catching(ioe);
        }
        catch (final InterruptedException e) {
            // This can happen in normal operation so ignore
        }
        isStarted = false;
    }

    /**
     * This method returns the current connection URI , if the server started otherwise it throws {@link IllegalStateException}.
     *
     * @return connection URI
     */
    public String getConnectionURI() {
        if (connectionURI == null) {
            throw new IllegalStateException("URI not set - The server is not started");
        }
        return connectionURI;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.MessagingService#send(com.ericsson.apex.core.infrastructure.messaging.MessageHolder)
     */
    @Override
    public void send(final MessageHolder<MESSAGE> message) {
        // Send the incoming message to all clients connected to this web socket
        final Collection<WebSocket> connections = connections();
        for (final WebSocket webSocket : connections) {
            webSocket.send(MessagingUtils.serializeObject(message));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.MessagingService#send(java.lang.String)
     */
    @Override
    public void send(final String messageString) {
        final Collection<WebSocket> connections = connections();
        for (final WebSocket webSocket : connections) {
            webSocket.send(messageString);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.MessagingService#isStarted()
     */
    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public void onStart() {
        LOGGER.debug("started deployment server on URI: {}", connectionURI);
    }
}
