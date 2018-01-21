/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestExceptions {

    @Test
    public void test() {
        assertNotNull(new ApexException("Message"));
        assertNotNull(new ApexException("Message", new AxArtifactKey()));
        assertNotNull(new ApexException("Message", new IOException()));
        assertNotNull(new ApexException("Message", new IOException(), new AxArtifactKey()));
        
        AxArtifactKey key = new AxArtifactKey();
        ApexException ae = new ApexException("Message", new IOException("IO exception message"), key);
        assertEquals("Message\ncaused by: Message\ncaused by: IO exception message", ae.getCascadedMessage());
        assertEquals(key, ae.getObject());
        
        assertNotNull(new ApexRuntimeException("Message"));
        assertNotNull(new ApexRuntimeException("Message", new AxArtifactKey()));
        assertNotNull(new ApexRuntimeException("Message", new IOException()));
        assertNotNull(new ApexRuntimeException("Message", new IOException(), new AxArtifactKey()));
        
        AxArtifactKey rKey = new AxArtifactKey();
        ApexRuntimeException re = new ApexRuntimeException("Runtime Message", new IOException("IO runtime exception message"), rKey);
        assertEquals("Runtime Message\ncaused by: Runtime Message\ncaused by: IO runtime exception message", re.getCascadedMessage());
        assertEquals(key, re.getObject());
        
        assertNotNull(new ApexConceptException("Message"));
        assertNotNull(new ApexConceptException("Message", new IOException()));
        
        AxArtifactKey cKey = new AxArtifactKey();
        ApexException ace = new ApexException("Concept Message", new IOException("IO concept exception message"), cKey);
        assertEquals("Concept Message\ncaused by: Concept Message\ncaused by: IO concept exception message", ace.getCascadedMessage());
        assertEquals(cKey, ace.getObject());
    }

}
