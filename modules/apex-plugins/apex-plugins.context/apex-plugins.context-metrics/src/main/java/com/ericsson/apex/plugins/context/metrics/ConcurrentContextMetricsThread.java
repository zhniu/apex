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

import java.util.Random;

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
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;

/**
 * The Class ConcurrentContextMetricsThread gets metrics for concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContextMetricsThread implements Runnable {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextMetricsThread.class);
    private final Distributor contextDistributor;
    private final int jvm;
    private final int instance;
    private final int threadLoops;
    private final int longArraySize;
    private final int lockType;

    /**
     * The Constructor.
     *
     * @param jvm the jvm
     * @param instance the instance
     * @param threadLoops the thread loops
     * @param longArraySize the long array size
     * @param lockType the lock type
     * @throws ApexException the apex exception
     */
    public ConcurrentContextMetricsThread(final int jvm, final int instance, final int threadLoops, final int longArraySize, final int lockType)
            throws ApexException {
        this.jvm = jvm;
        this.instance = instance;
        this.threadLoops = threadLoops;
        this.longArraySize = longArraySize;
        this.lockType = lockType;

        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor_" + jvm + "_" + instance, "0.0.1");
        contextDistributor = new DistributorFactory().getDistributor(distributorKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.info("running ConcurrentContextMetricsThread_" + jvm + "_" + instance + " . . .");

        ContextAlbum lTypeAlbum = null;
        try {
            final AxContextModel axTestContextModel = TestContextAlbumFactory.createMultiAlbumsContextModel();
            contextDistributor.registerModel(axTestContextModel);
            lTypeAlbum = contextDistributor.createContextAlbum(new AxArtifactKey("LTypeContextAlbum", "0.0.1"));
        }
        catch (final Exception e) {
            LOGGER.error("could not get the test context album", e);
            LOGGER.error("failed ConcurrentContextMetricsThread_" + jvm + "_" + instance);
            return;
        }

        if (lTypeAlbum == null) {
            LOGGER.error("could not find the test context album");
            LOGGER.error("failed ConcurrentContextMetricsThread_" + jvm + "_" + instance);
            return;
        }

        final AxArtifactKey[] usedArtifactStackArray = {new AxArtifactKey("testCC-top", "0.0.1"), new AxArtifactKey("testCC-" + instance, "0.0.1")};

        lTypeAlbum.setUserArtifactStack(usedArtifactStackArray);

        final Random rand = new Random();

        for (int i = 0; i < threadLoops; i++) {
            // Get the next random entry to use
            final String nextLongKey = Integer.toString(rand.nextInt(longArraySize));

            if (lockType == 0) {
                final TestContextItem003 item = (TestContextItem003) lTypeAlbum.get(nextLongKey);
                final long value = item.getLongValue();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("lock type=" + lockType + ", value=" + value);
                }
                continue;
            }

            if (lockType == 1) {
                try {
                    lTypeAlbum.lockForReading(nextLongKey);
                }
                catch (final ContextException e) {
                    LOGGER.error("could not acquire read lock on context album, key=" + nextLongKey, e);
                    continue;
                }

                final TestContextItem003 item = (TestContextItem003) lTypeAlbum.get(nextLongKey);
                final long value = item.getLongValue();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("lock type=" + lockType + ", value=" + value);
                }

                try {
                    lTypeAlbum.unlockForReading(nextLongKey);
                }
                catch (final ContextException e) {
                    LOGGER.error("could not release read lock on context album, key=" + nextLongKey, e);
                }

                continue;
            }

            if (lockType == 2) {
                try {
                    lTypeAlbum.lockForWriting(nextLongKey);
                }
                catch (final ContextException e) {
                    LOGGER.error("could not acquire write lock on context album, key=" + nextLongKey, e);
                    continue;
                }

                final TestContextItem003 item = (TestContextItem003) lTypeAlbum.get(nextLongKey);
                long value = item.getLongValue();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("lock type=" + lockType + ", value=" + value);
                }
                item.setLongValue(++value);
                lTypeAlbum.put(nextLongKey, item);

                try {
                    lTypeAlbum.unlockForWriting(nextLongKey);
                }
                catch (final ContextException e) {
                    LOGGER.error("could not release write lock on context album, key=" + nextLongKey, e);
                }
                continue;
            }
        }

        LOGGER.info("completed ConcurrentContextMetricsThread_" + jvm + "_" + instance);
    }
}
