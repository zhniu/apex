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

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.context.test.distribution.ContextInstantiation;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanDistributorParameters;

public class TestContextInstantiation {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestContextInstantiation.class);

	@Test
	public void testContextInstantiationJVMLocalVarSet() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextInstantiationJVMLocalVarSet test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass(DistributorParameters.DEFAULT_DISTRIBUTOR_PLUGIN_CLASS);
		new ContextInstantiation().testContextInstantiation();

		logger.debug("Ran testContextInstantiationJVMLocalVarSet test");
	}

	@Test
	public void testContextInstantiationHazelcast() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextInstantiationHazelcast test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.hazelcast.HazelcastContextDistributor");
		new ContextInstantiation().testContextInstantiation();

		logger.debug("Ran testContextInstantiationHazelcast test");
	}

	@Test
	public void testContextInstantiationInfinispan() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextInstantiationInfinispan test . . .");

		ContextParameters contextParameters = new ContextParameters();
		contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.infinispan.InfinispanContextDistributor");
		new InfinispanDistributorParameters();
		
		new ContextInstantiation().testContextInstantiation();

		logger.debug("Ran testContextInstantiationInfinispan test");
	}

	@Test
	public void testContextInstantiationJVMLocalVarNotSet() throws ApexModelException, IOException, ApexException {
		logger.debug("Running testContextInstantiationJVMLocalVarNotSet test . . .");

		new ContextParameters();
		new ContextInstantiation().testContextInstantiation();

		logger.debug("Ran testContextInstantiationJVMLocalVarNotSet test");
	}
}
