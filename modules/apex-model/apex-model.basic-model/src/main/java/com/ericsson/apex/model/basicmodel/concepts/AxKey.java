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

/**
 * The key uniquely identifies every entity in the system. This class is an abstract class to give a common parent for all key types in the
 * system.
 */
public abstract class AxKey extends AxConcept {
    private static final long serialVersionUID = 6281159885962014041L;

    /** Regular expression to specify the structure of key names. */
    public static final String NAME_REGEXP = "[A-Za-z0-9\\-_\\.]+";

    /** Regular expression to specify the structure of key versions. */
    public static final String VERSION_REGEXP = "[A-Za-z0-9.]+";

    /** Regular expression to specify the structure of key IDs. */
    public static final String KEY_ID_REGEXP = "[A-Za-z0-9\\-_\\.]+:[0-9].[0-9].[0-9]";

    /** Specifies the value for names in NULL keys. */
    public static final String NULL_KEY_NAME = "NULL";

    /** Specifies the value for versions in NULL keys. */
    public static final String NULL_KEY_VERSION = "0.0.0";

    /**
     * This enumeration is returned on key compatibility checks.
     */
    public enum Compatibility {
        /** The keys have different names. */
        DIFFERENT,
        /** The name of the key matches but the Major version number of the keys is different (x in x.y.z do not match). */
        MAJOR,
        /** The name of the key matches but the Minor version number of the keys is different (y in x.y.z do not match). */
        MINOR,
        /** The name of the key matches but the Patch version number of the keys is different (z in x.y.z do not match). */
        PATCH,
        /** The keys match completely. */
        IDENTICAL
    }

    /**
     * Default constructor
     * @param copyConcept the concept to copy from
     */
    public AxKey() {
    		super();
    }
    
    /**
     * Copy constructor
     * @param copyConcept the concept to copy from
     */
    public AxKey(final AxKey copyConcept) {
    		super(copyConcept);
    }
    
   /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getID()
     */
    @Override
    public abstract String getID();

    /**
     * Return the result of a compatibility check of two keys.
     *
     * @param otherKey the key to check compatibility against
     * @return the compatibility result of the check
     */
    public abstract Compatibility getCompatibility(AxKey otherKey);

    /**
     * Check if two keys are compatible, that is the keys are IDENTICAL or have only MINOR, PATCH differences.
     *
     * @param otherKey the key to check compatibility against
     * @return true, if the keys are compatible
     */
    public abstract boolean isCompatible(AxKey otherKey);
}
