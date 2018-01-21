/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Standard output file handling and tests.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class OutputFile {

    /** The output file name. */
    private final String fileName;

    /** The output file name. */
    private final boolean overwrite;

    /**
     * Creates a new object for a given file name.
     *
     * @param fileName the file name
     */
    public OutputFile(final String fileName) {
        this(fileName, false);
    }

    /**
     * Creates a new object for a given file name.
     *
     * @param fileName the file name
     * @param overwrite if the file already exists, can it be overwritten, or should an error be raised
     */
    public OutputFile(final String fileName, final boolean overwrite) {
        Validate.notBlank(fileName);
        this.fileName = fileName;
        this.overwrite = overwrite;
    }

    /**
     * Get a File object for this output file.
     *
     * @return a File object for this output file
     */
    public File toFile() {
        final Path fp = FileSystems.getDefault().getPath(fileName);
        return fp.toFile();
    }

    /**
     * Get a Writer object for this output file.
     *
     * @return a Writer object for this output file
     */
    public Writer toWriter() {
        try {
            return new BufferedWriter(new FileWriter(toFile()));
        }
        catch (final IOException e) {
            return null;
        }
    }

    /**
     * Get a OutputStream object for this output file.
     *
     * @return an OutputStream object for this output file
     */
    public OutputStream toOutputStream() {
        try {
            return new FileOutputStream(toFile());
        }
        catch (final IOException e) {
            return null;
        }
    }

    /**
     * Validates the output file. Validation tests for file name being blank, file existing, creation, and finally can-write.
     *
     * @return null on success, an error message on error
     */
    public String validate() {
        if (StringUtils.isBlank(fileName)) {
            return "file name was blank";
        }

        final File file = toFile();
        if (file.exists()) {
            if (!overwrite) {
                return "file already exists";
            }
        }
        else {
            try {
                file.createNewFile();
            }
            catch (final IOException e) {
                return "could not create output file: " + e.getMessage();
                // e.printStackTrace();//TODO
            }
        }

        if (!file.canWrite()) {
            return "cannot write to file";
        }

        return null;
    }
}
