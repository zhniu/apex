/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.utilities.comparison.KeyedMapComparer;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;

/**
 * This class compares the context in two AxContext objects and returns the differences. The differences are returned in a {@link KeyedMapDifference} object
 * that contains the left, equal, and right context schemas or albums.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ContextComparer {

    /**
     * Compare two {@link AxContextAlbums} objects, comparing their context albums one after another.
     *
     * @param left the left context
     * @param right the right context
     * @return the difference
     */
    public KeyedMapDifference<AxArtifactKey, AxContextAlbum> compare(final AxContextAlbums left, final AxContextAlbums right) {
        // Find the difference between the AxContext objects
        return new KeyedMapComparer<AxArtifactKey, AxContextAlbum>().compareMaps(left.getAlbumsMap(), right.getAlbumsMap());
    }

    /**
     * Compare two {@link AxContextSchema} objects, comparing their context schemas one after another.
     *
     * @param left the left context
     * @param right the right context
     * @return the difference
     */
    public KeyedMapDifference<AxArtifactKey, AxContextSchema> compare(final AxContextSchemas left, final AxContextSchemas right) {
        // Find the difference between the AxContext objects
        return new KeyedMapComparer<AxArtifactKey, AxContextSchema>().compareMaps(left.getSchemasMap(), right.getSchemasMap());
    }

}
