/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.myfirstpolicy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.domains.myfirstpolicy.model.MFPDomainModelFactory;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * The Class TestMFPLogic.
 */
public class TestMFPLogic {

    private static final Map<String, String> LOGICEXTENSIONS = new LinkedHashMap<>();

    /**
     * Test setup.
     */
    @BeforeClass
    public static void testMFPUseCaseSetup() {
        LOGICEXTENSIONS.put("MVEL",         "mvel");
        LOGICEXTENSIONS.put("JAVASCRIPT",   "js");
    }

    /**
     * Check logic for MyFirstPolicy#1.
     */
    @Test
    public void testMFP1TaskLogic() {
        final AxPolicyModel apexPolicyModel = new MFPDomainModelFactory().getMFP1PolicyModel();
        assertNotNull(apexPolicyModel);

        final Map<String, String> logics = new LinkedHashMap<>();
        logics.putAll(getTSLLogics(apexPolicyModel));
        logics.putAll(getTaskLogics(apexPolicyModel));

        for (final Entry<String, String> logicvalue : logics.entrySet()) {
            final String filename = "examples/models/MyFirstPolicy/1/" + logicvalue.getKey();
            final String logic = logicvalue.getValue();
            final String expectedlogic = ResourceUtils.getResourceAsString(filename);
            assertNotNull("File " + filename + " was not found. It should contain logic for PolicyModel " + apexPolicyModel.getKey(), expectedlogic);
            assertEquals("The task in " + filename + " is not the same as the relevant logic in PolicyModel " + apexPolicyModel.getKey(),
                    expectedlogic.replaceAll("\\s", ""), logic.replaceAll("\\s", ""));
        }
    }


    /**
     * Check logic for MyFirstPolicyAlt#1.
     */
    @Test
    public void testMFP1AltTaskLogic() {
        final AxPolicyModel apexPolicyModel = new MFPDomainModelFactory().getMFP1AltPolicyModel();
        assertNotNull(apexPolicyModel);

        final Map<String, String> logics = new LinkedHashMap<>();
        logics.putAll(getTSLLogics(apexPolicyModel));
        logics.putAll(getTaskLogics(apexPolicyModel));

        for (final Entry<String, String> logicvalue : logics.entrySet()) {
            final String filename = "examples/models/MyFirstPolicy/1/" + logicvalue.getKey();
            final String logic = logicvalue.getValue();
            final String expectedlogic = ResourceUtils.getResourceAsString(filename);
            assertNotNull("File " + filename + " was not found. It should contain logic for PolicyModel " + apexPolicyModel.getKey(), expectedlogic);
            assertEquals("The task in " + filename + " is not the same as the relevant logic in PolicyModel " + apexPolicyModel.getKey(),
                    expectedlogic.replaceAll("\\s", ""), logic.replaceAll("\\s", ""));
        }
    }

    /**
     * Check logic for MyFirstPolicy2.
     */
    @Test
    public void testMFP2TaskLogic() {
        final AxPolicyModel apexPolicyModel = new MFPDomainModelFactory().getMFP2PolicyModel();
        assertNotNull(apexPolicyModel);

        final Map<String, String> logics = new LinkedHashMap<>();
        logics.putAll(getTSLLogics(apexPolicyModel));
        logics.putAll(getTaskLogics(apexPolicyModel));

        for (final Entry<String, String> logicvalue : logics.entrySet()) {
            final String filename = "examples/models/MyFirstPolicy/2/" + logicvalue.getKey();
            final String logic = logicvalue.getValue();
            final String expectedlogic = ResourceUtils.getResourceAsString(filename);
            assertNotNull("File " + filename + " was not found. It should contain logic for PolicyModel " + apexPolicyModel.getKey(), expectedlogic);
            assertEquals("The task in " + filename + " is not the same as the relevant logic in PolicyModel " + apexPolicyModel.getKey(),
                    expectedlogic.replaceAll("\\s", ""), logic.replaceAll("\\s", ""));
        }
    }

    /**
     * Gets the TSL logics.
     *
     * @param apexPolicyModel the apex policy model
     * @return the TSL logics
     */
    private Map<String, String> getTSLLogics(final AxPolicyModel apexPolicyModel) {
        final Map<String, String> ret = new LinkedHashMap<>();
        for (final Entry<AxArtifactKey, AxPolicy> policyentry : apexPolicyModel.getPolicies().getPolicyMap().entrySet()) {
            for (final Entry<String, AxState> statesentry : policyentry.getValue().getStateMap().entrySet()) {
                final AxState state = statesentry.getValue();
                final String tsllogic = state.getTaskSelectionLogic().getLogic();
                final String tsllogicflavour = state.getTaskSelectionLogic().getLogicFlavour();
                if (tsllogic != null && tsllogic.trim().length() > 0) {
                    assertNotNull("Logic Type \"" + tsllogicflavour + "\" in state " + statesentry.getKey() + " in policy " + policyentry.getKey()
                            + " is not supported in this test", LOGICEXTENSIONS.get(tsllogicflavour.toUpperCase()));
                    final String filename = policyentry.getKey().getName() + "_" + statesentry.getKey() + "TSL."
                            + LOGICEXTENSIONS.get(tsllogicflavour.toUpperCase());
                    ret.put(filename, tsllogic);
                }
            }
        }
        return ret;
    }

    /**
     * Gets the task logics.
     *
     * @param apexPolicyModel the apex policy model
     * @return the task logics
     */
    private Map<String, String> getTaskLogics(final AxPolicyModel apexPolicyModel) {
        final Map<String, String> ret = new LinkedHashMap<>();
        for (final Entry<AxArtifactKey, AxTask> taskentry : apexPolicyModel.getTasks().getTaskMap().entrySet()) {
            final AxTask task = taskentry.getValue();
            final String tasklogic = task.getTaskLogic().getLogic();
            final String tasklogicflavour = task.getTaskLogic().getLogicFlavour();
            assertTrue("No/Blank logic found in task " + taskentry.getKey(), (tasklogic != null && tasklogic.trim().length() > 0));
            assertNotNull("Logic Type \"" + tasklogicflavour + "\" in task " + taskentry.getKey() + " is not supported in this test",
                    LOGICEXTENSIONS.get(tasklogicflavour.toUpperCase()));
            final String filename = taskentry.getKey().getName() + "." + LOGICEXTENSIONS.get(tasklogicflavour.toUpperCase());
            ret.put(filename, tasklogic);
        }
        return ret;
    }
}
