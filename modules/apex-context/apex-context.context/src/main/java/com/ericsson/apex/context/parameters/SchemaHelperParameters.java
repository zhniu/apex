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
 * An empty schema helper parameter class that may be specialized by context schema helper plugins that require plugin specific parameters.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SchemaHelperParameters extends AbstractParameters {
    // Schema helper plugin class for the schema
    private String schemaHelperPluginClass;

    /**
     * Constructor to create a schema helper parameters instance and register the instance with the parameter service.
     */
    public SchemaHelperParameters() {
        super(SchemaHelperParameters.class.getCanonicalName());
        ParameterService.registerParameters(SchemaHelperParameters.class, this);
    }

    /**
     * Constructor to create a schema helper parameters instance with the name of a sub class of this class and register the instance with the parameter
     * service.
     *
     * @param parameterClassName the class name of a sub class of this class
     */
    public SchemaHelperParameters(final String parameterClassName) {
        super(parameterClassName);
    }

    /**
     * Gets the schema helper plugin class.
     *
     * @return the schema helper plugin class
     */
    public String getSchemaHelperPluginClass() {
        return schemaHelperPluginClass;
    }

    /**
     * Sets the schema helper plugin class.
     *
     * @param pluginClass the schema helper plugin class
     */
    public void setSchemaHelperPluginClass(final String pluginClass) {
        schemaHelperPluginClass = pluginClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.service.AbstractParameters#toString()
     */
    @Override
    public String toString() {
        return "SchemaHelperParameters [schemaHelperPluginClass=" + schemaHelperPluginClass + "]";
    }
}
