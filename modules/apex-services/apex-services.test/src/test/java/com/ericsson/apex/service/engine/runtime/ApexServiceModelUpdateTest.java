/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelWriter;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.runtime.impl.EngineServiceImpl;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

//CHECKSTYLE:OFF: checkstyle:magicnumber  

/**
 * The Class ApexServiceTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexServiceModelUpdateTest {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexServiceModelUpdateTest.class);

    private final AxArtifactKey engineServiceKey = new AxArtifactKey("Machine-1_process-1_engine-1", "0.0.0");
    private final EngineServiceParameters parameters = new EngineServiceParameters();
    private EngineService service = null;
    private TestListener listener = null;
    private int actionEventsReceived = 0;

    private AxPolicyModel apexSamplePolicyModel = null;
    private String apexSampleModelString;

    /**
     * Sets up the test by creating an engine and reading in the test policy.
     *
     * @throws ApexException if something goes wron
     */
    @Before
    public void setUp() throws ApexException {
        // create engine with 3 threads
        parameters.setInstanceCount(3);
        parameters.setName(engineServiceKey.getName());
        parameters.setVersion(engineServiceKey.getVersion());
        parameters.setId(100);
        parameters.getEngineParameters().getExecutorParameterMap().put("MVEL",  new MVELExecutorParameters());
        service = EngineServiceImpl.create(parameters);

        LOGGER.debug("Running TestApexEngine. . .");

        apexSamplePolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
        assertNotNull(apexSamplePolicyModel);

        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        new ApexModelWriter<AxPolicyModel>(AxPolicyModel.class).write(apexSamplePolicyModel, baOutputStream);
        apexSampleModelString = baOutputStream.toString();

        // create engine
        listener = new TestListener();
        service.registerActionListener("MyListener", listener);
    }

    /**
     * Tear down the the test infrastructure.
     *
     * @throws ApexException if there is an error
     */
    @After
    public void tearDown() throws Exception {
        if(service != null) {
            service.stop();
        }
        service = null;
    }

    /**
     * Test start with no model.
     */
    @Test
    public void testNoModelStart() {
        try {
            service.startAll();
            fail("Engine should not start with no model");
        }
        catch (Exception e) {
            e.printStackTrace();
            assertEquals("start()<-Machine-1_process-1_engine-1-0:0.0.0,STOPPED,  cannot start engine, "
                    + "engine has not been initialized, its model is not loaded", e.getMessage());
        }
    }

    /**
     * Test model update with string model without force.
     *
     * @throws ApexException if there is an error
     */
    @Test
    public void testModelUpdateStringNewNoForce() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexSampleModelString, false);
        service.startAll();
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());
    }

    /**
     * Test model update with string model with force.
     *
     * @throws ApexException if there is an error
     */
    @Test
    public void testModelUpdateStringNewForce() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexSampleModelString, true);
        service.startAll();
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());
    }

    /**
     * Test model update with a new string model without force.
     *
     * @throws ApexException if there is an error
     */
    @Test
    public void testModelUpdateStringNewNewNoForce() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexSampleModelString, false);
        service.startAll();
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());

        sendEvents();

        service.updateModel(parameters.getEngineKey(), apexSampleModelString, false);
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());

        sendEvents();
    }

    /**
     * Test incompatible model update with a model object without force.
     *
     * @throws ApexException if there is an error
     */
    @Test
    public void testModelUpdateIncoNoForce() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexSamplePolicyModel, false);
        service.startAll();
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());

        // Different model name, incompatible
        AxPolicyModel incoPolicyModel0 = (AxPolicyModel) apexSamplePolicyModel.clone();
        incoPolicyModel0.getKey().setName("INCOMPATIBLE");

        try {
            service.updateModel(parameters.getEngineKey(), incoPolicyModel0, false);
            fail("model update should fail on incompatible model without force being true");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            assertEquals("apex model update failed, supplied model with key \"INCOMPATIBLE:0.0.1\" is not a compatible "
                    + "model update from the existing engine model with key \"SamplePolicyModelMVEL:0.0.1\"", e.getMessage());
        }

        // Still on old model
        sendEvents();

        // Different major version, incompatible
        AxPolicyModel incoPolicyModel1 = (AxPolicyModel) apexSamplePolicyModel.clone();
        incoPolicyModel1.getKey().setVersion("1.0.1");

        try {
            service.updateModel(parameters.getEngineKey(), incoPolicyModel1, false);
            fail("model update should fail on incompatible model without force being true");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            assertEquals("apex model update failed, supplied model with key \"SamplePolicyModelMVEL:1.0.1\" is not a compatible "
                    + "model update from the existing engine model with key \"SamplePolicyModelMVEL:0.0.1\"", e.getMessage());
        }

        // Still on old model
        sendEvents();

        // Different minor version, compatible
        AxPolicyModel coPolicyModel0 = (AxPolicyModel) apexSamplePolicyModel.clone();
        coPolicyModel0.getKey().setVersion("0.1.0");
        service.updateModel(parameters.getEngineKey(), coPolicyModel0, false);

        // On new compatible model
        sendEvents();

        // Different patch version, compatible
        AxPolicyModel coPolicyModel1 = (AxPolicyModel) apexSamplePolicyModel.clone();
        coPolicyModel1.getKey().setVersion("0.0.2");
        service.updateModel(parameters.getEngineKey(), coPolicyModel1, false);

        // On new compatible model
        sendEvents();

    }

    /**
     * Test incompatible model update with a model object with force.
     *
     * @throws ApexException if there is an error
     */
    @Test
    public void testModelUpdateIncoForce() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexSamplePolicyModel, false);
        service.startAll();
        assertEquals(apexSamplePolicyModel.getKey(), ModelService.getModel(AxPolicyModel.class).getKey());

        // Different model name, incompatible
        AxPolicyModel incoPolicyModel0 = (AxPolicyModel) apexSamplePolicyModel.clone();
        incoPolicyModel0.getKey().setName("INCOMPATIBLE");
        service.updateModel(parameters.getEngineKey(), incoPolicyModel0, true);

        // On updated model
        sendEvents();

        // Different major version, incompatible
        AxPolicyModel incoPolicyModel1 = (AxPolicyModel) apexSamplePolicyModel.clone();
        incoPolicyModel1.getKey().setVersion("1.0.1");
        service.updateModel(parameters.getEngineKey(), incoPolicyModel1, true);

        // On updated model
        sendEvents();

        // Different minor version, compatible
        AxPolicyModel coPolicyModel0 = (AxPolicyModel) apexSamplePolicyModel.clone();
        coPolicyModel0.getKey().setVersion("0.1.0");
        service.updateModel(parameters.getEngineKey(), coPolicyModel0, true);

        // On new compatible model
        sendEvents();

        // Different patch version, compatible
        AxPolicyModel coPolicyModel1 = (AxPolicyModel) apexSamplePolicyModel.clone();
        coPolicyModel1.getKey().setVersion("0.0.2");
        service.updateModel(parameters.getEngineKey(), coPolicyModel1, true);

        // On new compatible model
        sendEvents();

    }

    /**
     * Utility method to send some events into the test engine.
     * 
     * @throws ApexEventException if there is an error
     */
    private void sendEvents() throws ApexEventException {
        final EngineServiceEventInterface engineServiceEventInterface = service.getEngineServiceEventInterface();

        // Send some events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<String, Object>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte)123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event);

        final ApexEvent event2 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event2.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event2);

        // Wait for results
        while (actionEventsReceived < 2) {
            ThreadUtilities.sleep(100);
        }
        ThreadUtilities.sleep(500);
    }

    /**
     * The listener interface for receiving test events. The class that is interested in processing a test event implements this interface, and the object
     * created with that class is registered with a component using the component's <code>addTestListener</code> method. When
     * the test event occurs, that object's appropriate
     * method is invoked.
     *
     * @see TestEvent
     */
    private final class TestListener implements ApexEventListener {

        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.service.engine.runtime.ApexEventListener#onApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
         */
        @Override
        public synchronized void onApexEvent(final ApexEvent event) {
            LOGGER.debug("result 1 is:" + event);
            checkResult(event);
            actionEventsReceived++;

            final Date testStartTime = new Date((Long) event.get("TestTimestamp"));
            final Date testEndTime = new Date();

            LOGGER.info("policy execution time: " + (testEndTime.getTime() - testStartTime.getTime()) + "ms");
        }

        /**
         * Check result.
         *
         * @param result the result
         */
        private void checkResult(final ApexEvent result) {
            assertTrue(result.getName().startsWith("Event0004") || result.getName().startsWith("Event0104"));

            assertTrue(result.get("TestSlogan").equals("This is a test slogan"));
            assertTrue(result.get("TestMatchCase").equals(new Byte((byte) 123)));
            assertTrue(result.get("TestTemperature").equals(34.5445667));
            assertTrue(((byte) result.get("TestMatchCaseSelected"))     >= 0  && ((byte) result.get("TestMatchCaseSelected")     <= 3));
            assertTrue(((byte) result.get("TestEstablishCaseSelected")) >= 0  && ((byte) result.get("TestEstablishCaseSelected") <= 3));
            assertTrue(((byte) result.get("TestDecideCaseSelected"))    >= 0  && ((byte) result.get("TestDecideCaseSelected")    <= 3));
            assertTrue(((byte) result.get("TestActCaseSelected"))       >= 0  && ((byte) result.get("TestActCaseSelected")       <= 3));
        }
    }
}
