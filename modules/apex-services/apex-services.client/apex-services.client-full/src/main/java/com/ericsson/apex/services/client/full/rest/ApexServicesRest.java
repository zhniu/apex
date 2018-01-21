/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.full.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is used to launch the services. It creates a Grizzly embedded web server and runs the services.
 */
public class ApexServicesRest {
	// Logger for this class
	private static final XLogger logger = XLoggerFactory.getXLogger(ApexServicesRest.class);
	
	// The HTTP server exposing JAX-RS resources defined in this application.
	private HttpServer server;

	/**
	 * Starts the HTTP server for the Apex services client on the default base URI and with the default REST packages
	 */
	public ApexServicesRest() {
		this(new ApexServicesRestParameters());
	}

	/**
	 * Starts the HTTP server for the Apex services client
	 * @param parameters: The Apex parameters to use to start the server
	 */
	public ApexServicesRest(final ApexServicesRestParameters parameters) {
		Assertions.argumentNotNull(parameters, "parameters may not be null");

		logger.debug("Apex services RESTful client starting . . .");

		// Create a resource configuration that scans for JAX-RS resources and providers
		// in com.ericsson.apex.services.client.full.rest package
		final ResourceConfig rc = new ResourceConfig().packages(parameters.getRESTPackages());
		
		// Add MultiPartFeature class for jersey-media-multipart
		rc.register(MultiPartFeature.class);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		server = GrizzlyHttpServerFactory.createHttpServer(parameters.getBaseURI(), rc);

		// Add static content
		server.getServerConfiguration().addHttpHandler(new org.glassfish.grizzly.http.server.CLStaticHttpHandler(ApexServicesRest.class.getClassLoader(), "/webapp/"), parameters.getStaticPath());

		logger.debug("Apex services RESTful client started");
	}
	
	/**
	 * Shut down the web server
	 */
	public void shutdown() {
		logger.debug("Apex services RESTful client shutting down . . .");
		server.shutdown();
		logger.debug("Apex services RESTful client shut down");
	}
}
