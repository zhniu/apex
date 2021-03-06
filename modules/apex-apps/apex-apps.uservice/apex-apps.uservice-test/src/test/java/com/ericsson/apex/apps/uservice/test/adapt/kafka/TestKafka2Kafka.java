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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.common.utils.Time;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.main.ApexMain;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.MockTime;
import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import kafka.zk.EmbeddedZookeeper;

public class TestKafka2Kafka {
    // The method of starting an embedded Kafka server used in this example is based on the method on slashdot at
    // https://github.com/asmaier/mini-kafka

    private static final long MAX_TEST_LENGTH = 10000;
    
    private static final int EVENT_COUNT    = 100;
    private static final int EVENT_INTERVAL =  20;

    private static final String ZKHOST     = "127.0.0.1";
    private static final String BROKERHOST = "127.0.0.1";
    private static final String BROKERPORT = "39902";

    private static EmbeddedZookeeper zkServer;
    private static ZkClient zkClient;
    private static KafkaServer kafkaServer;

    @BeforeClass
    public static void setupDummyKafkaServer() throws IOException {
        // setup Zookeeper
        zkServer = new EmbeddedZookeeper();
        String zkConnect = ZKHOST + ":" + zkServer.port();
        zkClient = new ZkClient(zkConnect, 30000, 30000, ZKStringSerializer$.MODULE$);
        ZkUtils zkUtils = ZkUtils.apply(zkClient, false);

        // setup Broker
        Properties brokerProps = new Properties();
        brokerProps.setProperty("zookeeper.connect", zkConnect);
        brokerProps.setProperty("broker.id", "0");
        brokerProps.setProperty("log.dirs", Files.createTempDirectory("kafka-").toAbsolutePath().toString());
        brokerProps.setProperty("listeners", "PLAINTEXT://" + BROKERHOST +":" + BROKERPORT);
        KafkaConfig config = new KafkaConfig(brokerProps);
        Time mock = new MockTime();
        kafkaServer = TestUtils.createServer(config, mock);
        
        // create topics
        AdminUtils.createTopic(zkUtils, "apex-in-0", 1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$);
        AdminUtils.createTopic(zkUtils, "apex-in-1", 1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$);
        AdminUtils.createTopic(zkUtils, "apex-out",  1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$);
    }

    @AfterClass
    public static void shutdownDummyKafkaServer() throws IOException {
        kafkaServer.shutdown();
        zkClient.close();
        zkServer.shutdown();
    }

    @Test
    public void testJsonKafkaEvents() throws MessagingException, ApexException {
        String[] args = {"src/test/resources/prodcons/Kafka2KafkaJsonEvent.json"};
        testKafkaEvents(args, false, "json");
    }

    @Test
    public void testXMLKafkaEvents() throws MessagingException, ApexException {
        String[] args= {"src/test/resources/prodcons/Kafka2KafkaXmlEvent.json"};
        testKafkaEvents(args, true, "xml");
    }

    private void testKafkaEvents(final String[] args, final Boolean xmlEvents, final String topicSuffix) throws MessagingException, ApexException {
        KafkaEventSubscriber subscriber  = new KafkaEventSubscriber("apex-out-" + topicSuffix, "localhost:" + BROKERPORT);
        KafkaEventProducer   producer    = new KafkaEventProducer  ("apex-in-"  + topicSuffix, "localhost:" + BROKERPORT, EVENT_COUNT, xmlEvents, EVENT_INTERVAL);

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
