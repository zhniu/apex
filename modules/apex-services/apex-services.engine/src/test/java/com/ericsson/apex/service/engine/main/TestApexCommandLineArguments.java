/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexCommandLineArguments {

    @Test
    public void testCommandLineArguments() {
        ApexCommandLineArguments apexArguments = new ApexCommandLineArguments();

        String[] args00 = {""};
        try {
            apexArguments.parse(args00);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("Apex configuration file was not specified as an argument", e.getMessage());
        }

        String[] args01 = {"-h"};
        try {
            String result = apexArguments.parse(args01);
            assertTrue(result.startsWith("usage: com.ericsson.apex.service.engine.main.ApexMain [options...]"));
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }
        
        String[] args02 = {"-v"};
        try {
            String result = apexArguments.parse(args02);
            assertTrue(result.startsWith("Apex Adaptive Policy Engine"));
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }

        String[] args03 = {"-v", "-h"};
        try {
            String result = apexArguments.parse(args03);
            assertTrue(result.startsWith("usage: com.ericsson.apex.service.engine.main.ApexMain [options...]"));
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }

        String[] args04 = {"-h", "-v"};
        try {
            String result = apexArguments.parse(args04);
            assertTrue(result.startsWith("usage: com.ericsson.apex.service.engine.main.ApexMain [options...]"));
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }

        String[] args05 = {"-a"};
        try {
            apexArguments.parse(args05);
        }
        catch (ApexException e) {
            assertEquals("invalid command line arguments specified : Unrecognized option: -a", e.getMessage());
        }

        String[] args06 = {"-c", "hello", "-m", "goodbye", "-h", "-v"};
        try {
            String result = apexArguments.parse(args06);
            assertTrue(result.startsWith("usage: com.ericsson.apex.service.engine.main.ApexMain [options...]"));
        }
        catch (ApexException e) {
            assertEquals("invalid command line arguments specified : Unrecognized option: -a", e.getMessage());
        }

        String[] args07 = {"-c", "hello", "-m", "goodbye", "-h", "aaa"};
        try {
            String result = apexArguments.parse(args07);
            assertTrue(result.startsWith("usage: com.ericsson.apex.service.engine.main.ApexMain [options...]"));
        }
        catch (ApexException e) {
            assertEquals("too many command line arguments specified : [-c, hello, -m, goodbye, -h, aaa]", e.getMessage());
        }
    }
    
    @Test
    public void testCommandLineFileParameters() {
        ApexCommandLineArguments apexArguments = new ApexCommandLineArguments();

        String[] args00 = {"-c", "zooby"};
        try {
            apexArguments.parse(args00);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("Apex configuration file \"zooby\" does not exist", e.getMessage());
        }

        String[] args01 = {"-c"};
        try {
            apexArguments.parse(args01);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("invalid command line arguments specified : Missing argument for option: c", e.getMessage());
        }

        String[] args02 = {"-c", "src/test/resources/parameters/goodParams.json"};
        try {
            apexArguments.parse(args02);
            apexArguments.validate();
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }

        String[] args03 = {"-c", "src/test/resources/parameters/goodParams.json", "-m", "zooby"};
        try {
            apexArguments.parse(args03);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("Apex model file \"zooby\" does not exist", e.getMessage());
        }

        String[] args04 = {"-m"};
        try {
            apexArguments.parse(args04);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("invalid command line arguments specified : Missing argument for option: m", e.getMessage());
        }

        String[] args05 = {"-c", "src/test/resources/parameters/goodParams.json", "-m"};
        try {
            apexArguments.parse(args05);
            apexArguments.validate();
            fail("Test should throw an exception here");
        }
        catch (ApexException e) {
            assertEquals("invalid command line arguments specified : Missing argument for option: m", e.getMessage());
        }

        String[] args06 = {"-c", "src/test/resources/parameters/goodParams.json", "-m", "src/test/resources/main/DummyModelFile.json"};
        try {
            apexArguments.parse(args06);
            apexArguments.validate();
        }
        catch (ApexException e) {
            e.printStackTrace();
            fail("Test should not throw an exception");
        }
        
        String[] args07 = {"-c", "parameters/goodParams.json", "-m", "main/DummyModelFile.json"};
        try {
            apexArguments.parse(args07);
            apexArguments.validate();
        }
        catch (ApexException e) {
            e.printStackTrace();
           fail("Test should not throw an exception");
        }
    }
}
