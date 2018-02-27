/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.engine.impl;

import java.util.HashMap;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.ExecutorFactory;
import com.ericsson.apex.core.engine.executor.StateMachineExecutor;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.core.engine.executor.impl.ExecutorFactoryImpl;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.policymodel.concepts.AxPolicies;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;

/**
 * This handler holds and manages state machines for each policy in an Apex engine. When the class is instantiated, an executor {@link StateMachineExecutor} is
 * created for each policy in the policy model the state machine handler will execute. The executors for each policy are held in a map indexed by event.
 * <p>
 * When an event is received on the policy, the state machine executor to execute that event is looked up on the executor map and the event is passed to the
 * executor for execution.
 *
 * @author Liam Fallon
 *
 */
public class StateMachineHandler {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(StateMachineHandler.class);

    // The key of the Apex model we are executing
    private final AxArtifactKey key;

    // The state machines in this engine
    private final HashMap<AxEvent, StateMachineExecutor> stateMachineExecutorMap = new HashMap<>();

    // The executor factory is used to get logic executors for the particular type of executor we need for task selection logic or task logic
    private final ExecutorFactory executorFactory;

    /**
     * This constructor builds the state machines for the policies in the apex model.
     *
     * @param internalContext The internal context we are using
     * @throws StateMachineException On state machine initiation errors
     */
    protected StateMachineHandler(final ApexInternalContext internalContext) throws StateMachineException {
        LOGGER.entry("StateMachineHandler()->" + internalContext.getKey().getID());

        key = internalContext.getKey();

        // Create the executor factory to generate executors as the engine runs policies
        executorFactory = new ExecutorFactoryImpl();

        // Iterate over the policies in the policy model and create a state machine for each one
        for (final AxPolicy policy : ModelService.getModel(AxPolicies.class).getPolicyMap().values()) {
            // Create a state machine for this policy
            final StateMachineExecutor thisStateMachineExecutor = new StateMachineExecutor(executorFactory, policy.getKey());

            // This executor is the top executor so has no parent
            thisStateMachineExecutor.setContext(null, policy, internalContext);

            // Get the incoming trigger event
            final AxEvent triggerEvent = ModelService.getModel(AxEvents.class).get(policy.getStateMap().get(policy.getFirstState()).getTrigger());

            // Put the state machine executor on the map for this trigger
            final StateMachineExecutor lastStateMachineExecutor = stateMachineExecutorMap.put(triggerEvent, thisStateMachineExecutor);
            if (lastStateMachineExecutor != null && lastStateMachineExecutor.getSubject() != thisStateMachineExecutor.getSubject()) {
                LOGGER.error("No more than one policy in a model can have the same trigger event. In model " + internalContext.getKey().getID() + " Policy ("
                        + lastStateMachineExecutor.getSubject().getKey().getID() + ") and Policy (" + thisStateMachineExecutor.getSubject().getKey().getID()
                        + ") have the same Trigger event (" + triggerEvent.getKey().getID() + ") ");
                LOGGER.error(" Policy (" + lastStateMachineExecutor.getSubject().getKey() + ") has overwritten Policy ("
                        + thisStateMachineExecutor.getSubject().getKey().getID() + " so this overwritten policy will never be triggered in this engine.");
            }
        }

        LOGGER.exit("StateMachineHandler()<-" + internalContext.getKey().getID());
    }

    /**
     * This constructor starts the state machines for each policy, carrying out whatever initialization executors need.
     *
     * @throws StateMachineException On state machine initiation errors
     */
    protected void start() throws StateMachineException {
        LOGGER.entry("start()->" + key.getID());

        // Iterate over the state machines
        for (final StateMachineExecutor smExecutor : stateMachineExecutorMap.values()) {
            try {
                smExecutor.prepare();
            }
            catch (final StateMachineException e) {
                final String stateMachineID = smExecutor.getContext().getKey().getID();
                LOGGER.warn("start()<-" + key.getID() + ", start failed, state machine \"" + stateMachineID + "\"", e);
                throw new StateMachineException("start()<-" + key.getID() + ", start failed, state machine \"" + stateMachineID + "\"", e);
            }
        }

        LOGGER.exit("start()<-" + key.getID());
    }

    /**
     * This method is called to execute an event on the state machines in an engine.
     *
     * @param event The trigger event for the state machine
     * @return The result of the state machine execution run
     * @throws StateMachineException On execution errors in a state machine
     */
    protected EnEvent execute(final EnEvent event) throws StateMachineException {
        LOGGER.entry("execute()->" + event.getName());

        // Try to execute the state machine for the trigger
        final StateMachineExecutor stateMachineExecutor = stateMachineExecutorMap.get(event.getAxEvent());
        if (stateMachineExecutor == null) {
            String exceptionMessage = "state machine execution not possible, policy not found for trigger event " + event.getName();
            LOGGER.warn(exceptionMessage);

            event.setExceptionMessage(exceptionMessage);
            return event;
        }

        // Run the state machine
        try {
            LOGGER.debug("execute(): state machine \"{}\" execution starting  . . .", stateMachineExecutor);
            final EnEvent outputObject = stateMachineExecutor.execute(event.getExecutionID(), event);

            LOGGER.debug("execute()<-: state machine \"{}\" execution completed", stateMachineExecutor);
            return outputObject;
        }
        catch (final Exception e) {
            LOGGER.warn("execute()<-: state machine \"" + stateMachineExecutor + "\" execution failed", e);
            throw new StateMachineException("execute()<-: execution failed on state machine " + stateMachineExecutor, e);
        }
    }

    /**
     * Closes down the state machines of an engine.
     */
    protected void stop() {
        LOGGER.entry("stop()->");

        // Iterate through all state machines and clean them
        for (final StateMachineExecutor smExecutor : stateMachineExecutorMap.values()) {
            try {
                smExecutor.cleanUp();
            }
            catch (final StateMachineException e) {
                final String smID = smExecutor.getContext().getKey().getID();
                LOGGER.warn("stop()<-clean up failed, state machine \"" + smID + "\" cleanup failed", e);
            }
        }
        LOGGER.exit("stop()<-");
    }
}
