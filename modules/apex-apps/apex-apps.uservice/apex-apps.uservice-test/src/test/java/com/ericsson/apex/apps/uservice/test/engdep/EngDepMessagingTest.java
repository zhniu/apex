/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.engdep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.apps.uservice.test.engine.stats.EngineTestServer;
import com.ericsson.apex.core.deployment.BatchDeployer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

//CHECKSTYLE:OFF: checkstyle:magicNumber

/**
 * The Class EngDepMessagingTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EngDepMessagingTest {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EngDepMessagingTest.class);

    private static final long MAX_START_WAIT = 10000; // 10 sec

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test EngDep messaging.
     *
     * @throws URISyntaxException the URI syntax exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexException the apex exception
     */
    @Test
    public void testEngDepMessaging() throws URISyntaxException, IOException, ApexException {
        LOGGER.debug("engine<-->deployment messaging test starting . .  .");

        ModelService.clear();

        final EngineServiceParameters parameters = new EngineServiceParameters();
        parameters.setName("EngDepMessagingTest");
        parameters.setVersion("0.0.1");
        parameters.setDeploymentPort(58820);
        parameters.setInstanceCount(3);
        parameters.setId(100);
        parameters.getEngineParameters().getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());

        final EngineTestServer server = new EngineTestServer(parameters);
        assertNotNull(server);

        final Thread serverThread = new Thread(server);
        serverThread.start();
        final long starttime = System.currentTimeMillis();
        while (server.isStarting() && System.currentTimeMillis() - starttime < MAX_START_WAIT) {
            ThreadUtilities.sleep(100);
        }
        if (server.isStarting()) {
            fail("Test server failed to start after " + MAX_START_WAIT + " ms");
        }

        final AxPolicyModel apexPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");

        final BatchDeployer deployer1 = new BatchDeployer("localhost", 58820);
        assertNotNull(deployer1);

        deployer1.init();
        deployer1.deployModel(apexPolicyModel, false, false);
        deployer1.stopEngines();
        deployer1.startEngines();
        deployer1.close();

        // Send events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte) 123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event0 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "apex", "test");
        event0.putAll(eventDataMap);
        server.sendEvent(event0);

        final ApexEvent event1 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "apex", "test");
        event1.putAll(eventDataMap);
        server.sendEvent(event1);

        // Wait for results
        while (server.getTotalActionEventsReceived() < 2) {
            ThreadUtilities.sleep(100);
        }
        ThreadUtilities.sleep(500);

        assertEquals(server.getTotalActionEventsReceived(), 2);

        deployer1.init();
        deployer1.stopEngines();
        deployer1.close();

        // Test re-initialization of model
        final BatchDeployer deployer2 = new BatchDeployer("localhost", 58820);
        assertNotNull(deployer2);

        deployer2.init();
        deployer2.deployModel(apexPolicyModel, true, true);
        deployer2.stopEngines();
        deployer2.startEngines();
        deployer2.close();

        server.sendEvent(event0);
        server.sendEvent(event1);

        // Wait for results
        while (server.getTotalActionEventsReceived() < 4) {
            ThreadUtilities.sleep(100);
        }
        ThreadUtilities.sleep(500);

        assertEquals(server.getTotalActionEventsReceived(), 4);

        deployer2.init();
        deployer2.stopEngines();
        deployer2.close();

        server.stopServer();
        LOGGER.debug("engine<-->deployment messaging test finished");
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
    }
}
