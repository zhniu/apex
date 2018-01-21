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

import java.util.Arrays;
import java.util.Map.Entry;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * This class initiates Apex as a complete service from the command line.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexMain {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexMain.class);

    // The Apex Activator that activates the Apex engine
    private ApexActivator activator;

    // The parameters read in from JSON
    private ApexParameters parameters;

    /**
     * Instantiates the Apex Apex service.
     *
     * @param args the commaind line arguments
     */
    public ApexMain(final String[] args) {
        System.out.println("Starting Apex service with parameters " + Arrays.toString(args) + " . . .");
        LOGGER.entry("Starting Apex service with parameters " + Arrays.toString(args) + " . . .");

        // Check the arguments
        final ApexCommandLineArguments arguments = new ApexCommandLineArguments();
        try {
            // The arguments return a string if there is a message to print and we should exit
            String argumentMessage = arguments.parse(args);
            if (argumentMessage != null) {
                LOGGER.info(argumentMessage);
                System.out.println(argumentMessage);
                return;
            }

            // Validate that the arguments are sane
            arguments.validate();
        }
        catch (final ApexException e) {
            System.err.println("start of Apex service failed: " + e.getMessage());
            LOGGER.error("start of Apex service failed", e);
            System.err.println(arguments.help(ApexMain.class.getCanonicalName()));
            return;
        }

        // Read the parameters
        try {
            parameters = new ApexParameterHandler().getParameters(arguments);
        }
        catch (final Exception e) {
            System.err.println("start of Apex service failed\n" + e.getMessage());
            LOGGER.error("start of Apex service failed", e);
            return;
        }

        // Set the name of the event handler parameters for producers and consumers
        for (Entry<String, EventHandlerParameters> ehParameterEntry: parameters.getEventOutputParameters().entrySet()) {
            if (!ehParameterEntry.getValue().checkSetName()) {
                ehParameterEntry.getValue().setName(ehParameterEntry.getKey());
            }
        }
        for (Entry<String, EventHandlerParameters> ehParameterEntry: parameters.getEventInputParameters().entrySet()) {
            if (!ehParameterEntry.getValue().checkSetName()) {
                ehParameterEntry.getValue().setName(ehParameterEntry.getKey());
            }
        }

        // Now, create the activator for the Apex service
        activator = new ApexActivator(parameters);

        // Start the activator
        try {
            activator.initialize();
        }
        catch (final ApexActivatorException e) {
            System.err.println("start of Apex service failed, used parameters are " + Arrays.toString(args));
            e.printStackTrace(System.err);
            LOGGER.error("start of Apex service failed, used parameters are " + Arrays.toString(args), e);
            return;
        }

        // Add a shutdown hook to shut everything down in an orderly manner
        Runtime.getRuntime().addShutdownHook(new ApexMainShutdownHookClass());
        LOGGER.exit("Started Apex");
        System.out.println("Started Apex service");
    }

    /**
     * Get the parameters specified in JSON.
     *
     * @return the parameters
     */
    public ApexParameters getParameters() {
        return parameters;
    }

    /**
     * Shut down Execution.
     *
     * @throws ApexException on shutdown errors
     */
    public void shutdown() throws ApexException {
        if (activator != null) {
            activator.terminate();
        }
    }

    /**
     * The Class ApexMainShutdownHookClass terminates the Apex engine for the Apex service when its run method is called.
     *
     * @author Liam Fallon (liam.fallon@ericsson.com)
     */
    private class ApexMainShutdownHookClass extends Thread {
        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                // Shutdown the Apex engine and wait for everything to stop
                activator.terminate();
            }
            catch (final ApexException e) {
                LOGGER.warn("error occured during shut down of the Apex service", e);
            }
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        new ApexMain(args);
    }
}
