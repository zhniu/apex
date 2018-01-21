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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

/**
 * This class maps a Curator {@link InterProcessReadWriteLock} to a Java {@link ReadWriteLock}.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CuratorReentrantReadWriteLock implements ReadWriteLock {
    // The Lock ID
    private final String lockID;

    // The Curator lock
    private final InterProcessReadWriteLock curatorReadWriteLock;

    // The Curator Lock facades for read and write locks
    private final CuratorLockFacade readLockFacade;
    private final CuratorLockFacade writeLockFacade;

    /**
     * Create a Curator lock.
     *
     * @param curatorFramework the Curator framework to use to create the lock
     * @param lockId The unique ID of the lock.
     */
    public CuratorReentrantReadWriteLock(final CuratorFramework curatorFramework, final String lockId) {
        lockID = lockId;

        // Create the Curator lock
        curatorReadWriteLock = new InterProcessReadWriteLock(curatorFramework, lockId);

        // Create the lock facades
        readLockFacade = new CuratorLockFacade(curatorReadWriteLock.readLock(), lockId);
        writeLockFacade = new CuratorLockFacade(curatorReadWriteLock.writeLock(), lockId);
    }

    /**
     * Get the lock Id of the lock.
     *
     * @return the lock ID
     */
    public String getLockID() {
        return lockID;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.ReadWriteLock#readLock()
     */
    @Override
    public Lock readLock() {
        return readLockFacade;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.ReadWriteLock#writeLock()
     */
    @Override
    public Lock writeLock() {
        return writeLockFacade;
    }
}
