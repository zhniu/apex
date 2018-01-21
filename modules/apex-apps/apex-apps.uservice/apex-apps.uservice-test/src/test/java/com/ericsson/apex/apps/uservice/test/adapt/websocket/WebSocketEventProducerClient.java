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
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageClient;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

public class WebSocketEventProducerClient implements WSStringMessageListener {
    private final String host;
    private final int port;
    private final int eventCount;
    private final boolean xmlEvents;
    private final long eventInterval;
    private long eventsSentCount = 0;

    WSStringMessageClient client;

    public WebSocketEventProducerClient(String host, int port, int eventCount, boolean xmlEvents, long eventInterval) throws MessagingException {
        this.host = host;
        this.port = port;
        this.eventCount = eventCount;
        this.xmlEvents = xmlEvents;
        this.eventInterval = eventInterval;

        client = new WSStringMessageClient(host, port);
        client.start(this);

        System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ": host " + host + ", port " + port + ", event count " + eventCount + ", xmlEvents " + xmlEvents);
    }

    public void sendEvents() {
        System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ": sending events on host " + host + ", port " + port + ", event count " + eventCount + ", xmlEvents " + xmlEvents);

        for (int i = 0; i < eventCount; i++) {
            System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ": waiting " + eventInterval + " milliseconds before sending next event");
            ThreadUtilities.sleep(eventInterval);

            String eventString = null;
            if (xmlEvents) {
                eventString = EventGenerator.xmlEvent();
            }
            else {
                eventString = EventGenerator.jsonEvent();
            }
            client.sendString(eventString);
            eventsSentCount++;
            System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ":  host " + host + ", port " + port + ", sent event " + eventString);
        }
        System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ": completed");
    }

    public long getEventsSentCount() {
        return eventsSentCount;
    }

    public void shutdown() {
        client.stop();
        System.out.println(WebSocketEventProducerClient.class.getCanonicalName() + ": stopped");
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener#receiveString(java.lang.String)
     */
    @Override
    public void receiveString(String eventString) {
        System.out.println(WebSocketEventProducerServer.class.getCanonicalName() + ":  host " + host + ", port " + port + ", received event " + eventString);
    }

    public static void main(String[] args) throws MessagingException {
        if (args.length != 5) {
            System.err.println("usage WebSocketEventProducerClient host port #events XML|JSON eventInterval");
            return;
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerClient host port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }

        int eventCount = 0;
        try {
            eventCount = Integer.parseInt(args[2]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerClient host port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }

        long eventInterval = 0;
        try {
            eventInterval = Long.parseLong(args[4]);
        }
        catch (Exception e) {
            System.err.println("usage WebSocketEventProducerClient host port #events XML|JSON eventInterval");
            e.printStackTrace();
            return;
        }

        boolean xmlEvents = false;
        if (args[3].equalsIgnoreCase("XML")) {
            xmlEvents = true;
        }
        else if (!args[3].equalsIgnoreCase("JSON")) {
            System.err.println("usage WebSocketEventProducerClient host port #events XML|JSON eventInterval");
            return;
        }

        WebSocketEventProducerClient client = new WebSocketEventProducerClient(args[0], port, eventCount, xmlEvents, eventInterval);

        client.sendEvents();
        client.shutdown();
    }
}
