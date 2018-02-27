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
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.executor.Executor;
import com.ericsson.apex.core.engine.executor.TaskExecutor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.policymodel.concepts.AxTask;

/**
 * Container class for the execution context for Task logic executions in a task being executed in an Apex engine. The task must have easy access to the task
 * definition, the incoming and outgoing field contexts, as well as the policy, global, and external context.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class TaskExecutionContext {
    // Logger for task execution
    private static final XLogger EXECUTION_LOGGER = XLoggerFactory.getXLogger("com.ericsson.apex.executionlogging.TaskExecutionLogging");

    // CHECKSTYLE:OFF: checkstyle:VisibilityModifier Logic has access to these field

    /** A constant <code>boolean true</code> value available for reuse e.g., for the return value*/
    public final Boolean TRUE = true;

    /** A constant <code>boolean false</code> value available for reuse e.g., for the return value*/
    public final Boolean FALSE = false;

    /** A facade to the full task definition for the task logic being executed. */
    public final AxTaskFacade subject;

    /** 	the execution ID for the current APEX policy execution instance. */
	public final Long executionID;

    /**
     * The incoming fields from the trigger event for the task. The task logic can access these fields when executing its logic.
     */
    public final Map<String, Object> inFields;

    /**
     * The outgoing fields from the task. The task logic can access and set these fields with its logic. A task outputs its result using these fields.
     */
    public final Map<String, Object> outFields;

    /** Logger for task execution, task logic can use this field to access and log to Apex logging. */
    public final XLogger logger = EXECUTION_LOGGER;

    // CHECKSTYLE:ON: checkstyle:VisibilityModifier

    // All available context albums
    private final Map<String, ContextAlbum> context;

    // The artifact stack of users of this context
    private final List<AxConcept> usedArtifactStack;

    /**
     * Instantiates a new task execution context.
     *
     * @param taskExecutor the task executor that requires context
     * @param executionID the execution ID for the current APEX policy execution instance
     * @param axTask the task definition that is the subject of execution
     * @param inFields the in fields
     * @param outFields the out fields
     * @param internalContext the execution context of the Apex engine in which the task is being executed
     */
    public TaskExecutionContext(final TaskExecutor taskExecutor, final long executionID, final AxTask axTask, final Map<String, Object> inFields, final Map<String, Object> outFields,
            final ApexInternalContext internalContext) {
        // The subject is the task definition
        subject = new AxTaskFacade(axTask);

        // Execution ID is the current policy execution instance
        this.executionID = executionID;
        
        // The input and output fields
        this.inFields = Collections.unmodifiableMap(inFields);
        this.outFields = outFields;

        // Set up the context albums for this task
        context = new TreeMap<>();
        for (final AxArtifactKey mapKey : subject.task.getContextAlbumReferences()) {
            context.put(mapKey.getName(), internalContext.getContextAlbums().get(mapKey));
        }

        // Get the artifact stack of the users of the policy
        usedArtifactStack = new ArrayList<>();
        for (Executor<?, ?, ?, ?> parent = taskExecutor.getParent(); parent != null; parent = parent.getParent()) {
            // Add each parent to the top of the stack
            usedArtifactStack.add(0, parent.getKey());
        }

        // Change the stack to an array
        final AxConcept[] usedArtifactStackArray = usedArtifactStack.toArray(new AxConcept[usedArtifactStack.size()]);

        // Set the user of the context
        for (final ContextAlbum contextAlbum : context.values()) {
            contextAlbum.setUserArtifactStack(usedArtifactStackArray);
        }
    }

    /**
     * Return a context album if it exists in the context definition of this task.
     *
     * @param contextAlbumName The context album name
     * @return The context album
     * @throws ContextRuntimeException if the context album does not exist on the task for this executor
     */
    public ContextAlbum getContextAlbum(final String contextAlbumName) throws ContextRuntimeException {
        // Find the context album
        final ContextAlbum foundContextAlbum = context.get(contextAlbumName);

        // Check if the context album exists
        if (foundContextAlbum != null) {
            return foundContextAlbum;
        }
        else {
            throw new ContextRuntimeException("cannot find definition of context album \"" + contextAlbumName + "\" on task \"" + subject.getId() + "\"");
        }
    }
}
