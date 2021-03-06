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

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;

/**
 * This interface is implemented by plugin classes that distribute context albums in Apex.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface Distributor {

    /**
     * Initialize the distributor with its properties.
     *
     * @param key The key that identifies this distributor
     * @throws ContextException On errors initializing the distributor
     */
    void init(AxArtifactKey key) throws ContextException;

    /**
     * Shut down distributor.
     *
     * @throws ContextException On errors initializing the distributor
     */
    void shutdown() throws ContextException;

    /**
     * Get the key of the distributor.
     *
     * @return the contextSetKey
     */
    AxArtifactKey getKey();

    /**
     * Register the context model and its sub models with the model service.
     *
     * @param contextModel the context model to register
     * @throws ContextException on model registration errors
     */
    void registerModel(AxContextModel contextModel) throws ContextException;

    /**
     * Create a context album on a distributor, the distributor looks up the album and initialize it. The {@link AxContextAlbum} is used to check that the album
     * in the distributor matches the album definition we expect to get.
     *
     * @param axContextAlbumKey the key of the model context album for this context album
     * @return the context album
     * @throws ContextException if the album cannot be initialised
     */
    ContextAlbum createContextAlbum(AxArtifactKey axContextAlbumKey) throws ContextException;

    /**
     * Remove a context album from a distributor.
     *
     * @param contextAlbum The album to remove
     * @throws ContextException if the album cannot be removed
     */
    void removeContextAlbum(AxContextAlbum contextAlbum) throws ContextException;

    /**
     * Flush all context albums owned by the distributor to the distribution mechanism.
     *
     * @throws ContextException on context flushing errors
     */
    void flush() throws ContextException;

    /**
     * Flush a context album owned by the distributor to the distribution mechanism.
     *
     * @param contextAlbum the context album to flush
     * @throws ContextException on errors in flushing the context album
     */
    void flushContextAlbum(ContextAlbum contextAlbum) throws ContextException;

    /**
     * Place a read lock on an item in an album across the entire cluster.
     *
     * @param albumKey The key of the album containing the item
     * @param keyOnMap The key on the album to lock
     * @throws ContextException on locking errors
     */
    void lockForReading(AxArtifactKey albumKey, String keyOnMap) throws ContextException;

    /**
     * Place a write lock on an album item across the entire cluster.
     *
     * @param albumKey The key of the album containing the item
     * @param key The key on the album to lock
     * @throws ContextException on locking errors
     */
    void lockForWriting(AxArtifactKey albumKey, String key) throws ContextException;

    /**
     * Release the read lock on a key across the entire cluster.
     *
     * @param albumKey The key of the album containing the item
     * @param key The key on the album to unlock
     * @throws ContextException on locking errors
     */
    void unlockForReading(AxArtifactKey albumKey, String key) throws ContextException;

    /**
     * Release the write lock on a key across the entire cluster.
     *
     * @param albumKey The key of the album containing the item
     * @param key The key on the album to unlock
     * @throws ContextException on locking errors
     */
    void unlockForWriting(AxArtifactKey albumKey, String key) throws ContextException;

    /**
     * Clear all the context from the context distributor.
     *
     * @throws ContextException on context clearing exceptions
     */
    void clear() throws ContextException;
}
