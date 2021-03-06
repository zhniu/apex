/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.executor;

import java.util.Map;
import java.util.TreeMap;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;

/**
 * This class is the executor for a state machine built from a policy.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class StateMachineExecutor implements Executor<EnEvent, EnEvent, AxPolicy, ApexInternalContext> {
    // The Apex Policy and context for this state machine
    private AxPolicy axPolicy = null;
    private Executor<?, ?, ?, ?> parent = null;
    private ApexInternalContext internalContext = null;

    // The list of state executors for this state machine
    private final Map<AxReferenceKey, StateExecutor> stateExecutorMap = new TreeMap<>();

    // The first executor
    private StateExecutor firstExecutor = null;

    // The next state machine executor
    private Executor<EnEvent, EnEvent, AxPolicy, ApexInternalContext> nextExecutor = null;

    // The executor factory
    private ExecutorFactory executorFactory = null;

    /**
     * Constructor, save the executor factory that will give us executors for task selection logic and task logic.
     *
     * @param executorFactory the executor factory
     * @param owner the artifact key of the owner of this state machine
     */
    public StateMachineExecutor(final ExecutorFactory executorFactory, final AxArtifactKey owner) {
        this.executorFactory = executorFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setContext(com.ericsson.apex.core.engine.executor.Executor, java.lang.Object, java.lang.Object)
     */
    @Override
    public void setContext(final Executor<?, ?, ?, ?> newParent, final AxPolicy newAxPolicy, final ApexInternalContext newInternalContext) {
        // Save the policy and context for this state machine
        this.parent = newParent;
        this.axPolicy = newAxPolicy;
        this.internalContext = newInternalContext;

        // Clear the first executor, setContext can be called multiple times
        firstExecutor = null;

        // Create the state executors for this state machine
        StateExecutor lastExecutor = null;
        for (final AxState state : axPolicy.getStateMap().values()) {
            // Create a state executor for this state and add its context (the state)
            final StateExecutor stateExecutor = new StateExecutor(executorFactory);
            stateExecutor.setContext(this, state, internalContext);

            // Update the next executor on the last executor
            if (lastExecutor != null) {
                lastExecutor.setNext(stateExecutor);
            }
            lastExecutor = stateExecutor;

            // Add the state executor to the executor list
            stateExecutorMap.put(state.getKey(), stateExecutor);

            // Set the first executor if it is not set
            if (state.getKey().getLocalName().equals(axPolicy.getFirstState())) {
                firstExecutor = stateExecutor;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#prepare()
     */
    @Override
    public void prepare() throws StateMachineException {
        for (final StateExecutor stateExecutor : stateExecutorMap.values()) {
            stateExecutor.prepare();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executeDirected(java.lang.long, java.lang.Object)
     */
    @Override
    public EnEvent execute(final long executionID, final EnEvent incomingEvent) throws StateMachineException, ContextException {
        // Check if there are any states on the state machine
        if (stateExecutorMap.size() == 0) {
            throw new StateMachineException("no states defined on state machine");
        }

        // Check if the first state of the machine is defined
        if (firstExecutor == null) {
            throw new StateMachineException("first state not defined on state machine");
        }

        // Get the first state of the state machine and define a state output that starts state execution
        StateExecutor stateExecutor = firstExecutor;
        StateOutput stateOutput = new StateOutput(
                new AxStateOutput(firstExecutor.getSubject().getKey(), incomingEvent.getKey(), firstExecutor.getSubject().getKey()), incomingEvent);
        while (true) {
            // Execute the state
            stateOutput = stateExecutor.execute(executionID, stateOutput.getOutputEvent());
            if (stateOutput == null) {
                throw new StateMachineException("state execution failed, invalid state output returned");
            }

            // Use the next state of the state output to find if all the states have executed
            if (stateOutput.getNextState().equals(AxReferenceKey.getNullKey())) {
                break;
            }

            // Use the next state of the state output to find the next state
            stateExecutor = stateExecutorMap.get(stateOutput.getNextState());
            if (stateExecutor == null) {
                throw new StateMachineException("state execution failed, next state \"" + stateOutput.getNextState().getID() + "\" not found");
            }
        }

        return stateOutput.getOutputEvent();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executePre(java.lang.long, java.lang.Object)
     */
    @Override
    public final void executePre(final long executionID, final EnEvent incomingEntity) throws StateMachineException {
        throw new StateMachineException("execution pre work not implemented on class");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executePost(boolean)
     */
    @Override
    public final void executePost(final boolean returnValue) throws StateMachineException {
        throw new StateMachineException("execution post work not implemented on class");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#cleanUp()
     */
    @Override
    public void cleanUp() throws StateMachineException {
        for (final StateExecutor stateExecutor : stateExecutorMap.values()) {
            stateExecutor.cleanUp();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getKey()
     */
    @Override
    public AxArtifactKey getKey() {
        return axPolicy.getKey();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getParent()
     */
    @Override
    public final Executor<?, ?, ?, ?> getParent() {
        return parent;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getSubject()
     */
    @Override
    public final AxPolicy getSubject() {
        return axPolicy;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getContext()
     */
    @Override
    public final ApexInternalContext getContext() {
        return internalContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getIncoming()
     */
    @Override
    public final EnEvent getIncoming() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getOutgoing()
     */
    @Override
    public final EnEvent getOutgoing() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setNext(com.ericsson.apex.core.engine.executor.Executor)
     */
    @Override
    public final void setNext(final Executor<EnEvent, EnEvent, AxPolicy, ApexInternalContext> newNextExecutor) {
        this.nextExecutor = newNextExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getNext()
     */
    @Override
    public final Executor<EnEvent, EnEvent, AxPolicy, ApexInternalContext> getNext() {
        return nextExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setParameters(com.ericsson.apex.core.engine.ExecutorParameters)
     */
    @Override
    public void setParameters(final ExecutorParameters parameters) {
    }
}
