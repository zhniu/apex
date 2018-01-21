/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestAxReferenceKeyAdapter {

    @Test
    public void test() throws Exception {
        AxReferenceKeyAdapter arka = new AxReferenceKeyAdapter();
        assertNotNull(arka);
        
        AxReferenceKey rKey = new AxReferenceKey("Name", "0.0.1", "PLN", "LN");
        
        String rKeyString = arka.marshal(rKey);
        assertEquals("LN", rKeyString);
        assertEquals(rKey.getLocalName(),arka.unmarshal(rKeyString).getLocalName());
    }
}
