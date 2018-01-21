/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.distribution.hazelcast;

import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.impl.distribution.AbstractDistributor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * This context distributor distributes context across threads in multiple JVMs on multiple hosts. It uses hazelcast to distribute maps.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class HazelcastContextDistributor extends AbstractDistributor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(HazelcastContextDistributor.class);

    // The hazelcast instance for distributing context for this JVM
    private static HazelcastInstance hazelcastInstance = null;

    /**
     * Create an instance of a Hazelcast Context Distributor.
     *
     * @throws ContextException On errors creating the context distributor
     */
    public HazelcastContextDistributor() throws ContextException {
        super();
        LOGGER.entry("HazelcastContextDistributor()");

        LOGGER.exit("HazelcastContextDistributor()");
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

        // Create the hazelcast instance if it does not already exist
        if (hazelcastInstance == null) {
            hazelcastInstance = Hazelcast.newHazelcastInstance();
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
        // Get the map from Hazelcast
        LOGGER.info("HazelcastContextDistributor: create album: " + contextAlbumKey.getID());
        return hazelcastInstance.getMap(contextAlbumKey.getID());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.impl.distribution.AbstractContextDistributor#shutdown()
     */
    @Override
    public void shutdown() {
        // Shut down the hazelcast instance
        if (hazelcastInstance != null) {
            hazelcastInstance.shutdown();
        }
        hazelcastInstance = null;
    }
}
