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
import org.apache.avro.generic.GenericData.Array;

/**
 * Object mapper for arrays, uses default behaviour except for a specific default constructor implementation.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroArrayObjectMapper extends AvroDirectObjectMapper {
    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#createNewinstance(org.apache.avro.Schema)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object createNewInstance(final Schema avroSchema) {
        return new Array(0, avroSchema);
    }
}
