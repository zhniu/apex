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

import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.eventmodel.concepts.AxInputField;
import com.ericsson.apex.model.eventmodel.concepts.AxOutputField;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestTasks {

    @Test
    public void testTasks() {
        TreeMap<String, AxInputField>    ifEmptyMap   = new TreeMap<>();
        TreeMap<String, AxOutputField>   ofEmptyMap   = new TreeMap<>();
        TreeMap<String, AxTaskParameter> tpEmptyMap   = new TreeMap<>();
        TreeSet<AxArtifactKey>           ctxtEmptySet = new TreeSet<>();

        TreeMap<String, AxInputField>    ifMap   = new TreeMap<>();
        TreeMap<String, AxOutputField>   ofMap   = new TreeMap<>();
        TreeMap<String, AxTaskParameter> tpMap   = new TreeMap<>();
        TreeSet<AxArtifactKey>           ctxtSet = new TreeSet<>();

        assertNotNull(new AxTask());
        assertNotNull(new AxTask(new AxArtifactKey()));
        assertNotNull(new AxTask(new AxArtifactKey(), ifMap, ofMap, tpMap, ctxtSet, new AxTaskLogic()));

        AxTask task = new AxTask();

        AxArtifactKey taskKey = new AxArtifactKey("TaskName", "0.0.1");
        task.setKey(taskKey);
        assertEquals("TaskName:0.0.1", task.getKey().getID());
        assertEquals("TaskName:0.0.1", task.getKeys().get(0).getID());

        AxArtifactKey f0SchemaKey = new AxArtifactKey("FS0", "0.0.1");

        AxInputField    if0 = new AxInputField (new AxReferenceKey(taskKey, "IF0"), f0SchemaKey, false);
        AxInputField    if1 = new AxInputField (new AxReferenceKey(taskKey, "IF1"), f0SchemaKey, false);
        AxOutputField   of0 = new AxOutputField(new AxReferenceKey(taskKey, "OF0"), f0SchemaKey, false);
        AxOutputField   of1 = new AxOutputField(new AxReferenceKey(taskKey, "OF1"), f0SchemaKey, false);
        AxTaskParameter tp0 = new AxTaskParameter(new AxReferenceKey(taskKey, "TP0"), "DefaultValue");
        AxArtifactKey   cr0 = new AxArtifactKey("ContextReference", "0.0.1");
        AxTaskLogic     tl  = new AxTaskLogic(taskKey, "LogicName", "LogicFlavour", "Logic");
        
        ifMap.put(if0.getKey().getLocalName(), if0);
        ofMap.put(of0.getKey().getLocalName(), of0);
        tpMap.put(tp0.getKey().getLocalName(), tp0);
        ctxtSet.add(cr0);
        
        task.setInputFields(ifMap);
        assertEquals(ifMap, task.getInputFields());
        assertTrue(task.getInputFieldSet().contains(if0));
        assertTrue(task.getRawInputFields().keySet().contains(if0.getKey().getLocalName()));
        
        task.setOutputFields(ofMap);
        assertEquals(ofMap, task.getOutputFields());
        assertTrue(task.getOutputFieldSet().contains(of0));
        assertTrue(task.getRawOutputFields().keySet().contains(of0.getKey().getLocalName()));
        
        TreeMap<String, AxField> ifDupMap   = new TreeMap<>();
        TreeMap<String, AxField> ofDupMap   = new TreeMap<>();
        ifDupMap.put(if1.getKey().getLocalName(), if1);
        ofDupMap.put(of1.getKey().getLocalName(), of1);
        task.duplicateInputFields(ifDupMap);
        task.duplicateOutputFields(ofDupMap);
        assertTrue(ifMap.containsKey("IF1"));
        assertTrue(ofMap.containsKey("OF1"));
        
        task.setTaskParameters(tpMap);
        assertEquals(tpMap, task.getTaskParameters());
        
        task.setContextAlbumReferences(ctxtSet);
        assertEquals(ctxtSet, task.getContextAlbumReferences());
        
        task.setTaskLogic(tl);
        assertEquals(tl, task.getTaskLogic());
        
        task.setKey(taskKey);
        assertEquals("TaskName:0.0.1", task.getKey().getID());
        assertEquals("TaskName:0.0.1", task.getKeys().get(0).getID());
        
        task.afterUnmarshal(null, null);
        assertEquals(1, task.getTaskParameters().size());

        AxValidationResult result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        task.setKey(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        task.setKey(taskKey);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        task.setInputFields(ifEmptyMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        task.setInputFields(ifMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        ifMap.put("NullField", null);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        ifMap.remove("NullField");
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        task.setOutputFields(ofEmptyMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        task.setOutputFields(ofMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        ofMap.put("NullField", null);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        ofMap.remove("NullField");
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        // Empty task parameter map is OK
        task.setTaskParameters(tpEmptyMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        task.setTaskParameters(tpMap);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        tpMap.put("NullField", null);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        tpMap.remove("NullField");
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        // Empty context reference set is OK
        task.setContextAlbumReferences(ctxtEmptySet);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        task.setContextAlbumReferences(ctxtSet);
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        ctxtSet.add(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        ctxtSet.remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = task.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        task.clean();

        AxTask clonedTask = (AxTask) task.clone();
        assertEquals("AxTask:(key=AxArtifactKey:(name=TaskName", clonedTask.toString().substring(0, 40));

        assertFalse(task.hashCode() == 0);

        assertTrue(task.equals(task));
        assertTrue(task.equals(clonedTask));
        assertFalse(task.equals(null));
        assertFalse(task.equals("Hello"));
        assertFalse(task.equals(new AxTask(new AxArtifactKey(), ifMap, ofMap, tpMap, ctxtSet, tl)));
        assertFalse(task.equals(new AxTask(taskKey, ifEmptyMap, ofMap, tpMap, ctxtSet, tl)));
        assertFalse(task.equals(new AxTask(taskKey, ifMap, ofEmptyMap, tpMap, ctxtSet, tl)));
        assertFalse(task.equals(new AxTask(taskKey, ifMap, ofMap, tpEmptyMap, ctxtSet, tl)));
        assertFalse(task.equals(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtEmptySet, tl)));
        assertFalse(task.equals(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtSet, new AxTaskLogic())));
        assertTrue(task.equals(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtSet, tl)));

        assertEquals(0, task.compareTo(task));
        assertEquals(0, task.compareTo(clonedTask));
        assertNotEquals(0, task.compareTo(new AxArtifactKey()));
        assertNotEquals(0, task.compareTo(null));
        assertNotEquals(0, task.compareTo(new AxTask(new AxArtifactKey(), ifMap, ofMap, tpMap, ctxtSet, tl)));
        assertNotEquals(0, task.compareTo(new AxTask(taskKey, ifEmptyMap, ofMap, tpMap, ctxtSet, tl)));
        assertNotEquals(0, task.compareTo(new AxTask(taskKey, ifMap, ofEmptyMap, tpMap, ctxtSet, tl)));
        assertNotEquals(0, task.compareTo(new AxTask(taskKey, ifMap, ofMap, tpEmptyMap, ctxtSet, tl)));
        assertNotEquals(0, task.compareTo(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtEmptySet, tl)));
        assertNotEquals(0, task.compareTo(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtSet, new AxTaskLogic())));
        assertEquals(0, task.compareTo(new AxTask(taskKey, ifMap, ofMap, tpMap, ctxtSet, tl)));

        assertNotNull(task.getKeys());
        
        AxTasks tasks = new AxTasks();
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        // Invalid, no tasks in task map
        tasks.setKey(new AxArtifactKey("TasksKey", "0.0.1"));
        assertEquals("TasksKey:0.0.1", tasks.getKey().getID());
        
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        tasks.getTaskMap().put(taskKey, task);
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        tasks.getTaskMap().put(AxArtifactKey.getNullKey(), null);
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        tasks.getTaskMap().remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        tasks.getTaskMap().put(new AxArtifactKey("NullValueKey", "0.0.1"), null);
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        tasks.getTaskMap().remove(new AxArtifactKey("NullValueKey", "0.0.1"));
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        tasks.getTaskMap().put(new AxArtifactKey("BadTaskKey", "0.0.1"), task);
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        tasks.getTaskMap().remove(new AxArtifactKey("BadTaskKey", "0.0.1"));
        result = new AxValidationResult();
        result = tasks.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        tasks.clean();
        tasks.afterUnmarshal(null, null);

        AxTasks clonedTasks = (AxTasks) tasks.clone();
        assertEquals("AxTasks:(key=AxArtifactKey:(name=TasksKey,version=0.0.1),tas", clonedTasks.toString().substring(0, 60));

        assertFalse(tasks.hashCode() == 0);

        assertTrue(tasks.equals(tasks));
        assertTrue(tasks.equals(clonedTasks));
        assertFalse(tasks.equals(null));
        assertFalse(tasks.equals("Hello"));
        assertFalse(tasks.equals(new AxTasks(new AxArtifactKey())));

        assertEquals(0, tasks.compareTo(tasks));
        assertEquals(0, tasks.compareTo(clonedTasks));
        assertNotEquals(0, tasks.compareTo(null));
        assertNotEquals(0, tasks.compareTo(new AxArtifactKey()));
        assertNotEquals(0, tasks.compareTo(new AxTasks(new AxArtifactKey())));

        clonedTasks.get(taskKey).getTaskLogic().setLogic("SomeChangedLogic");
        assertNotEquals(0, tasks.compareTo(clonedTasks));
        
        assertEquals(tasks.getKey(), tasks.getKeys().get(0));

        assertEquals("TaskName", tasks.get("TaskName").getKey().getName());
        assertEquals("TaskName", tasks.get("TaskName", "0.0.1").getKey().getName());
        assertEquals(1, tasks.getAll("TaskName", "0.0.1").size());
        assertEquals(0, tasks.getAll("NonExistantTaskName").size());
    }
}
