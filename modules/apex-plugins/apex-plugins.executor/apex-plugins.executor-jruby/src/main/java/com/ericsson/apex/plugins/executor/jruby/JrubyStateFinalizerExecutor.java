/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.jruby;

import java.util.Map;

import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.StateFinalizerExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class JrubyStateFinalizerExecutor is the state finalizer executor for state finalizer logic written in JRuby.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JrubyStateFinalizerExecutor extends StateFinalizerExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JrubyStateFinalizerExecutor.class);

    // Jruby container
    private ScriptingContainer container = new ScriptingContainer(LocalContextScope.CONCURRENT, LocalVariableBehavior.TRANSIENT, true);
    private EmbedEvalUnit parsedjruby = null;

    /**
     * Prepares the state finalizer for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Set up the JRuby engine
        container = (container == null) ? new ScriptingContainer(LocalContextScope.CONCURRENT, LocalVariableBehavior.TRANSIENT, true) : container;
        container.setError(System.err);
        container.setOutput(System.out);
        container.put("executor", getExecutionContext()); // needed for the compile
        parsedjruby = container.parse(getSubject().getLogic());
    }

    /**
     * Executes the executor for the state finalizer logic in a sequential manner.
     *
     * @param incomingFields the incoming fields for finalisation
     * @return The state output for the state
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    @Override
    public String execute(final Map<String, Object> incomingFields) throws StateMachineException, ContextException {
        // Do execution pre work
        executePre(incomingFields);

        // Check and execute the JRuby logic
        container.put("executor", getExecutionContext());

        /* Precompiled version */
        boolean returnValue = false;
        final IRubyObject ret = parsedjruby.run();
        if (ret != null) {
            final Boolean retbool = (Boolean) ret.toJava(java.lang.Boolean.class);
            if (retbool != null) {
                returnValue = true;
            }
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
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getLogicFlavour() + "," + getSubject().getLogic());
        container.terminate();
        container = null;
    }
}
