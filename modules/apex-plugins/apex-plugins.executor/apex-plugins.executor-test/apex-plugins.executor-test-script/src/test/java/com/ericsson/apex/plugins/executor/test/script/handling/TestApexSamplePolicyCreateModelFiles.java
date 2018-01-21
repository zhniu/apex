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

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.test.TestApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class TestApexSamplePolicyCreateModelFiles {
	@Test
	public void testModelWriteReadJava() throws Exception {
		TestApexModel<AxPolicyModel> testApexPolicyModel;
		testApexPolicyModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexSamplePolicyModelCreator("JAVA"));
		testApexPolicyModel.testApexModelWriteReadXML();
		testApexPolicyModel.testApexModelWriteReadJSON();
	}

	@Test
	public void testModelWriteReadJavascript() throws Exception {
		TestApexModel<AxPolicyModel> testApexPolicyModel;
		testApexPolicyModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexSamplePolicyModelCreator("JAVASCRIPT"));
		testApexPolicyModel.testApexModelWriteReadXML();
		testApexPolicyModel.testApexModelWriteReadJSON();
	}

	@Test
	public void testModelWriteReadJRuby() throws Exception {
		TestApexModel<AxPolicyModel> testApexPolicyModel;
		testApexPolicyModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexSamplePolicyModelCreator("JRUBY"));
		testApexPolicyModel.testApexModelWriteReadXML();
		testApexPolicyModel.testApexModelWriteReadJSON();
	}

	@Test
	public void testModelWriteReadJython() throws Exception {
		TestApexModel<AxPolicyModel> testApexPolicyModel;
		testApexPolicyModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexSamplePolicyModelCreator("JYTHON"));
		testApexPolicyModel.testApexModelWriteReadXML();
		testApexPolicyModel.testApexModelWriteReadJSON();
	}

	@Test
	public void testModelWriteReadMvel() throws Exception {
		TestApexModel<AxPolicyModel> testApexPolicyModel;
		testApexPolicyModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexSamplePolicyModelCreator("MVEL"));
		testApexPolicyModel.testApexModelWriteReadXML();
		testApexPolicyModel.testApexModelWriteReadJSON();
	}
}
