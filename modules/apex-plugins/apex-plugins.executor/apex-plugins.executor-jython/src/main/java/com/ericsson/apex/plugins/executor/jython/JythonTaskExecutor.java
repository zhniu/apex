/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.jython;

import java.util.Map;

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.TaskExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JythonTaskExecutor is the task executor for task logic written in Jython It is unlikely that this is thread safe.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JythonTaskExecutor extends TaskExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JythonTaskExecutor.class);

    // The Jython interpreter
    private final PythonInterpreter interpreter = new PythonInterpreter();
    private PyCode compiled = null;

    /**
     * Prepares the task for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        interpreter.setErr(System.err);
        interpreter.setOut(System.out);

        // Call generic prepare logic
        super.prepare();
        try {
            synchronized (Py.class) {
                compiled = Py.compile_flags(getSubject().getTaskLogic().getLogic(), "<" + getSubject().getKey().toString() + ">", "exec", null);
            }
        }
        catch (final PyException e) {
            LOGGER.warn("failed to compile Jython code for task " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to compile Jython code for task " + getSubject().getKey().getID(), e);
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

        boolean returnValue = false;

        // Do execution pre work
        executePre(executionID, incomingFields);

        try {

            // Check and execute the Jython logic
            /* Precompiled Version */
            synchronized (Py.class) {
                // Set up the Jython engine
                interpreter.set("executor", getExecutionContext());
                interpreter.exec(compiled);
                try {
                    Object ret = interpreter.get("returnValue", java.lang.Boolean.class);
                    if (ret == null) {
                        LOGGER.error("execute: task logic failed to set a return value for task  \"" + getSubject().getKey().getID() + "\"");
                        throw new StateMachineException("execute: task logic failed to set a return value for task  \"" + getSubject().getKey().getID() + "\"");
                    }
                    returnValue = (Boolean) ret;
                }
                catch (NullPointerException | ClassCastException e) {
                    LOGGER.error("execute: task selection logic failed to set a correct return value for state  \"" + getSubject().getKey().getID() + "\"", e);
                    throw new StateMachineException(
                            "execute: task selection logic failed to set a return value for state  \"" + getSubject().getKey().getID() + "\"", e);
                }
            }
            /* */
        }
        catch (final Exception e) {
            LOGGER.warn("failed to execute Jython code for task " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to execute Jython code for task " + getSubject().getKey().getID(), e);
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
        interpreter.cleanup();
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getTaskLogic().getLogicFlavour() + ","
                + getSubject().getTaskLogic().getLogic());
    }
}
