/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.apexprotocolplugin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventList;
import com.ericsson.apex.service.engine.event.ApexEventProtocolConverter;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * The Class Apex2ApexEventConverter passes through {@link ApexEvent} instances. It is used for transferring Apex events directly as POJOs between APEX producers and consumers.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class Apex2ApexEventConverter implements ApexEventProtocolConverter {
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(Apex2ApexEventConverter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventProtocolConverter#init(com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters)
	 */
	@Override
	public void init(final EventProtocolParameters parameters) {
		// Check and get the APEX parameters
		if (!(parameters instanceof ApexEventProtocolParameters)) {
			String errorMessage = "specified consumer properties are not applicable to the APEX event protocol";
			LOGGER.warn(errorMessage);
			throw new ApexEventRuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<ApexEvent> toApexEvent(final String eventName, final Object eventObject) throws ApexEventException {
		// Check the event eventObject
		if (eventObject == null) {
			LOGGER.warn("event processing failed, event is null");
			throw new ApexEventException("event processing failed, event is null");
		}

		// The list of events we will return
		List<ApexEvent> eventList = new ArrayList<>();

		try {
			// Check if its a single APEX event
			if (!(eventObject instanceof ApexEvent)) {
				throw new ApexEventException("incoming event (" + eventObject + ") is not an ApexEvent");
			}

			ApexEvent event = (ApexEvent) eventObject;
			
			// Check whether we have any ApexEventList fields, if so this is an event of events and all fields should be of type ApexEventList
			boolean foundEventListFields = false;
			boolean foundOtherFields     = false;
			for (Object fieldObject : event.values()) {
				if (fieldObject instanceof ApexEventList) {
					foundEventListFields = true;
					
					// Add the events to the event list
					eventList.addAll((ApexEventList) fieldObject);
				}
				else {
					foundOtherFields = true;
				}
			}
			
			// If we found both event list fields and other fields we're in trouble
			if (foundEventListFields && foundOtherFields) {
				throw new ApexEventException("incoming event (" + eventObject + ") has both event list fields and other fields, it cannot be processed");
			}

			// Check if the incoming event just has other fields, if so it's just a regular event and we add it to the event list as the only event there
			if (foundOtherFields) {
				eventList.add(event);
			}
		}
		catch (final Exception e) {
			final String errorString = "Failed to unmarshal APEX event: " + e.getMessage() + ", event=" + eventObject;
			LOGGER.warn(errorString, e);
			throw new ApexEventException(errorString, e);
		}

		// Return the list of events we have unmarshalled
		return eventList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.event.ApexEventConverter#fromApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
	 */
	@Override
	public Object fromApexEvent(final ApexEvent apexEvent) throws ApexEventException {
		// Check the Apex event
		if (apexEvent == null) {
			LOGGER.warn("event processing failed, Apex event is null");
			throw new ApexEventException("event processing failed, Apex event is null");
		}

		return apexEvent;
	}
}
