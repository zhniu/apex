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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;

/**
 * This class maps a Hazelcast {@link ILock} to a Java {@link ReadWriteLock}.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class HazelcastLock implements ReadWriteLock {
    // The Lock ID
    private final String lockID;

    // The hazelcast lock
    private final ILock readLock;
    private final ILock writeLock;

    /**
     * Create a Hazelcast lock.
     *
     * @param hazelcastInstance the hazelcast instance to use to create the lock
     * @param lockId The unique ID of the lock.
     */
    public HazelcastLock(final HazelcastInstance hazelcastInstance, final String lockId) {
        lockID = lockId;

        // Create the Hazelcast read and write locks
        readLock = hazelcastInstance.getLock(lockId + "_READ");
        writeLock = hazelcastInstance.getLock(lockId + "_WRITE");
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
        return readLock;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.locks.ReadWriteLock#writeLock()
     */
    @Override
    public Lock writeLock() {
        return writeLock;
    }
}
