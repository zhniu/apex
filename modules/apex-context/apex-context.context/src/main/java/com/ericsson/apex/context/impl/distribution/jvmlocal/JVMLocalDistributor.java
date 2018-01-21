/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.distribution.jvmlocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.impl.distribution.AbstractDistributor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * This context distributor distributes context across threads in a single JVM. It holds context in memory in a map.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class JVMLocalDistributor extends AbstractDistributor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(JVMLocalDistributor.class);

    /**
     * Create an instance of a JVM Local Context Distributor.
     */
    public JVMLocalDistributor() {
        super();
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.context.impl.distribution.AbstractDistributor#getContextAlbumMap(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public Map<String, Object> getContextAlbumMap(final AxArtifactKey contextMapKey) {
        LOGGER.debug("create map: " + contextMapKey.getID());
        return Collections.synchronizedMap(new HashMap<String, Object>());
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.context.impl.distribution.AbstractDistributor#shutdown()
     */
    @Override
    public void shutdown() {
        // No specific shutdown for the JVMLocalContextDistributor
    }
}
