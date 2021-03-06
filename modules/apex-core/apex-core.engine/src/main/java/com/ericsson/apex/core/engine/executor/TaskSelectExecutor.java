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

import static com.ericsson.apex.model.utilities.Assertions.argumentNotNull;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.engine.executor.context.TaskSelectionExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.policymodel.concepts.AxState;

/**
 * This abstract class executes a the task selection logic of a state of an Apex policy and is specialized by classes that implement execution of task selection
 * logic.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class TaskSelectExecutor implements Executor<EnEvent, AxArtifactKey, AxState, ApexInternalContext> {
	// Logger for this class
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(TaskSelectExecutor.class);

	// Hold the state and context definitions for this task selector
	private Executor<?, ?, ?, ?> parent = null;
	private AxState axState = null;
	private ApexInternalContext context = null;

	// Holds the incoming event and outgoing task keys
	private EnEvent incomingEvent = null;
	private AxArtifactKey outgoingTaskKey = null;

	// The next task selection executor
	private Executor<EnEvent, AxArtifactKey, AxState, ApexInternalContext> nextExecutor = null;

	// The task selection execution context; contains the facades for events and context to be used by tasks executed by this task selection executor
	private TaskSelectionExecutionContext executionContext;

	/**
	 * Gets the execution context.
	 *
	 * @return the execution context
	 */
	protected TaskSelectionExecutionContext getExecutionContext() {
		return executionContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#setContext(com.ericsson.apex.core.engine.executor.Executor, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setContext(final Executor<?, ?, ?, ?> newParent, final AxState newAxState, final ApexInternalContext newContext) {
		this.parent = newParent;
		this.axState = newAxState;
		this.context = newContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#prepare()
	 */
	@Override
	public void prepare() throws StateMachineException {
		LOGGER.debug("prepare:" + axState.getKey().getID() + "," + axState.getTaskSelectionLogic().getLogicFlavour() + ","
				+ axState.getTaskSelectionLogic().getLogic());
		argumentNotNull(axState.getTaskSelectionLogic().getLogic(), "task selection logic cannot be null.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#execute(java.lang.long, java.lang.Object)
	 */
	@Override
	public AxArtifactKey execute(final long executionID, final EnEvent newIncomingEvent) throws StateMachineException, ContextException {
		throw new StateMachineException("execute() not implemented on class");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#executePre(java.lang.long, java.lang.Object)
	 */
	@Override
	public final void executePre(final long executionID, final EnEvent newIncomingEvent) throws StateMachineException {
		LOGGER.debug("execute-pre:" + axState.getKey().getID() + "," + axState.getTaskSelectionLogic().getLogicFlavour() + ","
				+ axState.getTaskSelectionLogic().getLogic());

		this.incomingEvent = newIncomingEvent;

		// Initialize the returned task object so it can be set
		outgoingTaskKey = new AxArtifactKey();

		// Get task selection context object
		executionContext = new TaskSelectionExecutionContext(this, executionID, getSubject(), getIncoming(), getOutgoing(), getContext());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#executePost(boolean)
	 */
	@Override
	public final void executePost(final boolean returnValue) throws StateMachineException {
		if (!returnValue) {
			String errorMessage = "execute-post: task selection logic failed on state \"" + axState.getKey().getID() + "\"";
			if (executionContext.getMessage() != null) {
				errorMessage += ", user message: " + executionContext.getMessage();
			}
			LOGGER.warn(errorMessage);
			throw new StateMachineException(errorMessage);
		}

		if (outgoingTaskKey == null || AxArtifactKey.getNullKey().getName().equals(outgoingTaskKey.getName())) {
			outgoingTaskKey = axState.getDefaultTask();
			LOGGER.debug("execute-post:" + axState.getKey().getID() + ", returning default task");
			return;
		}

		if (!axState.getTaskReferences().containsKey(outgoingTaskKey)) {
			LOGGER.error("execute-post: task \"" + outgoingTaskKey.getID() + "\" returned by task selection logic not defined on state \""
					+ axState.getKey().getID() + "\"");
			throw new StateMachineException(
					"task \"" + outgoingTaskKey.getID() + "\" returned by task selection logic not defined on state \"" + axState.getKey().getID() + "\"");
		}

		LOGGER.debug("execute-post:" + axState.getKey().getID() + "," + ", returning task " + outgoingTaskKey.getID());
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
	public ApexInternalContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#setNext(com.ericsson.apex.core.engine.executor.Executor)
	 */
	@Override
	public void setNext(final Executor<EnEvent, AxArtifactKey, AxState, ApexInternalContext> newNextExecutor) {
		this.nextExecutor = newNextExecutor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#getNext()
	 */
	@Override
	public Executor<EnEvent, AxArtifactKey, AxState, ApexInternalContext> getNext() {
		return nextExecutor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#getIncoming()
	 */
	@Override
	public EnEvent getIncoming() {
		return incomingEvent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.core.engine.executor.Executor#getOutgoing()
	 */
	@Override
	public AxArtifactKey getOutgoing() {
		return outgoingTaskKey;
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
