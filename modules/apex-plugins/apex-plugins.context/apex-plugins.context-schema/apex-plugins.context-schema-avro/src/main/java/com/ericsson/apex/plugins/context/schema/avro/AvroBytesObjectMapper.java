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

import java.nio.ByteBuffer;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;

/**
 * This class does string mapping from the Avro BYTES type to a Java byte array.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroBytesObjectMapper implements AvroObjectMapper {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(AvroBytesObjectMapper.class);

    // The user keyAvro type for direct mapping
    private AxKey userKey;
    private Type avroType;

    // The Apex compatible class
    private final Class<Byte[]> schemaClass = Byte[].class;

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#getJavaClass()
     */
    @Override
    public Class<?> getJavaClass() {
        return schemaClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#setAvroType(org.apache.avro.Schema.Type)
     */
    @Override
    public void init(final AxKey intUserKey, final Type initAvroType) {
        this.userKey = intUserKey;
        this.avroType = initAvroType;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#createNewinstance(org.apache.avro.Schema)
     */
    @Override
    public Object createNewInstance(final Schema avroSchema) {
        // By default, we do not create an instance, normal Java object creation for byte arrays is sufficient
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#getAvroType()
     */
    @Override
    public Type getAvroType() {
        return avroType;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#mapFromAvro(java.lang.Object)
     */
    @Override
    public Object mapFromAvro(final Object avroObject) {
        // The Avro object should be a Utf8 object
        if (!(avroObject instanceof ByteBuffer)) {
            final String returnString = userKey.getID() + ": object \"" + avroObject + "\" of class \"" + avroObject.getClass()
                    + "\" cannot be decoded to an object of class \"" + schemaClass.getCanonicalName() + "\"";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        // Cast the byte buffer object so we get access to its methods
        final ByteBuffer byteBufferAvroObject = (ByteBuffer) avroObject;

        // read the byte buffer into a byte array
        final byte[] byteArray = new byte[byteBufferAvroObject.remaining()];
        byteBufferAvroObject.get(byteArray);

        return byteArray;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#mapToAvro(java.lang.Object)
     */
    @Override
    public Object mapToAvro(final Object object) {
        if (object == null) {
            final String returnString = userKey.getID() + ": cannot encode a null object of class \"" + schemaClass.getCanonicalName() + "\"";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        // The incoming object should be a byte array
        if (!(object instanceof byte[])) {
            final String returnString = userKey.getID() + ": object \"" + object + "\" of class \"" + object.getClass()
                    + "\" cannot be decoded to an object of class \"" + schemaClass.getCanonicalName() + "\"";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        // Create a ByteBuffer object to serialize the bytes
        final ByteBuffer byteBuffer = ByteBuffer.wrap((byte[]) object);

        return byteBuffer;
    }
}
