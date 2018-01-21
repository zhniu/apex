/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexEditorAPITask {
    @Test
    public void testTaskCRUD() {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.validateTask(null, null);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, result.getResult());

        result = apexModel.validateTask("%%%$£", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
   
        result = apexModel.createTask("@^^$^^$", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createTask("MyTask002", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createTask("MyTask002", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.createTask("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createTask("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.listTask(null, null);
        result = apexModel.createTask("MyTask002", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.createTask("MyTask012", "0.1.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteTask("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createTask("MyTask002", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.validateTask(null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.updateTask("@$$$£", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.updateTask("MyTask002", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTask("MyTask002", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateTask("MyTask002", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTask("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTask("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTask("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTask("MyTask015", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateTask("MyTask014", "0.1.5", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result = apexModel.listTask("£@£@@£@£", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.listTask(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTask("MyTask012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTask("MyTask012", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTask("MyTask012", "0.2.5");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTask("MyTask014", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTask("@£££@", "0.1.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTask("MyTask012", "0.1.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTask("MyTask012oooo", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listTask("MyTask012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 2);

        result = apexModel.deleteTask("MyTask012", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listTask("MyTask012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 1);

        result = apexModel.deleteTask("MyTask012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listTask("MyTask012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.updateTaskLogic("MyTask002", null, "NewLogic00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskLogic("MyTask123", null, "NewLogic00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskLogic("MyTask002", "4.5.6", "NewLogic00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskLogic("MyTask002", "0.1.4", "NewLogic00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskLogic("MyTask002", "0.0.2", "NewLogic00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result=apexModel.createTaskLogic("MyTask002", "0.0.2", "UNDEFINED", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskLogic("MyTask002", "0.0.2", "MVEL", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskLogic("MyTask002", "0.0.2", "JAVA", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskLogic("MyTask002", "0.0.2", "JYTHON", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskLogic("MyTask002", null, "JAVASCRIPT", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskLogic("MyTask002", null, "JRUBY", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.updateTaskLogic("MyTask002", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTaskLogic("MyTask002", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateTaskLogic("MyTask002", "0.0.2", "", "Some Other Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.updateTaskLogic("MyTask002", "0.0.2", "MVEL", "Some Other Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTaskLogic("MyTask012", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateTaskLogic("MyTask002", null, null, "Some Other Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTaskLogic("MyTask002", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateTaskLogic("MyTask015", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateTaskLogic("MyTask014", "0.1.5", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result = apexModel.listTaskLogic("MyTask002", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic("MyTask002", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        result = apexModel.deleteTaskLogic("@£@£@£", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTaskLogic("NonExistantTask", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        assertEquals(apexModel.listTaskLogic("MyTask002", null).getMessages().size(), 1);
        result = apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic("MyTask002", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskLogic("MyTask002", null).getMessages().size(), 1);
        result=apexModel.createTaskLogic("MyTask002", null, "JRUBY", "Some Task Logic");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskLogic("MyTask002", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.deleteTaskLogic("MyTask002", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskInputField("MyTask123", null, "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskInputField("MyTask002", "4.5.6", "NewField00", null, null, true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskInputField("MyTask002", "0.1.4", "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskInputField("MyTask002", "0.0.2", "NewField00", null, null, true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
   
        result=apexModel.createTaskInputField("MyTask002", "0.0.2", "NewField00", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskInputField("MyTask002", "0.0.2", "NewField00", "eventContextItem0", null, true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskInputField("MyTask002", "0.0.2", "NewField01", "eventContextItem0", "0.0.1", false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskInputField("MyTask002", "0.0.2", "NewField02", "eventContextItem0", "0.0.2", true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskInputField("MyTask002", null, "NewField02", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskInputField("MyTask002", null, "NewField03", "eventContextItem0", null, true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
   
        result = apexModel.listTaskInputField("@£$%", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.listTaskInputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskInputField("MyTask002", "0.0.1", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskInputField("MyTask002", "0.0.2", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskInputField("MyTask002", "0.0.2", "NewField01");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskInputField("MyTask002", "0.0.2", "NewField02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskInputField("MyTask002", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTaskInputField("@£$%", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTaskInputField("NonExistantTask", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskInputField("MyTask002", null, null).getMessages().size(), 4);
        result = apexModel.deleteTaskInputField("MyTask002", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskInputField("MyTask002", null, null).getMessages().size(), 4);
        result = apexModel.deleteTaskInputField("MyTask002", null, "NewField02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(apexModel.listTaskInputField("MyTask002", null, null).getMessages().size(), 3);
        result = apexModel.deleteTaskInputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskInputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskInputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskOutputField("MyTask123", null, "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskOutputField("MyTask002", "4.5.6", "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskOutputField("MyTask002", "0.1.4", "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskOutputField("MyTask002", "0.0.2", "NewField00", null, null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
   
        result=apexModel.createTaskOutputField("MyTask002", "0.0.2", "NewField00", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskOutputField("MyTask002", "0.0.2", "NewField00", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskOutputField("MyTask002", "0.0.2", "NewField01", "eventContextItem0", "0.0.1", false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskOutputField("MyTask002", "0.0.2", "NewField02", "eventContextItem0", "0.0.2", false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskOutputField("MyTask002", null, "NewField02", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskOutputField("MyTask002", null, "NewField03", "eventContextItem0", null, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
   
        result = apexModel.listTaskOutputField("@£$%", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.listTaskOutputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskOutputField("MyTask002", "0.0.1", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskOutputField("MyTask002", "0.0.2", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskOutputField("MyTask002", "0.0.2", "NewField01");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskOutputField("MyTask002", "0.0.2", "NewField02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskOutputField("MyTask002", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTaskOutputField("@£$%", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTaskOutputField("NonExistantTask", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskOutputField("MyTask002", null, null).getMessages().size(), 4);
        result = apexModel.deleteTaskOutputField("MyTask002", "0.0.2", "NewField04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskOutputField("MyTask002", null, null).getMessages().size(), 4);
        result = apexModel.deleteTaskOutputField("MyTask002", null, "NewField02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(apexModel.listTaskOutputField("MyTask002", null, null).getMessages().size(), 3);
        result = apexModel.deleteTaskOutputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskOutputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskOutputField("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskParameter("MyTask123", null, "NewPar00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskParameter("MyTask002", "4.5.6", "NewPar00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
   
        result=apexModel.createTaskParameter("MyTask002", "0.1.4", "NewPar00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
   
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar00", "eventContextItem0");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar00", "eventContextItem0");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar01", "eventContextItem0");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar02", "eventContextItem0");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar02", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskParameter("MyTask002", "0.0.2", "NewPar03", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result=apexModel.createTaskParameter("MyTask002", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result=apexModel.createTaskParameter("MyTask002", null, null, "Default value");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
   
        result = apexModel.listTaskParameter("@£$%", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.listTaskParameter("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskParameter("MyTask002", "0.0.3", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskParameter("MyTask002", "0.0.2", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskParameter("MyTask002", "0.0.2", "NewPar01");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskParameter("MyTask002", "0.0.2", "NewPar02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskParameter("MyTask002", "0.0.2", "NewPar04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTaskParameter("@£$%", "0.0.2", "NewPar04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTaskParameter("NonExistantTask", "0.0.2", "NewPar04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskParameter("MyTask002", null, null).getMessages().size(), 3);
        result = apexModel.deleteTaskParameter("MyTask002", "0.0.2", "NewPar04");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskParameter("MyTask002", null, null).getMessages().size(), 3);
        result = apexModel.deleteTaskParameter("MyTask002", null, "NewPar02");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(apexModel.listTaskParameter("MyTask002", null, null).getMessages().size(), 2);
        result = apexModel.deleteTaskParameter("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listTaskParameter("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskParameter("MyTask002", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskContextRef("@£$$", null, "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));   
        result=apexModel.createTaskContextRef("MyTask123", null, "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));   
        result=apexModel.createTaskContextRef("MyTask123", null, "AContextMap00", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask123", null, "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask002", "4.5.6", "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask002", "0.1.4", "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "AContextMap00", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum2", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum1", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result=apexModel.createTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result=apexModel.createTaskContextRef("MyTask002", null, "contextAlbum0", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
   
        result = apexModel.listTaskContextRef("@£$%", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.listTaskContextRef("MyTask002", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 2);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 2);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "AContextMap04", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum0", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum1", "0.0.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.listTaskContextRef("MyTask002", "0.0.2", "contextAlbum1", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTaskContextRef("@£$%", "0.0.2", "AContextMap04", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.deleteTaskContextRef("NonExistantTask", "0.0.2", "AContextMap04", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        assertEquals(apexModel.listTaskContextRef("MyTask002", null, null, null).getMessages().size(), 2);
        result = apexModel.deleteTaskContextRef("MyTask002", "0.0.2", "AContextMap04", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskContextRef("MyTask002", null, "contextAlbum0", "0.0.3");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskContextRef("MyTask002", null, "contextAlbum0", "0.1.5");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.deleteTaskContextRef("MyTask002", null, "contextAlbum0", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.deleteTaskContextRef("MyTask002", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(result.getMessages().size(), 1);
        result = apexModel.listTaskContextRef("MyTask002", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteTask(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(2, result.getMessages().size());

        result = apexModel.listTask(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(0, result.getMessages().size());
    }
}
