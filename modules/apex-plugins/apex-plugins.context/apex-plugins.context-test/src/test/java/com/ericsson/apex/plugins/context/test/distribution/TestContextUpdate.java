/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.test.distribution;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.context.test.distribution.ContextUpdate;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanDistributorParameters;

public class TestContextUpdate {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestContextUpdate.class);

	@Before
	public void beforeTest() {
	}

	@After
	public void afterTest() {
	}

	@Test
	public void testContextUpdateJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextUpdateJVMLocalVarSet test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass(DistributorParameters.DEFAULT_DISTRIBUTOR_PLUGIN_CLASS);
		new ContextUpdate().testContextUpdate();

		logger.debug("Ran testContextUpdateJVMLocalVarSet test");
	}

	@Test
	public void testContextUpdateHazelcast() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextUpdateHazelcast test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.hazelcast.HazelcastContextDistributor");
		new ContextUpdate().testContextUpdate();

		logger.debug("Ran testContextUpdateHazelcast test");
	}

	@Test
	public void testContextUpdateInfinispan() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextUpdateInfinispan test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanContextDistributor");
		new InfinispanDistributorParameters();

		new ContextUpdate().testContextUpdate();

		logger.debug("Ran testContextUpdateInfinispan test");
	}

	@Test
	public void testContextUpdateJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextUpdateJVMLocalVarNotSet test . . .");

		new ContextParameters();
		new ContextUpdate().testContextUpdate();

		logger.debug("Ran testContextUpdateJVMLocalVarNotSet test");
	}
}
