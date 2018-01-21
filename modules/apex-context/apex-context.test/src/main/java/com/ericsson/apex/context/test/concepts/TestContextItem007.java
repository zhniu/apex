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

/**
 * The Class TestContextItem007.
 */
public class TestContextItem007 implements Serializable {
    private static final long serialVersionUID = -1029406737866392421L;

    private static final int HASH_PRIME_1 = 31;

    private Long longValue = new Long(0);

    /**
     * The Constructor.
     */
    public TestContextItem007() {
    }

    /**
     * The Constructor.
     *
     * @param longValue the long value
     */
    public TestContextItem007(final Long longValue) {
        this.longValue = longValue;
    }

    /**
     * Gets the long value.
     *
     * @return the long value
     */
    public Long getLongValue() {
        return longValue;
    }

    /**
     * Sets the long value.
     *
     * @param longValue the long value
     */
    public void setLongValue(final Long longValue) {
        this.longValue = longValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((longValue == null) ? 0 : longValue.hashCode());
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
        TestContextItem007 other = (TestContextItem007) obj;
        if (longValue == null) {
            if (other.longValue != null) {
                return false;
            }
        }
        else if (!longValue.equals(other.longValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem007 [longValue=" + longValue + "]";
    }
}
