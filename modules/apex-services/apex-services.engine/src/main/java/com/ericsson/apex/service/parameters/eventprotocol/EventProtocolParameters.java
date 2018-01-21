/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.eventprotocol;

import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.service.parameters.ApexParameterValidator;

/**
 * A default event protocol parameter class that may be specialized by event protocol plugins that require plugin specific parameters.
 *
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>label: The label of the event protocol technology.
 * <li>eventProducerPluginClass: The name of the plugin class that will be used by Apex to produce and emit output events for this carrier technology
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class EventProtocolParameters extends AbstractParameters implements ApexParameterValidator {
    // The event protocol label
    private String label = null;

    // Event protocol converter plugin class for this event protocol
    private String eventProtocolPluginClass;

    /**
     * Constructor to create an event protocol parameters instance with the name of a sub class of this class and register the instance with the parameter
     * service.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public EventProtocolParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the label of the event protocol.
     *
     * @return the label of the event protocol
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the event protocol.
     *
     * @param label the label of the event protocol
     */
    public void setLabel(final String label) {
        this.label = label.replaceAll("\\s+", "");
    }

    /**
     * Gets the event event protocol plugin class.
     *
     * @return the event event protocol plugin class
     */
    public String getEventProtocolPluginClass() {
        return eventProtocolPluginClass;
    }

    /**
     * Sets the event event protocol plugin class.
     *
     * @param eventProtocolPluginClass the event event protocol plugin class
     */
    public void setEventProtocolPluginClass(final String eventProtocolPluginClass) {
        this.eventProtocolPluginClass = eventProtocolPluginClass.replaceAll("\\s+", "");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.service.AbstractParameters#toString()
     */
    @Override
    public String toString() {
        return "CarrierTechnologyParameters [label=" + label + ", EventProtocolPluginClass=" + eventProtocolPluginClass + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        if (label == null || label.length() == 0) {
            errorMessageBuilder.append("  event protocol label not specified or is blank\n");
        }

        if (eventProtocolPluginClass == null || eventProtocolPluginClass.length() == 0) {
            errorMessageBuilder.append("  event protocol eventProtocolPluginClass not specified or is blank\n");
        }

        return errorMessageBuilder.toString();
    }
}
