/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine;

import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * This class provides the executors for a logic flavour. Plugin classes for execution of task logic, task selection logic, and state finalizer logic for the
 * logic flavour must be specified.
 * <p>
 * Specializations of this class may provide extra parameters for their specific logic flavour executors.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ExecutorParameters extends AbstractParameters {
    // Executor Plugin classes for executors
    private String taskExecutorPluginClass;
    private String taskSelectionExecutorPluginClass;
    private String stateFinalizerExecutorPluginClass;

    /**
     * Constructor to create an executor parameters instance and register the instance with the parameter service.
     */
    public ExecutorParameters() {
        super(ExecutorParameters.class.getCanonicalName());
        ParameterService.registerParameters(ExecutorParameters.class, this);
    }

    /**
     * Constructor to create an executor parameters instance with the name of a sub class of this class and register the instance with the parameter service.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public ExecutorParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the task executor plugin class for the executor.
     *
     * @return the task executor plugin class for the executor
     */
    public String getTaskExecutorPluginClass() {
        return taskExecutorPluginClass;
    }

    /**
     * Sets the task executor plugin class for the executor.
     *
     * @param taskExecutorPluginClass the task executor plugin class for the executor
     */
    public void setTaskExecutorPluginClass(final String taskExecutorPluginClass) {
        this.taskExecutorPluginClass = taskExecutorPluginClass;
    }

    /**
     * Gets the task selection executor plugin class for the executor.
     *
     * @return the task selection executor plugin class for the executor
     */
    public String getTaskSelectionExecutorPluginClass() {
        return taskSelectionExecutorPluginClass;
    }

    /**
     * Sets the task selection executor plugin class for the executor.
     *
     * @param taskSelectionExecutorPluginClass the task selection executor plugin class for the executor
     */
    public void setTaskSelectionExecutorPluginClass(final String taskSelectionExecutorPluginClass) {
        this.taskSelectionExecutorPluginClass = taskSelectionExecutorPluginClass;
    }

    /**
     * Gets the state finalizer executor plugin class for the executor.
     *
     * @return the state finalizer executor plugin class for the executor
     */
    public String getStateFinalizerExecutorPluginClass() {
        return stateFinalizerExecutorPluginClass;
    }

    /**
     * Sets the state finalizer executor plugin class for the executor.
     *
     * @param stateFinalizerExecutorPluginClass the state finalizer executor plugin class for the executor
     */
    public void setStateFinalizerExecutorPluginClass(final String stateFinalizerExecutorPluginClass) {
        this.stateFinalizerExecutorPluginClass = stateFinalizerExecutorPluginClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.service.AbstractParameters#toString()
     */
    @Override
    public String toString() {
        return "ExecutorParameters [taskExecutorPluginClass=" + taskExecutorPluginClass + ", taskSelectionExecutorPluginClass="
                + taskSelectionExecutorPluginClass + ", StateFinalizerExecutorPluginClass=" + stateFinalizerExecutorPluginClass + "]";
    }
}
