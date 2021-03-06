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

import org.junit.Test;

/**
 * The Class ResourceUtilsTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AssertionsTest {
    @Test
    public void testAssertions() {
        Assertions.argumentNotFalse(true, "it is true");
        
        try {
            Assertions.argumentNotFalse(false, "it is false");
        }
        catch (IllegalArgumentException e) {
            assertEquals("it is false", e.getMessage());
        }
        
        Assertions.argumentNotFalse(true, ArithmeticException.class, "it is true");
        
        try {
            Assertions.argumentNotFalse(false, ArithmeticException.class, "it is false");
        }
        catch (Exception e) {
            assertEquals("it is false", e.getMessage());
        }
        
        Assertions.argumentNotNull("Hello", "it is OK");
        
        try {
            Assertions.argumentNotNull(null, "it is null");
        }
        catch (IllegalArgumentException e) {
            assertEquals("it is null", e.getMessage());
        }

        Assertions.argumentNotNull(true, ArithmeticException.class, "it is OK");
        
        try {
            Assertions.argumentNotNull(null, ArithmeticException.class, "it is null");
        }
        catch (Exception e) {
            assertEquals("it is null", e.getMessage());
        }
        
        Assertions.assignableFrom(java.util.TreeMap.class, java.util.Map.class);
        
        try {
            Assertions.assignableFrom(java.util.Map.class, java.util.TreeMap.class);
        }
        catch (IllegalArgumentException e) {
            assertEquals("java.util.Map is not an instance of java.util.TreeMap", e.getMessage());
        }
        
        Assertions.instanceOf("Hello", String.class);
        
        try {
            Assertions.instanceOf(100, String.class);
        }
        catch (IllegalArgumentException e) {
            assertEquals("java.lang.Integer is not an instance of java.lang.String", e.getMessage());
        }
        
        Assertions.validateStringParameter("name", "MyName", "^M.*e$");

        try {
            Assertions.validateStringParameter("name", "MyName", "^M.*f$");
        }
        catch (IllegalArgumentException e) {
            assertEquals("parameter \"name\": value \"MyName\", does not match regular expression \"^M.*f$\"", e.getMessage());
        }
    }
}
