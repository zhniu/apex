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

import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageListener;
import com.ericsson.apex.core.infrastructure.messaging.stringmessaging.WSStringMessageServer;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

public class StringTestServer {
	private WSStringMessageServer server;
	
	public StringTestServer(final int port, long timeToLive) throws MessagingException {
		System.out.println("StringTestServer starting on port " + port + " for " + timeToLive + " seconds . . .");
		server = new WSStringMessageServer(port);
		assertNotNull(server);
		server.start(new WSStringServerMessageListener());

		System.out.println("StringTestServer started on port " + port + " for " + timeToLive + " seconds");

		for ( ; timeToLive > 0; timeToLive--) {
			ThreadUtilities.sleep(1000);
		}

		server.stop();
		System.out.println("StringTestServer completed");
	}
	
	private class WSStringServerMessageListener implements WSStringMessageListener {
		@Override
		public void receiveString(String stringMessage) {
			System.out.println("Server received string \"" + stringMessage + "\"");
			server.sendString("Server echoing back the message: \"" + stringMessage + "\"");
		}
	}

	public static void main(String[] args) throws MessagingException {
		if (args.length != 2) {
			System.err.println("Usage: StringTestServer port timeToLive");
			return;
		}
		
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		}
		catch (Exception e) {
			System.err.println("Usage: StringTestServer port timeToLive");
			e.printStackTrace();
			return;
		}

		long timeToLive = 0;
		try {
			timeToLive = Long.parseLong(args[1]);
		}
		catch (Exception e) {
			System.err.println("Usage: StringTestServer port timeToLive");
			e.printStackTrace();
			return;
		}
		
		new StringTestServer(port, timeToLive);

	}
}
