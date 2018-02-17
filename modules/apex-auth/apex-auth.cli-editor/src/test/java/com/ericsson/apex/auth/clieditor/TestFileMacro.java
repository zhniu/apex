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
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * Test FileMacro in the CLI.
 */
public class TestFileMacro {
	private String[] fileMacroArgs;

	private File tempModelFile;
	private File tempLogFile;

	@Before
	public void createTempFiles() throws IOException {
		tempModelFile = File.createTempFile("TestPolicyModel", ".json");
		tempLogFile   = File.createTempFile("TestPolicyModel", ".log");

		fileMacroArgs = new String[] {
				"-c",
				"src/test/resources/scripts/FileMacro.apex",
				"-l",
				tempLogFile.getCanonicalPath(),
				"-o",
				tempModelFile.getCanonicalPath(),
				"-if",
				"true"
		};
	}

	@After
	public void removeGeneratedModels() {
		tempModelFile.delete();
	}

	/**
	 * Test logic block macro in CLI scripts.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ApexModelException if there is an Apex error
	 */
	@Test
	public void testLogicBlock() throws IOException, ApexModelException {
		ApexCLIEditorMain cliEditor = new ApexCLIEditorMain(fileMacroArgs);
		// We expect eight errors
		assertEquals(8, cliEditor.getErrorCount());

		// Read the file from disk
		final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);
		modelReader.setValidateFlag(false);

		final URL writtenModelURL = ResourceUtils.getLocalFile(tempModelFile.getCanonicalPath());
		final AxPolicyModel writtenModel = modelReader.read(writtenModelURL.openStream());

		final URL compareModelURL = ResourceUtils.getLocalFile("src/test/resources/compare/FileMacroModel_Compare.json");
		final AxPolicyModel compareModel = modelReader.read(compareModelURL.openStream());

		// Ignore key info UUIDs
		writtenModel.getKeyInformation().getKeyInfoMap().clear();
		compareModel.getKeyInformation().getKeyInfoMap().clear();

		assertTrue(writtenModel.equals(compareModel));

		// The output event is in this file
		final File outputLogFile = new File(tempLogFile.getCanonicalPath());

		String tmpPath = Paths.get("").toAbsolutePath().toString() + '/';
		tmpPath = tmpPath.replace("\\", "\\\\");
		final String outputLogString = TextFileUtils
				.getTextFileAsString(outputLogFile.getCanonicalPath())
				.replaceAll(tmpPath, "")
				.replaceAll("\\s+", "");

		// We compare the log to what we expect to get
		final String outputLogCompareString = TextFileUtils
				.getTextFileAsString("src/test/resources/compare/FileMacro_Compare.log")
				.replaceAll("\\s+", "");

		// Check what we got is what we expected to get
		assertEquals(outputLogCompareString, outputLogString);
	}
}
