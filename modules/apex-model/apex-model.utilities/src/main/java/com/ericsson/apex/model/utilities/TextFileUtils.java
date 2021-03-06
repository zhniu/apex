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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The Class TextFileUtils is class that provides useful functions for handling text files. Functions to read and wrtie text files to strings and strings are
 * provided.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class TextFileUtils {
    private static final int READER_CHAR_BUFFER_SIZE_4096 = 4096;

    private TextFileUtils() {
    		// This class cannot be initialized
    }
    
    /**
     * Method to return the contents of a text file as a string.
     *
     * @param textFilePath The path to the file as a string
     * @return A string containing the contents of the file
     * @throws IOException on errors reading text from the file
     */
    public static String getTextFileAsString(final String textFilePath) throws IOException {
        final File textFile = new File(textFilePath);
        final FileInputStream textFileInputStream = new FileInputStream(textFile);
        final byte[] textData = new byte[(int) textFile.length()];
        textFileInputStream.read(textData);
        textFileInputStream.close();
        return new String(textData);
    }

    /**
     * Method to write contents of a string to a text file.
     *
     * @param outString The string to write
     * @param textFilePath The path to the file as a string
     * @throws IOException on errors reading text from the file
     */
    public static void putStringAsTextFile(final String outString, final String textFilePath) throws IOException {
        final File textFile = new File(textFilePath);
        putStringAsFile(outString, textFile);
    }

    /**
     * Method to write contents of a string to a text file.
     *
     * @param outString The string to write
     * @param textFile The file to write the string to
     * @throws IOException on errors reading text from the file
     */
    public static void putStringAsFile(final String outString, final File textFile) throws IOException {
        final FileOutputStream textFileOutputStream = new FileOutputStream(textFile);
        textFileOutputStream.write(outString.getBytes());
        textFileOutputStream.close();
    }

    /**
     * Method to return the contents of a text steam as a string.
     *
     * @param textStream The stream
     * @return A string containing the output of the stream as text
     * @throws IOException on errors reading text from the file
     */
    public static String getStreamAsString(final InputStream textStream) throws IOException {
        return getReaderAsString(new BufferedReader(new InputStreamReader(textStream)));
    }

    /**
     * Method to return the contents of a reader steam as a string. This closes the reader after use
     *
     * @param textReader The reader
     * @return A string containing the output of the reader as text
     * @throws IOException on errors reading text from the file
     */
    public static String getReaderAsString(final BufferedReader textReader) throws IOException {

        final StringBuilder builder = new StringBuilder();
        int charsRead = -1;
        final char[] chars = new char[READER_CHAR_BUFFER_SIZE_4096];
        do {
            charsRead = textReader.read(chars, 0, chars.length);
            if (charsRead > 0) {
                builder.append(chars, 0, charsRead);
            }
        }
        while (charsRead > 0);
        return builder.toString();
    }
}
