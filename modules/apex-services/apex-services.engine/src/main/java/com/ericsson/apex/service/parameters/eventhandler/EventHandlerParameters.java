/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.eventhandler;

import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.service.parameters.ApexParameterValidator;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * The parameters for a single event producer, event consumer or synchronous event handler.
 * <p>
 * Event producers, consumers, and synchronous event handlers all use a carrier technology and an event protocol so the actual parameters for each one are the
 * same. Therefore, we use the same class for the parameters of each one.
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>carrierTechnologyParameters: The carrier technology is the type of messaging infrastructure used to carry events. Examples are File, Kafka or REST.
 * <li>eventProtocolParameters: The format that the events are in when being carried. Examples are JSON, XML, or Java Beans. carrier technology
 * <li>synchronousMode: true if the event handler is working in synchronous mode, defaults to false
 * <li>synchronousPeer: the peer event handler (consumer for producer or producer for consumer) of this event handler in synchronous mode
 * <li>synchronousTimeout: the amount of time to wait for the reply to synchronous events before they are timed out
 * <li>eventNameFilter: a regular expression to apply to events on this event handler. If specified, events not matching the given regular expression are
 * ignored. If it is null, all events are handledDefaults to null.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventHandlerParameters extends AbstractParameters implements ApexParameterValidator {
    private String name = null;
    private CarrierTechnologyParameters carrierTechnologyParameters = null;
    private EventProtocolParameters eventProtocolParameters = null;
    private boolean synchronousMode = false;
    private String synchronousPeer = null;
    private long synchronousTimeout = 0;
    private String eventNameFilter = null;

    /**
     * Constructor to create an event handler parameters instance.
     */
    public EventHandlerParameters() {
        super(EventHandlerParameters.class.getCanonicalName());
    }

    /**
     * Constructor to create an event handler parameters instance with the name of a sub class of this class.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public EventHandlerParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the name of the event handler.
     *
     * @return the event handler name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event handler.
     *
     * @param name the event handler name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Checks if the name of the event handler is set.
     * 
     * @return true if the name is set
     */
    public boolean checkSetName() {
        if (name == null) {
            return false;
        }

        if (name.trim().length() == 0) {
            return false;
        }

        return true;
    }

    /**
     * Gets the carrier technology parameters of the event handler.
     *
     * @return the carrierTechnologyParameters of the event handler
     */
    public CarrierTechnologyParameters getCarrierTechnologyParameters() {
        return carrierTechnologyParameters;
    }

    /**
     * Sets the carrier technology parameters of the event handler.
     *
     * @param carrierTechnologyParameters the carrierTechnologyParameters to set
     */
    public void setCarrierTechnologyParameters(final CarrierTechnologyParameters carrierTechnologyParameters) {
        this.carrierTechnologyParameters = carrierTechnologyParameters;
    }

    /**
     * Gets the event protocol parameters of the event handler.
     *
     * @return the eventProtocolParameters
     */
    public EventProtocolParameters getEventProtocolParameters() {
        return eventProtocolParameters;
    }

    /**
     * Sets the event protocol parameters.
     *
     * @param eventProtocolParameters the eventProtocolParameters to set
     */
    public void setEventProtocolParameters(final EventProtocolParameters eventProtocolParameters) {
        this.eventProtocolParameters = eventProtocolParameters;
    }

    /**
     * Checks if the event handler is in synchronous mode.
     *
     * @return true, if the event handler is in synchronous mode
     */
    public boolean isSynchronousMode() {
        return synchronousMode;
    }

    /**
     * Sets synchronous mode as true or false on the event handler.
     *
     * @param synchronousMode synchronous mode
     */
    public void setSynchronousMode(final boolean synchronousMode) {
        this.synchronousMode = synchronousMode;
    }

    /**
     * Gets the synchronous peer for this event handler.
     *
     * @return the synchronous peer
     */
    public String getSynchronousPeer() {
        return synchronousPeer;
    }

    /**
     * Sets the synchronous peer for this event handler.
     *
     * @param synchronousPeer the synchronous peer
     */
    public void setSynchronousPeer(final String synchronousPeer) {
        this.synchronousPeer = synchronousPeer;
    }

    /**
     * Get the timeout value for synchronous events.
     * 
     * @return the timeout value
     */
    public long getSynchronousTimeout() {
        return synchronousTimeout;
    }

    /**
     * Set the timeout value for synchronous events.
     * 
     * @param synchronousTimeout the timeout value
     */
    public void setSynchronousTimeout(final long synchronousTimeout) {
        this.synchronousTimeout = synchronousTimeout;
    }

    /**
     * Check if event name filtering is being used.
     *
     * @return true if event name filtering is being used
     */
    public boolean isSetEventNameFilter() {
        return eventNameFilter != null;
    }

    /**
     * Gets the event name filter for this event handler.
     *
     * @return the event name filter
     */
    public String getEventNameFilter() {
        return eventNameFilter;
    }

    /**
     * Sets the event name filter for this event handler.
     *
     * @param eventNameFilter the event name filter
     */
    public void setEventNameFilter(final String eventNameFilter) {
        this.eventNameFilter = eventNameFilter;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        if (eventProtocolParameters == null) {
            errorMessageBuilder.append("  event handler eventProtocolParameters not specified or blank\n");
        }
        else {
            errorMessageBuilder.append(eventProtocolParameters.validate());
        }

        if (carrierTechnologyParameters == null) {
            errorMessageBuilder.append("  event handler carrierTechnologyParameters not specified or blank\n");
        }
        else {
            errorMessageBuilder.append(carrierTechnologyParameters.validate());
        }

        return errorMessageBuilder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EventHandlerParameters [name=" + name + ", carrierTechnologyParameters=" + carrierTechnologyParameters + ", eventProtocolParameters="
                + eventProtocolParameters + ", synchronousMode=" + synchronousMode + ", synchronousPeer=" + synchronousPeer + "]";
    }
}
