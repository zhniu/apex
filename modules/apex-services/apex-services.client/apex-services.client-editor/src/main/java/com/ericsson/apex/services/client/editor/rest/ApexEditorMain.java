/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

import java.io.PrintStream;

/**
 * This class is the main class that is used to launch the Apex editor from the command line.
 *
 */
public class ApexEditorMain {
    /**
     * The Enum EditorState holds the current state of the editor.
     */
    // Editor state
    public enum EditorState {
        /** The editor is stopped. */
        STOPPED,
        /** The editor is ready to run. */
        READY,
        /** The editor is getting ready to run. */
        INITIALIZING,
        /** The editor is running. */
        RUNNING
    };

    private static final int EDITOR_RNNING_CHECK_TIMEOUT = 1000;

    private EditorState state = EditorState.STOPPED;

    // The Apex editor this class is running
    private ApexEditor apexEditor = null;

    // The parameters for the editor
    private ApexEditorParameters parameters = null;

    // Output and error streams for messages
    private final PrintStream outStream;

    /**
     * Main method, main entry point for command.
     *
     * @param args The command line arguments for the editor
     */
    public static void main(final String[] args) {
        try {
            final ApexEditorMain editorMain = new ApexEditorMain(args, System.out);
            editorMain.init();
        }
        catch (final Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Constructor, kicks off the editor.
     *
     * @param args The command line arguments for the editor
     * @param outStream The stream for output messages
     */
    public ApexEditorMain(final String[] args, final PrintStream outStream) {
        // Save the streams for output and error
        this.outStream = outStream;

        // Editor parameter parsing
        final ApexEditorParameterParser parser = new ApexEditorParameterParser();

        try {
            // Get and check the parameters
            parameters = parser.parse(args);
        }
        catch (final ApexEditorParameterException e) {
            throw new ApexEditorParameterException("Apex Editor REST endpoint (" + this.toString() + ") parameter error, " + e.getMessage() + '\n'
                    + parser.getHelp(ApexEditorMain.class.getCanonicalName()));
        }

        if (parameters.isHelpSet()) {
            throw new ApexEditorParameterException(parser.getHelp(ApexEditorMain.class.getCanonicalName()));
        }

        // Validate the parameters
        final String validationMessage = parameters.validate();
        if (validationMessage.length() > 0) {
            throw new ApexEditorParameterException("Apex Editor REST endpoint (" + this.toString() + ") parameters invalid, " + validationMessage + '\n'
                    + parser.getHelp(ApexEditorMain.class.getCanonicalName()));
        }

        state = EditorState.READY;
    }

    /**
     * Initialize the Apex editor.
     */
    public void init() {
        outStream.println("Apex Editor REST endpoint (" + this.toString() + ") starting at " + parameters.getBaseURI().toString() + " . . .");

        try {
            state = EditorState.INITIALIZING;

            // Start the editor
            apexEditor = new ApexEditor(parameters);

            // Add a shutdown hook to shut down the editor when the process is exiting
            Runtime.getRuntime().addShutdownHook(new Thread(new ApexEditorShutdownHook()));

            state = EditorState.RUNNING;

            if (parameters.getTimeToLive() == ApexEditorParameters.INFINITY_TIME_TO_LIVE) {
                outStream.println("Apex Editor REST endpoint (" + this.toString() + ") started at " + parameters.getBaseURI().toString());
            }
            else {
                outStream.println("Apex Editor REST endpoint (" + this.toString() + ") started");
            }

            // Find out how long is left to wait
            long timeRemaining = parameters.getTimeToLive();
            while (timeRemaining == ApexEditorParameters.INFINITY_TIME_TO_LIVE || timeRemaining > 0) {
                // decrement the time to live in the non-infinity case
                if (timeRemaining > 0) {
                    timeRemaining--;
                }

                // Wait for a second
                Thread.sleep(EDITOR_RNNING_CHECK_TIMEOUT);
            }
        }
        catch (final Exception e) {
            outStream.println("Apex Editor REST endpoint (" + this.toString() + ") failed at with error: " + e.getMessage());
        }
        finally {
            if (apexEditor != null) {
                apexEditor.shutdown();
                apexEditor = null;
            }
            state = EditorState.STOPPED;
        }
    }

    /**
     * Get the editor state.
     *
     * @return the state
     */
    public EditorState getState() {
        return state;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder ret = new StringBuilder();
        ret.append(this.getClass().getSimpleName()).append(": Config=[").append(parameters).append("], State=").append(this.getState());
        return ret.toString();
    }

    /**
     * Explicitly shut down the editor.
     */
    public void shutdown() {
        if (apexEditor != null) {
            outStream.println("Apex Editor REST endpoint (" + this.toString() + ") shutting down");
            apexEditor.shutdown();
        }
        state = EditorState.STOPPED;
        outStream.println("Apex Editor REST endpoint (" + this.toString() + ") shut down");
    }

    /**
     * This class is a shutdown hook for the Apex editor command.
     */
    private class ApexEditorShutdownHook implements Runnable {
        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            if (apexEditor != null) {
                apexEditor.shutdown();
            }
        }
    }
}
