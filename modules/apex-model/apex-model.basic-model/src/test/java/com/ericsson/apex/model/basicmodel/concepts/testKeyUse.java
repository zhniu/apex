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

import com.ericsson.apex.model.basicmodel.concepts.AxKey.Compatibility;

public class testKeyUse {

    @Test
    public void test() {
        assertNotNull(new AxKeyUse());
        assertNotNull(new AxKeyUse(new AxArtifactKey()));
        assertNotNull(new AxKeyUse(new AxReferenceKey()));
        
        AxArtifactKey key = new AxArtifactKey("Key", "0.0.1");
        AxKeyUse keyUse = new AxKeyUse();
        keyUse.setKey(key);
        assertEquals(key, keyUse.getKey());
        assertEquals("Key:0.0.1", keyUse.getID());
        assertEquals(key, keyUse.getKeys().get(0));
        
        assertEquals(Compatibility.IDENTICAL, keyUse.getCompatibility(key));
        assertTrue(keyUse.isCompatible(key));
        
        keyUse.clean();
        assertNotNull(keyUse);
        
        AxValidationResult result = new AxValidationResult();
        result = keyUse.validate(result);
        assertNotNull(result);
        
        assertNotEquals(0, keyUse.hashCode());
        
        AxKeyUse clonedKeyUse = (AxKeyUse) keyUse.clone();
        assertEquals("AxKeyUse:(usedKey=AxArtifactKey:(name=Key,version=0.0.1))", clonedKeyUse.toString());
        
        assertFalse(keyUse.hashCode() == 0);
        
        assertTrue(keyUse.equals(keyUse));
        assertTrue(keyUse.equals(clonedKeyUse));
        assertFalse(keyUse.equals("Hello"));
        assertTrue(keyUse.equals(new AxKeyUse(key)));
        
        assertEquals(0, keyUse.compareTo(keyUse));
        assertEquals(0, keyUse.compareTo(clonedKeyUse));
        assertNotEquals(0, keyUse.compareTo(new AxArtifactKey()));
        assertEquals(0, keyUse.compareTo(new AxKeyUse(key)));
        
        AxKeyUse keyUseNull = new AxKeyUse(AxArtifactKey.getNullKey());
        AxValidationResult resultNull = new AxValidationResult();
        assertEquals(false, keyUseNull.validate(resultNull).isValid());
    }
}
