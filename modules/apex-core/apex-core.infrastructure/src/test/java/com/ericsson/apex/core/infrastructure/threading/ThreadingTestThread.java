/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.threading;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The Class ThreadingTestThread.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ThreadingTestThread implements Runnable {

	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(ThreadingTestThread.class);

	private boolean interrupted = false;

	private long counter = -1;

	private Thread thread = null;

	/**
	 * Sets the thread.
	 *
	 * @param thread the new thread
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * Gets the thread.
	 *
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (logger.isDebugEnabled()) {
			logger.debug("starting threading test thread \"" + thread.getName() + "\" . . .");
		}

		while (!interrupted) {
			counter++;
			if (logger.isDebugEnabled()) {
				logger.debug("in threading test thread \"" + thread.getName() + "\", counter=" + counter + " . . .");
			}

			if (!ThreadUtilities.sleep(100)) {
				interrupted = true;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("stopped threading test thread \"" + thread.getName() + "\"");
		}
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return thread.getName();
	}

	/**
	 * Interrupt.
	 */
	public void interrupt() {
		interrupted = true;
	}

	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 */
	public Long getCounter() {
		return counter;
	}
}
