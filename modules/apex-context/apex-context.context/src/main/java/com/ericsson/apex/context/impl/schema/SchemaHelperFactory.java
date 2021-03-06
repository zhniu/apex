/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.schema;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.context.SchemaHelper;
import com.ericsson.apex.context.parameters.SchemaHelperParameters;
import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class returns a {@link SchemaHelper} for the particular type of schema mechanism configured for use.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SchemaHelperFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SchemaHelperFactory.class);

    /**
     * Return a {@link SchemaHelper} for the particular type of schema mechanism configured for use.
     *
     * @param owningEntityKey The key of the entity that owns the schema helper
     * @param schemaKey The key of the schema the schema helper is operating on
     * @return a lock schema that can handle translation of objects in a particular schema format
     * @throws ContextRuntimeException the context runtime exception
     */
    public SchemaHelper createSchemaHelper(final AxKey owningEntityKey, final AxArtifactKey schemaKey) throws ContextRuntimeException {
        LOGGER.entry("schema helper factory, owningEntityKey=" + owningEntityKey);
        Assertions.argumentNotNull(owningEntityKey, ContextRuntimeException.class, "Parameter \"owningEntityKey\" may not be null");
        Assertions.argumentNotNull(schemaKey, ContextRuntimeException.class, "Parameter \"schemaKey\" may not be null");

        // Get the schema for items in the album
        final AxContextSchema schema = ModelService.getModel(AxContextSchemas.class).get(schemaKey);
        if (schema == null) {
            final String resultString = "schema \"" + schemaKey.getID() + "\" for entity " + owningEntityKey.getID() + " does not exist";
            LOGGER.warn(resultString);
            throw new ContextRuntimeException(resultString);
        }

        // Get the schema class using the parameter service
        final SchemaParameters schemaParameters = ParameterService.getParameters(SchemaParameters.class);
        if (schemaParameters == null) {
            final String resultString = "context schema parameters \"" + SchemaParameters.class.getCanonicalName() + "\" not found in parameter service";
            LOGGER.warn(resultString);
            throw new ContextRuntimeException(resultString);
        }

        // Get the class for the schema helper from the schema parameters
        final SchemaHelperParameters schemaHelperParameters = schemaParameters.getSchemaHelperParameters(schema.getSchemaFlavour());
        if (schemaHelperParameters == null) {
            final String resultString = "context schema helper parameters not found for context schema  \"" + schema.getSchemaFlavour() + "\"";
            LOGGER.warn(resultString);
            throw new ContextRuntimeException(resultString);
        }

        // Get the class for the schema helper using reflection
        Object schemaHelperObject = null;
        final String pluginClass = schemaHelperParameters.getSchemaHelperPluginClass();
        try {
            schemaHelperObject = Class.forName(pluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            final String resultString = "Apex context schema helper class not found for context schema helper plugin \"" + pluginClass + "\"";
            LOGGER.warn(resultString, e);
            throw new ContextRuntimeException(resultString, e);
        }

        // Check the class is a schema helper
        if (!(schemaHelperObject instanceof SchemaHelper)) {
            final String resultString = "Specified Apex context schema helper plugin class \"" + pluginClass
                    + "\" does not implement the SchemaHelper interface";
            LOGGER.warn(resultString);
            throw new ContextRuntimeException(resultString);
        }

        // The context schema helper to return
        final SchemaHelper schemaHelper = (SchemaHelper) schemaHelperObject;

        // Lock and load the schema helper
        schemaHelper.init(owningEntityKey.getKey(), schema);

        LOGGER.exit("Schema Helper factory, owningEntityKey=" + owningEntityKey + ", selected schema helper of class " + schemaHelper.getClass());
        return schemaHelper;
    }
}
