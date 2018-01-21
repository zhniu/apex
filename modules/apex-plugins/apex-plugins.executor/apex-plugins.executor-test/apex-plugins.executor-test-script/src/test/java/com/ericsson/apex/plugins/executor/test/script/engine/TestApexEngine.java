/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.test.script.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;

public class TestApexEngine {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestApexEngine.class);

	/**
	 * Instantiates a new test apex engine.
	 *
	 * @param axLogicExecutorType the type of logic executor to use to construct the sample policy model for this test
	 * @throws ApexException the apex exception
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TestApexEngine(String axLogicExecutorType, EngineParameters parameters) throws ApexException, InterruptedException, IOException {
		before();

		logger.debug("Running TestApexEngine test for + " + axLogicExecutorType + "logic . . .");

		final AxPolicyModel apexPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel(axLogicExecutorType);
		assertNotNull(apexPolicyModel);
		final AxArtifactKey key = new AxArtifactKey("TestApexEngine", "0.0.1");

		final ApexEngine apexEngine = new ApexEngineFactory().createApexEngine(key);
		final TestApexActionListener listener = new TestApexActionListener("Test");
		apexEngine.addEventListener("listener", listener);
		apexEngine.updateModel(apexPolicyModel);
		apexEngine.start();

		for (final AxEvent axEvent : apexPolicyModel.getEvents().getEventMap().values()) {
			final EnEvent event = apexEngine.createEvent(axEvent.getKey());

			final Date aDate = new Date(1433453067123L);
			final Map<String, Object> eventDataMap = new HashMap<String, Object>();
			eventDataMap.put("TestSlogan",      "This is a test slogan for event " + event.getName());
			eventDataMap.put("TestMatchCase",   new Byte((byte) 123));
			eventDataMap.put("TestTimestamp",   aDate.getTime());
			eventDataMap.put("TestTemperature", 34.5445667);

			event.putAll(eventDataMap);

			apexEngine.handleEvent(event);
		}

		EnEvent result = listener.getResult(false);
		logger.debug("result 1 is:" + result);
		checkResult(result);
		result = listener.getResult(false);
		logger.debug("result 2 is:" + result);
		checkResult(result);

		Map<AxArtifactKey, Map<String, Object>> apexContext = apexEngine.getEngineContext();
		assertNotNull(apexContext);
		apexEngine.stop();
		after();
	}

	/**
	 * Before.
	 */
	private void before() {
	}

	/**
	 * After.
	 */
	private void after() {
	}

	/**
	 * Check result.
	 *
	 * @param result the result
	 */
	private void checkResult(EnEvent result) {
	    if (result.getExceptionMessage() == null) {
	        assertTrue(result.getName().equals("Event0004") || result.getName().equals("Event0104"));

	        assertTrue(((String)result.get("TestSlogan")).startsWith("This is a test slogan for event "));
	        assertTrue(((String)result.get("TestSlogan")).contains(result.getName().substring(0, 8)));

	        assertEquals((byte)123,  result.get("TestMatchCase"));
	        assertEquals(34.5445667, result.get("TestTemperature"));
	        assertTrue((Byte)result.get("TestMatchCaseSelected")     >= 0 && (Byte)result.get("TestMatchCaseSelected")     <= 4);
	        assertTrue((Byte)result.get("TestEstablishCaseSelected") >= 0 && (Byte)result.get("TestEstablishCaseSelected") <= 4);
	        assertTrue((Byte)result.get("TestDecideCaseSelected")    >= 0 && (Byte)result.get("TestDecideCaseSelected")    <= 4);
	        assertTrue((Byte)result.get("TestActCaseSelected")       >= 0 && (Byte)result.get("TestActCaseSelected")       <= 4);
	    }
	    else {
            assertTrue(result.getName().equals("Event0001") || result.getName().equals("Event0104"));

            assertTrue(((String)result.get("TestSlogan")).startsWith("This is a test slogan for event "));
            assertTrue(((String)result.get("TestSlogan")).contains(result.getName().substring(0, 8)));

            assertEquals((byte)123,  result.get("TestMatchCase"));
            assertEquals(34.5445667, result.get("TestTemperature"));
	    }
	}
}
