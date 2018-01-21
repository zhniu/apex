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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestParameterService {

    @Test
    public void testParameterService() {
        ParameterService.clear();

        assertFalse(ParameterService.existsParameters(LegalParameters.class));
        try {
            ParameterService.getParameters(LegalParameters.class);
        }
        catch (Exception e) {
            assertEquals("Parameters for com.ericsson.apex.model.basicmodel.service.LegalParameters not found in parameter service", e.getMessage());
        }
        
        ParameterService.registerParameters(LegalParameters.class, new LegalParameters());
        assertTrue(ParameterService.existsParameters(LegalParameters.class));
        assertNotNull(ParameterService.getParameters(LegalParameters.class));
        
        ParameterService.deregisterParameters(LegalParameters.class);
       
        assertFalse(ParameterService.existsParameters(LegalParameters.class));
        try {
            ParameterService.getParameters(LegalParameters.class);
        }
        catch (Exception e) {
            assertEquals("Parameters for com.ericsson.apex.model.basicmodel.service.LegalParameters not found in parameter service", e.getMessage());
        }

        ParameterService.registerParameters(LegalParameters.class, new LegalParameters());
        assertTrue(ParameterService.existsParameters(LegalParameters.class));
        assertNotNull(ParameterService.getParameters(LegalParameters.class));
        
        assertNotNull(ParameterService.getAll());
        ParameterService.clear();
        assertFalse(ParameterService.existsParameters(LegalParameters.class));
        try {
            ParameterService.getParameters(LegalParameters.class);
        }
        catch (Exception e) {
            assertEquals("Parameters for com.ericsson.apex.model.basicmodel.service.LegalParameters not found in parameter service", e.getMessage());
        }
        
    }
}
