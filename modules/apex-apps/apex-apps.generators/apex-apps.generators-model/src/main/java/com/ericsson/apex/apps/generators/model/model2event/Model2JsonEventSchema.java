/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.generators.model.model2event;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.commons.lang3.Validate;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.ericsson.apex.apps.generators.model.SchemaUtils;
import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.modelapi.ApexModelFactory;
import com.ericsson.apex.model.policymodel.concepts.AxPolicies;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;
import com.ericsson.apex.plugins.context.schema.avro.AvroSchemaHelperParameters;

/**
 * Takes a model and generates the JSON event schemas.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class Model2JsonEventSchema {

    /** Application name, used as prompt. */
    private final String appName;

    /** The file name of the policy model. */
    private final String modelFile;

    /** The type of events to generate: stimuli, response, internal. */
    private final String type;

    /**
     * Creates a new model to event schema generator.
     * 
     * @param modelFile the model file to be used
     * @param type the type of events to generate, one of: stimuli, response, internal
     * @param appName application name for printouts
     */
    public Model2JsonEventSchema(final String modelFile, final String type, final String appName) {
        Validate.notNull(modelFile, "Model2JsonEvent: given model file name was blank");
        Validate.notNull(type, "Model2JsonEvent: given type was blank");
        Validate.notNull(appName, "Model2JsonEvent: given application name was blank");
        this.modelFile = modelFile;
        this.type = type;
        this.appName = appName;
    }

    /**
     * Adds a type to a field for a given schema.
     * 
     * @param schema the schema to add a type for
     * @param stg the STG
     * @return a template with the type
     */
    protected ST addFieldType(final Schema schema, final STGroup stg) {
        ST ret = null;
        switch (schema.getType()) {
        case BOOLEAN:
        case BYTES:
        case DOUBLE:
        case FIXED:
        case FLOAT:
        case INT:
        case LONG:
        case STRING:
            ret = stg.getInstanceOf("fieldTypeAtomic");
            ret.add("type", schema.getType());
            break;

        case ARRAY:
            ret = stg.getInstanceOf("fieldTypeArray");
            ret.add("array", this.addFieldType(schema.getElementType(), stg));
            break;
        case ENUM:
            ret = stg.getInstanceOf("fieldTypeEnum");
            ret.add("symbols", schema.getEnumSymbols());
            break;

        case MAP:
            ret = stg.getInstanceOf("fieldTypeMap");
            ret.add("map", this.addFieldType(schema.getValueType(), stg));
            break;

        case RECORD:
            ret = stg.getInstanceOf("fieldTypeRecord");
            for (final Field field : schema.getFields()) {
                final ST st = stg.getInstanceOf("field");
                st.add("name", field.name());
                st.add("type", this.addFieldType(field.schema(), stg));
                ret.add("fields", st);
            }
            break;

        case NULL:
            break;
        case UNION:
            break;
        default:
            break;
        }
        return ret;
    }

    /**
     * Runs the application.
     * 
     * @throws ApexException if any problem occurred in the model
     * @return status of the application execution, 0 for success, positive integer for exit condition (such as help or version), negative integer for errors
     */
    public int runApp() throws ApexException {
        final STGroupFile stg = new STGroupFile("com/ericsson/apex/apps/generators/model/event-json.stg");
        final ST stEvents = stg.getInstanceOf("events");

        final ApexModelFactory factory = new ApexModelFactory();
        final ApexModel model = factory.createApexModel(new Properties(), true);

        final ApexAPIResult result = model.loadFromFile(modelFile);
        if (result.isNOK()) {
            System.err.println(appName + ": " + result.getMessage());
            return -1;
        }

        final AxPolicyModel policyModel = model.getPolicyModel();
        policyModel.register();
        new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());

        final Set<AxEvent> events = new HashSet<>();
        final Set<AxArtifactKey> eventKeys = new HashSet<>();
        final AxPolicies policies = policyModel.getPolicies();
        switch (type) {
        case "stimuli":
            for (final AxPolicy policy : policies.getPolicyMap().values()) {
                final String firsState = policy.getFirstState();
                for (final AxState state : policy.getStateMap().values()) {
                    if (state.getKey().getLocalName().equals(firsState)) {
                        eventKeys.add(state.getTrigger());
                    }
                }
            }
            break;
        case "response":
            for (final AxPolicy policy : policies.getPolicyMap().values()) {
                for (final AxState state : policy.getStateMap().values()) {
                    if (state.getNextStateSet().iterator().next().equals("NULL")) {
                        for (final AxStateOutput output : state.getStateOutputs().values()) {
                            eventKeys.add(output.getOutgingEvent());
                        }
                    }
                }
            }
            break;
        case "internal":
            for (final AxPolicy policy : policies.getPolicyMap().values()) {
                final String firsState = policy.getFirstState();
                for (final AxState state : policy.getStateMap().values()) {
                    if (state.getKey().getLocalName().equals(firsState)) {
                        continue;
                    }
                    if (state.getNextStateSet().iterator().next().equals("NULL")) {
                        continue;
                    }
                    for (final AxStateOutput output : state.getStateOutputs().values()) {
                        eventKeys.add(output.getOutgingEvent());
                    }
                }
            }
            break;
        default:
            System.err.println(appName + ": unknown type <" + type + ">, cannot proceed");
            return -1;
        }

        for (final AxEvent event : policyModel.getEvents().getEventMap().values()) {
            for (final AxArtifactKey key : eventKeys) {
                if (event.getKey().equals(key)) {
                    events.add(event);
                }
            }
        }

        for (final AxEvent event : events) {
            final ST stEvent = stg.getInstanceOf("event");
            stEvent.add("name", event.getKey().getName());
            stEvent.add("nameSpace", event.getNameSpace());
            stEvent.add("version", event.getKey().getVersion());
            stEvent.add("source", event.getSource());
            stEvent.add("target", event.getTarget());

            final Schema avro = SchemaUtils.getEventSchema(event);
            for (final Field field : avro.getFields()) {
                // filter magic names
                switch (field.name()) {
                case "name":
                case "nameSpace":
                case "version":
                case "source":
                case "target":
                    break;
                default:
                    stEvent.add("fields", this.setField(field, stg));
                }
            }
            stEvents.add("event", stEvent);
        }
        System.err.println(stEvents.render());
        return 0;
    }

    /**
     * Adds a field to the output.
     * 
     * @param field the field from the event
     * @param stg the STG
     * @return a template for the field
     */
    protected ST setField(final Field field, final STGroup stg) {
        final ST st = stg.getInstanceOf("field");
        switch (field.name()) {
        case "name":
        case "nameSpace":
        case "version":
        case "source":
        case "target":
            break;
        default:
            st.add("name", field.name());
            st.add("type", this.addFieldType(field.schema(), stg));
        }
        return st;
    }

}
