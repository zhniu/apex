/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * The Class TestCLIEditorEventsContext.
 */
public class TestCLIEditorEventsContext {
    //CHECKSTYLE:OFF: MagicNumber

    /**
     * Test java context model.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if an Apex error happens
     */
    @Test
    public void testJavaContextModel() throws IOException, ApexModelException {
        final File tempLogFile = File.createTempFile("TestPolicyJavaEventsAndContext", ".log");
        final File tempModelFile = File.createTempFile("TestPolicyJavaEventsAndContext", ".json");

        final String[] cliArgs = new String[] {"-c", "src/main/resources/examples/scripts/TestPolicyJavaEventContext.apex", "-l",
                tempLogFile.getAbsolutePath(), "-o", tempModelFile.getAbsolutePath() };

        ApexCLIEditorMain cliEditor = new ApexCLIEditorMain(cliArgs);
        assertEquals(0, cliEditor.getErrorCount());

        // Get the model and log into strings
        final String logString = TextFileUtils.getTextFileAsString(tempLogFile.getCanonicalPath());
        final String modelString = TextFileUtils.getTextFileAsString(tempModelFile.getCanonicalPath());

        // As a sanity check, count the number of non white space characters in log and model files
        final int logCharCount = logString.replaceAll("\\s+", "").length();
        final int modelCharCount = modelString.replaceAll("\\s+", "").length();

        assertEquals(24798, logCharCount);
        assertEquals(46084, modelCharCount);

        tempLogFile.delete();
        tempModelFile.delete();
    }

    /**
     * Test avro context model.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if an Apex error happens
     */
    @Test
    public void testAvroContextModel() throws IOException, ApexModelException {
        final File tempLogFile = File.createTempFile("TestPolicyAvroEventsAndContext", ".log");
        final File tempModelFile = File.createTempFile("TestPolicyAvroEventsAndContext", ".json");

        final String[] cliArgs = new String[] {"-c", "src/main/resources/examples/scripts/TestPolicyAvroEventContext.apex", "-l",
                tempLogFile.getAbsolutePath(), "-o", tempModelFile.getAbsolutePath()};

        ApexCLIEditorMain cliEditor = new ApexCLIEditorMain(cliArgs);
        assertEquals(0, cliEditor.getErrorCount());

        // Get the model and log into strings
        final String logString = TextFileUtils.getTextFileAsString(tempLogFile.getCanonicalPath());
        final String modelString = TextFileUtils.getTextFileAsString(tempModelFile.getCanonicalPath());

        // As a sanity check, count the number of non white space characters in log and model files
        final int logCharCount = logString.replaceAll("\\s+", "").length();
        final int modelCharCount = modelString.replaceAll("\\s+", "").length();

        assertEquals(29256, logCharCount);
        assertEquals(52930, modelCharCount);

        tempLogFile.delete();
        tempModelFile.delete();
    }
}
