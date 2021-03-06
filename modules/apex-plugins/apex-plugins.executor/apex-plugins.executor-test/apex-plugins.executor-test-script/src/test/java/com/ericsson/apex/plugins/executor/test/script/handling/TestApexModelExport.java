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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.handling.PolicyModelSplitter;

/**
 * The Class TestApexModelReader tests Apex model reading.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexModelExport {
	private static final XLogger logger = XLoggerFactory.getXLogger(TestApexModelExport.class);

	private AxPolicyModel model = null;

	@Before
	public void initApexModelSmall() throws ApexException {
		model = new TestApexSamplePolicyModelCreator("MVEL").getModel();
	}

	@Test
	public void testApexModelExport() throws Exception {
		logger.info("Starting test: testApexModelExport");

		List<AxArtifactKey> exportPolicyList = new ArrayList<AxArtifactKey>();
		exportPolicyList.addAll(model.getPolicies().getPolicyMap().keySet());
		
		AxPolicyModel exportedModel0 = PolicyModelSplitter.getSubPolicyModel(model, exportPolicyList);
		
		// Remove unused schemas and their keys
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem000", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem001", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem002", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem003", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem004", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem005", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem006", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem007", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem008", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem009", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem00A", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem00B", "0.0.1"));
		model.getSchemas().getSchemasMap().remove(new AxArtifactKey("TestContextItem00C", "0.0.1"));

		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem000", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem001", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem002", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem003", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem004", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem005", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem006", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem007", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem008", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem009", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem00A", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem00B", "0.0.1"));
		model.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("TestContextItem00C", "0.0.1"));
		
		assertTrue(model.equals(exportedModel0));

		exportPolicyList.remove(0);

		AxPolicyModel exportedModel1 = PolicyModelSplitter.getSubPolicyModel(model, exportPolicyList);
		assertFalse(model.equals(exportedModel1));
		assertTrue(model.getPolicies().get("Policy1").equals(exportedModel1.getPolicies().get("Policy1")));

		exportPolicyList.clear();
		exportPolicyList.add(new AxArtifactKey("NonExistentPolicy", "0.0.1"));

		try {
			AxPolicyModel emptyExportedModel = PolicyModelSplitter.getSubPolicyModel(model, exportPolicyList);
			assertNotNull(emptyExportedModel);
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("new model is invalid:\n" + 
					"***validation of model failed***\n" +
					"AxPolicies:Policies:0.0.1 - policyMap may not be null or empty\n" +
					"AxEvents:Events:0.0.1 - eventMap may not be null or empty\n" +
					"********************************"));
		}
	}
}
