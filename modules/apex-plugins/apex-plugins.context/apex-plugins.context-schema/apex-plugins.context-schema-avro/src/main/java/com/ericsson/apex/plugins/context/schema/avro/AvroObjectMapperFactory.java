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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.avro.Schema;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;

/**
 * This class maps between Avro types to Java types. This class is thread safe.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroObjectMapperFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(AvroObjectMapperFactory.class);

    // Map for Avro primitive types to Java primitive types
    private static final Map<Schema.Type, Class<? extends AvroObjectMapper>> AVRO_OBJECT_MAPPER_MAP = new TreeMap<>();

    // @formatter:off
    // Initialize the mapping
    static {
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.ARRAY,   AvroArrayObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.BOOLEAN, AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.BYTES,   AvroBytesObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.DOUBLE,  AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.ENUM,    AvroEnumObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.FIXED,   AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.FLOAT,   AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.INT,     AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.LONG,    AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.MAP,     AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.NULL,    AvroDirectObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.RECORD,  AvroRecordObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.STRING,  AvroStringObjectMapper.class);
        AVRO_OBJECT_MAPPER_MAP.put(Schema.Type.UNION,   null);
    }
    // @formatter:on

    /**
     * Gets the Avro object mapper to use for an artifact with the given key and schema.
     *
     * @param userKey the key of the artifact
     * @param incomingSchema the incoming schema
     * @return the avro object mapper
     */
    public AvroObjectMapper get(final AxKey userKey, final Schema incomingSchema) {
        Schema schema = incomingSchema;
        boolean isnullable = false;
        if (Schema.Type.UNION.equals(schema.getType())) {

            final List<Schema> types = schema.getTypes();
            // TODO: properly support UNIONS
            // currently only support unions with 2 types, one of which is NULL
            final Schema nullschema = Schema.create(Schema.Type.NULL);
            if (types.size() != 2 || !types.contains(nullschema)) {
                final String resultSting = userKey.getID() + ": Apex currently only supports UNION schemas with 2 options, one must be NULL";
                LOGGER.warn(resultSting);
                throw new ContextRuntimeException(resultSting);
            }
            isnullable = true;
            // get the non-null schema given for the union so it can be wrapped
            schema = types.get(0);
            if (Schema.Type.NULL.equals(schema.getType())) {
                schema = types.get(1);
            }
            if (Schema.Type.NULL.equals(schema.getType()) || Schema.Type.NULL.equals(schema.getType())) {
                final String resultSting = userKey.getID()
                        + ": Apex currently only supports UNION schema2 with 2 options, only one can be NULL, and the other cannot be another UNION";
                LOGGER.warn(resultSting);
                throw new ContextRuntimeException(resultSting);
            }
        }

        final Schema.Type avroType = schema.getType();

        // Check that there is a definition for the mapper for this type
        if (!AVRO_OBJECT_MAPPER_MAP.containsKey(avroType) || AVRO_OBJECT_MAPPER_MAP.get(avroType) == null) {
            final String resultSting = userKey.getID() + ": no Avro object mapper defined for Avro type \"" + avroType + "\"";
            LOGGER.warn(resultSting);
            throw new ContextRuntimeException(resultSting);
        }

        // Create a mapper
        AvroObjectMapper avroObjectMapper;
        try {
            avroObjectMapper = AVRO_OBJECT_MAPPER_MAP.get(avroType).newInstance();
            if (isnullable) {
                avroObjectMapper = new AvroNullableMapper(avroObjectMapper);
            }

        }
        catch (final Exception e) {
            final String resultSting = userKey.getID() + ": could not create an Avro object mapper of type \"" + AVRO_OBJECT_MAPPER_MAP.get(avroType)
                    + "\" for Avro type \"" + avroType + "\" : " + e;
            LOGGER.warn(resultSting, e);
            throw new ContextRuntimeException(resultSting, e);
        }

        // Set the type and return
        avroObjectMapper.init(userKey, avroType);

        return avroObjectMapper;
    }
}
