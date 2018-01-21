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

import com.ericsson.apex.core.protocols.engdep.messages.UpdateModel;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * The Class UpdateModelTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class UpdateModelTest {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(UpdateModelTest.class);

	UpdateModel message = null;

	/**
	 * Test register entity.
	 *
	 * @throws UnknownHostException the unknown host exception
	 */
	@Test
	public void testRegisterEntity() throws UnknownHostException {
		final AxArtifactKey targetKey = new AxArtifactKey("UpdateModelTest", "0.0.1");
		message = new UpdateModel(targetKey, new String("Placeholder for Apex model XML"), false, false);
		assertNotNull(message);
		logger.debug(message.toString());
		assertTrue((message.toString()).contains("Placeholder for Apex model XML"));
	}
}
