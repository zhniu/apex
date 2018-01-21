/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * The State Bean.
 */
@XmlType
public class BeanState extends BeanBase {

    private String name = null;
    private BeanKeyRef trigger = null;
    private BeanKeyRef defaultTask = null;
    private BeanKeyRef[] contexts = null;
    private BeanLogic taskSelectionLogic = null;
    private Map<String, BeanStateTaskRef> tasks = null;
    private Map<String, BeanLogic> finalizers = null;
    private Map<String, BeanStateOutput> stateOutputs = null;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the trigger.
     *
     * @return the trigger
     */
    public BeanKeyRef getTrigger() {
        return trigger;
    }

    /**
     * Gets the contexts.
     *
     * @return the contexts
     */
    public BeanKeyRef[] getContexts() {
        return contexts;
    }

    /**
     * Gets the task selection logic.
     *
     * @return the task selection logic
     */
    public BeanLogic getTaskSelectionLogic() {
        return taskSelectionLogic;
    }

    /**
     * Gets the tasks.
     *
     * @return the tasks
     */
    public Map<String, BeanStateTaskRef> getTasks() {
        return tasks;
    }

    /**
     * Gets the finalizers.
     *
     * @return the finalizers
     */
    public Map<String, BeanLogic> getFinalizers() {
        return finalizers;
    }

    /**
     * Gets the state outputs.
     *
     * @return the state outputs
     */
    public Map<String, BeanStateOutput> getStateOutputs() {
        return stateOutputs;
    }

    /**
     * Gets the default task.
     *
     * @return the default task
     */
    public BeanKeyRef getDefaultTask() {
        return defaultTask;
    }

    /**
     * Sets the default task.
     *
     * @param defaultTask the new default task
     */
    public void setDefaultTask(final BeanKeyRef defaultTask) {
        this.defaultTask = defaultTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "State [name=" + name + ", trigger=" + trigger + ", defaultTask=" + defaultTask + ", contexts=" + Arrays.toString(contexts)
                + ", taskSelectionLogic=" + taskSelectionLogic + ", tasks=" + tasks + ", finalizers=" + finalizers + ", stateOutputs=" + stateOutputs + "]";
    }

}
