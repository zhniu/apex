/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.context;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConceptGetter;
import com.ericsson.apex.model.basicmodel.concepts.AxConceptGetterImpl;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.handling.ContextComparer;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;
import com.google.common.collect.Maps;

/**
 * This class manages the internal context for an Apex engine. This class is not thread safe and need not be because each Context object is owned by one and
 * only one ApexEngine, which runs in a single thread and only runs one policy at a time. Therefore there is only ever one policy using a Context object at a
 * time. The currentPolicyContextAlbum is set on the Context object by the StateMachineExecutor each time a policy is triggered.
 *
 * @author Liam Fallon
 */
public final class ApexInternalContext implements AxConceptGetter<ContextAlbum> {
    // The key of the currently running Apex model
    private final AxArtifactKey key;

    // The context albums being used in this engine
    private final NavigableMap<AxArtifactKey, ContextAlbum> contextAlbums = Maps.synchronizedNavigableMap(new TreeMap<AxArtifactKey, ContextAlbum>());

    // The internal context uses a context distributor to handle distribution of context across multiple instances
    private Distributor contextDistributor = null;

    // The key of the current policy, used to return the correct policy context album to the user
    private final AxArtifactKey currentPolicyKey = null;

    /**
     * Constructor, instantiate the context object from the Apex model.
     *
     * @param apexPolicyModel the apex model
     * @throws ContextException On errors on context setting
     */
    public ApexInternalContext(final AxPolicyModel apexPolicyModel) throws ContextException {
        apexPolicyModel.register();

        // The context distributor used to distribute context across policy engine instances
        contextDistributor = new DistributorFactory().getDistributor(apexPolicyModel.getKey());

        // Set up the context albums for this engine
        for (final AxArtifactKey contextAlbumKey : ModelService.getModel(AxContextAlbums.class).getAlbumsMap().keySet()) {
            contextAlbums.put(contextAlbumKey, contextDistributor.createContextAlbum(contextAlbumKey));
        }

        // Record the key of the current model
        key = apexPolicyModel.getKey();
    }

    /**
     * Get the key of the internal context, which is the same as the key of the engine.
     *
     * @return the key
     */
    public AxArtifactKey getKey() {
        return key;
    }

    /**
     * Get the context albums of the engine.
     *
     * @return the context albums
     */
    public Map<AxArtifactKey, ContextAlbum> getContextAlbums() {
        return contextAlbums;
    }

    /**
     * Update the current context so that it aligns with this incoming model, transferring context values if they exist in the new model.
     *
     * @param newPolicyModel The new incoming Apex model to use for context
     * @throws ContextException On errors on context setting
     */
    public void update(final AxPolicyModel newPolicyModel) throws ContextException {
        if (newPolicyModel == null) {
            throw new ContextException("internal context update failed, supplied model is null");
        }

        // Get the differences between the existing context and the new context
        final KeyedMapDifference<AxArtifactKey, AxContextAlbum> contextDifference = new ContextComparer().compare(ModelService.getModel(AxContextAlbums.class),
                newPolicyModel.getAlbums());

        // Remove maps that are no longer used
        for (final Entry<AxArtifactKey, AxContextAlbum> removedContextAlbumEntry : contextDifference.getLeftOnly().entrySet()) {
            contextDistributor.removeContextAlbum(removedContextAlbumEntry.getValue());
            contextAlbums.remove(removedContextAlbumEntry.getKey());
        }

        // We switch over to the new Apex model
        newPolicyModel.register();

        // Set up the new context albums
        for (final AxArtifactKey contextAlbumKey : contextDifference.getRightOnly().keySet()) {
            contextAlbums.put(contextAlbumKey, contextDistributor.createContextAlbum(contextAlbumKey));
        }

        // Handle the updated maps
        for (final Entry<AxArtifactKey, List<AxContextAlbum>> contextAlbumEntry : contextDifference.getDifferentValues().entrySet()) {
            // Compare the updated maps
            final AxContextAlbum currentContextAlbum = contextAlbumEntry.getValue().get(0);
            final AxContextAlbum newContextAlbum = contextAlbumEntry.getValue().get(1);

            // Check that the schemas are the same on the old and new context albums
            if (currentContextAlbum.getItemSchema().equals(newContextAlbum.getItemSchema())) {
                // The schema is different, throw an exception because the schema should not change if the key of the album has not changed
                throw new ContextException("internal context update failed on context album \"" + contextAlbumEntry.getKey().getID() + "\" in model \""
                        + key.getID() + "\", schema \"" + currentContextAlbum.getItemSchema().getID() + "\" on existing context model does not equal schema \""
                        + newContextAlbum.getItemSchema().getID() + "\" on incoming model");
            }
        }

    }

    /**
     * Clear the internal context.
     *
     * @throws ContextException on clearing errors
     */
    public void clear() throws ContextException {
        // Clear all context in the distributor
        contextDistributor.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApexInternalContext [contextAlbums=" + contextAlbums + ", contextDistributor=" + contextDistributor + ", currentPolicyKey=" + currentPolicyKey
                + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(com.ericsson.apex.core.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public ContextAlbum get(final AxArtifactKey conceptKey) {
        return new AxConceptGetterImpl<>(contextAlbums).get(conceptKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(java.lang.String)
     */
    @Override
    public ContextAlbum get(final String conceptKeyName) {
        return new AxConceptGetterImpl<>(contextAlbums).get(conceptKeyName);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(java.lang.String, java.lang.String)
     */
    @Override
    public ContextAlbum get(final String conceptKeyName, final String conceptKeyVersion) {
        return new AxConceptGetterImpl<>(contextAlbums).get(conceptKeyName, conceptKeyVersion);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#getAll(java.lang.String)
     */
    @Override
    public Set<ContextAlbum> getAll(final String conceptKeyName) {
        return new AxConceptGetterImpl<>(contextAlbums).getAll(conceptKeyName);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#getAll(java.lang.String, java.lang.String)
     */
    @Override
    public Set<ContextAlbum> getAll(final String conceptKeyName, final String conceptKeyVersion) {
        return new AxConceptGetterImpl<>(contextAlbums).getAll(conceptKeyName, conceptKeyVersion);
    }
}
