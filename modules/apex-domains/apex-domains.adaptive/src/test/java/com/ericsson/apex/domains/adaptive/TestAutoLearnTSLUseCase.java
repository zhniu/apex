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
 * Test Auto learning in TSL.
 *
 * @author John Keeney (John.Keeney@ericsson.com)
 */
public class TestAutoLearnTSLUseCase {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(TestAutoLearnTSLUseCase.class);

    private static final int MAXITERATIONS = 1000;
    private static final Random rand = new Random(System.currentTimeMillis());

    @Test
    // once through the long running test below
    public void TestAutoLearnTSL() throws ApexException, InterruptedException, IOException {
        final AxPolicyModel apexPolicyModel = new AdaptiveDomainModelFactory().getAutoLearnPolicyModel();
        assertNotNull(apexPolicyModel);

        final AxValidationResult validationResult = new AxValidationResult();
        apexPolicyModel.validate(validationResult);
        assertTrue(validationResult.isValid());

        final AxArtifactKey key = new AxArtifactKey("AADMApexEngine", "0.0.1");
        final EngineParameters parameters = new EngineParameters();
        parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
        parameters.getExecutorParameterMap().put("JAVA", new JavaExecutorParameters());

        final ApexEngine apexEngine1 = new ApexEngineFactory().createApexEngine(key);

        final TestApexActionListener listener1 = new TestApexActionListener("TestListener1");
        apexEngine1.addEventListener("listener", listener1);
        apexEngine1.updateModel(apexPolicyModel);
        apexEngine1.start();
        final EnEvent triggerEvent = apexEngine1.createEvent(new AxArtifactKey("AutoLearnTriggerEvent", "0.0.1"));
        final double rval = rand.nextGaussian();
        triggerEvent.put("MonitoredValue", rval);
        triggerEvent.put("LastMonitoredValue", 0D);
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
     * This policy passes, and receives a Double event context filed called "EVCDouble"<br>
     * The policy tries to keep the value at 50, with a Min -100, Max 100 (These should probably be set using TaskParameters!)<br>
     * The policy has 7 Decide Tasks that manipulate the value of this field in unknown ways.<br>
     * The Decide TSL learns the effect of each task, and then selects the appropriate task to get the value back to 50<br>
     * After the value settles close to 50 for a while, the test Rests the value to to random number and then continues<br>
     * To plot the results grep stdout debug results for the string "*******", paste into excel and delete non-relevant columns<br>
     *
     * @throws ApexException the apex exception
     * @throws InterruptedException the interrupted exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    // @Test
    public void TestAutoLearnTSL_main() throws ApexException, InterruptedException, IOException {

        final double WANT = 50.0;
        final double toleranceTileJump = 3.0;

        final AxPolicyModel apexPolicyModel = new AdaptiveDomainModelFactory().getAutoLearnPolicyModel();
        assertNotNull(apexPolicyModel);

        final AxValidationResult validationResult = new AxValidationResult();
        apexPolicyModel.validate(validationResult);
        assertTrue(validationResult.isValid());

        final AxArtifactKey key = new AxArtifactKey("AADMApexEngine", "0.0.1");
        final EngineParameters parameters = new EngineParameters();
        parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
        parameters.getExecutorParameterMap().put("JAVA", new JavaExecutorParameters());

        final ApexEngine apexEngine1 = new ApexEngineFactory().createApexEngine(key);

        final TestApexActionListener listener1 = new TestApexActionListener("TestListener1");
        apexEngine1.addEventListener("listener1", listener1);
        apexEngine1.updateModel(apexPolicyModel);
        apexEngine1.start();

        final EnEvent triggerEvent = apexEngine1.createEvent(new AxArtifactKey("AutoLearnTriggerEvent", "0.0.1"));
        assertNotNull(triggerEvent);
        final double MIN = -100;
        final double MAX = 100;

        double rval = (((rand.nextGaussian() + 1) / 2) * (MAX - MIN)) + MIN;
        triggerEvent.put("MonitoredValue", rval);
        triggerEvent.put("LastMonitoredValue", 0);

        double avval = 0;
        double distance;
        double avcount = 0;

        for (int iteration = 0; iteration < MAXITERATIONS; iteration++) {
            // Trigger the policy in engine 1
            LOGGER.info("Triggering policy in Engine 1 with " + triggerEvent);
            apexEngine1.handleEvent(triggerEvent);
            final EnEvent result = listener1.getResult();
            LOGGER.info("Receiving action event {} ", result);
            triggerEvent.clear();

            double val = (Double) result.get("MonitoredValue");
            final double prevval = (Double) result.get("LastMonitoredValue");

            triggerEvent.put("MonitoredValue", prevval);
            triggerEvent.put("LastMonitoredValue", val);

            avcount = Math.min((avcount + 1), 20); // maintain average of only the last 20 values
            avval = ((avval * (avcount - 1)) + val) / (avcount);

            distance = Math.abs(WANT - avval);
            if (distance < toleranceTileJump) {
                rval = (((rand.nextGaussian() + 1) / 2) * (MAX - MIN)) + MIN;
                val = rval;
                triggerEvent.put("MonitoredValue", val);
                LOGGER.info("Iteration " + iteration + ": Average " + avval + " has become closer (" + distance + ") than " + toleranceTileJump + " to " + WANT
                        + " so reseting val:\t\t\t\t\t\t\t\t" + val);
                avval = 0;
                avcount = 0;
            }
            LOGGER.info("Iteration " + iteration + ": \tpreval\t" + prevval + "\tval\t" + val + "\tavval\t" + avval);

            result.clear();
            Thread.sleep(1);
        }

        apexEngine1.stop();
        Thread.sleep(1000);

    }

    public static void main(final String[] args) throws ApexException, InterruptedException, IOException {
        new TestAutoLearnTSLUseCase().TestAutoLearnTSL_main();
    }
}
