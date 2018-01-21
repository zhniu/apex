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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.auth.clieditor.ApexCLIEditorMain;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * Test MyFirstPolicyModel CLI.
 */
public class TestMFPModelCLI {
    private static AxPolicyModel testApexModel1;
    private static AxPolicyModel testApexModel2;

    /**
     * Setup the test.
     * @throws Exception if there is an error
     */
    @BeforeClass
    public static void setup() throws Exception {
        testApexModel1 = new TestMFPModelCreator.TestMFP1ModelCreator().getModel();
        testApexModel2 = new TestMFPModelCreator.TestMFP2ModelCreator().getModel();
    }

    /**
     * Test CLI policy.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException ifd there is an Apex Error
     */
    @Test
    public void testCLIPolicy() throws IOException, ApexModelException {

        final File tempLogFile1 = File.createTempFile("TestMyFirstPolicy1CLI", ".log");
        final File tempModelFile1 = File.createTempFile("TestMyFirstPolicy1CLI", ".json");
        final File tempLogFile2 = File.createTempFile("TestMyFirstPolicy2CLI", ".log");
        final File tempModelFile2 = File.createTempFile("TestMyFirstPolicy2CLI", ".json");
        final String[] testApexModel1CliArgs = {"-c", "src/main/resources/examples/models/MyFirstPolicy/1/MyFirstPolicyModel_0.0.1.apex", "-l",
                tempLogFile1.getAbsolutePath(), "-o", tempModelFile1.getAbsolutePath() };
        final String[] testApexModel2CliArgs = {"-c", "src/main/resources/examples/models/MyFirstPolicy/2/MyFirstPolicyModel_0.0.1.apex", "-l",
                tempLogFile2.getAbsolutePath(), "-o", tempModelFile2.getAbsolutePath() };

        new ApexCLIEditorMain(testApexModel1CliArgs);
        new ApexCLIEditorMain(testApexModel2CliArgs);

        final ApexModelReader<AxPolicyModel> reader = new ApexModelReader<>(AxPolicyModel.class);
        AxPolicyModel generatedmodel = reader.read(TextFileUtils.getTextFileAsString(tempModelFile1.getAbsolutePath()));

        assertEquals("Model generated from the CLI (" + testApexModel1CliArgs[1] + ") into file " + tempModelFile1.getAbsolutePath()
                + " is not the same as the test Model for " + testApexModel1.getKey(), testApexModel1, generatedmodel);

        generatedmodel = reader.read(TextFileUtils.getTextFileAsString(tempModelFile2.getAbsolutePath()));
        assertEquals("Model generated from the CLI (" + testApexModel2CliArgs[1] + ") into file " + tempModelFile2.getAbsolutePath()
                + " is not the same as the test Model for " + testApexModel2.getKey(), testApexModel2, generatedmodel);

        tempLogFile1.delete();
        tempModelFile1.delete();

        tempLogFile2.delete();
        tempModelFile2.delete();

    }
}
