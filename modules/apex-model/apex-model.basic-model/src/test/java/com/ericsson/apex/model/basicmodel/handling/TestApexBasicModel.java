/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.basicmodel.test.TestApexModel;

public class TestApexBasicModel {
    private Connection connection;
    TestApexModel<AxModel> testApexModel;

    @Before
    public void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");

        testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexBasicModelCreator());
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
    public void testApexModelVaidateObservation() throws Exception {
        try {
            testApexModel.testApexModelVaidateObservation();
        }
        catch (ApexException e) {
            assertEquals("model should have observations", e.getMessage());
        }
    }

    @Test
    public void testApexModelVaidateWarning() throws Exception {
        AxValidationResult result = testApexModel.testApexModelVaidateWarning();
        assertTrue(result.toString().equals(WARNING_MODEL_STRING));
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

    // As there are no real concepts in a basic model, this is as near to a valid model as we can get
    private static final String VALID_MODEL_STRING = "\n" +
            "***warnings issued during validation of model***\n" +
            "AxArtifactKey:(name=FloatKIKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "AxArtifactKey:(name=IntegerKIKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "********************************";

    private static final String WARNING_MODEL_STRING = "\n" +
            "***warnings issued during validation of model***\n" +
            "AxArtifactKey:(name=FloatKIKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "AxArtifactKey:(name=IntegerKIKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "AxArtifactKey:(name=Unref0,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "AxArtifactKey:(name=Unref1,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:WARNING:key not found for key information entry\n" +
            "********************************";

    private static final String INVALID_MODEL_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=BasicModelKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:WARNING:UUID is a zero UUID: 00000000-0000-0000-0000-000000000000\n" +
            "AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:OBSERVATION:description is blank\n" +
            "AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:WARNING:UUID is a zero UUID: 00000000-0000-0000-0000-000000000000\n" +
            "AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation:INVALID:duplicate UUID found on keyInfoMap entry AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):00000000-0000-0000-0000-000000000000\n" +
            "********************************";

    private static final String INVALID_MODEL_MALSTRUCTURED_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=BasicModelKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:WARNING:UUID is a zero UUID: 00000000-0000-0000-0000-000000000000\n" +
            "AxArtifactKey:(name=BasicModelKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxModel:INVALID:key information not found for key AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1)\n" +
            "********************************";
}
