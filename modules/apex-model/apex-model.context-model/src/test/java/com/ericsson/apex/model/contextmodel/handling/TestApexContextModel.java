/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.handling;

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
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;

public class TestApexContextModel {
    private Connection connection;
    TestApexModel<AxContextModel> testApexModel;

    @Before
    public void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");

        testApexModel = new TestApexModel<AxContextModel>(AxContextModel.class, new TestApexContextModelCreator());
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

    private static final String VALID_MODEL_STRING =
            "***validation of model successful***";

    private static final String OBSERVATION_MODEL_STRING = "\n" +
            "***observations noted during validation of model***\n" +
            "AxArtifactKey:(name=contextAlbum1,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:OBSERVATION:description is blank\n" +
            "********************************";

    private static final String WARNING_MODEL_STRING = "\n" +
            "***warnings issued during validation of model***\n" +
            "AxArtifactKey:(name=contextAlbum1,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo:WARNING:UUID is a zero UUID: 00000000-0000-0000-0000-000000000000\n" +
            "********************************";

    private static final String INVALID_MODEL_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=StringType,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextSchema:INVALID:no schemaDefinition specified, schemaDefinition may not be blank\n" +
            "AxArtifactKey:(name=contextAlbum0,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum:INVALID:scope is not defined\n" +
            "********************************";

    private static final String INVALID_MODEL_MALSTRUCTURED_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=ContextModel,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextModel:INVALID:key information not found for key AxArtifactKey:(name=contextAlbum1,version=0.0.2)\n" +
            "AxArtifactKey:(name=contextAlbum1,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextModel:WARNING:key not found for key information entry\n" +
            "AxArtifactKey:(name=ContextSchemas,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas:INVALID:key on schemas entry AxArtifactKey:(name=MapType,version=0.0.1) does not equal entry key AxArtifactKey:(name=MapType,version=0.0.2)\n" +
            "AxArtifactKey:(name=contextAlbums,version=0.0.1):com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums:INVALID:key on context album entry key AxArtifactKey:(name=contextAlbum1,version=0.0.1) does not equal context album value key AxArtifactKey:(name=contextAlbum1,version=0.0.2)\n" +
            "********************************";

}
