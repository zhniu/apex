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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelWriter;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.runtime.impl.EngineServiceImpl;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

//CHECKSTYLE:OFF: checkstyle:magicnumber  

/**
 * The Class ApexServiceTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexServiceTest {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexServiceTest.class);

    private static final long MAX_STOP_WAIT    = 5000; //5 sec
    private static final long MAX_START_WAIT   = 5000; //5 sec
    private static final long MAX_RECV_WAIT    = 5000; //5 sec

    private final static AxArtifactKey engineServiceKey = new AxArtifactKey("Machine-1_process-1_engine-1", "0.0.0");
    private final static EngineServiceParameters parameters = new EngineServiceParameters();
    private static EngineService service = null;
    private static TestListener listener = null;
    private static AxPolicyModel apexPolicyModel = null;
    private static int actionEventsReceived = 0;

    private static String apexModelString;
    
    private boolean waitFlag = true;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        // create engine with 3 threads
        parameters.setInstanceCount(3);
        parameters.setName(engineServiceKey.getName());
        parameters.setVersion(engineServiceKey.getVersion());
        parameters.setId(100);
        parameters.getEngineParameters().getExecutorParameterMap().put("MVEL",  new MVELExecutorParameters());
        service = EngineServiceImpl.create(parameters);


        LOGGER.debug("Running TestApexEngine. . .");

        apexPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
        assertNotNull(apexPolicyModel);

        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        new ApexModelWriter<AxPolicyModel>(AxPolicyModel.class).write(apexPolicyModel, baOutputStream);
        apexModelString = baOutputStream.toString();

        // create engine
        listener = new TestListener();
        service.registerActionListener("Listener", listener);
    }

    /**
     * Update the engine then test the engine with 2 sample events.
     *
     * @throws ApexException if there is a problem
     */
    @Test
    public void testExecutionSet1() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexModelString, true);
        //Start the service
        service.startAll();
        final long starttime = System.currentTimeMillis();
        for (AxArtifactKey engineKey : service.getEngineKeys()) {
            System.out.println(service.getStatus(engineKey));
        }
        while(!service.isStarted() && System.currentTimeMillis()-starttime < MAX_START_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStarted()){
            fail("Apex Service "+service.getKey()+" failed to start after "+MAX_START_WAIT+" ms");
        }
 
        final EngineServiceEventInterface engineServiceEventInterface = service.getEngineServiceEventInterface();

        // Send some events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<String, Object>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte)123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event.setExecutionID(System.nanoTime());
        event.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event);

        final ApexEvent event2 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event2.setExecutionID(System.nanoTime());
        event2.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event2);

        // Wait for results
        final long recvtime = System.currentTimeMillis();
        while (actionEventsReceived < 2 && System.currentTimeMillis()-recvtime < MAX_RECV_WAIT) {
            ThreadUtilities.sleep(100);
        }
        ThreadUtilities.sleep(500);
        assertEquals(2,actionEventsReceived);
        actionEventsReceived = 0;
        

        // Stop all engines on this engine service
        long stoptime = System.currentTimeMillis();
        service.stop();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            fail("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
        }
    }

    /**
     * Update the engine then test the engine with 2 sample events.
     *
     * @throws ApexException if there is a problem
     */
    @Test
    public void testExecutionSet1Sync() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexModelString, true);
        //Start the service
        service.startAll();
        final long starttime = System.currentTimeMillis();
        for (AxArtifactKey engineKey : service.getEngineKeys()) {
            System.out.println(service.getStatus(engineKey));
        }
        while(!service.isStarted() && System.currentTimeMillis()-starttime < MAX_START_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStarted()){
            fail("Apex Service "+service.getKey()+" failed to start after "+MAX_START_WAIT+" ms");
        }

        // Send some events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<String, Object>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte)123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event1 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event1.putAll(eventDataMap);
        event1.setExecutionID(System.nanoTime());
        
        ApexEventListener myEventListener1 = new ApexEventListener() {
            @Override
            public void onApexEvent(ApexEvent responseEvent) {
                assertNotNull("Synchronous sendEventWait failed", responseEvent);
                assertEquals(event1.getExecutionID(), responseEvent.getExecutionID());
                waitFlag = false;
            }
        };
        
        waitFlag = true;
        service.registerActionListener("Listener1", myEventListener1);
        service.getEngineServiceEventInterface().sendEvent(event1);
        
        while (waitFlag) {
            ThreadUtilities.sleep(100);
        }
        
        final ApexEvent event2 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event2.setExecutionID(System.nanoTime());
        event2.putAll(eventDataMap);

        ApexEventListener myEventListener2 = new ApexEventListener() {
            @Override
            public void onApexEvent(ApexEvent responseEvent) {
                assertNotNull("Synchronous sendEventWait failed", responseEvent);
                assertEquals(event2.getExecutionID(), responseEvent.getExecutionID());
                assertEquals(2, actionEventsReceived);
                waitFlag = false;
            }
        };
        
        waitFlag = true;
        service.deregisterActionListener("Listener1");
        service.registerActionListener("Listener2", myEventListener2);
        service.getEngineServiceEventInterface().sendEvent(event2);

        while (waitFlag) {
            ThreadUtilities.sleep(100);
        }
        service.deregisterActionListener("Listener2");
       
        actionEventsReceived = 0;

        // Stop all engines on this engine service
        final long stoptime = System.currentTimeMillis();
        service.stop();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            fail("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
        }
    }

    /**
     * Update the engine then test the engine with 2 sample events - again.
     *
     * @throws ApexException if there is a problem
     */
    @Test
    public void testExecutionSet2() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexModelString, true);
        //Start the service
        service.startAll();
        final long starttime = System.currentTimeMillis();
        for (AxArtifactKey engineKey : service.getEngineKeys()) {
            System.out.println(service.getStatus(engineKey));
        }
        while(!service.isStarted() && System.currentTimeMillis()-starttime < MAX_START_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStarted()){
            fail("Apex Service "+service.getKey()+" failed to start after "+MAX_START_WAIT+" ms");
        }

        final EngineServiceEventInterface engineServiceEventInterface = service.getEngineServiceEventInterface();

        // Send some events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<String, Object>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte)123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event.setExecutionID(System.nanoTime());
        event.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event);

        final ApexEvent event2 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event2.setExecutionID(System.nanoTime());
        event2.putAll(eventDataMap);
        engineServiceEventInterface.sendEvent(event2);

        // Wait for results
        final long recvtime = System.currentTimeMillis();
        while (actionEventsReceived < 2 && System.currentTimeMillis()-recvtime < MAX_RECV_WAIT) {
            ThreadUtilities.sleep(100);
        }
        ThreadUtilities.sleep(500);
        assertEquals(2,actionEventsReceived);
        actionEventsReceived = 0;

        // Stop all engines on this engine service
        final long stoptime = System.currentTimeMillis();
        service.stop();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            fail("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
        }
    }

    /**
     * Update the engine then test the engine with 2 sample events - again.
     *
     * @throws ApexException if there is a problem
     */
    @Test
    public void testExecutionSet2Sync() throws ApexException {
        service.updateModel(parameters.getEngineKey(), apexModelString, true);
        //Start the service
        service.startAll();
        final long starttime = System.currentTimeMillis();
        for (AxArtifactKey engineKey : service.getEngineKeys()) {
            System.out.println(service.getStatus(engineKey));
        }
        while(!service.isStarted() && System.currentTimeMillis()-starttime < MAX_START_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStarted()){
            fail("Apex Service "+service.getKey()+" failed to start after "+MAX_START_WAIT+" ms");
        }

        // Send some events
        final Date testStartTime = new Date();
        final Map<String, Object> eventDataMap = new HashMap<String, Object>();
        eventDataMap.put("TestSlogan", "This is a test slogan");
        eventDataMap.put("TestMatchCase", (byte)123);
        eventDataMap.put("TestTimestamp", testStartTime.getTime());
        eventDataMap.put("TestTemperature", 34.5445667);

        final ApexEvent event1 = new ApexEvent("Event0000", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event1.putAll(eventDataMap);
        
        ApexEventListener myEventListener1 = new ApexEventListener() {
            @Override
            public void onApexEvent(ApexEvent responseEvent) {
                assertNotNull("Synchronous sendEventWait failed", responseEvent);
                assertEquals(event1.getExecutionID(), responseEvent.getExecutionID());
                waitFlag = false;
            }
        };
        
        waitFlag = true;
        service.registerActionListener("Listener1", myEventListener1);
        service.getEngineServiceEventInterface().sendEvent(event1);

        while (waitFlag) {
            ThreadUtilities.sleep(100);
        }

        final ApexEvent event2 = new ApexEvent("Event0100", "0.0.1", "com.ericsson.apex.domains.sample.events", "test", "apex");
        event2.putAll(eventDataMap);
        
        ApexEventListener myEventListener2 = new ApexEventListener() {
            @Override
            public void onApexEvent(ApexEvent responseEvent) {
                assertNotNull("Synchronous sendEventWait failed", responseEvent);
                assertEquals(event2.getExecutionID(), responseEvent.getExecutionID());
                waitFlag = false;
            }
        };
        
        waitFlag = true;
        service.registerActionListener("Listener2", myEventListener2);
        service.deregisterActionListener("Listener1");
        service.getEngineServiceEventInterface().sendEvent(event2);

        while (waitFlag) {
            ThreadUtilities.sleep(100);
        }

        service.deregisterActionListener("Listener2");

        assertEquals(2, actionEventsReceived);
        
        actionEventsReceived = 0;

        // Stop all engines on this engine service
        final long stoptime = System.currentTimeMillis();
        service.stop();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            fail("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
        }
    }

    /**
     * Tear down the the test infrastructure.
     *
     * @throws ApexException if there is an error
     */
    @AfterClass
    public static void tearDown() throws Exception {
        // Stop all engines on this engine service
        final long stoptime = System.currentTimeMillis();
        service.stop();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            fail("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
        }
        service = null;
    }

    /**
     * The listener interface for receiving test events. The class that is interested in processing a test event implements this interface, and the object
     * created with that class is registered with a component using the component's <code>addTestListener</code> method. When
     * the test event occurs, that object's appropriate
     * method is invoked.
     *
     * @see TestEvent
     */
    private final static class TestListener implements ApexEventListener {

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
