/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;

/**
 * The main container parameter class for an Apex service.
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>engineServiceParameters: The parameters for the Apex engine service itself, such as the number of engine threads to run and the deployment port number to
 * use.
 * <li>eventOutputParameters: A map of parameters for event outputs that Apex will use to emit events. Apex emits events on all outputs
 * <li>eventInputParameters: A map or parameters for event inputs from which Apex will consume events. Apex reads events from all its event inputs.
 * <li>synchronousEventHandlerParameters: A map of parameters for synchronous event handlers That Apex receives events from and replies immediately to those
 * events.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexParameters extends AbstractParameters implements ApexParameterValidator {
    /**
     * Constructor to create an apex parameters instance and register the instance with the parameter service.
     */
    public ApexParameters() {
        super(ContextParameters.class.getCanonicalName());
        ParameterService.registerParameters(ApexParameters.class, this);
    }

    // Parameters for the engine service and the engine threads in the engine service
    private EngineServiceParameters engineServiceParameters;

    // Parameters for the event outputs that Apex will use to send events on its outputs
    private Map<String, EventHandlerParameters> eventOutputParameters = new LinkedHashMap<String, EventHandlerParameters>();

    // Parameters for the event inputs that Apex will use to receive events on its inputs
    private Map<String, EventHandlerParameters> eventInputParameters = new LinkedHashMap<String, EventHandlerParameters>();

    /**
     * Gets the parameters for the Apex engine service.
     *
     * @return the engine service parameters
     */
    public EngineServiceParameters getEngineServiceParameters() {
        return engineServiceParameters;
    }

    /**
     * Sets the engine service parameters.
     *
     * @param engineServiceParameters the engine service parameters
     */
    public void setEngineServiceParameters(final EngineServiceParameters engineServiceParameters) {
        this.engineServiceParameters = engineServiceParameters;
    }

    /**
     * Gets the event output parameter map.
     *
     * @return the parameters for all event outputs
     */
    public Map<String, EventHandlerParameters> getEventOutputParameters() {
        return eventOutputParameters;
    }

    /**
     * Sets the event output parameters.
     *
     * @param eventOutputParameters the event outputs parameters
     */
    public void setEventOutputParameters(final Map<String, EventHandlerParameters> eventOutputParameters) {
        this.eventOutputParameters = eventOutputParameters;
    }

    /**
     * Gets the event input parameter map.
     *
     * @return the parameters for all event inputs
     */
    public Map<String, EventHandlerParameters> getEventInputParameters() {
        return eventInputParameters;
    }

    /**
     * Sets the event input parameters.
     *
     * @param eventInputParameters the event input parameters
     */
    public void setEventInputParameters(final Map<String, EventHandlerParameters> eventInputParameters) {
        this.eventInputParameters = eventInputParameters;
    }

    /**
     * This method formats a validation result with a header if the result is not empty.
     *
     * @param validationResultMessage The incoming message
     * @param heading The heading to prepend on the message
     * @return the formatted message
     */
    private String validationResultFormatter(final String validationResultMessage, final String heading) {
        StringBuilder errorMessageBuilder = new StringBuilder();

        if (validationResultMessage.length() > 0) {
            errorMessageBuilder.append(heading);
            errorMessageBuilder.append(validationResultMessage);
        }

        return errorMessageBuilder.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        if (engineServiceParameters == null) {
            errorMessageBuilder.append(" engine service parameters are not specified\n");
        }
        else {
            errorMessageBuilder.append(validationResultFormatter(engineServiceParameters.validate(), " engine service parameters invalid\n"));
        }

        // Sanity check, we must have an entry in both output and input maps
        if (eventOutputParameters.isEmpty() || eventInputParameters.isEmpty()) {
            errorMessageBuilder.append(" at least one event output and one event input must be specified\n");
        }

        // Check synchronous parameters on inputs and outputs
        Set<String> synchronousPeersInInputs = new LinkedHashSet<String>();
        Set<String> synchronousPeersInOutputs = new LinkedHashSet<String>();

        validateEventHandlerMap("event input", errorMessageBuilder, synchronousPeersInInputs, eventInputParameters, eventOutputParameters);
        validateEventHandlerMap("event output", errorMessageBuilder, synchronousPeersInOutputs, eventOutputParameters, eventInputParameters);

        // Check that peered parameters are compatible, we need only traverse the input parameters
        for (Map.Entry<String, EventHandlerParameters> inputParameterEntry : eventInputParameters.entrySet()) {
            if (!inputParameterEntry.getValue().isSynchronousMode()) {
                continue;
            }

            // The peer of the output peer of the input peer should be the input
            String inputSynchronousPeer = inputParameterEntry.getValue().getSynchronousPeer();
            if (inputSynchronousPeer == null || eventOutputParameters.get(inputSynchronousPeer) == null) {
                // Unspecified peer errors are already detected in the validateEventHandlerMap() method
                continue;
            }

            EventHandlerParameters peerEventOutputParameters = eventOutputParameters.get(inputSynchronousPeer);
            String outputSynchronousPeer = peerEventOutputParameters.getSynchronousPeer();

            if (!outputSynchronousPeer.equals(inputParameterEntry.getKey())) {
                errorMessageBuilder.append(" synchronous peers of event input \"" + inputParameterEntry.getKey() + "\" and event output \""
                        + inputSynchronousPeer + "/" + outputSynchronousPeer + "\" do not match\n");
            }

            // Cross-set the timeouts if they are not specified
            if (inputParameterEntry.getValue().getSynchronousTimeout() != 0) {
                if (peerEventOutputParameters.getSynchronousTimeout() != 0) {
                    if (inputParameterEntry.getValue().getSynchronousTimeout() != peerEventOutputParameters.getSynchronousTimeout()) {
                        errorMessageBuilder.append(" synchronous timeout of event input \"" + inputParameterEntry.getKey() + "\" and event output \""
                                + inputSynchronousPeer + "\" [" + inputParameterEntry.getValue().getSynchronousTimeout() + "/"
                                + peerEventOutputParameters.getSynchronousTimeout() + "] do not match\n");
                    }
                }
                else {
                    peerEventOutputParameters.setSynchronousTimeout(inputParameterEntry.getValue().getSynchronousTimeout());
                }
            }
            else {
                if (peerEventOutputParameters.getSynchronousTimeout() != 0) {
                    inputParameterEntry.getValue().setSynchronousTimeout(peerEventOutputParameters.getSynchronousTimeout());
                }
            }
        }

        // Check if we have any errors
        if (errorMessageBuilder.length() > 0) {
            errorMessageBuilder.insert(0, "Apex parameters invalid\n");
        }

        return errorMessageBuilder.toString().trim();
    }

    /**
     * This method validates the parameters in an event handler map.
     * 
     * @param eventHandlerType the type of the event handler to use on error messages
     * @param errorMessageBuilder the builder to use to return validation messages
     * @param synchronousPeersInMap the set of synchronous peers specified in the map
     * @param eventOutputParameters2
     * @param parsForValidation
     */
    // CHECKSTYLE:OFF: checkstyle:finalParameter
    private void validateEventHandlerMap(final String eventHandlerType, StringBuilder errorMessageBuilder, Set<String> synchronousPeersInMap,
            final Map<String, EventHandlerParameters> parsForValidation, final Map<String, EventHandlerParameters> peerParameters) {
        // CHECKSTYLE:ON: checkstyle:finalParameter
        for (Map.Entry<String, EventHandlerParameters> parameterEntry : parsForValidation.entrySet()) {
            if (parameterEntry.getKey() == null || parameterEntry.getKey().trim().isEmpty()) {
                errorMessageBuilder.append(" invalid " + eventHandlerType + " name \"" + parameterEntry.getKey() + "\" \n");
            }
            else if (parameterEntry.getValue() == null) {
                errorMessageBuilder
                .append(" invalid/Null event input prameters specified for " + eventHandlerType + " name \"" + parameterEntry.getKey() + "\" \n");
            }
            else {
                errorMessageBuilder.append(validationResultFormatter(parameterEntry.getValue().validate(),
                        " " + eventHandlerType + " (" + parameterEntry.getKey() + ") parameters invalid\n"));
            }

            String synchronousPeer = parameterEntry.getValue().getSynchronousPeer();

            if (parameterEntry.getValue().isSynchronousMode()) {
                if (synchronousPeer == null || synchronousPeer.trim().isEmpty()) {
                    errorMessageBuilder.append(" synchronous mode mandatory parameter \"synchronousPeer\" not specified or is null on " + eventHandlerType
                            + " \"" + parameterEntry.getKey() + "\" \n");
                }
                else {
                    if (synchronousPeersInMap.contains(synchronousPeer)) {
                        errorMessageBuilder.append(" value of parameter \"synchronousPeer\" on " + eventHandlerType + " \"" + parameterEntry.getKey()
                        + "\" must be unique, it s used on another " + eventHandlerType + "\n");
                    }
                    else {
                        synchronousPeersInMap.add(synchronousPeer);
                    }

                    if (!peerParameters.containsKey(synchronousPeer)) {
                        errorMessageBuilder.append(" specified \"synchronousPeer\" parameter value \"" + synchronousPeer + "\" on " + eventHandlerType + " \""
                                + parameterEntry.getKey() + "\" does not exist or is an invalid peer for this event handler\n");
                    }
                }
                if (parameterEntry.getValue().getSynchronousTimeout() < 0) {
                    errorMessageBuilder.append(" parameter \\\"synchronousTimeout\\\" value \"" + parameterEntry.getValue().getSynchronousTimeout()
                            + "\" is illegal on synchronous " + eventHandlerType + " \"" + parameterEntry.getKey()
                            + "\", specify a non-negative timeout value in milliseconds\n");
                }
            }
            else {
                if (synchronousPeer != null) {
                    errorMessageBuilder.append(
                            " parameter \\\"synchronousPeer\\\" is illegal on non synchronous " + eventHandlerType + " \"" + parameterEntry.getKey() + "\" \n");
                }
                if (parameterEntry.getValue().getSynchronousTimeout() != 0) {
                    errorMessageBuilder.append(" parameter \\\"synchronousTimeout\\\" is illegal on non synchronous " + eventHandlerType + " \""
                            + parameterEntry.getKey() + "\" \n");
                }
            }
        }
    }
}
