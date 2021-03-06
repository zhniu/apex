/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.aadm;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.basicmodel.test.TestApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class TestAADMModel {
	private Connection connection;
	TestApexModel<AxPolicyModel> testApexModel;

	@Before
	public void setup() throws Exception {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");

		testApexModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestAADMModelCreator());
	}

	@After
	public void teardown() throws Exception {
		connection.close();
		new File("derby.log").delete();
	}

	@Test
	public void testModelValid() throws Exception {
		AxValidationResult result = testApexModel.testApexModelValid();
		assertTrue(result.toString().equals(VALID_MODEL_STRING));
	}

	@Test
	public void testModelWriteReadXML() throws Exception {
		testApexModel.testApexModelWriteReadXML();
	}

	@Test
	public void testModelWriteReadJSON() throws Exception {
		testApexModel.testApexModelWriteReadJSON();
	}

	@Test
	public void testModelWriteReadJPA() throws Exception {
		DAOParameters daoParameters = new DAOParameters();
		daoParameters.setPluginClass("com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao");
		daoParameters.setPersistenceUnit("AADMModelTest");

		testApexModel.testApexModelWriteReadJPA(daoParameters);
	}

	private static final String VALID_MODEL_STRING = 
			"***validation of model successful***";
}
