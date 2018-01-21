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

import java.net.InetSocketAddress;
import java.net.URI;

import com.ericsson.apex.core.infrastructure.messaging.impl.ws.client.MessagingClient;
import com.ericsson.apex.core.infrastructure.messaging.impl.ws.server.MessageServerImpl;

/**
 * A factory class to create a "server" or "client" type Messaging Service.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type of message to be handled by this messaging service
 */
public class MessagingServiceFactory<MESSAGE> {

    /**
     * Create a web socket server instance and returns to the caller.
     *
     * @param address the address of the server machine
     * @return the messaging service
     */
    public MessagingService<MESSAGE> createServer(final InetSocketAddress address) {
        return new MessageServerImpl<>(address);
    }

    /**
     * Create a web socket client instance and returns to the caller.
     *
     * @param uri the URI of the server to connect to
     * @return an instance of {@link MessagingService}
     */
    public MessagingService<MESSAGE> createClient(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        return new MessagingClient<>(uri);
    }
}
