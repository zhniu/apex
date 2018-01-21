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
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class TestContextItem00B.
 */
public class TestContextItem00B implements Serializable {
    private static final long serialVersionUID = 1254589722957250388L;

    private static final int HASH_PRIME_1 = 31;

    private TreeSet<String> setValue  = new TreeSet<String>();

    /**
     * The Constructor.
     */
    public TestContextItem00B() {
    }

    /**
     * The Constructor.
     *
     * @param setArray the set array
     */
    public TestContextItem00B(final String[] setArray) {
    }

    /**
     * The Constructor.
     *
     * @param setValue the set value
     */
    public TestContextItem00B(final TreeSet<String> setValue) {
        this.setValue = setValue;
    }

    /**
     * Gets the set value.
     *
     * @return the sets the value
     */
    public Set<String> getSetValue() {
        if (setValue == null) {
            setValue = new TreeSet<String>();
        }
        return setValue;
    }

    /**
     * Sets the set value.
     *
     * @param setValue the sets the value
     */
    public void setSetValue(final TreeSet<String> setValue) {
        this.setValue = setValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((setValue == null) ? 0 : setValue.hashCode());
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
        TestContextItem00B other = (TestContextItem00B) obj;
        if (setValue == null) {
            if (other.setValue != null) {
                return false;
            }
        }
        else if (!setValue.equals(other.setValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem00B [setValue=" + setValue + "]";
    }
}
