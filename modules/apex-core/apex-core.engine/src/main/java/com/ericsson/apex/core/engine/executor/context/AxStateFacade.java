/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.executor.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxTasks;

/**
 * The Class AxStateFacade acts as a facade into the AxState class so that task logic can easily access information in an AxState instance.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class AxStateFacade {
    // CHECKSTYLE:OFF: checkstyle:visibilityModifier Logic has access to this field

    /** The full definition information for the state. */
    public AxState state;

    // CHECKSTYLE:ON: checkstyle:visibilityModifier

    /**
     * Instantiates a new AxState facade.
     *
     * @param state the state for which a facade is being presented
     */
    public AxStateFacade(final AxState state) {
        this.state = state;
    }

    /**
     * Gets the default task key of the state.
     *
     * @return the default task key
     */
    public AxArtifactKey getDefaultTaskKey() {
        return state.getDefaultTask();
    }

    /**
     * Gets the ID of the state.
     *
     * @return the ID
     */
    public String getId() {
        return state.getKey().getID();
    }

    /**
     * Gets the name of the state.
     *
     * @return the state name
     */
    public String getStateName() {
        return state.getKey().getLocalName();
    }

    /**
     * Check if a task is defined for a given task name on a state and, if so, return its key.
     *
     * @param taskName the name of the task to get
     * @return the task key or null if it does not exist
     */
    public AxArtifactKey getTaskKey(final String taskName) {
        if (taskName == null) {
            return null;
        }

        return ModelService.getModel(AxTasks.class).get(taskName).getKey();
    }

    /**
     * Check if a task is defined for a given task name on a state and, if so, return its key.
     *
     * @return unmodifiable list of names of tasks available
     */
    public List<String> getTaskNames() {
        final Set<AxArtifactKey> tasks = state.getTaskReferences().keySet();
        final List<String> ret = new ArrayList<>(tasks.size());
        for (final AxArtifactKey task : tasks) {
            ret.add(task.getName());
        }
        return Collections.unmodifiableList(ret);
    }
}
