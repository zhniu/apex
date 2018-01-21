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

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class acts as a facade for operations towards a policy model for event operations operations.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventFacade {
    // Apex model we're working towards
    private final ApexModel apexModel;

    // Properties to use for the model
    private final Properties apexProperties;

    // Facade classes for working towards the real Apex model
    private final KeyInformationFacade keyInformationFacade;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * Constructor to create an event facade for the Model API.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public EventFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;

        keyInformationFacade = new KeyInformationFacade(apexModel, apexProperties, jsonMode);
    }

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
    public ApexAPIResult createEvent(final String name, final String version, final String nameSpace, final String source, final String target,
            final String uuid, final String description) {
        try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                key.setVersion(apexProperties.getProperty("DEFAULT_CONCEPT_VERSION"));
            }

            if (apexModel.getPolicyModel().getEvents().getEventMap().containsKey(key)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + key.getID() + " already exists");
            }

            AxEvent event = new AxEvent(key);

            event.setNameSpace((nameSpace != null ? nameSpace : apexProperties.getProperty("DEFAULT_EVENT_NAMESPACE")));
            event.setSource((source != null ? source : apexProperties.getProperty("DEFAULT_EVENT_SOURCE")));
            event.setTarget((target != null ? target : apexProperties.getProperty("DEFAULT_EVENT_TARGET")));

            apexModel.getPolicyModel().getEvents().getEventMap().put(key, event);

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
    public ApexAPIResult updateEvent(final String name, final String version, final String nameSpace, final String source, final String target,
            final String uuid, final String description) {
        try {
            AxEvent event = apexModel.getPolicyModel().getEvents().get(name, version);
            if (event == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (nameSpace != null) {
                event.setNameSpace(nameSpace);
            }
            if (source != null) {
                event.setSource(source);
            }
            if (target != null) {
                event.setTarget(target);
            }

            return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List events.
     *
     * @param name name of the event, set to null to list all
     * @param version starting version of the event, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult listEvent(final String name, final String version) {
        try {
            Set<AxEvent> eventSet = apexModel.getPolicyModel().getEvents().getAll(name, version);
            if (name != null && eventSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxEvent event : eventSet) {
                result.addMessage(new ApexModelStringWriter<AxEvent>(false).writeString(event, AxEvent.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete an event.
     *
     * @param name name of the event
     * @param version version of the event, set to null to delete all versions
     * @return result of the operation
     */
    public ApexAPIResult deleteEvent(final String name, final String version) {
        try {
            if (version != null) {
                AxArtifactKey key = new AxArtifactKey(name, version);
                AxEvent removedEvent = apexModel.getPolicyModel().getEvents().getEventMap().remove(key);
                if (removedEvent != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxEvent>(false).writeString(removedEvent, AxEvent.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + key.getID() + " does not exist");
                }
            }

            Set<AxEvent> eventSet = apexModel.getPolicyModel().getEvents().getAll(name, version);
            if (eventSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxEvent event : eventSet) {
                result.addMessage(new ApexModelStringWriter<AxEvent>(false).writeString(event, AxEvent.class, jsonMode));
                apexModel.getPolicyModel().getEvents().getEventMap().remove(event.getKey());
                keyInformationFacade.deleteKeyInformation(name, version);
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Validate events.
     *
     * @param name name of the event, set to null to list all
     * @param version starting version of the event, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult validateEvent(final String name, final String version) {
        try {
            Set<AxEvent> eventSet = apexModel.getPolicyModel().getEvents().getAll(name, version);
            if (eventSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxEvent event : eventSet) {
                AxValidationResult validationResult = event.validate(new AxValidationResult());
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(event.getKey(), AxArtifactKey.class, jsonMode));
                result.addMessage(validationResult.toString());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

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
    public ApexAPIResult createEventPar(final String name, final String version, final String parName, final String contextSchemaName,
            final String contextSchemaVersion, final boolean optional) {
        try {
            Assertions.argumentNotNull(parName, "parName may not be null");

            AxEvent event = apexModel.getPolicyModel().getEvents().get(name, version);
            if (event == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            AxReferenceKey refKey = new AxReferenceKey(event.getKey(), parName);

            if (event.getParameterMap().containsKey(refKey.getLocalName())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + refKey.getID() + " already exists");
            }

            AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(contextSchemaName, contextSchemaVersion);
            if (schema == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + contextSchemaName + ':' + contextSchemaVersion + " does not exist");
            }

            event.getParameterMap().put(refKey.getLocalName(), new AxField(refKey, schema.getKey(), optional));
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List event parameters.
     *
     * @param name name of the event
     * @param version version of the event, set to null to list latest version
     * @param parName name of the parameter, set to null to list all parameters of the event
     * @return result of the operation
     */
    public ApexAPIResult listEventPar(final String name, final String version, final String parName) {
        try {
            AxEvent event = apexModel.getPolicyModel().getEvents().get(name, version);
            if (event == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (parName != null) {
                AxField eventField = event.getParameterMap().get(parName);
                if (eventField != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxField>(false).writeString(eventField, AxField.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + parName + " does not exist");
                }
            }
            else {
                if (event.getParameterMap().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no parameters defined on event " + event.getKey().getID());
                }

                ApexAPIResult result = new ApexAPIResult();
                for (AxField eventPar : event.getParameterMap().values()) {
                    result.addMessage(new ApexModelStringWriter<AxField>(false).writeString(eventPar, AxField.class, jsonMode));
                }
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete an event parameter.
     *
     * @param name name of the event
     * @param version version of the event, set to null to use the latest version
     * @param parName of the parameter, set to null to delete all parameters
     * @return result of the operation
     */
    public ApexAPIResult deleteEventPar(final String name, final String version, final String parName) {
        try {
            AxEvent event = apexModel.getPolicyModel().getEvents().get(name, version);
            if (event == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            if (parName != null) {
                if (event.getParameterMap().containsKey(parName)) {
                    result.addMessage(new ApexModelStringWriter<AxField>(false).writeString(event.getParameterMap().get(parName), AxField.class, jsonMode));
                    event.getParameterMap().remove(parName);
                    return result;
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            "concept " + name + ':' + version + ':' + parName + " does not exist");
                }
            }
            else {
                if (event.getParameterMap().size() == 0) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "no parameters defined on event " + event.getKey().getID());
                }

                for (AxField eventPar : event.getParameterMap().values()) {
                    result.addMessage(new ApexModelStringWriter<AxField>(false).writeString(eventPar, AxField.class, jsonMode));
                }
                event.getParameterMap().clear();
                return result;
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }
}
