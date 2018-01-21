/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.deployment;

import java.util.Arrays;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * This utility class is used to start and stop periodic events on Apex engines over the EngDep protocol.
 */
public class PeriodicEventManager {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(BatchDeployer.class);

    private static final int NUM_ARGUMENTS = 4;
    private static final int PERIODIC_EVENT_INTERVAL = 3;

    // The facade that is handling messaging to the engine service
    private EngineServiceFacade engineServiceFacade = null;

    /**
     * The main method, reads the Apex server host address, port and location of the Apex model XML file from the command line arguments.
     *
     * @param args the arguments that specify the Apex engine and the Apex model file
     */
    public static void main(final String[] args) {
        if (args.length != NUM_ARGUMENTS) {
            LOGGER.error("invalid arguments: " + Arrays.toString(args));
            LOGGER.error("usage: Deployer <server address> <port address> <start/stop> <periods in ms>");
            return;
        }

        PeriodicEventManager deployer = null;
        try {
            // Use a Deployer object to handle model deployment
            deployer = new PeriodicEventManager(args[0], Integer.parseInt(args[1]));
            deployer.init();
            if (args[2].equalsIgnoreCase("start")) {
                deployer.startPerioidicEvents(Long.parseLong(args[PERIODIC_EVENT_INTERVAL]));
            }
            else {
                deployer.stopPerioidicEvents();
            }
        }
        catch (final ApexException e) {
            LOGGER.error("model deployment failed on parameters {}", args, e);
        }
        finally {
            if (deployer != null) {
                deployer.close();
            }
        }
    }

    /**
     * Instantiates a new deployer.
     *
     * @param hostName the host name of the host running the Apex Engine
     * @param port the port to use for EngDep communication with the Apex engine
     */
    public PeriodicEventManager(final String hostName, final int port) {
        engineServiceFacade = new EngineServiceFacade(hostName, port);
    }

    /**
     * Initializes the deployer, opens an EngDep communication session with the Apex engine.
     *
     * @throws ApexDeploymentException thrown on deployment and communication errors
     */
    public void init() throws ApexDeploymentException {
        engineServiceFacade.init();
    }

    /**
     * Close the EngDep connection to the Apex server.
     */
    public void close() {
        engineServiceFacade.close();
    }

    /**
     * Start the Apex engines on the engine service.
     *
     * @param period the interval in milliseconds between periodic events
     * @throws ApexDeploymentException on messaging errors
     */
    public void startPerioidicEvents(final long period) throws ApexDeploymentException {
        for (final AxArtifactKey engineKey : engineServiceFacade.getEngineKeyArray()) {
            engineServiceFacade.startPerioidicEvents(engineKey, period);
        }
    }

    /**
     * Stop the Apex engines on the engine service.
     *
     * @throws ApexDeploymentException on messaging errors
     */
    public void stopPerioidicEvents() throws ApexDeploymentException {
        for (final AxArtifactKey engineKey : engineServiceFacade.getEngineKeyArray()) {
            engineServiceFacade.stopPerioidicEvents(engineKey);
        }
    }
}
