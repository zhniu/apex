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

import java.io.FileReader;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParametersJSONAdapter;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParametersJSONAdapter;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParametersJSONAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class handles reading, parsing and validating of Apex parameters from JSON files.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexParameterHandler {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexParameterHandler.class);

    /**
     * Read the parameters from the parameter file.
     *
     * @param arguments the arguments passed to Apex
     * @return the parameters read from the configuration file
     * @throws ApexParameterException on parameter exceptions
     */
    public ApexParameters getParameters(final ApexCommandLineArguments arguments) throws ApexParameterException {
        ApexParameters parameters = null;

        // Read the parameters
        try {
            // Register the adapters for our carrier technologies and event protocols with GSON
            // @formatter:off
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(EngineParameters           .class, new EngineServiceParametersJSONAdapter())
                    .registerTypeAdapter(CarrierTechnologyParameters.class, new CarrierTechnologyParametersJSONAdapter())
                    .registerTypeAdapter(EventProtocolParameters    .class, new EventProtocolParametersJSONAdapter())
                    .create();
            // @formatter:on
            parameters = gson.fromJson(new FileReader(arguments.getFullConfigurationFilePath()), ApexParameters.class);
        }
        catch (final Exception e) {
            String errorMessage = "error reading parameters from \"" + arguments.getConfigurationFilePath() + "\"\n" + "(" + e.getClass().getSimpleName() + "):"
                    + e.getMessage();
            LOGGER.error(errorMessage, e);
            throw new ApexParameterException(errorMessage, e);
        }

        // The JSON processing returns null if there is an empty file
        if (parameters == null) {
            String errorMessage = "no parameters found in \"" + arguments.getConfigurationFilePath() + "\"";
            LOGGER.error(errorMessage);
            throw new ApexParameterException(errorMessage);
        }

        // Check if we should override the model file parameter
        String modelFilePath = arguments.getModelFilePath();
        if (modelFilePath != null && modelFilePath.replaceAll("\\s+", "").length() > 0) {
            parameters.getEngineServiceParameters().setPolicyModelFileName(modelFilePath);
        }

        // validate the parameters
        final String validationResult = parameters.validate();
        if (!validationResult.isEmpty()) {
            String returnMessage = "validation error(s) on parameters from \"" + arguments.getConfigurationFilePath() + "\"\n";
            returnMessage += validationResult;

            LOGGER.error(returnMessage);
            throw new ApexParameterException(returnMessage);
        }

        return parameters;
    }
}
