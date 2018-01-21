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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class TestPolicyModelSplitter {
    @Test
    public void test() {
        AxPolicyModel apexModel = new TestApexPolicyModelCreator().getModel();

        Set<AxArtifactKey> requiredPolicySet = new TreeSet<AxArtifactKey>();
        requiredPolicySet.add(new AxArtifactKey("policy", "0.0.1"));

        // There's only one policy so a split of this model on that policy should return the same model
        AxPolicyModel splitApexModel = null;
        try {
            splitApexModel = PolicyModelSplitter.getSubPolicyModel(apexModel, requiredPolicySet);
        }
        catch (ApexModelException e) {
            fail(e.getMessage());
        }

        // The only difference between the models should be that the unused event outEvent1 should not be in the split model
        apexModel.getEvents().getEventMap().remove(new AxArtifactKey("outEvent1", "0.0.1"));
        apexModel.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("outEvent1", "0.0.1"));
        assertTrue(apexModel.equals(splitApexModel));

        Set<AxArtifactKey> requiredMissingPolicySet = new TreeSet<AxArtifactKey>();
        requiredPolicySet.add(new AxArtifactKey("MissingPolicy", "0.0.1"));

        AxPolicyModel missingSplitApexModel = null;
        try {
            missingSplitApexModel = PolicyModelSplitter.getSubPolicyModel(apexModel, requiredMissingPolicySet);
        }
        catch (ApexModelException e) {
            fail(e.getMessage());
        }
        assertNotNull(missingSplitApexModel);

        splitApexModel = null;
        try {
            splitApexModel = PolicyModelSplitter.getSubPolicyModel(apexModel, requiredPolicySet, true);
        }
        catch (ApexModelException e) {
            fail(e.getMessage());
        }

        // The only difference between the models should be that the unused event outEvent1 should not be in the split model
        apexModel.getEvents().getEventMap().remove(new AxArtifactKey("outEvent1", "0.0.1"));
        apexModel.getKeyInformation().getKeyInfoMap().remove(new AxArtifactKey("outEvent1", "0.0.1"));
        assertTrue(apexModel.equals(splitApexModel));

        // There's only one policy so a split of this model on that policy should return the same model
        try {
            apexModel.getKey().setName("InvalidPolicyModelName");
            PolicyModelSplitter.getSubPolicyModel(apexModel, requiredPolicySet);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("source model is invalid: \n***validation of model f", e.getMessage().substring(0,  50));
        }
        
    }
}
