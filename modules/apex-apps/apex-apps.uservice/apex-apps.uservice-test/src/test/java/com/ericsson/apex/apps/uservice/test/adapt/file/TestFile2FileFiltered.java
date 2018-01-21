/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.utilities.TextFileUtils;
import com.ericsson.apex.service.engine.main.ApexMain;

public class TestFile2FileFiltered {

    @Test
    public void testJsonFilteredFileInOutEvents() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2FileFilteredInOutJsonEvent.json"};

        String[] outFilePaths =  {
                "src/test/resources/events/Events0004Out.json",
                "src/test/resources/events/Events0104Out.json"
        };

        long[] expectedFileSizes = {
                25790,
                22866
        };

        testFilteredFileEvents(args, outFilePaths, expectedFileSizes);
    }

    @Test
    public void testJsonFilteredFileOutEvents() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2FileFilteredOutJsonEvent.json"};

        String[] outFilePaths =  {
                "src/test/resources/events/Events0004Out.json",
                "src/test/resources/events/Events0104Out.json"
        };

        long[] expectedFileSizes = {
                25790,
                22866
        };

        testFilteredFileEvents(args, outFilePaths, expectedFileSizes);
    }

    @Test
    public void testJsonFilteredFileInEvents() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2FileFilteredInJsonEvent.json"};

        String[] outFilePaths =  {
                "src/test/resources/events/Events0004Out.json"
        };

        long[] expectedFileSizes = {
                25790
        };

        testFilteredFileEvents(args, outFilePaths, expectedFileSizes);
    }

    private void testFilteredFileEvents(String[] args, final String[] outFilePaths, final long[] expectedFileSizes) throws MessagingException, ApexException, IOException {
        ApexMain apexMain = new ApexMain(args);

        File outFile0  = new File(outFilePaths[0]);

        while (!outFile0.exists()) {
            ThreadUtilities.sleep(500);
        }

        // Wait for the file to be filled
        long outFile0Size = 0;
        for (int i = 0; i < 4; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePaths[0]).replaceAll("\\s+","");
            outFile0Size = fileString.length();
            if (outFile0Size > 0 && outFile0Size >= expectedFileSizes[0]) {
                break;
            }
            ThreadUtilities.sleep(500);
        }

        ThreadUtilities.sleep(500);
        apexMain.shutdown();

        long[] actualFileSizes = new long[expectedFileSizes.length];

        for (int i = 0; i < outFilePaths.length; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePaths[i]).replaceAll("\\s+","");
            actualFileSizes[i] = fileString.length();
            new File(outFilePaths[i]).delete();
        }

        for (int i = 0; i < actualFileSizes.length; i++) {
            assertEquals(actualFileSizes[i], expectedFileSizes[i]);
        }
    }
}
