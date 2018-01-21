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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelFileWriter {

    @Test
    public void testModelFileWriter() throws IOException, ApexException {
        AxModel model = new TestApexBasicModelCreator().getModel();

        ApexModelFileWriter<AxModel> modelFileWriter = new ApexModelFileWriter<>(true);

        modelFileWriter.setValidateFlag(true);
        assertTrue(modelFileWriter.isValidateFlag());

        File tempFile = File.createTempFile("ApexFileWriterTest", "test");
        File tempDir = tempFile.getParentFile();
        tempFile.delete();
        
        File jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/ApexFileWriterTest.json");
        File xmlTempFile = new File(tempDir.getAbsolutePath() + "/ccc/ApexFileWriterTest.xml");
        
        modelFileWriter.apexModelWriteJSONFile(model, AxModel.class, jsonTempFile.getAbsolutePath());

        modelFileWriter.apexModelWriteXMLFile(model, AxModel.class, xmlTempFile.getAbsolutePath());
        
        jsonTempFile.delete();
        xmlTempFile.delete();
        new File(tempDir.getAbsolutePath() + "/aaa").delete();
        new File(tempDir.getAbsolutePath() + "/ccc").delete();
       
        jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/bbb/ApexFileWriterTest.json");
        xmlTempFile = new File(tempDir.getAbsolutePath() + "/ccc/ddd/ApexFileWriterTest.xml");
        
        modelFileWriter.apexModelWriteJSONFile(model, AxModel.class, jsonTempFile.getAbsolutePath());
        modelFileWriter.apexModelWriteXMLFile(model, AxModel.class, xmlTempFile.getAbsolutePath());
                
        jsonTempFile.delete();
        xmlTempFile.delete();

        new File(tempDir.getAbsolutePath() + "/aaa/bbb").delete();
        new File(tempDir.getAbsolutePath() + "/aaa").delete();
        new File(tempDir.getAbsolutePath() + "/ccc/ddd").delete();
        new File(tempDir.getAbsolutePath() + "/ccc").delete();
        
        File dirA = new File(tempDir.getAbsolutePath() + "/aaa");
        //File dirB = new File(tempDir.getAbsolutePath() + "/aaa/bbb");
        dirA.createNewFile();
        //dirB.createNewFile();
        
        jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/bbb/ApexFileWriterTest.json");
        jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/bbb/ApexFileWriterTest.xml");

        try {
            modelFileWriter.apexModelWriteJSONFile(model, AxModel.class, jsonTempFile.getAbsolutePath());
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("could not create directory "));
        }

        try {
            modelFileWriter.apexModelWriteXMLFile(model, AxModel.class, jsonTempFile.getAbsolutePath());
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("could not create directory "));
        }

        dirA.delete();

        dirA = new File(tempDir.getAbsolutePath() + "/aaa");
        File fileB = new File(tempDir.getAbsolutePath() + "/aaa/bbb");
        dirA.mkdir();
        fileB.createNewFile();
        
        jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/bbb/ApexFileWriterTest.json");
        jsonTempFile = new File(tempDir.getAbsolutePath() + "/aaa/bbb/ApexFileWriterTest.xml");

        try {
            modelFileWriter.apexModelWriteJSONFile(model, AxModel.class, jsonTempFile.getAbsolutePath());
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("error processing file "));
        }

        try {
            modelFileWriter.apexModelWriteXMLFile(model, AxModel.class, jsonTempFile.getAbsolutePath());
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().contains("error processing file "));
        }

        fileB.delete();
        dirA.delete();
    }
}
