/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context;

import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.google.gson.JsonElement;

/**
 * This interface is implemented by plugin classes that use a particular schema to convert Apex context objects to an understandable form.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface SchemaHelper {

    /**
     * Initialize the schema helper with its properties.
     *
     * @param userKey The key that identifies the user of the schema helper
     * @param schema the schema
     * @throws ContextRuntimeException the context runtime exception
     */
    void init(AxKey userKey, AxContextSchema schema) throws ContextRuntimeException;

    /**
     * Get the user key of the schema helper.
     *
     * @return the user key
     */
    AxKey getUserKey();

    /**
     * Get the schema of the schema helper.
     *
     * @return the schema
     */
    AxContextSchema getSchema();

    /**
     * The Java class that this schema produces on the Java side.
     *
     * @return the schema class
     */
    Class<?> getSchemaClass();

    /**
     * The Java class that handles the schema for the schema technology in use.
     *
     * @return the schema object
     */
    Object getSchemaObject();

    /**
     * Create a new instance of the schema class using whatever schema technology is being used.
     *
     * @return the new instance
     */
    Object createNewInstance();

    /**
     * Create a new instance of the schema class using whatever schema technology is being used.
     *
     * @param stringValue the string represents the value the new instance should have
     * @return the new instance
     */
    Object createNewInstance(String stringValue);

    /**
     * Create a new instance of the schema class from a GSON JsonElement using whatever schema technology is being used.
     *
     * @param jsonElement the JSON element that holds the Json representation of the object
     * @return the new instance
     */
    Object createNewInstance(JsonElement jsonElement);

    /**
     * Unmarshal an object in schema format into a Java object.
     *
     * @param object the object as a Java object
     * @return the object in schema format
     */
    Object unmarshal(Object object);

    /**
     * Marshal a Java object into Json format.
     *
     * @param schemaObject the object in schema format
     * @return the object as a Json string
     */
    String marshal2Json(Object schemaObject);

    /**
     * Marshal a Java object into a GSON json element.
     *
     * @param schemaObject the object in schema format
     * @return the object as a GSON Json element
     */
    JsonElement marshal2JsonElement(Object schemaObject);
}
