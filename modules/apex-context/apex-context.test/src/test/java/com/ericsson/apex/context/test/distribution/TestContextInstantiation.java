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
public class TestContextInstantiation {
    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(TestContextInstantiation.class);

    @Before
    public void beforeTest() {
    }

    /**
     * Test context instantiation clear down.
     */
    @After
    public void afterTest() {
    }

    @Test
    public void testContextInstantiationJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextInstantiationJVMLocalVarSet test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.context.impl.distribution.jvmlocal.JVMLocalDistributor");
        new ContextInstantiation().testContextInstantiation();

        logger.debug("Ran testContextInstantiationJVMLocalVarSet test");
    }

    @Test
    public void testContextInstantiationJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextInstantiationJVMLocalVarNotSet test . . .");

        new ContextParameters();
        new ContextInstantiation().testContextInstantiation();

        logger.debug("Ran testContextInstantiationJVMLocalVarNotSet test");
    }

}
