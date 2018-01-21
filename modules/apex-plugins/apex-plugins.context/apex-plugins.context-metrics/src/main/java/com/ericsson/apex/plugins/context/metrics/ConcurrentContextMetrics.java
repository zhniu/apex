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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.context.parameters.LockManagerParameters;
import com.ericsson.apex.context.test.concepts.TestContextItem003;
import com.ericsson.apex.context.test.factory.TestContextAlbumFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanDistributorParameters;
import com.ericsson.apex.plugins.context.locking.curator.CuratorLockManagerParameters;

/**
 * The Class concurrentContextMetrics tests concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContextMetrics {
    // @formatter:off
    private static final int NUM_ARGS              = 8;
    private static final int ARG_LABEL             = 0;
    private static final int ARG_JVM_COUNT         = 1;
    private static final int ARG_THREAD_COUNT      = 2;
    private static final int ARG_ITERATIONS        = 3;
    private static final int ARG_ARRAY_SIZE        = 4;
    private static final int ARG_LOCK_TYPE         = 5;
    private static final int ARG_ZOOKEEPER_ADDRESS = 6;
    private static final int ARG_INTERACTIVE       = 7;
    // @formatter:on

    private static final int TIME_10_MS = 10;

    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextMetrics.class);

    // Test parameters
    private String testLabel = null;
    private int jvmCount = -1;
    private int threadCount = -1;
    private int threadLoops = -1;
    private int longArraySize = -1;
    private int lockType = -1;
    private String zookeeperAddress = null;
    private long total = -1;
    private boolean interactive = false;

    // The context distributor and map used by each test
    private Distributor contextDistributor = null;
    private ContextAlbum lTypeAlbum = null;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,S");

    /**
     * The main method.
     *
     * @param args the args
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    public static void main(final String[] args) throws ApexModelException, IOException, ApexException {
        if (args.length != NUM_ARGS) {
            System.err
                    .println("usage: ConcurrentContextMetrics testLabel jvmCount threadCount threadLoops longArraySize lockType zookeeperAddress interactive");
            return;
        }
        // @formatter:off
        final ConcurrentContextMetrics concurrentContextMetrics = new ConcurrentContextMetrics(
                args[ARG_LABEL],
                Integer.valueOf(args[ARG_JVM_COUNT]),
                Integer.valueOf(args[ARG_THREAD_COUNT]),
                Integer.valueOf(args[ARG_ITERATIONS]),
                Integer.valueOf(args[ARG_ARRAY_SIZE]),
                Integer.valueOf(args[ARG_LOCK_TYPE]),
                args[ARG_ZOOKEEPER_ADDRESS],
                Boolean.valueOf(args[ARG_INTERACTIVE]));
        // @formatter:on

        concurrentContextMetrics.concurrentContextMetricsJVMLocal();
        concurrentContextMetrics.concurrentContextMetricsCurator();
        concurrentContextMetrics.concurrentContextMetricsHazelcast();
        concurrentContextMetrics.concurrentContextMetricsHazelcastMultiJVMHazelcastLock();
        concurrentContextMetrics.concurrentContextMetricsInfinispanMultiJVMHazelcastlock();
        concurrentContextMetrics.concurrentContextMetricsInfinispanMultiJVMCuratorLock();
        concurrentContextMetrics.concurrentContextMetricsHazelcastMultiJVMCuratorLock();
    }

    /**
     * The Constructor.
     *
     * @param testLabel the test label
     * @param jvmCount the jvm count
     * @param threadCount the thread count
     * @param threadLoops the thread loops
     * @param longArraySize the long array size
     * @param lockType the lock type
     * @param zookeeperAddress the zookeeper address
     * @param interactive the interactive
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
    public ConcurrentContextMetrics(final String testLabel, final int jvmCount, final int threadCount, final int threadLoops, final int longArraySize,
            final int lockType, final String zookeeperAddress, final boolean interactive) {
        // CHECKSTYLE:ON: checkstyle:parameterNumber
        super();
        this.testLabel = testLabel;
        this.jvmCount = jvmCount;
        this.threadCount = threadCount;
        this.threadLoops = threadLoops;
        this.longArraySize = longArraySize;
        this.lockType = lockType;
        this.zookeeperAddress = zookeeperAddress;
        this.interactive = interactive;
    }

    /**
     * Concurrent context metrics JVM local.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsJVMLocal() throws ApexModelException, IOException, ApexException {
        if (jvmCount != 1) {
            return;
        }

        LOGGER.debug("Running concurrentContextMetricsJVMLocalVarSet metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass(DistributorParameters.DEFAULT_DISTRIBUTOR_PLUGIN_CLASS);
        contextParameters.getLockManagerParameters().setPluginClass(LockManagerParameters.DEFAULT_LOCK_MANAGER_PLUGIN_CLASS);
        runConcurrentContextMetrics("JVMLocal");

        LOGGER.debug("Ran concurrentContextMetricsJVMLocalVarSet metrics");
    }

    /**
     * Concurrent context metrics hazelcast.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsHazelcast() throws ApexModelException, IOException, ApexException {
        if (jvmCount != 1) {
            return;
        }

        LOGGER.debug("Running concurrentContextMetricsHazelcast metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass(DistributorParameters.DEFAULT_DISTRIBUTOR_PLUGIN_CLASS);
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.plugins.context.locking.hazelcast.HazelcastLockManager");
        runConcurrentContextMetrics("Hazelcast");

        LOGGER.debug("Ran concurrentContextMetricsHazelcast metrics");
    }

    /**
     * Concurrent context metrics curator.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsCurator() throws ApexModelException, IOException, ApexException {
        if (jvmCount != 1) {
            return;
        }

        LOGGER.debug("Running concurrentContextMetricsCurator metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass(DistributorParameters.DEFAULT_DISTRIBUTOR_PLUGIN_CLASS);

        final CuratorLockManagerParameters curatorParameters = new CuratorLockManagerParameters();
        curatorParameters.setPluginClass("com.ericsson.apex.plugins.context.locking.curator.CuratorLockManager");
        contextParameters.setLockManagerParameters(curatorParameters);
        curatorParameters.setZookeeperAddress(zookeeperAddress);

        runConcurrentContextMetrics("Curator");

        LOGGER.debug("Ran concurrentContextMetricsCurator metrics");
    }

    /**
     * Concurrent context metrics hazelcast multi JVM hazelcast lock.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsHazelcastMultiJVMHazelcastLock() throws ApexModelException, IOException, ApexException {
        LOGGER.debug("Running concurrentContextMetricsHazelcastMultiJVMHazelcastLock metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.hazelcast.HazelcastContextDistributor");
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.plugins.context.locking.hazelcast.HazelcastLockManager");
        runConcurrentContextMetrics("HazelcastMultiJVMHazelcastLock");

        LOGGER.debug("Ran concurrentContextMetricsHazelcastMultiJVMHazelcastLock metrics");
    }

    /**
     * Concurrent context metrics infinispan multi JVM hazelcastlock.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsInfinispanMultiJVMHazelcastlock() throws ApexModelException, IOException, ApexException {
        LOGGER.debug("Running concurrentContextMetricsInfinispanMultiJVMHazelcastlock metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanContextDistributor");
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.plugins.context.locking.hazelcast.HazelcastLockManager");

        final InfinispanDistributorParameters infinispanParameters = new InfinispanDistributorParameters();
        contextParameters.setDistributorParameters(infinispanParameters);

        runConcurrentContextMetrics("InfinispanMultiJVMHazelcastlock");

        LOGGER.debug("Ran concurrentContextMetricsInfinispanMultiJVMHazelcastlock metrics");
    }

    /**
     * Concurrent context metrics infinispan multi JVM curator lock.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsInfinispanMultiJVMCuratorLock() throws ApexModelException, IOException, ApexException {
        LOGGER.debug("Running concurrentContextMetricsInfinispanMultiJVMCuratorLock metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanContextDistributor");

        final CuratorLockManagerParameters curatorParameters = new CuratorLockManagerParameters();
        curatorParameters.setPluginClass("com.ericsson.apex.plugins.context.locking.curator.CuratorLockManager");
        contextParameters.setLockManagerParameters(curatorParameters);
        curatorParameters.setZookeeperAddress(zookeeperAddress);

        final InfinispanDistributorParameters infinispanParameters = new InfinispanDistributorParameters();
        contextParameters.setDistributorParameters(infinispanParameters);

        runConcurrentContextMetrics("InfinispanMultiJVMCuratorLock");

        LOGGER.debug("Ran concurrentContextMetricsInfinispanMultiJVMCuratorLock metrics");
    }

    /**
     * Concurrent context metrics hazelcast multi JVM curator lock.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void concurrentContextMetricsHazelcastMultiJVMCuratorLock() throws ApexModelException, IOException, ApexException {
        LOGGER.debug("Running concurrentContextMetricsHazelcastMultiJVMCuratorLock metrics . . .");

        final ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.hazelcast.HazelcastContextDistributor");

        final CuratorLockManagerParameters curatorParameters = new CuratorLockManagerParameters();
        curatorParameters.setPluginClass("com.ericsson.apex.plugins.context.locking.curator.CuratorLockManager");
        contextParameters.setLockManagerParameters(curatorParameters);
        curatorParameters.setZookeeperAddress(zookeeperAddress);

        runConcurrentContextMetrics("HazelcastMultiJVMCuratorLock");

        LOGGER.debug("Ran concurrentContextMetricsHazelcastMultiJVMCuratorLock metrics");
    }

    /**
     * Run concurrent context metrics.
     *
     * @param testName the test name
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    private void runConcurrentContextMetrics(final String testName) throws ApexModelException, IOException, ApexException {
        total = -1;
        outMetricLine(testName, "Init");

        try {
            setupContext();
        }
        catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }

        outMetricLine(testName, "Start");

        Thread[] threadArray;

        // Check if we have a single JVM or multiple JVMs
        int runningThreadCount = -1;
        if (jvmCount == 1) {
            threadArray = new Thread[threadCount];

            // Run everything in this JVM
            for (int t = 0; t < threadCount; t++) {
                threadArray[t] = new Thread(new ConcurrentContextMetricsThread(0, t, threadLoops, longArraySize, lockType));
                threadArray[t].setName(testLabel + "_" + testName + ":concurrentContextMetricsThread_0_" + t);
                threadArray[t].start();
            }

            outMetricLine(testName, "Running");
            runningThreadCount = threadCount;
        }
        else {
            threadArray = new Thread[jvmCount];

            final ConcurrentContextMetricsJVMThread[] jvmArray = new ConcurrentContextMetricsJVMThread[jvmCount];
            // Spawn JVMs to run the tests
            for (int j = 0; j < jvmCount; j++) {
                jvmArray[j] = new ConcurrentContextMetricsJVMThread(testLabel + "_" + testName, j, threadCount, threadLoops, longArraySize, lockType);
                threadArray[j] = new Thread(jvmArray[j]);
                threadArray[j].setName(testLabel + "_" + testName + ":concurrentContextMetricsJVMThread_" + j);
                threadArray[j].start();
            }

            boolean allReadyToGo;
            do {
                ThreadUtilities.sleep(TIME_10_MS);
                allReadyToGo = true;
                for (int j = 0; j < jvmCount; j++) {
                    if (!jvmArray[j].isReadyToGo()) {
                        allReadyToGo = false;
                        break;
                    }
                }
            }
            while (!allReadyToGo);

            outMetricLine(testName, "Ready");
            if (interactive) {
                System.in.read();
            }
            outMetricLine(testName, "Running");

            for (int j = 0; j < jvmCount; j++) {
                jvmArray[j].offYouGo();
            }

            boolean allFinished;
            do {
                ThreadUtilities.sleep(TIME_10_MS);
                allFinished = true;
                for (int j = 0; j < jvmCount; j++) {
                    if (!jvmArray[j].isAllFinished()) {
                        allFinished = false;
                        break;
                    }
                }
            }
            while (!allFinished);

            outMetricLine(testName, "Completed");

            verifyContext(testName);

            for (int j = 0; j < jvmCount; j++) {
                jvmArray[j].finishItOut();
            }

            runningThreadCount = jvmCount;
        }

        boolean allFinished;
        do {
            ThreadUtilities.sleep(TIME_10_MS);
            allFinished = true;
            for (int i = 0; i < runningThreadCount; i++) {
                if (threadArray[i].isAlive()) {
                    allFinished = false;
                    break;
                }
            }
        }
        while (!allFinished);

        if (jvmCount == 1) {
            outMetricLine(testName, "Completed");
            verifyContext(testName);
        }

        clearContext(testName);
    }

    /**
     * Setup context.
     *
     * @throws ContextException the context exception
     */
    private void setupContext() throws ContextException {
        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor", "0.0.1");
        contextDistributor = new DistributorFactory().getDistributor(distributorKey);

        final AxArtifactKey[] usedArtifactStackArray = {new AxArtifactKey("testC-top", "0.0.1"), new AxArtifactKey("testC-next", "0.0.1"),
                new AxArtifactKey("testC-bot", "0.0.1") };

        final AxContextModel albumsModel = TestContextAlbumFactory.createMultiAlbumsContextModel();
        contextDistributor.registerModel(albumsModel);

        lTypeAlbum = contextDistributor.createContextAlbum(new AxArtifactKey("LTypeContextAlbum", "0.0.1"));
        assert (lTypeAlbum != null);
        lTypeAlbum.setUserArtifactStack(usedArtifactStackArray);

        for (int i = 0; i < longArraySize; i++) {
            final String longKey = Integer.toString(i);
            final TestContextItem003 longItem = new TestContextItem003();
            longItem.setLongValue(0);
            lTypeAlbum.put(longKey, longItem);
        }
    }

    /**
     * Verify context.
     *
     * @param testName the test name
     * @throws ContextException the context exception
     */
    private void verifyContext(final String testName) throws ContextException {
        total = 0;

        try {
            for (int i = 0; i < longArraySize; i++) {
                total += ((TestContextItem003) lTypeAlbum.get(Integer.toString(i))).getLongValue();
            }

            outMetricLine(testName, "Totaled");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }

        if (lockType == 2) {
            if (total == jvmCount * threadCount * threadLoops) {
                outMetricLine(testName, "VerifiedOK");
            }
            else {
                outMetricLine(testName, "VerifiedFail");
            }
        }
        else {
            if (total == 0) {
                outMetricLine(testName, "VerifiedOK");
            }
            else {
                outMetricLine(testName, "VerifiedFail");
            }
        }
    }

    /**
     * Clear context.
     *
     * @param testName the test name
     * @throws ContextException the context exception
     */
    private void clearContext(final String testName) throws ContextException {
        contextDistributor.clear();
        contextDistributor = null;

        outMetricLine(testName, "Cleared");
    }

    /**
     * Out metric line.
     *
     * @param testName the test name
     * @param testPhase the test phase
     */
    public void outMetricLine(final String testName, final String testPhase) {
        System.out.println("ContextMetrics," + dateFormat.format(new Date()) + "," + System.currentTimeMillis() + "," + testLabel + "," + testName + ","
                + testPhase + "," + jvmCount + "," + threadCount + "," + threadLoops + "," + longArraySize + "," + lockType + "," + total);
    }
}
