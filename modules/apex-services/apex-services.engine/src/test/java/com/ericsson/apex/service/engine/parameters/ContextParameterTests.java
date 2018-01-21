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
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperDistributorParameters;
import com.ericsson.apex.service.parameters.ApexParameterException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;

/**
 * Test for an empty parameter file
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ContextParameterTests {

    @Test
    public void noParamsTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextNoParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextNoParams.json\"\n"
                    + "(ApexParameterRuntimeException):could not find field \"parameterClassName\" in \"contextParameters\" entry", e.getMessage());
        }
    }

    @Test
    public void badParamsTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadParams.json\"\n"
                    + "(ApexParameterRuntimeException):failed to deserialize the parameters for \"contextParameters\" to parameter class \"hello\"\n"
                    + "java.lang.ClassNotFoundException: hello", e.getMessage());
        }
    }

    @Test
    public void badPluginParamNameTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadPluginNameParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadPluginNameParams.json\"\n"
                    + "(ApexParameterRuntimeException):could not find field \"parameterClassName\" in \"contextParameters\" entry", e.getMessage());
        }
    }

    @Test
    public void badClassParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadClassParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals(
                    "error reading parameters from \"src/test/resources/parameters/serviceContextBadClassParams.json\"\n"
                            + "(ApexParameterRuntimeException):failed to deserialize the parameters for \"contextParameters\" to parameter class \"java.lang.Integer\"\n"
                            + "com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected NUMBER but was BEGIN_OBJECT at path $",
                    e.getMessage());
        }
    }

    @Test
    public void badPluginClassTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadPluginClassParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadPluginClassParams.json\"\n"
                    + "(ClassCastException):com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperExecutorParameters cannot be cast to com.ericsson.apex.context.parameters.ContextParameters",
                    e.getMessage());
        }
    }

    @Test
    public void okFlushParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextOKFlushParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals("com.ericsson.apex.context.parameters.ContextParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getParameterClassName());
            assertEquals(123456,
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getFlushPeriod());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw any exception: "+e.getMessage());
        }
    }

    @Test
    public void okDefaultParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextOKDefaultParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals("com.ericsson.apex.context.parameters.ContextParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getParameterClassName());
            assertEquals(300000,
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getFlushPeriod());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw any exception: "+e.getMessage());
        }
    }

    @Test
    public void okDistParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextOKDistParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals("com.ericsson.apex.context.parameters.ContextParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.DistributorParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getDistributorParameters().getParameterClassName());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw any exception: "+e.getMessage());
        }
    }

    @Test
    public void okFullDefaultParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/goodParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals("com.ericsson.apex.context.parameters.ContextParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.DistributorParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getDistributorParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.LockManagerParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getLockManagerParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.PersistorParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getParameterClassName());
            assertEquals(300000,
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getFlushPeriod());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw any exception: "+e.getMessage());
        }
    }

    @Test
    public void okFullParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextOKFullParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals("com.ericsson.apex.context.parameters.ContextParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.LockManagerParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getLockManagerParameters().getParameterClassName());
            assertEquals("com.ericsson.apex.context.parameters.PersistorParameters",
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getParameterClassName());
            assertEquals(123456,
                    parameters.getEngineServiceParameters().getEngineParameters().getContextParameters().getPersistorParameters().getFlushPeriod());

            SuperDooperDistributorParameters infinispanParameters = (SuperDooperDistributorParameters) parameters.getEngineServiceParameters()
                    .getEngineParameters().getContextParameters().getDistributorParameters();
            assertEquals("com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperDistributorParameters",
                    infinispanParameters.getParameterClassName());
            assertEquals("my/lovely/configFile.xml", infinispanParameters.getConfigFile());
            assertEquals("holy/stone.xml", infinispanParameters.getJgroupsFile());
            assertEquals(false, infinispanParameters.preferIPv4Stack());
            assertEquals("fatherted", infinispanParameters.getjGroupsBindAddress());

        }
        catch (ApexParameterException e) {
            fail("This test should not throw any exception: "+e.getMessage());
        }
    }

    @Test
    public void badClassDistParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadClassDistParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadClassDistParams.json\"\n"
                    + "(ClassCastException):com.ericsson.apex.context.parameters.ContextParameters cannot be cast to com.ericsson.apex.context.parameters.DistributorParameters",
                    e.getMessage());
        }
    }

    @Test
    public void badClassLockParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadClassLockParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadClassLockParams.json\"\n"
                    + "(ClassCastException):com.ericsson.apex.context.parameters.ContextParameters cannot be cast to com.ericsson.apex.context.parameters.LockManagerParameters",
                    e.getMessage());
        }
    }

    @Test
    public void badClassPersistParamTest() {
        String[] args = { "-c", "src/test/resources/parameters/serviceContextBadClassPersistParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("error reading parameters from \"src/test/resources/parameters/serviceContextBadClassPersistParams.json\"\n"
                    + "(ClassCastException):com.ericsson.apex.context.parameters.ContextParameters cannot be cast to com.ericsson.apex.context.parameters.PersistorParameters",
                    e.getMessage());
        }
    }
}
