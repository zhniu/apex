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
import java.util.Map;

import org.mvel2.MVEL;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.TaskExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;

/**
 * The Class MvelTaskExecutor is the task executor for task logic written in MVEL.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class MvelTaskExecutor extends TaskExecutor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(MvelTaskExecutor.class);

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
            compiled = MVEL.compileExpression(getSubject().getTaskLogic().getLogic());
        }
        catch (final Exception e) {
            LOGGER.warn("failed to compile MVEL code for task " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to compile MVEL code for task " + getSubject().getKey().getID(), e);
        }
        argumentNotNull(compiled, "MVEL task not compiled.");
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

        // Check and execute the MVEL logic
        argumentNotNull(compiled, "MVEL task not compiled.");
        boolean returnValue = false;

        try {
            // Execute the MVEL code
            returnValue = (boolean) MVEL.executeExpression(compiled, getExecutionContext(), new HashMap<String, Object>());
        }
        catch (final Exception e) {
            LOGGER.warn("failed to execute MVEL code for task " + getSubject().getKey().getID(), e);
            throw new StateMachineException("failed to execute MVEL code for task " + getSubject().getKey().getID(), e);
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
