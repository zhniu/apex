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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class DirectoryUtilsTest {

    @Test
    public void test() throws IOException {
        DirectoryUtils.emptyDirectory(new File("/i/dont/exist"));
        
        File tempDir = Files.createTempDirectory("test").toFile();

        Files.createTempDirectory(tempDir.toPath(), "testsubprefix");

        TextFileUtils.putStringAsTextFile("Temp File 0 contents", tempDir.getAbsolutePath() + "/tempFile0.tmp");
        TextFileUtils.putStringAsTextFile("Temp File 1 contents", tempDir.getAbsolutePath() + "/tempFile1.tmp");
        
        DirectoryUtils.emptyDirectory(tempDir);
        
        DirectoryUtils.getLocalTempDirectory(null);
        
        byte[] byteArray = new byte[] {0, 0, 0};
        DirectoryUtils.getLocalTempDirectory(Arrays.toString(byteArray));
    }

}
