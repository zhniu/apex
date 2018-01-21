/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.executor;

import java.util.Map;
import java.util.Map.Entry;

import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is the output of a state, and is used by the engine to decide what the next state for execution is.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class StateOutput {
    // The state output has a state and an event
    private final AxStateOutput stateOutputDefinition;
    private final AxEvent outputEventDef;
    private final EnEvent outputEvent;

    /**
     * Create a new state output from a state output definition.
     *
     * @param axStateOutput the state output definition
     */
    public StateOutput(final AxStateOutput axStateOutput) {
        this(axStateOutput, new EnEvent(axStateOutput.getOutgingEvent()));
    }

    /**
     * Create a new state output with the given definition and event key.
     *
     * @param stateOutputDefinition the state output definition
     * @param outputEvent the output event
     */
    public StateOutput(final AxStateOutput stateOutputDefinition, final EnEvent outputEvent) {
        Assertions.argumentNotNull(stateOutputDefinition, "stateOutputDefinition may not be null");
        Assertions.argumentNotNull(outputEvent, "outputEvent may not be null");

        this.stateOutputDefinition = stateOutputDefinition;
        this.outputEvent = outputEvent;
        outputEventDef = ModelService.getModel(AxEvents.class).get(stateOutputDefinition.getOutgingEvent());
    }

    /**
     * Gets the next state.
     *
     * @return the next state
     */
    public AxReferenceKey getNextState() {
        return stateOutputDefinition.getNextState();
    }

    /**
     * Gets the state output definition.
     *
     * @return the state output definition
     */
    public AxStateOutput getStateOutputDefinition() {
        return stateOutputDefinition;
    }

    /**
     * Gets the output event.
     *
     * @return the output event
     */
    public EnEvent getOutputEvent() {
        return outputEvent;
    }

    /**
     * Transfer the fields from the incoming field map into the event.
     *
     * @param incomingFieldDefinitionMap definitions of the incoming fields
     * @param eventFieldMap the event field map
     * @throws StateMachineException on errors populating the event fields
     */
    public void setEventFields(final Map<String, AxField> incomingFieldDefinitionMap, final Map<String, Object> eventFieldMap) throws StateMachineException {
        Assertions.argumentNotNull(incomingFieldDefinitionMap, "incomingFieldDefinitionMap may not be null");
        Assertions.argumentNotNull(eventFieldMap, "eventFieldMap may not be null");

        if (!incomingFieldDefinitionMap.keySet().equals(eventFieldMap.keySet())) {
            throw new StateMachineException("field definitions and values do not match for event " + outputEventDef.getID() + '\n'
                    + incomingFieldDefinitionMap.keySet() + '\n' + eventFieldMap.keySet());
        }
        for (final Entry<String, Object> incomingFieldEntry : eventFieldMap.entrySet()) {
            final String fieldName = incomingFieldEntry.getKey();
            final AxField fieldDef = incomingFieldDefinitionMap.get(fieldName);
            try {

                // Check if this field is a field in the event
                if (!outputEventDef.getFields().contains(fieldDef)) {
                    throw new StateMachineException("field \"" + fieldName + "\" does not exist on event \"" + outputEventDef.getID() + "\"");
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }

            // Set the value in the output event
            outputEvent.put(fieldName, incomingFieldEntry.getValue());
        }
    }

    /**
     * This method copies any fields that exist on the input event that also exist on the output event if they are not set on the output event.
     *
     * @param incomingEvent The incoming event to copy from
     */
    public void copyUnsetFields(final EnEvent incomingEvent) {
        Assertions.argumentNotNull(incomingEvent, "incomingEvent may not be null");

        for (final Entry<String, Object> incomingField : incomingEvent.entrySet()) {
            final String fieldName = incomingField.getKey();

            // Check if the field exists on the outgoing event
            if (!outputEventDef.getParameterMap().containsKey(fieldName)) {
                continue;
            }

            // Check if the field is set on the outgoing event
            if (outputEvent.containsKey(fieldName)) {
                continue;
            }

            // Now, check the fields have the same type
            if (!incomingEvent.getAxEvent().getParameterMap().get(fieldName).equals(outputEvent.getAxEvent().getParameterMap().get(fieldName))) {
                continue;
            }

            // All checks done, we can copy the value
            outputEvent.put(fieldName, incomingField.getValue());
        }

    }
}
