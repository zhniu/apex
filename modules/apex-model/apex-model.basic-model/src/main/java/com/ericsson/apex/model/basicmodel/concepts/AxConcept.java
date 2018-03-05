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

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is the base class for all Apex concept classes. It enforces implementation of abstract methods and interfaces on all concepts
 * that are sub-classes of this class.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */

@XmlType(name = "AxConcept", namespace = "http://www.ericsson.com/apex")

public abstract class AxConcept implements Serializable, Comparable<AxConcept> {
    private static final long serialVersionUID = -7434939557282697490L;

    /**
     * Gets the key of this concept.
     *
     * @return the concept key
     */
    public abstract AxKey getKey();

    /**
     * Gets a list of all keys for this concept and all concepts that are defined or referenced by this concept and its sub-concepts.
     *
     * @return the keys used by this concept and it's contained concepts
     */
    public abstract List<AxKey> getKeys();

    /**
     * Validate that this concept is structurally correct.
     *
     * @param result the parameter in which the result of the validation will be returned
     * @return the validation result that was passed in in the @{link result} field with the result of this validation added
     */
    public abstract AxValidationResult validate(AxValidationResult result);

    /**
     * Clean this concept, tidy up any superfluous information such as leading and trailing white space.
     */
    public abstract void clean();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public abstract Object clone();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public abstract boolean equals(Object otherObject);

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public abstract int hashCode();

    /**
     * Copy this concept to another object. The target object must have the same class as the source object.
     *
     * @param target the target object to which this object is copied
     * @return the copied object
     */
    public abstract Object copyTo(Object target);

    /**
     * Gets the ID string of this concept.
     *
     * @return the ID string of this concept
     */
    public String getID() {
        return getKey().getID();
    }

    /**
     * Checks if this key matches the given key ID.
     *
     * @param id the key ID to match against
     * @return true, if this key matches the ID
     */
    public final boolean matchesID(final String id) {
        Assertions.argumentNotNull(id, "id may not be null");

        // Check the ID
        return getID().equals(id);
    }
}
