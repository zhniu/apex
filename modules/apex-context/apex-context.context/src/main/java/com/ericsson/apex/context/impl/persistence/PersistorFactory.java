/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.persistence;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Persistor;
import com.ericsson.apex.context.parameters.PersistorParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class returns a persistor for the particular type of persistor mechanism that has been configured for use.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PersistorFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(PersistorFactory.class);

    /**
     * Return a persistor for the persistence mechanism configured for use.
     *
     * @param key The key for the persistor
     * @return a persistor
     * @throws ContextException on invalid persistor types
     */
    public Persistor createPersistor(final AxArtifactKey key) throws ContextException {
        LOGGER.entry("persistor factory, key=" + key);
        Assertions.argumentNotNull(key, ContextException.class, "Parameter \"key\" may not be null");

        final PersistorParameters persistorParameters = ParameterService.getParameters(PersistorParameters.class);

        // Get the class for the persistor using reflection
        Object persistorObject = null;
        final String pluginClass = persistorParameters.getPluginClass();
        try {
            persistorObject = Class.forName(pluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Apex context persistor class not found for context persistor plugin \"" + pluginClass + "\"", e);
            throw new ContextException("Apex context persistor class not found for context persistor plugin \"" + pluginClass + "\"", e);
        }

        // Check the class is a persistor
        if (!(persistorObject instanceof Persistor)) {
            LOGGER.error("Specified Apex context persistor plugin class \"" + pluginClass + "\" does not implement the ContextDistributor interface");
            throw new ContextException(
                    "Specified Apex context persistor plugin class \"" + pluginClass + "\" does not implement the ContextDistributor interface");
        }

        // The persistor to return
        final Persistor persistor = (Persistor) persistorObject;

        // Lock and load the persistor
        persistor.init(key);

        LOGGER.exit("Persistor factory, key=" + key + ", selected persistor of class " + persistor.getClass());
        return persistor;
    }
}
