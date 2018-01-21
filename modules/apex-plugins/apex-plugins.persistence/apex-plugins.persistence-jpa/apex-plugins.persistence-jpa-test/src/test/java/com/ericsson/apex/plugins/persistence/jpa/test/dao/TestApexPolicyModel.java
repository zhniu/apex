/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.persistence.jpa.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.basicmodel.test.TestApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class TestApexPolicyModel {
	TestApexModel<AxPolicyModel> testApexModel;

	@Before
	public void setup() throws Exception {
		testApexModel = new TestApexModel<AxPolicyModel>(AxPolicyModel.class, new TestApexPolicyModelCreator());
	}

	@Test
	public void testModelValid() throws Exception {
		AxValidationResult result = testApexModel.testApexModelValid();
		assertTrue(result.toString().equals(VALID_MODEL_STRING));
	}

	@Test
	public void testApexModelVaidateObservation() throws Exception {
		AxValidationResult result = testApexModel.testApexModelVaidateObservation();
		assertTrue(result.toString().equals(OBSERVATION_MODEL_STRING));
	}


	@Test
	public void testApexModelVaidateWarning() throws Exception {
		AxValidationResult result = testApexModel.testApexModelVaidateWarning();
		assertTrue(result.toString().equals(WARNING_MODEL_STRING));
	}

	@Test
	public void testModelVaidateInvalidModel() throws Exception {
		AxValidationResult result = testApexModel.testApexModelVaidateInvalidModel();
		assertEquals(INVALID_MODEL_STRING, result.toString());
	}

	@Test
	public void testModelVaidateMalstructured() throws Exception {
		AxValidationResult result = testApexModel.testApexModelVaidateMalstructured();
		assertTrue(result.toString().equals(INVALID_MODEL_MALSTRUCTURED_STRING));
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
	public void testModelWriteReadJPAEclipselink() throws Exception {
		DAOParameters daoParameters = new DAOParameters();
		daoParameters.setPersistenceUnit("DAOTestEclipselink");
		daoParameters.setPluginClass("com.ericsson.apex.plugins.persistence.jpa.eclipselink.EclipselinkApexDao");

		testApexModel.testApexModelWriteReadJPA(daoParameters);
	}

	@Test
	public void testModelWriteReadJPAHibernate() throws Exception {
		DAOParameters daoParameters = new DAOParameters();
		daoParameters.setPersistenceUnit("DAOTestHibernate");
		daoParameters.setPluginClass("com.ericsson.apex.plugins.persistence.jpa.hibernate.HibernateApexDao");

		testApexModel.testApexModelWriteReadJPA(daoParameters);
	}

	private static final String VALID_MODEL_STRING =
			"***validation of model successful***";

	private static final String OBSERVATION_MODEL_STRING = "\n" +
			"***observations noted during validation of model***\n" +
			"AxReferenceKey:(parentKeyName=policy,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=state):com.ericsson.apex.model.policymodel.concepts.AxState:OBSERVATION:state output stateOutput0 is not used directly by any task\n" +
			"********************************";

	private static final String WARNING_MODEL_STRING = "\n" +
			"***warnings issued during validation of model***\n" +
			"AxArtifactKey:(name=policy,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicy:WARNING:state AxReferenceKey:(parentKeyName=policy,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=anotherState) is not referenced in the policy execution tree\n" +
			"********************************";

	private static final String INVALID_MODEL_STRING = "\n" +
			"***validation of model failed***\n" +
			"AxArtifactKey:(name=BoolTypeAlbum,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum:INVALID:scope is not defined\n" +
			"AxArtifactKey:(name=MapTypeAlbum,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum:INVALID:scope is not defined\n" +
			"AxReferenceKey:(parentKeyName=policy,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=state):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:task output field AxOutputField:(key=AxReferenceKey:(parentKeyName=task,parentKeyVersion=0.0.1,parentLocalName=outputFields,localName=OE1PAR0),fieldSchemaKey=AxArtifactKey:(name=eventContextItem0,version=0.0.1),optional=false) for task task:0.0.1 not in output event outEvent0:0.0.1\n" +
			"AxReferenceKey:(parentKeyName=policy,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=state):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:task output field AxOutputField:(key=AxReferenceKey:(parentKeyName=task,parentKeyVersion=0.0.1,parentLocalName=outputFields,localName=OE1PAR1),fieldSchemaKey=AxArtifactKey:(name=eventContextItem1,version=0.0.1),optional=false) for task task:0.0.1 not in output event outEvent0:0.0.1\n" +
			"********************************";

	private static final String INVALID_MODEL_MALSTRUCTURED_STRING = "\n" +
			"***validation of model failed***\n" +
			"AxArtifactKey:(name=policyModel_KeyInfo,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation:INVALID:keyInfoMap may not be empty\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_KeyInfo,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_Schemas,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_Events,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_Albums,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_Tasks,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicyModel:INVALID:key information not found for key AxArtifactKey:(name=policyModel_Policies,version=0.0.1)\n" +
			"AxArtifactKey:(name=policyModel_Schemas,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas:INVALID:contextSchemas may not be empty\n" +
			"AxArtifactKey:(name=policyModel_Events,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvents:INVALID:eventMap may not be empty\n" +
			"AxArtifactKey:(name=policyModel_Albums,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums:OBSERVATION:albums are empty\n" +
			"AxArtifactKey:(name=policyModel_Tasks,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxTasks:INVALID:taskMap may not be empty\n" +
			"AxArtifactKey:(name=policyModel_Policies,version=0.0.1):com.ericsson.apex.model.policymodel.concepts.AxPolicies:INVALID:policyMap may not be empty\n" +
			"********************************";
}
