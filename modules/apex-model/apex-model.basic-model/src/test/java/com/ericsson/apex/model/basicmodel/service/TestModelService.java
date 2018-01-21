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

import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.handling.TestApexBasicModelCreator;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelService {

    @Test
    public void testModelService() {
        ModelService.clear();

        assertFalse(ModelService.existsModel(AxKeyInformation.class));
        try {
            ModelService.getModel(AxKeyInformation.class);
        }
        catch (Exception e) {
            assertEquals("Model for com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation not found in model service", e.getMessage());
        }
        
        ModelService.registerModel(AxKeyInformation.class, new TestApexBasicModelCreator().getModel().getKeyInformation());
        assertTrue(ModelService.existsModel(AxKeyInformation.class));
        assertNotNull(ModelService.getModel(AxKeyInformation.class));
        
        ModelService.deregisterModel(AxKeyInformation.class);
       
        assertFalse(ModelService.existsModel(AxKeyInformation.class));
        try {
            ModelService.getModel(AxKeyInformation.class);
        }
        catch (Exception e) {
            assertEquals("Model for com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation not found in model service", e.getMessage());
        }

        ModelService.registerModel(AxKeyInformation.class, new TestApexBasicModelCreator().getModel().getKeyInformation());
        assertTrue(ModelService.existsModel(AxKeyInformation.class));
        assertNotNull(ModelService.getModel(AxKeyInformation.class));
        
        ModelService.clear();
        assertFalse(ModelService.existsModel(AxKeyInformation.class));
        try {
            ModelService.getModel(AxKeyInformation.class);
        }
        catch (Exception e) {
            assertEquals("Model for com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation not found in model service", e.getMessage());
        }

    }
}
