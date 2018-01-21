/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.handling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.policymodel.concepts.AxLogic;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestPolicyLogicReader {

    @Test
    public void test() {
        AxReferenceKey logicKey = new AxReferenceKey("LogicParent", "0.0.1", "LogicInstanceName");
        
        PolicyLogicReader plReader= new PolicyLogicReader();

        plReader.setLogicPackage("somewhere.over.the.rainbow");
        assertEquals("somewhere.over.the.rainbow", plReader.getLogicPackage());

        plReader.setDefaultLogic("FunkyDefaultLogic");
        assertEquals("FunkyDefaultLogic", plReader.getDefaultLogic());

        try {
            new AxLogic(logicKey, "FunkyLogic", plReader);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("logic not found for logic \"somewhere/over/the/rainbow/funkylogic/FunkyDefaultLogic.funkylogic\"", e.getMessage());
        }
        
        plReader.setDefaultLogic(null);
        try {
            new AxLogic(logicKey, "FunkyLogic", plReader);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("logic not found for logic \"somewhere/over/the/rainbow/funkylogic/LogicParent_LogicInstanceName.funkylogic\"", e.getMessage());
        }

        logicKey.setParentLocalName("LogicParentLocalName");
        try {
            new AxLogic(logicKey, "FunkyLogic", plReader);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("logic not found for logic \"somewhere/over/the/rainbow/funkylogic/LogicParent_LogicParentLocalName_LogicInstanceName.funkylogic\"", e.getMessage());
        }
        
        plReader.setLogicPackage("path.to.apex.logic");
        try {
            AxLogic logic = new AxLogic(logicKey, "FunkyLogic", plReader);
            assertEquals("Way out man, this is funky logic!", logic.getLogic());
        }
        catch (Exception e) {
            fail("test should not throw an exception");
        }
        
        plReader.setLogicPackage("somewhere.over.the.rainbow");
        plReader.setDefaultLogic("JavaLogic");

        try {
            AxLogic logic = new AxLogic(logicKey, "JAVA", plReader);
            assertEquals("somewhere.over.the.rainbow.java.JavaLogic", logic.getLogic());
        }
        catch (Exception e) {
            fail("test should not throw an exception");
        }
        
        plReader.setDefaultLogic(null);
        try {
            AxLogic logic = new AxLogic(logicKey, "JAVA", plReader);
            assertEquals("somewhere.over.the.rainbow.java.LogicParent_LogicParentLocalName_LogicInstanceName", logic.getLogic());
        }
        catch (Exception e) {
            fail("test should not throw an exception");
        }
        
        logicKey.setParentLocalName(AxKey.NULL_KEY_NAME);
        try {
            AxLogic logic = new AxLogic(logicKey, "JAVA", plReader);
            assertEquals("somewhere.over.the.rainbow.java.LogicParent_LogicInstanceName", logic.getLogic());
        }
        catch (Exception e) {
            fail("test should not throw an exception");
        }
    }
}
