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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.impl.apexprotocolplugin.ApexEventProtocolParameters;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.JSONEventProtocolParameters;
import com.ericsson.apex.service.parameters.ApexParameterRuntimeException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class serialises and deserialises various type of event protocol parameters to and from JSON.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventProtocolParametersJSONAdapter implements JsonSerializer<EventProtocolParameters>, JsonDeserializer<EventProtocolParameters> {
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(EventProtocolParametersJSONAdapter.class);

	private static final String PARAMETER_CLASS_NAME = "parameterClassName";

	private static final String EVENT_PROTOCOL_TOKEN = "eventProtocol";
	private static final String EVENT_PROTOCOL_PARAMETERS = "parameters";

	// Built in event protocol parameters
	private static final Map<String, String> BUILT_IN_EVENT_RPOTOCOL_PARMETER_CLASS_MAP = new HashMap<>();
	static {
		BUILT_IN_EVENT_RPOTOCOL_PARMETER_CLASS_MAP.put("JSON", JSONEventProtocolParameters.class.getCanonicalName());
		BUILT_IN_EVENT_RPOTOCOL_PARMETER_CLASS_MAP.put("APEX", ApexEventProtocolParameters.class.getCanonicalName());
	}    		

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(final EventProtocolParameters src, final Type typeOfSrc, final JsonSerializationContext context) {
		String returnMessage = "serialization of Apex event protocol parameters to Json is not supported";
		LOGGER.error(returnMessage);
		throw new ApexParameterRuntimeException(returnMessage);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public EventProtocolParameters deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		// Get the event protocol label primitive
		JsonPrimitive labelJsonPrimitive = (JsonPrimitive) jsonObject.get(EVENT_PROTOCOL_TOKEN);

		// Check if we found our event protocol
		if (labelJsonPrimitive == null) {
			LOGGER.warn("event protocol parameter \"" + EVENT_PROTOCOL_TOKEN + "\" not found in JSON file");
			return null;
		}

		// Get and check the event protocol label
		String eventProtocolLabel = labelJsonPrimitive.getAsString().replaceAll("\\s+", "");
		if (eventProtocolLabel == null || eventProtocolLabel.length() == 0) {
			String errorMessage = "event protocol parameter \"" + EVENT_PROTOCOL_TOKEN + "\" value \"" + labelJsonPrimitive.getAsString()
			+ "\" invalid in JSON file";
			LOGGER.warn(errorMessage);
			throw new ApexParameterRuntimeException(errorMessage);
		}

		// We now get the event protocol parameter class
		String eventProtocolParameterClassName = null;

		// Get the event protocol parameter class for the event protocol plugin class from the configuration parameters
		JsonPrimitive classNameJsonPrimitive = (JsonPrimitive) jsonObject.get(PARAMETER_CLASS_NAME);

		// If no event protocol parameter class was specified, we use the default
		if (classNameJsonPrimitive == null) {
			eventProtocolParameterClassName = BUILT_IN_EVENT_RPOTOCOL_PARMETER_CLASS_MAP.get(eventProtocolLabel);
		}
		else {
			// We use the specified one
			eventProtocolParameterClassName = classNameJsonPrimitive.getAsString().replaceAll("\\s+", "");
		}

		// Check the event protocol parameter class
		if (eventProtocolParameterClassName == null || eventProtocolParameterClassName.length() == 0) {
			String errorMessage = "event protocol \"" + eventProtocolLabel + "\" parameter \"" + PARAMETER_CLASS_NAME + "\" value \""
					+ classNameJsonPrimitive.getAsString() + "\" invalid in JSON file";
			LOGGER.warn(errorMessage);
			throw new ApexParameterRuntimeException(errorMessage);
		}

		// Get the class for the event protocol
		Class<?> eventProtocolParameterClass = null;
		try {
			eventProtocolParameterClass = Class.forName(eventProtocolParameterClassName);
		}
		catch (ClassNotFoundException e) {
			String errorMessage = "event protocol \"" + eventProtocolLabel + "\" parameter \"" + PARAMETER_CLASS_NAME + "\" value \""
					+ eventProtocolParameterClassName + "\", could not find class";
			LOGGER.warn(errorMessage, e);
			throw new ApexParameterRuntimeException(errorMessage, e);
		}

		// Deserialise the class
		EventProtocolParameters eventProtocolParameters = context.deserialize(jsonObject.get(EVENT_PROTOCOL_PARAMETERS),
				eventProtocolParameterClass);
		if (eventProtocolParameters == null) {
			// OK no parameters for the event protocol have been specified, just instantiate the default parameters
			try {
				eventProtocolParameters = (EventProtocolParameters) eventProtocolParameterClass.newInstance();
			}
			catch (Exception e) {
				String errorMessage = "could not create default parameters for event protocol \"" + eventProtocolLabel + "\"\n" + e.getMessage();
				LOGGER.warn(errorMessage, e);
				throw new ApexParameterRuntimeException(errorMessage, e);
			}
		}

		// Check that the event protocol label matches the label in the event protocol parameters object
		if (!eventProtocolParameters.getLabel().equals(eventProtocolLabel)) {
			String errorMessage = "event protocol \"" + eventProtocolLabel + "\" does not match plugin \"" + eventProtocolParameters.getLabel()
			+ "\" in \"" + eventProtocolParameterClassName + "\", specify correct event protocol parameter plugin in parameter \""
			+ PARAMETER_CLASS_NAME + "\"";
			LOGGER.warn(errorMessage);
			throw new ApexParameterRuntimeException(errorMessage);
		}

		return eventProtocolParameters;
	}
}
