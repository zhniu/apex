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

import java.util.List;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class records a usage of a key in the system. When the list of keys being used by a concept is built using the getKeys() method of
 * the {@link AxConcept} class, an instance of this class is created for every key occurrence. The list of keys returned by the getKeys()
 * method is a list of {@link AxKeyUse} objects.
 * <p>
 * Validation checks that each key is valid.
 */

public class AxKeyUse extends AxKey {
    private static final long serialVersionUID = 2007147220109881705L;

    private AxKey usedKey;

    /**
     * The Default Constructor creates this concept with a null key.
     */
    public AxKeyUse() {
        this(new AxArtifactKey());
    }

    /**
     * Copy constructor
     * @param copyConcept the concept to copy from
     */
    public AxKeyUse(final AxKeyUse copyConcept) {
    		super(copyConcept);
    }
    
    /**
     * This constructor creates an instance of this class, and holds a reference to a used key.
     *
     * @param usedKey a used key
     */
    public AxKeyUse(final AxKey usedKey) {
        Assertions.argumentNotNull(usedKey, "usedKey may not be null");
        this.usedKey = usedKey;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKey()
     */
    @Override
    public AxKey getKey() {
        return usedKey;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKeys()
     */
    @Override
    public List<AxKey> getKeys() {
        return usedKey.getKeys();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxKey#getID()
     */
    @Override
    public String getID() {
        return usedKey.getID();
    }

    /**
     * Sets the key.
     *
     * @param key the key
     */
    public void setKey(final AxKey key) {
        Assertions.argumentNotNull(key, "usedKey may not be null");
        this.usedKey = key;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxKey#getCompatibility(com. ericsson.apex.model.basicmodel.concepts.AxKey)
     */
    @Override
    public AxKey.Compatibility getCompatibility(final AxKey otherKey) {
        return usedKey.getCompatibility(otherKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxKey#isCompatible(com. ericsson.apex.model.basicmodel.concepts.AxKey)
     */
    @Override
    public boolean isCompatible(final AxKey otherKey) {
        return usedKey.isCompatible(otherKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#validate(com. ericsson.apex.model.basicmodel.concepts.AxValidationResult)
     */
    @Override
    public AxValidationResult validate(final AxValidationResult result) {
        if (usedKey.equals(AxArtifactKey.getNullKey())) {
            result.addValidationMessage(
                    new AxValidationMessage(usedKey, this.getClass(), ValidationResult.INVALID, "usedKey is a null key"));
        }
        return usedKey.validate(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#clean()
     */
    @Override
    public void clean() {
        usedKey.clean();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(":(");
        builder.append("usedKey=");
        builder.append(usedKey);
        builder.append(")");
        return builder.toString();
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#copyTo(com.ericsson.apex.model.basicmodel.concepts.AxConcept)
	 */
	@Override
	public AxConcept copyTo(final AxConcept target) {
        Assertions.argumentNotNull(target, "target may not be null");

        final Object copyObject = target;
        Assertions.instanceOf(copyObject, AxKeyUse.class);

        final AxKeyUse copy = ((AxKeyUse) copyObject);
        usedKey.copyTo(copy);
        
        return copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + usedKey.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#equals(java.lang. Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("comparison object may not be null");
        }

        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AxKeyUse other = (AxKeyUse) obj;
        return usedKey.equals(other.usedKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final AxConcept otherObj) {
        Assertions.argumentNotNull(otherObj, "comparison object may not be null");

        if (this == otherObj) {
            return 0;
        }
        if (getClass() != otherObj.getClass()) {
            return this.hashCode() - otherObj.hashCode();
        }

        final AxKeyUse other = (AxKeyUse) otherObj;

        return usedKey.compareTo(other.usedKey);
    }
}
