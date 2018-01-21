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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestPolicyModelMerger {

    @Test
    public void testPolicyModelMerger() {
        AxPolicyModel leftPolicyModel = new TestApexPolicyModelCreator().getModel();
        AxPolicyModel rightPolicyModel = new TestApexPolicyModelCreator().getModel();

        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false);
            assertEquals(leftPolicyModel, mergedPolicyModel);
            assertEquals(rightPolicyModel, mergedPolicyModel);
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }

        leftPolicyModel.setKey(new AxArtifactKey("LeftPolicyModel", "0.0.1"));
        try {
            PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false);
            fail("test should throw an exception here");
        }
        catch (ApexModelException e) {
            assertEquals("left model is invalid: \n***validation of model fai", e.getMessage().substring(0, 50));
        }

        leftPolicyModel.setKey(new AxArtifactKey("LeftPolicyModel", "0.0.1"));
        try {
            assertNotNull(PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false, true));
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }

        leftPolicyModel.getKeyInformation().generateKeyInfo(leftPolicyModel);
        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, true);
            assertNotNull(mergedPolicyModel);
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }

        rightPolicyModel.setKey(new AxArtifactKey("RightPolicyModel", "0.0.1"));
        try {
            PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false);
            fail("test should throw an exception here");
        }
        catch (ApexModelException e) {
            assertEquals("right model is invalid: \n***validation of model fa", e.getMessage().substring(0, 50));
        }

        rightPolicyModel.setKey(new AxArtifactKey("RightPolicyModel", "0.0.1"));
        try {
            assertNotNull(PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false, true));
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }

        rightPolicyModel.getKeyInformation().generateKeyInfo(rightPolicyModel);
        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, false);
            assertNotNull(mergedPolicyModel);
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }

        rightPolicyModel = new TestApexPolicyModelCreator().getAnotherModel();
        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(leftPolicyModel, rightPolicyModel, true);
            assertNotNull(mergedPolicyModel);
        }
        catch (ApexModelException e) {
            fail("test should not throw an exception");
        }
    }
}
