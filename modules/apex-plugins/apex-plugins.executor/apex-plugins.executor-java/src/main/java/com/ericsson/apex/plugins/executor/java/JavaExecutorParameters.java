/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.java;

import com.ericsson.apex.core.engine.ExecutorParameters;

/**
 * This class provides executor parameters for the Java Executor plugin. It specifies the classes that provide the Java implementations of the abstract classes
 * {@link com.ericsson.apex.core.engine.executor.TaskExecutor}, {@link com.ericsson.apex.core.engine.executor.TaskSelectExecutor}, and
 * {@link com.ericsson.apex.core.engine.executor.StateFinalizerExecutor}.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavaExecutorParameters extends ExecutorParameters {
    /**
     * Constructor that sets the abstract implementation classes.
     */
    public JavaExecutorParameters() {
        this.setTaskExecutorPluginClass(JavaTaskExecutor.class.getCanonicalName());
        this.setTaskSelectionExecutorPluginClass(JavaTaskSelectExecutor.class.getCanonicalName());
        this.setStateFinalizerExecutorPluginClass(JavaStateFinalizerExecutor.class.getCanonicalName());
    }
}
