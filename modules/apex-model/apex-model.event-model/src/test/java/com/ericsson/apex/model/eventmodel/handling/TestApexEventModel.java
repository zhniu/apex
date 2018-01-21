/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.handling;

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
import com.ericsson.apex.model.eventmodel.concepts.AxEventModel;

public class TestApexEventModel {
    private Connection connection;
    TestApexModel<AxEventModel> testApexModel;

    @Before
    public void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");

        testApexModel = new TestApexModel<AxEventModel>(AxEventModel.class, new TestApexEventModelCreator());
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

    private static final String VALID_MODEL_STRING = "***validation of model successful***";

    private static final String OBSERVATION_MODEL_STRING = "\n" +
            "***observations noted during validation of model***\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event2,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event2,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "********************************";

    private static final String WARNING_MODEL_STRING = "\n" +
            "***warnings issued during validation of model***\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:WARNING:nameSpace on event is blank\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:WARNING:nameSpace on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event2,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:WARNING:nameSpace on event is blank\n" +
            "AxArtifactKey:(name=event2,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event2,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "********************************";

    private static final String INVALID_MODEL_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation:INVALID:keyInfoMap may not be empty\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=smallEventModel,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=Schemas,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=BigIntType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=BooleanType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=IntType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=MapType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=SetType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=StringType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=smallEventMap,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=event0,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par0)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par2)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par3)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par4)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par5)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=par6)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:duplicate key AxArtifactKey:(name=event1,version=0.0.1) found\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:duplicate key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar) found\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar)\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event0,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:INVALID:parent key on parameter field AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar) does not equal event key\n" +
            "AxArtifactKey:(name=smallEventMap,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvents:INVALID:key on event entry key AxArtifactKey:(name=event2,version=0.0.1) does not equal event value key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:INVALID:parent key on parameter field AxReferenceKey:(parentKeyName=event0,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar) does not equal event key\n" +
            "********************************";

    private static final String INVALID_MODEL_MALSTRUCTURED_STRING = "\n" +
            "***validation of model failed***\n" +
            "AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1):com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation:INVALID:keyInfoMap may not be empty\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=smallEventModel,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=KeyInfoMapKey,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=Schemas,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=SetType,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=smallEventMap,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event1,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:duplicate key AxArtifactKey:(name=event1,version=0.0.1) found\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:duplicate key AxReferenceKey:(parentKeyName=event1,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar) found\n" +
            "AxArtifactKey:(name=smallEventModel,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEventModel:INVALID:key information not found for parent key of key AxReferenceKey:(parentKeyName=event1,parentKeyVersion=0.0.1,parentLocalName=NULL,localName=theOnlyPar)\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "AxArtifactKey:(name=smallEventMap,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvents:INVALID:key on event entry key AxArtifactKey:(name=event2,version=0.0.1) does not equal event value key AxArtifactKey:(name=event1,version=0.0.1)\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:source on event is blank\n" +
            "AxArtifactKey:(name=event1,version=0.0.1):com.ericsson.apex.model.eventmodel.concepts.AxEvent:OBSERVATION:target on event is blank\n" +
            "********************************";
}
