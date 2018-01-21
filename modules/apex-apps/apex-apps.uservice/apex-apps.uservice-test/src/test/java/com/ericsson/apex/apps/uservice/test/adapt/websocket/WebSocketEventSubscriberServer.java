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
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageServer;

public class WebSocketEventSubscriberServer implements WSStringMessageListener {
    private final int port;
    private long eventsReceivedCount = 0;
    
    private WSStringMessageServer server;
    
    public WebSocketEventSubscriberServer(final int port) throws MessagingException {
        this.port = port;
        
        server = new WSStringMessageServer(port);
        server.start(this);
        
        System.out.println(WebSocketEventSubscriberServer.class.getCanonicalName() + ": port " + port + ", waiting for events");
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener#receiveString(java.lang.String)
     */
    @Override
    public void receiveString(String eventString) {
        System.out.println(WebSocketEventSubscriberServer.class.getCanonicalName() + ": port " + port + ", received event " + eventString);
        eventsReceivedCount++;
    }
    
    public long getEventsReceivedCount() {
        return eventsReceivedCount;
    }
    
    public void shutdown() {
        server.stop();
        System.out.println(WebSocketEventSubscriberServer.class.getCanonicalName() + ": stopped");
    }

    public static void main(String[] args) throws MessagingException {
        if (args.length != 1) {
            System.err.println("usage WebSocketEventSubscriberClient port");
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
        
        new WebSocketEventSubscriberServer(port);
    }
}
