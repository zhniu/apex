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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.TextFileUtils;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.Apex2JSONEventConverter;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.JSONEventProtocolParameters;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestJSONEventHandler {
    private static final XLogger logger = XLoggerFactory.getXLogger(TestJSONEventHandler.class);

    @BeforeClass
    public static void setupEventModel() throws IOException, ApexModelException {
        String policyModelString =  TextFileUtils.getTextFileAsString("src/test/resources/policymodels/SamplePolicyModelMVEL.json");
        ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<AxPolicyModel>(AxPolicyModel.class);
        AxPolicyModel apexPolicyModel = modelReader.read(new ByteArrayInputStream(policyModelString.getBytes()));

        // Set up the models in the model service
        apexPolicyModel.register();
    }

    @Before
    public void initializeDefaultSchemaParameters() {
        new SchemaParameters();
    }

    @Test
    public void testJSONtoApexEvent() throws ApexException {
        try {
            final Apex2JSONEventConverter jsonEventConverter = new Apex2JSONEventConverter();
            assertNotNull(jsonEventConverter);
            jsonEventConverter.init(new JSONEventProtocolParameters());

            final String apexEventJSONStringIn = EventGenerator.jsonEvent();

            logger.debug("input event\n" + apexEventJSONStringIn);

            for ( ApexEvent apexEvent : jsonEventConverter.toApexEvent(null, apexEventJSONStringIn)) {
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
            throw new ApexException("Exception reading Apex event JSON file", e);
        }
    }

    @Test
    public void testJSONtoApexBadEvent() throws ApexException {
        try {
            final Apex2JSONEventConverter jsonEventConverter = new Apex2JSONEventConverter();
            assertNotNull(jsonEventConverter);
            jsonEventConverter.init(new JSONEventProtocolParameters());

            String apexEventJSONStringIn = null;

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventNoName();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: event received without mandatory parameter \"name\" on configuration or on event"));
            }

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventBadName();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: field \"name\" with value \"%%%%\" is invalid"));
            }

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventNoExName();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: an event definition for an event named \"I_DONT_EXIST\" not found in Apex model"));
            }

            apexEventJSONStringIn = EventGenerator.jsonEventNoVersion();
            ApexEvent event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals("0.0.1", event.getVersion());

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventBadVersion();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: field \"version\" with value \"#####\" is invalid"));
            }

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventNoExVersion();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: an event definition for an event named \"Event0000\" with version \"1.2.3\" not found in Apex model"));
            }

            apexEventJSONStringIn = EventGenerator.jsonEventNoNamespace();
            event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals("com.ericsson.apex.sample.events", event.getNameSpace());

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventBadNamespace();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: field \"nameSpace\" with value \"hello.&&&&\" is invalid"));
            }

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventNoExNamespace();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith(
                        "Failed to unmarshal JSON event: namespace \"pie.in.the.sky\" on event \"Event0000\" " +
                        "does not match namespace \"com.ericsson.apex.sample.events\" for that event in the Apex model"));
            }

            apexEventJSONStringIn = EventGenerator.jsonEventNoSource();
            event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals("Outside", event.getSource());

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventBadSource();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: field \"source\" with value \"%!@**@!\" is invalid"));
            }

            apexEventJSONStringIn = EventGenerator.jsonEventNoTarget();
            event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals("Match", event.getTarget());

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventBadTarget();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: field \"target\" with value \"KNIO(*S)A(S)D\" is invalid"));
            }

            try {
                apexEventJSONStringIn = EventGenerator.jsonEventMissingFields();
                jsonEventConverter.toApexEvent(null, apexEventJSONStringIn);
                fail("Test should throw an exception here");
            }
            catch (ApexEventException e) {
                assertTrue(e.getMessage().startsWith("Failed to unmarshal JSON event: error parsing Event0000:0.0.1 "
                        + "event from Json. Field \"TestMatchCase\" is missing, but is mandatory."));
            }

            apexEventJSONStringIn = EventGenerator.jsonEventNullFields();
            event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals(event.get("TestSlogan"), null);
            assertEquals(event.get("TestMatchCase"), (byte)-1);
            assertEquals(event.get("TestTimestamp"), (long)-1);
            assertEquals(event.get("TestTemperature"), -1.0);

            // Set the missing fields as optional in the model
            AxEvent eventDefinition = ModelService.getModel(AxEvents.class).get("Event0000");
            eventDefinition.getParameterMap().get("TestSlogan").setOptional(true);
            eventDefinition.getParameterMap().get("TestMatchCase").setOptional(true);
            eventDefinition.getParameterMap().get("TestTimestamp").setOptional(true);
            eventDefinition.getParameterMap().get("TestTemperature").setOptional(true);

            apexEventJSONStringIn = EventGenerator.jsonEventMissingFields();
            event = jsonEventConverter.toApexEvent(null, apexEventJSONStringIn).get(0);
            assertEquals(null, event.get("TestSlogan"));
            assertEquals(null, event.get("TestMatchCase"));
            assertEquals(null, event.get("TestTimestamp"));
            assertEquals(null, event.get("TestTemperature"));
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new ApexException("Exception reading Apex event JSON file", e);
        }
    }

    @Test
    public void testApexEventToJSON() throws ApexException {
        try {
            final Apex2JSONEventConverter jsonEventConverter = new Apex2JSONEventConverter();
            assertNotNull(jsonEventConverter);

            final Date event0000StartTime = new Date();
            final Map<String, Object> event0000DataMap = new HashMap<String, Object>();
            event0000DataMap.put("TestSlogan", "This is a test slogan");
            event0000DataMap.put("TestMatchCase", 12345);
            event0000DataMap.put("TestTimestamp", event0000StartTime.getTime());
            event0000DataMap.put("TestTemperature", 34.5445667);

            final ApexEvent apexEvent0000 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.sample.events", "test", "apex");
            apexEvent0000.putAll(event0000DataMap);

            final String apexEvent0000JSONString = (String) jsonEventConverter.fromApexEvent(apexEvent0000);

            logger.debug(apexEvent0000JSONString);

            assertTrue(apexEvent0000JSONString.contains("\"name\": \"Event0000\""));
            assertTrue(apexEvent0000JSONString.contains("\"version\": \"0.0.1\""));
            assertTrue(apexEvent0000JSONString.contains("\"nameSpace\": \"com.ericsson.apex.sample.events\""));
            assertTrue(apexEvent0000JSONString.contains("\"source\": \"test\""));
            assertTrue(apexEvent0000JSONString.contains("\"target\": \"apex\""));
            assertTrue(apexEvent0000JSONString.contains("\"TestSlogan\": \"This is a test slogan\""));
            assertTrue(apexEvent0000JSONString.contains("\"TestMatchCase\": 12345"));
            assertTrue(apexEvent0000JSONString.contains("\"TestTimestamp\": " + event0000StartTime.getTime()));
            assertTrue(apexEvent0000JSONString.contains("\"TestTemperature\": 34.5445667"));

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

            final ApexEvent apexEvent0004 = new ApexEvent("Event0004", "0.0.1", "com.ericsson.apex.sample.events", "test", "apex");
            apexEvent0004.putAll(event0004DataMap);

            final String apexEvent0004JSONString = (String) jsonEventConverter.fromApexEvent(apexEvent0004);

            logger.debug(apexEvent0004JSONString);

            assertTrue(apexEvent0004JSONString.contains("\"name\": \"Event0004\""));
            assertTrue(apexEvent0004JSONString.contains("\"version\": \"0.0.1\""));
            assertTrue(apexEvent0004JSONString.contains("\"nameSpace\": \"com.ericsson.apex.sample.events\""));
            assertTrue(apexEvent0004JSONString.contains("\"source\": \"test\""));
            assertTrue(apexEvent0004JSONString.contains("\"target\": \"apex\""));
            assertTrue(apexEvent0004JSONString.contains("\"TestSlogan\": \"Test slogan for External Event\""));
            assertTrue(apexEvent0004JSONString.contains("1434370506078"));
            assertTrue(apexEvent0004JSONString.contains("1064.43"));
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw new ApexException("Exception reading Apex event JSON file", e);
        }
    }
}
