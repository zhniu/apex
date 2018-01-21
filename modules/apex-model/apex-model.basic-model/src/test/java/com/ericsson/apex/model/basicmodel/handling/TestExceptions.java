/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

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
        assertNotNull(new ApexModelException("Message"));
        assertNotNull(new ApexModelException("Message", new IOException()));

        ApexModelException ame = new ApexModelException("Message", new IOException("IO exception message"));
        assertEquals("Message\ncaused by: Message\ncaused by: IO exception message", ame.getCascadedMessage());
    }
}
