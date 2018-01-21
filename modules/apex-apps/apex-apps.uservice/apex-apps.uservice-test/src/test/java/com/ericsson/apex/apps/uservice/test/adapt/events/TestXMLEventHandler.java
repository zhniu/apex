/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.events;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.plugins.event.protocol.xml.Apex2XMLEventConverter;
import com.ericsson.apex.service.engine.event.ApexEvent;

/**
 * The Class TestApexXMLEventHandlerURL.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestXMLEventHandler {
    private static final XLogger logger = XLoggerFactory.getXLogger(TestXMLEventHandler.class);

    /**
     * Test XML to apex event.
     *
     * @throws ApexException on Apex event handling errors
     */
    @Test
    public void testXMLtoApexEvent() throws ApexException {
        try {
            final Apex2XMLEventConverter xmlEventConverter = new Apex2XMLEventConverter();
            assertNotNull(xmlEventConverter);

            final String apexEventXMLStringIn = EventGenerator.xmlEvent();

            logger.debug("input event\n" + apexEventXMLStringIn);

            for (ApexEvent apexEvent : xmlEventConverter.toApexEvent(apexEventXMLStringIn)) {
                assertNotNull(apexEvent);

                logger.debug(apexEvent.toString());

                assertTrue(apexEvent.getName().equals("Event0000") || apexEvent.getName().equals("Event0100"));
                assertTrue(apexEvent.getVersion().equals("0.0.1"));
                assertTrue(apexEvent.getNameSpace().equals("com.ericsson.apex.sample.events"));
                assertTrue(apexEvent.getSource().equals("test"));
                assertTrue(apexEvent.getTarget().equals("apex"));
                assertTrue(apexEvent.get("TestSlogan").toString().startsWith("Test slogan for External Event"));

                final Object testMatchCaseSelected = apexEvent.get("TestMatchCaseSelected");
                assertTrue(testMatchCaseSelected == null);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new ApexException("Exception reading Apex event xml file", e);
        }
    }

    /**
     * Test apex event to xml.
     *
     * @throws ApexException on Apex event handling errors
     */
    @Test
    public void testApexEventToXML() throws ApexException {
        try {
            final Apex2XMLEventConverter xmlEventConverter = new Apex2XMLEventConverter();
            assertNotNull(xmlEventConverter);

            final Date event0000StartTime = new Date();
            final Map<String, Object> event0000DataMap = new HashMap<String, Object>();
            event0000DataMap.put("TestSlogan", "This is a test slogan");
            event0000DataMap.put("TestMatchCase", 12345);
            event0000DataMap.put("TestTimestamp", event0000StartTime.getTime());
            event0000DataMap.put("TestTemperature", 34.5445667);

            final ApexEvent apexEvent0000 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.sample.events", "test", "apex");
            apexEvent0000.putAll(event0000DataMap);

            final String apexEvent0000XMLString = xmlEventConverter.fromApexEvent(apexEvent0000);

            logger.debug(apexEvent0000XMLString);

            assertTrue(apexEvent0000XMLString.contains("<name>Event0000</name>"));
            assertTrue(apexEvent0000XMLString.contains("<version>0.0.1</version>"));
            assertTrue(apexEvent0000XMLString.contains("<value>This is a test slogan</value>"));
            assertTrue(apexEvent0000XMLString.contains("<value>12345</value>"));
            assertTrue(apexEvent0000XMLString.contains("<value>" + event0000StartTime.getTime() + "</value>"));
            assertTrue(apexEvent0000XMLString.contains("<value>34.5445667</value>"));

            final Date event0004StartTime = new Date(1434363272000L);
            final Map<String, Object> event0004DataMap = new HashMap<String, Object>();
            event0004DataMap.put("TestSlogan", "Test slogan for External Event");
            event0004DataMap.put("TestMatchCase", new Integer(2));
            event0004DataMap.put("TestTimestamp", new Long(event0004StartTime.getTime()));
            event0004DataMap.put("TestTemperature", new Double(1064.43));
            event0004DataMap.put("TestMatchCaseSelected", new Integer(2));
            event0004DataMap.put("TestMatchStateTime", new Long(1434370506078L));
            event0004DataMap.put("TestEstablishCaseSelected", new Integer(0));
            event0004DataMap.put("TestEstablishStateTime", new Long(1434370506085L));
            event0004DataMap.put("TestDecideCaseSelected", new Integer(3));
            event0004DataMap.put("TestDecideStateTime", new Long(1434370506092L));
            event0004DataMap.put("TestActCaseSelected", new Integer(2));
            event0004DataMap.put("TestActStateTime", new Long(1434370506095L));

            final ApexEvent apexEvent0004 = new ApexEvent("Event0004", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
            apexEvent0004.putAll(event0004DataMap);

            final String apexEvent0004XMLString = xmlEventConverter.fromApexEvent(apexEvent0004);

            logger.debug(apexEvent0004XMLString);

            assertTrue(apexEvent0004XMLString.contains("<name>Event0004</name>"));
            assertTrue(apexEvent0004XMLString.contains("<version>0.0.1</version>"));
            assertTrue(apexEvent0004XMLString.contains("<value>Test slogan for External Event</value>"));
            assertTrue(apexEvent0004XMLString.contains("<value>1434370506078</value>"));
            assertTrue(apexEvent0004XMLString.contains("<value>" + event0004StartTime.getTime() + "</value>"));
            assertTrue(apexEvent0004XMLString.contains("<value>1064.43</value>"));
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new ApexException("Exception reading Apex event xml file", e);
        }
    }
}
