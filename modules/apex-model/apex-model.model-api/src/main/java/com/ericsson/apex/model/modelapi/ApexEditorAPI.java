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

/**
 * The Interface ApexEditorAPI is used to manipulate Apex models.
 */
public interface ApexEditorAPI {
    /*
     * Model API Methods
     */

    /**
     * Create model.
     *
     * @param name name of the model
     * @param version version of the model, set to null to use the default version
     * @param uuid model UUID, set to null to generate a UUID
     * @param description model description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createModel(String name, String version, String uuid, String description);

    /**
     * Update model.
     *
     * @param name name of the model
     * @param version version of the model, set to null to update the latest version
     * @param uuid key information UUID, set to null to not update
     * @param description policy description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateModel(String name, String version, String uuid, String description);

    /**
     * Get the key of an Apex model.
     *
     * @return the result of the operation
     */
    ApexAPIResult getModelKey();

    /**
     * List an Apex model.
     *
     * @return the result of the operation
     */
    ApexAPIResult listModel();

    /**
     * Delete an Apex model, clear all the concepts in the model.
     *
     * @return the result of the operation
     */
    ApexAPIResult deleteModel();

    /*
     * Key Information API Methods
     */

    /**
     * Create key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to use the default version
     * @param uuid key information UUID, set to null to generate a UUID
     * @param description key information description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createKeyInformation(String name, String version, String uuid, String description);

    /**
     * Update key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to update the latest version
     * @param uuid key information UUID, set to null to not update
     * @param description key information description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateKeyInformation(String name, String version, String uuid, String description);

    /**
     * List key information.
     *
     * @param name name of the concept for the key information, set to null to list all
     * @param version starting version of the concept for the key information, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listKeyInformation(String name, String version);

    /**
     * Delete key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to delete all versions
     * @return result of the operation
     */
    ApexAPIResult deleteKeyInformation(String name, String version);

    /**
     * Validate key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to validate all versions
     * @return result of the operation
     */
    ApexAPIResult validateKeyInformation(String name, String version);

    /*
     * Context Schema API Methods
     */

    /**
     * Create a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to use the default version
     * @param schemaFlavour a string identifying the flavour of this context schema
     * @param schemaDefinition a string containing the definition of this context schema
     * @param uuid context schema UUID, set to null to generate a UUID
     * @param description context schema description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createContextSchema(String name, String version, String schemaFlavour, String schemaDefinition, String uuid, String description);

    /**
     * Update a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to update the latest version
     * @param schemaFlavour a string identifying the flavour of this context schema
     * @param schemaDefinition a string containing the definition of this context schema
     * @param uuid context schema UUID, set to null to not update
     * @param description context schema description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateContextSchema(String name, String version, String schemaFlavour, String schemaDefinition, String uuid, String description);

    /**
     * List context schemas.
     *
     * @param name name of the context schema, set to null to list all
     * @param version starting version of the context schema, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listContextSchemas(String name, String version);

    /**
     * Delete a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to delete all versions
     * @return result of the operation
     */
    ApexAPIResult deleteContextSchema(String name, String version);

    /**
     * Validate context schemas.
     *
     * @param name name of the context schema, set to null to list all
     * @param version starting version of the context schema, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult validateContextSchemas(String name, String version);

    /*
     * Event API Methods
     */

    /**
     * Create an event.
     *
     * @param name name of the event
     * @param version version of the event, set to null to use the default version
     * @param nameSpace of the event, set to null to use the default value
     * @param source of the event, set to null to use the default value
     * @param target of the event, set to null to use the default value
     * @param uuid event UUID, set to null to generate a UUID
     * @param description event description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createEvent(String name, String version, String nameSpace, String source, String target, String uuid, String description);

    /**
     * Update an event.
     *
     * @param name name of the event
     * @param version version of the event, set to null to use the latest version
     * @param nameSpace of the event, set to null to not update
     * @param source of the event, set to null to not update
     * @param target of the event, set to null to not update
     * @param uuid event UUID, set to null to not update
     * @param description event description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateEvent(String name, String version, String nameSpace, String source, String target, String uuid, String description);

    /**
     * List events.
     *
     * @param name name of the event, set to null to list all
     * @param version starting version of the event, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listEvent(String name, String version);

    /**
     * Delete an event.
     *
     * @param name name of the event
     * @param version version of the event, set to null to delete all versions
     * @return result of the operation
     */
    ApexAPIResult deleteEvent(String name, String version);

