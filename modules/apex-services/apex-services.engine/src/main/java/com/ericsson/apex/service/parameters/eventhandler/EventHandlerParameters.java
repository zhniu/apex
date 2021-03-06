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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
 * <li>requestorMode: true if the event handler is working in requestor mode, defaults to false
 * <li>requestorPeer: the peer event handler (consumer for producer or producer for consumer) of this event handler in requestor mode
 * <li>requestorTimeout: the amount of time to wait for the reply to synchronous events before they are timed out
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
	private boolean requestorMode = false;
	private String requestorPeer = null;
	private long requestorTimeout = 0;
	private String eventName = null;
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
		return !(name == null || name.trim().length() == 0);
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
	 * Checks if the event handler is in the given peered mode.
	 *
	 * @param peeredMode the peer mode
	 * @return true, if the event handler is in the peered mode
	 */
	public boolean isPeeredMode(final EventHandlerPeeredMode peeredMode) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			return synchronousMode;
		case REQUESTOR:
			return requestorMode;
		default:
			return false;
		}
	}

	/**
	 * Sets a peered mode as true or false on the event handler.
	 *
	 * @param peeredMode the peered mode to set
	 * @param peeredModeValue the value to set the peered mode to
	 */
	public void setPeeredMode(final EventHandlerPeeredMode peeredMode, final boolean peeredModeValue) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			synchronousMode = peeredModeValue;
			return;
		case REQUESTOR: 
			requestorMode = peeredModeValue;
			return;
		default: 
			return;
		}
	}

	/**
	 * Gets the peer for the event handler in this peered mode.
	 *
	 * @param peeredMode the peered mode to get the peer for
	 * @return the peer
	 */
	public String getPeer(final EventHandlerPeeredMode peeredMode) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			return synchronousPeer;
		case REQUESTOR: 
			return requestorPeer;
		default: 
			return null;
		}
	}

	/**
	 * Sets the peer for the event handler in this peered mode.
	 *
	 * @param peeredMode the peered mode to set the peer for
	 * @param peer the peer
	 */
	public void setPeer(final EventHandlerPeeredMode peeredMode, final String peer) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			synchronousPeer = peer;
			return;
		case REQUESTOR: 
			requestorPeer = peer;
			return;
		default: 
			return;
		}
	}

	/**
	 * Get the timeout value for the event handler in peered mode.
	 * 
	 * @param peeredMode the peered mode to get the timeout for
	 * @return the timeout value
	 */
	public long getPeerTimeout(final EventHandlerPeeredMode peeredMode) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			return synchronousTimeout;
		case REQUESTOR: 
			return requestorTimeout;
		default: 
			return -1;
		}
	}

	/**
	 * Set the timeout value for the event handler in peered mode.
	 * 
	 * @param peeredMode the peered mode to set the timeout for
	 * @param timeout the timeout value
	 */
	public void setPeerTimeout(final EventHandlerPeeredMode peeredMode, final long timeout) {
		switch (peeredMode) {
		case SYNCHRONOUS: 
			synchronousTimeout = timeout;
			return;
		case REQUESTOR: 
			requestorTimeout = timeout;
			return;
		default: 
			return;
		}
	}

	/**
	 * Check if an event name is being used.
	 *
	 * @return true if an event name is being used
	 */
	public boolean isSetEventName() {
		return eventName != null;
	}

	/**
	 * Gets the event name for this event handler.
	 *
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the event name for this event handler.
	 *
	 * @param eventName the event name
	 */
	public void setEventName(final String eventName) {
		this.eventName = eventName;
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

		if (eventNameFilter != null) {
			try {
				Pattern.compile(eventNameFilter);
			}
			catch (PatternSyntaxException pse) {
				errorMessageBuilder.append("  event handler eventNameFilter is not a valid regular expression: " + pse.getMessage() + "\n");
			}
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
		return "EventHandlerParameters [name=" + name + ", carrierTechnologyParameters=" + carrierTechnologyParameters
				+ ", eventProtocolParameters=" + eventProtocolParameters + ", synchronousMode=" + synchronousMode
				+ ", synchronousPeer=" + synchronousPeer + ", synchronousTimeout=" + synchronousTimeout
				+ ", requestorMode=" + requestorMode + ", requestorPeer=" + requestorPeer + ", requestorTimeout="
				+ requestorTimeout + ", eventName=" + eventName + ", eventNameFilter=" + eventNameFilter + "]";
	}
}
