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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class TestPolicyAnalyser {
    @Test
    public void test() {
        AxPolicyModel apexModel = new TestApexPolicyModelCreator().getModel();

        PolicyAnalyser policyAnalyser = new PolicyAnalyser();
        PolicyAnalysisResult analysisResult = policyAnalyser.analyse(apexModel);

        assertTrue(analysisResult.toString().equals(EXPECTED_ANALYSIS_RESULT));
        
        assertNotNull(analysisResult.getUsedContextAlbums());
        assertNotNull(analysisResult.getUsedContextSchemas());
        assertNotNull(analysisResult.getUsedEvents());
        assertNotNull(analysisResult.getUsedTasks());
        assertNotNull(analysisResult.getUnusedContextAlbums());
        assertNotNull(analysisResult.getUnusedContextSchemas());
        assertNotNull(analysisResult.getUnusedEvents());
        assertNotNull(analysisResult.getUnusedTasks());
    }

    private static final String EXPECTED_ANALYSIS_RESULT = "" +
            "Context Schema usage\n" +
            " MapType:0.0.1\n" +
            "  contextAlbum0:0.0.1\n" +
            " StringType:0.0.1\n" +
            "  contextAlbum1:0.0.1\n" +
            " eventContextItem0:0.0.1\n" +
            "  inEvent:0.0.1\n" +
            "  outEvent0:0.0.1\n" +
            "  outEvent1:0.0.1\n" +
            "  task:0.0.1\n" +
            " eventContextItem1:0.0.1\n" +
            "  inEvent:0.0.1\n" +
            "  outEvent0:0.0.1\n" +
            "  outEvent1:0.0.1\n" +
            "  task:0.0.1\n" +
            "Context Album usage\n" +
            " contextAlbum0:0.0.1\n" +
            "  task:0.0.1\n" +
            "  policy:0.0.1:NULL:state\n" +
            " contextAlbum1:0.0.1\n" +
            "  task:0.0.1\n" +
            "  policy:0.0.1:NULL:state\n" +
            "Event usage\n" +
            " inEvent:0.0.1\n" +
            "  policy:0.0.1:NULL:state\n" +
            " outEvent0:0.0.1\n" +
            "  policy:0.0.1:NULL:state\n" +
            " outEvent1:0.0.1 (unused)\n" +
            "Task usage\n" +
            " task:0.0.1\n" +
            "  policy:0.0.1:NULL:state\n";
}
