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

public class JavaEventAlbumContextTest {
    @Test
    public void testJavaEventAlbumContextTest() throws IOException, ApexException {
        File tempCommandFile = File.createTempFile("TestPolicyJavaEventContext", ".apex");
        File tempLogFile     = File.createTempFile("TestPolicyJavaEventContext", ".log");
        File tempModelFile   = File.createTempFile("TestPolicyJavaEventContext", ".json");
        
        String javaEventContextString = ResourceUtils.getResourceAsString("examples/scripts/TestPolicyJavaEventContext.apex");
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

        ApexMain apexMain = new ApexMain(new String[]{"-m", tempModelFile.getAbsolutePath(), "-c", "src/test/resources/prodcons/Context_JavaEventAlbum_file2file.json"});
        ThreadUtilities.sleep(1000);
        apexMain.shutdown();
        
        // The output event is in this file
        File outputEventFile = new File("src/test/resources/events/Context_JavaEventAlbum_EventOut.json");
        String outputEventString = TextFileUtils.getTextFileAsString(outputEventFile.getCanonicalPath()).replaceAll("\\s+","");

        // We compare the output to what we expect to get
        String outputEventCompareString = TextFileUtils.getTextFileAsString("src/test/resources/events/Context_JavaEventAlbum_EventOutCompare.json").replaceAll("\\s+","");

        // Check what we got is what we expected to get
        assertEquals(outputEventCompareString, outputEventString);
        
        tempModelFile.delete();
        outputEventFile.delete();
    }
}
