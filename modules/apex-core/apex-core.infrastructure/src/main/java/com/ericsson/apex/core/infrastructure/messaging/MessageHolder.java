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

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The Class MessageHolder holds a set of messages to be sent as a single block of messages in this messaging implementation.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type of message being handled by a message holder instance
 */
public class MessageHolder<MESSAGE> implements Serializable {
    private static final int HASH_PRIME = 31;
    private static final int FOUR_BYTES = 32;

    // Serial ID
    private static final long serialVersionUID = 1235487535388793719L;

    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MessageHolder.class);

    // Properties of the message holder
    private final long creationTime;
    private final InetAddress senderHostAddress;

    // Sequence of message in the message holder
    private final List<MESSAGE> messages;

    /**
     * Constructor, create the message holder.
     *
     * @param senderHostAddress the host address of the sender of the message holder container
     */
    public MessageHolder(final InetAddress senderHostAddress) {
        LOGGER.entry(senderHostAddress);
        messages = new ArrayList<>();
        this.senderHostAddress = senderHostAddress;
        creationTime = System.currentTimeMillis();
    }

    /**
     * Return the messages in this message holder.
     *
     * @return the messages
     */
    public List<MESSAGE> getMessages() {
        return messages;
    }

    /**
     * Adds a message to this message holder.
     *
     * @param message the message to add
     */
    public void addMessage(final MESSAGE message) {
        if (!messages.contains(message)) {
            messages.add(message);
        }
        else {
            LOGGER.warn("duplicate message {} added to message holder", message);
        }
    }

    /**
     * Gets the creation time.
     *
     * @return the creation time
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Gets the sender host address.
     *
     * @return the sender host address
     */
    public InetAddress getSenderHostAddress() {
        return senderHostAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApexCommandProtocol [creationTime=" + creationTime + ", senderHostAddress=" + senderHostAddress + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME;
        int result = 1;
        result = prime * result + ((senderHostAddress == null) ? 0 : senderHostAddress.hashCode());
        result = prime * result + ((messages == null) ? 0 : messages.hashCode());
        result = prime * result + (int) (creationTime ^ (creationTime >>> FOUR_BYTES));
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageHolder<?> other = (MessageHolder<?>) obj;
        if (senderHostAddress == null) {
            if (other.senderHostAddress != null) {
                return false;
            }
        }
        else if (!senderHostAddress.equals(other.senderHostAddress)) {
            return false;
        }
        if (messages == null) {
            if (other.messages != null) {
                return false;
            }
        }
        else if (!messages.equals(other.messages)) {
            return false;
        }
        if (creationTime != other.creationTime) {
            return false;
        }
        return true;
    }
}
