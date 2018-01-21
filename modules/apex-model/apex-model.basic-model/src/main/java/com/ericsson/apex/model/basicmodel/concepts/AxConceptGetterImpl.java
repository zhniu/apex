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

import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.apex.model.utilities.Assertions;

/**
 * Implements concept getting from navigable maps.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <CONCEPT> the type of concept on which the interface implementation is applied.
 */
public class AxConceptGetterImpl<CONCEPT> implements AxConceptGetter<CONCEPT> {

    // The map from which to get concepts
    private final NavigableMap<AxArtifactKey, CONCEPT> conceptMap;

    /**
     * Constructor that sets the concept map on which the getter methods in the interface will operate..
     *
     * @param conceptMap the concept map on which the method will operate
     */
    public AxConceptGetterImpl(final NavigableMap<AxArtifactKey, CONCEPT> conceptMap) {
        this.conceptMap = conceptMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(com. ericsson.apex.core.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public CONCEPT get(final AxArtifactKey conceptKey) {
        return conceptMap.get(conceptKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(java.lang. String)
     */
    @Override
    public CONCEPT get(final String conceptKeyName) {
        Assertions.argumentNotNull(conceptKeyName, "conceptKeyName may not be null");

        // The very fist key that could have this name
        final AxArtifactKey lowestArtifactKey = new AxArtifactKey(conceptKeyName, "0.0.1");

        // Check if we found a key for our name
        AxArtifactKey foundKey = conceptMap.ceilingKey(lowestArtifactKey);
        if (foundKey == null || !foundKey.getName().equals(conceptKeyName)) {
            return null;
        }

        // Look for higher versions of the key
        do {
            final AxArtifactKey nextkey = conceptMap.higherKey(foundKey);
            if (nextkey == null || !nextkey.getName().equals(conceptKeyName)) {
                break;
            }
            foundKey = nextkey;
        }
        while (true);

        return conceptMap.get(foundKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#get(java.lang. String, java.lang.String)
     */
    @Override
    public CONCEPT get(final String conceptKeyName, final String conceptKeyVersion) {
        Assertions.argumentNotNull(conceptKeyName, "conceptKeyName may not be null");

        if (conceptKeyVersion != null) {
            return conceptMap.get(new AxArtifactKey(conceptKeyName, conceptKeyVersion));
        }
        else {
            return this.get(conceptKeyName);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#getAll(java. lang.String)
     */
    @Override
    public Set<CONCEPT> getAll(final String conceptKeyName) {
        return getAll(conceptKeyName, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.basicmodel.concepts.AxConceptGetter#getAll(java. lang.String, java.lang.String)
     */
    @Override
    public Set<CONCEPT> getAll(final String conceptKeyName, final String conceptKeyVersion) {
        final Set<CONCEPT> returnSet = new TreeSet<>();

        if (conceptKeyName == null) {
            returnSet.addAll(conceptMap.values());
            return returnSet;
        }

        // The very fist key that could have this name
        final AxArtifactKey lowestArtifactKey = new AxArtifactKey(conceptKeyName, "0.0.1");
        if (conceptKeyVersion != null) {
            lowestArtifactKey.setVersion(conceptKeyVersion);
        }

        // Check if we found a key for our name
        AxArtifactKey foundKey = conceptMap.ceilingKey(lowestArtifactKey);
        if (foundKey == null || !foundKey.getName().equals(conceptKeyName)) {
            return returnSet;
        }
        returnSet.add(conceptMap.get(foundKey));

        // Look for higher versions of the key
        do {
            foundKey = conceptMap.higherKey(foundKey);
            if (foundKey == null || !foundKey.getName().equals(conceptKeyName)) {
                break;
            }
            returnSet.add(conceptMap.get(foundKey));
        }
        while (true);

        return returnSet;
    }
}
