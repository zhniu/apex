/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.javascript;

import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.StateFinalizerExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JavascriptStateFinalizerExecutor is the state finalizer executor for state finalizer logic written in Javascript It is unlikely that this is thread
 * safe.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavascriptStateFinalizerExecutor extends StateFinalizerExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JavascriptStateFinalizerExecutor.class);

    // Javascript engine
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private CompiledScript compiled = null;

    /**
     * Prepares the state finalizer for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();
        try {
            compiled = ((Compilable) engine).compile(getSubject().getLogic());
        }
        catch (final ScriptException e) {
            LOGGER.error("execute: state finalizer logic failed to compile for state finalizer  \"" + getSubject().getKey().getID() + "\"");
            throw new StateMachineException("state finalizer logic failed to compile for state finalizer  \"" + getSubject().getKey().getID() + "\"", e);
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

        // Set up the Javascript engine
        engine.put("executor", getExecutionContext());

        // Check and execute the Javascript logic
        boolean returnValue = false;
        try {
            if (compiled == null) {
                engine.eval(getSubject().getLogic());
            }
            else {
                compiled.eval(engine.getContext());
            }
        }
        catch (final ScriptException e) {
            LOGGER.error("execute: state finalizer logic failed to run for state finalizer  \"" + getSubject().getKey().getID() + "\"");
            throw new StateMachineException("state finalizer logic failed to run for state finalizer  \"" + getSubject().getKey().getID() + "\"", e);
        }

        returnValue = (boolean) engine.get("returnValue");

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
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getLogicFlavour() + "," + getSubject().getLogic());
        engine = null;
    }
}
