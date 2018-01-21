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

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.EnumSymbol;

/**
 * Object mapper for enums, uses default behaviour except for a specific default constructor implementation.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroEnumObjectMapper extends AvroDirectObjectMapper {
    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.plugins.context.schema.avro.AvroObjectMapper#createNewinstance(org.apache.avro.Schema)
     */
    @Override
    public Object createNewInstance(final Schema avroSchema) {
        // Initialize the ENUM to the first ENUM symbol on the list
        final List<String> enumSymbols = avroSchema.getEnumSymbols();

        // Check if any ENUM symbols have been defined
        if (enumSymbols == null || enumSymbols.isEmpty()) {
            return null;
        }

        return new EnumSymbol(avroSchema, enumSymbols.get(0));
    }
}
