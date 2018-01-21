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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ericsson.apex.model.utilities.CollectionUtils;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CollectionUtilitiesTest {

    @Test
    public void testNullLists() {
        List<String> leftList  = new ArrayList<String>();
        List<String> rightList = new ArrayList<String>();
        
        int result = 0;
        
        result = CollectionUtils.compareLists(null, null);
        assertEquals(0, result);

        result = CollectionUtils.compareLists(leftList, null);
        assertEquals(-1, result);

        result = CollectionUtils.compareLists(null, rightList);
        assertEquals(1, result);

        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(0, result);

        leftList.add("AAA");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(-1, result);

        rightList.add("AAA");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(0, result);

        rightList.add("BBB");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(1, result);

        leftList.add("BBB");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(0, result);

        leftList.add("CCA");
        rightList.add("CCB");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(-1, result);
        
        leftList.remove(leftList.size() -1);
        rightList.remove(rightList.size() -1);
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(0, result);

        leftList.add("CCB");
        rightList.add("CCA");
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(1, result);
        
        leftList.remove(leftList.size() -1);
        rightList.remove(rightList.size() -1);
        result = CollectionUtils.compareLists(leftList, rightList);
        assertEquals(0, result);
    }
}
