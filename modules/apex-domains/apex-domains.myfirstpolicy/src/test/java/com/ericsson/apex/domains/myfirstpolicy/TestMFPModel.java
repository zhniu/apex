/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.myfirstpolicy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.test.TestApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * Test MyFirstPolicy Model.
 * 
 * @author John Keeney (john.keeney@ericsson.com)
 */
public class TestMFPModel {

    private static Connection connection;
    private static TestApexModel<AxPolicyModel> testApexModel1;
    private static TestApexModel<AxPolicyModel> testApexModel2;

    /**
     * Setup.
     * 
     * @throws Exception if there is an error
     */
    @BeforeClass
    public static void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");
        testApexModel1 = new TestApexModel<>(AxPolicyModel.class, new TestMFPModelCreator.TestMFP1ModelCreator());
        testApexModel2 = new TestApexModel<>(AxPolicyModel.class, new TestMFPModelCreator.TestMFP2ModelCreator());
    }

    /**
     * Teardown.
     * 
     * @throws Exception if there is an error
     */
    @AfterClass
    public static void teardown() throws Exception {
        connection.close();
        new File("derby.log").delete();
    }

    /**
     * Test model is valid.
     * 
     * @throws Exception if there is an error
     */
    @Test
    public void testModelValid() throws Exception {
        AxValidationResult result = testApexModel1.testApexModelValid();
        assertTrue("Model did not validate cleanly", result.isOK());

        result = testApexModel2.testApexModelValid();
        assertTrue("Model did not validate cleanly", result.isOK());
    }

    /**
     * Test model write and read XML.
     * 
     * @throws Exception if there is an error
     */
    @Test
    public void testModelWriteReadXML() throws Exception {
        testApexModel1.testApexModelWriteReadXML();
        testApexModel2.testApexModelWriteReadXML();
    }

    /**
     * Test model write and read JSON.
     * 
     * @throws Exception if there is an error
     */
    @Test
    public void testModelWriteReadJSON() throws Exception {
        testApexModel1.testApexModelWriteReadJSON();
        testApexModel2.testApexModelWriteReadJSON();
    }
}
