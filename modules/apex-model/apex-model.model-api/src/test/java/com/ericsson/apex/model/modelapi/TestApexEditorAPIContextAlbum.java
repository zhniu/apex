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
public class TestApexEditorAPIContextAlbum {
    @Test
    public void testContextAlbumCRUD() {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.validateContextAlbum(null, null);
        assertEquals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, result.getResult());

        result = apexModel.validateContextAlbum("%%%$£", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
   
        result = apexModel.createContextAlbum("MyMap002", "0.0.2", "APPLICATION", "true", "MapType", "0.0.1", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextAlbum("MyMap012", "0.1.2", "ZOOBY", "false", "MapType", "0.0.1", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 012");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextAlbum("MyMap012", "0.1.4", "UNDEFINED", null, "MapType", "0.0.1", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 014");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextAlbum("MyMap012", null, null, null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "+++", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "MapZooby", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "MapType", "--++", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "MapType", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "MapType", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextAlbum("MyMap012", null, "EPHEMERAL", "false", "MapType", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.createContextAlbum("MyMap002", "0.0.2", "APPLICATION", "true", "MapType", null, "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_EXISTS));
        result = apexModel.createContextAlbum("MyMap011", "0.1.2", "APPLICATION", "true", "MapType", "0.0.1", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteContextAlbum("MyMap012", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.createContextAlbum("MyMap012", "0.1.2", "ZOOBY", "false", "MapType", "0.0.1", "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 012");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.validateContextAlbum(null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModel.updateContextAlbum(null, null, null, null, null, null, null, null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());
        result = apexModel.updateContextAlbum("MyMap002", "0.0.2", null, null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap002", "0.0.2", "ZOOBY", "true", null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap002", "0.0.2", null, null, null, null, "1fa2e430-f2b2-11e6-bc64-92361f002700", "A description of 002");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, null, null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, null, "true", null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap015", null, null, null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateContextAlbum("MyMap014", "0.1.5", null, null, null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "false", null, null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "false", "StringType", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "false", "String", null, null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "false", "StringType", "0.0.2", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "false", "StringType", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.updateContextAlbum("MyMap012", null, "APPLICATION", "Hello", "StringType", "0.0.1", null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listContextAlbum("@£%%$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.listContextAlbum(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listContextAlbum("MyMap012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listContextAlbum("MyMap012", "0.0.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        result = apexModel.listContextAlbum("MyMap012", "0.2.5");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));
        result = apexModel.listContextAlbum("MyMap014", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteContextAlbum("@£%%$", null);
        assertEquals(ApexAPIResult.RESULT.FAILED, result.getResult());

        result = apexModel.deleteContextAlbum("MyMap012", "0.1.1");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteContextAlbum("MyMap012oooo", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.listContextAlbum("MyMap012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 3);

        result = apexModel.deleteContextAlbum("MyMap012", "0.1.2");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listContextAlbum("MyMap012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertTrue(result.getMessages().size() == 2);

        result = apexModel.deleteContextAlbum("MyMap012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.listContextAlbum("MyMap012", null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST));

        result = apexModel.deleteContextAlbum(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(4, result.getMessages().size());

        result = apexModel.listContextAlbum(null, null);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        assertEquals(0, result.getMessages().size());
    }
}
