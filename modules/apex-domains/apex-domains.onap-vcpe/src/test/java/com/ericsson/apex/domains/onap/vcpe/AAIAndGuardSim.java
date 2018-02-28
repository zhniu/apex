package com.ericsson.apex.domains.onap.vcpe;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

public class AAIAndGuardSim {
	private static final String BASE_URI = "http://localhost:54321/AAIAndGuardSim";
	private HttpServer server;

	public AAIAndGuardSim() {
		final ResourceConfig rc = new ResourceConfig(AAIAndGuardSimEndpoint.class);
		server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);   

		while (!server.isStarted()) {
			ThreadUtilities.sleep(50);
		}
	}
	public void tearDown() throws Exception {
		server.shutdown();
	}

	public static void main(String[] args) throws Exception {
		AAIAndGuardSim sim = new AAIAndGuardSim();
		
		while (true) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				break;
			}
		}
		sim.tearDown();
	}
}
