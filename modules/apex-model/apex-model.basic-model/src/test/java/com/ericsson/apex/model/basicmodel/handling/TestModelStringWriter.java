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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestModelStringWriter {

    @Test
    public void testModelStringWriter() throws IOException, ApexException {
        AxModel basicModel = new TestApexBasicModelCreator().getModel();
        assertNotNull(basicModel);
       
        AxKeyInfo intKI   = basicModel.getKeyInformation().get("IntegerKIKey");
        AxKeyInfo floatKI = basicModel.getKeyInformation().get("FloatKIKey");

        // Ensure marshalling is OK
        ApexModelStringWriter<AxKeyInfo> stringWriter = new ApexModelStringWriter<AxKeyInfo>(true);
        
        assertNotNull(stringWriter.writeJSONString(intKI,   AxKeyInfo.class));
        assertNotNull(stringWriter.writeJSONString(floatKI, AxKeyInfo.class));
       
        assertNotNull(stringWriter.writeString(intKI,   AxKeyInfo.class, true));
        assertNotNull(stringWriter.writeString(floatKI, AxKeyInfo.class, true));
       
        assertNotNull(stringWriter.writeString(intKI,   AxKeyInfo.class, false));
        assertNotNull(stringWriter.writeString(floatKI, AxKeyInfo.class, false));
        
        assertNotNull(stringWriter.writeXMLString(intKI,   AxKeyInfo.class));
        assertNotNull(stringWriter.writeXMLString(floatKI, AxKeyInfo.class));
        
        try {
            stringWriter.writeString(null, AxKeyInfo.class, true);
            fail("test should thrown an exception here");
        }
        catch (Exception e) {
            assertEquals("concept may not be null", e.getMessage());
        }
        
        try {
            stringWriter.writeString(null, AxKeyInfo.class, false);
            fail("test should thrown an exception here");
        }
        catch (Exception e) {
            assertEquals("concept may not be null", e.getMessage());
        }
        
        try {
            stringWriter.writeJSONString(null, AxKeyInfo.class);
            fail("test should thrown an exception here");
        }
        catch (Exception e) {
            assertEquals("error writing JSON string", e.getMessage());
        }
        
        try {
            stringWriter.writeXMLString(null, AxKeyInfo.class);
            fail("test should thrown an exception here");
        }
        catch (Exception e) {
            assertEquals("error writing XML string", e.getMessage());
        }
        
        stringWriter.setValidateFlag(true);
        assertTrue(stringWriter.isValidateFlag());
    }
}
