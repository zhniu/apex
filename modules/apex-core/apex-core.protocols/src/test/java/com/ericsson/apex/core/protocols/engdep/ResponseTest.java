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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.protocols.engdep.messages.Response;
import com.ericsson.apex.core.protocols.engdep.messages.UpdateModel;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class ResponseTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ResponseTest {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(ResponseTest.class);

	Response message = null;

	/**
	 * Test response.
	 *
	 * @throws UnknownHostException the unknown host exception
	 */
	@Test
	public void testResponse() throws UnknownHostException {
		final AxArtifactKey responseKey = new AxArtifactKey("ResponseTest", "0.0.1");
		final AxArtifactKey responseToKey = new AxArtifactKey("ResponseTestTO", "0.0.1");
		message = new Response(responseKey, false, new UpdateModel(responseToKey));
		logger.debug(message.toString());
		assertTrue(message.toString().contains("ResponseTest"));
		assertFalse(message.isSuccessful());

		message = new Response(responseKey, true, new UpdateModel(responseToKey));
		logger.debug(message.toString());
		assertTrue(message.toString().contains("ResponseTest"));
		assertTrue(message.isSuccessful());
	}
}
