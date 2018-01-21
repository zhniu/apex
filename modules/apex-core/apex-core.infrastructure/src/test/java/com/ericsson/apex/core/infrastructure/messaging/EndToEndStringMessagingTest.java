/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageClient;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageServer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

/**
 * The Class EndToEndMessagingTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EndToEndStringMessagingTest {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(EndToEndStringMessagingTest.class);
	
	private WSStringMessageServer server;
	private WSStringMessageClient client;
	
	private boolean finished = false;

	@Test
	public void testEndToEndMessaging() throws MessagingException {
		logger.debug("end to end messaging test starting . . .");
		server = new WSStringMessageServer(44441);
		assertNotNull(server);
		server.start(new WSStringServerMessageListener());

		client = new WSStringMessageClient("localhost", 44441);
		assertNotNull(client);
		client.start(new WSStringClientMessageListener());

		client.sendString("Hello, client here");
		
		while (!finished) {
			ThreadUtilities.sleep(50);
		}
		client.stop();

		server.stop();
		logger.debug("end to end messaging test finished");
	}
	
	private class WSStringServerMessageListener implements WSStringMessageListener {
		@Override
		public void receiveString(String stringMessage) {
			logger.debug(stringMessage);
			assertEquals("Hello, client here", stringMessage);
			server.sendString("Hello back from server");
		}
	}
	
	private class WSStringClientMessageListener implements WSStringMessageListener {
		@Override
		public void receiveString(String stringMessage) {
			logger.debug(stringMessage);
			assertEquals("Hello back from server", stringMessage);
			finished = true;
		}
	}
}
