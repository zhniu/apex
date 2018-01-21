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
import org.apache.avro.util.Utf8;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;

/**
 * This class does string mapping from the Avro Utf8 class to the Java String class.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroStringObjectMapper implements AvroObjectMapper {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(AvroStringObjectMapper.class);

    // The user keyAvro type for direct mapping
    private AxKey userKey;
    private Type avroType;

    // The Apex compatible class
    private final Class<String> schemaClass = String.class;

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
    public void init(final AxKey initUserKey, final Type initAvroType) {
        this.userKey = initUserKey;
        this.avroType = initAvroType;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#createNewinstance(org.apache.avro.Schema)
     */
    @Override
    public Object createNewInstance(final Schema avroSchema) {
        // By default, we do not create an instance, normal Java object creation for strings is sufficient
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
        if (!(avroObject instanceof Utf8)) {
            final String returnString = userKey.getID() + ": object \"" + avroObject + "\" of class \"" + avroObject.getClass()
                    + "\" cannot be decoded to an object of class \"" + schemaClass.getCanonicalName() + "\"";
            LOGGER.warn(returnString);
            throw new ContextRuntimeException(returnString);
        }

        return avroObject.toString();
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

        return object;
    }
}
