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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestStateOutput {

    @Test
    public void testStateOutput() {
        assertNotNull(new AxStateOutput());
        assertNotNull(new AxStateOutput(new AxReferenceKey()));
        assertNotNull(new AxStateOutput(new AxReferenceKey(), new AxReferenceKey(), new AxArtifactKey()));
        assertNotNull(new AxStateOutput(new AxReferenceKey(), new AxArtifactKey(), new AxReferenceKey()));

        AxStateOutput so = new AxStateOutput();

        AxReferenceKey soKey = new AxReferenceKey("SOStateParent", "0.0.1", "SOState", "SOName");
        AxReferenceKey nsKey = new AxReferenceKey("SOStateParent", "0.0.1", "NotUsed", "NextStateName");
        AxArtifactKey  eKey = new AxArtifactKey("EventName", "0.0.1");
        
        try {
            so.setKey(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("key may not be null", e.getMessage());
        }

        so.setKey(soKey);
        assertEquals("SOStateParent:0.0.1:SOState:SOName", so.getKey().getID());
        assertEquals("SOStateParent:0.0.1:SOState:SOName", so.getKeys().get(0).getID());

        try {
            so.setNextState(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("nextState may not be null", e.getMessage());
        }

        so.setNextState(nsKey);
        assertEquals(nsKey, so.getNextState());

        try {
            so.setOutgoingEvent(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("outgoingEvent may not be null", e.getMessage());
        }
        
        so.setOutgoingEvent(eKey);
        assertEquals(eKey, so.getOutgingEvent());

        AxValidationResult result = new AxValidationResult();
        result = so.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());

        so.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = so.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        so.setKey(soKey);
        result = new AxValidationResult();
        result = so.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        so.setOutgoingEvent(AxArtifactKey.getNullKey());
        result = new AxValidationResult();
        result = so.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        so.setOutgoingEvent(eKey);
        result = new AxValidationResult();
        result = so.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        so.clean();

        AxStateOutput clonedPar = new AxStateOutput(so);
        assertEquals("AxStateOutput:(stateKey=AxReferenceKey:(parentKeyN", clonedPar.toString().substring(0, 50));

        assertFalse(so.hashCode() == 0);

        assertTrue(so.equals(so));
        assertTrue(so.equals(clonedPar));
        assertFalse(so.equals(null));
        assertFalse(so.equals("Hello"));
        assertFalse(so.equals(new AxStateOutput(AxReferenceKey.getNullKey(), eKey, nsKey)));
        assertFalse(so.equals(new AxStateOutput(soKey, new AxArtifactKey(), nsKey)));
        assertFalse(so.equals(new AxStateOutput(soKey, eKey, new AxReferenceKey())));
        assertTrue(so.equals(new AxStateOutput(soKey, eKey, nsKey)));

        assertEquals(0, so.compareTo(so));
        assertEquals(0, so.compareTo(clonedPar));
        assertNotEquals(0, so.compareTo(new AxArtifactKey()));
        assertNotEquals(0, so.compareTo(null));
        assertNotEquals(0, so.compareTo(new AxStateOutput(AxReferenceKey.getNullKey(), eKey, nsKey)));
        assertNotEquals(0, so.compareTo(new AxStateOutput(soKey, new AxArtifactKey(), nsKey)));
        assertNotEquals(0, so.compareTo(new AxStateOutput(soKey, eKey, new AxReferenceKey())));
        assertEquals(0, so.compareTo(new AxStateOutput(soKey, eKey, nsKey)));

        assertNotNull(so.getKeys());
    }
}
