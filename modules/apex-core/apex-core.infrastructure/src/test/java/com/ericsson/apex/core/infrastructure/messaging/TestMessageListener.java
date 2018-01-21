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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.MessageBlock;
import com.google.common.eventbus.Subscribe;

/**
 * The listener interface for receiving testMessage events. The class that is interested in processing a testMessage event implements this interface, and the
 * object created with that class is registered with a component using the component's <code>addTestMessageListener</code> method. When
 * the testMessage event occurs, that object's appropriate
 * method is invoked.
 *
 */
public abstract class TestMessageListener implements MessageListener<String> {

	/** The Constant logger. */
	private static final XLogger logger = XLoggerFactory.getXLogger(TestMessageListener.class);

	/**
	 * On command.
	 *
	 * @param data the data
	 */
	public abstract void onCommand(MessageBlock<String> data);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.core.infrastructure.messaging.MessageListener#onMessage(com.ericsson.apex.core.infrastructure.messaging.impl.ws.data.Data)
	 */
	@Subscribe
	@Override
	public final void onMessage(MessageBlock<String> data) {
		if (data != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{} command recieved from machine {} ", data.getMessages().size(), data.getConnection().getRemoteSocketAddress().getHostString());
			}
			onCommand(data);
		}
	}
}
