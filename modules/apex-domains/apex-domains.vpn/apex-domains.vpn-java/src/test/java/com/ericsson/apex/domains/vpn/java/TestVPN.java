/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

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
import com.ericsson.apex.domains.vpn.java.concepts.VPNCustomerMap;
import com.ericsson.apex.domains.vpn.java.concepts.VPNLinkMap;
import com.ericsson.apex.domains.vpn.java.model.VPNDomainModelFactory;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;

/**
 * The Class TestApexEngine_VPN_MVEL.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestVPN {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestVPNEventHandler.class);

	private ApexEngine engine = null;
	private TestVPNEventHandler eventHandler = null;
	
	/**
	 * Test vpn setup.
	 * 
	 * @throws Exception on errors setting up the VPN network
	 */
	@Before
	public void testVPNSetup() throws Exception {
		AxPolicyModel apexPolicyModel = new VPNDomainModelFactory().getVPNPolicyModel();
		assertNotNull(apexPolicyModel);
		
		AxValidationResult result = new AxValidationResult();
		apexPolicyModel.validate(result);
		assertTrue(result.isValid());

		eventHandler = new TestVPNEventHandler(engine);
		
		EngineParameters parameters = new EngineParameters();
		parameters.getExecutorParameterMap().put("MVEL", new  MVELExecutorParameters());
		
		engine = new ApexEngineFactory().createApexEngine(new AxArtifactKey("TestApexEngine", "0.0.1"));
		eventHandler = new TestVPNEventHandler(engine);
		engine.addEventListener("listener", eventHandler);
		engine.updateModel(apexPolicyModel);
		engine.start();
	}

	@Test
	public void testVPNMapsUninitialized() throws Exception {
		logger.debug("running test testVPNMapsUninitialized . . . ");
		
		final EnEvent vpnTriggerEvent = engine.createEvent(new AxArtifactKey("VPNTriggerEvent", "0.0.1"));
		vpnTriggerEvent.put("Link", "L09");
		vpnTriggerEvent.put("Status", "DOWN");
		EnEvent actionEvent01 = eventHandler.sendEvent(vpnTriggerEvent);
		assertNotNull(actionEvent01);
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent01.getExecutionID());
		logger.debug("ran test testVPNMapsUninitialized");
	}

	/**
	 * Test vpn policy.
	 * 
	 * @throws Exception on errors testing the VPN policy
	 */
	@Test
	public void testVPNPolicy() throws Exception {
		logger.debug("running test testVPNPolicy . . . ");
		
		setupLinkContext("L09", true);
		setupLinkContext("L10", true);

		setupCustomerContext("A", "L09 L10", 300,  50);
		setupCustomerContext("B", "L09 L10", 300, 299);
		setupCustomerContext("C", "L09 L10", 300, 300);
		setupCustomerContext("D", "L09 L10", 300, 400);

		exerciseLink("L09");
		exerciseLink("L10");

		logger.debug("ran test testVPNPolicy");
	}

	/**
	 * Test vpn cleardown.
	 */
	@After
	public void testVPNCleardown() {
	}

	/**
	 * Setup link context.
	 *
	 * @param link the link
	 * @param isUp the is up
	 * @throws ContextException the apex context exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void setupLinkContext(final String link, final Boolean isUp) throws ContextException, InterruptedException {
		final EnEvent linkCtxtTriggerEvent = engine.createEvent(new AxArtifactKey("VPNLinkCtxtTriggerEvent", "0.0.1"));
		linkCtxtTriggerEvent.put("Link",   link);
		linkCtxtTriggerEvent.put("LinkUp", isUp);
		EnEvent actionEventLink = eventHandler.sendEvent(linkCtxtTriggerEvent);
		assertNotNull(actionEventLink);
		assertEquals(linkCtxtTriggerEvent.get("Link"),   actionEventLink.get("Link"));
		assertEquals(linkCtxtTriggerEvent.get("LinkUp"), actionEventLink.get("LinkUp"));
        assertEquals("ExecutionIDs are different", linkCtxtTriggerEvent.getExecutionID(), actionEventLink.getExecutionID());
	}

	/**
	 * Setup customer context.
	 *
	 * @param customerName the customer name
	 * @param linkList the link list
	 * @param slaDT the sla dt
	 * @param ytdDT the ytd dt
	 * @throws ContextException the apex context exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void setupCustomerContext(final String customerName, final String linkList, final int slaDT, final int ytdDT) throws ContextException, InterruptedException {
		final EnEvent customerCtxtTriggerEvent = engine.createEvent(new AxArtifactKey("VPNCustomerCtxtTriggerEvent", "0.0.1"));
		customerCtxtTriggerEvent.put("CustomerName",  customerName);
		customerCtxtTriggerEvent.put("LinkList",      linkList);
		customerCtxtTriggerEvent.put("SlaDT",         slaDT);
		customerCtxtTriggerEvent.put("YtdDT",         ytdDT);
		EnEvent actionEventCustomer = eventHandler.sendEvent(customerCtxtTriggerEvent);
		assertNotNull(actionEventCustomer);
		assertEquals(customerCtxtTriggerEvent.get("CustomerName"), actionEventCustomer.get("CustomerName"));
		assertEquals(customerCtxtTriggerEvent.get("LinkList"),     actionEventCustomer.get("LinkList"));
		assertEquals(customerCtxtTriggerEvent.get("SlaDT"),        actionEventCustomer.get("SlaDT"));
		assertEquals(customerCtxtTriggerEvent.get("YtdDT"),        actionEventCustomer.get("YtdDT"));
        assertEquals("ExecutionIDs are different", customerCtxtTriggerEvent.getExecutionID(), actionEventCustomer.getExecutionID());
	}

	/**
	 * Exercise link.
	 *
	 * @param link the link
	 * @throws ContextException the apex context exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void exerciseLink(final String link) throws ContextException, InterruptedException {
		Set<String> linkKeys = new TreeSet<String>(Arrays.asList("L09", "L10"));		
		Set<String> custKeys = new TreeSet<String>(Arrays.asList("A", "B", "C", "D"));		

		final EnEvent vpnTriggerEvent = engine.createEvent(new AxArtifactKey("VPNTriggerEvent", "0.0.1"));
		vpnTriggerEvent.put("Link", link);
		
		vpnTriggerEvent.put("Status", "DOWN");
		EnEvent actionEvent01 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent01.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent01.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent01.getExecutionID());

		vpnTriggerEvent.put("Status", "DOWN");
		EnEvent actionEvent02 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent02.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent02.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent02.getExecutionID());

		vpnTriggerEvent.put("Status", "UP");
		EnEvent actionEvent03 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent03.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent03.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent03.getExecutionID());

		vpnTriggerEvent.put("Status", "UP");
		EnEvent actionEvent04 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent04.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent04.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent04.getExecutionID());

		vpnTriggerEvent.put("Status", "DOWN");
		EnEvent actionEvent05 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent05.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent05.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent05.getExecutionID());

		vpnTriggerEvent.put("Status", "UP");
		EnEvent actionEvent06 = eventHandler.sendEvent(vpnTriggerEvent);
		assertEquals(linkKeys, ((VPNLinkMap)    actionEvent06.get("LinkMap"    )).keySet());
		assertEquals(custKeys, ((VPNCustomerMap)actionEvent06.get("CustomerMap")).keySet());
        assertEquals("ExecutionIDs are different", vpnTriggerEvent.getExecutionID(), actionEvent06.getExecutionID());
	}
}
