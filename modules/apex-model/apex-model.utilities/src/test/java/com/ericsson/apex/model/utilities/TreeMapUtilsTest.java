/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TreeMapUtilsTest {

    @Test
    public void test() {
        TreeMap<String, String> testTreeMap = new TreeMap<String, String>();
        testTreeMap.put("G", "G");
        testTreeMap.put("H", "H");
        testTreeMap.put("JA", "JA");
        testTreeMap.put("JAM", "JAM");
        testTreeMap.put("JOE", "JOE");
        testTreeMap.put("JOSH", "JOSH");
        testTreeMap.put("K", "K");
        
        List<Entry<String, String>> foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "F");
        assertEquals(0, foundKeyList.size());

        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "G");
        assertEquals("G", foundKeyList.get(0).getKey());

        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "H");
        assertEquals("H", foundKeyList.get(0).getKey());
 
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "I");
        assertEquals(0, foundKeyList.size());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "J");
        assertEquals("JA", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JA");
        assertEquals("JA", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JB");
        assertEquals(0, foundKeyList.size());

        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JO");
        assertEquals("JOE", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JOE");
        assertEquals("JOE", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JOS");
        assertEquals("JOSH", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "JOSH");
        assertEquals("JOSH", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "K");
        assertEquals("K", foundKeyList.get(0).getKey());
        
        foundKeyList = TreeMapUtils.findMatchingEntries(testTreeMap, "L");
        assertEquals(0, foundKeyList.size());
    }
}
