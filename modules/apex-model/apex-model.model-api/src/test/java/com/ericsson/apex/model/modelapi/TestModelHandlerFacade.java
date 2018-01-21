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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.modelapi.impl.ModelHandlerFacade;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelHandlerFacade {

    @Test
    public void testModelHandlerFacade() throws IOException {
        try {
            new ModelHandlerFacade(null, null, false);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("apexModel may not be null", e.getMessage());
        }

        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        try {
            new ModelHandlerFacade(apexModel, null, false);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("apexProperties may not be null", e.getMessage());
        }

        Properties modelProperties = new Properties();
        ModelHandlerFacade mhf = new ModelHandlerFacade(apexModel, modelProperties, false);
        assertNotNull(mhf);

        ApexAPIResult result = mhf.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mhf.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertEquals(ApexAPIResult.RESULT.CONCEPT_EXISTS, result.getResult());

        String modelString = TextFileUtils.getTextFileAsString("src/test/resources/models/PolicyModel.json");

        result = apexModel.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mhf.loadFromString(modelString);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mhf.loadFromString(modelString);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_EXISTS, result.getResult());

        DAOParameters daoParameters = new DAOParameters();
        result = mhf.loadFromDatabase("SomeModel", null, daoParameters);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_EXISTS, result.getResult());

        result = apexModel.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = mhf.loadFromDatabase("SomeModel", null, daoParameters);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.saveToDatabase(daoParameters);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.readFromURL("http://somewhere/over/the/rainbow");
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.loadFromString(modelString);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mhf.readFromURL("http://somewhere/over/the/rainbow");
        assertEquals(ApexAPIResult.RESULT.CONCEPT_EXISTS, result.getResult());

        File tempFile = File.createTempFile("ApexModel", "json");
        tempFile.deleteOnExit();

        result = mhf.writeToURL("File:///" +  tempFile.getCanonicalPath(), false);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.validate();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mhf.validate();
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.compare("src/test/resources/models/NonExistant.json", true, true);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.compareWithString("zooby", true, true);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.split("FailSplit", "NonExistantPolicy");
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = mhf.split("NonExistantPolicy");
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        result = mhf.merge("src/test/resources/models/NonExistant.json", false);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        result = apexModel.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mhf.merge("src/test/resources/models/PolicyModel.json", false);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        
        result = mhf.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = mhf.mergeWithString("@£@$@£", true);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        result = mhf.mergeWithString(modelString, false);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
    }
}
