/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.engine.stats;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.deployment.BatchDeployer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

/**
 * The Class EngineStats is a feature test program used to test the performance of Apex engines by continuously sending events to it and testing its execution
 * time.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class EngineStats implements Runnable {
    private static final int ARG_BACKOFF_DELAY = 4;

    private static final int SERVER_START_WAIT_TIME = 100;
    private static final int SERVER_EXECUTE_WAIT_TIME = 10000;
    private static final int SERVER_BACKOFF_WAIT_TIME = 100;

    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EngineStats.class);

    // Create an instance of the test server for this test
    private EngineTestServer server = null;

    // The back off delay is the maximum difference between the sent event count and received event count to allow before pausing event sending
    private int backoffDifference = 0;

    /**
     * The main method kicks off the program and runs the test.
     *
     * @param args {@code server-name} {@code thread-count} {@code engdep-port} {@code apex-model-file} {@code backoff-delay}
     * @throws Exception any exception thrown during the test
     */
    public static void main(final String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: EngineStats <apex-parameter-file> <backoff-delay>");
            return;
        }

        ApexCommandLineArguments arguments = new ApexCommandLineArguments();
        arguments.parse(args);
        arguments.validate();

        ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

        // Run the test and generate statistics
        new EngineStats(parameters.getEngineServiceParameters(), Integer.valueOf(args[ARG_BACKOFF_DELAY]));
    }

    /**
     * Instantiates a new test to get statistics on engine performance.
     *
     * @param serviceParameters the service parameters
     * @param backoffDelay the maximum difference between the sent event count and received event count to allow before pausing event sending
     * @throws ApexException the apex exception
     * @throws IOException the IO exception
     */
    private EngineStats(final EngineServiceParameters serviceParameters, final int backoffDelay) throws ApexException, IOException {
        LOGGER.debug("engine stats starting . . .");

        // Store the back off delay
        this.backoffDifference = backoffDelay;

        // Create a test server
        server = new EngineTestServer(serviceParameters);
        assert server != null;

        // Start the test server thread and wait for it to spin up
        final Thread serverThread = new Thread(server);
        serverThread.start();

        while (server.isStarting()) {
            ThreadUtilities.sleep(SERVER_START_WAIT_TIME);
        }

        // Start a deployer to deploy the Apex model to the test server
        final BatchDeployer deployer = new BatchDeployer("localhost", serviceParameters.getDeploymentPort());
        deployer.init();
        deployer.deployModel(serviceParameters.getPolicyModelFileName(), false, false);
        deployer.startEngines();
        deployer.close();

        // Start the testing thread of this class, which continually sends events to the Apex engine to test its performance
        new Thread(this).start();

        while (!Thread.interrupted()) {
            ThreadUtilities.sleep(SERVER_EXECUTE_WAIT_TIME);
            final long[] stats = server.getAndResetStats();
            if (stats != null) {
                LOGGER.info("," + serviceParameters.getEngineKey().getID() + ",avgExecTime(ms)," + stats[0] + ",T/ms," + stats[1]);
            }
        }

        deployer.init();
        deployer.stopEngines();
        deployer.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.info("engine stats server running . . .");

        // Initialize the event count and state
        long triggerEventsSent = 0;
        boolean interrupted = false;

        // Run until interrupted
        while (!interrupted) {
            try {
                // Instantiate two events
                final Date testStartTime = new Date();
                final Map<String, Object> eventDataMap = new HashMap<String, Object>();
                // CHECKSTYLE:OFF: checkstyle:magicNumber
                eventDataMap.put("TestSlogan", "This is a test slogan");
                eventDataMap.put("TestMatchCase", 12345);
                eventDataMap.put("TestTimestamp", testStartTime.getTime());
                eventDataMap.put("TestTemperature", 34.5445667);
                // CHECKSTYLE:ON: checkstyle:magicNumber

                final ApexEvent event0000 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
                final ApexEvent event0100 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");

                event0000.putAll(eventDataMap);
                event0100.putAll(eventDataMap);

                // Send the two events to the test server
                server.sendEvent(event0000);
                server.sendEvent(event0100);

                triggerEventsSent += 2;
            }
            catch (final Exception e) {
                LOGGER.error("engine stats server, exception on event sending", e);
            }

            // Do not send any more events if there are >= backoffDifference outstanding events not replied to
            while (triggerEventsSent > server.getTotalActionEventsReceived() + backoffDifference) {
                try {
                    Thread.sleep(SERVER_BACKOFF_WAIT_TIME);
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        }

        // Test is completed
        server.stopServer();
        LOGGER.info("engine stats server stopped");
    }
}
