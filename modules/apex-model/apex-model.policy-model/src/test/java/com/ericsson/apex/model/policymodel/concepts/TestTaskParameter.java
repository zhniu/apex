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
public class TestTaskParameter {

    @Test
    public void testTaskParameter() {
        assertNotNull(new AxTaskParameter());
        assertNotNull(new AxTaskParameter(new AxReferenceKey()));
        assertNotNull(new AxTaskParameter(new AxReferenceKey(), "DefaultValue"));

        AxTaskParameter par = new AxTaskParameter();

        AxReferenceKey parKey = new AxReferenceKey("ParParentName", "0.0.1", "PLN", "LN");
        par.setKey(parKey);
        assertEquals("ParParentName:0.0.1:PLN:LN", par.getKey().getID());
        assertEquals("ParParentName:0.0.1:PLN:LN", par.getKeys().get(0).getID());

        par.setDefaultValue("DefaultValue");
        assertEquals("DefaultValue", par.getTaskParameterValue());

        AxValidationResult result = new AxValidationResult();
        result = par.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());

        par.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = par.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        par.setKey(parKey);
        result = new AxValidationResult();
        result = par.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        try {
            par.setDefaultValue(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("defaultValue may not be null", e.getMessage());
        }
        
        par.setDefaultValue("");
        result = new AxValidationResult();
        result = par.validate(result);
        assertEquals(ValidationResult.WARNING, result.getValidationResult());

        par.setDefaultValue("DefaultValue");
        result = new AxValidationResult();
        result = par.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        par.clean();

        AxTaskParameter clonedPar = new AxTaskParameter(par);
        assertEquals("AxTaskParameter:(key=AxReferenceKey:(parentKeyName=ParParentName,parentKeyVersion=0.0.1,parentLocalName=PLN,localName=LN),defaultValue=DefaultValue)", clonedPar.toString());

        assertFalse(par.hashCode() == 0);

        assertTrue(par.equals(par));
        assertTrue(par.equals(clonedPar));
        assertFalse(par.equals(null));
        assertFalse(par.equals("Hello"));
        assertFalse(par.equals(new AxTaskParameter(AxReferenceKey.getNullKey(), "DefaultValue")));
        assertFalse(par.equals(new AxTaskParameter(parKey, "OtherDefaultValue")));
        assertTrue(par.equals(new AxTaskParameter(parKey, "DefaultValue")));

        assertEquals(0, par.compareTo(par));
        assertEquals(0, par.compareTo(clonedPar));
        assertNotEquals(0, par.compareTo(new AxArtifactKey()));
        assertNotEquals(0, par.compareTo(null));
        assertNotEquals(0, par.compareTo(new AxTaskParameter(AxReferenceKey.getNullKey(), "DefaultValue")));
        assertNotEquals(0, par.compareTo(new AxTaskParameter(parKey, "OtherDefaultValue")));
        assertEquals(0, par.compareTo(new AxTaskParameter(parKey, "DefaultValue")));

        assertNotNull(par.getKeys());
    }
}
