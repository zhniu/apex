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

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

/**
 * The Class SingleFileManager is a {@link ForwardingJavaFileManager} which in turn implements {@code JavaFileManager}. A {@code JavaFileManager} handles source
 * files for Java language handling tools. A {@link ForwardingJavaFileManager} is an implementation of {@code JavaFileManager} that forwards the
 * {@code JavaFileManager} methods to a given file manager.
 *
 * This class instantiates and forwards those requests to a {@link StandardJavaFileManager} instance to act as a {@code JavaFileManager} for a Java single file,
 * managing class loading for the class.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SingleFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    // THe class loader for our single class
    private final SingleClassLoader singleClassLoader;

    /**
     * Instantiates a new single file manager.
     *
     * @param compiler the compiler we are using
     * @param byteCodeFileObject the byte code for the compiled class
     */
    public SingleFileManager(final JavaCompiler compiler, final SingleClassByteCodeFileObject byteCodeFileObject) {
        super(compiler.getStandardFileManager(null, null, null));
        singleClassLoader = new SingleClassLoader(byteCodeFileObject);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind,
     * javax.tools.FileObject)
     */
    @Override
    public JavaFileObject getJavaFileForOutput(final Location notUsed, final String className, final JavaFileObject.Kind kind, final FileObject sibling)
            throws IOException {
        // Return the JavaFileObject to the compiler so that it can write byte code into it
        return singleClassLoader.getFileObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.tools.ForwardingJavaFileManager#getClassLoader(javax.tools.JavaFileManager.Location)
     */
    @Override
    public SingleClassLoader getClassLoader(final Location location) {
        return singleClassLoader;
    }
}
