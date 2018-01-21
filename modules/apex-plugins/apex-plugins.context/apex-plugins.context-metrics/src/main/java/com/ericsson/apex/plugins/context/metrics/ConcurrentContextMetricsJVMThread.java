/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
 * The Class ConcurrentContextMetricsJVMThread gets metrics for concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContextMetricsJVMThread implements Runnable {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextMetricsJVMThread.class);

    private final String testType;
    private final int jvm;
    private final int threadCount;
    private final int threadLoops;
    private final int longArraySize;
    private final int lockType;

    private boolean readyToGo = false;
    private boolean allFinished = false;

    private PrintWriter processWriter;

    /**
     * The Constructor.
     *
     * @param testType the test type
     * @param jvm the jvm
     * @param threadCount the thread count
     * @param threadLoops the thread loops
     * @param longArraySize the long array size
     * @param lockType the lock type
     * @throws ApexException the apex exception
     */
    public ConcurrentContextMetricsJVMThread(final String testType, final int jvm, final int threadCount, final int threadLoops, final int longArraySize,
            final int lockType) throws ApexException {
        this.testType = testType;
        this.jvm = jvm;
        this.threadCount = threadCount;
        this.threadLoops = threadLoops;
        this.longArraySize = longArraySize;
        this.lockType = lockType;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final List<String> commandList = new ArrayList<>();
        commandList.add(System.getProperty("java.home") + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator") + "java");
        commandList.add("-cp");
        commandList.add(System.getProperty("java.class.path"));
        for (final Entry<Object, Object> property : System.getProperties().entrySet()) {
            if (property.getKey().toString().startsWith("APEX") || property.getKey().toString().equals("java.net.preferIPv4Stack")
                    || property.getKey().toString().equals("jgroups.bind_addr")) {
                commandList.add("-D" + property.getKey().toString() + "=" + property.getValue().toString());
            }
        }
        commandList.add("com.ericsson.apex.plugins.context.metrics.ConcurrentContextMetricsJVM");
        commandList.add(testType);
        commandList.add(new Integer(jvm).toString());
        commandList.add(new Integer(threadCount).toString());
        commandList.add(new Integer(threadLoops).toString());
        commandList.add(new Integer(longArraySize).toString());
        commandList.add(new Integer(lockType).toString());

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
            processWriter = new PrintWriter(process.getOutputStream());
            final InputStreamReader isr = new InputStreamReader(is);
            final BufferedReader br = new BufferedReader(isr);

            String line;

            LOGGER.info("JVM Output for command " + commandList + "\n");
            while ((line = br.readLine()) != null) {
                LOGGER.info(line);

                if (line.trim().equals("ReadyToGo")) {
                    readyToGo = true;
                }
                else if (line.trim().equals("AllFinished")) {
                    allFinished = true;
                }
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

    /**
     * Checks if is ready to go.
     *
     * @return true, if checks if is ready to go
     */
    public boolean isReadyToGo() {
        return readyToGo;
    }

    /**
     * Checks if is all finished.
     *
     * @return true, if checks if is all finished
     */
    public boolean isAllFinished() {
        return allFinished;
    }

    /**
     * Off you go.
     */
    public void offYouGo() {
        processWriter.println("OffYouGo");
        processWriter.flush();
    }

    /**
     * Finish it out.
     */
    public void finishItOut() {
        processWriter.println("FinishItOut");
        processWriter.flush();
    }
}
