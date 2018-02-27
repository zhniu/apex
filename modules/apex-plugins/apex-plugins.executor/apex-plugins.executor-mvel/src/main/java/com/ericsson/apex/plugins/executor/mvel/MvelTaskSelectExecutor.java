/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.mvel;

import static com.ericsson.apex.model.utilities.Assertions.argumentNotNull;

import java.io.Serializable;
import java.util.HashMap;

import org.mvel2.MVEL;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.TaskSelectExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class MvelTaskSelectExecutor is the task selection executor for task selection logic written in MVEL.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class MvelTaskSelectExecutor extends TaskSelectExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MvelTaskSelectExecutor.class);

    // The MVEL code
    private Serializable compiled = null;

    /**
     * Prepares the task for processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    @Override
    public void prepare() throws StateMachineException {
        // Call generic prepare logic
        super.prepare();

        // Compile the MVEL code
        try {
            compiled = MVEL.compileExpression(getSubject().getTaskSelectionLogic().getLogic());
        }
        catch (final Exception e) {
            LOGGER.warn("failed to compile MVEL code for state " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to compile MVEL code for state " + getSubject().getKey().getID(), e);
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

        // Check and execute the MVEL logic
        argumentNotNull(compiled, "MVEL task not compiled.");

        boolean returnValue = false;
        try {
            // Execute the MVEL code
            returnValue = (boolean) MVEL.executeExpression(compiled, getExecutionContext(), new HashMap<String, Object>());
        }
        catch (final Exception e) {
            LOGGER.warn("failed to execute MVEL code for state " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to execute MVEL code for state " + getSubject().getKey().getID(), e);
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
