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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PropertyUtilsTest {

    @Test
    public void test() {
        System.setProperty("boolean.true", "true");
        System.setProperty("boolean.false", "false");
        System.setProperty("boolean.blank", " ");
        
        assertNotNull(PropertyUtils.getAllProperties());
        
        assertEquals(PropertyUtils.propertySetOrTrue(null), false);
        assertEquals(PropertyUtils.propertySetOrTrue("ZOOBY"), false);
        assertEquals(PropertyUtils.propertySetOrTrue("boolean.true"), true);
        assertEquals(PropertyUtils.propertySetOrTrue("boolean.blank"), true);
        assertEquals(PropertyUtils.propertySetOrTrue("boolean.false"), false);
    }
}
