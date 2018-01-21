/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.enginemodel.handling;

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
import com.ericsson.apex.model.enginemodel.concepts.AxEngineModel;

public class TestApexEngineModel {
    private Connection connection;
    TestApexModel<AxEngineModel> testApexModel;

    @Before
    public void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");

        testApexModel = new TestApexModel<AxEngineModel>(AxEngineModel.class, new TestApexEngineModelCreator());
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
    public void testModelVaidateInvalidModel() throws Exception {
        AxValidationResult result = testApexModel.testApexModelVaidateInvalidModel();
        assertTrue(result.toString().equals(INVALID_MODEL_STRING));
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
    public void testModelWriteReadJPA() throws Exception {
        DAOParameters daoParameters = new DAOParameters();
        daoParameters.setPluginClass("com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao");
        daoParameters.setPersistenceUnit("DAOTest");

        testApexModel.testApexModelWriteReadJPA(daoParameters);
    }

    private static final String VALID_MODEL_STRING = "***validation of model successful***";

    private static final String INVALID_MODEL_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=AnEngine,version=0.0.1):com.ericsson.apex.model.enginemodel.concepts.AxEngineModel:INVALID:AxEngineModel - state is UNDEFINED\n" +
            "********************************";

    private static final String INVALID_MODEL_MALSTRUCTURED_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=AnEngine,version=0.0.1):com.ericsson.apex.model.enginemodel.concepts.AxEngineModel:INVALID:AxEngineModel - timestamp is not set\n" +
            "AxArtifactKey:(name=AnEngine,version=0.0.1):com.ericsson.apex.model.enginemodel.concepts.AxEngineModel:INVALID:AxEngineModel - state is UNDEFINED\n" +
            "********************************";
}
