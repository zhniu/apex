/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.distribution;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.parameters.DistributorParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class returns a context distributor for the particular type of distribution mechanism configured for use.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class DistributorFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(DistributorFactory.class);

    /**
     * Get a context distributor for a given context set key.
     *
     * @param key The key for the distributor
     * @return a context distributor
     * @throws ContextException on context distributor creation errors
     */
    public Distributor getDistributor(final AxArtifactKey key) throws ContextException {
        LOGGER.entry("Distributor factory, key=" + key);

        Assertions.argumentNotNull(key, ContextException.class, "Parameter \"key\" may not be null");

        // Get the class for the distributor using reflection
        final DistributorParameters distributorParameters = ParameterService.getParameters(DistributorParameters.class);
        final String pluginClass = distributorParameters.getPluginClass();
        Object contextDistributorObject = null;
        try {
            contextDistributorObject = Class.forName(pluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Apex context distributor class not found for context distributor plugin \"" + pluginClass + "\"", e);
            throw new ContextException("Apex context distributor class not found for context distributor plugin \"" + pluginClass + "\"", e);
        }

        // Check the class is a distributor
        if (!(contextDistributorObject instanceof Distributor)) {
            final String returnString = "Specified Apex context distributor plugin class \"" + pluginClass
                    + "\" does not implement the ContextDistributor interface";
            LOGGER.error(returnString);
            throw new ContextException(returnString);
        }

        // The context Distributor to return
        final Distributor contextDistributor = (Distributor) contextDistributorObject;

        // Lock and load the context distributor
        contextDistributor.init(key);

        LOGGER.exit("Distributor factory, key=" + key + ", selected distributor of class " + contextDistributor.getClass());
        return contextDistributor;
    }
}
