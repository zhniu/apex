/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities.typeutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.utilities.comparison.KeyComparer;
import com.ericsson.apex.model.utilities.comparison.KeyDifference;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestKeyComparer {

    @Test
    public void test() {
        KeyDifference<String> keyDifference = new KeyComparer<String>().compareKeys("Hello", "Goodbye");
        
        assertFalse(keyDifference.isEqual());
        assertTrue("Hello".equals(keyDifference.getLeftKey().toString()));
        assertTrue("Goodbye".equals(keyDifference.getRightKey().toString()));

        assertTrue("left key Hello and right key Goodbye differ\n".equals(keyDifference.asString(true)));
        assertTrue("left key Hello and right key Goodbye differ\n".equals(keyDifference.asString(false)));
        
        KeyDifference<String> keyDifference2 = new KeyComparer<String>().compareKeys("Here", "Here");
        assertTrue("".equals(keyDifference2.asString(true)));
        assertTrue("left key Here equals right key Here\n".equals(keyDifference2.asString(false)));
    }
}
