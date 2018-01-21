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

import com.ericsson.apex.apps.uservice.test.adapt.events.EventGenerator;
import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageServer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

public class WebSocketEventProducerServer implements WSStringMessageListener {
    private final int port;
    private final int eventCount;
    private final boolean xmlEvents;
    private final long eventInterval;
    private long eventsSentCount = 0;
    
    WSStringMessageServer server;
    
    public WebSocketEventProducerServer(final int port, final int eventCount, final boolean xmlEvents, final long eventInterval) throws MessagingException {
        this.port = port;
        this.eventCount = eventCount;
        this.xmlEvents = xmlEvents;
        this.eventInterval = eventInterval;
        
        server = new WSStringMessageServer(port);
        server.start(this);
        
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": port " + port + ", event count " + eventCount + ", xmlEvents " + xmlEvents);
    }
    
    public void sendEvents() {
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": sending events on port " + port + ", event count " + eventCount + ", xmlEvents " + xmlEvents);
        
        for (int i = 0; i < eventCount; i++) {
            System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": waiting " + eventInterval + " milliseconds before sending next event");
            ThreadUtilities.sleep(eventInterval);

            String eventString = null;
            if (xmlEvents) {
                eventString = EventGenerator.xmlEvent();
            }
            else {
                eventString = EventGenerator.jsonEvent();
            }
            server.sendString(eventString);
            eventsSentCount++;
            System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": port " + port + ", sent event " + eventString);
        }
        
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": event sending completed");
    }
    
    public long getEventsSentCount() {
        return eventsSentCount;
    }
    
    public void shutdown() {
        server.stop();
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": stopped");
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener#receiveString(java.lang.String)
     */
    @Override
    public void receiveString(String eventString) {
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ": port " + port + ", received event " + eventString);
    }

    public static void main(String[] args) throws MessagingException {
        if (args.length != 4) {
            System.err.println("usage WebSocketEventProducerServer port #events XML|JSON eventInterval");
            return;
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerServer port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }
        
        int eventCount = 0;
        try {
            eventCount = Integer.parseInt(args[1]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerServer port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }
        
        long eventInterval = 0;
        try {
            eventInterval = Long.parseLong(args[3]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerServer port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }
        
        boolean xmlEvents = false;
        if (args[2].equalsIgnoreCase("XML")) {
            xmlEvents = true;
        }
        else if (!args[2].equalsIgnoreCase("JSON")) {
            System.err.println("usage WebSocketEventProducerServer port #events XML|JSON startDelay eventInterval");
            return;
        }
        
        WebSocketEventProducerServer server = new WebSocketEventProducerServer(port, eventCount, xmlEvents, eventInterval);
        
        server.sendEvents();
        server.shutdown();
    }
}
