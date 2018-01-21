/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestExceptions {

    @Test
    public void test() {
        assertNotNull(new ApexEditorException("Message"));
        assertNotNull(new ApexEditorException("Message", "Object of Exception"));
        assertNotNull(new ApexEditorException("Message", new IOException()));
        assertNotNull(new ApexEditorException("Message", new IOException(), "Object of Exception"));
    }
}
