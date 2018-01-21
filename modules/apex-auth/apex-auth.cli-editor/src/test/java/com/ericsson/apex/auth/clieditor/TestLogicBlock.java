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

public class TestLogicBlock {
    private String[] logicBlockArgs;
    private String[] avroSchemaArgs;

    private File tempLogicModelFile;
    private File tempAvroModelFile;

    @Before
    public void createTempFiles() throws IOException {
        tempLogicModelFile = File.createTempFile("TestLogicPolicyModel", ".json");
        tempAvroModelFile  = File.createTempFile("TestAvroPolicyModel", ".json");

        logicBlockArgs = new String[] {
                "-c",
                "src/test/resources/scripts/LogicBlock.apex",
                "-o",
                tempLogicModelFile.getCanonicalPath(),
                "-nl"
        };

        avroSchemaArgs = new String[] {
                "-c",
                "src/test/resources/scripts/AvroSchema.apex",
                "-o",
                tempAvroModelFile.getCanonicalPath(),
                "-nl"
        };
    }

    @After
    public void removeTempFiles() {
        tempLogicModelFile.delete();
        tempAvroModelFile.delete();
    }

    /**
     * Test logic block.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if there is an Apex error
     */
    @Test
    public void testLogicBlock() throws IOException, ApexModelException {
        new ApexCLIEditorMain(logicBlockArgs);

        // Read the file from disk
        final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);
        modelReader.setValidateFlag(false);

        final URL writtenModelURL = ResourceUtils.getLocalFile(tempLogicModelFile.getCanonicalPath());
        final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());

        final URL compareModelURL = ResourceUtils.getLocalFile("src/test/resources/compare/LogicBlockModel_Compare.json");
        final AxPolicyModel compareModel = modelReader.read(compareModelURL.openStream());

        // Ignore key info UUIDs
        writtenModel.getKeyInformation().getKeyInfoMap().clear();
        compareModel.getKeyInformation().getKeyInfoMap().clear();

        assertTrue(writtenModel.equals(compareModel));
    }

    /**
     * Test avro schema.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ApexModelException if there is an Apex error
     */
    @Test
    public void testAvroSchema() throws IOException, ApexModelException {
        new ApexCLIEditorMain(avroSchemaArgs);

        // Read the file from disk
        final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);
        modelReader.setValidateFlag(false);

        final URL writtenModelURL = ResourceUtils.getLocalFile(tempAvroModelFile.getCanonicalPath());
        final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());

        final URL compareModelURL = ResourceUtils.getLocalFile("src/test/resources/compare/AvroSchemaModel_Compare.json");
        final AxPolicyModel compareModel = modelReader.read(compareModelURL.openStream());

        // Ignore key info UUIDs
        writtenModel.getKeyInformation().getKeyInfoMap().clear();
        compareModel.getKeyInformation().getKeyInfoMap().clear();

        assertTrue(writtenModel.equals(compareModel));
    }
}
