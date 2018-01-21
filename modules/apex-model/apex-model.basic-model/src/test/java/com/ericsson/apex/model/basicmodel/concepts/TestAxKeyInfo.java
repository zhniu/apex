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

import java.util.UUID;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestAxKeyInfo {

    @Test
    public void testAxKeyInfo() {
        assertNotNull(new AxKeyInfo());
        assertNotNull(new AxKeyInfo(new AxArtifactKey()));
        assertNotNull(new AxKeyInfo(new AxArtifactKey(), UUID.randomUUID(), "Key description"));

        AxKeyInfo testKeyInfo = new AxKeyInfo();
        testKeyInfo.setKey((new AxArtifactKey("PN", "0.0.1")));
        assertEquals("PN:0.0.1", testKeyInfo.getKey().getID());

        AxArtifactKey key = new AxArtifactKey("key", "0.0.1");
        testKeyInfo.setKey(key);
        assertEquals(key, testKeyInfo.getKey());
        
        UUID uuid = UUID.randomUUID();
        testKeyInfo.setUuid(uuid);
        assertEquals(uuid, testKeyInfo.getUUID());
        testKeyInfo.setDescription("Key Description");
        assertEquals("Key Description", testKeyInfo.getDescription());
        
        AxKeyInfo clonedReferenceKey = (AxKeyInfo) testKeyInfo.clone();
        assertTrue(clonedReferenceKey.toString().startsWith("AxKeyInfo:(artifactId=AxArtifactKey:(name=key,version=0.0.1),uuid="));
        
        assertFalse(testKeyInfo.hashCode() == 0);
        
        assertTrue(testKeyInfo.equals(testKeyInfo));
        assertTrue(testKeyInfo.equals(clonedReferenceKey));
        assertFalse(testKeyInfo.equals(null));
        assertFalse(testKeyInfo.equals(new AxArtifactKey()));
        assertFalse(testKeyInfo.equals(new AxKeyInfo(new AxArtifactKey())));
        assertFalse(testKeyInfo.equals(new AxKeyInfo(key, UUID.randomUUID(), "Some Description")));
        assertFalse(testKeyInfo.equals(new AxKeyInfo(key, uuid, "Some Description")));
        assertTrue(testKeyInfo.equals(new AxKeyInfo(key, uuid, "Key Description")));
        
        assertEquals(0, testKeyInfo.compareTo(testKeyInfo));
        assertEquals(0, testKeyInfo.compareTo(clonedReferenceKey));
        assertNotEquals(0, testKeyInfo.compareTo(null));
        assertNotEquals(0, testKeyInfo.compareTo(new AxArtifactKey()));
        assertNotEquals(0, testKeyInfo.compareTo(new AxKeyInfo(new AxArtifactKey())));
        assertNotEquals(0, testKeyInfo.compareTo(new AxKeyInfo(key, UUID.randomUUID(), "Some Description")));
        assertNotEquals(0, testKeyInfo.compareTo(new AxKeyInfo(key, uuid, "Some Description")));
        assertEquals(0, testKeyInfo.compareTo(new AxKeyInfo(key, uuid, "Key Description")));
        
        assertNotNull(testKeyInfo.getKeys());
        
        AxValidationResult result = new AxValidationResult();
        result = testKeyInfo.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());
        
        testKeyInfo.setDescription("");
        result = testKeyInfo.validate(result);
        assertEquals(AxValidationResult.ValidationResult.OBSERVATION, result.getValidationResult());
        
        testKeyInfo.setUuid(new UUID(0, 0));
        result = testKeyInfo.validate(result);
        assertEquals(AxValidationResult.ValidationResult.WARNING, result.getValidationResult());
        
        testKeyInfo.setKey(AxArtifactKey.getNullKey());
        result = testKeyInfo.validate(result);
        assertEquals(AxValidationResult.ValidationResult.INVALID, result.getValidationResult());
        
        assertNotNull(AxKeyInfo.generateReproducibleUUID(null));
        assertNotNull(AxKeyInfo.generateReproducibleUUID("SeedString"));
        
        testKeyInfo.clean();
        assertNotNull(testKeyInfo);
    }
}
