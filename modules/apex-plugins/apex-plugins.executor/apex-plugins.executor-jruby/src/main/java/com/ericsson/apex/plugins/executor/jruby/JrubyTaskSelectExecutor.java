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

import java.util.Set;
import java.util.TreeSet;

import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.TaskSelectExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class JrubyTaskSelectExecutor is the task selection executor for task selection logic written in JRuby.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JrubyTaskSelectExecutor extends TaskSelectExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JrubyTaskSelectExecutor.class);

    // Jruby container
    private ScriptingContainer container = new ScriptingContainer(LocalContextScope.CONCURRENT, LocalVariableBehavior.TRANSIENT, true);
    private EmbedEvalUnit parsedjruby = null;

    /**
     * Prepares the task for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Set up the JRuby engine
        container = (container == null) ? new ScriptingContainer(LocalContextScope.CONCURRENT, LocalVariableBehavior.TRANSIENT, true) : container;
        container.put("executor", getExecutionContext()); // needed for compile as a placeholder
        parsedjruby = container.parse(getSubject().getTaskSelectionLogic().getLogic());
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
     * Cleans up the task after processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void cleanUp() throws StateMachineException {
        LOGGER.debug("cleanUp:" + getSubject().getKey().getID() + "," + getSubject().getTaskSelectionLogic().getLogicFlavour() + ","
                + getSubject().getTaskSelectionLogic().getLogic());
        container.terminate();
        container = null;
    }

    /**
     * Gets the output event set.
     *
     * @return the output event set
     */
    public Set<AxArtifactKey> getOutputEventSet() {
        return new TreeSet<>();
    }
}
