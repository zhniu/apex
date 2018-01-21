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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.executor.Executor;
import com.ericsson.apex.core.engine.executor.StateFinalizerExecutor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.policymodel.concepts.AxState;

/**
 * Container class for the execution context for state finalizer logic executions in a state being executed in an Apex engine. The state finalizer must have
 * easy access to the state definition, the fields, as well as the policy, global, and external context.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class StateFinalizerExecutionContext {
    /** Logger for state finalizer execution, state finalizer logic can use this field to access and log to Apex logging. */
    private static final XLogger EXCEUTION_LOGGER = XLoggerFactory.getXLogger("com.ericsson.apex.executionlogging.StateFinalizerExecutionLogging");

    // CHECKSTYLE:OFF: checkstyle:VisibilityModifier Logic has access to these field

    /** A facade to the full state definition for the state finalizer logic being executed. */
    public final AxStateFacade subject;

    /**
     * The list of state outputs for this state finalizer. The purpose of a state finalizer is to select a state output for a state from this list of state
     * output names.
     */
    public final Set<String> stateOutputNames;

    /**
     * The fields of this state finalizer. A state finalizer receives this list of fields from a task and may use these fields to determine what state output to
     * select. Once a state finalizer has selected a state output, it must marshal these fields so that they match the fields required for the event defined in
     * the state output.
     */
    public Map<String, Object> fields;

    /**
     * The state output that the state finalizer logic has selected for a state. The state finalizer logic sets this field in its logic after executing and the Apex engine
     * uses this state output for this state.
     */
    private String selectedStateOutputName;


    /** Logger for state finalizer execution, state finalizer logic can use this field to access and log to Apex logging. */
    public final XLogger logger = EXCEUTION_LOGGER;

    // CHECKSTYLE:ON: checkstyle:visibilityModifier

    // All available context albums
    private final Map<String, ContextAlbum> context;

    /**
     * Instantiates a new state finalizer execution context.
     *
     * @param stateFinalizerExecutor the state finalizer executor that requires context
     * @param axState the state definition that is the subject of execution
     * @param fields the fields to be manipulated by the state finalizer
     * @param stateOutputNames the state output names, one of which will be selected by the state finalizer
     * @param internalContext the execution context of the Apex engine in which the task is being executed
     */
    public StateFinalizerExecutionContext(final StateFinalizerExecutor stateFinalizerExecutor, final AxState axState, final Map<String, Object> fields,
            final Set<String> stateOutputNames, final ApexInternalContext internalContext) {
        subject = new AxStateFacade(axState);
        this.fields = fields;
        this.stateOutputNames = stateOutputNames;

        // Set up the context albums for this task
        context = new TreeMap<>();
        for (final AxArtifactKey mapKey : subject.state.getContextAlbumReferences()) {
            context.put(mapKey.getName(), internalContext.getContextAlbums().get(mapKey));
        }

        // Get the artifact stack of the users of the policy
        final List<AxConcept> usedArtifactStack = new ArrayList<>();
        for (Executor<?, ?, ?, ?> parent = stateFinalizerExecutor.getParent(); parent != null; parent = parent.getParent()) {
            // Add each parent to the top of the stack
            usedArtifactStack.add(0, parent.getKey());
        }

        // Change the stack to an array
        final AxConcept[] usedArtifactStackArray = usedArtifactStack.toArray(new AxConcept[usedArtifactStack.size()]);

        // Set the user of the context
        // Set the user of the context
        for (final ContextAlbum contextAlbum : context.values()) {
            contextAlbum.setUserArtifactStack(usedArtifactStackArray);
        }
    }

    /**
     * Return a context album if it exists in the context definition of this state.
     *
     * @param contextAlbumName The context album name
     * @return The context album
     * @throws ContextRuntimeException if the context album does not exist on the state for this executor
     */
    public ContextAlbum getContextAlbum(final String contextAlbumName) throws ContextRuntimeException {
        // Find the context album
        final ContextAlbum foundContextAlbum = context.get(contextAlbumName);

        // Check if the context album exists
        if (foundContextAlbum != null) {
            return foundContextAlbum;
        }
        else {
            throw new ContextRuntimeException("cannot find definition of context album \"" + contextAlbumName + "\" on state \"" + subject.getId() + "\"");
        }
    }
    
    /**
    * Return the state output name selected by the state finalizer logic.
    * @return the state output name
    */
    public String getSelectedStateOutputName() {
        return selectedStateOutputName;
    }

    /**
    * Set the state output name selected by the state finalizer logic.
    * @param selectedStateOutputName the state output name
    */
    public void setSelectedStateOutputName(final String selectedStateOutputName) {
        this.selectedStateOutputName = selectedStateOutputName;
    }

}
