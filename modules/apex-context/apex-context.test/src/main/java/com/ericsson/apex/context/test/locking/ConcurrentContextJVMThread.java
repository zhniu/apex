/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.locking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.google.gson.Gson;

/**
 * The Class TestConcurrentContextThread tests concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContextJVMThread implements Runnable {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextJVMThread.class);

    private final String testType;
    private final int jvm;
    private final int threadCount;
    private final int target;

    /**
     * The Constructor.
     *
     * @param testType the test type
     * @param jvm the jvm
     * @param threadCount the thread count
     * @param target the target
     * @throws ApexException the apex exception
     */
    public ConcurrentContextJVMThread(final String testType, final int jvm, final int threadCount, final int target) throws ApexException {
        this.testType = testType;
        this.jvm = jvm;
        this.threadCount = threadCount;
        this.target = target;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final List<String> commandList = new ArrayList<>();
        commandList.add(System.getProperty("java.home") + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator") + "java");
        commandList.add("-cp");
        commandList.add(System.getProperty("java.class.path"));
        commandList.add("com.ericsson.apex.context.test.locking.ConcurrentContextJVM");
        commandList.add(testType);
        commandList.add(new Integer(jvm).toString());
        commandList.add(new Integer(threadCount).toString());
        commandList.add(new Integer(target).toString());

        for (final Entry<Class<?>, AbstractParameters> parameterServiceEntry : ParameterService.getAll()) {
            commandList.add(parameterServiceEntry.getKey().getCanonicalName());
            commandList.add(new Gson().toJson(parameterServiceEntry.getValue()));
        }

        LOGGER.info("starting JVM " + jvm);

        // Run the JVM
        final ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        processBuilder.redirectErrorStream(true);
        Process process;

        try {
            process = processBuilder.start();

            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            final BufferedReader br = new BufferedReader(isr);
            String line;
            LOGGER.info("JVM Output for command " + commandList + "\n");
            while ((line = br.readLine()) != null) {
                LOGGER.info(line);
            }

            // Wait to get exit value
            try {
                final int exitValue = process.waitFor();
                LOGGER.info("\n\nJVM " + jvm + " finished, exit value is " + exitValue);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch (final IOException e1) {
            e1.printStackTrace();
        }
    }
}
