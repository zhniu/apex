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

import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateFinalizerLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTask;

/**
 * This class is used by the state machine to get implementations of task selection and task executors.
 *
 * @author Liam Fallon
 */

public abstract class ExecutorFactory {
    /**
     * Get an executor for task selection logic.
     *
     * @param stateExecutor the state executor that is requesting the task selection executor
     * @param state the state containing the task selection logic
     * @param context the context the context in which the task selection logic will execute
     * @return The executor that will run the task selection logic
     */
    public abstract TaskSelectExecutor getTaskSelectionExecutor(Executor<?, ?, ?, ?> stateExecutor, AxState state, ApexInternalContext context);

    /**
     * Get an executor for task logic.
     *
     * @param stateExecutor the state executor that is requesting the task executor
     * @param task the task containing the task logic
     * @param context the context the context in which the task logic will execute
     * @return The executor that will run the task logic
     */
    public abstract TaskExecutor getTaskExecutor(Executor<?, ?, ?, ?> stateExecutor, AxTask task, ApexInternalContext context);

    /**
     * Get an executor for state finalizer logic.
     *
     * @param stateExecutor the state executor that is requesting the state finalizer executor
     * @param logic the state finalizer logic to execute
     * @param context the context the context in which the state finalizer logic will execute
     * @return The executor that will run the state finalizer logic
     */
    public abstract StateFinalizerExecutor getStateFinalizerExecutor(Executor<?, ?, ?, ?> stateExecutor, AxStateFinalizerLogic logic,
            ApexInternalContext context);
}
