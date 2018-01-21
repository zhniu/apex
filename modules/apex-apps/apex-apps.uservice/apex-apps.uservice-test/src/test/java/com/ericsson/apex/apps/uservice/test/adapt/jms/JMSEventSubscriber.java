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
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JMSEventSubscriber implements Runnable {
    private String topic;
    private long eventsReceivedCount = 0;

    private Thread subscriberThread;
    private Connection connection;

    public JMSEventSubscriber(final String topic, final String host, final String port, final String userName, final String password) throws JMSException {
        this.topic = topic;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("host", host);
        params.put("port", port);
        TransportConfiguration config = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
        ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, config);
        connection = factory.createConnection(userName, password);
        connection.start();

        subscriberThread = new Thread(this);
        subscriberThread.start();
    }

    @Override
    public void run() {
        try {
            Topic jmsTopic = HornetQJMSClient.createTopic(topic);
            Session jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsTopic);

            System.out.println(JMSEventSubscriber.class.getCanonicalName() + ": receiving events from Kafka server on topic " + topic);

            while (subscriberThread.isAlive() && !subscriberThread.isInterrupted()) {
                try {
                    Message message = jmsConsumer.receive(100);
                    if (message == null) {
                        continue;
                    }

                    if (message instanceof ObjectMessage) {
                        TestPing testPing = (TestPing) ((ObjectMessage)message).getObject();
                        System.out.println("Received message: " + testPing.toString());
                        testPing.verify();
                    }
                    else if (message instanceof TextMessage) {
                        String textMessage = ((TextMessage)message).getText();
                        System.out.println("Received message: " + textMessage);
                    }
                    else  {
                        throw new ApexEventException("unknowm message \"" + message + "\" of type \"" + message.getClass().getCanonicalName() + "\" received");
                    }
                    eventsReceivedCount++;
                }
                catch (Exception e) {
                    // Thread interrupted
                    break;
                }
            }

            jmsConsumer.close();
            jmsSession.close();
        }
        catch (Exception e) {
            throw new ApexEventRuntimeException("JMS event consumption failed", e);
        }

        System.out.println(JMSEventSubscriber.class.getCanonicalName() + ": event reception completed");
    }

    public long getEventsReceivedCount() {
        return eventsReceivedCount;
    }

    public void shutdown() throws JMSException {
        subscriberThread.interrupt();

        while (subscriberThread.isAlive()) {
            ThreadUtilities.sleep(10);
        }

        connection.close();
        System.out.println(JMSEventSubscriber.class.getCanonicalName() + ": stopped");
    }


    public static void main(String[] args) throws JMSException {
        if (args.length != 5) {
            System.err.println("usage JMSEventSubscriber topic host port username password");
            return;
        }
        new JMSEventSubscriber(args[0], args[1], args[2], args[3], args[4]);
    }
}
