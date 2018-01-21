/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.websocket;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageClient;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;

public class WebSocketEventSubscriberClient implements WSStringMessageListener {
    private final int port;
    private long eventsReceivedCount = 0;
    
    private WSStringMessageClient client;
    
    public WebSocketEventSubscriberClient(final String host, final int port) throws MessagingException {
        this.port = port;
        
        client = new WSStringMessageClient(host, port);
        client.start(this);
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener#receiveString(java.lang.String)
     */
    @Override
    public void receiveString(String eventString) {
        System.out.println(WebSocketEventSubscriberClient.class.getCanonicalName() + ": port " + port + ", received event " + eventString);
        eventsReceivedCount++;
    }

    public long getEventsReceivedCount() {
        return eventsReceivedCount;
    }
    
    public void shutdown() {
        client.stop();
        System.out.println(WebSocketEventSubscriberServer.class.getCanonicalName() + ": stopped");
    }

    public static void main(String[] args) throws MessagingException {
        if (args.length != 2) {
            System.err.println("usage WebSocketEventSubscriberClient host port");
            return;
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventSubscriberClient port");
            e.printStackTrace();
            return;
        }
        
        new WebSocketEventSubscriberClient(args[0], port);
    }
}
