/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.test.locking;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.plugins.context.locking.curator.CuratorLockManager;
import com.ericsson.apex.plugins.context.locking.curator.CuratorLockManagerParameters;

public class CuratorManagerTest {
	// Zookeeper test server
	TestingServer zkTestServer;
	
	@Before
	public void beforeTest() throws Exception {
	    zkTestServer = new TestingServer(62181);
	}

	@After
	public void afterTest() throws IOException {
	    zkTestServer.stop();
	}

	@Test
	public void testCuratorManagerConfigProperty() {
		ContextParameters contextParameters = new ContextParameters();
		contextParameters.setLockManagerParameters(new CuratorLockManagerParameters());
		
		((CuratorLockManagerParameters)contextParameters.getLockManagerParameters()).setZookeeperAddress(null);

		try {
			CuratorLockManager curatorManager = new CuratorLockManager();
			curatorManager.init(new AxArtifactKey());
			assertNull(curatorManager);
		}
		catch (ContextException e) {
			assertEquals(e.getMessage(), "could not set up Curator locking, check if the curator Zookeeper address parameter is set correctly");
		}

		((CuratorLockManagerParameters)contextParameters.getLockManagerParameters()).setZookeeperAddress("zooby");

		try {
			CuratorLockManager curatorManager = new CuratorLockManager();
			curatorManager.init(new AxArtifactKey());
			fail("Curator manager test should fail");
		}
		catch (ContextException e) {
			assertEquals(e.getMessage(), "could not connect to Zookeeper server at \"zooby\", see error log for details");
		}

		((CuratorLockManagerParameters)contextParameters.getLockManagerParameters()).setZookeeperAddress("localhost:62181");

		try {
			CuratorLockManager curatorManager0 = new CuratorLockManager();
			curatorManager0.init(new AxArtifactKey());
			assertNotNull(curatorManager0);

			CuratorLockManager curatorManager1 = new CuratorLockManager();
			curatorManager1.init(new AxArtifactKey());
			assertNotNull(curatorManager1);
			
			curatorManager0.shutdown();
			curatorManager1.shutdown();
		}
		catch (ContextException e) {
			assertNull(e);
		}
	}
}