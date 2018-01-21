/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.TreeMap;

import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.modelapi.ApexModelFactory;

/**
 * This class instantiates and holds the Apex model being manipulated by the editor.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexModelHandler {
    private ApexModel apexModel = null;

    /**
     * Create the Apex Model with the properties specified.
     *
     * @param properties The properties of the Apex model
     */
    public ApexModelHandler(final Properties properties) {
        apexModel = new ApexModelFactory().createApexModel(properties, true);
    }

    /**
     * Create the Apex Model with the properties specified and load it from a file.
     *
     * @param properties The properties of the Apex model
     * @param modelFileName The name of the model file to edit
     */
    public ApexModelHandler(final Properties properties, final String modelFileName) {
        this(properties);

        if (modelFileName == null) {
            return;
        }

        final ApexAPIResult result = apexModel.loadFromFile(modelFileName);
        if (result.isNOK()) {
            throw new CLIException(result.getMessages().get(0));
        }
    }

    /**
     * Execute a command on the Apex model.
     *
     * @param command The command to execute
     * @param argumentValues Arguments of the command
     * @param writer A writer to which to write output
     * @return true, if execute command
     */
    public boolean executeCommand(final CLICommand command, final TreeMap<String, CLIArgumentValue> argumentValues, final PrintWriter writer) {
        // Get the method
        final Method apiMethod = getCommandMethod(command);

        // Get the method arguments
        final Object[] parameterArray = getParameterArray(command, argumentValues, apiMethod);

        try {
            final Object returnObject = apiMethod.invoke(apexModel, parameterArray);
            writer.println(returnObject);
        }
        catch (IllegalAccessException | IllegalArgumentException e) {
            writer.println("invocation of specified method \"" + command.getApiMethod() + "\" failed for command \"" + command.getName() + "\"");
            e.printStackTrace(writer);
            throw new CLIException("invocation of specified method \"" + command.getApiMethod() + "\" failed for command \"" + command.getName() + "\"", e);
        }
        catch (final InvocationTargetException e) {
            writer.println("invocation of specified method \"" + command.getApiMethod() + "\" failed for command \"" + command.getName() + "\"");
            e.getCause().printStackTrace(writer);
            throw new CLIException("invocation of specified method \"" + command.getApiMethod() + "\" failed for command \"" + command.getName() + "\"", e);
        }

        return true;
    }

    /**
     * Find the API method for the command.
     *
     * @param command The command
     * @return the API method
     */
    private Method getCommandMethod(final CLICommand command) {
        final String className = command.getAPIClassName();
        final String methodName = command.getAPIMethodName();

        try {
            final Class<? extends Object> apiClass = Class.forName(className);
            for (final Method apiMethod : apiClass.getMethods()) {
                if (apiMethod.getName().equals(methodName)) {
                    return apiMethod;
                }
            }
            throw new CLIException("specified method \"" + command.getApiMethod() + "\" not found for command \"" + command.getName() + "\"");
        }
        catch (final ClassNotFoundException e) {
            throw new CLIException("specified class \"" + command.getApiMethod() + "\" not found for command \"" + command.getName() + "\"");
        }
    }

    /**
     * Get the arguments of the command as an ordered array of objects ready for the method.
     *
     * @param command the command that invoked the method
     * @param argumentValues the argument values for the method
     * @param apiMethod the method itself
     * @return the argument list
     */
    private Object[] getParameterArray(final CLICommand command, final TreeMap<String, CLIArgumentValue> argumentValues, final Method apiMethod) {
        final Object[] parameterArray = new Object[argumentValues.size()];

        int i = 0;
        try {
            for (final Class<?> parametertype : apiMethod.getParameterTypes()) {
                final String parameterValue = argumentValues.get(command.getArgumentList().get(i).getArgumentName()).getValue();

                if (parametertype.equals(boolean.class)) {
                    parameterArray[i] = (boolean) Boolean.valueOf(parameterValue);
                }
                else {
                    parameterArray[i] = parameterValue;
                }
                i++;
            }
        }
        catch (final Exception e) {
            throw new CLIException("number of argument mismatch on method \"" + command.getApiMethod() + "\" for command \"" + command.getName() + "\"");
        }

        return parameterArray;
    }

    /**
     * Save the model to a string.
     *
     * @param messageWriter the writer to write status messages to
     * @return the string
     */
    public String writeModelToString(final PrintWriter messageWriter) {
        final ApexAPIResult result = apexModel.listModel();

        if (result.isOK()) {
            return result.getMessage();
        }
        else {
            return null;
        }
    }
}
