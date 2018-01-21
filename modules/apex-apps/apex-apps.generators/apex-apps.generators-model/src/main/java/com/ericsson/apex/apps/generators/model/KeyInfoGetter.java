/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.generators.model;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * Getter methods for policy keys.
 *
 * @author John Keeney (john.keeney@ericsson.com)
 */

public class KeyInfoGetter {

    /** The policy model for the getters. */
    private final AxPolicyModel model;

    /**
     * Creates a new key getter.
     * 
     * @param model the policy model to use
     */
    public KeyInfoGetter(final AxPolicyModel model) {
        this.model = model;
    }

    /**
     * Returns the key name as string.
     * 
     * @param key the key to transform
     * @return key name as string, null if key was null
     */
    public String getName(final AxArtifactKey key) {
        if (key == null) {
            return null;
        }
        return key.getName();
    }

    /**
     * Returns the version of an artifact key.
     * 
     * @param key the key to extract version from
     * @return version of the key, null if key was null
     */
    public String getVersion(final AxArtifactKey key) {
        if (key == null) {
            return null;
        }
        return key.getVersion();
    }

    /**
     * Returns the parent name for the key.
     * 
     * @param key the key to process
     * @return parent name, null if key was null
     */
    public String getPName(final AxReferenceKey key) {
        if (key == null) {
            return null;
        }
        return key.getParentKeyName();
    }

    /**
     * Returns the parent version for the key.
     * 
     * @param key the key to process
     * @return parent version, null if key was null
     */
    public String getPVersion(final AxReferenceKey key) {
        if (key == null) {
            return null;
        }
        return key.getParentKeyVersion();
    }

    /**
     * Returns the local name for the key.
     * 
     * @param key the key to process
     * @return local name, null if key was null
     */
    public String getLName(final AxReferenceKey key) {
        if (key == null) {
            return null;
        }
        return key.getLocalName();
    }

    /**
     * Returns the local name of the parent for the key.
     * 
     * @param key the key to process
     * @return local name of the parent, null if key was null
     */
    public String getPLName(final AxReferenceKey key) {
        if (key == null) {
            return null;
        }
        return key.getParentLocalName();
    }

    /**
     * Returns the UUID of an artifact key.
     * 
     * @param key the key to extract version from
     * @return UUID of the key, null if key was null
     */
    public String getUUID(final AxArtifactKey key) {
        final AxKeyInfo ki = model.getKeyInformation().get(key);
        if (ki == null || ki.getUUID() == null) {
            return null;
        }
        return ki.getUUID().toString();
    }

    /**
     * Returns the description of an artifact key.
     * 
     * @param key the key to extract version from
     * @return description of the key, null if key was null
     */
    public String getDesc(final AxArtifactKey key) {
        final AxKeyInfo ki = model.getKeyInformation().get(key);
        if (ki == null || ki.getDescription() == null) {
            return null;
        }
        return ki.getDescription().toString();
    }
}
