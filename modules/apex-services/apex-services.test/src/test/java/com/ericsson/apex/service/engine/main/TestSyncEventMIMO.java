/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.main;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.core.infrastructure.messaging.MessagingException;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.utilities.TextFileUtils;

public class TestSyncEventMIMO {

    @Test
    public void testJsonFileAsyncMIMO() throws MessagingException, ApexException, IOException {
        String[] args = {
                "-c",
                "src/test/resources/parameters/File2FileJsonEventSyncMIMO.json"
        };
        String[] outFilePaths = {
                "src/test/resources/events/EventsOutMulti0.json",
                "src/test/resources/events/EventsOutMulti1.json",
                "src/test/resources/events/EventsOutMulti2.json"
        };

        testFileEvents(args, outFilePaths, 48656*3);
    }

    private void testFileEvents(final String[] args, final String[] outFilePaths, final long expectedFileSize) throws MessagingException, ApexException, IOException {
        ApexMain apexMain = new ApexMain(args);

        File outFile0  = new File(outFilePaths[0]);
        File outFile1  = new File(outFilePaths[1]);
        File outFile2  = new File(outFilePaths[2]);

        while (!outFile0.exists() && !outFile1.exists() && !outFile2.exists()) {
            ThreadUtilities.sleep(500);
        }

        // Wait for the files to be filled
        long outFile0Size = 0;
        for (int i = 0; i < 10; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePaths[0]).replaceAll("\\s+","");
            outFile0Size = fileString.length();
            if (outFile0Size > 0 && outFile0Size >= expectedFileSize) {
                break;
            }
            ThreadUtilities.sleep(500);
        }

        long outFile1Size = 0;
        for (int i = 0; i < 10; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePaths[1]).replaceAll("\\s+","");
            outFile1Size = fileString.length();
            if (outFile1Size > 0 && outFile1Size >= expectedFileSize) {
                break;
            }
            ThreadUtilities.sleep(500);
            outFile1Size = fileString.length();
        }

        long outFile2Size = 0;
        for (int i = 0; i < 10; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePaths[2]).replaceAll("\\s+","");
            outFile2Size = fileString.length();
            if (outFile2Size > 0 && outFile2Size >= expectedFileSize) {
                break;
            }
            outFile2Size = fileString.length();
            ThreadUtilities.sleep(500);
        }

        apexMain.shutdown();
        outFile0.delete();
        outFile1.delete();
        outFile2.delete();

        assertEquals(outFile0Size, expectedFileSize);
        assertEquals(outFile1Size, expectedFileSize);
        assertEquals(outFile2Size, expectedFileSize);
    }
}
