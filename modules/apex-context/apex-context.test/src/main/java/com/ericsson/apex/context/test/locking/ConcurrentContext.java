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

import java.io.IOException;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.test.concepts.TestContextItem003;
import com.ericsson.apex.context.test.factory.TestContextAlbumFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;

/**
 * The Class TestConcurrentContext tests concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContext {
    private static final int TEN_MILLISECONDS = 10;

    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContext.class);

    // The context distributor and map used by each test
    private Distributor contextDistributor = null;
    private ContextAlbum lTypeAlbum = null;

    /**
     * Test concurrent context.
     *
     * @param testType the test type
     * @param jvmCount the jvm count
     * @param threadCount the thread count
     * @param threadLoops the thread loops
     * @return the long
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    public long testConcurrentContext(final String testType, final int jvmCount, final int threadCount, final int threadLoops)
            throws ApexModelException, IOException, ApexException {
        final ConcurrentContext concurrentContext = new ConcurrentContext();

        try {
            concurrentContext.setupAndVerifyContext();
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }

        LOGGER.debug("starting JVMs and threads . . .");

        final Thread[] threadArray = new Thread[threadCount];

        // Check if we have a single JVM or multiple JVMs
        int runningThreadCount = -1;
        if (jvmCount == 1) {
            // Run everything in this JVM
            for (int t = 0; t < threadCount; t++) {
                threadArray[t] = new Thread(new ConcurrentContextThread(0, t, threadLoops));
                threadArray[t].setName(testType + ":TestConcurrentContextThread_0_" + t);
                threadArray[t].start();
            }

            runningThreadCount = threadCount;
        }
        else {
            // Spawn JVMs to run the tests
            for (int j = 0; j < jvmCount; j++) {
                threadArray[j] = new Thread(new ConcurrentContextJVMThread(testType, j, threadCount, threadLoops));
                threadArray[j].setName(testType + ":TestConcurrentContextJVMThread_" + j);
                threadArray[j].start();
            }
            runningThreadCount = jvmCount;
        }

        boolean allFinished;
        do {
            allFinished = true;
            for (int i = 0; i < runningThreadCount; i++) {
                if (threadArray[i].isAlive()) {
                    allFinished = false;
                    try {
                        Thread.sleep(TEN_MILLISECONDS);
                    }
                    catch (final Exception e) {
                    }
                    break;
                }
            }
        }
        while (!allFinished);

        return concurrentContext.verifyAndClearContext(jvmCount, threadCount, threadLoops);
    }

    /**
     * Setup and verify context.
     *
     * @throws ContextException the context exception
     */
    private void setupAndVerifyContext() throws ContextException {
        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor", "0.0.1");
        contextDistributor = new DistributorFactory().getDistributor(distributorKey);

        // @formatter:off
        final AxArtifactKey[] usedArtifactStackArray = {
                new AxArtifactKey("testC-top", "0.0.1"),
                new AxArtifactKey("testC-next", "0.0.1"),
                new AxArtifactKey("testC-bot", "0.0.1")
                };
        // @formatter:on

        final AxContextModel albumsModel = TestContextAlbumFactory.createMultiAlbumsContextModel();
        contextDistributor.registerModel(albumsModel);

        lTypeAlbum = contextDistributor.createContextAlbum(new AxArtifactKey("LTypeContextAlbum", "0.0.1"));
        assert (lTypeAlbum != null);
        lTypeAlbum.setUserArtifactStack(usedArtifactStackArray);

        // CHECKSTYLE:OFF: checkstyle:magicNumber
        lTypeAlbum.put("lTypeValue0", new TestContextItem003(0xFFFFFFFFFFFFFFFFL));
        lTypeAlbum.put("lTypeValue1", new TestContextItem003(0xFFFFFFFFFFFFFFFEL));
        lTypeAlbum.put("lTypeValue2", new TestContextItem003(0xFFFFFFFFFFFFFFFDL));
        lTypeAlbum.put("lTypeValue3", new TestContextItem003(0xFFFFFFFFFFFFFFFCL));
        lTypeAlbum.put("lTypeValue4", new TestContextItem003(0xFFFFFFFFFFFFFFFBL));
        lTypeAlbum.put("lTypeValue5", new TestContextItem003(0xFFFFFFFFFFFFFFFAL));
        lTypeAlbum.put("lTypeValue6", new TestContextItem003(0xFFFFFFFFFFFFFFF9L));
        lTypeAlbum.put("lTypeValue7", new TestContextItem003(0xFFFFFFFFFFFFFFF8L));
        lTypeAlbum.put("lTypeValue8", new TestContextItem003(0xFFFFFFFFFFFFFFF7L));
        lTypeAlbum.put("lTypeValue9", new TestContextItem003(0xFFFFFFFFFFFFFFF6L));
        lTypeAlbum.put("lTypeValueA", new TestContextItem003(0xFFFFFFFFFFFFFFF5L));
        lTypeAlbum.put("lTypeValueB", new TestContextItem003(0xFFFFFFFFFFFFFFF4L));
        lTypeAlbum.put("lTypeValueC", new TestContextItem003(0xFFFFFFFFFFFFFFF3L));
        lTypeAlbum.put("lTypeValueD", new TestContextItem003(0xFFFFFFFFFFFFFFF2L));
        lTypeAlbum.put("lTypeValueE", new TestContextItem003(0xFFFFFFFFFFFFFFF1L));
        lTypeAlbum.put("lTypeValueF", new TestContextItem003(0xFFFFFFFFFFFFFFF0L));
        LOGGER.debug(lTypeAlbum.toString());
        assert (lTypeAlbum.size() >= 16);
        // CHECKSTYLE:ON: checkstyle:magicNumber

        // The initial value for concurrent testing
        final TestContextItem003 item = new TestContextItem003(0L);
        lTypeAlbum.put("testValue", item);

    }

    /**
     * Verify and clear context.
     *
     * @param jvmCount the jvm count
     * @param threadCount the thread count
     * @param threadLoops the thread loops
     * @return the long
     * @throws ContextException the context exception
     */
    private long verifyAndClearContext(final int jvmCount, final int threadCount, final int threadLoops) throws ContextException {
        try {
            LOGGER.debug("threads finished, end value is {}", ((TestContextItem003) lTypeAlbum.get("testValue")).getLongValue());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        final long total = ((TestContextItem003) lTypeAlbum.get("testValue")).getLongValue();

        contextDistributor.clear();
        contextDistributor = null;

        return total;
    }
}
