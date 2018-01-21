/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.handling;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.ericsson.apex.model.contextmodel.handling.ContextComparer;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextComparison {
    private AxContextModel emptyModel;
    private AxContextModel fullModel;
    private AxContextModel noGlobalContextModel;
    private AxContextModel shellModel;
    private AxContextModel singleEntryModel;

    @Before
    public void getContext() {
        TestContextComparisonFactory factory = new TestContextComparisonFactory();
        emptyModel           = factory.getEmptyModel();
        fullModel            = factory.getFullModel();
        noGlobalContextModel = factory.getNoGlobalContextModel();
        shellModel           = factory.getShellModel();
        singleEntryModel     = factory.getSingleEntryModel();
    }
    
    @Test
    public void testEmptyEmpty() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(emptyModel.getSchemas(), emptyModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(emptyModel.getSchemas().getSchemasMap().equals(schemaResult.getIdenticalValues()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(emptyModel.getAlbums(), emptyModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(emptyModel.getAlbums().getAlbumsMap().equals(albumResult.getIdenticalValues()));
    }
    
    @Test
    public void testEmptyFull() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(emptyModel.getSchemas(), fullModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(fullModel.getSchemas().getSchemasMap().equals(schemaResult.getRightOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(emptyModel.getAlbums(), fullModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(fullModel.getAlbums().getAlbumsMap().equals(albumResult.getRightOnly()));
    }
    
    @Test
    public void testFullEmpty() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(fullModel.getSchemas(), emptyModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(fullModel.getSchemas().getSchemasMap().equals(schemaResult.getLeftOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(fullModel.getAlbums(), emptyModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(fullModel.getAlbums().getAlbumsMap().equals(albumResult.getLeftOnly()));
    }
    
    @Test
    public void testEmptyNoGlobalContext() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(emptyModel.getSchemas(), noGlobalContextModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(noGlobalContextModel.getSchemas().getSchemasMap().equals(schemaResult.getRightOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(emptyModel.getAlbums(), noGlobalContextModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(noGlobalContextModel.getAlbums().getAlbumsMap().equals(albumResult.getRightOnly()));
    }
    
    @Test
    public void testNoGlobalContextEmpty() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(noGlobalContextModel.getSchemas(), emptyModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(noGlobalContextModel.getSchemas().getSchemasMap().equals(schemaResult.getLeftOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(noGlobalContextModel.getAlbums(), emptyModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(noGlobalContextModel.getAlbums().getAlbumsMap().equals(albumResult.getLeftOnly()));
    }
    
    @Test
    public void testEmptyShell() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(emptyModel.getSchemas(), shellModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(shellModel.getSchemas().getSchemasMap().equals(schemaResult.getRightOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(emptyModel.getAlbums(), shellModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(shellModel.getAlbums().getAlbumsMap().equals(albumResult.getRightOnly()));
    }
    
    @Test
    public void testShellEmpty() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(shellModel.getSchemas(), emptyModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(shellModel.getSchemas().getSchemasMap().equals(schemaResult.getLeftOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(shellModel.getAlbums(), emptyModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(shellModel.getAlbums().getAlbumsMap().equals(albumResult.getLeftOnly()));
    }
    
    @Test
    public void testEmptySingleEntry() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(emptyModel.getSchemas(), singleEntryModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(singleEntryModel.getSchemas().getSchemasMap().equals(schemaResult.getRightOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(emptyModel.getAlbums(), singleEntryModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(singleEntryModel.getAlbums().getAlbumsMap().equals(albumResult.getRightOnly()));
    }
    
    @Test
    public void testSingleEntryEmpty() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(singleEntryModel.getSchemas(), emptyModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(singleEntryModel.getSchemas().getSchemasMap().equals(schemaResult.getLeftOnly()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(singleEntryModel.getAlbums(), emptyModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(singleEntryModel.getAlbums().getAlbumsMap().equals(albumResult.getLeftOnly()));
    }
    
    @Test
    public void testFullFull() {
        KeyedMapDifference<AxArtifactKey, AxContextSchema> schemaResult = new ContextComparer().compare(fullModel.getSchemas(), fullModel.getSchemas());
        assertNotNull(schemaResult);
        assertTrue(fullModel.getSchemas().getSchemasMap().equals(schemaResult.getIdenticalValues()));

        KeyedMapDifference<AxArtifactKey, AxContextAlbum> albumResult = new ContextComparer().compare(fullModel.getAlbums(), fullModel.getAlbums());
        assertNotNull(albumResult);
        assertTrue(fullModel.getAlbums().getAlbumsMap().equals(albumResult.getIdenticalValues()));
    }
}
