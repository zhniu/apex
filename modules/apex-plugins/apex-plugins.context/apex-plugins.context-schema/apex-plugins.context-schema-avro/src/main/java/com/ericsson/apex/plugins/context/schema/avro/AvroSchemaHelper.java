/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.schema.avro;

import java.io.ByteArrayOutputStream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.io.JsonEncoder;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.context.impl.schema.AbstractSchemaHelper;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * This class is the implementation of the {@link com.ericsson.apex.context.SchemaHelper} interface for Avro schemas.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroSchemaHelper extends AbstractSchemaHelper {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(AvroSchemaHelper.class);

    // The Avro schema for this context schema
    private Schema avroSchema;

    // The mapper that translates between Java and Avro objects
    private AvroObjectMapper avroObjectMapper;

    @Override
    public void init(final AxKey userKey, final AxContextSchema schema) throws ContextRuntimeException {
        super.init(userKey, schema);

        // Configure the Avro schema
        try {
            avroSchema = new Schema.Parser().parse(schema.getSchema());
        }
        catch (final Exception e) {
            final String resultSting = userKey.getID() + ": avro context schema \"" + schema.getID() + "\" schema is invalid: " + e.getMessage() + ", schema: "
                    + schema.getSchema();
            LOGGER.warn(resultSting);
            throw new ContextRuntimeException(resultSting);
        }

        // Get the object mapper for the schema type to a Java class
        avroObjectMapper = new AvroObjectMapperFactory().get(userKey, avroSchema);

        // Get the Java type for this schema, if it is a primitive type then we can do direct conversion to JAva
        setSchemaClass(avroObjectMapper.getJavaClass());
    }

    /**
     * Getter to get the Avro schema.
     *
     * @return the Avro schema
     */
    public Schema getAvroSchema() {
        return avroSchema;
    }

    @Override
    public Object getSchemaObject() {
        return avroSchema;
    }

    @Override
    public Object createNewInstance() {
        // Create a new instance using the Avro object mapper
        final Object newInstance = avroObjectMapper.createNewInstance(avroSchema);

        // If no new instance is created, use default schema handler behavior
        if (newInstance != null) {
            return newInstance;
        }
        else {
            return super.createNewInstance();
        }
    }

    @Override
    public Object createNewInstance(final String stringValue) {
        return unmarshal(stringValue);
    }

    @Override
    public Object createNewInstance(final JsonElement jsonElement) {
        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String elementJsonString = gson.toJson(jsonElement);

        return createNewInstance(elementJsonString);
    }

    @Override
    public Object unmarshal(final Object object) {
        // If an object is already in the correct format, just carry on
        if (passThroughObject(object)) {
            return object;
        }

        // Check that the incoming object is a string, the incoming object must be a string containing Json
        String objectString;
        try {
            if (object == null) {
                objectString = null;
            }
            if (object != null && avroSchema.getType().equals(Schema.Type.STRING)) {
                objectString = object.toString().trim();
                if (objectString.length() == 0) {
                    objectString = "\"\"";
                }
                else if (objectString.length() == 1) {
                    objectString = "\"" + objectString + "\"";
                }
                else {
                    // All strings must be quoted for decoding
                    if (objectString.charAt(0) != '"') {
                        objectString = '"' + objectString;
                    }
                    if (objectString.charAt(objectString.length() - 1) != '"') {
                        objectString += '"';
                    }
                }
            }
            else {
                objectString = (String) object;
            }
        }
        catch (final ClassCastException e) {
            final String returnString = getUserKey().getID() + ": object \"" + object.toString() + "\" of type \"" + object.getClass().getCanonicalName()
                    + "\" must be assignable to \"" + getSchemaClass().getCanonicalName()
                    + "\" or be a Json string representation of it for Avro unmarshalling";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        // Translate illegal characters in incoming JSON keys to legal Avro values
        objectString = AvroSchemaKeyTranslationUtilities.translateIllegalKeys(objectString, false);

        // Decode the object
        Object decodedObject;
        try {
            final JsonDecoder jsonDecoder = DecoderFactory.get().jsonDecoder(avroSchema, objectString);
            decodedObject = new GenericDatumReader<GenericRecord>(avroSchema).read(null, jsonDecoder);
        }
        catch (final Exception e) {
            final String returnString = getUserKey().getID() + ": object \"" + objectString + "\" Avro unmarshalling failed: " + e.getMessage();
            LOGGER.warn(returnString, e);
            throw new ContextRuntimeException(returnString, e);
        }

        // Now map the decoded object into something we can handle
        return avroObjectMapper.mapFromAvro(decodedObject);
    }

    @Override
    public String marshal2Json(final Object object) {
        // Condition the object for Avro encoding
        final Object conditionedObject = avroObjectMapper.mapToAvro(object);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            final DatumWriter<Object> writer = new GenericDatumWriter<>(avroSchema);
            final JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(avroSchema, output, true);
            writer.write(conditionedObject, jsonEncoder);
            jsonEncoder.flush();
            output.close();
        }
        catch (final Exception e) {
            final String returnString = getUserKey().getID() + ": object \"" + object + "\" Avro marshalling failed: " + e.getMessage();
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString, e);
        }

        return AvroSchemaKeyTranslationUtilities.translateIllegalKeys(new String(output.toByteArray()), true);
    }

    @Override
    public JsonElement marshal2JsonElement(final Object schemaObject) {
        // Get the object as a Json string
        final String schemaObjectAsString = marshal2Json(schemaObject);

        // Get a Gson instance to convert the Json string to an object created by Json
        final Gson gson = new Gson();

        // Convert the Json string into an object
        final Object schemaObjectAsObject = gson.fromJson(schemaObjectAsString, Object.class);

        return gson.toJsonTree(schemaObjectAsObject);
    }

    /**
     * Check if we can pass this object straight through encoding or decoding, is it an object native to the schema.
     *
     * @param object the object to check
     * @return true if it's a straight pass through
     */
    private boolean passThroughObject(final Object object) {
        if (object == null || getSchemaClass() == null) {
            return false;
        }

        // All strings must be mapped
        if (object instanceof String) {
            return false;
        }

        // Now, check if the object is native
        return getSchemaClass().isAssignableFrom(object.getClass());
    }
}
