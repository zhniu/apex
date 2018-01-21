/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestAbstractParameters {

    @Test
    public void testAbstractParameters() {
        LegalParameters parameters = new LegalParameters();
        assertNotNull(parameters);
        assertEquals("AbstractParameters [parameterClassName=com.ericsson.apex.model.basicmodel.service.LegalParameters]", parameters.toString());

        assertEquals(LegalParameters.class, parameters.getParameterClass());
        assertEquals("com.ericsson.apex.model.basicmodel.service.LegalParameters", parameters.getParameterClassName());
        
        try {
            new IllegalParameters();
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("class \"somewhere.over.the.rainbow\" not found or not an instance of \"com.ericsson.apex.model.basicmodel.service.IllegalParameters\"", e.getMessage());
        }
    }
}
