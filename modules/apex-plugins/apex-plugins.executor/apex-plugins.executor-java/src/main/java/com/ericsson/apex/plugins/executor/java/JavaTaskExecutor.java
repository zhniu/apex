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
import com.ericsson.apex.core.engine.executor.TaskExecutor;
import com.ericsson.apex.core.engine.executor.context.TaskExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JavaTaskExecutor is the task executor for task logic written in Java.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavaTaskExecutor extends TaskExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JavaTaskExecutor.class);

    // The Java Task executor class
    private Object taskLogicObject = null;

    /**
     * Prepares the task for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Get the class for task execution
        try {
            // Create the task logic object from the byte code of the class
            taskLogicObject = Class.forName(getSubject().getTaskLogic().getLogic()).newInstance();
        }
        catch (final Exception e) {
            LOGGER.error("instantiation error on Java class \"" + getSubject().getTaskLogic().getLogic() + "\"", e);
            throw new StateMachineException("instantiation error on Java class \"" + getSubject().getTaskLogic().getLogic() + "\"", e);
        }
    }

    /**
     * Executes the executor for the task in a sequential manner.
     *
     * @param executionID the execution ID for the current APEX policy execution
     * @param incomingFields the incoming fields
     * @return The outgoing fields
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    @Override
    public Map<String, Object> execute(final long executionID, final Map<String, Object> incomingFields) throws StateMachineException, ContextException {
        // Do execution pre work
        executePre(executionID, incomingFields);

        // Check and execute the Java logic
        boolean returnValue = false;
        try {
            // Find and call the method with the signature "public boolean getEvent(final TaskExecutionContext executor) throws ApexException" to invoke the
            // task logic in the Java class
            final Method method = taskLogicObject.getClass().getDeclaredMethod("getEvent", new Class[] {TaskExecutionContext.class});
            returnValue = (boolean) method.invoke(taskLogicObject, getExecutionContext());
        }
        catch (final Exception e) {
            LOGGER.error("execute: task logic failed to run for task  \"" + getSubject().getKey().getID() + "\"");
            throw new StateMachineException("task logic failed to run for task  \"" + getSubject().getKey().getID() + "\"", e);
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
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getTaskLogic().getLogicFlavour() + ","
                + getSubject().getTaskLogic().getLogic());
    }
}
