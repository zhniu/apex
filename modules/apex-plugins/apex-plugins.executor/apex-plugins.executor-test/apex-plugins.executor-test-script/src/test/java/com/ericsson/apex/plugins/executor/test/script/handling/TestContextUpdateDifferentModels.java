/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.test.script.handling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineFactory;
import com.ericsson.apex.core.engine.engine.impl.ApexEngineImpl;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.test.script.engine.TestApexActionListener;
import com.ericsson.apex.plugins.executor.testdomain.model.SampleDomainModelFactory;

/**
 * The Class TestApexEngine.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextUpdateDifferentModels {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(TestContextUpdateDifferentModels.class);

	@Before
	public void testContextUpdateDifferentModelsBefore() {
	}

	@Test
	public void testContextUpdateDifferentModels() throws ApexException, InterruptedException, IOException {
		logger.debug("Running test testContextUpdateDifferentModels . . .");

		final AxPolicyModel apexModelSample = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
		assertNotNull(apexModelSample);

		EngineParameters parameters = new EngineParameters();
		parameters.getExecutorParameterMap().put("MVEL", new MVELExecutorParameters());

		final ApexEngineImpl apexEngine = (ApexEngineImpl)new ApexEngineFactory().createApexEngine(new AxArtifactKey("TestApexEngine", "0.0.1"));
		final TestApexActionListener listener = new TestApexActionListener("Test");
		apexEngine.addEventListener("listener", listener);
		apexEngine.updateModel(apexModelSample);
		apexEngine.start();

		apexEngine.stop();
		
		final AxPolicyModel someSpuriousModel = new AxPolicyModel(new AxArtifactKey("SomeSpuriousModel", "0.0.1"));
		assertNotNull(someSpuriousModel);

		try {
			apexEngine.updateModel(null);
			fail("null model should throw an exception");
		}
		catch (ApexException e) {
			assertEquals("updateModel()<-TestApexEngine:0.0.1, Apex model is not defined, it has a null value", e.getMessage());
		}
		assertEquals(apexEngine.getInternalContext().getContextAlbums().size(), apexModelSample.getAlbums().getAlbumsMap().size());
		for (ContextAlbum contextAlbum: apexEngine.getInternalContext().getContextAlbums().values()) {
			assertTrue(contextAlbum.getAlbumDefinition().equals(apexModelSample.getAlbums().get(contextAlbum.getKey())));
		}
		
		apexEngine.updateModel(someSpuriousModel);
		assertEquals(apexEngine.getInternalContext().getContextAlbums().size(), someSpuriousModel.getAlbums().getAlbumsMap().size());
		for (ContextAlbum contextAlbum: apexEngine.getInternalContext().getContextAlbums().values()) {
			assertTrue(contextAlbum.getAlbumDefinition().equals(someSpuriousModel.getAlbums().get(contextAlbum.getKey())));
		}

		apexEngine.clear();

		logger.debug("Ran test testContextUpdateDifferentModels");
	}

	@After
	public void testContextUpdateDifferentModelsAfter() {
	}
}
