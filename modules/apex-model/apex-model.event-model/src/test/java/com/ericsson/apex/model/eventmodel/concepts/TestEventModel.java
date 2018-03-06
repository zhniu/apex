/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestEventModel {

    @Test
    public void testEventModel() {
        assertNotNull(new AxEventModel());
        assertNotNull(new AxEventModel(new AxArtifactKey()));
        assertNotNull(new AxEventModel(new AxArtifactKey(), new AxContextSchemas(), new AxKeyInformation(), new AxEvents()));
        
        AxArtifactKey modelKey = new AxArtifactKey("ModelKey", "0.0.1");
        AxArtifactKey schemasKey = new AxArtifactKey("SchemasKey", "0.0.1");
        AxArtifactKey eventsKey = new AxArtifactKey("EventsKey", "0.0.1");
        AxArtifactKey keyInfoKey = new AxArtifactKey("SchemasKey", "0.0.1");
        AxEventModel model = new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(keyInfoKey), new AxEvents(eventsKey));
        model.register();
        
        model.clean();
        assertNotNull(model);
        assertEquals("AxEventModel:(AxEventModel:(key=AxArtifactKey:(nam", model.toString().substring(0, 50));
        
        AxEventModel clonedModel = new AxEventModel(model);

        assertFalse(model.hashCode() == 0);

        assertTrue(model.equals(model));
        assertTrue(model.equals(clonedModel));
        assertFalse(model.equals("Hello"));
        assertFalse(model.equals(new AxEventModel(new AxArtifactKey())));
        assertFalse(model.equals(new AxEventModel(modelKey, new AxContextSchemas(), new AxKeyInformation(keyInfoKey), new AxEvents(eventsKey))));
        assertFalse(model.equals(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(), new AxEvents(eventsKey))));
        assertFalse(model.equals(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(keyInfoKey), new AxEvents())));
        assertTrue(model.equals(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(keyInfoKey), new AxEvents(eventsKey))));

        assertEquals(0, model.compareTo(model));
        assertEquals(0, model.compareTo(clonedModel));
        assertNotEquals(0, model.compareTo(new AxArtifactKey()));
        assertNotEquals(0, model.compareTo(new AxEventModel(modelKey, new AxContextSchemas(), new AxKeyInformation(keyInfoKey), new AxEvents(eventsKey))));
        assertNotEquals(0, model.compareTo(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(), new AxEvents(eventsKey))));
        assertNotEquals(0, model.compareTo(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(keyInfoKey), new AxEvents())));
        assertEquals(0, model.compareTo(new AxEventModel(modelKey, new AxContextSchemas(schemasKey), new AxKeyInformation(keyInfoKey), new AxEvents(eventsKey))));
    }
}
