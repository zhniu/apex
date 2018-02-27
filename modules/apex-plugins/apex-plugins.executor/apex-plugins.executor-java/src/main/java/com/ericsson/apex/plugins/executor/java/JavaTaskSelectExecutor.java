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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.TaskSelectExecutor;
import com.ericsson.apex.core.engine.executor.context.TaskSelectionExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class JavaTaskSelectExecutor is the task selection executor for task selection logic written in Java.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavaTaskSelectExecutor extends TaskSelectExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JavaTaskSelectExecutor.class);

    // The Java Task Selection executor class
    private Object taskSelectionLogicObject = null;

    /**
     * Prepares the task for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Get the class for task selection
        try {
            // Create the task logic object from the byte code of the class
            taskSelectionLogicObject = Class.forName(getSubject().getTaskSelectionLogic().getLogic()).newInstance();
        }
        catch (final Exception e) {
            LOGGER.error("instantiation error on Java class" + taskSelectionLogicObject, e);
            throw new StateMachineException("instantiation error on Java class" + taskSelectionLogicObject, e);
        }
    }

    /**
     * Executes the executor for the task in a sequential manner.
     *
     * @param executionID the execution ID for the current APEX policy execution
     * @param incomingEvent the incoming event
     * @return The outgoing event
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    @Override
    public AxArtifactKey execute(final long executionID, final EnEvent incomingEvent) throws StateMachineException, ContextException {
        // Do execution pre work
        executePre(executionID, incomingEvent);

        // Check and execute the Java logic
        boolean returnValue = false;
        try {
            // Find and call the method with the signature "public boolean getTask(final TaskSelectionExecutionContext executor)" to invoke the task selection
            // logic in the Java class
            final Method method = taskSelectionLogicObject.getClass().getDeclaredMethod("getTask", new Class[] {TaskSelectionExecutionContext.class});
            returnValue = (boolean) method.invoke(taskSelectionLogicObject, getExecutionContext());
        }
        catch (final Exception e) {
            LOGGER.error("execute: task selection logic failed to run for state  \"" + getSubject().getKey().getID() + "\"", e);
            throw new StateMachineException("task selection logic failed to run for state  \"" + getSubject().getKey().getID() + "\"", e);
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
     * Cleans up the task after processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void cleanUp() throws StateMachineException {
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getTaskSelectionLogic().getLogicFlavour() + ","
                + getSubject().getTaskSelectionLogic().getLogic());
    }
}
