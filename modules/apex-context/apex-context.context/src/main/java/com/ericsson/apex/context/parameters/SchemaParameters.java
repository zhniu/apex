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

import java.util.Map;
import java.util.TreeMap;

import com.ericsson.apex.context.impl.schema.java.JavaSchemaHelperParameters;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * Bean class holding schema parameters for schemas and their helpers. As more than one schema can be used in Apex simultaneously, this class is used to hold
 * the schemas that are defined in a given Apex system and to get the schema helper plugin parameters {@link SchemaHelperParameters} for each schema.
 * <p>
 * The default {@code Java} schema is always defined and its parameters are held in a {@link JavaSchemaHelperParameters} instance.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SchemaParameters extends AbstractParameters {
    /** The Java schema flavour is always available for use. */
    public static final String DEFAULT_SCHEMA_FLAVOUR = "Java";

    // A map of parameters for executors of various logic types
    private Map<String, SchemaHelperParameters> schemaHelperParameterMap;

    /**
     * Constructor to create a distributor parameters instance and register the instance with the parameter service.
     */
    public SchemaParameters() {
        super(SchemaParameters.class.getCanonicalName());
        ParameterService.registerParameters(SchemaParameters.class, this);

        schemaHelperParameterMap = new TreeMap<>();

        // The default schema helper
        schemaHelperParameterMap.put(DEFAULT_SCHEMA_FLAVOUR, new JavaSchemaHelperParameters());
    }

    /**
     * Gets a map of the schemas and schema helper parameters that are defined.
     *
     * @return the schema helper parameter map
     */
    public Map<String, SchemaHelperParameters> getSchemaHelperParameterMap() {
        return schemaHelperParameterMap;
    }

    /**
     * Sets the map of the schemas and schema helper parameters.
     *
     * @param schemaHelperParameterMap the schema helper parameter map
     */
    public void setSchemaHelperParameterMap(final Map<String, SchemaHelperParameters> schemaHelperParameterMap) {
        this.schemaHelperParameterMap = schemaHelperParameterMap;
    }

    /**
     * Gets the schema helper parameters for a given context schema flavour.
     *
     * @param schemaFlavour the schema flavour for which to get the schema helper parameters
     * @return the schema helper parameters for the given schema flavour
     */
    public SchemaHelperParameters getSchemaHelperParameters(final String schemaFlavour) {
        return schemaHelperParameterMap.get(schemaFlavour);
    }
}
