/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.deployment.rest;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.deployment.ApexDeploymentException;
import com.ericsson.apex.core.deployment.EngineServiceFacade;
import com.google.gson.JsonObject;
/**
 * The class represents the root resource exposed at the base URL<br>
 * The url to access this resource would be in the form {@code <baseURL>/rest/....} <br>
 * For example: a GET request to the following URL {@code http://localhost:18989/apexservices/rest/?hostName=localhost&port=12345}
 * 
 * <b>Note:</b> An allocated {@code hostName} and {@code port} query parameter must be included in all requests.
 * Datasets for different {@code hostName} are completely isolated from one another.
 * 
 */
@Path("deployment/")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })

public class ApexDeploymentRestResource {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexDeploymentRestResource.class);
    
    /**
     * Constructor, a new resource director is created for each request.
     */
    public ApexDeploymentRestResource() {
    }
    
    /**
     * Query the engine service for data
     *
     * @param hostName the host name of the engine service to connect to.
     * @param port the port number of the engine service to connect to.
     * @return a Response object containing the engines service, status and context data in JSON
     */
    @GET
    public Response createSession(@QueryParam("hostName") String hostName, @QueryParam("port") int port) {
        String host = hostName + ":" + port;
        final EngineServiceFacade engineServiceFacade = new EngineServiceFacade(hostName, port);
        
        try {
            engineServiceFacade.init();
        }
        catch (final ApexDeploymentException e) {
            String errorMessage = "Error connecting to Apex Engine Service at " + host;
            LOGGER.warn(errorMessage + "<br>", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage + "\n" + e.getMessage()).build();
        }
        
        JsonObject responseObject = new JsonObject();
        
        // Engine Service data
        responseObject.addProperty("engine_id", engineServiceFacade.getKey().getID());
        responseObject.addProperty("model_id", engineServiceFacade.getApexModelKey() != null ? engineServiceFacade.getApexModelKey().getID() : "Not Set");
        responseObject.addProperty("server", hostName);
        responseObject.addProperty("port", Integer.toString(port));
        
        return Response.ok(responseObject.toString(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * Upload a model.
     *
     * @param hostName the host name of the engine service to connect to.
     * @param port the port number of the engine service to connect to.
     * @param uploadedInputStream input stream
     * @param fileDetail details on the file
     * @param ignoreConflicts conflict policy
     * @param forceUpdate update policy
     * @return a response object in plain text confirming the upload was successful 
     */
    @POST
    @Path("modelupload/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response modelUpload(@FormDataParam("hostName") String hostName, @FormDataParam("port") int port, @FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("ignoreConflicts") boolean ignoreConflicts, @FormDataParam("forceUpdate") boolean forceUpdate) {
        final EngineServiceFacade engineServiceFacade = new EngineServiceFacade(hostName, port);
        
        try {
            engineServiceFacade.init();
        }
        catch (final ApexDeploymentException e) {
            String errorMessage = "Error connecting to Apex Engine Service at " + hostName + ":" + port;
            LOGGER.warn(errorMessage + "<br>", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage + "\n" + e.getMessage()).build();
        }
        
        try {
            engineServiceFacade.deployModel(fileDetail.getFileName(), uploadedInputStream, ignoreConflicts, forceUpdate);
        }
        catch (final Exception e) {
            LOGGER.warn("Error updating model on engine service " + engineServiceFacade.getKey().getID(), e);
            String errorMessage = "Error updating model on engine service " + engineServiceFacade.getKey().getID();
            LOGGER.warn(errorMessage + "<br>", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage + "\n" + e.getMessage()).build();
        }
        
        return Response.ok("Model " + fileDetail.getFileName() + " deployed on engine service " + engineServiceFacade.getKey().getID()).build();
    }
    
}
