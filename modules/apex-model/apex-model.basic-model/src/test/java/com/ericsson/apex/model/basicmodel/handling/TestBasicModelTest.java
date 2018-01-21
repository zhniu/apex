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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.test.TestApexModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestBasicModelTest {

    @Test
    public void testNormalModelCreator() throws ApexException {
        TestApexModel<AxModel> testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexBasicModelCreator());

        testApexModel.testApexModelValid();
        try {
            testApexModel.testApexModelVaidateObservation();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("model should have observations", e.getMessage());
        }
        testApexModel.testApexModelVaidateWarning();
        testApexModel.testApexModelVaidateInvalidModel();
        testApexModel.testApexModelVaidateMalstructured();

        testApexModel.testApexModelWriteReadJSON();
        testApexModel.testApexModelWriteReadXML();
    }

    @Test
    public void testModelCreator0() throws ApexException {
        TestApexModel<AxModel> testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexTestModelCreator0());

        testApexModel.testApexModelValid();
        try {
            testApexModel.testApexModelVaidateObservation();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("model should have observations", e.getMessage());
        }
        try {
            testApexModel.testApexModelVaidateWarning();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("model should have warnings", e.getMessage());
        }
        try {
            testApexModel.testApexModelVaidateInvalidModel();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("should not be valid ***validation of model successful***", e.getMessage());
        }
        try {
            testApexModel.testApexModelVaidateMalstructured();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("should not be valid ***validation of model successful***", e.getMessage());
        }
    }

    @Test
    public void testModelCreator1() throws ApexException {
        TestApexModel<AxModel> testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexTestModelCreator1());

        try {
            testApexModel.testApexModelValid();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("model is invalid"));
        }
        try {
            testApexModel.testApexModelVaidateObservation();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("model is invalid"));
        }
        try {
            testApexModel.testApexModelVaidateWarning();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("model is invalid"));
        }
        testApexModel.testApexModelVaidateInvalidModel();
        testApexModel.testApexModelVaidateMalstructured();
    }
    
    @Test
    public void testModelCreator2() throws ApexException {
        TestApexModel<AxModel> testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexTestModelCreator2());

        testApexModel.testApexModelValid();
        testApexModel.testApexModelVaidateObservation();
        try {
            testApexModel.testApexModelVaidateWarning();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertEquals("model should have warnings", e.getMessage());
        }
    }
    
    @Test
    public void testModelCreator1XMLJSON() throws ApexException {
        TestApexModel<AxModel> testApexModel = new TestApexModel<AxModel>(AxModel.class, new TestApexTestModelCreator1());

        try {
            testApexModel.testApexModelWriteReadJSON();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("error processing file"));
        }

        try {
            testApexModel.testApexModelWriteReadXML();
            fail("Test should throw an exception");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("error processing file"));
        }
    }
}
