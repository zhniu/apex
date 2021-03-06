/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.deployment;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.MessageHolder;
import com.ericsson.apex.core.infrastructure.messaging.MessageListener;
import com.ericsson.apex.core.infrastructure.messaging.MessagingService;
import com.ericsson.apex.core.infrastructure.messaging.MessagingServiceFactory;
import com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.MessageBlock;
import com.ericsson.apex.core.infrastructure.messaging.util.MessagingUtils;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.core.protocols.Message;
import com.google.common.eventbus.Subscribe;

/**
 * The Class DeploymentClient handles the client side of an EngDep communication session with an Apex server. It runs a thread to handle message sending and
 * session monitoring. It uses a sending queue to queue messages for sending by the client thread and a receiving queue to queue messages received from the Apex
 * engine.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class DeploymentClient implements Runnable {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(DeploymentClient.class);

    private static final int CLIENT_STOP_WAIT_INTERVAL = 100;

    // Host and port to use for EngDep messaging
    private String host = null;
    private int port = 0;

    // Messaging service is used to transmit and receive messages over the web socket
    private static MessagingServiceFactory<Message> factory = new MessagingServiceFactory<>();
    private MessagingService<Message> service = null;

    // Send and receive queues for message buffering
    private final BlockingQueue<Message> sendQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> receiveQueue = new LinkedBlockingQueue<>();

    // Thread management fields
    private boolean started = false;
    private Thread thisThread = null;

    /**
     * Instantiates a new deployment client.
     *
     * @param host the host name that the EngDep server is running on
     * @param port the port the port the EngDep server is using
     */
    public DeploymentClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.debug("engine<-->deployment to \"ws://" + host + ":" + port + "\" thread starting . . .");

        // Set up the thread name
        thisThread = Thread.currentThread();
        thisThread.setName(DeploymentClient.class.getName() + "-" + host + ":" + port);

        try {
            // Establish a connection to the Apex server for EngDep message communication over Web Sockets
            service = factory.createClient(new URI("ws://" + host + ":" + port));
            service.addMessageListener(new DeploymentClientListener());

            service.startConnection();
            started = true;
            LOGGER.debug("engine<-->deployment client thread started");
        }
        catch (final Exception e) {
            LOGGER.error("engine<-->deployment client thread exception", e);
            return;
        }

        // Loop forever, sending messages as they appear on the queue
        while (true) {
            try {
                final Message messageForSending = sendQueue.take();
                sendMessage(messageForSending);
            }
            catch (final InterruptedException e) {
                // Message sending has been interrupted, we are finished
                LOGGER.debug("engine<-->deployment client interrupted");
                break;
            }
        }

        // Thread has been interrupted
        thisThread = null;
        LOGGER.debug("engine<-->deployment client thread finished");
    }

    /**
     * Send an EngDep message to the Apex server.
     *
     * @param message the message to send to the Apex server
     */
    public void sendMessage(final Message message) {
        final MessageHolder<Message> messageHolder = new MessageHolder<>(MessagingUtils.getHost());

        // Send the message in its message holder
        messageHolder.addMessage(message);
        service.send(messageHolder);
    }

    /**
     * Stop the deployment client.
     */
    public void stopClient() {
        LOGGER.debug("engine<-->deployment test client stopping . . .");
        thisThread.interrupt();

        // Wait for the thread to stop
        while (thisThread != null && thisThread.isAlive()) {
            ThreadUtilities.sleep(CLIENT_STOP_WAIT_INTERVAL);
        }

        // Close the Web Services connection
        service.stopConnection();
        started = false;
        LOGGER.debug("engine<-->deployment test client stopped . . .");
    }

    /**
     * Checks if the client thread is started.
     *
     * @return true, if the client thread is started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Allows users of this class to get a reference to the receive queue to receove messages.
     *
     * @return the receive queue
     */
    public BlockingQueue<Message> getReceiveQueue() {
        return receiveQueue;
    }

    /**
     * The listener interface for receiving deploymentClient events. The class that is interested in processing a deploymentClient event implements this
     * interface, and the object created with that class is registered with a component using the component's {@code addDeploymentClientListener} method.
     * When the deploymentClient event occurs, that object's appropriate method is invoked.
     *
     * @see DeploymentClientEvent
     */
    private class DeploymentClientListener implements MessageListener<Message> {
        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.core.infrastructure.messaging.MessageListener#onMessage(com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.
         * MessageBlock)
         */
        @Subscribe
        @Override
        public void onMessage(final MessageBlock<Message> messageData) {
            receiveQueue.addAll(messageData.getMessages());
        }

        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.core.infrastructure.messaging.MessageListener#onMessage(java.lang.String)
         */
        @Override
        public void onMessage(final String messageString) {
            throw new UnsupportedOperationException("String mesages are not supported on the EngDep protocol");
        }
    }
}
