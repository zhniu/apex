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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.context.SchemaHelper;
import com.ericsson.apex.context.impl.schema.SchemaHelperFactory;
import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @version
 */
public class TestInstanceCreation {
    private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
    private AxContextSchemas schemas;

    @Before
    public void initTest() {
        schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
        ModelService.registerModel(AxContextSchemas.class, schemas);
        new SchemaParameters();
    }

    @Test
    public void testNullEncoding() {
        AxContextSchema javaBooleanSchema = new AxContextSchema(new AxArtifactKey("Boolean", "0.0.1"), "Java", "java.lang.Boolean");
        AxContextSchema javaLongSchema    = new AxContextSchema(new AxArtifactKey("Long",    "0.0.1"), "Java", "java.lang.Long");
        AxContextSchema javaStringSchema  = new AxContextSchema(new AxArtifactKey("String",  "0.0.1"), "Java", "java.lang.String");

        schemas.getSchemasMap().put(javaBooleanSchema.getKey(), javaBooleanSchema);
        schemas.getSchemasMap().put(javaLongSchema.getKey(),    javaLongSchema);
        schemas.getSchemasMap().put(javaStringSchema.getKey(),  javaStringSchema);
        
        SchemaHelper schemaHelper0 = new SchemaHelperFactory().createSchemaHelper(testKey, javaBooleanSchema.getKey());
        SchemaHelper schemaHelper1 = new SchemaHelperFactory().createSchemaHelper(testKey, javaLongSchema   .getKey());
        SchemaHelper schemaHelper2 = new SchemaHelperFactory().createSchemaHelper(testKey, javaStringSchema .getKey());
        
        try {
            schemaHelper0.createNewInstance();
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Boolean\" using the default constructor \"Boolean()\"", e.getMessage());
        }
        assertEquals(true, schemaHelper0.createNewInstance("true"));

        
        try {
            schemaHelper1.createNewInstance();
            fail("this test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Long\" using the default constructor \"Long()\"", e.getMessage());
        }
        assertEquals(65536L, schemaHelper1.createNewInstance("65536"));

        assertEquals("",     schemaHelper2.createNewInstance());
        assertEquals("true", schemaHelper2.createNewInstance("true"));
}
}
