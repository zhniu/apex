/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.adaptive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.domains.adaptive.model.AdaptiveDomainModelFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.java.JavaExecutorParameters;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;

/**
 * This policy passes, and recieves a Double event context filed called "EVCDouble".<br>
 * The policy tries to detect anomalies in the pattern of values for EVCDouble<br>
 * See the 2 test cases below (1 short, 1 long)
 *
 * @author John Keeney (John.Keeney@ericsson.com)
 */
public class TestAnomalyDetectionTSLUseCase {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(TestAnomalyDetectionTSLUseCase.class);

    private static final int MAXITERATIONS = 3660;
    private static final Random RAND = new Random(System.currentTimeMillis());

    @Test
    // once through the long running test below
    public void TestAnomalyDetectionTSL() throws ApexException, InterruptedException, IOException {
        final AxPolicyModel apexPolicyModel = new AdaptiveDomainModelFactory().getAnomalyDetectionPolicyModel();
        assertNotNull(apexPolicyModel);

        final AxValidationResult validationResult = new AxValidationResult();
        apexPolicyModel.validate(validationResult);
        assertTrue(validationResult.isValid());

        final AxArtifactKey key = new AxArtifactKey("AnomalyTSLApexEngine", "0.0.1");
        final EngineParameters parameters = new EngineParameters();
        parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
        parameters.getExecutorParameterMap().put("JAVA", new JavaExecutorParameters());

        final ApexEngine apexEngine1 = new ApexEngineFactory().createApexEngine(key);

        final TestApexActionListener listener1 = new TestApexActionListener("TestListener1");
        apexEngine1.addEventListener("listener", listener1);
        apexEngine1.updateModel(apexPolicyModel);
        apexEngine1.start();
        final EnEvent triggerEvent = apexEngine1.createEvent(new AxArtifactKey("AnomalyDetectionTriggerEvent", "0.0.1"));
        final double rval = RAND.nextGaussian();
        triggerEvent.put("Iteration", 0);
        triggerEvent.put("MonitoredValue", rval);
        LOGGER.info("Triggering policy in Engine 1 with " + triggerEvent);
        apexEngine1.handleEvent(triggerEvent);
        final EnEvent result = listener1.getResult();
        LOGGER.info("Receiving action event {} ", result);
        assertEquals("ExecutionIDs are different", triggerEvent.getExecutionID(),result.getExecutionID());
        triggerEvent.clear();
        result.clear();
        Thread.sleep(1);
        apexEngine1.stop();
    }

    /**
     * This policy passes, and recieves a Double event context filed called "EVCDouble"<br>
     * The policy tries to detect anomalies in the pattern of values for EVCDouble <br>
     * This test case generates a SineWave-like pattern for the parameter, repeating every 360 iterations. (These Period should probably be set using
     * TaskParameters!) Every 361st value is a random number!, so should be identified as an Anomaly. The policy has 3 Decide Tasks, and the Decide
     * TaskSelectionLogic picks one depending on the 'Anomaliness' of the input data. <br>
     * To plot the results grep debug results for the string "************", paste into excel and delete non-relevant columns<br>
     *
     * @throws ApexException the apex exception
     * @throws InterruptedException the interrupted exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    // Test is disabled by default. uncomment below, or execute using the main() method
    // @Test
    // EG Dos command: apex-core.engine> mvn
    // -Dtest=com.ericsson.apex.core.engine.ml.TestAnomalyDetectionTSLUseCase test | findstr /L /C:"Apex [main] DEBUG c.e.a.e.TaskSelectionExecutionLogging -
    // TestAnomalyDetectionTSL_Policy0000DecideStateTaskSelectionLogic.getTask():"
    public void TestAnomalyDetectionTSL_main() throws ApexException, InterruptedException, IOException {

        final AxPolicyModel apexPolicyModel = new AdaptiveDomainModelFactory().getAnomalyDetectionPolicyModel();
        assertNotNull(apexPolicyModel);

        final AxValidationResult validationResult = new AxValidationResult();
        apexPolicyModel.validate(validationResult);
        assertTrue(validationResult.isValid());

        final AxArtifactKey key = new AxArtifactKey("AnomalyTSLApexEngine", "0.0.1");
        final EngineParameters parameters = new EngineParameters();
        parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
        parameters.getExecutorParameterMap().put("JAVA", new JavaExecutorParameters());

        final ApexEngine apexEngine1 = new ApexEngineFactory().createApexEngine(key);

        final TestApexActionListener listener1 = new TestApexActionListener("TestListener1");
        apexEngine1.addEventListener("listener1", listener1);
        apexEngine1.updateModel(apexPolicyModel);
        apexEngine1.start();

        final EnEvent triggerEvent = apexEngine1.createEvent(new AxArtifactKey("AnomalyDetectionTriggerEvent", "0.0.1"));
        assertNotNull(triggerEvent);

        for (int iteration = 0; iteration < MAXITERATIONS; iteration++) {
            // Trigger the policy in engine 1

            double value = (Math.sin(Math.toRadians(iteration))) + (RAND.nextGaussian() / 25.0);
            // lets make every 361st number a random value to perhaps flag as an anomaly
            if (((iteration + 45) % 361) == 0) {
                value = (RAND.nextGaussian() * 2.0);
            }
            triggerEvent.put("Iteration", iteration);
            triggerEvent.put("MonitoredValue", value);
            LOGGER.info("Iteration " + iteration + ":\tTriggering policy in Engine 1 with " + triggerEvent);
            apexEngine1.handleEvent(triggerEvent);
            final EnEvent result = listener1.getResult();
            LOGGER.info("Iteration " + iteration + ":\tReceiving action event {} ", result);
            triggerEvent.clear();
            result.clear();
        }
        apexEngine1.stop();
        Thread.sleep(1000);
    }

    public static void main(final String[] args) throws ApexException, InterruptedException, IOException {
        new TestAnomalyDetectionTSLUseCase().TestAnomalyDetectionTSL_main();
    }
}
