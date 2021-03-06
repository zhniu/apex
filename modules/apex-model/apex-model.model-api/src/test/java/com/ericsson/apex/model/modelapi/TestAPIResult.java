/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.ericsson.apex.model.modelapi.ApexAPIResult.RESULT;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestAPIResult {

    @Test
    public void testAPIResult() {
        assertNotNull(new ApexAPIResult());
        
        for (RESULT result : RESULT.values()) {
            assertNotNull(new ApexAPIResult(result));
        }
        
        assertNotNull(new ApexAPIResult(RESULT.SUCCESS, "Result Message"));
        assertNotNull(new ApexAPIResult(RESULT.FAILED, new IOException("IO Exception message")));
        assertNotNull(new ApexAPIResult(RESULT.FAILED, "Result Message", new IOException("IO Exception message")));
        
        ApexAPIResult result = new ApexAPIResult(RESULT.FAILED, "Result Message", new IOException("IO Exception message"));
        
        assertFalse(result.isOK());
        assertTrue(result.isNOK());
        assertEquals(RESULT.FAILED, result.getResult());
        assertEquals("Result Message\nIO Exception message\njava.io.IOExce", result.getMessage().substring(0, 50));
        
        ApexAPIResult result2 = new ApexAPIResult(RESULT.SUCCESS);
        result2.addMessage(null);
        assertEquals("", result2.getMessage());
        result2.addMessage("");
        assertEquals("", result2.getMessage());
        result2.addMessage("funky message");
        assertEquals("funky message\n", result2.getMessage());
        
        result2.setResult(RESULT.OTHER_ERROR);
        assertEquals(RESULT.OTHER_ERROR, result2.getResult());
        
        String[] messages = {"First Message", "Second Message", "Third Message"};
        result2.setMessages(Arrays.asList(messages));
        assertEquals("First Message", result2.getMessages().get(0));
        assertEquals("Second Message", result2.getMessages().get(1));
        assertEquals("Third Message", result2.getMessages().get(2));
        
        assertEquals("result: OTHER_ERROR\nFirst Message\nSecond Message\nThird Message\n", result2.toString());
        assertEquals("{\n" + 
                "\"result\": \"OTHER_ERROR\",\n" + 
                "\"messages\": [\n" + 
                "\"First Message\",\n" + 
                "\"Second Message\",\n" + 
                "\"Third Message\"]\n" + 
                "}\n", result2.toJSON());
   }
}
