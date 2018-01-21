/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * This interface provides a facade to hide implementation details of various lock managers that may be used to manage locking of context items.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface LockManager {

    /**
     * Initialize the lock manager with its properties.
     *
     * @param key The key of this lock manager
     * @throws ContextException On errors initializing the persistor
     */
    void init(AxArtifactKey key) throws ContextException;

    /**
     * Get the key of the lock manager.
     *
     * @return the managers key
     */
    AxArtifactKey getKey();

    /**
     * Place a read lock on a lock type and key across the entire cluster.
     *
     * @param lockTypeKey The key of the map where the context item to lock is
     * @param lockKey The key on the map to lock
     * @throws ContextException on locking errors
     */
    void lockForReading(String lockTypeKey, String lockKey) throws ContextException;

    /**
     * Place a write lock on a lock type and key across the entire cluster.
     *
     * @param lockTypeKey The key of the map where the context item to lock is
     * @param lockKey The key on the map to lock
     * @throws ContextException on locking errors
     */
    void lockForWriting(String lockTypeKey, String lockKey) throws ContextException;

    /**
     * Release a read lock on a lock type and key across the entire cluster.
     *
     * @param lockTypeKey The key of the map where the context item to lock is
     * @param lockKey The key on the map to lock
     * @throws ContextException on locking errors
     */
    void unlockForReading(String lockTypeKey, String lockKey) throws ContextException;

    /**
     * Release a write lock on a lock type and key across the entire cluster.
     *
     * @param lockTypeKey The key of the map where the context item to lock is
     * @param lockKey The key on the map to lock
     * @throws ContextException on locking errors
     */
    void unlockForWriting(String lockTypeKey, String lockKey) throws ContextException;

    /**
     * Shut down the lock manager and clear any connections or data it is using.
     */
    void shutdown();
}
