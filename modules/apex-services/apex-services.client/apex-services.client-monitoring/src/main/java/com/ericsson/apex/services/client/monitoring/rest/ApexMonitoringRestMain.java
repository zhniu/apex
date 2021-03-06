/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.monitoring.rest;

import java.io.PrintStream;

/**
 * User: ewatkmi Date: 31 Jul 2017
 */
public class ApexMonitoringRestMain {
    // Services state
    public enum ServicesState {
                    STOPPED,
                    READY,
                    INITIALIZING,
                    RUNNING
    };
    private ServicesState state = ServicesState.STOPPED;
    
    // The parameters for the client
    private ApexMonitoringRestParameters parameters = null;
    
    // Output and error streams for messages
    private final PrintStream outStream;
    
    // The Apex services client this class is running
    private ApexMonitoringRest apexMonitoringRest = null;

    /**
     * Main method, main entry point for command
     * 
     * @param args
     *            The command line arguments for the client
     */
    public static void main(String[] args) {
        try {
            ApexMonitoringRestMain restMain = new ApexMonitoringRestMain(args, System.out);
            restMain.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Constructor, kicks off the rest service
     * @param args The command line arguments for the RESTful service
     * @param outStream The stream for output messages
     */
    public ApexMonitoringRestMain(String[] args, final PrintStream outStream) {
            // Save the streams for output and error
            this.outStream = outStream;
            
            // Client parameter parsing
            ApexMonitoringRestParameterParser parser = new ApexMonitoringRestParameterParser();

            try {
                    // Get and check the parameters
                    parameters = parser.parse(args);
            }
            catch (ApexMonitoringRestParameterException e) {
                    throw new ApexMonitoringRestParameterException("Apex Services REST endpoint ("+this.toString()+") parameter error, " + e.getMessage() + '\n' + parser.getHelp(ApexMonitoringRestMain.class.getCanonicalName()));
            }

            if (parameters.isHelpSet()) {
                    throw new ApexMonitoringRestParameterException(parser.getHelp(ApexMonitoringRestMain.class.getCanonicalName()));
            }

            // Validate the parameters
            String validationMessage = parameters.validate();
            if (validationMessage.length() > 0) {
                    throw new ApexMonitoringRestParameterException("Apex Services REST endpoint ("+this.toString()+") parameters invalid, " + validationMessage + '\n' + parser.getHelp(ApexMonitoringRestMain.class.getCanonicalName()));
            }

            state = ServicesState.READY;
    }
    
    /**
     * Initialize the rest service
     */
    public void init() {
            outStream.println("Apex Services REST endpoint ("+this.toString()+") starting at " + parameters.getBaseURI().toString() + " . . .");
            
            try {
                    state = ServicesState.INITIALIZING;
                    
                    // Start the REST service
                    apexMonitoringRest = new ApexMonitoringRest(parameters);

                    // Add a shutdown hook to shut down the rest services when the process is exiting
                    Runtime.getRuntime().addShutdownHook(new Thread(new ApexServicesShutdownHook()));

                    state = ServicesState.RUNNING;

                    if (parameters.getTimeToLive() == ApexMonitoringRestParameters.INFINITY_TIME_TO_LIVE) {
                            outStream.println("Apex Services REST endpoint ("+this.toString()+") started at " + parameters.getBaseURI().toString());
                    }
                    else {
                            outStream.println("Apex Services REST endpoint ("+this.toString()+") started");
                    }

                    // Find out how long is left to wait
                    long timeRemaining = parameters.getTimeToLive();
                    while (timeRemaining == ApexMonitoringRestParameters.INFINITY_TIME_TO_LIVE || timeRemaining > 0) {
                            // decrement the time to live in the non-infinity case
                            if (timeRemaining > 0) {
                                    timeRemaining--;
                            }

                            // Wait for a second
                            Thread.sleep(1000);
                    }
            }
            catch (Exception e) {
                    outStream.println("Apex Services REST endpoint ("+this.toString()+") failed at with error: " + e.getMessage());
            }
            finally {
                    if (apexMonitoringRest != null) {
                            apexMonitoringRest.shutdown();
                            apexMonitoringRest = null;
                    }
                    state = ServicesState.STOPPED;
            }
            
    }

    /**
     * Get services state.
     * @return the service state
     */
    public ServicesState getState() {
            return state;
    }

    @Override
    public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append(this.getClass().getSimpleName()).append(": Config=[").append(this.parameters).append("], State=").append(this.getState());
            return ret.toString();
    }

    /**
     * Explicitly shut down the services
     */
    public void shutdown() {
            if (apexMonitoringRest != null) {
                    outStream.println("Apex Services REST endpoint ("+this.toString()+") shutting down");
                    apexMonitoringRest.shutdown();
            }
            state = ServicesState.STOPPED;
            outStream.println("Apex Services REST endpoint ("+this.toString()+") shut down");
    }

    /**
     * This class is a shutdown hook for the Apex services command
     */
    private class ApexServicesShutdownHook implements Runnable {
            /* (non-Javadoc)
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                    if (apexMonitoringRest != null) {
                            apexMonitoringRest.shutdown();
                    }
            }
    }

}
