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
public class TestLogic {

    @Test
    public void testLogic() {
        DummyLogicReader logicReader = new DummyLogicReader();

        assertNotNull(new AxLogic());
        assertNotNull(new AxLogic(new AxReferenceKey()));
        assertNotNull(new AxLogic(new AxReferenceKey(), "LogicFlavour", "Logic"));
        assertNotNull(new AxLogic(new AxReferenceKey(), "LogicName", "LogicFlavour", "Logic"));
        assertNotNull(new AxLogic(new AxReferenceKey(), "LogicFlavour", logicReader));

        assertNotNull(new AxTaskLogic());
        assertNotNull(new AxTaskLogic(new AxReferenceKey()));
        assertNotNull(new AxTaskLogic(new AxReferenceKey(), "LogicFlavour", "Logic"));
        assertNotNull(new AxTaskLogic(new AxReferenceKey(), "LogicFlavour", logicReader));
        assertNotNull(new AxTaskLogic(new AxLogic()));
        assertNotNull(new AxTaskLogic(new AxArtifactKey(), "LogicName", "LogicFlavour", logicReader));
        assertNotNull(new AxTaskLogic(new AxArtifactKey(), "LogicName", "LogicFlavour", "Logic"));
        assertNotNull(new AxTaskLogic(new AxReferenceKey(), "LogicFlavour", logicReader));

        assertNotNull(new AxTaskSelectionLogic());
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey()));
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey(), "LogicFlavour", "Logic"));
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey(), "LogicName", "LogicFlavour", "Logic"));
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey(), "LogicFlavour", logicReader));
        assertNotNull(new AxTaskSelectionLogic(new AxLogic()));
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey(), "LogicFlavour", logicReader));
        assertNotNull(new AxTaskSelectionLogic(new AxReferenceKey(), "LogicName", "LogicFlavour", logicReader));

        assertNotNull(new AxStateFinalizerLogic());
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey()));
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey(), "LogicFlavour", "Logic"));
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey(), "LogicName", "LogicFlavour", "Logic"));
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey(), "LogicFlavour", logicReader));
        assertNotNull(new AxStateFinalizerLogic(new AxLogic()));
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey(), "LogicFlavour", logicReader));
        assertNotNull(new AxStateFinalizerLogic(new AxReferenceKey(), "LogicName", "LogicFlavour", logicReader));

        AxLogic logic = new AxLogic();

        AxReferenceKey logicKey = new AxReferenceKey("LogicParentName", "0.0.1", "PLN", "LN");
        logic.setKey(logicKey);
        assertEquals("LogicParentName:0.0.1:PLN:LN", logic.getKey().getID());
        assertEquals("LogicParentName:0.0.1:PLN:LN", logic.getKeys().get(0).getID());

        logic.setLogicFlavour("LogicFlavour");
        assertEquals("LogicFlavour", logic.getLogicFlavour());

        logic.setLogic("Logic");
        assertEquals("Logic", logic.getLogic());

        AxValidationResult result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(AxValidationResult.ValidationResult.VALID, result.getValidationResult());

        logic.setKey(AxReferenceKey.getNullKey());
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        logic.setKey(logicKey);
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        try {
            logic.setLogicFlavour(null);
            fail("test shold throw an exception here");
        }
        catch (Exception e) {
            assertEquals("parameter \"logicFlavour\" is null", e.getMessage());
        }

        try {
            logic.setLogicFlavour("");
            fail("test shold throw an exception here");
        }
        catch (Exception e) {
            assertEquals("parameter \"logicFlavour\": value \"\", does not match regular expression \"[A-Za-z0-9\\-_]+\"", e.getMessage());
        }

        logic.setLogicFlavour(AxLogic.LOGIC_FLAVOUR_UNDEFINED);
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        logic.setLogicFlavour("LogicFlavour");
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        try {
            logic.setLogic(null);
            fail("test shold throw an exception here");
        }
        catch (Exception e) {
            assertEquals("logic may not be null", e.getMessage());
        }

        logic.setLogic("");
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());

        logic.setLogic("Logic");
        result = new AxValidationResult();
        result = logic.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());

        logic.clean();

        AxLogic clonedLogic = (AxLogic) logic.clone();
        assertEquals("AxLogic:(key=AxReferenceKey:(parentKeyName=LogicParentName,parentKeyVersion=0.0.1,parentLocalName=PLN,localName=LN),logicFlavour=LogicFlavour,logic=Logic)", clonedLogic.toString());

        assertFalse(logic.hashCode() == 0);

        assertTrue(logic.equals(logic));
        assertTrue(logic.equals(clonedLogic));
        assertFalse(logic.equals(null));
        assertFalse(logic.equals("Hello"));
        assertFalse(logic.equals(new AxLogic(AxReferenceKey.getNullKey(), "LogicFlavour", "Logic")));
        assertFalse(logic.equals(new AxLogic(logicKey, "AnotherLogicFlavour", "Logic")));
        assertFalse(logic.equals(new AxLogic(logicKey, "LogicFlavour", "AnotherLogic")));
        assertTrue(logic.equals(new AxLogic(logicKey, "LogicFlavour", "Logic")));

        assertEquals(0, logic.compareTo(logic));
        assertEquals(0, logic.compareTo(clonedLogic));
        assertNotEquals(0, logic.compareTo(new AxArtifactKey()));
        assertNotEquals(0, logic.compareTo(null));
        assertNotEquals(0, logic.compareTo(new AxLogic(AxReferenceKey.getNullKey(), "LogicFlavour", "Logic")));
        assertNotEquals(0, logic.compareTo(new AxLogic(logicKey, "AnotherLogicFlavour", "Logic")));
        assertNotEquals(0, logic.compareTo(new AxLogic(logicKey, "LogicFlavour", "AnotherLogic")));
        assertEquals(0, logic.compareTo(new AxLogic(logicKey, "LogicFlavour", "Logic")));

        assertNotNull(logic.getKeys());
    }
}
