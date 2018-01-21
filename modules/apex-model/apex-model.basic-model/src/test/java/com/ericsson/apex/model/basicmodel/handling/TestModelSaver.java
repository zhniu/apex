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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelSaver {

    @Test
    public void testModelSaver() throws IOException, ApexException {
        AxModel model = new TestApexBasicModelCreator().getModel();
        
        Path tempPath = Files.createTempDirectory("ApexTest");
        
        ApexModelSaver<AxModel> modelSaver = new ApexModelSaver<AxModel>(AxModel.class, model, tempPath.toAbsolutePath().toString());
        
        modelSaver.apexModelWriteXML();
        modelSaver.apexModelWriteJSON();
        
        Files.deleteIfExists(new File(tempPath.toAbsolutePath() + "/BasicModel.json").toPath());
        Files.deleteIfExists(new File(tempPath.toAbsolutePath() + "/BasicModel.xml").toPath());
        Files.deleteIfExists(tempPath);
    }
}
