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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.ApexFileEventConsumer;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.producer.ApexFileEventProducer;
import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperCarrierTechnologyParameters;
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperEventProducer;
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperEventSubscriber;
import com.ericsson.apex.service.parameters.ApexParameterException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * Test for an empty parameter file
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ParameterTests {
    @Test
    public void invalidParametersNoFileTest() throws ApexParameterException {
        String[] args = {"-c", "src/test/resources/parameters/invalidNoFile.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertTrue(e.getMessage().startsWith("error reading parameters from \"src"));
            assertTrue(e.getMessage().contains("FileNotFoundException"));
        }
    }

    @Test
    public void invalidParametersEmptyTest() {
        String[] args = {"-c", "src/test/resources/parameters/empty.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertTrue(e.getMessage().startsWith("validation error(s) on parameters from \"src/test/resources/parameters/empty.json\""));
        }
    }

    @Test
    public void invalidParametersNoParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/noParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/noParams.json\"\n" +
                    "Apex parameters invalid\n" +
                    " engine service parameters are not specified\n" +
                    " at least one event output and one event input must be specified",
                    e.getMessage());
        }
    }

    @Test
    public void invalidParametersBlankParamsTest() {
        String[] args = {"-c", "src/test/resources/parameters/blankParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/blankParams.json\"\n" +
                    "Apex parameters invalid\n" +
                    " engine service parameters invalid\n" +
                    "  id not specified or specified value [-1] invalid, must be specified as id >= 0\n" +
                    " at least one event output and one event input must be specified",
                    e.getMessage());
        }
    }

    @Test
    public void invalidParametersTest() {
        String[] args = {"-c", "src/test/resources/parameters/badParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/badParams.json\"\n" +
                        "Apex parameters invalid\n" +
                        " engine service parameters invalid\n" +
                        "  name [hello there] and/or version [PA1] invalid\n" +
                        "   parameter \"name\": value \"hello there\", does not match regular expression \"[A-Za-z0-9\\-_\\.]+\"\n" +
                        "  id not specified or specified value [-45] invalid, must be specified as id >= 0\n" +
                        "  instanceCount [-345] invalid, must be specified as instanceCount >= 1\n" +
                        "  deploymentPort [65536] invalid, must be specified as 1024 <= port <= 65535\n" +
                        "  policyModelFileName [/some/file/name.xml] not found or is not a plain file\n" +
                        " event input (TheFileConsumer1) parameters invalid\n" +
                        "  fileName not specified or is blank or null, it must be specified as a valid file location\n" +
                        " event output (FirstProducer) parameters invalid\n" +
                        "  fileName not specified or is blank or null, it must be specified as a valid file location",
                        e.getMessage());
        }
    }

    @Test
    public void goodParametersTest() {
        String[] args = {"-c", "src/test/resources/parameters/goodParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

            assertEquals(2, parameters.getEventInputParameters().size());
            assertEquals(2, parameters.getEventOutputParameters().size());

            assertTrue(parameters.getEventOutputParameters().containsKey("FirstProducer"));
            assertTrue(parameters.getEventOutputParameters().containsKey("MyOtherProducer"));
            assertEquals("FILE",          parameters.getEventOutputParameters().get("FirstProducer").getCarrierTechnologyParameters().getLabel());
            assertEquals("FILE",          parameters.getEventOutputParameters().get("MyOtherProducer").getCarrierTechnologyParameters().getLabel());
            assertEquals(ApexFileEventProducer.class.getCanonicalName(), parameters.getEventOutputParameters().get("MyOtherProducer").getCarrierTechnologyParameters().getEventProducerPluginClass());
            assertEquals(ApexFileEventConsumer.class.getCanonicalName(), parameters.getEventOutputParameters().get("MyOtherProducer").getCarrierTechnologyParameters().getEventConsumerPluginClass());
            assertEquals("JSON",          parameters.getEventOutputParameters().get("FirstProducer").getEventProtocolParameters().getLabel());
            assertEquals("JSON",          parameters.getEventOutputParameters().get("MyOtherProducer").getEventProtocolParameters().getLabel());

            assertTrue(parameters.getEventInputParameters().containsKey("TheFileConsumer1"));
            assertTrue(parameters.getEventInputParameters().containsKey("MySuperDooperConsumer1"));
            assertEquals("FILE",          parameters.getEventInputParameters().get("TheFileConsumer1").getCarrierTechnologyParameters().getLabel());
            assertEquals("SUPER_DOOPER",  parameters.getEventInputParameters().get("MySuperDooperConsumer1").getCarrierTechnologyParameters().getLabel());
            assertEquals("JSON",          parameters.getEventInputParameters().get("TheFileConsumer1").getEventProtocolParameters().getLabel());
            assertEquals("SUPER_TOK_DEL", parameters.getEventInputParameters().get("MySuperDooperConsumer1").getEventProtocolParameters().getLabel());
            assertEquals(ApexFileEventProducer.class.getCanonicalName(), parameters.getEventInputParameters().get("TheFileConsumer1").getCarrierTechnologyParameters().getEventProducerPluginClass());
            assertEquals(ApexFileEventConsumer.class.getCanonicalName(), parameters.getEventInputParameters().get("TheFileConsumer1").getCarrierTechnologyParameters().getEventConsumerPluginClass());
            assertEquals(SuperDooperEventProducer.class.getCanonicalName(),    parameters.getEventInputParameters().get("MySuperDooperConsumer1").getCarrierTechnologyParameters().getEventProducerPluginClass());
            assertEquals(SuperDooperEventSubscriber.class.getCanonicalName(),  parameters.getEventInputParameters().get("MySuperDooperConsumer1").getCarrierTechnologyParameters().getEventConsumerPluginClass());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw an exception");
        }
    }

    @Test
    public void superDooperParametersTest() {
        String[] args = {"-c", "src/test/resources/parameters/superDooperParams.json"};
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

            assertEquals("MyApexEngine", parameters.getEngineServiceParameters().getName());
            assertEquals("0.0.1",        parameters.getEngineServiceParameters().getVersion());
            assertEquals(45,             parameters.getEngineServiceParameters().getId());
            assertEquals(345,            parameters.getEngineServiceParameters().getInstanceCount());
            assertEquals(65522,          parameters.getEngineServiceParameters().getDeploymentPort());
            
            CarrierTechnologyParameters prodCT = parameters.getEventOutputParameters().get("FirstProducer").getCarrierTechnologyParameters();
            EventProtocolParameters     prodEP = parameters.getEventOutputParameters().get("FirstProducer").getEventProtocolParameters();
            CarrierTechnologyParameters consCT = parameters.getEventInputParameters().get("MySuperDooperConsumer1").getCarrierTechnologyParameters();
            EventProtocolParameters     consEP = parameters.getEventInputParameters().get("MySuperDooperConsumer1").getEventProtocolParameters();

            assertEquals("SUPER_DOOPER",  prodCT.getLabel());
            assertEquals("SUPER_TOK_DEL", prodEP.getLabel());
            assertEquals("SUPER_DOOPER",  consCT.getLabel());
            assertEquals("JSON",          consEP.getLabel());

            assertTrue(prodCT instanceof SuperDooperCarrierTechnologyParameters);

            SuperDooperCarrierTechnologyParameters superDooperParameters = (SuperDooperCarrierTechnologyParameters) prodCT;
            assertEquals("somehost:12345",          superDooperParameters.getBootstrapServers());
            assertEquals("0",                       superDooperParameters.getAcks());
            assertEquals(25,                        superDooperParameters.getRetries());
            assertEquals(98765,                     superDooperParameters.getBatchSize());
            assertEquals(21,                        superDooperParameters.getLingerTime());
            assertEquals(50505050,                  superDooperParameters.getBufferMemory());
            assertEquals("first-group-id",          superDooperParameters.getGroupId());
            assertFalse(                            superDooperParameters.isEnableAutoCommit());
            assertEquals(441,                       superDooperParameters.getAutoCommitTime());
            assertEquals(987,                       superDooperParameters.getSessionTimeout());
            assertEquals("producer-out",            superDooperParameters.getProducerTopic());
            assertEquals(101,                       superDooperParameters.getConsumerPollTime());
            assertEquals("some.key.serailizer",     superDooperParameters.getKeySerializer());
            assertEquals("some.value.serailizer",   superDooperParameters.getValueSerializer());
            assertEquals("some.key.deserailizer",   superDooperParameters.getKeyDeserializer());
            assertEquals("some.value.deserailizer", superDooperParameters.getValueDeserializer());

            String[] consumerTopics = {"consumer-out-0", "consumer-out-1", "consumer-out-2"};
            assertEquals(Arrays.asList(consumerTopics), superDooperParameters.getConsumerTopicList());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw an exception");
        }
    }
}
