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


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;

/**
 * The Class TestConcurrentContext tests concurrent use of context.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestConcurrentContext {
    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(TestConcurrentContext.class);

    // Test parameters
    private static final int TEST_JVM_COUNT_SINGLE_JVM    =   1;
    private static final int TEST_JVM_COUNT_MULTI_JVM     =   3;
    private static final int TEST_THREAD_COUNT_SINGLE_JVM =  64;
    private static final int TEST_THREAD_COUNT_MULTI_JVM  =  20;
    private static final int TEST_THREAD_LOOPS            = 100;

    @Test
    public void testConcurrentContextJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testConcurrentContextJVMLocalVarSet test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.context.impl.locking.jvmlocal.JVMLocalLockManager");
        long result = new ConcurrentContext().testConcurrentContext("JVMLocalVarSet", TEST_JVM_COUNT_SINGLE_JVM, TEST_THREAD_COUNT_SINGLE_JVM, TEST_THREAD_LOOPS);

        assertEquals(TEST_JVM_COUNT_SINGLE_JVM * TEST_THREAD_COUNT_SINGLE_JVM * TEST_THREAD_LOOPS, result);

        logger.debug("Ran testConcurrentContextJVMLocalVarSet test");
    }

    @Test
    public void testConcurrentContextJVMLocalNoVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testConcurrentContextJVMLocalNoVarSet test . . .");

        new ContextParameters();
        long result = new ConcurrentContext().testConcurrentContext("JVMLocalNoVarSet", TEST_JVM_COUNT_SINGLE_JVM, TEST_THREAD_COUNT_SINGLE_JVM, TEST_THREAD_LOOPS);

        assertEquals(TEST_JVM_COUNT_SINGLE_JVM * TEST_THREAD_COUNT_SINGLE_JVM * TEST_THREAD_LOOPS, result);

        logger.debug("Ran testConcurrentContextJVMLocalNoVarSet test");
    }

    @Test
    public void testConcurrentContextMultiJVMNoLock() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testConcurrentContextMultiJVMNoLock test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.context.impl.distribution.jvmlocal.JVMLocalDistributor");
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.context.impl.locking.jvmlocal.JVMLocalLockManager");

        long result = new ConcurrentContext().testConcurrentContext("testConcurrentContextMultiJVMNoLock", TEST_JVM_COUNT_MULTI_JVM, TEST_THREAD_COUNT_MULTI_JVM, TEST_THREAD_LOOPS);

        // No concurrent map so result will be zero
        assertEquals(0, result);

        logger.debug("Ran testConcurrentContextMultiJVMNoLock test");
    }
}
