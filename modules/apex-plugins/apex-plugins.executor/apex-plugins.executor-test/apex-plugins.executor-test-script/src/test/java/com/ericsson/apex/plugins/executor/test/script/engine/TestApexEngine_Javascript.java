/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.test.script.engine;

import java.io.IOException;

import org.junit.Test;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.plugins.executor.javascript.JavascriptExecutorParameters;

public class TestApexEngine_Javascript {

	/**
	 * Test apex engine.
	 *
	 * @throws ApexException the apex exception
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testApexEngineJavascript() throws ApexException, InterruptedException, IOException {
		EngineParameters parameters = new EngineParameters();
		parameters.getExecutorParameterMap().put("JAVASCRIPT", new JavascriptExecutorParameters());

		new TestApexEngine("JAVASCRIPT", parameters);
		new TestApexEngine("JAVASCRIPT", parameters);
	}
}
