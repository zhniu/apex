/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.UUID;

import org.junit.Test;

import com.ericsson.apex.model.modelapi.impl.ModelFacade;

public class TestModelFacade {

    @Test
    public void testModelFacade() {
        try {
            new ModelFacade(null, null, false);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("apexModel may not be null", e.getMessage());
        }
        
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        try {
            new ModelFacade(apexModel, null, false);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("apexProperties may not be null", e.getMessage());
        }
        
        Properties modelProperties = new Properties();
        ModelFacade mf = new ModelFacade(apexModel, modelProperties, false);

        ApexAPIResult result = mf.createModel(null, null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        result = mf.createModel("ModelName", null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        result = mf.createModel("ModelName", "0.0.1", null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        modelProperties.setProperty("DEFAULT_CONCEPT_VERSION", "");
        result = mf.createModel("ModelName", null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        modelProperties.setProperty("DEFAULT_CONCEPT_VERSION", "£$£$£$");
        result = mf.createModel("ModelName", null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        modelProperties.setProperty("DEFAULT_CONCEPT_VERSION", "0.0.1");
        result = mf.createModel("ModelName", null, null, null);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_EXISTS, result.getResult());
        result = mf.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mf.createModel("ModelName", null, null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = mf.updateModel("ModelName", null, UUID.randomUUID().toString(), "New Description");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mf.updateModel("ModelName", "0.0.1", UUID.randomUUID().toString(), "New Description");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        modelProperties.remove("DEFAULT_CONCEPT_VERSION");
        result = mf.updateModel("ModelName", null, UUID.randomUUID().toString(), "New Description");
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mf.updateModel(null, null, UUID.randomUUID().toString(), "New Description");
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mf.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mf.updateModel("name", "0.0.1", UUID.randomUUID().toString(), "New Description");
        assertEquals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, result.getResult());

        result = mf.getModelKey();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mf.listModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        assertEquals("AxPolicyModel:(AxPolicyModel:(key=AxArtifactKey:(n", result.getMessage().substring(0,  50));

        result = mf.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        assertNotNull(mf);
    }
}
