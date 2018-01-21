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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexEditorAPIKeyInfo {

    @Test
    public void testKeyInfoCRUD() {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.validateKeyInformation(null, null);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, result.getResult());

        result = apexModel.validateKeyInformation("%%%$£", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.createKeyInformation(null, null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.createKeyInformation("Hello", "0.0.2", "1fa2e430-f2b2-11e6-bc64-92361f002671", "A description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createKeyInformation("Hello", "0.1.2", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createKeyInformation("Hello", "0.1.4", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of connichi wa");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createKeyInformation("Hello", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createKeyInformation("Hello", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));

        result = apexModel.createKeyInformation("Hello", "0.1.2", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));

        result = apexModel.validateKeyInformation(null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.updateKeyInformation(null, null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.updateKeyInformation("Hello", "0.0.2", null, "An updated description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateKeyInformation("Hello", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateKeyInformation("Hello", "0.1.2", "1fa2e430-f2b2-11e6-bc64-92361f002673", "A further updated description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.updateKeyInformation("Hello2", "0.0.2", null, "An updated description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listKeyInformation(null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.listKeyInformation("%%%$$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.listKeyInformation("Hello", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 2);

        result = apexModel.listKeyInformation("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 4);

        result = apexModel.deleteKeyInformation("Hello", "0.1.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteKeyInformation("Hellooooo", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listKeyInformation("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 4);

        result = apexModel.listKeyInformation(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 22);

        result = apexModel.deleteKeyInformation("%%%$$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.deleteKeyInformation("Hello", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listKeyInformation("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 3);

        result = apexModel.deleteKeyInformation("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listKeyInformation("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteKeyInformation(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(18, result.getMessages().size());

        result = apexModel.listKeyInformation(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(0, result.getMessages().size());
    }
}
