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
import com.ericsson.apex.core.engine.executor.StateFinalizerExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JythonStateFinalizerExecutor is the state finalizer executor for state finalizer logic written in Jython It is unlikely that this is thread safe.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JythonStateFinalizerExecutor extends StateFinalizerExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JythonStateFinalizerExecutor.class);

    // The Jython interpreter
    private final PythonInterpreter interpreter = new PythonInterpreter();
    private PyCode compiled = null;

    /**
     * Prepares the state finalizer for processing.
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
                compiled = Py.compile_flags(getSubject().getLogic(), "<" + getSubject().getKey().toString() + ">", "exec", null);
            }
        }
        catch (final PyException e) {
            LOGGER.warn("failed to compile Jython code for state finalizer " + getSubject().getKey(), e);
            throw new StateMachineException("failed to compile Jython code for state finalizer " + getSubject().getKey(), e);
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
                returnValue = (boolean) interpreter.get("returnValue", java.lang.Boolean.class);
            }
            /* */
        }
        catch (final Exception e) {
            LOGGER.warn("failed to execute Jython code for state finalizer " + getSubject().getKey(), e);
            throw new StateMachineException("failed to execute Jython code for state finalizer " + getSubject().getKey(), e);
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
        interpreter.cleanup();
        LOGGER.debug("cleanUp:" + getSubject().getKey() + "," + getSubject().getLogicFlavour() + "," + getSubject().getLogic());
    }
}
