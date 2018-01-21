/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.locking.hazelcast;

import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.impl.locking.AbstractLockManager;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * The Class HazelcastLockManager manages Hazelcast locks for locks on items in Apex context albums.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class HazelcastLockManager extends AbstractLockManager {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(HazelcastLockManager.class);

    private HazelcastInstance hazelcastInstance;

    /**
     * Constructor, set up a lock manager that uses Hazelcast locking.
     *
     * @throws ContextException On errors connecting to the Hazelcast cluster
     */
    public HazelcastLockManager() throws ContextException {
        LOGGER.entry("HazelcastLockManager(): setting up the Hazelcast lock manager . . .");

        LOGGER.exit("HazelcastLockManager(): Hazelcast lock manager set up");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.impl.locking.AbstractLockManager#init(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public void init(final AxArtifactKey key) throws ContextException {
        LOGGER.entry("init(" + key + ")");

        super.init(key);

        // Set up the Hazelcast instance for lock handling
        hazelcastInstance = Hazelcast.newHazelcastInstance();

        LOGGER.exit("init(" + key + ")");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.impl.locking.AbstractLockManager#getReentrantReadWriteLock(java.lang.String)
     */
    @Override
    public ReadWriteLock getReentrantReadWriteLock(final String lockId) throws ContextException {
        // Check if the framework is active
        if (hazelcastInstance != null && hazelcastInstance.getLifecycleService().isRunning()) {
            return new HazelcastLock(hazelcastInstance, lockId);
        }
        else {
            throw new ContextException("creation of hazelcast lock failed, see error log for details");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.LockManager#shutdown()
     */
    @Override
    public void shutdown() {
        if (hazelcastInstance == null) {
            return;
        }
        hazelcastInstance.shutdown();
        hazelcastInstance = null;
    }
}
