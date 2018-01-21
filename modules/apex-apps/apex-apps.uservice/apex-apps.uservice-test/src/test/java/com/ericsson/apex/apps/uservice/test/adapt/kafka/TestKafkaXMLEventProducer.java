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

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.ericsson.apex.plugins.event.protocol.xml.jaxb.XMLApexEvent;
import com.ericsson.apex.plugins.event.protocol.xml.jaxb.XMLApexEventData;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestKafkaXMLEventProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:49092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        XMLApexEvent xmlEvent = new XMLApexEvent();
        xmlEvent.setName("XMLEvent-1");
        xmlEvent.setVersion("0.0.1");
        xmlEvent.getData().add(new XMLApexEventData("Data-1", "Data Value -1"));
        
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 100; i++) {
            xmlEvent.setName("XMLEvent" + Integer.toString(i));
            xmlEvent.setVersion("0.0.1");
            xmlEvent.getData().add(new XMLApexEventData("Data" + Integer.toString(i), "Data Value " + Integer.toString(i)));

            producer.send(new ProducerRecord<String, String>("apex-in-0", xmlEvent.getName(), xmlEvent.toString()));
        }
        producer.close();
    }
}
