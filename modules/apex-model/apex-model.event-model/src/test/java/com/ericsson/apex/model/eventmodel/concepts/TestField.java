/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestField {

    @Test
    public void testField() {
        assertNotNull(new AxField());
        assertNotNull(new AxField(new AxReferenceKey()));
        assertNotNull(new AxField(new AxReferenceKey(), new AxArtifactKey()));
        assertNotNull(new AxField(new AxReferenceKey(), new AxArtifactKey(), false));
        assertNotNull(new AxField("LocalName", new AxArtifactKey(), false));
        assertNotNull(new AxField("LocalName", new AxArtifactKey()));
        assertNotNull(new AxField("LocalName", new AxArtifactKey(), false));

        assertNotNull(new AxInputField());
        assertNotNull(new AxInputField(new AxReferenceKey()));
        assertNotNull(new AxInputField(new AxReferenceKey(), new AxArtifactKey()));
        assertNotNull(new AxInputField(new AxReferenceKey(), new AxArtifactKey(), true));
        assertNotNull(new AxInputField("LocalName", new AxArtifactKey()));
        assertNotNull(new AxInputField(new AxInputField()));

        assertNotNull(new AxOutputField());
        assertNotNull(new AxOutputField(new AxReferenceKey()));
        assertNotNull(new AxOutputField(new AxReferenceKey(), new AxArtifactKey()));
        assertNotNull(new AxOutputField(new AxReferenceKey(), new AxArtifactKey(), false));
        assertNotNull(new AxOutputField("LocalName", new AxArtifactKey()));
        assertNotNull(new AxOutputField(new AxOutputField()));

        AxField field = new AxField();

        AxReferenceKey fieldKey = new AxReferenceKey("FieldName", "0.0.1", "PLN", "LN");
        field.setKey(fieldKey);
        assertEquals("FieldName:0.0.1:PLN:LN", field.getKey().getID());
        assertEquals("FieldName:0.0.1:PLN:LN", field.getKeys().get(0).getID());
        
        AxArtifactKey schemaKey = new AxArtifactKey("SchemaName", "0.0.1");
        field.setSchema(schemaKey);
        assertEquals("SchemaName:0.0.1", field.getSchema().getID());

        assertEquals(false, field.getOptional());
        field.setOptional(true);
        assertEquals(true, field.getOptional());

        AxValidationResult result = new AxValidationResult();
        result = field.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());

        field.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = field.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        field.setKey(fieldKey);
        result = new AxValidationResult();
        result = field.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        field.setSchema(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = field.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        field.setSchema(schemaKey);
        result = new AxValidationResult();
        result = field.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        field.clean();

        AxField clonedField = (AxField) field.clone();
        assertEquals("AxField:(key=AxReferenceKey:(parentKeyName=FieldName,parentKeyVersion=0.0.1,parentLocalName=PLN,localName=LN),fieldSchemaKey=AxArtifactKey:(name=SchemaName,version=0.0.1),optional=true)", clonedField.toString());

        assertFalse(field.hashCode() == 0);

        assertTrue(field.equals(field));
        assertTrue(field.equals(clonedField));
        assertFalse(field.equals(null));
        assertFalse(field.equals("Hello"));
        assertFalse(field.equals(new AxField(AxReferenceKey.getNullKey(), AxArtifactKey.getNullKey(), false)));
        assertFalse(field.equals(new AxField(fieldKey, AxArtifactKey.getNullKey(), false)));
        assertFalse(field.equals(new AxField(fieldKey, schemaKey, false)));
        assertTrue(field.equals(new AxField(fieldKey, schemaKey, true)));

        assertEquals(0, field.compareTo(field));
        assertEquals(0, field.compareTo(clonedField));
        assertNotEquals(0, field.compareTo(new AxArtifactKey()));
        assertNotEquals(0, field.compareTo(null));
        assertNotEquals(0, field.compareTo(new AxField(AxReferenceKey.getNullKey(), AxArtifactKey.getNullKey(), false)));
        assertNotEquals(0, field.compareTo(new AxField(fieldKey, AxArtifactKey.getNullKey(), false)));
        assertNotEquals(0, field.compareTo(new AxField(fieldKey, schemaKey, false)));
        assertEquals(0, field.compareTo(new AxField(fieldKey, schemaKey, true)));

        assertNotNull(field.getKeys());
    }
}
