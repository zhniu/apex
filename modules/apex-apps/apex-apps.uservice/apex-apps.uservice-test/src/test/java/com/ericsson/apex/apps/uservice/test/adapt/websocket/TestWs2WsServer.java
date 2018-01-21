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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;

public class TestWs2WsServer {
    private static final long MAX_TEST_LENGTH = 10000;
    
    private static final int EVENT_COUNT    = 100;
    private static final int EVENT_INTERVAL =  20;

    @Test
    public void testJsonWSEvents() throws MessagingException, ApexException {
        String[] args = {"src/test/resources/prodcons/Ws2WsServerJsonEvent.json"};
        testWSEvents(args, false);
    }

    @Test
    public void testXMLWSEvents() throws MessagingException, ApexException {
        String[] args = {"src/test/resources/prodcons/Ws2WsServerXMLEvent.json"};
        testWSEvents(args, true);
    }

    public void testWSEvents(final String[] args, final boolean xmlEvents) throws MessagingException, ApexException {
        ApexMain apexMain = new ApexMain(args);

        WebSocketEventSubscriberClient subClient  = new WebSocketEventSubscriberClient("localhost", 42452);
        WebSocketEventProducerClient   prodClient = new WebSocketEventProducerClient  ("localhost", 42450, EVENT_COUNT, xmlEvents, EVENT_INTERVAL);

        prodClient.sendEvents();

        long testStartTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() < testStartTime + MAX_TEST_LENGTH && subClient.getEventsReceivedCount() < EVENT_COUNT) {
            ThreadUtilities.sleep(EVENT_INTERVAL);
        }
        
        assertEquals(subClient.getEventsReceivedCount(), prodClient.getEventsSentCount());

        prodClient.shutdown();
        subClient.shutdown();
        apexMain.shutdown();
    }
}
