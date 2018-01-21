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
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxKey.Compatibility;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AxKeyTest {

    @Test
    public void testArtifactKey() {
        AxArtifactKey aKey0 = new AxArtifactKey();
        AxArtifactKey aKey1 = new AxArtifactKey("name", "0.0.1");
        AxArtifactKey aKey2 = new AxArtifactKey(aKey1);
        AxArtifactKey aKey3 = new AxArtifactKey(aKey1.getID());
        AxArtifactKey aKey4 = new AxArtifactKey(aKey1);
        AxArtifactKey aKey5 = new AxArtifactKey(aKey1);
        AxArtifactKey aKey6 = new AxArtifactKey(aKey1);

        try {
            new AxArtifactKey("some bad key id");
            fail("This test should throw an exception");
        }
        catch (IllegalArgumentException e) {
            assertEquals("parameter \"id\": value \"some bad key id\", does not match regular expression \"[A-Za-z0-9\\-_\\.]+:[0-9].[0-9].[0-9]\"", e.getMessage());
        }
        
        assertEquals(AxArtifactKey.getNullKey(), aKey0);
        assertEquals(aKey1, aKey2);
        assertEquals(aKey1, aKey3);
        
        assertEquals(aKey2, aKey1.getKey());
        assertEquals(1, aKey1.getKeys().size());
        
        aKey0.setName("zero");
        aKey0.setVersion("0.0.2");
        aKey3.setVersion("0.0.2");
        aKey4.setVersion("0.1.2");
        aKey5.setVersion("1.2.2");
        aKey6.setVersion("3");
        
        assertEquals(Compatibility.DIFFERENT, aKey0.getCompatibility(new AxReferenceKey()));
        assertEquals(Compatibility.DIFFERENT, aKey0.getCompatibility(aKey1));
        assertEquals(Compatibility.IDENTICAL, aKey2.getCompatibility(aKey1));
        assertEquals(Compatibility.PATCH,     aKey3.getCompatibility(aKey1));
        assertEquals(Compatibility.MINOR,     aKey4.getCompatibility(aKey1));
        assertEquals(Compatibility.MAJOR,     aKey5.getCompatibility(aKey1));
        assertEquals(Compatibility.MAJOR,     aKey6.getCompatibility(aKey1));
        
        assertTrue(aKey1.isCompatible(aKey2));
        assertTrue(aKey1.isCompatible(aKey3));
        assertTrue(aKey1.isCompatible(aKey4));
        assertFalse(aKey1.isCompatible(aKey0));
        assertFalse(aKey1.isCompatible(aKey5));
        assertFalse(aKey1.isCompatible(new AxReferenceKey()));
        
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey0.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey1.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey2.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey3.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey4.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey5.validate(new AxValidationResult()).getValidationResult());
        assertEquals(AxValidationResult.ValidationResult.VALID, aKey6.validate(new AxValidationResult()).getValidationResult());
        
        aKey0.clean();
        assertNotNull(aKey0.toString());
        
        AxArtifactKey aKey7 = (AxArtifactKey)aKey1.clone();
        assertEquals(150332875, aKey7.hashCode());
        assertEquals(0, aKey7.compareTo(aKey1));
        assertEquals(-12, aKey7.compareTo(aKey0));
        
        try {
            aKey0.compareTo(null);
        }
        catch (IllegalArgumentException e) {
            assertEquals("comparison object may not be null", e.getMessage());
        }
        
        assertEquals(0, aKey0.compareTo(aKey0));
        assertEquals(353602977, aKey0.compareTo(new AxReferenceKey()));
        
        assertFalse(aKey0.equals(null));
        assertTrue(aKey0.equals(aKey0));
        assertFalse(((AxKey)aKey0).equals(new AxReferenceKey()));
    }

}
