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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelReader {

    @Test
    public void testModelReader() throws IOException, ApexException {
        AxModel model = new TestApexBasicModelCreator().getModel();
        AxModel invalidModel = new TestApexBasicModelCreator().getInvalidModel();
        
        ApexModelWriter<AxModel> modelWriter = new ApexModelWriter<AxModel>(AxModel.class);
        modelWriter.setValidateFlag(true);
        modelWriter.setJsonOutput(true);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        modelWriter.write(model, baos);
        
        ByteArrayOutputStream baosInvalid = new ByteArrayOutputStream();
        modelWriter.setValidateFlag(false);
        modelWriter.write(invalidModel, baosInvalid);
        
        ApexModelReader<AxModel> modelReader = new ApexModelReader<AxModel>(AxModel.class, true);
        
        modelReader.setValidateFlag(true);
        assertTrue(modelReader.getValidateFlag());
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        AxModel readModel = modelReader.read(bais);
        assertEquals(model, readModel);
        
        ByteArrayInputStream baisInvalid = new ByteArrayInputStream(baosInvalid.toByteArray());
        try {
            modelReader.read(baisInvalid);
            fail("test should throw an exceptino here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Apex concept validation failed"));
        }
        
        modelReader.setValidateFlag(false);
        assertFalse(modelReader.getValidateFlag());
        
        ByteArrayInputStream bais2 = new ByteArrayInputStream(baos.toByteArray());
        AxModel readModel2 = modelReader.read(bais2);
        assertEquals(model, readModel2);
        
        modelWriter.setJsonOutput(false);
        
        ByteArrayOutputStream baosXML = new ByteArrayOutputStream();
        modelWriter.write(model, baosXML);
        
        ByteArrayInputStream baisXML = new ByteArrayInputStream(baosXML.toByteArray());
        AxModel readModelXML = modelReader.read(baisXML);
        assertEquals(model, readModelXML);
        
        String dummyString = "SomeDummyText";
        ByteArrayInputStream baisDummy = new ByteArrayInputStream(dummyString.getBytes());
        try {
            modelReader.read(baisDummy);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("format of input for Apex concept is neither JSON nor XML", e.getMessage());
        }
        
        try {
            ByteArrayInputStream nullBais = null;
            modelReader.read(nullBais);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("concept stream may not be null", e.getMessage());
        }
        
        try {
            FileInputStream fis = new FileInputStream(new File("somewhere/over/the/rainbow"));
            modelReader.read(fis);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("rainbow"));
        }
        
        File tempFile = File.createTempFile("Apex", "Dummy");
        try {
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            br.close();
            modelReader.read(br);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Unable to read Apex concept ", e.getMessage());
        }
        finally {
            tempFile.delete();
        }
        
        modelReader.setSchema(null);
        
        tempFile = File.createTempFile("Apex", "Dummy");
        try {
            modelReader.setSchema(tempFile.getCanonicalPath());
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Unable to load schema", e.getMessage());
        }
        finally {
            tempFile.delete();
        }
        
        modelReader.setSchema("xml/example.xsd");
    }
}
