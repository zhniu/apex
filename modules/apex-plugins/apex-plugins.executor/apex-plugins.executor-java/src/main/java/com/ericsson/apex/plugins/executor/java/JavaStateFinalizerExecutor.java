/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.java;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.StateFinalizerExecutor;
import com.ericsson.apex.core.engine.executor.context.StateFinalizerExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JavaStateFinalizerExecutor is the state finalizer executor for state finalizer logic written in Java.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavaStateFinalizerExecutor extends StateFinalizerExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JavaStateFinalizerExecutor.class);

    // The Java State Finalizer executor class
    private Object stateFinalizerLogicObject = null;

    /**
     * Prepares the state finalizer for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Get the class for state finalizer execution
        try {
            // Create the state finalizer logic object from the byte code of the class
            stateFinalizerLogicObject = Class.forName(getSubject().getLogic()).newInstance();
        }
        catch (final Exception e) {
            LOGGER.error("instantiation error on Java class \"" + getSubject().getLogic() + "\"", e);
            throw new StateMachineException("instantiation error on Java class \"" + getSubject().getLogic() + "\"", e);
        }
    }

    /**
     * Executes the executor for the state finalizer logic in a sequential manner.
     *
     * @param executionID the execution ID for the current APEX policy execution
     * @param incomingFields the incoming fields for finalisation
     * @return The state output for the state
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    @Override
    public String execute(final long executionID, final Map<String, Object> incomingFields) throws StateMachineException, ContextException {
        // Do execution pre work
        executePre(executionID, incomingFields);

        // Check and execute the Java logic
        boolean returnValue = false;
        try {
            // Find and call the method with the signature "public boolean getStateOutput(final StateFinalizerExecutionContext executor) throws ApexException"
            // to invoke the
            // task logic in the Java class
            final Method method = stateFinalizerLogicObject.getClass().getDeclaredMethod("getStateOutput",
                    new Class[] {StateFinalizerExecutionContext.class});
            returnValue = (boolean) method.invoke(stateFinalizerLogicObject, getExecutionContext());
        }
        catch (final Exception e) {
            LOGGER.error("execute: state finalizer logic failed to run for state finalizer  \"" + getSubject().getID() + "\"");
            throw new StateMachineException("state finalizer logic failed to run for state finalizer  \"" + getSubject().getID() + "\"", e);
        }

        // Do the execution post work
        executePost(returnValue);

        // Send back the return event
        if (returnValue) {
            return getOutgoing();
        }
        else {
            return null;
        }
    }

    /**
     * Cleans up the state finalizer after processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void cleanUp() throws StateMachineException {
        LOGGER.debug("cleanUp:" + getSubject().getID() + "," + getSubject().getLogicFlavour() + "," + getSubject().getLogic());
    }
}
