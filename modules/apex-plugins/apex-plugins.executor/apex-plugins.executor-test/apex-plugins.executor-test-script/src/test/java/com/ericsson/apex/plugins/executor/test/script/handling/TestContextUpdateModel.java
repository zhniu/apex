/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.test.script.handling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.test.script.engine.TestApexActionListener;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;

/**
 * The Class TestApexEngine.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextUpdateModel {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestContextUpdateModel.class);

	@Before
	public void testContextUpdateModelBefore() {
	}

	@Test
	public void testContextUpdateModel() throws ApexException, InterruptedException, IOException {
		final AxArtifactKey key = new AxArtifactKey("TestApexEngine", "0.0.1");

		EngineParameters parameters = new EngineParameters();
		parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());

		final ApexEngine apexEngine = new ApexEngineFactory().createApexEngine(key);
		final TestApexActionListener listener = new TestApexActionListener("Test");
		apexEngine.addEventListener("listener", listener);

		final AxPolicyModel model1 = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(model1);
		assertEquals(2, model1.getPolicies().getPolicyMap().size());

		apexEngine.updateModel(model1);
		apexEngine.start();
		sendEvent(apexEngine, listener, "Event0000", true);
		sendEvent(apexEngine, listener, "Event0100", true);
		apexEngine.stop();

		final AxPolicyModel model2 = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(model2);
		model2.getPolicies().getPolicyMap().remove(new AxArtifactKey("Policy0", "0.0.1"));
		assertEquals(1, model2.getPolicies().getPolicyMap().size());
		apexEngine.updateModel(model2);
		apexEngine.start();
		sendEvent(apexEngine, listener, "Event0000", false);
		sendEvent(apexEngine, listener, "Event0100", true);
		apexEngine.stop();

		final AxPolicyModel model3 = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(model3);
		model3.getPolicies().getPolicyMap().remove(new AxArtifactKey("Policy1", "0.0.1"));
		assertEquals(1, model3.getPolicies().getPolicyMap().size());
		apexEngine.updateModel(model3);
		apexEngine.start();
		sendEvent(apexEngine, listener, "Event0000", true);
		sendEvent(apexEngine, listener, "Event0100", false);
		apexEngine.stop();

		final AxPolicyModel model4 = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(model4);
		assertEquals(2, model4.getPolicies().getPolicyMap().size());
		apexEngine.updateModel(model4);
		apexEngine.start();
		sendEvent(apexEngine, listener, "Event0100", true);
		sendEvent(apexEngine, listener, "Event0000", true);
		apexEngine.stop();

		apexEngine.clear();
	}

	@After
	public void testContextUpdateModelAfter() {
	}

	private void sendEvent(ApexEngine apexEngine, TestApexActionListener listener, String eventName, boolean shouldWork) throws ContextException {
		final Date aDate = new Date(1433453067123L);
		final Map<String, Object> eventDataMap = new HashMap<String, Object>();
		eventDataMap.put("TestSlogan",                "This is a test slogan");
		eventDataMap.put("TestMatchCase",             new Byte((byte) 123));
		eventDataMap.put("TestTimestamp",             aDate.getTime());
		eventDataMap.put("TestTemperature",           34.5445667);

		final EnEvent event0 = apexEngine.createEvent(new AxArtifactKey(eventName, "0.0.1"));
		event0.putAll(eventDataMap);
		apexEngine.handleEvent(event0);

		EnEvent result = listener.getResult(true);
		logger.debug("result 1 is:" + result);
		checkResult(result, shouldWork);
	}

	private void checkResult(EnEvent result, boolean shouldWork) {
		if (!shouldWork) {
			assertNotNull(result.getExceptionMessage());
			return;
		}
		
		assertTrue(result.getName().equals("Event0004") || result.getName().equals("Event0104"));

		if (result.getName().equals("Event0004")) {
			assertEquals("This is a test slogan", result.get("TestSlogan"));
			assertEquals((byte)123,  result.get("TestMatchCase"));
			assertEquals(34.5445667, result.get("TestTemperature"));
			assertEquals((byte)2,    result.get("TestMatchCaseSelected"));
			assertEquals((byte)0,    result.get("TestEstablishCaseSelected"));
			assertEquals((byte)1,    result.get("TestDecideCaseSelected"));
			assertEquals((byte)3,    result.get("TestActCaseSelected"));
		}
		else {
			assertEquals("This is a test slogan", result.get("TestSlogan"));
			assertEquals((byte)123,  result.get("TestMatchCase"));
			assertEquals(34.5445667, result.get("TestTemperature"));
			assertEquals((byte)1,    result.get("TestMatchCaseSelected"));
			assertEquals((byte)3,    result.get("TestEstablishCaseSelected"));
			assertEquals((byte)1,    result.get("TestDecideCaseSelected"));
			assertEquals((byte)2,    result.get("TestActCaseSelected"));
		}
	}
}
