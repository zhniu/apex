/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestBeans {

    @Test
    public void testBeans() {
        assertNotNull(new BeanEvent().toString());
        assertNotNull(new BeanState().toString());
        assertNotNull(new BeanContextAlbum().toString());
        assertNotNull(new BeanPolicy().toString());
        assertNotNull(new BeanContextSchema().toString());
        assertNotNull(new BeanField().toString());
        assertNotNull(new BeanModel().toString());
        assertNotNull(new BeanLogic().toString());
        assertNotNull(new BeanStateOutput().toString());
        assertNotNull(new BeanTaskParameter().toString());
        assertNotNull(new BeanKeyRef().toString());
        assertNotNull(new BeanStateTaskRef().toString());
        assertNotNull(new BeanTask().toString());
        
        BeanState beanState = new BeanState();
        assertNull(beanState.getName());
        beanState.setDefaultTask(new BeanKeyRef());
        assertNotNull(beanState.getDefaultTask());

        BeanEvent beanEvent = new BeanEvent();
        assertNull(beanEvent.get("name"));
        
        BeanFake beanFake = new BeanFake();
        assertNull(beanFake.get("name"));
        assertNull(beanFake.get("field1"));
        
        try {
            beanFake.get("iDontExist");
            fail("test should throw an exception here");
        }
        catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        try {
            beanFake.get("nome");
            fail("test should throw an exception here");
        }
        catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        try {
            beanFake.get("field2");
            fail("test should throw an exception here");
        }
        catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        try {
            beanFake.get("field3");
            fail("test should throw an exception here");
        }
        catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
}
