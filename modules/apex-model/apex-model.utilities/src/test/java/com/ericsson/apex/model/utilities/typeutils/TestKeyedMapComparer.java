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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import org.junit.Test;

import com.ericsson.apex.model.utilities.comparison.KeyedMapComparer;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestKeyedMapComparer {

    @Test
    public void test() {
        TreeMap<String, String> leftMap = new TreeMap<String, String>();
        leftMap.put("B", "BBBBB");
        leftMap.put("C", "CCCCC");
        leftMap.put("E", "EEEEE");
        leftMap.put("G", "GGGGG");

        TreeMap<String, String> rightMap = new TreeMap<String, String>();
        rightMap.put("A", "AAAAA");
        rightMap.put("B", "B");
        rightMap.put("D", "DDDDD");
        rightMap.put("E", "EEEEE");
        rightMap.put("F", "FFFFF");
        rightMap.put("G", "G");
        
        KeyedMapDifference<String, String> kmComparedSame = new KeyedMapComparer<String, String>().compareMaps(leftMap, leftMap);
        KeyedMapDifference<String, String> kmComparedDiff = new KeyedMapComparer<String, String>().compareMaps(leftMap, rightMap);
        
        assertTrue(kmComparedSame.getIdenticalValues().equals(leftMap));
        assertEquals(1, kmComparedDiff.getLeftOnly().size());
        assertEquals(3, kmComparedDiff.getRightOnly().size());
        assertEquals(2, kmComparedDiff.getDifferentValues().size());
        assertEquals(1, kmComparedDiff.getIdenticalValues().size());
        
        assertNotNull(kmComparedSame.asString(true, true));
        assertNotNull(kmComparedSame.asString(true, false));
        assertNotNull(kmComparedSame.asString(false, false));
        assertNotNull(kmComparedSame.asString(false, true));
        
        assertNotNull(kmComparedDiff.asString(true, true));
        assertNotNull(kmComparedDiff.asString(true, false));
        assertNotNull(kmComparedDiff.asString(false, false));
        assertNotNull(kmComparedDiff.asString(false, true));
    }
}
