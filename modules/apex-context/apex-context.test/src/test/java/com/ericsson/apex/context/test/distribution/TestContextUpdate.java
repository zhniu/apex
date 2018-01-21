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

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;

/**
 * The Class TestContextUpdate.
 *
 * @author Sergey Sachkov (sergey.sachkov@ericsson.com)
 */
public class TestContextUpdate {
    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(TestContextUpdate.class);

    @Test
    public void testContextUpdateJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextUpdateJVMLocalVarSet test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.context.impl.distribution.jvmlocal.JVMLocalDistributor");
        new ContextUpdate().testContextUpdate();

        logger.debug("Ran testContextUpdateJVMLocalVarSet test");
    }

    @Test
    public void testContextUpdateJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextUpdateJVMLocalVarNotSet test . . .");

        new ContextParameters();
        new ContextUpdate().testContextUpdate();

        logger.debug("Ran testContextUpdateJVMLocalVarNotSet test");
    }
}
