/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.distribution;


import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;

/**
 * The Class TestContextInstantiation.
 *
 * @author Sergey Sachkov (sergey.sachkov@ericsson.com)
 */
public class TestSequentialContextInstantiation {
    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(TestSequentialContextInstantiation.class);

    @Before
    public void beforeTest() {
    }

    @After
    public void afterTest() {
    }

    @Test
    public void testSequentialContextInstantiationJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testSequentialContextInstantiationJVMLocalVarSet test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.context.impl.distribution.jvmlocal.JVMLocalDistributor");
        new SequentialContextInstantiation().testSequentialContextInstantiation();

        logger.debug("Ran testSequentialContextInstantiationJVMLocalVarSet test");
    }

    @Test
    public void testSequentialContextInstantiationJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testSequentialContextInstantiationJVMLocalVarNotSet test . . .");

        new ContextParameters();
        new SequentialContextInstantiation().testSequentialContextInstantiation();

        logger.debug("Ran testSequentialContextInstantiationJVMLocalVarNotSet test");
    }
}
