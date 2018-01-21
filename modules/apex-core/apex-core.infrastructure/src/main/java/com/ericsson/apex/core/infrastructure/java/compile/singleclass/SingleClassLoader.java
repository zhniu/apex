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

/**
 * The Class SingleClassLoader is responsible for class loading the single Java class being held in memory.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SingleClassLoader extends ClassLoader {
    // The byte code of the class held in memory as byte code in a ByteCodeFileObject
    private final SingleClassByteCodeFileObject byteCodeFileObject;

    /**
     * Instantiates a new single class loader to load the byte code of the class that is being held in memory.
     *
     * @param byteCodeFileObject the byte code of the class
     */
    public SingleClassLoader(final SingleClassByteCodeFileObject byteCodeFileObject) {
        this.byteCodeFileObject = byteCodeFileObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(final String className) throws ClassNotFoundException {
        // Creates a java Class that can be instantiated from the class defined in the byte code in the ByteCodeFileObejct
        return defineClass(className, byteCodeFileObject.getByteCode(), 0, byteCodeFileObject.getByteCode().length);
    }

    /**
     * Gets the file object.
     *
     * @return the file object
     */
    SingleClassByteCodeFileObject getFileObject() {
        return byteCodeFileObject;
    }
}
