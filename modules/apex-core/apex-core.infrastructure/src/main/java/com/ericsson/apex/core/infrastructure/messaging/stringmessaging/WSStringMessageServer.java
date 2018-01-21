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

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.MessageListener;
import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.messaging.MessagingService;
import com.ericsson.apex.core.infrastructure.messaging.MessagingServiceFactory;
import com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.MessageBlock;
import com.ericsson.apex.core.infrastructure.messaging.util.MessagingUtils;
import com.google.common.eventbus.Subscribe;

/**
 * This class runs a web socket server for sending and receiving of strings over a web socket.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class WSStringMessageServer implements WSStringMessager {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(WSStringMessageServer.class);

    // Message service factory and the message service itself
    private final MessagingServiceFactory<String> factory = new MessagingServiceFactory<>();
    private MessagingService<String> service = null;

    // The listener to use for reception of strings
    private WSStringMessageListener wsStringMessageListener;

    // Address of the server
    private final int port;

    /**
     * Constructor, define the port of the server.
     *
     * @param port the port of the server
     */
    public WSStringMessageServer(final int port) {
        this.port = port;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageSender#start(com.ericsson.apex.core.infrastructure.messaging.
     * stringmessaging.WSStringMessageListener)
     */
    @Override
    public void start(final WSStringMessageListener newWsStringMessageListener) throws MessagingException {
        this.wsStringMessageListener = newWsStringMessageListener;

        LOGGER.entry("web socket event consumer server starting . . .");

        try {
            final InetAddress addrLan = MessagingUtils.getLocalHostLANAddress();
            LOGGER.debug("web socket string message server LAN address=" + addrLan.getHostAddress());
            final InetAddress addr = InetAddress.getLocalHost();
            LOGGER.debug("web socket string message server host address=" + addr.getHostAddress());

            service = factory.createServer(new InetSocketAddress(port));
            service.addMessageListener(new WSStringMessageServerListener());

            service.startConnection();
        }
        catch (final Exception e) {
            LOGGER.warn("web socket string message server start failed", e);
            throw new MessagingException("web socket string message start failed", e);
        }

        LOGGER.exit("web socket string message server started");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageSender#stop()
     */
    @Override
    public void stop() {
        LOGGER.entry("web socket string message server stopping . . .");
        service.stopConnection();
        LOGGER.exit("web socket string message server stopped");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageSender#sendString(java.lang.String)
     */
    @Override
    public void sendString(final String stringMessage) {
        service.send(stringMessage);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("server sent message: " + stringMessage);
        }
    }

    /**
     * The listener for strings coming into the server.
     */
    private class WSStringMessageServerListener implements MessageListener<String> {

        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.core.infrastructure.messaging.MessageListener#onMessage(com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.
         * MessageBlock)
         */
        @Subscribe
        @Override
        public void onMessage(final MessageBlock<String> messageBlock) {
            throw new UnsupportedOperationException("raw messages are not supported on string message clients");
        }

        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.core.infrastructure.messaging.MessageListener#onMessage(java.lang.String)
         */
        @Subscribe
        @Override
        public void onMessage(final String messageString) {
            wsStringMessageListener.receiveString(messageString);
        }
    }
}
