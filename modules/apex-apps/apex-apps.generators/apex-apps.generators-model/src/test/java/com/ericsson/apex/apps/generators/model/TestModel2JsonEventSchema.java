/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.generators.model;

import org.junit.Test;

import com.ericsson.apex.apps.generators.model.model2event.Application;
import com.ericsson.apex.apps.generators.model.model2event.Model2JsonEventSchema;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * Tests for {@link Model2JsonEventSchema}.
 *
 * @author Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */

public class TestModel2JsonEventSchema {

    /**
     * Test help short.
     * @throws ApexException the apex exception
     */
    @Test
    public void testHelpShort() throws ApexException {
        final String[] args = new String[] {"-h"};
        Application.main(args);
    }

    /**
     * Test help long.
     * @throws ApexException the apex exception
     */
    @Test
    public void testHelpLong() throws ApexException {
        final String[] args = new String[] {"--help"};
        Application.main(args);
    }

    /**
     * Test version short.
     * @throws ApexException the apex exception
     */
    @Test
    public void testVersionShort() throws ApexException {
        final String[] args = new String[] {"-v"};
        Application.main(args);
    }

    /**
     * Test version long.
     * @throws ApexException the apex exception
     */
    @Test
    public void testVersionLong() throws ApexException {
        final String[] args = new String[] {"--version"};
        Application.main(args);
    }

    /**
     * Test stimuli.
     */
    @Test
    public void testStimuli() {
        final String[] args = new String[] {"-m", "src/test/resources/models/VPNPolicyModelAvro.json",
                // "-m", "src/test/resources/models/VPNPolicyModelJava.json",
                // "-m", "src/test/resources/models/AADMPolicyModel.json",
                "-t", "stimuli"};
        Application.main(args);
    }

    /**
     * Test response.
     */
    @Test
    public void testResponse() {
        final String[] args = new String[] {"-m", "src/test/resources/models/VPNPolicyModelAvro.json",
                // "-m", "src/test/resources/models/VPNPolicyModelJava.json",
                // "-m", "src/test/resources/models/AADMPolicyModel.json",
                "-t", "response"};
        Application.main(args);
    }

    /**
     * Test internal.
     */
    @Test
    public void testInternal() {
        final String[] args = new String[] {"-m", "src/test/resources/models/VPNPolicyModelAvro.json",
                // "-m", "src/test/resources/models/VPNPolicyModelJava.json",
                // "-m", "src/test/resources/models/AADMPolicyModel.json",
                "-t", "internal"};
        Application.main(args);
    }
}
