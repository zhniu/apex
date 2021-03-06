/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestState {

    @Test
    public void testState() {
        TreeMap<String, AxStateOutput>               soEmptyMap   = new TreeMap<>();
        TreeSet<AxArtifactKey>                       ctxtEmptySet = new TreeSet<>();
        TreeMap<String, AxStateFinalizerLogic>       sflEmptyMap  = new TreeMap<>();
        TreeMap<AxArtifactKey, AxStateTaskReference> trEmptyMap   = new TreeMap<>();

        TreeMap<String, AxStateOutput>               soMap    = new TreeMap<>();
        TreeSet<AxArtifactKey>                       ctxtSet  = new TreeSet<>();
        TreeMap<String, AxStateFinalizerLogic>       sflMap   = new TreeMap<>();
        TreeMap<AxArtifactKey, AxStateTaskReference> trMap    = new TreeMap<>();

        assertNotNull(new AxState());
        assertNotNull(new AxState(new AxReferenceKey()));
        assertNotNull(new AxState(new AxReferenceKey(), new AxArtifactKey(), soEmptyMap, ctxtEmptySet, new AxTaskSelectionLogic(), sflEmptyMap, new AxArtifactKey(), trEmptyMap));

        AxState state = new AxState();

        AxReferenceKey       stateKey     = new AxReferenceKey("PolicyName", "0.0.1", "StateName");
        AxReferenceKey       stateKeyNext = new AxReferenceKey("PolicyName", "0.0.1", "StateNameNext");
        AxReferenceKey       stateKeyBad  = new AxReferenceKey("PolicyName", "0.0.1", "BadStateName");
        AxArtifactKey        triggerKey   = new AxArtifactKey("TriggerName", "0.0.1");
        AxTaskSelectionLogic tsl          = new AxTaskSelectionLogic(stateKey, "TSL", "LogicFlavour", "Some Logic");
        AxArtifactKey        defTaskKey   = new AxArtifactKey("TaskName", "0.0.1");
        AxArtifactKey        taskKey1     = new AxArtifactKey("Task1", "0.0.1");
        AxArtifactKey        taskKey2     = new AxArtifactKey("Task2", "0.0.1");
        AxArtifactKey        taskKeyBad   = new AxArtifactKey("TaskBad", "0.0.1");
      
        try {
            state.setKey(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("key may not be null", e.getMessage());
        }

        state.setKey(stateKey);
        assertEquals("PolicyName:0.0.1:NULL:StateName", state.getKey().getID());
        assertEquals("PolicyName:0.0.1:NULL:StateName", state.getKeys().get(0).getID());

        AxStateOutput   so0 = new AxStateOutput(new AxReferenceKey(stateKey, "SO0"), triggerKey, new AxReferenceKey());
        AxStateOutput   soU = new AxStateOutput(new AxReferenceKey(stateKey, "SOU"), triggerKey, stateKeyNext);
        AxStateOutput   soSame = new AxStateOutput(new AxReferenceKey(stateKey, "SOU"), triggerKey, stateKey);
        AxArtifactKey   cr0 = new AxArtifactKey("ContextReference", "0.0.1");
        AxStateFinalizerLogic sfl   = new AxStateFinalizerLogic(stateKey, "SFLogicName", "LogicFlavour", "Logic");
        AxStateFinalizerLogic sflU  = new AxStateFinalizerLogic(stateKey, "UnusedSFLogicName", "LogicFlavour", "Logic");
        AxStateTaskReference  str0 = new AxStateTaskReference(new AxReferenceKey(stateKey, "STR0"), AxStateTaskOutputType.DIRECT, so0.getKey());
        AxStateTaskReference  str1 = new AxStateTaskReference(new AxReferenceKey(stateKey, "STR1"), AxStateTaskOutputType.DIRECT, so0.getKey());
        AxStateTaskReference  str2 = new AxStateTaskReference(new AxReferenceKey(stateKey, "STR2"), AxStateTaskOutputType.LOGIC,  sfl.getKey());

        AxStateTaskReference  strBadState = new AxStateTaskReference(new AxReferenceKey(stateKeyBad, "STR2"), AxStateTaskOutputType.LOGIC,      sfl.getKey());
        AxStateTaskReference  strBadSO    = new AxStateTaskReference(new AxReferenceKey(stateKey,    "STR2"), AxStateTaskOutputType.UNDEFINED,  sfl.getKey());
        AxStateTaskReference  strBadSFL   = new AxStateTaskReference(new AxReferenceKey(stateKeyBad, "STR2"), AxStateTaskOutputType.LOGIC,      new AxReferenceKey(stateKey, "SomeSFL"));
                
        soMap.put(so0.getKey().getLocalName(), so0);
        ctxtSet.add(cr0);
        sflMap.put(sfl.getKey().getLocalName(), sfl);
        trMap.put(defTaskKey.getKey(), str0);
        trMap.put(taskKey1.getKey(), str1);
        trMap.put(taskKey2.getKey(), str2);

        try {
            state.setTrigger(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("trigger may not be null", e.getMessage());
        }
        
        state.setTrigger(triggerKey);
        assertEquals(triggerKey, state.getTrigger());
        
        try {
            state.setStateOutputs(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("stateOutputs may not be null", e.getMessage());
        }
        
        state.setStateOutputs(soMap);
        assertEquals(soMap, state.getStateOutputs());
        
        try {
            state.setContextAlbumReferences(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("contextAlbumReferenceSet may not be null", e.getMessage());
        }
        
        state.setContextAlbumReferences(ctxtSet);
        assertEquals(ctxtSet, state.getContextAlbumReferences());
        
        try {
            state.setTaskSelectionLogic(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("taskSelectionLogic may not be null", e.getMessage());
        }
        
        assertEquals(false, state.checkSetTaskSelectionLogic());
        state.setTaskSelectionLogic(tsl);
        assertEquals(tsl, state.getTaskSelectionLogic());
        assertEquals(true, state.checkSetTaskSelectionLogic());
        
        try {
            state.setStateFinalizerLogicMap(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("stateFinalizerLogic may not be null", e.getMessage());
        }
        
        state.setStateFinalizerLogicMap(sflMap);
        assertEquals(sflMap, state.getStateFinalizerLogicMap());
        
        try {
            state.setDefaultTask(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("defaultTask may not be null", e.getMessage());
        }
        
        state.setDefaultTask(defTaskKey);
        assertEquals(defTaskKey, state.getDefaultTask());
        
        try {
            state.setTaskReferences(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("taskReferenceMap may not be null", e.getMessage());
        }
        
        state.setTaskReferences(trMap);
        assertEquals(trMap, state.getTaskReferences());
        
        state.afterUnmarshal(null, null);
        assertEquals(state.getKey(), state.getKeys().get(0));
        state.getTaskSelectionLogic().getKey().setLocalName(AxKey.NULL_KEY_NAME);
        state.afterUnmarshal(null, null);
        assertEquals(state.getKey(), state.getKeys().get(0));
        
        Set<String> stateSet = state.getNextStateSet();
        assertEquals(1, stateSet.size());
        
        AxValidationResult result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setKey(stateKey);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.setTrigger(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setTrigger(triggerKey);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.setStateOutputs(soEmptyMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        so0.getKey().setParentLocalName("Zooby");
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        so0.getKey().setParentLocalName("StateName");
        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        soMap.put("NullOutput", null);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        soMap.remove("NullOutput");
        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        soMap.put("DupOutput", so0);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        soMap.remove("DupOutput");
        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        soMap.put("UnusedOutput", soU);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.OBSERVATION, result.getValidationResult());

        soMap.remove("UnusedOutput");
        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        soMap.put("OutputToSameState", soSame);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        soMap.remove("OutputToSameState");
        state.setStateOutputs(soMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        // Empty context reference set is OK
        state.setContextAlbumReferences(ctxtEmptySet);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.setContextAlbumReferences(ctxtSet);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        ctxtSet.add(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        ctxtSet.remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
      
        // Null TSL is OK
        state.getTaskSelectionLogic().setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.getTaskSelectionLogic().setKey(new AxReferenceKey(stateKey, "TSL"));
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.setDefaultTask(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setDefaultTask(defTaskKey);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        state.setTaskReferences(trEmptyMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setTaskReferences(trMap);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        trMap.put(AxArtifactKey.getNullKey(), null);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        trMap.remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        trMap.put(AxArtifactKey.getNullKey(), str0);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        trMap.remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        trMap.put(taskKeyBad, strBadSO);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        trMap.remove(taskKeyBad);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        trMap.put(taskKeyBad, strBadState);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        trMap.remove(taskKeyBad);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        trMap.put(taskKeyBad, strBadSFL);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        trMap.remove(taskKeyBad);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        state.setDefaultTask(new AxArtifactKey("NonExistantTask", "0.0.1"));
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        state.setDefaultTask(defTaskKey);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        sflMap.put("NullSFL", null);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        sflMap.remove("NullSFL");
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        sflMap.put(sflU.getKey().getLocalName(), sflU);
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.OBSERVATION, result.getValidationResult());

        sflMap.remove(sflU.getKey().getLocalName());
        result = new AxValidationResult();
        result = state.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        state.clean();

        AxState clonedState = new AxState(state);
        assertEquals("AxState:(stateKey=AxReferenceKey:(parent", clonedState.toString().substring(0, 40));

        assertFalse(state.hashCode() == 0);

        assertTrue(state.equals(state));
        assertTrue(state.equals(clonedState));
        assertFalse(state.equals(null));
        assertFalse(state.equals("Hello"));
        assertFalse(state.equals(new AxState(new AxReferenceKey(), triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, new AxArtifactKey(), soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soEmptyMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtEmptySet, tsl, sflMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtSet, new AxTaskSelectionLogic(), sflMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflEmptyMap, defTaskKey, trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, new AxArtifactKey(), trMap)));
        assertFalse(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trEmptyMap)));
        assertTrue(state.equals(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));

        assertEquals(0, state.compareTo(state));
        assertEquals(0, state.compareTo(clonedState));
        assertNotEquals(0, state.compareTo(new AxArtifactKey()));
        assertNotEquals(0, state.compareTo(null));
        assertNotEquals(0, state.compareTo(new AxState(new AxReferenceKey(), triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, new AxArtifactKey(), soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soEmptyMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtEmptySet, tsl, sflMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtSet, new AxTaskSelectionLogic(), sflMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflEmptyMap, defTaskKey, trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, new AxArtifactKey(), trMap)));
        assertNotEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trEmptyMap)));
        assertEquals(0, state.compareTo(new AxState(stateKey, triggerKey, soMap, ctxtSet, tsl, sflMap, defTaskKey, trMap)));

        assertNotNull(state.getKeys());
    }
}
