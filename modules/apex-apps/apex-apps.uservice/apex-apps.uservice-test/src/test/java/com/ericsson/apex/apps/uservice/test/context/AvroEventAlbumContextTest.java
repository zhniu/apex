/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.context;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.auth.clieditor.ApexCLIEditorMain;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.model.utilities.TextFileUtils;
import com.ericsson.apex.service.engine.main.ApexMain;

/**
 * The Class AvroEventAlbumContextTest.
 */
public class AvroEventAlbumContextTest {
    
    /**
     * Test avro event fields, by starting an engine, send event in, test event out.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexException the apex exception
     */
    @Test
    public void testAvroEventAlbumContextTest() throws IOException, ApexException {
        File tempCommandFile = File.createTempFile("TestPolicyAvroEventContext", ".apex");
        File tempLogFile     = File.createTempFile("TestPolicyAvroEventContext", ".log");
        File tempModelFile   = File.createTempFile("TestPolicyAvroEventContext", ".json");
        
        String javaEventContextString = ResourceUtils.getResourceAsString("examples/scripts/TestPolicyAvroEventContext.apex");
        TextFileUtils.putStringAsFile(javaEventContextString, tempCommandFile);
        
        String[] cliArgs = new String[]{
                "-c",
                tempCommandFile.getCanonicalPath(),
                "-l",
                tempLogFile.getAbsolutePath(),
                "-o",
                tempModelFile.getAbsolutePath()
        };

        ModelService.clear();

        new ApexCLIEditorMain(cliArgs);

        tempCommandFile.delete();
        tempLogFile    .delete();

        ModelService.clear();

        ApexMain apexMain = new ApexMain(new String[]{"-m", tempModelFile.getAbsolutePath(), "-c", "src/test/resources/prodcons/Context_AvroEventAlbum_file2file.json"});
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();

        // The output event is in this file
        File outputEventFile = new File("src/test/resources/events/Context_AvroEventAlbum_EventOut.json");
        String outputEventString = TextFileUtils.getTextFileAsString(outputEventFile.getCanonicalPath()).replaceAll("\\s+","");

        // We compare the output to what we expect to get
        String outputEventCompareString = TextFileUtils.getTextFileAsString("src/test/resources/events/Context_AvroEventAlbum_EventOutCompare.json").replaceAll("\\s+","");

        // Check what we got is what we expected to get
        assertEquals(outputEventCompareString, outputEventString);
        
        tempModelFile.delete();
        outputEventFile.delete();
    }
}
