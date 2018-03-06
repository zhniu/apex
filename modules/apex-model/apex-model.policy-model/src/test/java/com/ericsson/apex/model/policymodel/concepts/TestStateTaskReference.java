/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestStateTaskReference {

    @Test
    public void testStateTaskReference() {
        assertNotNull(new AxStateTaskReference());
        assertNotNull(new AxStateTaskReference(new AxReferenceKey()));
        assertNotNull(new AxStateTaskReference(new AxReferenceKey(), AxStateTaskOutputType.UNDEFINED, new AxReferenceKey()));
        assertNotNull(new AxStateTaskReference(new AxReferenceKey(), new AxArtifactKey(), AxStateTaskOutputType.UNDEFINED, new AxReferenceKey()));

        AxStateTaskReference stRef = new AxStateTaskReference();

        AxReferenceKey stRefKey = new AxReferenceKey("StateParent", "0.0.1", "SOState", "SOName");
        AxReferenceKey soKey    = new AxReferenceKey("StateParent", "0.0.1", "SOState", "STRef0");
        
        try {
            stRef.setKey(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("key may not be null", e.getMessage());
        }

        stRef.setKey(stRefKey);
        assertEquals("StateParent:0.0.1:SOState:SOName", stRef.getKey().getID());
        assertEquals("StateParent:0.0.1:SOState:SOName", stRef.getKeys().get(0).getID());

        try {
            stRef.setStateTaskOutputType(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("outputType may not be null", e.getMessage());
        }

        stRef.setStateTaskOutputType(AxStateTaskOutputType.UNDEFINED);
        assertEquals(AxStateTaskOutputType.UNDEFINED, stRef.getStateTaskOutputType());
        stRef.setStateTaskOutputType(AxStateTaskOutputType.DIRECT);
        assertEquals(AxStateTaskOutputType.DIRECT, stRef.getStateTaskOutputType());
        stRef.setStateTaskOutputType(AxStateTaskOutputType.LOGIC);
        assertEquals(AxStateTaskOutputType.LOGIC, stRef.getStateTaskOutputType());

        try {
            stRef.setOutput(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("output may not be null", e.getMessage());
        }
        
        stRef.setOutput(soKey);
        assertEquals(soKey, stRef.getOutput());

        AxValidationResult result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());

        stRef.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        stRef.setKey(stRefKey);
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        stRef.setStateTaskOutputType(AxStateTaskOutputType.UNDEFINED);
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        stRef.setStateTaskOutputType(AxStateTaskOutputType.LOGIC);
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        stRef.setOutput(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        stRef.setOutput(soKey);
        result = new AxValidationResult();
        result = stRef.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        stRef.clean();

        AxStateTaskReference clonedStRef = new AxStateTaskReference(stRef);
        assertEquals("AxStateTaskReference:(stateKey=AxReferenceKey:(par", clonedStRef.toString().substring(0, 50));

        assertFalse(stRef.hashCode() == 0);

        assertTrue(stRef.equals(stRef));
        assertTrue(stRef.equals(clonedStRef));
        assertFalse(stRef.equals(null));
        assertFalse(stRef.equals("Hello"));
        assertFalse(stRef.equals(new AxStateTaskReference(AxReferenceKey.getNullKey(), AxStateTaskOutputType.LOGIC, soKey)));
        assertFalse(stRef.equals(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.DIRECT, soKey)));
        assertFalse(stRef.equals(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.LOGIC, new AxReferenceKey())));
        assertTrue(stRef.equals(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.LOGIC, soKey)));

        assertNotNull(new AxStateTaskReference(new AxReferenceKey(), new AxArtifactKey(), AxStateTaskOutputType.UNDEFINED, new AxReferenceKey()));

        assertEquals(0, stRef.compareTo(stRef));
        assertEquals(0, stRef.compareTo(clonedStRef));
        assertNotEquals(0, stRef.compareTo(new AxArtifactKey()));
        assertNotEquals(0, stRef.compareTo(null));
        assertNotEquals(0, stRef.compareTo(new AxStateTaskReference(AxReferenceKey.getNullKey(), AxStateTaskOutputType.LOGIC, soKey)));
        assertNotEquals(0, stRef.compareTo(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.DIRECT, soKey)));
        assertNotEquals(0, stRef.compareTo(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.LOGIC, new AxReferenceKey())));
        assertEquals(0, stRef.compareTo(new AxStateTaskReference(stRefKey, AxStateTaskOutputType.LOGIC, soKey)));

        assertNotNull(stRef.getKeys());
    }
}
