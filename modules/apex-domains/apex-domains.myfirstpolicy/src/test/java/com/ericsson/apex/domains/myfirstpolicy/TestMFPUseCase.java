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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineImpl;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.domains.myfirstpolicy.model.MFPDomainModelFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.plugins.executor.javascript.JavascriptExecutorParameters;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Test MyFirstPolicy Use Case.
 */
public class TestMFPUseCase {
    // CHECKSTYLE:OFF: MagicNumber

    private static ApexEngineImpl apexEngine;

    /**
     * Test MFP use case setup.
     */
    @BeforeClass
    public static void testMFPUseCaseSetup() {
        final AxArtifactKey key = new AxArtifactKey("MyFirstPolicyApexEngine", "0.0.1");
        final EngineParameters parameters = new EngineParameters();
        parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());
        parameters.getExecutorParameterMap().put("JAVASCRIPT", new JavascriptExecutorParameters());
        apexEngine = (ApexEngineImpl) new ApexEngineFactory().createApexEngine(key);
    }

    /**
     * Test MyFirstPolicy#1 use case.
     *
     * @throws ApexException if there is an Apex error
     * @throws InterruptedException if there is an Interruption.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testMFP1Case() throws ApexException, InterruptedException, IOException {
        final AxPolicyModel apexPolicyModel = new MFPDomainModelFactory().getMFP1PolicyModel();
        assertNotNull(apexPolicyModel);

        final TestSaleAuthListener listener = new TestSaleAuthListener("Test");
        apexEngine.addEventListener("listener", listener);
        apexEngine.updateModel(apexPolicyModel);
        apexEngine.start();

        final AxEvent axEventin = apexPolicyModel.getEvents().get(new AxArtifactKey("SALE_INPUT:0.0.1"));
        assertNotNull(axEventin);
        final AxEvent axEventout = apexPolicyModel.getEvents().get(new AxArtifactKey("SALE_AUTH:0.0.1"));
        assertNotNull(axEventout);

        EnEvent event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_BoozeItem_084106GMT.json");
        apexEngine.handleEvent(event);
        EnEvent resultout = listener.getResult();
        EnEvent resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_BoozeItem_084106GMT.json");
        assertEquals(resulexpected, resultout);

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_BoozeItem_201713GMT.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_BoozeItem_201713GMT.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_NonBoozeItem_101309GMT.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_NonBoozeItem_101309GMT.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        apexEngine.stop();
    }

    /**
     * Test MyFirstPolicy#2 use case.
     *
     * @throws ApexException if there is an Apex error
     * @throws InterruptedException if there is an Interruption.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testMFP2Case() throws ApexException, InterruptedException, IOException {
        final AxPolicyModel apexPolicyModel = new MFPDomainModelFactory().getMFP2PolicyModel();
        assertNotNull(apexPolicyModel);

        final TestSaleAuthListener listener = new TestSaleAuthListener("Test");
        apexEngine.addEventListener("listener", listener);
        apexEngine.updateModel(apexPolicyModel);
        apexEngine.start();

        final AxEvent axEventin = apexPolicyModel.getEvents().get(new AxArtifactKey("SALE_INPUT:0.0.1"));
        assertNotNull(axEventin);
        final AxEvent axEventout = apexPolicyModel.getEvents().get(new AxArtifactKey("SALE_AUTH:0.0.1"));
        assertNotNull(axEventout);

        EnEvent event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_BoozeItem_084106GMT.json");
        apexEngine.handleEvent(event);
        EnEvent resultout = listener.getResult();
        EnEvent resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_BoozeItem_084106GMT.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_BoozeItem_201713GMT.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_BoozeItem_201713GMT.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/1/EventIn_NonBoozeItem_101309GMT.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/1/EventOut_NonBoozeItem_101309GMT.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/2/EventIn_BoozeItem_101433CET_thurs.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/2/EventOut_BoozeItem_101433CET_thurs.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/2/EventIn_BoozeItem_171937CET_sun.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/2/EventOut_BoozeItem_171937CET_sun.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());

        event = fillTriggerEvent(axEventin, "examples/events/MyFirstPolicy/2/EventIn_NonBoozeItem_111309CET_mon.json");
        apexEngine.handleEvent(event);
        resultout = listener.getResult();
        resulexpected = fillResultEvent(axEventout, "examples/events/MyFirstPolicy/2/EventOut_NonBoozeItem_111309CET_mon.json");
        assertEquals(resulexpected, resultout);
        assertEquals("ExecutionIDs are different", event.getExecutionID(), resultout.getExecutionID());
        
        apexEngine.stop();
    }

    /**
     * Fill trigger event for test.
     *
     * @param event the event
     * @param inputFile the input file
     * @return the filled event
     */
    private EnEvent fillTriggerEvent(final AxEvent event, final String inputFile) {
        final EnEvent ret = new EnEvent(event.getKey());
        final GsonBuilder gb = new GsonBuilder();
        gb.serializeNulls().enableComplexMapKeySerialization();
        final JsonObject jsonObject = gb.create().fromJson(ResourceUtils.getResourceAsString(inputFile), JsonObject.class);
        assertNotNull(jsonObject);
        assertTrue(jsonObject.has("name"));
        assertTrue(ret.getName().equals(jsonObject.get("name").getAsString()));
        assertTrue(ret.getAxEvent().getKey().getName().equals(jsonObject.get("name").getAsString()));
        assertTrue(jsonObject.has("nameSpace"));
        assertTrue(ret.getAxEvent().getNameSpace().equals(jsonObject.get("nameSpace").getAsString()));
        assertTrue(jsonObject.has("version"));
        assertTrue(ret.getAxEvent().getKey().getVersion().equals(jsonObject.get("version").getAsString()));
        final List<String> reserved = Arrays.asList("name", "nameSpace", "version", "source", "target");
        for (final Map.Entry<String, ?> e : jsonObject.entrySet()) {
            if (reserved.contains(e.getKey())) {
                continue;
            }
            assertTrue("Event file " + inputFile + " has a field " + e.getKey() + " but this is not defined for " + event.getID(),
                    (event.getParameterMap().containsKey(e.getKey())));
            if (jsonObject.get(e.getKey()).isJsonNull()) {
                ret.put(e.getKey(), null);
            }
        }
        for (final AxField field : event.getFields()) {
            if (!field.getOptional()) {
                assertTrue("Event file " + inputFile + " is missing a mandatory field " + field.getKey().getLocalName() + " for " + event.getID(),
                        jsonObject.has(field.getKey().getLocalName()));
            }
            else{
                ret.put(field.getKey().getLocalName(), null);
            }
        }
        if (jsonObject.has("time") && !jsonObject.get("time").isJsonNull()) {
            ret.put("time", jsonObject.get("time").getAsLong());
        }
        if (jsonObject.has("sale_ID") && !jsonObject.get("sale_ID").isJsonNull()) {
            ret.put("sale_ID", jsonObject.get("sale_ID").getAsLong());
        }
        if (jsonObject.has("amount") && !jsonObject.get("amount").isJsonNull()) {
            ret.put("amount", jsonObject.get("amount").getAsDouble());
        }
        if (jsonObject.has("item_ID") && !jsonObject.get("item_ID").isJsonNull()) {
            ret.put("item_ID", jsonObject.get("item_ID").getAsLong());
        }
        if (jsonObject.has("quantity") && !jsonObject.get("quantity").isJsonNull()) {
            ret.put("quantity", jsonObject.get("quantity").getAsInt());
        }
        if (jsonObject.has("assistant_ID") && !jsonObject.get("assistant_ID").isJsonNull()) {
            ret.put("assistant_ID", jsonObject.get("assistant_ID").getAsLong());
        }
        if (jsonObject.has("branch_ID") && !jsonObject.get("branch_ID").isJsonNull()) {
            ret.put("branch_ID", jsonObject.get("branch_ID").getAsLong());
        }
        if (jsonObject.has("notes") && !jsonObject.get("notes").isJsonNull()) {
            ret.put("notes", jsonObject.get("notes").getAsString());
        }
        return ret;
    }

    /**
     * Fill result event for test.
     *
     * @param event the event
     * @param inputFile the input file
     * @return the filled event
     */
    private EnEvent fillResultEvent(final AxEvent event, final String inputFile) {
        final EnEvent ret = new EnEvent(event.getKey());
        final GsonBuilder gb = new GsonBuilder();
        gb.serializeNulls().enableComplexMapKeySerialization();
        final JsonObject jsonObject = gb.create().fromJson(ResourceUtils.getResourceAsString(inputFile), JsonObject.class);
        assertNotNull(jsonObject);
        assertTrue(jsonObject.has("name"));
        assertTrue(ret.getName().equals(jsonObject.get("name").getAsString()));
        assertTrue(ret.getAxEvent().getKey().getName().equals(jsonObject.get("name").getAsString()));
        assertTrue(jsonObject.has("nameSpace"));
        assertTrue(ret.getAxEvent().getNameSpace().equals(jsonObject.get("nameSpace").getAsString()));
        assertTrue(jsonObject.has("version"));
        assertTrue(ret.getAxEvent().getKey().getVersion().equals(jsonObject.get("version").getAsString()));
        final List<String> reserved = Arrays.asList("name", "nameSpace", "version", "source", "target");
        for (final Map.Entry<String, ?> e : jsonObject.entrySet()) {
            if (reserved.contains(e.getKey())) {
                continue;
            }
            assertTrue("Event file " + inputFile + " has a field " + e.getKey() + " but this is not defined for " + event.getID(),
                    (event.getParameterMap().containsKey(e.getKey())));
            if (jsonObject.get(e.getKey()).isJsonNull()) {
                ret.put(e.getKey(), null);
            }
        }
        for (final AxField field : event.getFields()) {
            if (!field.getOptional()) {
                assertTrue("Event file " + inputFile + " is missing a mandatory field " + field.getKey().getLocalName() + " for " + event.getID(),
                        jsonObject.has(field.getKey().getLocalName()));
            }
            else{
                ret.put(field.getKey().getLocalName(), null);
            }
        }
        if (jsonObject.has("time") && !jsonObject.get("time").isJsonNull()) {
            ret.put("time", jsonObject.get("time").getAsLong());
        }
        if (jsonObject.has("sale_ID") && !jsonObject.get("sale_ID").isJsonNull()) {
            ret.put("sale_ID", jsonObject.get("sale_ID").getAsLong());
        }
        if (jsonObject.has("amount") && !jsonObject.get("amount").isJsonNull()) {
            ret.put("amount", jsonObject.get("amount").getAsDouble());
        }
        if (jsonObject.has("item_ID") && !jsonObject.get("item_ID").isJsonNull()) {
            ret.put("item_ID", jsonObject.get("item_ID").getAsLong());
        }
        if (jsonObject.has("quantity") && !jsonObject.get("quantity").isJsonNull()) {
            ret.put("quantity", jsonObject.get("quantity").getAsInt());
        }
        if (jsonObject.has("assistant_ID") && !jsonObject.get("assistant_ID").isJsonNull()) {
            ret.put("assistant_ID", jsonObject.get("assistant_ID").getAsLong());
        }
        if (jsonObject.has("branch_ID") && !jsonObject.get("branch_ID").isJsonNull()) {
            ret.put("branch_ID", jsonObject.get("branch_ID").getAsLong());
        }
        if (jsonObject.has("notes") && !jsonObject.get("notes").isJsonNull()) {
            ret.put("notes", jsonObject.get("notes").getAsString());
        }
        if (jsonObject.has("authorised") && !jsonObject.get("authorised").isJsonNull()) {
            ret.put("authorised", jsonObject.get("authorised").getAsString());
        }
        if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
            ret.put("message", jsonObject.get("message").getAsString());
        }
        return ret;
    }
}
