/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.concepts;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class TestContextItem00C.
 */
public class TestContextItem00C implements Serializable {
    private static final long serialVersionUID = -7497746259264651884L;

    private static final int HASH_PRIME_1 = 31;

    private Map<String, String> mapValue = new TreeMap<String, String>();

    /**
     * The Constructor.
     */
    public TestContextItem00C() {
    }

    /**
     * The Constructor.
     *
     * @param mapValue the map value
     */
    public TestContextItem00C(final Map<String, String> mapValue) {
        this.mapValue = mapValue;
    }

    /**
     * Gets the map value.
     *
     * @return the map value
     */
    public Map<String, String> getMapValue() {
        if (mapValue == null) {
            mapValue = new TreeMap<String, String>();
        }
        return mapValue;
    }

    /**
     * Sets the map value.
     *
     * @param mapValue the map value
     */
    public void setMapValue(final Map<String, String> mapValue) {
        this.mapValue = mapValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((mapValue == null) ? 0 : mapValue.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TestContextItem00C other = (TestContextItem00C) obj;
        if (mapValue == null) {
            if (other.mapValue != null) {
                return false;
            }
        }
        else if (!mapValue.equals(other.mapValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem00C [mapValue=" + mapValue + "]";
    }

}
