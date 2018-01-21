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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.handling.PolicyAnalyser;
import com.ericsson.apex.model.policymodel.handling.PolicyAnalysisResult;

public class TestApexPolicyModelAnalysis {
	@Test
	public void testApexPolicyModelAnalysis() throws Exception {
		AxPolicyModel model = new TestApexSamplePolicyModelCreator("MVEL").getModel();
		PolicyAnalysisResult result = new PolicyAnalyser().analyse(model);
	
		assertNotNull(result);
		System.out.println(result);
	}
}
