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

public class TestFile2File {

    @Test
    public void testJsonFileEvents() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2FileJsonEvent.json"};

        testFileEvents(args, "src/test/resources/events/EventsOut.json", 48656);
    }

    @Test
    public void testXMLFileEvents() throws MessagingException, ApexException, IOException {
        String[] args = {"src/test/resources/prodcons/File2FileXmlEvent.json"};

        testFileEvents(args, "src/test/resources/events/EventsOut.xmlfile", 105239);
    }

    private void testFileEvents(String[] args, final String outFilePath, final long expectedFileSize) throws MessagingException, ApexException, IOException {
        ApexMain apexMain = new ApexMain(args);

        File outFile  = new File(outFilePath);

        while (!outFile.exists()) {
            ThreadUtilities.sleep(500);
        }

        // Wait for the file to be filled
        long outFileSize = 0;
        while (true) {
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


