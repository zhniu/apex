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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.core.engine.executor.exception.StateMachineRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateFinalizerLogic;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskOutputType;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskReference;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.model.policymodel.concepts.AxTasks;

/**
 * This class is the executor for a state of a policy.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class StateExecutor implements Executor<EnEvent, StateOutput, AxState, ApexInternalContext> {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(StateExecutor.class);

    // Hold the state and context definitions for this state
    private AxState axState = null;
    private Executor<?, ?, ?, ?> parent = null;
    private ApexInternalContext context = null;

    // Holds the incoming event and the state output for this state
    private EnEvent lastIncomingEvent = null;
    private StateOutput lastStateOutput = null;

    // The task selection logic executor
    private TaskSelectExecutor taskSelectExecutor = null;

    // The map of task executors for this state
    private final Map<AxArtifactKey, TaskExecutor> taskExecutorMap = new HashMap<>();

    // The map of state outputs used directly by tasks
    private final Map<AxArtifactKey, String> directStateOutputMap = new HashMap<>();

    // The map of state finalizer logic executors used by tasks
    private final Map<AxArtifactKey, StateFinalizerExecutor> task2StateFinalizerMap = new HashMap<>();

    // The next state executor
    private Executor<EnEvent, StateOutput, AxState, ApexInternalContext> nextExecutor = null;

    // The executor factory
    private ExecutorFactory executorFactory = null;

    /**
     * Constructor, save the executor factory.
     *
     * @param executorFactory the executor factory to use for getting executors for task selection logic
     */
    public StateExecutor(final ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setContext(com.ericsson.apex.core.engine.executor.Executor, java.lang.Object, java.lang.Object)
     */
    @Override
    public void setContext(final Executor<?, ?, ?, ?> incomingParent, final AxState incomingAxState, final ApexInternalContext incomingContext) {
        // Save the state and context definition
        this.parent = incomingParent;
        this.axState = incomingAxState;
        this.context = incomingContext;

        // Set the task selection executor
        taskSelectExecutor = executorFactory.getTaskSelectionExecutor(this, axState, context);

        // Set a task executor for each task
        for (final Entry<AxArtifactKey, AxStateTaskReference> stateTaskReferenceEntry : axState.getTaskReferences().entrySet()) {
            final AxArtifactKey taskKey = stateTaskReferenceEntry.getKey();
            final AxStateTaskReference taskReference = stateTaskReferenceEntry.getValue();

            // Get the task
            final AxTask task = ModelService.getModel(AxTasks.class).get(taskKey);

            // Create a task executor for the task
            taskExecutorMap.put(taskKey, executorFactory.getTaskExecutor(this, task, context));

            // Check what type of output is specified for the task on this sate
            if (taskReference.getStateTaskOutputType().equals(AxStateTaskOutputType.DIRECT)) {
                // Create a task state output reference for this task
                directStateOutputMap.put(taskKey, taskReference.getOutput().getLocalName());
            }
            else if (taskReference.getStateTaskOutputType().equals(AxStateTaskOutputType.LOGIC)) {
                // Get the state finalizer logic for this task
                final AxStateFinalizerLogic finalizerLogic = axState.getStateFinalizerLogicMap().get(taskReference.getOutput().getLocalName());
                if (finalizerLogic == null) {
                    // Finalizer logic for the task does not exist
                    throw new StateMachineRuntimeException(
                            "state finalizer logic on task reference \"" + taskReference + "\" on state \"" + axState.getID() + "\" does not exist");
                }

                // Create a state finalizer executor for the task
                task2StateFinalizerMap.put(taskKey, executorFactory.getStateFinalizerExecutor(this, finalizerLogic, context));
            }
            else {
                // This should never happen but.....
                throw new StateMachineRuntimeException(
                        "invalid state output type on task reference \"" + taskReference + "\" on state \"" + axState.getID() + "\"");
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
        // There may be no task selection logic
        if (taskSelectExecutor != null) {
            // Prepare the task selector
            taskSelectExecutor.prepare();
        }

        // Prepare the tasks
        for (final TaskExecutor taskExecutor : taskExecutorMap.values()) {
            taskExecutor.prepare();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#execute(java.lang.long, java.lang.Object)
     */
    @Override
    public StateOutput execute(final long executionID, final EnEvent incomingEvent) throws StateMachineException, ContextException {
        this.lastIncomingEvent = incomingEvent;

        // Check that the incoming event matches the trigger for this state
        if (!incomingEvent.getAxEvent().getKey().equals(axState.getTrigger())) {
            throw new StateMachineException("incoming event \"" + incomingEvent.getID() + "\" does not match trigger \"" + axState.getTrigger().getID()
                    + "\" of state \"" + axState.getID() + "\"");
        }

        // The key of the task to execute
        AxArtifactKey taskKey = null;

        try {
            // There may be no task selection logic, in which case just return the default task
            if (taskSelectExecutor != null) {
                // Fire the task selector to find the task to run
                taskKey = taskSelectExecutor.execute(executionID, incomingEvent);
            }

            // If there's no task selection logic or the TSL returned no task, just use the default task
            if (taskKey == null) {
                taskKey = axState.getDefaultTask();
            }

            // Execute the task
            final TreeMap<String, Object> incomingValues = new TreeMap<>();
            incomingValues.putAll(incomingEvent);
            final Map<String, Object> taskExecutionResultMap = taskExecutorMap.get(taskKey).execute(executionID, incomingValues);
            final AxTask task = taskExecutorMap.get(taskKey).getSubject();

            // Check if this task has direct output
            String stateOutputName = directStateOutputMap.get(taskKey);

            // If a direct state output name was not found, state finalizer logic should be defined for the task
            if (stateOutputName == null) {
                // State finalizer logic should exist for the task
                final StateFinalizerExecutor finalizerLogicExecutor = task2StateFinalizerMap.get(taskKey);
                if (finalizerLogicExecutor == null) {
                    throw new StateMachineException(
                            "state finalizer logic for task \"" + taskKey.getID() + "\" not found for state \"" + axState.getID() + "\"");
                }

                // Execute the state finalizer logic to select a state output and to adjust the taskExecutionResultMap
                stateOutputName = finalizerLogicExecutor.execute(incomingEvent.getExecutionID(), taskExecutionResultMap);
            }

            // Now look up the the actual state output
            final AxStateOutput stateOutputDefinition = axState.getStateOutputs().get(stateOutputName);
            if (stateOutputDefinition == null) {
                throw new StateMachineException(
                        "state output definition for state output \"" + stateOutputName + "\" not found for state \"" + axState.getID() + "\"");
            }

            // Create the state output and transfer all the fields across to its event
            final StateOutput stateOutput = new StateOutput(stateOutputDefinition);
            this.lastStateOutput = stateOutput;

            stateOutput.setEventFields(task.getRawOutputFields(), taskExecutionResultMap);

            // Copy across fields from the incoming event that are not set on the outgoing event
            stateOutput.copyUnsetFields(incomingEvent);

            // Set the ExecutionID for the outgoing event to the value in the incoming event.
            if (stateOutput != null && stateOutput.getOutputEvent() != null) {
                stateOutput.getOutputEvent().setExecutionID(incomingEvent.getExecutionID());
            }

            // That's it, the state execution is complete
            return stateOutput;
        }
        catch (Exception e) {
            String errorMessage = "State execution of state \"" + axState.getID() + "\" on task \""
                    + (taskKey != null ? taskKey.getID() : "null") + "\" failed: " + e.getMessage();

            LOGGER.warn(errorMessage);
            throw new StateMachineException(errorMessage, e);
        }
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
        // Clean the tasks
        for (final TaskExecutor taskExecutor : taskExecutorMap.values()) {
            taskExecutor.cleanUp();
        }

        if (taskSelectExecutor != null) {
            // Clean the task selector
            taskSelectExecutor.cleanUp();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getKey()
     */
    @Override
    public AxReferenceKey getKey() {
        return axState.getKey();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getParent()
     */
    @Override
    public Executor<?, ?, ?, ?> getParent() {
        return parent;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getSubject()
     */
    @Override
    public AxState getSubject() {
        return axState;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getContext()
     */
    @Override
    public final ApexInternalContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getIncoming()
     */
    @Override
    public final EnEvent getIncoming() {
        return lastIncomingEvent;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getOutgoing()
     */
    @Override
    public final StateOutput getOutgoing() {
        return lastStateOutput;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setNext(com.ericsson.apex.core.engine.executor.Executor)
     */
    @Override
    public final void setNext(final Executor<EnEvent, StateOutput, AxState, ApexInternalContext> incomingNextExecutor) {
        this.nextExecutor = incomingNextExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getNext()
     */
    @Override
    public final Executor<EnEvent, StateOutput, AxState, ApexInternalContext> getNext() {
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
