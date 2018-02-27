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
import java.util.TreeMap;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.Executor;
import com.ericsson.apex.core.engine.executor.TaskSelectExecutor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.policymodel.concepts.AxState;

/**
 * Container class for the execution context for Task Selection logic executions in a task being executed in an Apex engine. The task must have easy access to
 * the state definition, the incoming and outgoing event contexts, as well as the policy, global, and external context.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class TaskSelectionExecutionContext {
    // Logger for task execution
    private static final XLogger EXECUTION_LOGGER = XLoggerFactory.getXLogger("com.ericsson.apex.executionlogging.TaskSelectionExecutionLogging");

    // CHECKSTYLE:OFF: checkstyle:VisibilityModifier Logic has access to these field

    /** A constant <code>boolean true</code> value available for reuse e.g., for the return value*/
    public final Boolean TRUE = true;

    /** A constant <code>boolean false</code> value available for reuse e.g., for the return value*/
    public final Boolean FALSE = false;

    /** A facade to the full state definition for the task selection logic being executed. */
    public final AxStateFacade subject;

    /** 	the execution ID for the current APEX policy execution instance. */
	public final Long executionID;

    /**
     * The incoming fields from the trigger event for the state. The task selection logic can access these fields to decide what task to select for the state.
     */
    public final Map<String, Object> inFields;

    /**
     * The task that the task selection logic has selected for a state. The task selection logic sets this field in its logic prior to executing and the Apex engine
     * executes this task as the task for this state.
     */
    public final AxArtifactKey selectedTask;

    /** Logger for task selection execution, task selection logic can use this field to access and log to Apex logging. */
    public final XLogger logger = EXECUTION_LOGGER;

    // CHECKSTYLE:ON: checkstyle:VisibilityModifier

    // All available context albums
    private final Map<String, ContextAlbum> context;

    /**
     * Instantiates a new task selection execution context.
     *
     * @param taskSelectExecutor the task selection executor that requires context
     * @param axState the state definition that is the subject of execution
     * @param incomingEvent the incoming event for the state
     * @param outgoingKey the outgoing key for the task to execute in this state
     * @param internalContext the execution context of the Apex engine in which the task is being executed
     */
    public TaskSelectionExecutionContext(final TaskSelectExecutor taskSelectExecutor, final long executionID, final AxState axState, final EnEvent incomingEvent,
            final AxArtifactKey outgoingKey, final ApexInternalContext internalContext) {
        // The subject is the state definition
        subject = new AxStateFacade(axState);

        // Execution ID is the current policy execution instance
        this.executionID = executionID;
        
       // The events
        inFields = incomingEvent;
        selectedTask = outgoingKey;

        // Set up the context albums for this task
        // Set up the context albums for this task
        context = new TreeMap<>();
        for (final AxArtifactKey mapKey : subject.state.getContextAlbumReferences()) {
            context.put(mapKey.getName(), internalContext.getContextAlbums().get(mapKey));
        }

        // Get the artifact stack of the users of the policy
        final List<AxConcept> usedArtifactStack = new ArrayList<>();
        for (Executor<?, ?, ?, ?> parent = taskSelectExecutor.getParent(); parent != null; parent = parent.getParent()) {
            // Add each parent to the top of the stack
            usedArtifactStack.add(0, parent.getKey());
        }

        // Add the events to the artifact stack
        usedArtifactStack.add(incomingEvent.getKey());

        // Change the stack to an array
        final AxConcept[] usedArtifactStackArray = usedArtifactStack.toArray(new AxConcept[usedArtifactStack.size()]);

        // Set the user of the context
        // Set the user of the context
        for (final ContextAlbum contextAlbum : context.values()) {
            contextAlbum.setUserArtifactStack(usedArtifactStackArray);
        }
        incomingEvent.setUserArtifactStack(usedArtifactStackArray);
    }

    /**
     * Return a context album if it exists in the context definition of this state.
     *
     * @param contextAlbumName The context album name
     * @return The context albumxxxxxx
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
}
