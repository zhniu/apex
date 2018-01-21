/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi.impl;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.eventmodel.concepts.AxInputField;
import com.ericsson.apex.model.eventmodel.concepts.AxOutputField;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.model.policymodel.concepts.AxTaskLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTaskParameter;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class acts as a facade for operations towards a policy model for task operations.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TaskFacade {
    // Apex model we're working towards
    private ApexModel apexModel;

    // Properties to use for the model
    private Properties apexProperties;

    // Facade classes for working towards the real Apex model
    private KeyInformationFacade keyInformationFacade;

    // JSON output on list/delete if set
    private boolean jsonMode;

    /**
     * Constructor that creates a task facade for the Apex Model API.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public TaskFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;

        keyInformationFacade = new KeyInformationFacade(apexModel, apexProperties, jsonMode);
    }

    /**
     * Create a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the default version
     * @param uuid task UUID, set to null to generate a UUID
     * @param description task description, set to null to generate a description
     * @return result of the operation
     */
    public ApexAPIResult createTask(final String name, final String version, final String uuid, final String description) {
        try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                key.setVersion(apexProperties.getProperty("DEFAULT_CONCEPT_VERSION"));
            }

            if (apexModel.getPolicyModel().getTasks().getTaskMap().containsKey(key)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + key.getID() + " already exists");
            }

            apexModel.getPolicyModel().getTasks().getTaskMap().put(key, new AxTask(key));

            if (apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().containsKey(key)) {
                return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
            }
            else {
                return keyInformationFacade.createKeyInformation(name, version, uuid, description);
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Update a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param uuid task UUID, set to null to not update
     * @param description task description, set to null to not update
     * @return result of the operation
     */
    public ApexAPIResult updateTask(final String name, final String version, final String uuid, final String description) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List tasks.
     *
     * @param name name of the task, set to null to list all
     * @param version starting version of the task, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult listTask(final String name, final String version) {
        try {
            Set<AxTask> taskSet = apexModel.getPolicyModel().getTasks().getAll(name, version);
            if (name != null && taskSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxTask task : taskSet) {
                result.addMessage(new ApexModelStringWriter<AxTask>(false).writeString(task, AxTask.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @return result of the operation
     */
    public ApexAPIResult deleteTask(final String name, final String version) {
        try {
            if (version != null) {
                AxArtifactKey key = new AxArtifactKey(name, version);
                AxTask removedTask = apexModel.getPolicyModel().getTasks().getTaskMap().remove(key);
                if (removedTask != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxTask>(false).writeString(removedTask, AxTask.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + key.getID() + " does not exist");
                }
            }

            Set<AxTask> taskSet = apexModel.getPolicyModel().getTasks().getAll(name, version);
            if (taskSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxTask task : taskSet) {
                result.addMessage(new ApexModelStringWriter<AxTask>(false).writeString(task, AxTask.class, jsonMode));
                apexModel.getPolicyModel().getTasks().getTaskMap().remove(task.getKey());
                keyInformationFacade.deleteKeyInformation(name, version);
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Validate tasks.
     *
     * @param name name of the task, set to null to list all
     * @param version starting version of the task, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult validateTask(final String name, final String version) {
        try {
            Set<AxTask> taskSet = apexModel.getPolicyModel().getTasks().getAll(name, version);
            if (taskSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxTask task : taskSet) {
                AxValidationResult validationResult = task.validate(new AxValidationResult());
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(task.getKey(), AxArtifactKey.class, jsonMode));
                result.addMessage(validationResult.toString());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Create logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param logicFlavour the task logic flavour for the task, set to null to use the default task logic flavour
     * @param logic the source code for the logic of the task
     * @return result of the operation
     */
    public ApexAPIResult createTaskLogic(final String name, final String version, final String logicFlavour, final String logic) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            // There is only one logic item associated with a task so we use a hard coded logic name
            AxReferenceKey refKey = new AxReferenceKey(task.getKey(), "TaskLogic");

            if (!task.getTaskLogic().getKey().getLocalName().equals(AxKey.NULL_KEY_NAME)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + refKey.getID() + " already exists");
            }

            task.setTaskLogic(new AxTaskLogic(refKey, logicFlavour, logic));
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Update logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param logicFlavour the task logic flavour for the task, set to null to not update
     * @param logic the source code for the logic of the task, set to null to not update
     * @return result of the operation
     */
    public ApexAPIResult updateTaskLogic(final String name, final String version, final String logicFlavour, final String logic) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (task.getTaskLogic().getKey().getLocalName().equals(AxKey.NULL_KEY_NAME)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + task.getTaskLogic().getKey().getID() + " does not exist");
            }

            AxTaskLogic taskLogic = task.getTaskLogic();
            if (logicFlavour != null) {
                taskLogic.setLogicFlavour(logicFlavour);
            }
            if (logic != null) {
                taskLogic.setLogic(logic);
            }

            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List task logic.
     *
     * @param name name of the task
     * @param version version of the task, set to null to list the latest version
     * @return result of the operation
     */
    public ApexAPIResult listTaskLogic(final String name, final String version) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                    new ApexModelStringWriter<AxTaskLogic>(false).writeString(task.getTaskLogic(), AxTaskLogic.class, jsonMode));
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @return result of the operation
     */
    public ApexAPIResult deleteTaskLogic(final String name, final String version) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (task.getTaskLogic().getKey().getLocalName().equals(AxKey.NULL_KEY_NAME)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + task.getTaskLogic().getKey().getID() + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            result.addMessage(new ApexModelStringWriter<AxTaskLogic>(false).writeString(task.getTaskLogic(), AxTaskLogic.class, jsonMode));
            task.setTaskLogic(new AxTaskLogic());
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Create a task input field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the input field
     * @param contextSchemaName name of the input field context schema
     * @param contextSchemaVersion version of the input field context schema, set to null to use the latest version
     * @param optional true if the task field is optional, false otherwise
     * @return result of the operation
     */
    public ApexAPIResult createTaskInputField(final String name, final String version, final String fieldName, final String contextSchemaName,
            final String contextSchemaVersion, final boolean optional) {
        try {
            Assertions.argumentNotNull(fieldName, "fieldName may not be null");

            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            AxReferenceKey refKey = new AxReferenceKey(task.getKey().getName(), task.getKey().getVersion(), "inputFields", fieldName);

            if (task.getInputFields().containsKey(refKey.getLocalName())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + refKey.getID() + " already exists");
            }

            AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(contextSchemaName, contextSchemaVersion);
            if (schema == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextSchemaName + ':' + contextSchemaVersion + " does not exist");
            }

            task.getInputFields().put(refKey.getLocalName(), new AxInputField(refKey, schema.getKey(), optional));
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List task input fields.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName field name of the input field, set to null to list all input fields of the task
     * @return result of the operation
     */
    public ApexAPIResult listTaskInputField(final String name, final String version, final String fieldName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (fieldName != null) {
                AxInputField inputField = task.getInputFields().get(fieldName);
                if (inputField != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxInputField>(false).writeString(inputField, AxInputField.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + inputField + " does not exist");
                }
            }
            else {
                if (task.getInputFields().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no input fields defined on task " + task.getKey().getID());
                }

                ApexAPIResult result = new ApexAPIResult();
                for (AxInputField field : task.getInputFields().values()) {
                    result.addMessage(new ApexModelStringWriter<AxInputField>(false).writeString(field, AxInputField.class, jsonMode));
                }
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }

    }

    /**
     * Delete a task input field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the input field, set to null to delete all input fields
     * @return result of the operation
     */
    public ApexAPIResult deleteTaskInputField(final String name, final String version, final String fieldName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            if (fieldName != null) {
                if (task.getInputFields().containsKey(fieldName)) {
                    result.addMessage(
                            new ApexModelStringWriter<AxInputField>(false).writeString(task.getInputFields().get(fieldName), AxInputField.class, jsonMode));
                    task.getInputFields().remove(fieldName);
                    return result;
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + fieldName + " does not exist");
                }
            }
            else {
                if (task.getInputFields().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no input fields defined on task " + task.getKey().getID());
                }

                for (AxInputField field : task.getInputFields().values()) {
                    result.addMessage(new ApexModelStringWriter<AxInputField>(false).writeString(field, AxInputField.class, jsonMode));
                }
                task.getInputFields().clear();
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }

    }

    /**
     * Create a task output field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the output field
     * @param contextSchemaName name of the output field context schema
     * @param contextSchemaVersion version of the output field context schema, set to null to use the latest version
     * @param optional true if the task field is optional, false otherwise
     * @return result of the operation
     */
    public ApexAPIResult createTaskOutputField(final String name, final String version, final String fieldName, final String contextSchemaName,
            final String contextSchemaVersion, final boolean optional) {
        try {
            Assertions.argumentNotNull(fieldName, "fieldName may not be null");

            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            AxReferenceKey refKey = new AxReferenceKey(task.getKey().getName(), task.getKey().getVersion(), "outputFields", fieldName);

            if (task.getOutputFields().containsKey(refKey.getLocalName())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + refKey.getID() + " already exists");
            }

            AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(contextSchemaName, contextSchemaVersion);
            if (schema == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextSchemaName + ':' + contextSchemaVersion + " does not exist");
            }

            task.getOutputFields().put(refKey.getLocalName(), new AxOutputField(refKey, schema.getKey(), optional));
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List task output fields.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName field name of the output field, set to null to list all output fields of the task
     * @return result of the operation
     */
    public ApexAPIResult listTaskOutputField(final String name, final String version, final String fieldName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (fieldName != null) {
                AxOutputField outputField = task.getOutputFields().get(fieldName);
                if (outputField != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxOutputField>(false).writeString(outputField, AxOutputField.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + outputField + " does not exist");
                }
            }
            else {
                if (task.getOutputFields().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no output fields defined on task " + task.getKey().getID());
                }

                ApexAPIResult result = new ApexAPIResult();
                for (AxOutputField field : task.getOutputFields().values()) {
                    result.addMessage(new ApexModelStringWriter<AxOutputField>(false).writeString(field, AxOutputField.class, jsonMode));
                }
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a task output field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the output field, set to null to delete all output fields
     * @return result of the operation
     */
    public ApexAPIResult deleteTaskOutputField(final String name, final String version, final String fieldName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            if (fieldName != null) {
                if (task.getOutputFields().containsKey(fieldName)) {
                    result.addMessage(
                            new ApexModelStringWriter<AxOutputField>(false).writeString(task.getOutputFields().get(fieldName), AxOutputField.class, jsonMode));
                    task.getOutputFields().remove(fieldName);
                    return result;
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + fieldName + " does not exist");
                }
            }
            else {
                if (task.getOutputFields().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no output fields defined on task " + task.getKey().getID());
                }

                for (AxOutputField field : task.getOutputFields().values()) {
                    result.addMessage(new ApexModelStringWriter<AxOutputField>(false).writeString(field, AxOutputField.class, jsonMode));
                }
                task.getOutputFields().clear();
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Create a task parameter.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName of the parameter
     * @param defaultValue of the parameter
     * @return result of the operation
     */
    public ApexAPIResult createTaskParameter(final String name, final String version, final String parName, final String defaultValue) {
        try {
            Assertions.argumentNotNull(parName, "parName may not be null");

            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            AxReferenceKey refKey = new AxReferenceKey(task.getKey(), parName);

            if (task.getTaskParameters().containsKey(refKey.getLocalName())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + refKey.getID() + " already exists");
            }

            task.getTaskParameters().put(refKey.getLocalName(), new AxTaskParameter(refKey, defaultValue));
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List task parameters.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName name of the parameter, set to null to list all parameters of the task
     * @return result of the operation
     */
    public ApexAPIResult listTaskParameter(final String name, final String version, final String parName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (parName != null) {
                AxTaskParameter taskParameter = task.getTaskParameters().get(parName);
                if (taskParameter != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxTaskParameter>(false).writeString(taskParameter, AxTaskParameter.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + taskParameter + " does not exist");
                }
            }
            else {
                if (task.getTaskParameters().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no task parameters defined on task " + task.getKey().getID());
                }

                ApexAPIResult result = new ApexAPIResult();
                for (AxTaskParameter parameter : task.getTaskParameters().values()) {
                    result.addMessage(new ApexModelStringWriter<AxTaskParameter>(false).writeString(parameter, AxTaskParameter.class, jsonMode));
                }
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a task parameter.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName of the parameter, set to null to delete all task parameters
     * @return result of the operation
     */
    public ApexAPIResult deleteTaskParameter(final String name, final String version, final String parName) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            if (parName != null) {
                if (task.getTaskParameters().containsKey(parName)) {
                    result.addMessage(new ApexModelStringWriter<AxTaskParameter>(false).writeString(task.getTaskParameters().get(parName),
                            AxTaskParameter.class, jsonMode));
                    task.getTaskParameters().remove(parName);
                    return result;
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + parName + " does not exist");
                }
            }
            else {
                if (task.getTaskParameters().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no task parameters defined on task " + task.getKey().getID());
                }

                for (AxTaskParameter parameter : task.getTaskParameters().values()) {
                    result.addMessage(new ApexModelStringWriter<AxTaskParameter>(false).writeString(parameter, AxTaskParameter.class, jsonMode));
                }
                task.getTaskParameters().clear();
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Create a task context album reference.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    public ApexAPIResult createTaskContextRef(final String name, final String version, final String contextAlbumName, final String contextAlbumVersion) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            AxContextAlbum contextAlbum = apexModel.getPolicyModel().getAlbums().get(contextAlbumName, contextAlbumVersion);
            if (contextAlbum == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextAlbumName + ':' + contextAlbumVersion + " does not exist");
            }

            if (task.getContextAlbumReferences().contains(contextAlbum.getKey())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS,
                        "context album reference for concept " + contextAlbum.getKey().getID() + " already exists in task");
            }

            task.getContextAlbumReferences().add(contextAlbum.getKey());
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List task context album references.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference, set to null to list all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    public ApexAPIResult listTaskContextRef(final String name, final String version, final String contextAlbumName, final String contextAlbumVersion) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            boolean found = false;
            for (AxArtifactKey albumKey : task.getContextAlbumReferences()) {
                if (contextAlbumName == null) {
                    result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(albumKey, AxArtifactKey.class, jsonMode));
                    found = true;
                    continue;
                }

                if (!albumKey.getName().equals(contextAlbumName)) {
                    continue;
                }

                if (contextAlbumVersion == null || albumKey.getVersion().equals(contextAlbumVersion)) {
                    result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(albumKey, AxArtifactKey.class, jsonMode));
                    found = true;
                }
            }
            if (!found) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextAlbumName + ':' + contextAlbumVersion + " does not exist");
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a task context album reference.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference, set to null to delete all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    public ApexAPIResult deleteTaskContextRef(final String name, final String version, final String contextAlbumName, final String contextAlbumVersion) {
        try {
            AxTask task = apexModel.getPolicyModel().getTasks().get(name, version);
            if (task == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            Set<AxArtifactKey> deleteSet = new TreeSet<AxArtifactKey>();

            for (AxArtifactKey albumKey : task.getContextAlbumReferences()) {
                if (contextAlbumName == null) {
                    deleteSet.add(albumKey);
                    continue;
                }

                if (!albumKey.getName().equals(contextAlbumName)) {
                    continue;
                }

                if (contextAlbumVersion == null || albumKey.getVersion().equals(contextAlbumVersion)) {
                    deleteSet.add(albumKey);
                }
            }

            if (deleteSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextAlbumName + ':' + contextAlbumVersion + " does not exist");
            }
            ApexAPIResult result = new ApexAPIResult();
            for (AxArtifactKey keyToDelete : deleteSet) {
                task.getContextAlbumReferences().remove(keyToDelete);
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(keyToDelete, AxArtifactKey.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }
}
