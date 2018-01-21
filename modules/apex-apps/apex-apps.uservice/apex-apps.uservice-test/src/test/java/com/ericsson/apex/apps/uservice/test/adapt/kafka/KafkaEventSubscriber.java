/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class KafkaEventSubscriber implements Runnable {
    private String topic;
    private String kafkaServerAddress;
    private long eventsReceivedCount = 0;

    KafkaConsumer<String, String> consumer;

    Thread subscriberThread;

    public KafkaEventSubscriber(final String topic, final String kafkaServerAddress) throws MessagingException {
        this.topic = topic;
        this.kafkaServerAddress = kafkaServerAddress;

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServerAddress);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));

        subscriberThread = new Thread(this);
        subscriberThread.start();
    }

    @Override
    public void run() {
        System.out.println(KafkaEventSubscriber.class.getCanonicalName() + ": receiving events from Kafka server at " + kafkaServerAddress + " on topic " + topic);

        while (subscriberThread.isAlive() && !subscriberThread.isInterrupted()) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("******");
                    System.out.println("offset=" + record.offset());
                    System.out.println("key=" + record.key());
                    System.out.println("name=" + record.value());
                    eventsReceivedCount++;
                }
            }
            catch (Exception e) {
                // Thread interrupted
                break;
            }
        }

        System.out.println(KafkaEventSubscriber.class.getCanonicalName() + ": event reception completed");
    }

    public long getEventsReceivedCount() {
        return eventsReceivedCount;
    }

    public void shutdown() {
        subscriberThread.interrupt();

        while (subscriberThread.isAlive()) {
            ThreadUtilities.sleep(10);
        }

        consumer.close();
        System.out.println(KafkaEventSubscriber.class.getCanonicalName() + ": stopped");
    }


    public static void main(String[] args) throws MessagingException {
        if (args.length != 2) {
            System.err.println("usage KafkaEventSubscriber topic kafkaServerAddress");
            return;
        }
        new KafkaEventSubscriber(args[0], args[1]);
    }
}
