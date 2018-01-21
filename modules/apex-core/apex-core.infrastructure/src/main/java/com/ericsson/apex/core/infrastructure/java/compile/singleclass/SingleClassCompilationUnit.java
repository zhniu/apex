/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.java.compile.singleclass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * The Class SingleClassCompilationUnit is a container for the source code of the single Java class in memory. The class uses a {@link String} to hold the
 * source code.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SingleClassCompilationUnit extends SimpleJavaFileObject {

    private final String source;

    /**
     * Instantiates a new compilation unit.
     *
     * @param className the class name for the source code
     * @param source the source code for the class
     */
    public SingleClassCompilationUnit(final String className, final String source) {
        // Create a URI for the source code of the class
        super(URI.create("file:///" + className + ".java"), Kind.SOURCE);
        this.source = source;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
     */
    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
        // Return the source code to toe caller, the compiler
        return source;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.tools.SimpleJavaFileObject#openOutputStream()
     */
    @Override
    public OutputStream openOutputStream() {
        throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.tools.SimpleJavaFileObject#openInputStream()
     */
    @Override
    public InputStream openInputStream() {
        // Return the source code as a stream
        return new ByteArrayInputStream(source.getBytes());
    }
}
