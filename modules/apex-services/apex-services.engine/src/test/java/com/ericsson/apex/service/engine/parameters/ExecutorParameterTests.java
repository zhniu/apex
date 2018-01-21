/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.parameters.ApexParameterException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;

/**
 * Test for an empty parameter file
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ExecutorParameterTests {

	@Test
	public void noParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorNoParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);
        
		try {
			ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
			assertEquals(0, parameters.getEngineServiceParameters().getEngineParameters().getExecutorParameterMap().size());
		}
		catch (ApexParameterException e) {
			fail("This test should not throw any exception: "+e.getMessage());
		}
	}

	@Test
	public void badParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorBadParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);
        
        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorBadParams.json\"\n" +
					"(ApexParameterRuntimeException):value of \"executorParameters:ZOOBY\" entry is not a parameter JSON object",
					e.getMessage());
		}
	}

	@Test
	public void noExecutorParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorNoExecutorParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorNoExecutorParams.json\"\n" +
					"(ApexParameterRuntimeException):no \"executorParameters\" entry found in parameters, at least one executor parameter entry must be specified",
					e.getMessage());
		}
	}

	@Test
	public void emptyParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorEmptyParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorEmptyParams.json\"\n" +
					"(ApexParameterRuntimeException):could not find field \"parameterClassName\" in \"executorParameters:ZOOBY\" entry",
					e.getMessage());
		}
	}

	@Test
	public void badPluginParamNameTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorBadPluginNameParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorBadPluginNameParams.json\"\n" +
					"(ApexParameterRuntimeException):could not find field \"parameterClassName\" in \"executorParameters:ZOOBY\" entry",
					e.getMessage());
		}
	}

	@Test
	public void badPluginParamObjectTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorBadPluginValueObjectParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorBadPluginValueObjectParams.json\"\n" +
					"(ApexParameterRuntimeException):value for field \"parameterClassName\" in \"executorParameters:LOOBY\" entry is not a plain string",
					e.getMessage());
		}
	}

	@Test
	public void badPluginParamBlankTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorBadPluginValueBlankParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorBadPluginValueBlankParams.json\"\n" +
					"(ApexParameterRuntimeException):value for field \"parameterClassName\" in \"executorParameters:LOOBY\" entry is not specified or is blank",
					e.getMessage());
		}
	}


	@Test
	public void badPluginParamValueTest() {
        String[] args = {"-c", "src/test/resources/parameters/serviceExecutorBadPluginValueParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			new ApexParameterHandler().getParameters(arguments);
			fail("This test should throw an exception");
		}
		catch (ApexParameterException e) {
			assertEquals("error reading parameters from \"src/test/resources/parameters/serviceExecutorBadPluginValueParams.json\"\n" +
					"(ApexParameterRuntimeException):failed to deserialize the parameters for \"executorParameters:LOOBY\" to parameter class \"helloworld\"\n" +
					"java.lang.ClassNotFoundException: helloworld",
					e.getMessage());
		}
	}

	@Test
	public void goodParametersTest() {
        String[] args = {"-c", "src/test/resources/parameters/goodParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
			ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

			assertEquals("MyApexEngine", parameters.getEngineServiceParameters().getName());
			assertEquals("0.0.1",        parameters.getEngineServiceParameters().getVersion());
			assertEquals(45,             parameters.getEngineServiceParameters().getId());
			assertEquals(19,             parameters.getEngineServiceParameters().getInstanceCount());
			assertEquals(65522,          parameters.getEngineServiceParameters().getDeploymentPort());
		}
		catch (ApexParameterException e) {
			fail("This test should not throw any exception: "+e.getMessage());
		}
	}
}
