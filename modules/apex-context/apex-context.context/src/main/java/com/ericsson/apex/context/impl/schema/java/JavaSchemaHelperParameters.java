/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.schema.java;

import com.ericsson.apex.context.parameters.SchemaHelperParameters;

/**
 * The Schema helper parameter class for the Java schema helper is an empty parameter class that acts as a placeholder.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JavaSchemaHelperParameters extends SchemaHelperParameters {

    /**
     * The Constructor.
     */
    public JavaSchemaHelperParameters() {
        this.setSchemaHelperPluginClass(JavaSchemaHelper.class.getCanonicalName());
    }
}
