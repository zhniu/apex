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

import java.util.Map;
import java.util.TreeMap;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * This class holds the parameters for a single Apex engine. This parameter class holds parameters for context schemas and context albums for the engine and a
 * map of the logic flavour executors defined for the engine and the parameters for each of those executors.
 * <p>
 * The context parameters for the engine are held in a {@link ContextParameters} instance. This instance holds the parameters for context schema handling that
 * will be used by the engine as well as the context album distribution, locking, and persistence parameters.
 * <p>
 * In Apex, an engine can be configured to use many logic flavours. The executors for each logic flavour are identified by their name. Each logic flavour
 * executor must have an instance of {@link ExecutorParameters} defined for it, which specifies the executor plugins to use for that logic flavour executor and
 * specific parameters for those executor plugins.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EngineParameters extends AbstractParameters {
    private ContextParameters contextParameters = new ContextParameters();

    // A map of parameters for executors of various logic types
    private Map<String, ExecutorParameters> executorParameterMap = new TreeMap<String, ExecutorParameters>();

    /**
     * Constructor to create an engine parameters instance and register the instance with the parameter service.
     */
    public EngineParameters() {
        super(EngineParameters.class.getCanonicalName());
        ParameterService.registerParameters(EngineParameters.class, this);
    }

    /**
     * Gets the parameters for context schema and album handling.
     *
     * @return the parameters for context schema and album handling
     */
    public ContextParameters getContextParameters() {
        return contextParameters;
    }

    /**
     * Sets the parameters for context schema and album handling.
     *
     * @param contextParameters the parameters for context schema and album handling
     */
    public void setContextParameters(final ContextParameters contextParameters) {
        this.contextParameters = contextParameters;
    }

    /**
     * Gets the executor parameter map of the engine.
     *
     * @return the executor parameter map of the engine
     */
    public Map<String, ExecutorParameters> getExecutorParameterMap() {
        return executorParameterMap;
    }

    /**
     * Sets the executor parameter map of the engine.
     *
     * @param executorParameterMap the executor parameter map of the engine
     */
    public void setExecutorParameterMap(final Map<String, ExecutorParameters> executorParameterMap) {
        this.executorParameterMap = executorParameterMap;
    }
}
