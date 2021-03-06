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

import java.lang.reflect.Constructor;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.context.SchemaHelper;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class implements the {@link SchemaHelper} functionality that is common across all implementations. Schema helpers for specific schema mechanisms
 * specialize this class.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class AbstractSchemaHelper implements SchemaHelper {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(AbstractSchemaHelper.class);

    // The key of the user of this schema helper
    private AxKey userKey = null;

    // The schema of this schema helper
    private AxContextSchema schema = null;

    // The class of objects for this schema
    private Class<?> schemaClass;

    /**
     * Sets the schema class for the schema, designed jots to be called by sub classes.
     *
     * @param schemaClass the Java class that is used to hold items of this schema
     */
    protected void setSchemaClass(final Class<?> schemaClass) {
        this.schemaClass = schemaClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#init(com.ericsson.apex.model.basicmodel.concepts.AxKey,
     * com.ericsson.apex.model.contextmodel.concepts.AxContextSchema)
     */
    @Override
    public void init(final AxKey incomingUserKey, final AxContextSchema incomingSchema) throws ContextRuntimeException {
        Assertions.argumentNotNull(incomingUserKey, ContextRuntimeException.class, "incomingUserKey may not be null");
        Assertions.argumentNotNull(incomingSchema, ContextRuntimeException.class, "incomingSchema may not be null");

        this.userKey = incomingUserKey;
        this.schema = incomingSchema;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#getKey()
     */
    @Override
    public AxKey getUserKey() {
        return userKey;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#getSchema()
     */
    @Override
    public AxContextSchema getSchema() {
        return schema;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#getSchemaClass()
     */
    @Override
    public Class<?> getSchemaClass() {
        return schemaClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#getSchemaObject()
     */
    @Override
    public Object getSchemaObject() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#createNewInstance()
     */
    @Override
    public Object createNewInstance() {
        if (schemaClass == null) {
            final String returnString = userKey.getID() + ": could not create an instance, schema class for the schema is null";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        try {
            return schemaClass.newInstance();
        }
        catch (final Exception e) {
            final String returnString = userKey.getID() + ": could not create an instance of class \"" + schemaClass.getCanonicalName()
                    + "\" using the default constructor \"" + schemaClass.getSimpleName() + "()\"";
            LOGGER.warn(returnString, e);
            throw new ContextRuntimeException(returnString, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.SchemaHelper#createNewInstance(java.lang.String)
     */
    @Override
    public Object createNewInstance(final String stringValue) {
        if (schemaClass == null) {
            final String returnString = userKey.getID() + ": could not create an instance, schema class for the schema is null";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        try {
            // Find a string constructor
            final Constructor<?> stringConstructor = schemaClass.getConstructor(String.class);

            // Invoke the constructor
            return stringConstructor.newInstance(stringValue);
        }
        catch (final Exception e) {
            final String returnString = userKey.getID() + ": could not create an instance of class \"" + schemaClass.getCanonicalName()
                    + "\" using the string constructor \"" + schemaClass.getSimpleName() + "(String)\"";
            LOGGER.warn(returnString, e);
            throw new ContextRuntimeException(returnString);
        }
    }
}
