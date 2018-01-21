/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.locking.curator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class provides a facade over the {@link Lock} interface for Curator locks.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CuratorLockFacade implements Lock {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(CuratorLockFacade.class);

    // The Lock ID
    private final String lockId;

    // The mutex used for Curator locking
    private final InterProcessMutex lockMutex;

    /**
     * Create the lock Facade.
     *
     * @param lockMutex The lock mutex behind the facade
     * @param lockId The ID of the lock
     */
    public CuratorLockFacade(final InterProcessMutex lockMutex, final String lockId) {
        this.lockId = lockId;
        this.lockMutex = lockMutex;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#lock()
     */
    @Override
    public void lock() {
        try {
            lockMutex.acquire();
        }
        catch (final Exception e) {
            LOGGER.warn("failed to acquire lock for \"" + lockId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#lockInterruptibly()
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        LOGGER.warn("lockInterruptibly() not supported for \"" + lockId);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#tryLock()
     */
    @Override
    public boolean tryLock() {
        try {
            lockMutex.acquire();
            return true;
        }
        catch (final Exception e) {
            LOGGER.warn("failed to acquire lock for \"" + lockId, e);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        try {
            lockMutex.acquire(time, unit);
            return true;
        }
        catch (final Exception e) {
            LOGGER.warn("failed to acquire lock for \"" + lockId, e);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#unlock()
     */
    @Override
    public void unlock() {
        try {
            lockMutex.release();
        }
        catch (final Exception e) {
            LOGGER.warn("failed to release lock for \"" + lockId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.Lock#newCondition()
     */
    @Override
    public Condition newCondition() {
        LOGGER.warn("newCondition() not supported for \"" + lockId);
        return null;
    }
}