    /**
     * Validate events.
     *
     * @param name name of the event, set to null to list all
     * @param version starting version of the event, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult validateEvent(String name, String version);

    /**
     * Create an event parameter.
     *
     * @param name name of the event
     * @param version version of the event, set to null to use the latest version
     * @param parName of the parameter
     * @param contextSchemaName name of the parameter context schema
     * @param contextSchemaVersion version of the parameter context schema, set to null to use the latest version
     * @param optional true if the event parameter is optional, false otherwise
     * @return result of the operation
     */
    ApexAPIResult createEventPar(String name, String version, String parName, String contextSchemaName, String contextSchemaVersion, boolean optional);

    /**
     * List event parameters.
     *
     * @param name name of the event
     * @param version version of the event, set to null to list latest version
     * @param parName name of the parameter, set to null to list all parameters of the event
     * @return result of the operation
     */
    ApexAPIResult listEventPar(String name, String version, String parName);

    /**
     * Delete an event parameter.
     *
     * @param name name of the event
     * @param version version of the event, set to null to use the latest version
     * @param parName of the parameter, set to null to delete all parameters
     * @return result of the operation
     */
    ApexAPIResult deleteEventPar(String name, String version, String parName);

    /*
     * Context Album API Methods
     */

    /**
     * Create a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to use the default version
     * @param scope of the context album
     * @param writable "true" or "t" if the context album is writable, set to null or any other value for a read-only album
     * @param contextSchemaName name of the parameter context schema
     * @param contextSchemaVersion version of the parameter context schema, set to null to use the latest version
     * @param uuid context album UUID, set to null to generate a UUID
     * @param description context album description, set to null to generate a description
     * @return result of the operation
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
    ApexAPIResult createContextAlbum(String name, String version, String scope, String writable, String contextSchemaName, String contextSchemaVersion,
            String uuid, String description);
    // CHECKSTYLE:ON: checkstyle:parameterNumber

    /**
     * Update a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to use the default version
     * @param scope of the context album
     * @param writable "true" or "t" if the context album is writable, set to null or any other value for a read-only album
     * @param contextSchemaName name of the parameter context schema
     * @param contextSchemaVersion version of the parameter context schema, set to null to use the latest version
     * @param uuid context album UUID, set to null to generate a UUID
     * @param description context album description, set to null to generate a description
     * @return result of the operation
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
    ApexAPIResult updateContextAlbum(String name, String version, String scope, String writable, String contextSchemaName, String contextSchemaVersion,
            String uuid, String description);
    // CHECKSTYLE:ON: checkstyle:parameterNumber

    /**
     * List context albums.
     *
     * @param name name of the context album, set to null to list all
     * @param version starting version of the context album, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listContextAlbum(String name, String version);

    /**
     * Delete a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to delete versions
     * @return result of the operation
     */
    ApexAPIResult deleteContextAlbum(String name, String version);

    /**
     * Validate context albums.
     *
     * @param name name of the context album, set to null to list all
     * @param version starting version of the context album, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult validateContextAlbum(String name, String version);

    /*
     * Task API Methods
     */

    /**
     * Create a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the default version
     * @param uuid task UUID, set to null to generate a UUID
     * @param description task description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createTask(String name, String version, String uuid, String description);

    /**
     * Update a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param uuid task UUID, set to null to not update
     * @param description task description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateTask(String name, String version, String uuid, String description);

    /**
     * List tasks.
     *
     * @param name name of the task, set to null to list all
     * @param version starting version of the task, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listTask(String name, String version);

    /**
     * Delete a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deleteTask(String name, String version);

    /**
     * Validate tasks.
     *
     * @param name name of the task, set to null to list all
     * @param version starting version of the task, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult validateTask(String name, String version);

    /**
     * Create logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param logicFlavour the task logic flavour for the task, set to null to use the default task logic flavour
     * @param logic the source code for the logic of the task
     * @return result of the operation
     */
    ApexAPIResult createTaskLogic(String name, String version, String logicFlavour, String logic);

    /**
     * Update logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param logicFlavour the task logic flavour for the task, set to null to not update
     * @param logic the source code for the logic of the task, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updateTaskLogic(String name, String version, String logicFlavour, String logic);

    /**
     * List task logic.
     *
     * @param name name of the task
     * @param version version of the task, set to null to list the latest version
     * @return result of the operation
     */
    ApexAPIResult listTaskLogic(String name, String version);

    /**
     * Delete logic for a task.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deleteTaskLogic(String name, String version);

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
    ApexAPIResult createTaskInputField(String name, String version, String fieldName, String contextSchemaName, String contextSchemaVersion, boolean optional);

    /**
     * List task input fields.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName field name of the input field, set to null to list all input fields of the task
     * @return result of the operation
     */
    ApexAPIResult listTaskInputField(String name, String version, String fieldName);

