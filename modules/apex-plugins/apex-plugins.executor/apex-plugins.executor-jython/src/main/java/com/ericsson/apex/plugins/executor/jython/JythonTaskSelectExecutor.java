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

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.TaskSelectExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class JythonTaskSelectExecutor is the task selection executor for task selection logic written in Jython It is unlikely that this is thread safe.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JythonTaskSelectExecutor extends TaskSelectExecutor {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JythonTaskSelectExecutor.class);

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
                compiled = Py.compile_flags(getSubject().getTaskSelectionLogic().getLogic(), "<" + getSubject().getKey().toString() + ">", "exec", null);
            }
        }
        catch (final PyException e) {
            LOGGER.warn("failed to compile Jython code for task selection logic in " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to compile Jython code for task selection logic in " + getSubject().getKey().getID(), e);
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

        boolean returnValue = false;

        // Do execution pre work
        executePre(executionID, incomingEvent);

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
                        LOGGER.error("execute: task selection logic failed to set a return value for state  \"" + getSubject().getKey().getID() + "\"");
                        throw new StateMachineException(
                                "execute: task selection logic failed to set a return value for state  \"" + getSubject().getKey().getID() + "\"");
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
            LOGGER.warn("failed to execute Jython code for task selection logic in " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to execute Jython code for task selection logic in " + getSubject().getKey().getID(), e);
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
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getTaskSelectionLogic().getLogicFlavour() + ","
                + getSubject().getTaskSelectionLogic().getLogic());
    }
}
