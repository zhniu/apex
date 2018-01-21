/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.service.engine.main.ApexMain;

/**
 * This class is a listener that is called when the servlet is started and stopped. It brings up the Apex engine on servlet start and shuts it down on servlet
 * stop.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
@WebListener
public class ApexServletListener implements ServletContextListener {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexServletListener.class);

    // The Apex engine reference
    private ApexMain apexMain;

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        // The web.xml file contains the context parameters for the Apex engine
        String configFileName = servletContextEvent.getServletContext().getInitParameter("config-file");
        String modelFileName  = servletContextEvent.getServletContext().getInitParameter("model-file");

        LOGGER.info("Apex Servliet has been started, config-file= " + configFileName + ", model-file=" + modelFileName);

        // Check that a configuration file have been specified
        if (servletContextEvent.getServletContext().getInitParameter("config-file") == null) {
            String errorMessage = "Apex servlet start failed, servlet parameter \"config-file\" has not been specified";
            LOGGER.error("Apex servlet start failed, servlet parameter \"config-file\" has not been specified");
            throw new ApexRuntimeException(errorMessage);
        }

        // Construct the Apex command line arguments
        List<String> argsList = new ArrayList<String>();
        argsList.add("-config-file");
        argsList.add(configFileName);

        // Model file name is an optional parameter
        if (modelFileName != null) {
            argsList.add("-model-file");
            argsList.add(modelFileName);
        }

        // Initialize apex
        apexMain = new ApexMain(argsList.toArray(new String[argsList.size()]));
    }


    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        // Shut Apex down
        try {
            apexMain.shutdown();
            apexMain = null;
        }
        catch (ApexException e) {
            String errorMessage = "Apex servlet stop did not execute normally";
            LOGGER.error(errorMessage, e);
        }

        LOGGER.info("Apex Servliet has been stopped");
    }
}