    /**
     * Delete a task input field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the input field, set to null to delete all input fields
     * @return result of the operation
     */
    ApexAPIResult deleteTaskInputField(String name, String version, String fieldName);

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
    ApexAPIResult createTaskOutputField(String name, String version, String fieldName, String contextSchemaName, String contextSchemaVersion, boolean optional);

    /**
     * List task output fields.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName field name of the output field, set to null to list all output fields of the task
     * @return result of the operation
     */
    ApexAPIResult listTaskOutputField(String name, String version, String fieldName);

    /**
     * Delete a task output field.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param fieldName of the output field, set to null to delete all output fields
     * @return result of the operation
     */
    ApexAPIResult deleteTaskOutputField(String name, String version, String fieldName);

    /**
     * Create a task parameter.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName of the parameter
     * @param defaultValue of the parameter
     * @return result of the operation
     */
    ApexAPIResult createTaskParameter(String name, String version, String parName, String defaultValue);

    /**
     * List task parameters.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName name of the parameter, set to null to list all parameters of the task
     * @return result of the operation
     */
    ApexAPIResult listTaskParameter(String name, String version, String parName);

    /**
     * Delete a task parameter.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param parName of the parameter, set to null to delete all task parameters
     * @return result of the operation
     */
    ApexAPIResult deleteTaskParameter(String name, String version, String parName);

    /**
     * Create a task context album reference.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult createTaskContextRef(String name, String version, String contextAlbumName, String contextAlbumVersion);

    /**
     * List task context album references.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference, set to null to list all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult listTaskContextRef(String name, String version, String contextAlbumName, String contextAlbumVersion);

    /**
     * Delete a task context album reference.
     *
     * @param name name of the task
     * @param version version of the task, set to null to use the latest version
     * @param contextAlbumName name of the context album for the context album reference, set to null to delete all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deleteTaskContextRef(String name, String version, String contextAlbumName, String contextAlbumVersion);

    /*
     * Policy API Methods
     */

    /**
     * Create a policy.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the default version
     * @param template template used to create the policy, set to null to use the default template
     * @param firstState the first state of the policy
     * @param uuid policy UUID, set to null to generate a UUID
     * @param description policy description, set to null to generate a description
     * @return result of the operation
     */
    ApexAPIResult createPolicy(String name, String version, String template, String firstState, String uuid, String description);

    /**
     * Update a policy.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param template template used to create the policy, set to null to not update
     * @param firstState the first state of the policy
     * @param uuid policy UUID, set to null to not update
     * @param description policy description, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updatePolicy(String name, String version, String template, String firstState, String uuid, String description);

    /**
     * List policies.
     *
     * @param name name of the policy, set to null to list all
     * @param version starting version of the policy, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult listPolicy(String name, String version);

    /**
     * Delete a policy.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deletePolicy(String name, String version);

    /**
     * Validate policies.
     *
     * @param name name of the policy, set to null to list all
     * @param version starting version of the policy, set to null to list all versions
     * @return result of the operation
     */
    ApexAPIResult validatePolicy(String name, String version);

    /**
     * Create a policy state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param triggerName name of the trigger event for this state
     * @param triggerVersion version of the trigger event for this state, set to null to use the latest version
     * @param defaultTaskName the default task name
     * @param defaltTaskVersion the default task version, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult createPolicyState(String name, String version, String stateName, String triggerName, String triggerVersion, String defaultTaskName,
            String defaltTaskVersion);

    /**
     * Update a policy state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param triggerName name of the trigger event for this state, set to null to not update
     * @param triggerVersion version of the trigger event for this state, set to use latest version of trigger event
     * @param defaultTaskName the default task name, set to null to not update
     * @param defaltTaskVersion the default task version, set to use latest version of default task
     * @return result of the operation
     */
    ApexAPIResult updatePolicyState(String name, String version, String stateName, String triggerName, String triggerVersion, String defaultTaskName,
            String defaltTaskVersion);

    /**
     * List policy states.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state, set to null to list all states of the policy
     * @return result of the operation
     */
    ApexAPIResult listPolicyState(String name, String version, String stateName);

    /**
     * Delete a policy state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state, set to null to delete all states
     * @return result of the operation
     */
    ApexAPIResult deletePolicyState(String name, String version, String stateName);

    /**
     * Create task selection logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param logicFlavour the task selection logic flavour for the state, set to null to use the default task logic flavour
     * @param logic the source code for the logic of the state
     * @return result of the operation
     */
    ApexAPIResult createPolicyStateTaskSelectionLogic(String name, String version, String stateName, String logicFlavour, String logic);

