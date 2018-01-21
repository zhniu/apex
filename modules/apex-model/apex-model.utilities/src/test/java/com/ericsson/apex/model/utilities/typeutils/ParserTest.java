/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities.typeutils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 */
public class ParserTest {
    @Test
    public void testParser() {
        CharStream stream = new ANTLRInputStream("java.util.Map<java.util.List<java.lang.Integer>,java.util.Set<java.lang.String>>");
        TokenStream tokenStream = new CommonTokenStream(new ParametrizedTypeLexer(stream));

        ParametrizedTypeParser parser = new ParametrizedTypeParser(tokenStream);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());
        parser.setBuildParseTree(true);

        assertEquals( new TypeReference<Map<List<Integer>, Set<String>>>() {}.getType(),
                parser.type().value.build()
                );
    }

    @Test
    public void testBuilder() throws IllegalArgumentException {
        String t = "java.util.Map<java.util.List<java.lang.Integer>,java.util.Set<java.lang.String>>";
        Type ret = TypeBuilder.build(t);
        assertEquals(new TypeReference<Map<List<Integer>, Set<String>>>() {}.getType(), ret);
        assertEquals(java.util.Map.class,TypeBuilder.getJavaTypeClass(ret));
        Type[] args = TypeBuilder.getJavaTypeParameters(ret);
        assertArrayEquals(args,new Type[]{
                new TypeReference<List<Integer>>(){}.getType(),
                new TypeReference<Set<String>>(){}.getType()
        });
        t = "java.lang.Integer";
        ret = TypeBuilder.build(t);
        assertEquals(java.lang.Integer.class,TypeBuilder.getJavaTypeClass(ret));

    }

    @Test
    public void testBoundaryConditions() {
        try {
            TypeBuilder.build(null);
            fail("Test should throw exception");
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Blank type string passed to com.ericsson.apex.model.utilities.typeutils.TypeBuilder.build(String type)");
        }

        try {
            TypeBuilder.build("org.zooby.Wooby");
            fail("Test should throw exception");
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Failed to build type 'org.zooby.Wooby': java.lang.IllegalArgumentException: " +
                    "Class 'org.zooby.Wooby' not found. Also looked for a class called 'java.lang.org.zooby.Wooby'");
        }

        assertEquals(TypeBuilder.getJavaTypeClass("java.lang.String"), String.class);
    }
}
