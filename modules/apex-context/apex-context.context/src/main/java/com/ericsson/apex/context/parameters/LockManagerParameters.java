/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.parameters;

import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * An empty lock manager parameter class that may be specialized by context lock manager plugins that require plugin specific parameters. The class defines the
 * default lock manager plugin as the JVM local lock manager.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class LockManagerParameters extends AbstractParameters {
    /** The default lock manager can lock context album instance across all threads in a single JVM. */
    public static final String DEFAULT_LOCK_MANAGER_PLUGIN_CLASS = "com.ericsson.apex.context.impl.locking.jvmlocal.JVMLocalLockManager";

    // Plugin class names
    private String pluginClass = DEFAULT_LOCK_MANAGER_PLUGIN_CLASS;

    /**
     * Constructor to create a lock manager parameters instance and register the instance with the parameter service.
     */
    public LockManagerParameters() {
        super(LockManagerParameters.class.getCanonicalName());
        ParameterService.registerParameters(LockManagerParameters.class, this);
    }

    /**
     * Constructor to create a lock manager parameters instance with the name of a sub class of this class and register the instance with the parameter service.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public LockManagerParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the plugin class.
     *
     * @return the plugin class
     */
    public String getPluginClass() {
        return pluginClass;
    }

    /**
     * Sets the plugin class.
     *
     * @param pluginClass the plugin class
     */
    public void setPluginClass(final String pluginClass) {
        this.pluginClass = pluginClass;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.basicmodel.service.AbstractParameters#toString()
     */
    @Override
    public String toString() {
        return "LockManagerParameters [pluginClass=" + pluginClass + "]";
    }
}
