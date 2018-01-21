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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelWriter {

    @Test
    public void testModelWriter() throws IOException, ApexException {
        AxModel model = new TestApexBasicModelCreator().getModel();
        
        ApexModelWriter<AxModel> modelWriter = new ApexModelWriter<AxModel>(AxModel.class);
        
        modelWriter.setValidateFlag(true);
        assertTrue(modelWriter.getValidateFlag());
        assertEquals(0, modelWriter.getCDataFieldSet().size());
        
        assertFalse(modelWriter.isJsonOutput());
        modelWriter.setJsonOutput(true);
        assertTrue(modelWriter.isJsonOutput());
        modelWriter.setJsonOutput(false);
        assertFalse(modelWriter.isJsonOutput());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        modelWriter.write(model, baos);
        modelWriter.setJsonOutput(true);
        modelWriter.write(model, baos);
        modelWriter.setJsonOutput(false);
        
        modelWriter.setValidateFlag(false);
        modelWriter.write(model, baos);
        modelWriter.setJsonOutput(true);
        modelWriter.write(model, baos);
        modelWriter.setJsonOutput(false);
        
        modelWriter.setValidateFlag(true);
        model.getKeyInformation().getKeyInfoMap().clear();
        try {
            modelWriter.write(model, baos);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Apex concept xml (BasicModel:0.0.1) validation failed", e.getMessage());
        }
        model.getKeyInformation().generateKeyInfo(model);

        try {
            modelWriter.write(null, baos);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("concept may not be null", e.getMessage());
        }
        
        try {
            ByteArrayOutputStream nullBaos = null;
            modelWriter.write(model, nullBaos);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("concept stream may not be null", e.getMessage());
        }
    }
}
