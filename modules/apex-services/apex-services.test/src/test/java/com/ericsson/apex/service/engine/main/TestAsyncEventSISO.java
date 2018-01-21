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

public class TestAsyncEventSISO {

    @Test
    public void testJsonFileAsyncSISO() throws MessagingException, ApexException, IOException {
        String[] args = {"-c", "src/test/resources/parameters/File2FileJsonEventAsyncSISO.json"};

        testFileEvents(args, "src/test/resources/events/EventsOutSingle.json", 48656);
    }

    private void testFileEvents(String[] args, final String outFilePath, final long expectedFileSize) throws MessagingException, ApexException, IOException {
        ApexMain apexMain = new ApexMain(args);

        File outFile  = new File(outFilePath);

        while (!outFile.exists()) {
            ThreadUtilities.sleep(500);
        }

        // Wait for the file to be filled
        long outFileSize = 0;
        for (int i = 0; i < 10; i++) {
            String fileString = TextFileUtils.getTextFileAsString(outFilePath).replaceAll("\\s+","");
            outFileSize = fileString.length();
            if (outFileSize > 0 && outFileSize >= expectedFileSize) {
                break;
            }
            ThreadUtilities.sleep(500);
        }

        apexMain.shutdown();
        outFile.delete();
        assertEquals(outFileSize, expectedFileSize);
    }
}
