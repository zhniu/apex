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

import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.ExecutorParameters;
import com.ericsson.apex.core.engine.context.ApexInternalContext;
import com.ericsson.apex.core.engine.executor.context.StateFinalizerExecutionContext;
import com.ericsson.apex.core.engine.executor.exception.StateMachineException;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateFinalizerLogic;

/**
 * This abstract class executes state finalizer logic in a state of an Apex policy and is specialized by classes that implement execution of state finalizer
 * logic.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class StateFinalizerExecutor implements Executor<Map<String, Object>, String, AxStateFinalizerLogic, ApexInternalContext> {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(StateFinalizerExecutor.class);

    // Hold the state and context definitions
    private Executor<?, ?, ?, ?> parent = null;
    private AxState axState = null;
    private AxStateFinalizerLogic finalizerLogic = null;
    private ApexInternalContext internalContext = null;

    // Holds the incoming and outgoing fields
    private Map<String, Object> incomingFields = null;

    // The next state finalizer executor
    private Executor<Map<String, Object>, String, AxStateFinalizerLogic, ApexInternalContext> nextExecutor = null;

    // The execution context; contains the facades for events and context to be used by tasks executed by this task executor
    private StateFinalizerExecutionContext executionContext = null;

    /**
     * Gets the execution internalContext.
     *
     * @return the execution context
     */
    protected StateFinalizerExecutionContext getExecutionContext() {
        return executionContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setContext(com.ericsson.apex.core.engine.executor.Executor, java.lang.Object, java.lang.Object)
     */
    @Override
    public void setContext(final Executor<?, ?, ?, ?> incomingParent, final AxStateFinalizerLogic incomingFinalizerLogic,
            final ApexInternalContext incomingInternalContext) {
        this.parent = incomingParent;
        axState = (AxState) parent.getSubject();
        this.finalizerLogic = incomingFinalizerLogic;
        this.internalContext = incomingInternalContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#prepare()
     */
    @Override
    public void prepare() throws StateMachineException {
        LOGGER.debug("prepare:" + finalizerLogic.getID() + "," + finalizerLogic.getLogicFlavour() + "," + finalizerLogic.getLogic());
        argumentNotNull(finalizerLogic.getLogic(), StateMachineException.class, "task logic cannot be null.");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#execute(java.lang.long, java.lang.Object)
     */
    @Override
    public String execute(final long executionID, final Map<String, Object> newIncomingFields) throws StateMachineException, ContextException {
        throw new StateMachineException("execute() not implemented on abstract StateFinalizerExecutionContext class, only on its subclasses");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#executePre(java.lang.long, java.lang.Object)
     */
    @Override
    public final void executePre(final long executionID, final Map<String, Object> newIncomingFields) throws StateMachineException, ContextException {
        LOGGER.debug("execute-pre:" + finalizerLogic.getLogicFlavour() + "," + getSubject().getID() + "," + finalizerLogic.getLogic());

        // Record the incoming fields
        this.incomingFields = newIncomingFields;

        // Get state finalizer context object
        executionContext = new StateFinalizerExecutionContext(this, executionID, axState, getIncoming(), axState.getStateOutputs().keySet(), getContext());
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
                    "execute-post: state finalizer logic execution failure on state \"" + axState.getID() + "\" on finalizer logic " + finalizerLogic.getID());
            throw new StateMachineException(
                    "execute-post: state finalizer logic execution failure on state \"" + axState.getID() + "\" on finalizer logic " + finalizerLogic.getID());
        }

        // Check a state output has been selected
        if (getOutgoing() == null) {
            LOGGER.warn("execute-post: state finalizer logic \"" + finalizerLogic.getID() + "\" did not select an output state");
            throw new StateMachineException("execute-post: state finalizer logic \"" + finalizerLogic.getID() + "\" did not select an output state");
        }

        if (!axState.getStateOutputs().keySet().contains(getOutgoing())) {
            LOGGER.warn("execute-post: state finalizer logic \"" + finalizerLogic.getID() + "\" selected output state \"" + getOutgoing()
                    + "\" that does not exsist on state \"" + axState.getID() + "\"");
            throw new StateMachineException("execute-post: state finalizer logic \"" + finalizerLogic.getID() + "\" selected output state \"" + getOutgoing()
                    + "\" that does not exsist on state \"" + axState.getID() + "\"");
        }

        LOGGER.debug("execute-post:" + finalizerLogic.getID() + ", returning  state output \"" + getOutgoing() + " and fields " + incomingFields);
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
        return finalizerLogic.getKey();
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
    public AxStateFinalizerLogic getSubject() {
        return finalizerLogic;
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
    public String getOutgoing() {
        return executionContext.getSelectedStateOutputName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#setNext(com.ericsson.apex.core.engine.executor.Executor)
     */
    @Override
    public void setNext(final Executor<Map<String, Object>, String, AxStateFinalizerLogic, ApexInternalContext> incomingNextExecutor) {
        this.nextExecutor = incomingNextExecutor;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.engine.executor.Executor#getNext()
     */
    @Override
    public Executor<Map<String, Object>, String, AxStateFinalizerLogic, ApexInternalContext> getNext() {
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
