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

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import com.ericsson.apex.apps.uservice.test.adapt.events.EventGenerator;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JMSEventProducer implements Runnable {
    private String topic;
    private final int eventCount;
    private final boolean sendObjects;
    private final long eventInterval;
    private long eventsSentCount = 0;

    private Thread producerThread;
    private boolean sendEventsFlag = false;
    private boolean stopFlag = false;
    private Connection connection;

    public JMSEventProducer(final String topic, final String host, final String port, final String userName, final String password, final int eventCount,
            final boolean sendObjects, final long eventInterval) throws JMSException {
        this.topic = topic;
        this.eventCount = eventCount;
        this.sendObjects = sendObjects;
        this.eventInterval = eventInterval;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("host", host);
        params.put("port", port);
        TransportConfiguration config = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
        ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, config);
        connection = factory.createConnection(userName, password);
        connection.start();

        producerThread = new Thread(this);
        producerThread.start();
    }

    @Override
    public void run() {
        try {
            Topic jmsTopic = HornetQJMSClient.createTopic(topic);
            Session jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer jmsProducer = jmsSession.createProducer(jmsTopic);

            while (producerThread.isAlive() && !stopFlag) {
                ThreadUtilities.sleep(50);

                if (sendEventsFlag) {
                    sendEventsToTopic(jmsSession, jmsProducer);
                    sendEventsFlag = false;
                }
            }

            jmsProducer.close();
            jmsSession.close();
        }
        catch (Exception e) {
            throw new ApexEventRuntimeException("JMS event consumption failed", e);
        }
    }

    public void sendEvents() {
        sendEventsFlag = true;
    }

    private void sendEventsToTopic(Session jmsSession, MessageProducer jmsProducer) throws JMSException {
        System.out.println(JMSEventProducer.class.getCanonicalName() + ": sending events to JMS server, event count " + eventCount);

        for (int i = 0; i < eventCount; i++) {
            System.out.println(JMSEventProducer.class.getCanonicalName() + ": waiting " + eventInterval + " milliseconds before sending next event");
            ThreadUtilities.sleep(eventInterval);

            Message jmsMessage = null;
            if (sendObjects) {
                jmsMessage = jmsSession.createObjectMessage(new TestPing());
            }
            else {
                jmsMessage = jmsSession.createTextMessage(EventGenerator.jsonEvent());
            }
            jmsProducer.send(jmsMessage);
            eventsSentCount++;
            System.out.println(JMSEventProducer.class.getCanonicalName() + ": sent event " + jmsMessage.toString());
        }
        System.out.println(JMSEventProducer.class.getCanonicalName() + ": completed");
    }

    public long getEventsSentCount() {
        return eventsSentCount;
    }

    public void shutdown() {
        System.out.println(JMSEventProducer.class.getCanonicalName() + ": stopping");

        stopFlag = true;

        while (producerThread.isAlive()) {
            ThreadUtilities.sleep(10);
        }

        System.out.println(JMSEventProducer.class.getCanonicalName() + ": stopped");
    }

    public static void main(String[] args) throws JMSException {
        if (args.length != 8) {
            System.err.println("usage JMSEventProducer topic host port username password #events sendObjects eventInterval");
            return;
        }

        int eventCount = 0;
        try {
            eventCount = Integer.parseInt(args[5]);
        }
        catch (Exception e) {
            System.err.println("usage JMSEventProducer topic host port username password #events sendObjects eventInterval");
            e.printStackTrace();
            return;
        }

        long eventInterval = 0;
        try {
            eventInterval = Long.parseLong(args[7]);
        }
        catch (Exception e) {
            System.err.println("usage JMSEventProducer topic host port username password #events sendObjects eventInterval");
            e.printStackTrace();
            return;
        }

        boolean sendObjects = false;
        if (args[6].equalsIgnoreCase("true")) {
            sendObjects = true;
        }
        else if (!args[3].equalsIgnoreCase("JSON")) {
            System.err.println("usage JMSEventProducer topic host port username password #events sendObjects eventInterval");
            return;
        }

        JMSEventProducer producer = new JMSEventProducer(args[0], args[1], args[2], args[3], args[4], eventCount, sendObjects, eventInterval);

        producer.sendEvents();
        producer.shutdown();
    }
}
