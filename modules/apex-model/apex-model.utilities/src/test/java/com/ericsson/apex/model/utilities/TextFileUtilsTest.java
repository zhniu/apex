/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TextFileUtilsTest {

    @Test
    public void test() throws IOException {
        File tempTextFile = File.createTempFile("Test", "txt");
        
        TextFileUtils.putStringAsTextFile("This is the contents of a text file", tempTextFile.getAbsolutePath());
        
        String textFileString0 = TextFileUtils.getTextFileAsString(tempTextFile.getAbsolutePath());
        assertEquals("This is the contents of a text file", textFileString0);
        
        FileInputStream fis = new FileInputStream(tempTextFile);
        String textFileString1 = TextFileUtils.getStreamAsString(fis);
        assertEquals(textFileString0, textFileString1);
        
    }

}
