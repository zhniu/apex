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

import com.ericsson.apex.model.basicmodel.concepts.AxKey;

/**
 * This interface is used to allow mapping of Avro object to and from Java objects.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface AvroObjectMapper {
    /**
     * Get the Java class produced and consumed by this mapper.
     *
     * @return the Java class
     */
    Class<?> getJavaClass();

    /**
     * Initialize the mapper is working with.
     *
     * @param userKey the user key
     * @param avroType the avro type
     */
    void init(AxKey userKey, Type avroType);

    /**
     * Create a new instance of the java object the Avro schema maps to.
     *
     * @param avroSchema the Avro schema to use to create the new instance
     * @return a new instance of the object
     */
    Object createNewInstance(Schema avroSchema);

    /**
     * Set the Avro type the mapper is working with.
     *
     * @return the avro type
     */
    Type getAvroType();

    /**
     * Map the Avro object to an object Apex can handler.
     *
     * @param avroObject the Avro object to map
     * @return the Apex-compatible object
     */
    Object mapFromAvro(Object avroObject);

    /**
     * Map the Apex object to an Avro object.
     *
     * @param object the Apex-compatible object
     * @return the Avro object
     */
    Object mapToAvro(Object object);
}
