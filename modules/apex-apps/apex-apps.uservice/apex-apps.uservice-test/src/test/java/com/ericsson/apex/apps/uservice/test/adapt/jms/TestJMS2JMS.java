/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;

import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.apps.uservice.test.engine.stats.EngineTestServer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;

public class TestJMS2JMS {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EngineTestServer.class);

    private static final long MAX_TEST_LENGTH = 10000;

    private static final int EVENT_COUNT = 100;
    private static final int EVENT_INTERVAL = 20;

    private static EmbeddedJMS server;

    @BeforeClass
    public static void setupEmbeddedJMSServer() {
        server = new EmbeddedJMS();
        server.setConfigResourcePath("jmsconfig/hornetq-configuration.xml");
        server.setJmsConfigResourcePath("jmsconfig/hornetq-jms.xml");

        try {
            server.start();
            server.getHornetQServer().getSecurityManager().addUser("guest", "IAmAGuest");
            server.getHornetQServer().getSecurityManager().addRole("guest", "guest");
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to start JMS server", e);
        }
    }

    @AfterClass
    public static void shutdownEmbeddedJMSServer() {
        try {
            server.stop();
        }
        catch (Exception e) {
            LOGGER.warn("Failed to stop JMS server", e);
        }
    }

    @Test
    public void testJMSObjectEvents() throws ApexException, JMSException {
        String[] args = { "src/test/resources/prodcons/JMS2JMSObjectEvent.json" };
        testJMSEvents(args, true);
    }

    @Test
    public void testJMSJsonEvents() throws ApexException, JMSException {
        String[] args = { "src/test/resources/prodcons/JMS2JMSJsonEvent.json" };
        testJMSEvents(args, false);
    }

    private void testJMSEvents(final String[] args, final Boolean sendObjects) throws ApexException, JMSException {
        JMSEventSubscriber subscriber  = new JMSEventSubscriber("jms/topic/apexOut", "localhost", "5445", "guest", "IAmAGuest");
        JMSEventProducer   producer    = new JMSEventProducer  ("jms/topic/apexIn" , "localhost", "5445", "guest", "IAmAGuest", EVENT_COUNT, sendObjects, EVENT_INTERVAL);

        ApexMain apexMain = new ApexMain(args);
        ThreadUtilities.sleep(3000);

        producer.sendEvents();

        long testStartTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() < testStartTime + MAX_TEST_LENGTH && subscriber.getEventsReceivedCount() < EVENT_COUNT) {
            ThreadUtilities.sleep(EVENT_INTERVAL);
        }

        ThreadUtilities.sleep(1000);

        System.out.println("sent event count: " + producer.getEventsSentCount());
        System.out.println("received event count: " + subscriber.getEventsReceivedCount());
        assertTrue(subscriber.getEventsReceivedCount() == producer.getEventsSentCount());

        apexMain.shutdown();
        subscriber.shutdown();
        producer.shutdown();
        ThreadUtilities.sleep(1000);
    }
}
