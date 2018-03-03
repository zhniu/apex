/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.carriertechnology;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.impl.eventrequestor.EventRequestorCarrierTechnologyParameters;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.FILECarrierTechnologyParameters;
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
 * This class deserialises various type of carrier technology parameters from JSON.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CarrierTechnologyParametersJSONAdapter implements JsonSerializer<CarrierTechnologyParameters>, JsonDeserializer<CarrierTechnologyParameters> {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(CarrierTechnologyParametersJSONAdapter.class);

    private static final String PARAMETER_CLASS_NAME = "parameterClassName";

    private static final String CARRIER_TECHNOLOGY_TOKEN = "carrierTechnology";
    private static final String CARRIER_TECHNOLOGY_PARAMETERS = "parameters";

    // Built in technology parameters
	private static final Map<String, String> BUILT_IN_CARRIER_TECHNOLOGY_PARMETER_CLASS_MAP = new HashMap<>();
    static {
		BUILT_IN_CARRIER_TECHNOLOGY_PARMETER_CLASS_MAP.put("FILE",            FILECarrierTechnologyParameters.class.getCanonicalName());
		BUILT_IN_CARRIER_TECHNOLOGY_PARMETER_CLASS_MAP.put("EVENT_REQUESTOR", EventRequestorCarrierTechnologyParameters.class.getCanonicalName());
    }    		

    /*
     * (non-Javadoc)
     *
     * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
     */
    @Override
    public JsonElement serialize(final CarrierTechnologyParameters src, final Type typeOfSrc, final JsonSerializationContext context) {
        String returnMessage = "serialization of Apex carrier technology parameters to Json is not supported";
        LOGGER.error(returnMessage);
        throw new ApexParameterRuntimeException(returnMessage);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public CarrierTechnologyParameters deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Get the carrier technology label primitive
        JsonPrimitive labelJsonPrimitive = (JsonPrimitive) jsonObject.get(CARRIER_TECHNOLOGY_TOKEN);

        // Check if we found our carrier technology
        if (labelJsonPrimitive == null) {
            LOGGER.warn("carrier technology parameter \"" + CARRIER_TECHNOLOGY_TOKEN + "\" not found in JSON file");
            return null;
        }

        // Get and check the carrier technology label
        String carrierTechnologyLabel = labelJsonPrimitive.getAsString().replaceAll("\\s+", "");
        if (carrierTechnologyLabel == null || carrierTechnologyLabel.length() == 0) {
            String errorMessage = "carrier technology parameter \"" + CARRIER_TECHNOLOGY_TOKEN + "\" value \"" + labelJsonPrimitive.getAsString()
                    + "\" invalid in JSON file";
            LOGGER.warn(errorMessage);
            throw new ApexParameterRuntimeException(errorMessage);
        }

        // We now get the technology carrier parameter class
        String carrierTechnologyParameterClassName = null;

        // Get the technology carrier parameter class for the carrier technology plugin class from the configuration parameters
        JsonPrimitive classNameJsonPrimitive = (JsonPrimitive) jsonObject.get(PARAMETER_CLASS_NAME);

        // If no technology carrier parameter class was specified, we try to use a built in carrier technology
        if (classNameJsonPrimitive == null) {
            carrierTechnologyParameterClassName = BUILT_IN_CARRIER_TECHNOLOGY_PARMETER_CLASS_MAP.get(carrierTechnologyLabel);
        }
        else {
            // We use the specified one
            carrierTechnologyParameterClassName = classNameJsonPrimitive.getAsString().replaceAll("\\s+", "");
        }

        // Check the carrier technology parameter class
        if (carrierTechnologyParameterClassName == null || carrierTechnologyParameterClassName.length() == 0) {
            String errorMessage = "carrier technology \"" + carrierTechnologyLabel + "\" parameter \"" + PARAMETER_CLASS_NAME + "\" value \""
                    + classNameJsonPrimitive.getAsString() + "\" invalid in JSON file";
            LOGGER.warn(errorMessage);
            throw new ApexParameterRuntimeException(errorMessage);
        }

        // Get the class for the carrier technology
        Class<?> carrierTechnologyParameterClass = null;
        try {
            carrierTechnologyParameterClass = Class.forName(carrierTechnologyParameterClassName);
        }
        catch (ClassNotFoundException e) {
            String errorMessage = "carrier technology \"" + carrierTechnologyLabel + "\" parameter \"" + PARAMETER_CLASS_NAME + "\" value \""
                    + carrierTechnologyParameterClassName + "\", could not find class";
            LOGGER.warn(errorMessage, e);
            throw new ApexParameterRuntimeException(errorMessage, e);
        }

        // Deserialise the class
        CarrierTechnologyParameters carrierTechnologyParameters = context.deserialize(jsonObject.get(CARRIER_TECHNOLOGY_PARAMETERS),
                carrierTechnologyParameterClass);
        if (carrierTechnologyParameters == null) {
            // OK no parameters for the carrier technology have been specified, just instantiate the default parameters
            try {
                carrierTechnologyParameters = (CarrierTechnologyParameters) carrierTechnologyParameterClass.newInstance();
            }
            catch (Exception e) {
                String errorMessage = "could not create default parameters for carrier technology \"" + carrierTechnologyLabel + "\"\n" + e.getMessage();
                LOGGER.warn(errorMessage, e);
                throw new ApexParameterRuntimeException(errorMessage, e);
            }
        }

        // Check that the carrier technology label matches the label in the carrier technology parameters object
        if (!carrierTechnologyParameters.getLabel().equals(carrierTechnologyLabel)) {
            String errorMessage = "carrier technology \"" + carrierTechnologyLabel + "\" does not match plugin \"" + carrierTechnologyParameters.getLabel()
                    + "\" in \"" + carrierTechnologyParameterClassName + "\", specify correct carrier technology parameter plugin in parameter \""
                    + PARAMETER_CLASS_NAME + "\"";
            LOGGER.warn(errorMessage);
            throw new ApexParameterRuntimeException(errorMessage);
        }

        return carrierTechnologyParameters;
    }
}
