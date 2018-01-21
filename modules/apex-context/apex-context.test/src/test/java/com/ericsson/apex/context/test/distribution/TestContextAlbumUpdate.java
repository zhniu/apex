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
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;

public class TestContextAlbumUpdate {
    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(TestContextAlbumUpdate.class);

    @Test
    public void testContextAlbumUpdateJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextAlbumUpdateJVMLocalVarSet test . . .");

        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.context.impl.distribution.jvmlocal.JVMLocalDistributor");
        new ContextAlbumUpdate().testContextAlbumUpdate();

        logger.debug("Ran testContextAlbumUpdateJVMLocalVarSet test");
    }

    @Test
    public void testContextAlbumUpdateJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
        logger.debug("Running testContextAlbumUpdateJVMLocalVarNotSet test . . .");

        new ContextParameters();
        new ContextAlbumUpdate().testContextAlbumUpdate();

        logger.debug("Ran testContextAlbumUpdateJVMLocalVarNotSet test");
    }

    /**
     * Test context update cleardown.
     */
    @After
    public void testContextAlbumUpdateCleardown() {
    }
}
