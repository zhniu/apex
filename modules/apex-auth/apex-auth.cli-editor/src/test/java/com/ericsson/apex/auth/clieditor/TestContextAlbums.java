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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;

public class TestContextAlbums {
    private String[] logicBlockArgs;

    private File tempModelFile;

    @Before
    public void createTempFiles() throws IOException {
        tempModelFile = File.createTempFile("TestPolicyModel", ".json");

        logicBlockArgs = new String[] {
                "-c",
                "src/test/resources/scripts/ContextAlbums.apex",
                "-o",
                tempModelFile.getAbsolutePath(),
                "-nl"
        };
    }

    /**
     * Removes the generated models.
     */
    @After
    public void removeGeneratedModels() {
        tempModelFile.delete();
    }

    /**
     * Test logic block.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if there is an Apex error
     */
    @Test
    public void testLogicBlock() throws IOException, ApexModelException {
        ApexCLIEditorMain cliEditor = new ApexCLIEditorMain(logicBlockArgs);
        assertEquals(1, cliEditor.getErrorCount());

        // Read the file from disk
        final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);
        modelReader.setValidateFlag(false);

        final URL writtenModelURL = ResourceUtils.getLocalFile(tempModelFile.getCanonicalPath());
        final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());
        assertNotNull(writtenModel);

        final URL compareModelURL = ResourceUtils.getLocalFile("src/test/resources/compare/ContextAlbumsModel_Compare.json");
        final AxPolicyModel compareModel = modelReader.read(compareModelURL.openStream());

        // Ignore key info UUIDs
        writtenModel.getKeyInformation().getKeyInfoMap().clear();
        compareModel.getKeyInformation().getKeyInfoMap().clear();

        assertTrue(writtenModel.equals(compareModel));
    }
}
