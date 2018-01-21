/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.test.script.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;

/**
 * The Class TestEventInstantiation.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestEventInstantiation {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestEventInstantiation.class);

	/**
	 * Test event instantiation.
	 *
	 * @throws ApexModelException on errors in handling Apex models
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ApexException the apex exception
	 */
	@Test
	public void testEventInstantiation() throws ApexModelException, IOException, ApexException {
		final String xmlFileName = "xml/ApexModel_MVEL.xml";

		logger.debug("Running TestEventInstantiation test  on file {} . . .", xmlFileName);

		final AxPolicyModel apexPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(apexPolicyModel);

		EngineParameters parameters = new EngineParameters();
		parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
		
		final ApexEngine apexEngine = new ApexEngineFactory().createApexEngine(apexPolicyModel.getKey());
		apexEngine.updateModel(apexPolicyModel);
		apexEngine.start();

		final EnEvent event = apexEngine.createEvent(new AxArtifactKey("Event0000", "0.0.1"));

		Object slogan1 = event.put("TestSlogan", "This is a slogan");
		assertNull(slogan1);
		slogan1 = event.get("TestSlogan");
		assertNotNull(slogan1);
		assertEquals("This is a slogan", slogan1);

		Object mc1 = event.put("TestMatchCase", new Byte("4"));
		assertNull(mc1);
		mc1 = event.get("TestMatchCase");
		assertNotNull(mc1);
		assertEquals((byte) 4, mc1);

		Object mc2 = event.put("TestMatchCase", new Byte("16"));
		assertNotNull(mc2);
		assertEquals((byte) 4, mc2);
		mc2 = event.get("TestMatchCase");
		assertNotNull(mc2);
		assertEquals((byte) 16, mc2);

		final Date timeNow = new Date();
		Object timestamp1 = event.put("TestTimestamp", timeNow.getTime());
		assertNull(timestamp1);
		timestamp1 = event.get("TestTimestamp");
		assertNotNull(timestamp1);
		assertEquals(timeNow.getTime(), timestamp1);

		final double temperature = 123.456789;
		Object temp1 = event.put("TestTemperature", temperature);
		assertNull(temp1);
		temp1 = event.get("TestTemperature");
		assertNotNull(temp1);
		assertEquals(temperature, temp1);

		Object value = event.put("TestMatchCase", null);
		assertEquals(16, ((Byte)value).intValue());
		value = event.get("TestMatchCase");
		assertNull(value);

		try {
			event.put("TestMatchCase", "Hello");
		}
		catch (final Exception e) {
			assertEquals("Event0000:0.0.1:NULL:TestMatchCase: object \"Hello\" of class \"java.lang.String\" not compatible with class \"java.lang.Byte\"", e.getMessage());
		}

		try {
			event.put("TestMatchCase", 123.45);
		}
		catch (final Exception e) {
			assertEquals("Event0000:0.0.1:NULL:TestMatchCase: object \"123.45\" of class \"java.lang.Double\" not compatible with class \"java.lang.Byte\"", e.getMessage());
		}

		event.put("TestMatchCase", new Byte("16"));
		
		final String slogan2 = (String) event.get("TestSlogan");
		assertNotNull(slogan2);
		assertEquals("This is a slogan", slogan2);

		final byte mc21 = (byte) event.get("TestMatchCase");
		assertNotNull(mc21);
		assertEquals(16, mc21);

		final byte mc22 = (byte) event.get("TestMatchCase");
		assertNotNull(mc22);
		assertEquals((byte) 16, mc22);

		final long timestamp2 = (Long) event.get("TestTimestamp");
		assertNotNull(timestamp2);
		assertEquals(timestamp2, timestamp1);

		final double temp2 = (double) event.get("TestTemperature");
		assertNotNull(temp2);
		assertTrue(temp2 == 123.456789);

		final Double temp3 = (Double) event.get("TestTemperature");
		assertNotNull(temp3);
		assertTrue(temp3 == 123.456789);

		final Date aDate = new Date(1433453067123L);
		final Map<String, Object> eventDataList = new HashMap<String, Object>();
		eventDataList.put("TestSlogan",      "This is a test slogan"); 
		eventDataList.put("TestMatchCase",   new Byte("123"));
		eventDataList.put("TestTimestamp",   aDate.getTime());
		eventDataList.put("TestTemperature", 34.5445667);

		event.putAll(eventDataList);

		final String slogan3 = (String) event.get("TestSlogan");
		assertNotNull(slogan3);
		assertEquals("This is a test slogan", slogan3);

		final byte mc31 = (byte) event.get("TestMatchCase");
		assertNotNull(mc31);
		assertEquals((byte) 123, mc31);

		final long timestamp3 = (Long) event.get("TestTimestamp");
		assertNotNull(timestamp3);
		assertEquals(timestamp3, aDate.getTime());

		final double temp4 = (double) event.get("TestTemperature");
		assertNotNull(temp4);
		assertTrue(temp4 == 34.5445667);

		logger.debug(event.toString());
	}
}
