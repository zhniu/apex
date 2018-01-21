/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

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
        assertNotNull(new PolicyException("Message"));
        assertNotNull(new PolicyException("Message", new IOException()));
        
        PolicyException ae = new PolicyException("Message", new IOException("IO exception message"));
        assertEquals("Message\ncaused by: Message\ncaused by: IO exception message", ae.getCascadedMessage());
        
        assertNotNull(new PolicyRuntimeException("Message"));
        assertNotNull(new PolicyRuntimeException("Message", new IOException()));
        
        PolicyRuntimeException re = new PolicyRuntimeException("Runtime Message", new IOException("IO runtime exception message"));
        assertEquals("Runtime Message\ncaused by: Runtime Message\ncaused by: IO runtime exception message", re.getCascadedMessage());
    }
}
