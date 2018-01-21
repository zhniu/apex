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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The Class ThreadingTest.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ThreadingTest {

	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(ThreadingTest.class);

	/**
	 * Test thread factory initialization.
	 */
	@Test
	public void testThreadFactoryInitialization() {
		final ApplicationThreadFactory threadFactory0 = new ApplicationThreadFactory("localName", 0);
		assertNotNull("Failed to create ApplicationThreadFactory threadFactory0", threadFactory0);
		logger.debug(threadFactory0.toString());
		assertTrue("Failed to name ApplicationThreadFactory threadFactory0", threadFactory0.getName().startsWith("Apex-localName"));
		final ApplicationThreadFactory threadFactory1 = new ApplicationThreadFactory("localName", 0);
		assertNotNull("Failed to create ApplicationThreadFactory threadFactory1", threadFactory1);
		logger.debug(threadFactory1.toString());
		assertTrue("Failed to name ApplicationThreadFactory threadFactory1", threadFactory1.getName().startsWith("Apex-localName"));

		testThreadFactory(threadFactory0, 0);
		testThreadFactory(threadFactory1, 1);
	}

	/**
	 * Test thread factory.
	 *
	 * @param threadFactory the thread factory
	 * @param factoryId the factory id
	 */
	private void testThreadFactory(final ApplicationThreadFactory threadFactory, final int factoryId) {
		final List<ThreadingTestThread> threadList = new ArrayList<ThreadingTestThread>();

		for (int i = 0; i < 5; i++) {
			threadList.add(new ThreadingTestThread());
			threadList.get(i).setThread(threadFactory.newThread(threadList.get(i)));
			assertTrue(threadList.get(i).getName().startsWith("Apex-localName"));
			assertTrue(threadList.get(i).getName().contains(":" + i));
			threadList.get(i).getThread().start();
		}

		// Threads should need a little more than 300ms to count to 3
		ThreadUtilities.sleep(380);

		for (int i = 0; i < 5; i++) {
			threadList.get(i).interrupt();
		}

		for (int i = 0; i < 5; i++) {
			assertTrue("Thread (" + i + ") failed to get count (" + threadList.get(i).getCounter() + ") up to 3", threadList.get(i).getCounter() == 3);
		}
	}
}