    /**
     * Update task selection logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param logicFlavour the task selection logic flavour for the state, set to null to not update
     * @param logic the source code for the logic of the state, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updatePolicyStateTaskSelectionLogic(String name, String version, String stateName, String logicFlavour, String logic);

    /**
     * List task selection logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @return result of the operation
     */
    ApexAPIResult listPolicyStateTaskSelectionLogic(String name, String version, String stateName);

    /**
     * Delete task selection logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @return result of the operation
     */
    ApexAPIResult deletePolicyStateTaskSelectionLogic(String name, String version, String stateName);

    /**
     * Create a policy state output.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param outputName of the state output
     * @param eventName name of the output event for this state output
     * @param eventVersion version of the output event for this state output, set to null to use the latest version
     * @param nextState for this state to transition to, set to null if this is the last state that the policy transitions to on this branch
     * @return result of the operation
     */
    ApexAPIResult createPolicyStateOutput(String name, String version, String stateName, String outputName, String eventName, String eventVersion,
            String nextState);

    /**
     * List policy state outputs.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param outputName of the state output, set to null to list all outputs of the state
     * @return result of the operation
     */
    ApexAPIResult listPolicyStateOutput(String name, String version, String stateName, String outputName);

    /**
     * Delete a policy state output.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param outputName of the state output, set to null to delete all state outputs
     * @return result of the operation
     */
    ApexAPIResult deletePolicyStateOutput(String name, String version, String stateName, String outputName);

    /**
     * Create policy finalizer logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param finalizerLogicName name of the state finalizer logic
     * @param logicFlavour the policy finalizer logic flavour for the state, set to null to use the default task logic flavour
     * @param logic the source code for the logic of the state
     * @return result of the operation
     */
    ApexAPIResult createPolicyStateFinalizerLogic(String name, String version, String stateName, String finalizerLogicName, String logicFlavour,
            String logic);

    /**
     * Update policy finalizer logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param finalizerLogicName name of the state finalizer logic
     * @param logicFlavour the policy finalizer logic flavour for the state, set to null to not update
     * @param logic the source code for the logic of the state, set to null to not update
     * @return result of the operation
     */
    ApexAPIResult updatePolicyStateFinalizerLogic(String name, String version, String stateName, String finalizerLogicName, String logicFlavour,
            String logic);

    /**
     * List policy finalizer logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param finalizerLogicName name of the state finalizer logic
     * @return result of the operation
     */
    ApexAPIResult listPolicyStateFinalizerLogic(String name, String version, String stateName, String finalizerLogicName);

    /**
     * Delete policy finalizer logic for a state.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param finalizerLogicName name of the state finalizer logic
     * @return result of the operation
     */
    ApexAPIResult deletePolicyStateFinalizerLogic(String name, String version, String stateName, String finalizerLogicName);

    /**
     * Create a policy state task reference.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param taskLocalName the task local name
     * @param taskName name of the task
     * @param taskVersion version of the task, set to null to use the latest version
     * @param outputType Type of output for the task, must be DIRECT for direct output to a state output or LOGIC for output to state finalizer logic
     * @param outputName the name of the state output or state state finalizer logic to handle the task output
     * @return result of the operation
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
    ApexAPIResult createPolicyStateTaskRef(String name, String version, String stateName, String taskLocalName, String taskName, String taskVersion,
            String outputType, String outputName);
    // CHECKSTYLE:ON: checkstyle:parameterNumber

    /**
     * List policy state task references.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param taskName name of the task, set to null to list all task references
     * @param taskVersion version of the task, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult listPolicyStateTaskRef(String name, String version, String stateName, String taskName, String taskVersion);

    /**
     * Delete a policy state task reference.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param taskName name of the task, set to null to delete all task references
     * @param taskVersion version of the task, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deletePolicyStateTaskRef(String name, String version, String stateName, String taskName, String taskVersion);

    /**
     * Create a policy state context album reference.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param contextAlbumName name of the context album for the context album reference
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult createPolicyStateContextRef(String name, String version, String stateName, String contextAlbumName, String contextAlbumVersion);

    /**
     * List policy state context album references.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the latest version
     * @param stateName of the state
     * @param contextAlbumName name of the context album for the context album reference, set to null to list all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult listPolicyStateContextRef(String name, String version, String stateName, String contextAlbumName, String contextAlbumVersion);

    /**
     * Delete a policy state context album reference.
     *
     * @param name name of the policy
     * @param version version of the policy, set to null to use the default version
     * @param stateName of the state
     * @param contextAlbumName name of the context album for the context album reference, set to null to delete all task context album references
     * @param contextAlbumVersion version of the context album for the context album reference, set to null to use the latest version
     * @return result of the operation
     */
    ApexAPIResult deletePolicyStateContextRef(String name, String version, String stateName, String contextAlbumName, String contextAlbumVersion);
}
