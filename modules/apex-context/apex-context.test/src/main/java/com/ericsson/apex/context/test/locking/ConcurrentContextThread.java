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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.context.test.concepts.TestContextItem003;
import com.ericsson.apex.context.test.factory.TestContextAlbumFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;

/**
 * The Class TestConcurrentContextThread tests concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ConcurrentContextThread implements Runnable {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextThread.class);
    private final Distributor distributor;
    private final int jvm;
    private final int instance;
    private final int threadLoops;

    /**
     * The Constructor.
     *
     * @param jvm the jvm
     * @param instance the instance
     * @param threadLoops the thread loops
     * @throws ApexException the apex exception
     */
    public ConcurrentContextThread(final int jvm, final int instance, final int threadLoops) throws ApexException {
        this.jvm = jvm;
        this.instance = instance;
        this.threadLoops = threadLoops;

        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor_" + jvm + "_" + instance, "0.0.1");

        new ContextParameters();
        distributor = new DistributorFactory().getDistributor(distributorKey);
        final AxContextModel albumsModel = TestContextAlbumFactory.createMultiAlbumsContextModel();
        distributor.registerModel(albumsModel);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.info("running TestConcurrentContextThread_" + jvm + "_" + instance + " . . .");

        ContextAlbum lTypeAlbum = null;

        try {
            lTypeAlbum = distributor.createContextAlbum(new AxArtifactKey("LTypeContextAlbum", "0.0.1"));
        }
        catch (final Exception e) {
            LOGGER.error("could not get the test context album", e);
            LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
            return;
        }

        if (lTypeAlbum == null) {
            LOGGER.error("could not find the test context album");
            LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
            return;
        }

        // @formatter:off
        final AxArtifactKey[] usedArtifactStackArray = {
                new AxArtifactKey("testC-top", "0.0.1"),
                new AxArtifactKey("testC-next", "0.0.1"),
                new AxArtifactKey("testC-bot", "0.0.1")
                };
        // @formatter:on

        lTypeAlbum.setUserArtifactStack(usedArtifactStackArray);

        for (int i = 0; i < threadLoops; i++) {
            try {
                lTypeAlbum.lockForWriting("testValue");
                TestContextItem003 item = (TestContextItem003) lTypeAlbum.get("testValue");
                if (item != null) {
                    long value = item.getLongValue();
                    item.setLongValue(++value);
                }
                else {
                    item = new TestContextItem003(0L);
                }
                lTypeAlbum.put("testValue", item);
            }
            catch (final Exception e) {
                LOGGER.error("could not set the value in the test context album", e);
                LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
                return;
            }
            finally {
                try {
                    lTypeAlbum.unlockForWriting("testValue");
                }
                catch (final ContextException e) {
                    LOGGER.error("could not unlock test context album item", e);
                    LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
                    return;
                }
            }
        }

        try {
            lTypeAlbum.lockForWriting("testValue");
            final TestContextItem003 item = (TestContextItem003) lTypeAlbum.get("testValue");
            final long value = item.getLongValue();
            LOGGER.info("completed TestConcurrentContextThread_" + jvm + "_" + instance + ", value=" + value);
        }
        catch (final Exception e) {
            LOGGER.error("could not read the value in the test context album", e);
            LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
        }
        finally {
            try {
                lTypeAlbum.unlockForWriting("testValue");
                distributor.shutdown();
            }
            catch (final ContextException e) {
                LOGGER.error("could not unlock test context album item", e);
                LOGGER.error("failed TestConcurrentContextThread_" + jvm + "_" + instance);
            }
        }
    }
}
