/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextModel {

    @Test
    public void test() {
        assertNotNull(new AxContextModel());
        assertNotNull(new AxContextModel(new AxArtifactKey()));
        assertNotNull(new AxContextModel(new AxArtifactKey(), new AxContextSchemas(), new AxKeyInformation()));
        assertNotNull(new AxContextModel(new AxArtifactKey(), new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation()));
        
        AxArtifactKey modelKey = new AxArtifactKey("ModelKey", "0.0.1");
        AxArtifactKey schemasKey = new AxArtifactKey("SchemasKey", "0.0.1");
        AxArtifactKey albumsKey = new AxArtifactKey("SchemasKey", "0.0.1");
        AxArtifactKey keyInfoKey = new AxArtifactKey("SchemasKey", "0.0.1");
        AxContextModel model = new AxContextModel(modelKey, new AxContextSchemas(schemasKey), new AxContextAlbums(albumsKey), new AxKeyInformation(keyInfoKey));
        model.register();
        
        model.clean();
        assertNotNull(model);
        assertEquals("AxContextModel:(AxContextModel:(key=AxArtifactKey:", model.toString().substring(0, 50));
        
        AxContextModel clonedModel = new AxContextModel(model);

        assertFalse(model.hashCode() == 0);

        assertTrue(model.equals(model));
        assertTrue(model.equals(clonedModel));
        assertFalse(model.equals("Hello"));
        assertFalse(model.equals(new AxContextModel(new AxArtifactKey())));
        assertFalse(model.equals(new AxContextModel(new AxArtifactKey(), new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation())));
        assertFalse(model.equals(new AxContextModel(modelKey, new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation())));
        assertFalse(model.equals(new AxContextModel(modelKey, new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation(keyInfoKey))));
        assertFalse(model.equals(new AxContextModel(modelKey, new AxContextSchemas(schemasKey), new AxContextAlbums(), new AxKeyInformation(keyInfoKey))));
        assertTrue(model.equals(new AxContextModel(modelKey, new AxContextSchemas(schemasKey), new AxContextAlbums(albumsKey), new AxKeyInformation(keyInfoKey))));

        assertEquals(0, model.compareTo(model));
        assertEquals(0, model.compareTo(clonedModel));
        assertNotEquals(0, model.compareTo(new AxArtifactKey()));
        assertNotEquals(0, model.compareTo(new AxContextModel(new AxArtifactKey(), new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation())));
        assertNotEquals(0, model.compareTo(new AxContextModel(modelKey, new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation())));
        assertNotEquals(0, model.compareTo(new AxContextModel(modelKey, new AxContextSchemas(), new AxContextAlbums(), new AxKeyInformation(keyInfoKey))));
        assertNotEquals(0, model.compareTo(new AxContextModel(modelKey, new AxContextSchemas(schemasKey), new AxContextAlbums(), new AxKeyInformation(keyInfoKey))));
        assertEquals(0, model.compareTo(new AxContextModel(modelKey, new AxContextSchemas(schemasKey), new AxContextAlbums(albumsKey), new AxKeyInformation(keyInfoKey))));
    }
}
