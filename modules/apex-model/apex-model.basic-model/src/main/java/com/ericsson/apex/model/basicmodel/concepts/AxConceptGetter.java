/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import java.util.Set;

/**
 * This interface is used to allow get methods to be placed on concepts that have embedded maps.
 * <p>
 * It forces those concepts with maps to implement the get methods specified on this interface as convenience methods to avoid concept users having to use a
 * second level of referencing to access concepts in the the maps.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <C> the type of concept on which the interface is applied.
 */
public interface AxConceptGetter<C> {

    /**
     * Get the latest version for a concept with the given key.
     *
     * @param conceptKey The key of the concept
     * @return The concept
     */
    C get(AxArtifactKey conceptKey);

    /**
     * Get the latest version for a concept with the given key name.
     *
     * @param conceptKeyName The name of the concept
     * @return The concept
     */
    C get(String conceptKeyName);

    /**
     * Get the latest version for a concept with the given key name and version.
     *
     * @param conceptKeyName The name of the concept
     * @param conceptKeyVersion The version of the concept
     * @return The concept
     */
    C get(String conceptKeyName, String conceptKeyVersion);

    /**
     * Get the all versions for a concept with the given key name.
     *
     * @param conceptKeyName The name of the concept
     * @return The concepts
     */
    Set<C> getAll(String conceptKeyName);

    /**
     * Get the all versions for a concept with the given key name and starting version.
     *
     * @param conceptKeyName The name of the concept
     * @param conceptKeyVersion The first version version of the concept to get
     * @return The concepts
     */
    Set<C> getAll(String conceptKeyName, String conceptKeyVersion);
}
