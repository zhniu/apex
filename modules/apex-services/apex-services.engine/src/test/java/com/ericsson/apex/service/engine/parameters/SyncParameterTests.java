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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperCarrierTechnologyParameters;
import com.ericsson.apex.service.engine.parameters.dummyclasses.SuperTokenDelimitedEventProtocolParameters;
import com.ericsson.apex.service.parameters.ApexParameterException;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.ApexParameters;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * Test for an empty parameter file
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SyncParameterTests {
    @Test
    public void syncBadNoSyncWithPeer() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsNoSyncWithPeer.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsNoSyncWithPeer.json\"\n"
                    + "Apex parameters invalid\n" + " parameter \\\"synchronousPeer\\\" is illegal on non synchronous event output \"SyncProducer0\"",
                    e.getMessage());
        }
    }

    @Test
    public void syncBadNotSyncWithPeer() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsNotSyncWithPeer.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsNotSyncWithPeer.json\"\n"
                    + "Apex parameters invalid\n" + " parameter \\\"synchronousPeer\\\" is illegal on non synchronous event output \"SyncProducer0\"",
                    e.getMessage());
        }
    }

    @Test
    public void syncBadSyncBadPeers() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsBadPeers.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsBadPeers.json\"\n" + "Apex parameters invalid\n"
                    + " specified \"synchronousPeer\" parameter value \"SyncConsumer1\" on event input \"SyncConsumer0\" does not exist or is an invalid peer for this event handler\n"
                    + " specified \"synchronousPeer\" parameter value \"SyncConsumer0\" on event input \"SyncConsumer1\" does not exist or is an invalid peer for this event handler\n"
                    + " specified \"synchronousPeer\" parameter value \"SyncProducer1\" on event output \"SyncProducer0\" does not exist or is an invalid peer for this event handler\n"
                    + " specified \"synchronousPeer\" parameter value \"SyncProducer0\" on event output \"SyncProducer1\" does not exist or is an invalid peer for this event handler",
                    e.getMessage());
        }
    }

    @Test
    public void syncBadSyncInvalidTimeout() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsInvalidTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsInvalidTimeout.json\"\n" + 
                    "Apex parameters invalid\n" + 
                    " parameter \\\"synchronousTimeout\\\" value \"-1\" is illegal on synchronous event input \"SyncConsumer0\", specify a non-negative timeout value in milliseconds\n" + 
                    " parameter \\\"synchronousTimeout\\\" value \"-99999999\" is illegal on synchronous event input \"SyncConsumer1\", specify a non-negative timeout value in milliseconds\n" + 
                    " parameter \\\"synchronousTimeout\\\" value \"-10\" is illegal on synchronous event output \"SyncProducer0\", specify a non-negative timeout value in milliseconds\n" + 
                    " parameter \\\"synchronousTimeout\\\" value \"-3\" is illegal on synchronous event output \"SyncProducer1\", specify a non-negative timeout value in milliseconds\n" + 
                    " synchronous timeout of event input \"SyncConsumer0\" and event output \"SyncProducer0\" [-1/-10] do not match\n" + 
                    " synchronous timeout of event input \"SyncConsumer1\" and event output \"SyncProducer1\" [-99999999/-3] do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncBadSyncBadTimeout() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsBadTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsBadTimeout.json\"\n" + "Apex parameters invalid\n"
                    + " parameter \\\"synchronousTimeout\\\" is illegal on non synchronous event output \"MyOtherProducer\"", e.getMessage());
        }
    }

    @Test
    public void syncBadSyncUnpairedTimeout() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncBadParamsUnpairedTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals("validation error(s) on parameters from \"src/test/resources/parameters/syncBadParamsUnpairedTimeout.json\"\n" + 
                    "Apex parameters invalid\n" + 
                    " synchronous timeout of event input \"SyncConsumer0\" and event output \"SyncProducer0\" [1/10] do not match\n" + 
                    " synchronous timeout of event input \"SyncConsumer1\" and event output \"SyncProducer1\" [99999999/3] do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncGoodSyncGoodTimeoutProducer() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncGoodParamsProducerTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals(12345, parameters.getEventInputParameters() .get("SyncConsumer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventInputParameters() .get("SyncConsumer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(12345, parameters.getEventOutputParameters().get("SyncProducer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventOutputParameters().get("SyncProducer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
        }
        catch (Exception e) {
            fail("This test should not throw an exception");
        }
    }

    @Test
    public void syncGoodSyncGoodTimeoutConsumer() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncGoodParamsConsumerTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals(12345, parameters.getEventInputParameters() .get("SyncConsumer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventInputParameters() .get("SyncConsumer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(12345, parameters.getEventOutputParameters().get("SyncProducer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventOutputParameters().get("SyncProducer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
        }
        catch (Exception e) {
            fail("This test should not throw an exception");
        }
    }

    @Test
    public void syncGoodSyncGoodTimeoutBoth() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncGoodParamsBothTimeout.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);
            assertEquals(12345, parameters.getEventInputParameters() .get("SyncConsumer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventInputParameters() .get("SyncConsumer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(12345, parameters.getEventOutputParameters().get("SyncProducer0").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
            assertEquals(1,     parameters.getEventOutputParameters().get("SyncProducer1").getPeerTimeout(EventHandlerPeeredMode.SYNCHRONOUS));
        }
        catch (Exception e) {
            fail("This test should not throw an exception");
        }
    }

    @Test
    public void syncUnusedConsumerPeers() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncUnusedConsumerPeers.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals(
                    "validation error(s) on parameters from \"src/test/resources/parameters/syncUnusedConsumerPeers.json\"\n" + "Apex parameters invalid\n"
                            + " value of parameter \"synchronousPeer\" on event output \"SyncProducer1\" must be unique, it s used on another event output\n"
                            + "" + " synchronous peers of event input \"SyncConsumer1\" and event output \"SyncProducer1/SyncConsumer0\" do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncMismatchedPeers() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncMismatchedPeers.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals(
                    "validation error(s) on parameters from \"src/test/resources/parameters/syncMismatchedPeers.json\"\n" + "Apex parameters invalid\n"
                            + " synchronous peers of event input \"SyncConsumer0\" and event output \"SyncProducer0/SyncConsumer1\" do not match\n"
                            + " synchronous peers of event input \"SyncConsumer1\" and event output \"SyncProducer1/SyncConsumer0\" do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncUnusedProducerPeers() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncUnusedProducerPeers.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals(
                    "validation error(s) on parameters from \"src/test/resources/parameters/syncUnusedProducerPeers.json\"\n" + "Apex parameters invalid\n"
                            + " value of parameter \"synchronousPeer\" on event input \"SyncConsumer1\" must be unique, it s used on another event input\n"
                            + " synchronous peers of event input \"SyncConsumer0\" and event output \"SyncProducer1/SyncConsumer1\" do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncMismatchedTimeout() throws ApexParameterException {
        String[] args = { "-c", "src/test/resources/parameters/syncUnusedProducerPeers.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            new ApexParameterHandler().getParameters(arguments);
            fail("This test should throw an exception");
        }
        catch (ApexParameterException e) {
            assertEquals(
                    "validation error(s) on parameters from \"src/test/resources/parameters/syncUnusedProducerPeers.json\"\n" + "Apex parameters invalid\n"
                            + " value of parameter \"synchronousPeer\" on event input \"SyncConsumer1\" must be unique, it s used on another event input\n"
                            + " synchronous peers of event input \"SyncConsumer0\" and event output \"SyncProducer1/SyncConsumer1\" do not match",
                    e.getMessage());
        }
    }

    @Test
    public void syncGoodParametersTest() {
        String[] args = { "-c", "src/test/resources/parameters/SyncGoodParams.json" };
        ApexCommandLineArguments arguments = new ApexCommandLineArguments(args);

        try {
            ApexParameters parameters = new ApexParameterHandler().getParameters(arguments);

            assertEquals("MyApexEngine", parameters.getEngineServiceParameters().getName());
            assertEquals("0.0.1", parameters.getEngineServiceParameters().getVersion());
            assertEquals(45, parameters.getEngineServiceParameters().getId());
            assertEquals(19, parameters.getEngineServiceParameters().getInstanceCount());
            assertEquals(65522, parameters.getEngineServiceParameters().getDeploymentPort());

            CarrierTechnologyParameters prodCT0 = parameters.getEventOutputParameters().get("SyncProducer0").getCarrierTechnologyParameters();
            EventProtocolParameters prodEP0 = parameters.getEventOutputParameters().get("SyncProducer0").getEventProtocolParameters();
            CarrierTechnologyParameters consCT0 = parameters.getEventInputParameters().get("SyncConsumer0").getCarrierTechnologyParameters();
            EventProtocolParameters consEP0 = parameters.getEventInputParameters().get("SyncConsumer0").getEventProtocolParameters();
            CarrierTechnologyParameters prodCT1 = parameters.getEventOutputParameters().get("SyncProducer1").getCarrierTechnologyParameters();
            EventProtocolParameters prodEP1 = parameters.getEventOutputParameters().get("SyncProducer1").getEventProtocolParameters();
            CarrierTechnologyParameters consCT1 = parameters.getEventInputParameters().get("SyncConsumer1").getCarrierTechnologyParameters();
            EventProtocolParameters consEP1 = parameters.getEventInputParameters().get("SyncConsumer1").getEventProtocolParameters();

            assertEquals("FILE", prodCT0.getLabel());
            assertEquals("JSON", prodEP0.getLabel());
            assertEquals("FILE", consCT0.getLabel());
            assertEquals("JSON", consEP0.getLabel());
            assertEquals("FILE", prodCT1.getLabel());
            assertEquals("JSON", prodEP1.getLabel());
            assertEquals("SUPER_DOOPER", consCT1.getLabel());
            assertEquals("SUPER_TOK_DEL", consEP1.getLabel());

            assertTrue(consCT1 instanceof SuperDooperCarrierTechnologyParameters);
            assertTrue(consEP1 instanceof SuperTokenDelimitedEventProtocolParameters);

            SuperDooperCarrierTechnologyParameters superDooperParameters = (SuperDooperCarrierTechnologyParameters) consCT1;
            assertEquals("localhost:9092", superDooperParameters.getBootstrapServers());
            assertEquals("all", superDooperParameters.getAcks());
            assertEquals(0, superDooperParameters.getRetries());
            assertEquals(16384, superDooperParameters.getBatchSize());
            assertEquals(1, superDooperParameters.getLingerTime());
            assertEquals(33554432, superDooperParameters.getBufferMemory());
            assertEquals("default-group-id", superDooperParameters.getGroupId());
            assertTrue(superDooperParameters.isEnableAutoCommit());
            assertEquals(1000, superDooperParameters.getAutoCommitTime());
            assertEquals(30000, superDooperParameters.getSessionTimeout());
            assertEquals("apex-out", superDooperParameters.getProducerTopic());
            assertEquals(100, superDooperParameters.getConsumerPollTime());
            assertEquals("org.apache.superDooper.common.serialization.StringSerializer", superDooperParameters.getKeySerializer());
            assertEquals("org.apache.superDooper.common.serialization.StringSerializer", superDooperParameters.getValueSerializer());
            assertEquals("org.apache.superDooper.common.serialization.StringDeserializer", superDooperParameters.getKeyDeserializer());
            assertEquals("org.apache.superDooper.common.serialization.StringDeserializer", superDooperParameters.getValueDeserializer());

            String[] consumerTopics = { "apex-in" };
            assertEquals(Arrays.asList(consumerTopics), superDooperParameters.getConsumerTopicList());
        }
        catch (ApexParameterException e) {
            fail("This test should not throw an exception");
        }
    }
}
