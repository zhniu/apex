/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestValidation {

    @Test
    public void test() {
        AxValidationResult result = new AxValidationResult();
        AxReferenceKey refKey = new AxReferenceKey("PK", "0.0.1", "PLN", "LN");
        result = refKey.validate(result);
        
        assertNotNull(result);
        assertTrue(result.isOK());
        assertTrue(result.isValid());
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());
        assertNotNull(result.getMessageList());
        
        AxValidationMessage vMess0 = new AxValidationMessage(AxArtifactKey.getNullKey(), AxArtifactKey.class, ValidationResult.VALID, "Some message");
        result.addValidationMessage(vMess0);
        
        assertTrue(result.isOK());
        assertTrue(result.isValid());
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());
        assertNotNull(result.getMessageList());
        assertNotNull("hello", result.toString());
        
        AxValidationMessage vMess1 = new AxValidationMessage(AxArtifactKey.getNullKey(), AxArtifactKey.class, ValidationResult.OBSERVATION, "Some message");
        result.addValidationMessage(vMess1);
        
        assertTrue(result.isOK());
        assertTrue(result.isValid());
        assertEquals(AxValidationResult.ValidationResult.OBSERVATION, result.getValidationResult());
        assertNotNull(result.getMessageList());
        assertNotNull("hello", result.toString());
        
        AxValidationMessage vMess2 = new AxValidationMessage(AxArtifactKey.getNullKey(), AxArtifactKey.class, ValidationResult.WARNING, "Some message");
        result.addValidationMessage(vMess2);
        
        assertFalse(result.isOK());
        assertTrue(result.isValid());
        assertEquals(AxValidationResult.ValidationResult.WARNING, result.getValidationResult());
        assertNotNull(result.getMessageList());
        assertNotNull("hello", result.toString());
        
        AxValidationMessage vMess3 = new AxValidationMessage(AxArtifactKey.getNullKey(), AxArtifactKey.class, ValidationResult.INVALID, "Some message");
        result.addValidationMessage(vMess3);
        
        assertFalse(result.isOK());
        assertFalse(result.isValid());
        assertEquals(AxValidationResult.ValidationResult.INVALID, result.getValidationResult());
        assertNotNull(result.getMessageList());
        assertNotNull("hello", result.toString());
        
        assertEquals(AxValidationResult.ValidationResult.INVALID, result.getMessageList().get(3).getValidationResult());
        assertEquals("Some message", result.getMessageList().get(3).getMessage());
        assertEquals(AxArtifactKey.class.getCanonicalName(), result.getMessageList().get(3).getObservedClass());
        assertEquals(AxArtifactKey.getNullKey(), result.getMessageList().get(3).getObservedKey());
    }
}
