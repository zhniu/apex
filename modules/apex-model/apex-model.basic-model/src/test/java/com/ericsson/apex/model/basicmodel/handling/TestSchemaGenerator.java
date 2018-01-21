/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestSchemaGenerator {

    @Test
    public void test() throws IOException {
        ByteArrayOutputStream baos0 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos0));
        
        String[] args0 = {};
        ApexSchemaGenerator.main(args0);
        assertTrue(baos0.toString().contains("usage: ApexSchemaGenerator apex-root-class [schema-file-name]"));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos1));
        
        String[] args1 = {"hello", "goodbye", "here"};
        ApexSchemaGenerator.main(args1);
        assertTrue(baos1.toString().contains("usage: ApexSchemaGenerator apex-root-class [schema-file-name]"));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos2));
        
        String[] args2 = {"hello", "goodbye"};
        ApexSchemaGenerator.main(args2);
        assertTrue(baos2.toString().contains("error on Apex schema output"));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos3));
        
        String[] args3 = {"hello"};
        ApexSchemaGenerator.main(args3);
        assertTrue(baos3.toString().contains("could not create JAXB context, root class hello not found"));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        ByteArrayOutputStream baos4 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos4));
        
        String[] args4 = {"com.ericsson.apex.model.basicmodel.concepts.AxModel"};
        ApexSchemaGenerator.main(args4);
        assertTrue(baos4.toString().contains("targetNamespace=\"http://www.ericsson.com/apex\""));
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        ByteArrayOutputStream baos5 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos5));
        
        File tempFile = File.createTempFile("ApexSchemaGeneratorTest", "xsd");
        String[] args5 = {"com.ericsson.apex.model.basicmodel.concepts.AxModel", tempFile.getCanonicalPath()};
        
        ApexSchemaGenerator.main(args5);
        assertTrue(tempFile.length() > 100);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        tempFile.delete();
    }
}
