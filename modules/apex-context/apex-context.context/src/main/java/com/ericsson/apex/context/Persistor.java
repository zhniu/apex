/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context;

import java.util.Set;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;

/**
 * This interface is implemented by plugin classes that persist Context Albums in Apex.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface Persistor {

    /**
     * Initialize the persistor with its properties.
     *
     * @param key The key that identifies this persistor
     * @throws ContextException On errors initializing the persistor
     */
    void init(AxArtifactKey key) throws ContextException;

    /**
     * Get the key of the persistor.
     *
     * @return the contextSetKey
     */
    AxArtifactKey getKey();

    /**
     * Read a context item from the persistence mechanism.
     *
     * @param key the key of the context item
     * @param contextItemClassName the name of the context item class, a subclass of {@link AxContextSchema}
     * @return the context item that has been read
     * @throws ContextException on persistence read errors
     */
    AxContextSchema readContextItem(AxReferenceKey key, Class<?> contextItemClassName) throws ContextException;

    /**
     * Read all the values of a particular type from persistence.
     *
     * @param ownerKey the owner key
     * @param contextItemClassName The class name of the objects to return
     * @return the set of context item values read from persistence or null if none were found
     * @throws ContextException On read errors
     */
    Set<AxContextSchema> readContextItems(AxArtifactKey ownerKey, Class<?> contextItemClassName) throws ContextException;

    /**
     * Write a context item value to the persistence mechanism.
     *
     * @param contextItem the context item
     * @return the context item that has been written
     */
    AxContextSchema writeContextItem(AxContextSchema contextItem);
}
