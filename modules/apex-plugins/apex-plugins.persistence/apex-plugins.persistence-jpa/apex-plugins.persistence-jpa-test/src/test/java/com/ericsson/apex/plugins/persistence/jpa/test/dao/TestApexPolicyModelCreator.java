/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.persistence.jpa.test.dao;

import java.util.UUID;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.eventmodel.concepts.AxInputField;
import com.ericsson.apex.model.eventmodel.concepts.AxOutputField;
import com.ericsson.apex.model.policymodel.concepts.AxPolicies;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateFinalizerLogic;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskOutputType;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskReference;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.model.policymodel.concepts.AxTaskLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTaskParameter;
import com.ericsson.apex.model.policymodel.concepts.AxTaskSelectionLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTasks;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexPolicyModelCreator implements TestApexModelCreator<AxPolicyModel> {

	@Override
	public AxPolicyModel getModel() {
		AxContextSchema stringSchema   = new AxContextSchema(new AxArtifactKey("eventContextItem0", "0.0.1"), "Java", "java.lang.String");
		AxContextSchema longSchema     = new AxContextSchema(new AxArtifactKey("eventContextItem1", "0.0.1"), "Java", "java.lang.Long");
		AxContextSchema boolTypeSchema = new AxContextSchema(new AxArtifactKey("StringType",        "0.0.1"), "Java", "com.ericsson.apex.plugins.persistence.jpa.test.concepts.TestContextItem000");
		AxContextSchema dateTypeSchema = new AxContextSchema(new AxArtifactKey("DateType",          "0.0.1"), "Java", "com.ericsson.apex.plugins.persistence.jpa.test.concepts.TestContextItem00A");
		AxContextSchema mapTypeSchema  = new AxContextSchema(new AxArtifactKey("MapType",           "0.0.1"), "Java", "com.ericsson.apex.plugins.persistence.jpa.test.concepts.TestContextItem00C");

		AxContextSchemas schemas = new AxContextSchemas(new AxArtifactKey("ContextSchemas", "0.0.1"));
		schemas.getSchemasMap().put(stringSchema.getKey(),   stringSchema);
		schemas.getSchemasMap().put(longSchema.getKey(),     longSchema);
		schemas.getSchemasMap().put(boolTypeSchema.getKey(), boolTypeSchema);
		schemas.getSchemasMap().put(dateTypeSchema.getKey(), dateTypeSchema);
		schemas.getSchemasMap().put(mapTypeSchema.getKey(),  mapTypeSchema);

		AxContextAlbum stringAlbum   = new AxContextAlbum(new AxArtifactKey("StringAlbum",   "0.0.1"), "APPLICATION", true, stringSchema  .getKey());
		AxContextAlbum longAlbum     = new AxContextAlbum(new AxArtifactKey("LongAlbum",     "0.0.1"), "GLOBAL"     , true, longSchema    .getKey());
		AxContextAlbum boolTypeAlbum = new AxContextAlbum(new AxArtifactKey("BoolTypeAlbum", "0.0.1"), "GLOBAL"     , true, boolTypeSchema.getKey());
		AxContextAlbum dateTypeAlbum = new AxContextAlbum(new AxArtifactKey("DateTypeAlbum", "0.0.1"), "GLOBAL"     , true, dateTypeSchema.getKey());
		AxContextAlbum mapTypeAlbum  = new AxContextAlbum(new AxArtifactKey("MapTypeAlbum",  "0.0.1"), "GLOBAL"     , true, mapTypeSchema .getKey());

		AxContextAlbums albums = new AxContextAlbums(new AxArtifactKey("albums", "0.0.1"));
		albums.getAlbumsMap().put(stringAlbum  .getKey(), stringAlbum);
		albums.getAlbumsMap().put(longAlbum    .getKey(), longAlbum);
		albums.getAlbumsMap().put(boolTypeAlbum.getKey(), boolTypeAlbum);
		albums.getAlbumsMap().put(dateTypeAlbum.getKey(), dateTypeAlbum);
		albums.getAlbumsMap().put(mapTypeAlbum .getKey(), mapTypeAlbum);

		AxEvent inEvent = new AxEvent(new AxArtifactKey("inEvent", "0.0.1"), "com.ericsson.apex.model.policymodel.events", "Source", "Target");
		inEvent.getParameterMap().put("IEPAR0", new AxField(new AxReferenceKey(inEvent.getKey(), "IEPAR0"), stringSchema.getKey()));
		inEvent.getParameterMap().put("IEPAR1", new AxField(new AxReferenceKey(inEvent.getKey(), "IEPAR1"), longSchema.getKey()));

		AxEvent outEvent0 = new AxEvent(new AxArtifactKey("outEvent0", "0.0.1"), "com.ericsson.apex.model.policymodel.events", "Source", "Target");
		outEvent0.getParameterMap().put("OE0PAR0", new AxField(new AxReferenceKey(outEvent0.getKey(), "OE0PAR0"), stringSchema.getKey()));
		outEvent0.getParameterMap().put("OE0PAR1", new AxField(new AxReferenceKey(outEvent0.getKey(), "OE0PAR1"), longSchema.getKey()));
		outEvent0.getParameterMap().put("OE1PAR0", new AxField(new AxReferenceKey(outEvent0.getKey(), "OE1PAR0"), stringSchema.getKey()));
		outEvent0.getParameterMap().put("OE1PAR1", new AxField(new AxReferenceKey(outEvent0.getKey(), "OE1PAR1"), longSchema.getKey()));

		AxEvent outEvent1 = new AxEvent(new AxArtifactKey("outEvent1", "0.0.1"), "com.ericsson.apex.model.policymodel.events", "Source", "Target");
		outEvent1.getParameterMap().put("OE1PAR0", new AxField(new AxReferenceKey(outEvent1.getKey(), "OE1PAR0"), stringSchema.getKey()));
		outEvent1.getParameterMap().put("OE1PAR1", new AxField(new AxReferenceKey(outEvent1.getKey(), "OE1PAR1"), longSchema.getKey()));

		AxEvents events = new AxEvents(new AxArtifactKey("events", "0.0.1"));
		events.getEventMap().put(inEvent.getKey(), inEvent);
		events.getEventMap().put(outEvent0.getKey(), outEvent0);
		events.getEventMap().put(outEvent1.getKey(), outEvent1);

		AxTask task = new AxTask(new AxArtifactKey("task", "0.0.1"));

		for (AxField field: inEvent.getFields()) {
			AxReferenceKey fieldkey = new AxReferenceKey(task.getKey().getName(), task.getKey().getVersion(), "inputFields", field.getKey().getLocalName());
			AxInputField inputField = new AxInputField(fieldkey,field.getSchema());
			task.getInputFields().put(inputField.getKey().getLocalName(), inputField);
		}

		for (AxField field: outEvent0.getFields()) {
			AxReferenceKey fieldkey = new AxReferenceKey(task.getKey().getName(), task.getKey().getVersion(), "outputFields", field.getKey().getLocalName());
			AxOutputField outputField = new AxOutputField(fieldkey,field.getSchema());
			task.getOutputFields().put(outputField.getKey().getLocalName(), outputField);
		}

		for (AxField field: outEvent1.getFields()) {
			AxReferenceKey fieldkey = new AxReferenceKey(task.getKey().getName(), task.getKey().getVersion(), "outputFields", field.getKey().getLocalName());
			AxOutputField outputField = new AxOutputField(fieldkey,field.getSchema());
			task.getOutputFields().put(outputField.getKey().getLocalName(), outputField);
		}

		AxTaskParameter taskPar0 = new AxTaskParameter(new AxReferenceKey(task.getKey(), "taskParameter0"), "Task parameter 0 value");
		AxTaskParameter taskPar1 = new AxTaskParameter(new AxReferenceKey(task.getKey(), "taskParameter1"), "Task parameter 1 value");

		task.getTaskParameters().put(taskPar0.getKey().getLocalName(), taskPar0);
		task.getTaskParameters().put(taskPar1.getKey().getLocalName(), taskPar1);
		task.getContextAlbumReferences().add(stringAlbum  .getKey());
		task.getContextAlbumReferences().add(longAlbum    .getKey());
		task.getContextAlbumReferences().add(boolTypeAlbum.getKey());
		task.getContextAlbumReferences().add(dateTypeAlbum.getKey());
		task.getContextAlbumReferences().add(mapTypeAlbum .getKey());

		AxTaskLogic taskLogic = new AxTaskLogic(new AxReferenceKey(task.getKey(), "taskLogic"), "MVEL", "Some task logic");
		task.setTaskLogic(taskLogic);

		AxTasks tasks = new AxTasks(new AxArtifactKey("tasks", "0.0.1"));
		tasks.getTaskMap().put(task.getKey(), task);
		
		AxPolicy policy = new AxPolicy(new AxArtifactKey("policy", "0.0.1"));
		policy.setTemplate("FREEFORM");

		AxState state = new AxState(new AxReferenceKey(policy.getKey(), "state"));
		AxTaskSelectionLogic taskSelectionLogic = new AxTaskSelectionLogic(new AxReferenceKey(state.getKey(), "taskSelectionLogic"), "MVEL", "Some TS logic ");

		state.setTrigger(inEvent.getKey());
		state.getContextAlbumReferences().add(stringAlbum  .getKey());
		state.getContextAlbumReferences().add(longAlbum    .getKey());
		state.getContextAlbumReferences().add(boolTypeAlbum.getKey());
		state.getContextAlbumReferences().add(dateTypeAlbum.getKey());
		state.getContextAlbumReferences().add(mapTypeAlbum .getKey());
		state.setTaskSelectionLogic(taskSelectionLogic);
		state.setDefaultTask(task.getKey());

		AxStateOutput stateOutput0 = new AxStateOutput(new AxReferenceKey(state.getKey(), "stateOutput0"), outEvent0.getKey(), AxReferenceKey.getNullKey());
		state.getStateOutputs().put(stateOutput0.getKey().getLocalName(), stateOutput0);

		AxStateTaskReference stateTaskReference = new AxStateTaskReference(new AxReferenceKey(state.getKey(), task.getKey().getName()), AxStateTaskOutputType.DIRECT, stateOutput0.getKey());
		
		state.getTaskReferences().put(task.getKey(), stateTaskReference);

		policy.getStateMap().put(state.getKey().getLocalName(), state);
		policy.setFirstState(state.getKey().getLocalName());

		AxPolicies policies = new AxPolicies(new AxArtifactKey("policies", "0.0.1"));
		policies.getPolicyMap().put(policy.getKey(), policy);

		AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));
		AxPolicyModel policyModel = new AxPolicyModel(new AxArtifactKey("PolicyModel", "0.0.1"));
		policyModel.setKeyInformation(keyInformation);
		policyModel.setSchemas(schemas);
		policyModel.setAlbums(albums);
		policyModel.setEvents(events);
		policyModel.setTasks(tasks);
		policyModel.setPolicies(policies);
		policyModel.getKeyInformation().generateKeyInfo(policyModel);
		
		int uuidIncrementer = 0;
		for (AxKeyInfo keyInfo : policyModel.getKeyInformation().getKeyInfoMap().values()) {
			String uuidString = String.format("ce9168c-e6df-414f-9646-6da464b6e%03d", uuidIncrementer++);
			keyInfo.setUuid(UUID.fromString(uuidString));
		}
		
		AxValidationResult result = new AxValidationResult();
		policyModel.validate(result);
		
		return policyModel;
	}

	@Override
	public AxPolicyModel getMalstructuredModel() {
		AxPolicyModel policyModel = new AxPolicyModel(new AxArtifactKey("policyModel", "0.0.1"));
		return policyModel;
	}

	@Override
	public AxPolicyModel getObservationModel() {
		AxPolicyModel policyModel = getModel();

		AxState state     = policyModel.getPolicies().get("policy").getStateMap().get("state");
		AxTask  task      = policyModel.getTasks().get("task");

		AxStateFinalizerLogic stateFinalizerLogic = new AxStateFinalizerLogic(new AxReferenceKey(state.getKey(), "SFL"), "MVEL", "Some SF logic ");
		state.getStateFinalizerLogicMap().put(stateFinalizerLogic.getKey().getLocalName(), stateFinalizerLogic);
		AxStateTaskReference stateTaskReference = new AxStateTaskReference(new AxReferenceKey(state.getKey(), task.getKey().getName()), AxStateTaskOutputType.LOGIC, stateFinalizerLogic.getKey());
		
		state.getTaskReferences().put(task.getKey(), stateTaskReference);

		return policyModel;
	}

	@Override
	public AxPolicyModel getWarningModel() {
		AxPolicyModel policyModel = getModel();
			
		AxState anotherState = new AxState(new AxReferenceKey(new AxArtifactKey("policy", "0.0.1"), "anotherState"));

		AxEvent inEvent   = policyModel.getEvents().getEventMap().get(new AxArtifactKey("inEvent", "0.0.1"));
		AxEvent outEvent0 = policyModel.getEvents().getEventMap().get(new AxArtifactKey("outEvent0", "0.0.1"));
		
		AxTask anotherTask = new AxTask(new AxArtifactKey("anotherTask", "0.0.1"));

		for (AxField field: inEvent.getFields()) {
			AxReferenceKey fieldkey = new AxReferenceKey(anotherTask.getKey().getName(), anotherTask .getKey().getVersion(), "inputFields", field.getKey().getLocalName());
			AxInputField inputField = new AxInputField(fieldkey,field.getSchema());
			anotherTask.getInputFields().put(inputField.getKey().getLocalName(), inputField);
		}

		for (AxField field: outEvent0.getFields()) {
			AxReferenceKey fieldkey = new AxReferenceKey(anotherTask.getKey().getName(), anotherTask.getKey().getVersion(), "outputFields", field.getKey().getLocalName());
			AxOutputField outputField = new AxOutputField(fieldkey,field.getSchema());
			anotherTask.getOutputFields().put(outputField.getKey().getLocalName(), outputField);
		}

		AxTaskParameter taskPar0 = new AxTaskParameter(new AxReferenceKey(anotherTask.getKey(), "taskParameter0"), "Task parameter 0 value");
		AxTaskParameter taskPar1 = new AxTaskParameter(new AxReferenceKey(anotherTask.getKey(), "taskParameter1"), "Task parameter 1 value");

		anotherTask.getTaskParameters().put(taskPar0.getKey().getLocalName(), taskPar0);
		anotherTask.getTaskParameters().put(taskPar1.getKey().getLocalName(), taskPar1);

		AxTaskLogic taskLogic = new AxTaskLogic(new AxReferenceKey(anotherTask.getKey(), "taskLogic"), "MVEL", "Some task logic");
		anotherTask.setTaskLogic(taskLogic);
		policyModel.getTasks().getTaskMap().put(anotherTask.getKey(), anotherTask);

		AxStateOutput anotherStateOutput0 = new AxStateOutput(new AxReferenceKey(anotherState.getKey(), "stateOutput0"), outEvent0.getKey(), AxReferenceKey.getNullKey());
		anotherState.setTrigger(inEvent.getKey());
		anotherState.getStateOutputs().put(anotherStateOutput0.getKey().getLocalName(), anotherStateOutput0);
		anotherState.setDefaultTask(anotherTask.getKey());
		AxStateTaskReference anotherStateTaskReference = new AxStateTaskReference(new AxReferenceKey(anotherState.getKey(), anotherTask.getKey().getName()), AxStateTaskOutputType.DIRECT, anotherStateOutput0.getKey());
		anotherState.getTaskReferences().put(anotherTask.getKey(), anotherStateTaskReference);
		
		policyModel.getPolicies().getPolicyMap().get(new AxArtifactKey("policy", "0.0.1")).getStateMap().put(anotherState.getKey().getLocalName(), anotherState);
		
		policyModel.getKeyInformation().generateKeyInfo(policyModel);

		return policyModel;
	}

	@Override
	public AxPolicyModel getInvalidModel() {
		AxPolicyModel policyModel = getModel();
		
		policyModel.getAlbums().get(new AxArtifactKey("BoolTypeAlbum", "0.0.1")).setScope("UNDEFINED");
		policyModel.getAlbums().get(new AxArtifactKey("MapTypeAlbum",  "0.0.1")).setScope("UNDEFINED");
		
		AxEvent outEvent0 = policyModel.getEvents().get("outEvent0");
		outEvent0.getParameterMap().remove("OE1PAR0");
		outEvent0.getParameterMap().remove("OE1PAR1");

		return policyModel;
	}
}
