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

public class TestWs2WsClient {
    private static final long MAX_TEST_LENGTH = 10000;
    
    private static final int EVENT_COUNT    = 100;
    private static final int EVENT_INTERVAL =  20;

    @Test
    public void testJsonWSEvents() throws MessagingException, ApexException {
        String[] args = {"src/test/resources/prodcons/Ws2WsClientJsonEvent.json"};
        testWSEvents(args, false);
    }

    @Test
    public void testXMLWSEvents() throws MessagingException, ApexException {
        String[] args= {"src/test/resources/prodcons/Ws2WsClientXMLEvent.json"};
        testWSEvents(args, true);
    }

    private void testWSEvents(final String[] args, final Boolean xmlEvents) throws MessagingException, ApexException {
        WebSocketEventSubscriberServer subServer  = new WebSocketEventSubscriberServer(42453);
        WebSocketEventProducerServer   prodServer = new WebSocketEventProducerServer  (42451, EVENT_COUNT, xmlEvents, EVENT_INTERVAL);

        ApexMain apexMain = new ApexMain(args);

        prodServer.sendEvents();

        long testStartTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() < testStartTime + MAX_TEST_LENGTH && subServer.getEventsReceivedCount() < EVENT_COUNT) {
            ThreadUtilities.sleep(EVENT_INTERVAL);
        }
        
        assertEquals(prodServer.getEventsSentCount(), subServer.getEventsReceivedCount());
        
        apexMain.shutdown();
        prodServer.shutdown();
        subServer.shutdown();
    }
}
