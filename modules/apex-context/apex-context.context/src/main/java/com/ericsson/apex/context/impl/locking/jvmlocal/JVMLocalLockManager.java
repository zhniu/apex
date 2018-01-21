/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.locking.jvmlocal;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.impl.locking.AbstractLockManager;

/**
 * A lock manager that returns locks that have a range of just the local JVM. The implementation uses a Jav {@link ReentrantReadWriteLock} as the lock for
 * context album items.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JVMLocalLockManager extends AbstractLockManager {
    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.impl.locking.AbstractLockManager#getReentrantReadWriteLock(java.lang.String)
     */
    @Override
    public ReadWriteLock getReentrantReadWriteLock(final String lockId) throws ContextException {
        return new ReentrantReadWriteLock();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.LockManager#shutdown()
     */
    @Override
    public void shutdown() {
        // Nothing to do for this lock manager
    }
}
