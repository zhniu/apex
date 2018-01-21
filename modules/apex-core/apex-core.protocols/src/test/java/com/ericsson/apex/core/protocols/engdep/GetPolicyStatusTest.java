/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.protocols.engdep;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.protocols.engdep.messages.GetEngineStatus;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class GetPolicyStatusTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class GetPolicyStatusTest {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(GetPolicyStatusTest.class);

	GetEngineStatus message = null;

	/**
	 * Test register entity.
	 *
	 * @throws UnknownHostException the unknown host exception
	 */
	@Test
	public void testRegisterEntity() throws UnknownHostException {
		final AxArtifactKey targetKey = new AxArtifactKey("PolicyStatusTest", "0.0.1");
		message = new GetEngineStatus(targetKey);
		assertNotNull(message);
		logger.debug(message.toString());
		assertTrue((message.toString()).contains("PolicyStatusTest"));
	}
}
