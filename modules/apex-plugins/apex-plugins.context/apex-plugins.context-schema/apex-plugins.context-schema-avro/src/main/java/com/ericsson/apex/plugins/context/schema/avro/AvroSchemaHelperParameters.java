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

import com.ericsson.apex.context.parameters.SchemaHelperParameters;

/**
 * Schema helper parameter class for the Avro schema helper.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AvroSchemaHelperParameters extends SchemaHelperParameters {
    /**
     * The Default Constructor sets the {@link AvroSchemaHelper} as the schema helper class for Avro schemas.
     */
    public AvroSchemaHelperParameters() {
        this.setSchemaHelperPluginClass(AvroSchemaHelper.class.getCanonicalName());
    }
}
