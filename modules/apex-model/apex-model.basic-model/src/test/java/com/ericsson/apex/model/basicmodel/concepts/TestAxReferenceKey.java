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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestAxReferenceKey {

    @Test
    public void testAxReferenceKey() {
        assertNotNull(new AxReferenceKey());
        assertNotNull(new AxReferenceKey(new AxArtifactKey()));
        assertNotNull(new AxReferenceKey(new AxArtifactKey(), "LocalName"));
        assertNotNull(new AxReferenceKey(new AxReferenceKey()));
        assertNotNull(new AxReferenceKey(new AxReferenceKey(), "LocalName"));
        assertNotNull(new AxReferenceKey(new AxArtifactKey(), "ParentLocalName", "LocalName"));
        assertNotNull(new AxReferenceKey("ParentKeyName", "0.0.1", "LocalName"));
        assertNotNull(new AxReferenceKey("ParentKeyName", "0.0.1", "ParentLocalName", "LocalName"));
        assertNotNull(new AxReferenceKey("ParentKeyName:0.0.1:ParentLocalName:LocalName"));
        assertEquals(AxReferenceKey.getNullKey().getKey(), AxReferenceKey.getNullKey());
        assertEquals("NULL:0.0.0:NULL:NULL", AxReferenceKey.getNullKey().getID());

        AxReferenceKey testReferenceKey = new AxReferenceKey();
        testReferenceKey.setParentArtifactKey(new AxArtifactKey("PN", "0.0.1"));
        assertEquals("PN:0.0.1", testReferenceKey.getParentArtifactKey().getID());
        
        testReferenceKey.setParentReferenceKey(new AxReferenceKey("PN", "0.0.1", "LN"));
        assertEquals("PN:0.0.1:NULL:LN", testReferenceKey.getParentReferenceKey().getID());
        
        testReferenceKey.setParentKeyName("NPKN");
        assertEquals("NPKN", testReferenceKey.getParentKeyName());
        
        testReferenceKey.setParentKeyVersion("0.0.1");
        assertEquals("0.0.1", testReferenceKey.getParentKeyVersion());
        
        testReferenceKey.setParentLocalName("NPKLN");
        assertEquals("NPKLN", testReferenceKey.getParentLocalName());
        
        testReferenceKey.setLocalName("NLN");
        assertEquals("NLN", testReferenceKey.getLocalName());
        
        assertFalse(testReferenceKey.isCompatible(AxArtifactKey.getNullKey()));
        assertFalse(testReferenceKey.isCompatible(AxReferenceKey.getNullKey()));
        assertTrue(testReferenceKey.isCompatible(testReferenceKey));
        
        assertEquals(AxKey.Compatibility.DIFFERENT, testReferenceKey.getCompatibility(AxArtifactKey.getNullKey()));
        assertEquals(AxKey.Compatibility.DIFFERENT, testReferenceKey.getCompatibility(AxReferenceKey.getNullKey()));
        assertEquals(AxKey.Compatibility.IDENTICAL, testReferenceKey.getCompatibility(testReferenceKey));
        
        AxValidationResult result = new AxValidationResult();
        result = testReferenceKey.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());
        
        testReferenceKey.clean();
        
        AxReferenceKey clonedReferenceKey = (AxReferenceKey) testReferenceKey.clone();
        assertEquals("AxReferenceKey:(parentKeyName=NPKN,parentKeyVersion=0.0.1,parentLocalName=NPKLN,localName=NLN)", clonedReferenceKey.toString());
        
        assertFalse(testReferenceKey.hashCode() == 0);
        
        assertTrue(testReferenceKey.equals(testReferenceKey));
        assertTrue(testReferenceKey.equals(clonedReferenceKey));
        assertFalse(testReferenceKey.equals("Hello"));
        assertFalse(testReferenceKey.equals(new AxReferenceKey("PKN", "0.0.2", "PLN", "LN")));
        assertFalse(testReferenceKey.equals(new AxReferenceKey("NPKN", "0.0.2", "PLN", "LN")));
        assertFalse(testReferenceKey.equals(new AxReferenceKey("NPKN", "0.0.1", "PLN", "LN")));
        assertFalse(testReferenceKey.equals(new AxReferenceKey("NPKN", "0.0.1", "NPLN", "LN")));
        assertTrue(testReferenceKey.equals(new AxReferenceKey("NPKN", "0.0.1", "NPKLN", "NLN")));
        
        assertEquals(0, testReferenceKey.compareTo(testReferenceKey));
        assertEquals(0, testReferenceKey.compareTo(clonedReferenceKey));
        assertNotEquals(0, testReferenceKey.compareTo(new AxArtifactKey()));
        assertNotEquals(0, testReferenceKey.compareTo(new AxReferenceKey("PKN", "0.0.2", "PLN", "LN")));
        assertNotEquals(0, testReferenceKey.compareTo(new AxReferenceKey("NPKN", "0.0.2", "PLN", "LN")));
        assertNotEquals(0, testReferenceKey.compareTo(new AxReferenceKey("NPKN", "0.0.1", "PLN", "LN")));
        assertNotEquals(0, testReferenceKey.compareTo(new AxReferenceKey("NPKN", "0.0.1", "NPLN", "LN")));
        assertEquals(0, testReferenceKey.compareTo(new AxReferenceKey("NPKN", "0.0.1", "NPKLN", "NLN")));
        
        assertNotNull(testReferenceKey.getKeys());
    }
}
