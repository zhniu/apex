/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * The Class TestCLIEditorScripting.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestCLIEditorScripting {

    private File tempModelFile;
    private File tempLogFile;

    private String[] sampleLBPolicyArgs;

    private String[] sampleLBPolicyMapArgs;

    /**
     * Initialise args.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Before
    public void initialiseArgs() throws IOException {
        tempModelFile = File.createTempFile("SampleLBPolicyMap", ".json");
        tempLogFile   = File.createTempFile("SampleLBPolicyMap", ".log");

        sampleLBPolicyArgs = new String[] {
                "-c",
                "src/test/resources/scripts/SampleLBPolicy.apex",
                "-o",
                tempModelFile.getAbsolutePath(),
                "-l",
                tempLogFile.getAbsolutePath()
        };

        sampleLBPolicyMapArgs = new String[] {
                "-c",
                "src/test/resources/scripts/SampleLBPolicy_WithMap.apex",
                "-o",
                tempModelFile.getAbsolutePath(),
                "-l",
                tempLogFile.getAbsolutePath()
        };
    }

    /**
     * Removes the generated files.
     */
    @After
    public void removeGeneratedFiles() {
        tempModelFile.delete();
        tempLogFile.delete();
    }

    /**
     * Test sample Fuzzy LB policy script.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if there is an Apex error
     */
    @Test
    public void testSampleLBPolicyScript() throws IOException, ApexModelException {
        new ApexCLIEditorMain(sampleLBPolicyArgs);

        // Read the file from disk
        final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);

        final URL writtenModelURL = ResourceUtils.getLocalFile(tempModelFile.getCanonicalPath());
        final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());

        final URL compareModelURL = ResourceUtils.getLocalFile("src/test/resources/compare/FuzzyPolicyModel_Compare.json");
        final AxPolicyModel compareModel = modelReader.read(compareModelURL.openStream());

        // Ignore key info UUIDs
        writtenModel.getKeyInformation().getKeyInfoMap().clear();
        compareModel.getKeyInformation().getKeyInfoMap().clear();

        assertTrue(writtenModel.equals(compareModel));
    }

    /**
     * Test sample Fuzzy LB map policy script.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if there is an Apex error
     */
    @Test
    public void testSampleLBMapPolicyScript() throws IOException, ApexModelException {
        tempModelFile.delete();

        new ApexCLIEditorMain(sampleLBPolicyMapArgs);

        assertTrue(tempModelFile.isFile());

        // Read the file from disk
        final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);

        final URL writtenModelURL = ResourceUtils.getLocalFile(tempModelFile.getCanonicalPath());
        final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());

        final AxValidationResult validationResult = new AxValidationResult();
        writtenModel.validate(validationResult);
        assertEquals(AxValidationResult.ValidationResult.OBSERVATION, validationResult.getValidationResult());
    }
}
