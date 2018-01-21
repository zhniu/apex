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
public class TestApexEditorAPIContextSchema {
    @Test
    public void testContextSchemaCRUD() {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.validateContextSchemas(null, null);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, result.getResult());

        result = apexModel.validateContextSchemas("%%%$£", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listContextSchemas(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.createContextSchema("Hello", "0.0.2", "Java", "java.lang.String", "1fa2e430-f2b2-11e6-bc64-92361f002671", "A description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", "0.1.2", "Java", "java.lang.String", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", "0.1.4", "Java", "java.lang.String", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of connichi wa");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", null, "Java", "java.lang.String", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", null, "Java", "java.lang.String", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.deleteContextSchema("Hello", "0.1.4");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", "0.1.4", "Java", "java.lang.String", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of connichi wa");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
   
        result = apexModel.createContextSchema("Hello2", null, null, "java.lang.String", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextSchema("Hello2", null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextSchema("Hello2", null, "Java", "java.lang.String", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.createContextSchema("Hello", "0.1.2", "Java", "java.lang.Float", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));

        result = apexModel.deleteContextSchema("Hello", "0.1.4");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextSchema("Hello", "0.1.4", "Java", "java.lang.String", "1fa2e430-f2b2-11e6-bc64-92361f002672", "A description of connichi wa");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.validateContextSchemas(null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.updateContextSchema(null, null, null, null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.updateContextSchema("Hello", "0.0.2", null, null, null, "An updated description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextSchema("Hello", "0.0.2", null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextSchema("Hello", "0.1.2", "Java", "java.lang.Integer", "1fa2e430-f2b2-11e6-bc64-92361f002673", "A further updated description of hola");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.updateContextSchema("Hello2", "0.0.2", null, null, null, "An updated description of hello");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listContextSchemas("@£%%$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.listContextSchemas("Hello", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 2);

        result = apexModel.listContextSchemas("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 4);

        result = apexModel.listContextSchemas(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 9);

        result = apexModel.deleteContextSchema("@£%%$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.deleteContextSchema("Hello", "0.1.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteContextSchema("Hellooooo", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listContextSchemas("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 4);

        result = apexModel.deleteContextSchema("Hello", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listContextSchemas("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 3);

        result = apexModel.deleteContextSchema("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listContextSchemas("Hello", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listContextSchemas(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteContextSchema(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(5, result.getMessages().size());

        result = apexModel.listContextSchemas(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(0, result.getMessages().size());
    }
}
