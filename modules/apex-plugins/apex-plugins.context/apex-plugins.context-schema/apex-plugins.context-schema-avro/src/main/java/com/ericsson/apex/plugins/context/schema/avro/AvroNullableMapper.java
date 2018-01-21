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

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;

/**
 * The Class AvroNullableMapper handles Avro null mappings to Java null values.
 *
 * @author John Keeney (john.keeney@ericsson.com)
 */
public class AvroNullableMapper extends AvroDirectObjectMapper {
    // The wrapped mapper for nullables
    private final AvroObjectMapper wrappedMapper;

    /**
     * The Constructor.
     *
     * @param wrappedMapper the wrapped mapper
     */
    public AvroNullableMapper(final AvroObjectMapper wrappedMapper) {
        this.wrappedMapper = wrappedMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#getJavaClass()
     */
    @Override
    public Class<?> getJavaClass() {
        return wrappedMapper.getJavaClass();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#init(com.ericsson.apex.model.basicmodel.concepts.AxKey,
     * org.apache.avro.Schema.Type)
     */
    @Override
    public void init(final AxKey userKey, final Type avroType) {
        wrappedMapper.init(userKey, avroType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#createNewInstance(org.apache.avro.Schema)
     */
    @Override
    public Object createNewInstance(final Schema avroSchema) {
        return wrappedMapper.createNewInstance(avroSchema);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#getAvroType()
     */
    @Override
    public Type getAvroType() {
        return Schema.Type.UNION;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#mapFromAvro(java.lang.Object)
     */
    @Override
    public Object mapFromAvro(final Object avroObject) {
        if (avroObject == null) {
            return null;
        }
        else {
            return wrappedMapper.mapFromAvro(avroObject);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroDirectObjectMapper#mapToAvro(java.lang.Object)
     */
    @Override
    public Object mapToAvro(final Object object) {
        if (object == null) {
            return null;
        }
        else {
            throw new ApexRuntimeException("Unions/Nullable is not supported in output event ... Coming soon!");
        }

    }

}
