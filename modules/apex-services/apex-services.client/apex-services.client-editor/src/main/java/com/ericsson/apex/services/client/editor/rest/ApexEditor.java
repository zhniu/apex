/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is used to launch the editor. It creates a Grizzly embedded web server and runs the editor.
 */
public class ApexEditor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexEditor.class);

    // The HTTP server exposing JAX-RS resources defined in this application.
    private final HttpServer server;

    /**
     * Starts the HTTP server for the Apex editor on the default base URI and with the default REST packages.
     */
    public ApexEditor() {
        this(new ApexEditorParameters());
    }

    /**
     * Starts the HTTP server for the Apex editor.
     *
     * @param parameters the parameters
     */
    public ApexEditor(final ApexEditorParameters parameters) {
        Assertions.argumentNotNull(parameters, "parameters may not be null");

        LOGGER.debug("Apex RESTful editor starting . . .");

        // Create a resource configuration that scans for JAX-RS resources and providers
        // in com.ericsson.apex.auth.rest.editor package
        final ResourceConfig rc = new ResourceConfig().packages(parameters.getRESTPackages());

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        server = GrizzlyHttpServerFactory.createHttpServer(parameters.getBaseURI(), rc);

        // Add static content
        server.getServerConfiguration().addHttpHandler(
                new org.glassfish.grizzly.http.server.CLStaticHttpHandler(ApexEditorMain.class.getClassLoader(), "/webapp/"), parameters.getStaticPath());

        LOGGER.debug("Apex RESTful editor started");
    }

    /**
     * Shut down the web server.
     */
    public void shutdown() {
        LOGGER.debug("Apex RESTful editor shutting down . . .");
        server.shutdown();
        LOGGER.debug("Apex RESTful editor shut down");
    }
}
