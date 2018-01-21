/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextSchemas {

    @Test
    public void testContextSchemas() {
        assertNotNull(new AxContextSchema());
        assertNotNull(new AxContextSchema(new AxArtifactKey(), "SchemaFlavour", "SchemaDefinition"));

        AxContextSchema schema = new AxContextSchema(new AxArtifactKey("SchemaName", "0.0.1"), "SchemaFlavour", "SchemaDefinition");
        assertNotNull(schema);

        AxArtifactKey newKey = new AxArtifactKey("NewSchemaName", "0.0.1");
        schema.setKey(newKey);
        assertEquals("NewSchemaName:0.0.1", schema.getKey().getID());
        assertEquals("NewSchemaName:0.0.1", schema.getKeys().get(0).getID());

        try {
            schema.setSchemaFlavour("");
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("parameter \"schemaFlavour\": value \"\", does not match regular expression \"[A-Za-z0-9\\-_]+\"", e.getMessage());
        }

        schema.setSchemaFlavour("NewSchemaFlavour");
        assertEquals("NewSchemaFlavour", schema.getSchemaFlavour());

        schema.setSchema("NewSchemaDefinition");
        assertEquals("NewSchemaDefinition", schema.getSchema());

        AxValidationResult result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        schema.setKey(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        schema.setKey(newKey);
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        schema.setSchemaFlavour("UNDEFINED");
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        schema.setSchemaFlavour("NewSchemaFlavour");
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        schema.setSchema("");
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        schema.setSchema("NewSchemaDefinition");
        result = new AxValidationResult();
        result = schema.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        schema.clean();

        AxContextSchema clonedSchema = (AxContextSchema) schema.clone();
        assertEquals("AxContextSchema:(key=AxArtifactKey:(name=NewSchemaName,version=0.0.1),schemaFlavour=NewSchemaFlavour,schemaDefinition=NewSchemaDefinition)", clonedSchema.toString());

        assertFalse(schema.hashCode() == 0);

        assertTrue(schema.equals(schema));
        assertTrue(schema.equals(clonedSchema));
        assertFalse(schema.equals(null));
        assertFalse(schema.equals("Hello"));
        assertFalse(schema.equals(new AxContextSchema(new AxArtifactKey(), "Flavour", "Def")));
        assertFalse(schema.equals(new AxContextSchema(newKey, "Flavour", "Def")));
        assertFalse(schema.equals(new AxContextSchema(newKey, "NewSchemaFlavour", "Def")));
        assertTrue(schema.equals(new AxContextSchema(newKey, "NewSchemaFlavour", "NewSchemaDefinition")));

        assertEquals(0, schema.compareTo(schema));
        assertEquals(0, schema.compareTo(clonedSchema));
        assertNotEquals(0, schema.compareTo(null));
        assertNotEquals(0, schema.compareTo(new AxArtifactKey()));
        assertNotEquals(0, schema.compareTo(new AxContextSchema(new AxArtifactKey(), "Flavour", "Def")));
        assertNotEquals(0, schema.compareTo(new AxContextSchema(newKey, "Flavour", "Def")));
        assertNotEquals(0, schema.compareTo(new AxContextSchema(newKey, "NewSchemaFlavour", "Def")));
        assertEquals(0, schema.compareTo(new AxContextSchema(newKey, "NewSchemaFlavour", "NewSchemaDefinition")));

        AxContextSchemas schemas = new AxContextSchemas();
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        // Still invalid, no schemas in schema map
        schemas.setKey(new AxArtifactKey("SchemasKey", "0.0.1"));
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());
        
        schemas.getSchemasMap().put(newKey, schema);
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        schemas.getSchemasMap().put(AxArtifactKey.getNullKey(), null);
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        schemas.getSchemasMap().remove(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        schemas.getSchemasMap().put(new AxArtifactKey("NullValueKey", "0.0.1"), null);
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        schemas.getSchemasMap().remove(new AxArtifactKey("NullValueKey", "0.0.1"));
        result = new AxValidationResult();
        result = schemas.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        schemas.clean();

        AxContextSchemas clonedSchemas = (AxContextSchemas) schemas.clone();
        assertTrue(clonedSchemas.toString().startsWith("AxContextSchemas:(key=AxArtifactKey:(name=SchemasKey,version=0.0.1),"));

        assertFalse(schemas.hashCode() == 0);

        assertTrue(schemas.equals(schemas));
        assertTrue(schemas.equals(clonedSchemas));
        assertFalse(schemas.equals(null));
        assertFalse(schemas.equals("Hello"));
        assertFalse(schemas.equals(new AxContextSchemas(new AxArtifactKey())));

        assertEquals(0, schemas.compareTo(schemas));
        assertEquals(0, schemas.compareTo(clonedSchemas));
        assertNotEquals(0, schemas.compareTo(null));
        assertNotEquals(0, schemas.compareTo(new AxArtifactKey()));
        assertNotEquals(0, schemas.compareTo(new AxContextSchemas(new AxArtifactKey())));
        
        clonedSchemas.get(newKey).setSchemaFlavour("YetAnotherFlavour");
        assertNotEquals(0, schemas.compareTo(clonedSchemas));
        
        assertEquals("NewSchemaName", schemas.get("NewSchemaName").getKey().getName());
        assertEquals("NewSchemaName", schemas.get("NewSchemaName", "0.0.1").getKey().getName());
        assertEquals(1, schemas.getAll("NewSchemaName", "0.0.1").size());
        assertEquals(0, schemas.getAll("NonExistantSchemaName").size());
    }
}
