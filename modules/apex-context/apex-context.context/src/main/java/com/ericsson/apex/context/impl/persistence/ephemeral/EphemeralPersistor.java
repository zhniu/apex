/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.persistence.ephemeral;

import java.util.Set;
import java.util.TreeSet;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Persistor;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;

/**
 * This class acts as an "in memory" persistor for a single JVM. It just initiates stubs the Persistor interface and does not persist anything.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EphemeralPersistor implements Persistor {

    // The key of this persistor
    private AxArtifactKey key;

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.Persistor#init(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public void init(final AxArtifactKey persistorKey) throws ContextException {
        this.key = persistorKey;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.context.Persistor#getKey()
     */
    @Override
    public AxArtifactKey getKey() {
        return key;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.Persistor#readContextItem(com.ericsson.apex.core.basicmodel.concepts.AxReferenceKey, java.lang.Class)
     */
    @Override
    public AxContextSchema readContextItem(final AxReferenceKey itemKey, final Class<?> contextItemClass) {
        // Can't read from this persistor as nothing is persisted
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.Persistor#readContextItems(com.ericsson.apex.core.basicmodel.concepts.AxArtifactKey, java.lang.Class)
     */
    @Override
    public Set<AxContextSchema> readContextItems(final AxArtifactKey ownerKey, final Class<?> contextItemClass) throws ContextException {
        // No reading from persistence on the Ephemeral persistor, return an empty set
        return new TreeSet<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.context.Persistor#writeContextItem(com.ericsson.apex.core.contextmodel.concepts.AxContextItem)
     */
    @Override
    public AxContextSchema writeContextItem(final AxContextSchema contextItem) {
        // No writing to persistence on the Ephemeral persistor
        return contextItem;
    }
}
