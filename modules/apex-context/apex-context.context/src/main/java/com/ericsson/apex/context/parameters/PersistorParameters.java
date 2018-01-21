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
 * A persistor parameter class that may be specialized by context persistor plugins that require plugin specific parameters.
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>pluginClass: the persistor plugin as the JVM local dummy ephemeral persistor
 * <li>flushPeriod: Context is flushed to any persistor plugin that is defined periodically, and the period for flushing is the flush period.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PersistorParameters extends AbstractParameters {
    /** The default persistor is a dummy persistor that stubs the Persistor interface. */
   public static final String DEFAULT_PERSISTOR_PLUGIN_CLASS = "com.ericsson.apex.context.impl.persistence.ephemeral.EphemeralPersistor";

    /** Default periodic flushing interval, 5 minutes in milliseconds. */
    public static final long DEFAULT_FLUSH_PERIOD = 300000;

    // Plugin class names
    private String pluginClass = DEFAULT_PERSISTOR_PLUGIN_CLASS;

    // Parameters for flushing
    private long flushPeriod = DEFAULT_FLUSH_PERIOD;

   /**
     * Constructor to create a persistor parameters instance and register the instance with the parameter service.
     */
    public PersistorParameters() {
        super(PersistorParameters.class.getCanonicalName());
        ParameterService.registerParameters(PersistorParameters.class, this);
    }

    /**
     * Constructor to create a persistor parameters instance with the name of a sub class of this class and register the instance with the parameter service.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public PersistorParameters(final String parameterClassName) {
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

    /**
     * Gets the flush period in milliseconds.
     *
     * @return the flush period
     */
    public long getFlushPeriod() {
        return flushPeriod;
    }

    /**
     * Sets the flush period in milliseconds.
     *
     * @param flushPeriod the flush period
     */
    public void setFlushPeriod(final long flushPeriod) {
        if (flushPeriod <= 0) {
            this.flushPeriod = DEFAULT_FLUSH_PERIOD;
        }
        else {
            this.flushPeriod = flushPeriod;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.service.AbstractParameters#toString()
     */
    @Override
    public String toString() {
        return "PersistorParameters [pluginClass=" + pluginClass + ", flushPeriod=" + flushPeriod + "]";
    }
}
