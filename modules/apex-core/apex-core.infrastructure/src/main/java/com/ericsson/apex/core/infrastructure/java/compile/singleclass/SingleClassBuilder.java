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

import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.java.JavaHandlingException;

/**
 * The Class SingleClassBuilder is used to compile the Java code for a Java object and to create an instance of the object.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SingleClassBuilder {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SingleClassBuilder.class);

    // The class name and source code for the class that we are compiling and instantiating
    private final String className;
    private final String sourceCode;

    // This specialized JavaFileManager handles class loading for the single Java class
    private SingleFileManager singleFileManager = null;

    /**
     * Instantiates a new single class builder.
     *
     * @param className the class name
     * @param sourceCode the source code
     */
    public SingleClassBuilder(final String className, final String sourceCode) {
        // Save the fields of the class
        this.className = className;
        this.sourceCode = sourceCode;
    }

    /**
     * Compile the single class into byte code.
     *
     * @throws JavaHandlingException Thrown on compilation errors or handling errors on the single Java class
     */
    public void compile() throws JavaHandlingException {
        // Get the list of compilation units, there is only one here
        final List<? extends JavaFileObject> compilationUnits = Arrays.asList(new SingleClassCompilationUnit(className, sourceCode));

        // Allows us to get diagnostics from the compilation
        final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<>();

        // Get the Java compiler
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // Set up the target file manager and call the compiler
        singleFileManager = new SingleFileManager(compiler, new SingleClassByteCodeFileObject(className));
        final JavaCompiler.CompilationTask task = compiler.getTask(null, singleFileManager, diagnosticListener, null, null, compilationUnits);

        // Check if the compilation worked
        if (!task.call()) {
            final StringBuilder builder = new StringBuilder();
            for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListener.getDiagnostics()) {
                builder.append("code:");
                builder.append(diagnostic.getCode());
                builder.append(", kind:");
                builder.append(diagnostic.getKind());
                builder.append(", position:");
                builder.append(diagnostic.getPosition());
                builder.append(", start position:");
                builder.append(diagnostic.getStartPosition());
                builder.append(", end position:");
                builder.append(diagnostic.getEndPosition());
                builder.append(", source:");
                builder.append(diagnostic.getSource());
                builder.append(", message:");
                builder.append(diagnostic.getMessage(null));
                builder.append("\n");
            }

            LOGGER.warn("error compiling Java code for class \"" + className + "\": " + builder.toString());
            throw new JavaHandlingException("error compiling Java code for class \"" + className + "\": " + builder.toString());
        }
    }

    /**
     * Create a new instance of the Java class using its byte code definition.
     *
     * @return A new instance of the object
     * @throws InstantiationException if an instance of the object cannot be created, for example if the class has no default constructor
     * @throws IllegalAccessException the caller does not have permission to call the class
     * @throws ClassNotFoundException the byte code for the class is not found in the class loader
     * @throws JavaHandlingException the java handling exception if the Java class source code is not compiled
     */
    public Object createObject() throws InstantiationException, IllegalAccessException, ClassNotFoundException, JavaHandlingException {
        if (singleFileManager == null) {
            LOGGER.warn("error instantiating instance for class \"" + className + "\": code may not be compiled");
            throw new JavaHandlingException("error instantiating instance for class \"" + className + "\": code may not be compiled");
        }

        return singleFileManager.getClassLoader(null).findClass(className).newInstance();
    }
}
