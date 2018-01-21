/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.metrics;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMetrics {
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
	public void getSingleJVMMetrics() {
		String[] args = {
				"singleJVMTestNL",
				"1",
				"32",
				"1000",
				"65536",
				"0",
				"localhost:62181",
				"false"
		};
		
		try {
			ConcurrentContextMetrics.main(args);
		}
		catch (Exception e) {
			fail("Metrics test failed");
			e.printStackTrace();
		}
	}
}
