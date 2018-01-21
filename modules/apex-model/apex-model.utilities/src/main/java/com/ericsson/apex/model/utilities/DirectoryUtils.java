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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This is common utility class with static methods for handling directories. It is an abstract class to prevent any direct instantiation and private
 * constructor to prevent extending this class.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class DirectoryUtils {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(DirectoryUtils.class);

    /**
     * Private constructor used to prevent sub class instantiation.
     */
    private DirectoryUtils() {
    }

    /**
     * Method to get an empty temporary directory in the system temporary directory on the local machine that will be deleted on (normal) shutdown.
     *
     * @param nameprefix The prefix of the filename. System.nanoTime() will be appended to the pattern to create a unique file pattern
     * @return The temporary directory
     */
    public static File getLocalTempDirectory(final String nameprefix) {
        try {
            // Get the name of the temporary directory
            final String tempDirName = System.getProperty("java.io.tmpdir") + "/" + nameprefix + System.nanoTime();
            final File tempDir = new File(tempDirName);

            // Delete the directory if it already exists
            if (tempDir.exists()) {
                return null;
            }

            // Make the directory
            tempDir.mkdirs();

            // Add a shutdown hook that deletes the directory contents when the JVM closes
            Runtime.getRuntime().addShutdownHook(new DirectoryDeleteShutdownHook(tempDir));

            LOGGER.trace("creating temp directory\"{}\" : ", tempDir.getAbsolutePath());
            return tempDir;
        }
        catch (final Exception e) {
            LOGGER.debug("error creating temp directory\"{}\" : " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Method to recursively delete all the files in a directory.
     *
     * @param tempDir the directory to empty
     * @return true if the operation succeeds, false otherwise
     */
    public static boolean emptyDirectory(final File tempDir) {
        // Sanity check
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            return false;
        }

        // Walk the directory structure deleting files as we go
        final File[] files = tempDir.listFiles();
        if (files != null) {
            for (final File directoryFile : files) {
                // Check if this is a directory itself
                if (directoryFile.isDirectory()) {
                    // Recurse into the sub directory and empty it
                    emptyDirectory(directoryFile);
                }

                // Delete the directory entry
                directoryFile.delete();
            }
        }

        return true;
    }
}

/**
 * The Class DirectoryShutdownHook removes the contents of a directory and the directory itself at shutdown.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
final class DirectoryDeleteShutdownHook extends Thread {
    // The directory we are acting on
    private final File tempDir;

    /**
     * Constructor that defines the directory to act on at shutdown.
     *
     * @param tempDir The temporary directory to delete
     */
    DirectoryDeleteShutdownHook(final File tempDir) {
        this.tempDir = tempDir;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if (tempDir.exists()) {
            // Empty and delete the directory
            DirectoryUtils.emptyDirectory(tempDir);
            tempDir.delete();
        }
    }
}
