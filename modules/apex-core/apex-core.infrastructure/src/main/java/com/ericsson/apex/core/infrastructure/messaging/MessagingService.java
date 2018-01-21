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

/**
 * The Interface MessagingService specifies the methods that must be implemented by any implementation providing Apex messaging.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the type of message being passed by an implementation of Apex messaging
 */
public interface MessagingService<MESSAGE> {

    /**
     * Start the messaging connection.
     */
    void startConnection();

    /**
     * Stop the messaging connection.
     */
    void stopConnection();

    /**
     * Checks if the messaging connection is started.
     *
     * @return true, if is started
     */
    boolean isStarted();

    /**
     * Send a block of messages on the connection, the messages are contained in the the message holder container.
     *
     * @param messageHolder The message holder holding the messages to be sent
     */
    void send(MessageHolder<MESSAGE> messageHolder);

    /**
     * Send a string message on the connection.
     *
     * @param messageString The message string to be sent
     */
    void send(String messageString);

    /**
     * Adds a message listener that will be called when a message is received by this messaging service implementation.
     *
     * @param messageListener the message listener
     */
    void addMessageListener(MessageListener<MESSAGE> messageListener);

    /**
     * Removes the message listener.
     *
     * @param messageListener the message listener
     */
    void removeMessageListener(MessageListener<MESSAGE> messageListener);
}
