/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2014-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.executor;

import static com.ericsson.apex.model.utilities.Assertions.argumentNotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.executor.context.TaskExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.eventmodel.concepts.AxInputField;
import com.ericsson.apex.model.eventmodel.concepts.AxOutputField;
import com.ericsson.apex.model.policymodel.concepts.AxTask;

/**
 * This abstract class executes a task in a state of an Apex policy and is specialized by classes that implement execution of task logic.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class TaskExecutor implements Executor<Map<String, Object>, Map<String, Object>, AxTask, ApexInternalContext> {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(TaskExecutor.class);

    // Hold the task and context definitions for this task
    private Executor<?, ?, ?, ?> parent = null;
    private AxTask axTask = null;
    private ApexInternalContext internalContext = null;

    // Holds the incoming and outgoing fields
    private Map<String, Object> incomingFields = null;
    private Map<String, Object> outgoingFields = null;

    // The next task executor
    private Executor<Map<String, Object>, Map<String, Object>, AxTask, ApexInternalContext> nextExecutor = null;

    // The task execution context; contains the facades for events and context to be used by tasks executed by this task executor
    private TaskExecutionContext executionContext = null;

    /**
     * Gets the execution internalContext.
     *
     * @return the execution context
     */
    protected TaskExecutionContext getExecutionContext() {
        return executionContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setContext(com.ericsson.apex.core.engine.executor.Executor, java.lang.Object, java.lang.Object)
     */
    @Override
    public void setContext(final Executor<?, ?, ?, ?> newParent, final AxTask newAxTask, final ApexInternalContext newInternalContext) {
        this.parent = newParent;
        this.axTask = newAxTask;
        this.internalContext = newInternalContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#prepare()
     */
    @Override
    public void prepare() throws StateMachineException {
        LOGGER.debug("prepare:" + axTask.getKey().getID() + "," + axTask.getTaskLogic().getLogicFlavour() + "," + axTask.getTaskLogic().getLogic());
        argumentNotNull(axTask.getTaskLogic().getLogic(), StateMachineException.class, "task logic cannot be null.");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#execute(java.lang.Object)
     */
    @Override
    public Map<String, Object> execute(final Map<String, Object> newIncomingFields) throws StateMachineException, ContextException {
        throw new StateMachineException("execute() not implemented on abstract TaskExecutor class, only on its subclasses");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executePre(java.lang.Object)
     */
    @Override
    public final void executePre(final Map<String, Object> newIncomingFields) throws StateMachineException, ContextException {
        LOGGER.debug("execute-pre:" + getSubject().getTaskLogic().getLogicFlavour() + "," + getSubject().getKey().getID() + ","
                + getSubject().getTaskLogic().getLogic());

        // Check that the incoming event has all the input fields for this state
        final Set<String> missingTaskInputFields = new TreeSet<>(axTask.getInputFields().keySet());
        missingTaskInputFields.removeAll(newIncomingFields.keySet());
        
        // Remove fields from the set that are optional
        for (Iterator<String> missingFieldIterator = missingTaskInputFields.iterator(); missingFieldIterator.hasNext(); ) {
            String missingField = missingFieldIterator.next();
            if (axTask.getInputFields().get(missingField).getOptional()) {
                missingTaskInputFields.remove(missingField);
            }
        }
        if (!missingTaskInputFields.isEmpty()) {
            throw new StateMachineException("task input fields \"" + missingTaskInputFields + "\" are missing for task \"" + axTask.getKey().getID() + "\"");
        }

        // Record the incoming fields
        this.incomingFields = newIncomingFields;

        // Initiate the outgoing fields
        outgoingFields = new TreeMap<>();
        for (final String outputFieldName : getSubject().getOutputFields().keySet()) {
            outgoingFields.put(outputFieldName, null);
        }

        // Get task context object
        executionContext = new TaskExecutionContext(this, getSubject(), getIncoming(), getOutgoing(), getContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executePost(boolean)
     */
    @Override
    public final void executePost(final boolean returnValue) throws StateMachineException, ContextException {
        if (!returnValue) {
            LOGGER.warn(
                    "execute-post: task logic execution failure on task \"" + axTask.getKey().getName() + "\" in model " + internalContext.getKey().getID());
            throw new StateMachineException(
                    "execute-post: task logic execution failure on task \"" + axTask.getKey().getName() + "\" in model " + internalContext.getKey().getID());
        }

        // Copy any unset fields from the input to the output if their data type and names are identical
        for (final String field : axTask.getOutputFields().keySet()) {
            // Check if the field exists and is not set on the output
            if (!getOutgoing().containsKey(field) || getOutgoing().get(field) != null) {
                continue;
            }

            // This field is not in the output, check if it's on the input and is the same type (Note here, the output field definition has to exist so it's not
            // null checked)
            final AxInputField inputFieldDef = axTask.getInputFields().get(field);
            final AxOutputField outputFieldDef = axTask.getOutputFields().get(field);
            if (inputFieldDef == null || !inputFieldDef.getSchema().equals(outputFieldDef.getSchema())) {
                continue;
            }

            // We have an input field that matches our output field, copy the value across
            getOutgoing().put(field, getIncoming().get(field));
        }

        // Finally, check that the outgoing fields have all the output fields defined for this state and, if not, output a list of missing fields
        final Set<String> missingTaskOutputFields = new TreeSet<>(axTask.getOutputFields().keySet());
        missingTaskOutputFields.removeAll(outgoingFields.keySet());
        
        // Remove fields from the set that are optional
        for (Iterator<String> missingFieldIterator = missingTaskOutputFields.iterator(); missingFieldIterator.hasNext(); ) {
            String missingField = missingFieldIterator.next();
            if (axTask.getInputFields().get(missingField).getOptional()) {
                missingTaskOutputFields.remove(missingField);
            }
        }
        if (!missingTaskOutputFields.isEmpty()) {
            throw new StateMachineException("task output fields \"" + missingTaskOutputFields + "\" are missing for task \"" + axTask.getKey().getID() + "\"");
        }

        // Finally, check that the outgoing field map don't have any extra fields, if present, raise exception with the list of extra fields
        final Set<String> extraTaskOutputFields = new TreeSet<>(outgoingFields.keySet());
        extraTaskOutputFields.removeAll(axTask.getOutputFields().keySet());
        if (!extraTaskOutputFields.isEmpty()) {
            throw new StateMachineException("task output fields \"" + extraTaskOutputFields + "\" are unwanted for task \"" + axTask.getKey().getID() + "\"");
        }

        LOGGER.debug("execute-post:" + axTask.getKey().getID() + ", returning fields " + outgoingFields.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#cleanUp()
     */
    @Override
    public void cleanUp() throws StateMachineException {
        throw new StateMachineException("cleanUp() not implemented on class");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getKey()
     */
    @Override
    public AxArtifactKey getKey() {
        return axTask.getKey();
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
    public AxTask getSubject() {
        return axTask;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getContext()
     */
    @Override
    public ApexInternalContext getContext() {
        return internalContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getIncoming()
     */
    @Override
    public Map<String, Object> getIncoming() {
        return incomingFields;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getOutgoing()
     */
    @Override
    public Map<String, Object> getOutgoing() {
        return outgoingFields;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setNext(com.ericsson.apex.core.engine.executor.Executor)
     */
    @Override
    public void setNext(final Executor<Map<String, Object>, Map<String, Object>, AxTask, ApexInternalContext> newNextExecutor) {
        this.nextExecutor = newNextExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getNext()
     */
    @Override
    public Executor<Map<String, Object>, Map<String, Object>, AxTask, ApexInternalContext> getNext() {
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
