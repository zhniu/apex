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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.MessageListener;
import com.google.common.eventbus.EventBus;

/**
 * This class is used to pass messages received on a Java web socket to listening application class instances using an event bus.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type
 */
public class MessageBlockHandler<MESSAGE> {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MessageBlockHandler.class);

    /**
     * This event bus will forward the events to all of its subscribers.
     */
    private EventBus eventBus = null;

    /**
     * Instantiates a new data handler.
     *
     * @param eventBusName the name of the event bus for this message block handler
     */
    public MessageBlockHandler(final String eventBusName) {
        eventBus = new EventBus(eventBusName);
        LOGGER.trace("message bus {} created ", eventBusName);
    }

    /**
     * Post a raw message block on the data handler event bus of this class.
     *
     * @param rawMessageBlock the block containing raw messages
     */
    public void post(final RawMessageBlock rawMessageBlock) {
        if (rawMessageBlock.getMessage() != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("new raw message recieved from {}",
                        rawMessageBlock.getConn() == null ? "server" : rawMessageBlock.getConn().getRemoteSocketAddress().getHostName());
            }
            eventBus.post(rawMessageBlock);
        }
    }

    /**
     * Post a block of typed messages on the data handler event bus of this class.
     *
     * @param messageBlock the block containing typed messages
     */
    public void post(final MessageBlock<MESSAGE> messageBlock) {
        if (messageBlock.getMessages() != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("new data message recieved from {}",
                        messageBlock.getConnection() == null ? "server" : messageBlock.getConnection().getRemoteSocketAddress().getHostName());
            }
            eventBus.post(messageBlock);
        }
    }

    /**
     * Post a string message on the data handler event bus of this class.
     *
     * @param messageString the string message
     */
    public void post(final String messageString) {
        if (messageString != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("new string message recieved from server: " + messageString);
            }
            eventBus.post(messageString);
        }
    }

    /**
     * Register a listener to event bus.
     *
     * @param listener is an instance of WebSocketMessageListener
     */
    public void registerMessageHandler(final MessageListener<MESSAGE> listener) {
        LOGGER.entry(listener);
        if (listener == null) {
            throw new IllegalArgumentException("listener object cannot be null");
        }
        eventBus.register(listener);
        LOGGER.debug("message listener {} is registered with forwarder", listener);
        LOGGER.exit();
    }

    /**
     * Remove the listener subscribed to the event bus.
     *
     * @param listener the listener
     */
    public void unRegisterMessageHandler(final MessageListener<MESSAGE> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener object cannot be null");
        }
        LOGGER.entry(listener);
        eventBus.unregister(listener);
        LOGGER.trace(" message listener {} unregistered from forwarder", listener);
        LOGGER.exit();
    }
}
