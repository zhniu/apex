/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.distribution.infinispan;

import java.util.Map;

import org.infinispan.Cache;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.impl.distribution.AbstractDistributor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * This context distributor distributes context across threads in multiple JVMs on multiple hosts. It uses Infinispan to distribute maps.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class InfinispanContextDistributor extends AbstractDistributor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(InfinispanContextDistributor.class);

    // The infinispan manager for distributing context for this JVM
    private static InfinispanManager infinispanManager = null;

    /**
     * Create an instance of an Infinispan Context Distributor.
     *
     * @throws ContextException On errors creating the context distributor
     */
    public InfinispanContextDistributor() throws ContextException {
        LOGGER.entry("InfinispanContextDistributor()");

        LOGGER.exit("InfinispanContextDistributor()");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.impl.distribution.AbstractContextDistributor#init(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public void init(final AxArtifactKey key) throws ContextException {
        LOGGER.entry("init(" + key + ")");

        super.init(key);

        // Create the infinispan manager if it does not already exist
        if (infinispanManager == null) {
            // Get the parameters from the parameter service
            final InfinispanDistributorParameters parameters = ParameterService.getParameters(InfinispanDistributorParameters.class);

            LOGGER.debug("initiating Infinispan with the parameters: " + parameters);

            // Create the manager
            infinispanManager = new InfinispanManager(parameters);
        }

        LOGGER.exit("init(" + key + ")");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.impl.distribution.AbstractContextDistributor#getContextAlbumMap(com.ericsson.apex.core.model.concepts.AxArtifactKey)
     */
    @Override
    public Map<String, Object> getContextAlbumMap(final AxArtifactKey contextAlbumKey) {
        LOGGER.info("InfinispanContextDistributor: create album: " + contextAlbumKey.getID());

        // Get the Cache from Infinispan
        final Cache<String, Object> infinispanCache = infinispanManager.getCacheManager().getCache(contextAlbumKey.getID().replace(':', '_'));

        return infinispanCache;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.impl.distribution.AbstractContextDistributor#shutdown()
     */
    @Override
    public void shutdown() {
        // Shut down the infinispan manager
        if (infinispanManager != null) {
            infinispanManager.shutdown();
        }
        infinispanManager = null;
    }
}
