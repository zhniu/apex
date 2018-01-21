/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.avro;

import static org.junit.Assert.fail;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.core.engine.engine.EnEventListener;
import com.ericsson.apex.core.engine.event.EnEvent;

/**
 * The Class TestApexEngine_VPN.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestVPNEventHandler implements EnEventListener {
	BlockingQueue<EnEvent> actionEventQueue = new LinkedBlockingQueue<EnEvent>();

	private ApexEngine engine;

	public TestVPNEventHandler(ApexEngine engine) {
		super();
		this.engine = engine;
	}

	/**
	 * Send event.
	 *
	 * @param event the event
	 * @return the en event
	 * @throws InterruptedException the interrupted exception
	 */
	public EnEvent sendEvent(final EnEvent event) throws InterruptedException {
		engine.handleEvent(event);

		EnEvent receivedEvent = null;
		while (receivedEvent == null) {
			receivedEvent = actionEventQueue.poll(100, TimeUnit.MILLISECONDS);
		}

		return receivedEvent;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.core.engine.engine.EnEventListener#onEnEvent(com.ericsson.apex.core.engine.event.EnEvent)
	 */
	@Override
	public void onEnEvent(EnEvent event) throws ContextException {
		if (event != null) {
			System.out.println("Event Listener: Action event from engine:" + event.getName());
			actionEventQueue.add(event);
		}
		else {
			fail("Event Listener: null action event from engine");
		}
	}
}
