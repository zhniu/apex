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

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;

/**
 * This interface defines what operations must be provided by an executing entity in Apex. It is implemented by classes that execute logic in a state machine.
 * Each executor has an incoming entity {@code IN} that triggers execution, an outgoing entity {@code OUT} that is produced by execution, a subject
 * {@code SUBJECT} that is being executed, and a context {@code CONTEXT} in which execution is being carried out. An executor can be part of a chain of
 * executors and the {@code setNext} method is used to set the next executor to be executed after this executor has completed.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 *
 * @param <IN> type of the incoming entity
 * @param <OUT> type of the outgoing entity
 * @param <SUBJECT> type that is the subject of execution
 * @param <CONTEXT> context holding the context of execution
 */

public interface Executor<IN, OUT, SUBJECT, CONTEXT> {
    /**
     * Save the subject and context of the executor.
     *
     * @param parent the parent executor of this executor or null if this executor is the top executor
     * @param executorSubject the executor subject, the subject of execution
     * @param executorContext the executor context, the context in which execution takes place
     */
    void setContext(Executor<?, ?, ?, ?> parent, SUBJECT executorSubject, CONTEXT executorContext);

    /**
     * Prepares the processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    void prepare() throws StateMachineException;

    /**
     * Executes the executor, running through its context in its natural order.
     *
     * @param executionID the execution ID of the current APEX execution policy thread
     * @param incomingEntity the incoming entity that triggers execution
     * @return The outgoing entity that is the result of execution
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    OUT execute(long executionID, IN incomingEntity) throws StateMachineException, ContextException;

    /**
     * Carry out the preparatory work for execution.
     *
     * @param executionID the execution ID of the current APEX execution policy thread
     * @param incomingEntity the incoming entity that triggers execution
     * @throws StateMachineException on an execution error
     * @throws ContextException on context errors
     */
    void executePre(long executionID, IN incomingEntity) throws StateMachineException, ContextException;

    /**
     * Carry out the post work for execution, the returning entity should be set by the child execution object.
     *
     * @param returnValue the return value indicates whether the execution was successful and, if it failed, how it failed
     * @throws StateMachineException on an execution error
     * @throws ContextException On context errors
     */
    void executePost(boolean returnValue) throws StateMachineException, ContextException;

    /**
     * Cleans up after processing.
     *
     * @throws StateMachineException thrown when a state machine execution error occurs
     */
    void cleanUp() throws StateMachineException;

    /**
     * Get the key associated with the executor.
     *
     * @return The key associated with the executor
     */
    AxConcept getKey();

    /**
     * Get the parent executor of the executor.
     *
     * @return The parent executor of this executor
     */
    Executor<?, ?, ?, ?> getParent();

    /**
     * Get the subject of the executor.
     *
     * @return The subject for the executor
     */
    SUBJECT getSubject();

    /**
     * Get the context of the executor.
     *
     * @return The context for the executor
     */
    CONTEXT getContext();

    /**
     * Get the incoming object of the executor.
     *
     * @return The incoming object for the executor
     */
    IN getIncoming();

    /**
     * Get the outgoing object of the executor.
     *
     * @return The outgoing object for the executor
     */
    OUT getOutgoing();

    /**
     * Save the next executor for this executor.
     *
     * @param nextExecutor the next executor
     */
    void setNext(Executor<IN, OUT, SUBJECT, CONTEXT> nextExecutor);

    /**
     * Get the next executor to be run after this executor completes its execution.
     *
     * @return The next executor
     */
    Executor<IN, OUT, SUBJECT, CONTEXT> getNext();

    /**
     * Set parameters for this executor, overloaded by executors that use parameters.
     *
     * @param parameters executor parameters
     */
    void setParameters(ExecutorParameters parameters);
}
