/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import com.ericsson.apex.core.engine.ExecutorParameters;

/**
 * Default Executor parameters for MVEL
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @version 
 */
public class SuperDooperExecutorParameters extends ExecutorParameters {
	public SuperDooperExecutorParameters () {
		this.setTaskExecutorPluginClass          ("com.ericsson.apex.service.engine.parameters.dummyclasses.DummyTaskExecutor");
		this.setTaskSelectionExecutorPluginClass ("com.ericsson.apex.service.engine.parameters.dummyclasses.DummyTaskSelectExecutor");
		this.setStateFinalizerExecutorPluginClass("com.ericsson.apex.service.engine.parameters.dummyclasses.DummyStateFinalizerExecutor");
	}
}
