/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.dao;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This factory class returns an Apex DAO for the configured persistence mechanism. The factory uses the plugin class specified in {@link DAOParameters} to
 * instantiate a DAO instance.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexDaoFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexDaoFactory.class);

    /**
     * Return an Apex DAO for the required APEX DAO plugin class.
     *
     * @param daoParameters parameters to use to read the database configuration information
     * @return the Apex DAO
     * @throws ApexException on invalid JPA plugins
     */
    public ApexDao createApexDao(final DAOParameters daoParameters) throws ApexException {
        Assertions.argumentNotNull(daoParameters, ApexException.class, "Parameter \"daoParameters\" may not be null");

        // Get the class for the DAO using reflection
        Object apexDaoObject = null;
        try {
            apexDaoObject = Class.forName(daoParameters.getPluginClass()).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Apex DAO class not found for DAO plugin \"" + daoParameters.getPluginClass() + "\"", e);
            throw new ApexException("Apex DAO class not found for DAO plugin \"" + daoParameters.getPluginClass() + "\"", e);
        }

        // Check the class is an Apex DAO
        if (!(apexDaoObject instanceof ApexDao)) {
            LOGGER.error("Specified Apex DAO plugin class \"" + daoParameters.getPluginClass() + "\" does not implement the ApexDao interface");
            throw new ApexException("Specified Apex DAO plugin class \"" + daoParameters.getPluginClass() + "\" does not implement the ApexDao interface");
        }

        return (ApexDao) apexDaoObject;
    }
}
